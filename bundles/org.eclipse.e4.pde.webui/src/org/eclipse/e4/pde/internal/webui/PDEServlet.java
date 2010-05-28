/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.pde.internal.webui;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.*;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.ifeature.IFeature;
import org.eclipse.pde.internal.core.ifeature.IFeatureModel;
import org.osgi.framework.Version;

public class PDEServlet extends HttpServlet {

	private static final long serialVersionUID = -5263759597889185038L;

	private IWorkspace workspace;

	private static String sessionID;

	private synchronized IWorkspace getWorkspace() {
		if (workspace == null) {
			// enable auto-refresh:
			new InstanceScope().getNode(ResourcesPlugin.PI_RESOURCES)
					.putBoolean(ResourcesPlugin.PREF_AUTO_REFRESH, true);
			workspace = ResourcesPlugin.getWorkspace();
		}
		return workspace;
	}

	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!isAuthenticated(req)) {
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		String pathInfo = req.getPathInfo();
		if (pathInfo.startsWith("/site/")) {
				String wsPath = pathInfo.substring("/site".length());
				IResource resource = getWorkspace().getRoot().findMember(
						new Path(wsPath));
				if (resource.getType() != IResource.FILE) {
					resp.setStatus(405);
					return;
				}
				InputStream is = req.getInputStream(); 
				int BUFFER_SIZE = 8192;
				byte[] buffer = new byte[BUFFER_SIZE];
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				int numRead = 0;
				while ((numRead = is.read(buffer)) > 0) {
					os.write(buffer, 0, numRead);
				}
				String jsonString = new String(os.toByteArray(), "ISO-8859-1");
				Object json = JSONUtil.read(jsonString);
//				System.out.println(json);
				URI siteEcoreURI = URI.createPlatformPluginURI("/org.eclipse.e4.pde.webui/model/Site.ecore", true);
				ResourceSet resourceSet = createResourceSet(siteEcoreURI);
				EPackage ePackage = resourceSet.getPackageRegistry().getEPackage("platform:/plugin/org.eclipse.e4.pde.site.model/site.xsd");

				Resource.Factory factory = (Factory) resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().get(Resource.Factory.Registry.DEFAULT_EXTENSION);
				URI uri = URI.createPlatformResourceURI(resource.getFullPath()
						.toString(), true);
				Resource r = factory.createResource(uri);
				
				EClass documentRootClass = (EClass) ePackage.getEClassifier("DocumentRoot");
				EObject documentRoot = ePackage.getEFactoryInstance().create(documentRootClass);
				EClass siteClass = (EClass) ePackage.getEClassifier("Site");
				EObject site = ePackage.getEFactoryInstance().create(siteClass);
				fill(site, (HashMap) json, ePackage.getEFactoryInstance());
				documentRoot.eSet(documentRootClass.getEStructuralFeature("site"), site);
				r.getContents().add(documentRoot);
				
				OutputStream outputStream = resourceSet.getURIConverter().createOutputStream(uri);
				r.save(outputStream, new HashMap());
				outputStream.close();
		}
	}
	
	private void fill(EObject eObject, HashMap json, EFactory eFactory) {
		for(Iterator it = json.entrySet().iterator(); it.hasNext(); ) {
			Entry entry = (Entry) it.next();
			EStructuralFeature feature = eObject.eClass().getEStructuralFeature((String) entry.getKey());
			if (entry.getValue() instanceof Collection) {
				Collection targetCollection = (Collection) eObject.eGet(feature);
				Collection sourceCollection = (Collection) entry.getValue();
				for (Iterator it2 = sourceCollection.iterator(); it2
						.hasNext();) {
					Object element = it2.next();
					EClass elementClass = (EClass) feature.getEType();
					EObject targetElement = eFactory.create(elementClass);
					fill(targetElement, (HashMap) element, eFactory);
					targetCollection.add(targetElement);
				}
			} else if (entry.getValue() instanceof Map) {
				EClass elementClass = (EClass) feature.getEType();
				EObject targetElement = eFactory.create(elementClass);
				fill(targetElement, (HashMap) entry.getValue(), eFactory);
				eObject.eSet(feature, targetElement);
			} else if (entry.getValue() instanceof String) {
				eObject.eSet(feature, entry.getValue());
			} else if (entry.getValue() != null) {
				throw new RuntimeException("unsupported: " + entry.getValue());
			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		String pathInfo = req.getPathInfo();
		if (pathInfo.startsWith("/login")) {
			if ("e4".equals(req.getParameter("username"))) {
				if ("e4".equals(req.getParameter("password"))) {
					resp.addCookie(new Cookie("org.eclipse.e4.pde.auth", getSessionId()));
					try {
						resp.sendRedirect(req.getParameter("nextURL"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
		try {
			resp.sendRedirect("/pde/login?nextURL=" + URLEncoder.encode(req.getParameter("nextURL"), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pathInfo = req.getPathInfo();
		if (pathInfo.startsWith("/login")) {
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("text/html; charset=UTF-8");
			PrintWriter writer = resp.getWriter();
			writer.append("<html>");
			writer.append("<head>");
			writer.append("<title>Please Log In</title>");
			writer.append("</head>");
			writer.append("<body>");
			writer.append("<form method='post' action='/pde/login'>");
			writer.append("username: ");
			writer.append("<input type='text' name='username' />");
			writer.append("<br />");
			writer.append("password: ");
			writer.append("<input type='password' name='password' />");
			writer.append("<br />");
			writer.append("<input type='submit' value='Login' />");
			writer.append("<input type='hidden' name='nextURL' value='" + req.getParameter("nextURL") + "' />");
			writer.append("</form>");
			if (Activator.DEBUG) {
				writer.append("<script type='text/javascript' src='/listCookies.js'></script>");
			}
			writer.append("</body>");
			writer.append("</html>");
			return;
		}
		if (!isAuthenticated(req)) {
			resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		if (pathInfo.startsWith("/features/")) {
			IFeatureModel[] allModels = PDECore.getDefault().getFeatureModelManager().getModels();
			Arrays.sort(allModels, new Comparator() {
				public int compare(Object arg0, Object arg1) {
					IFeature f0 = ((IFeatureModel)arg0).getFeature();
					IFeature f1 = ((IFeatureModel)arg1).getFeature();
					return f0.getId().compareTo(f1.getId());
				}
			});
			List features = new ArrayList(allModels.length);
			for (int i = 0; i < allModels.length; i++) {
				IFeature feature = allModels[i].getFeature();
				Map featureJSON = new HashMap();
				featureJSON.put("feature", null);
				featureJSON.put("id", feature.getId());
				featureJSON.put("url", "features/" + feature.getId() + "_" + new Version(feature.getVersion()).toString() + ".jar");
				featureJSON.put("version", feature.getVersion());
				features.add(featureJSON);
			}
			resp.setStatus(200);
			resp.setContentType("application/json; charset=UTF-8");
			Map jsonResult = new HashMap();
			jsonResult.put("identifier", "id");
			jsonResult.put("label", "id");
			jsonResult.put("items", features);
			PrintWriter writer = resp.getWriter();
			writer.write("/*");
			writer.write(JSONUtil.write(jsonResult));
			writer.write("*/");
			return;
		} else if (pathInfo.startsWith("/site/")) {
			String wsPath = pathInfo.substring("/site".length());
			IResource resource = getWorkspace().getRoot().findMember(
					new Path(wsPath));
			if (resource.getType() != IResource.FILE) {
				resp.setStatus(405);
				return;
			}
			PrintWriter writer = resp.getWriter();
			try {
				URI siteEcoreURI = URI.createPlatformPluginURI("/org.eclipse.e4.pde.webui/model/Site.ecore", true);
				ResourceSet resourceSet = createResourceSet(siteEcoreURI);

				URI uri = URI.createPlatformResourceURI(resource.getFullPath()
						.toString(), true);
				Resource r = resourceSet.getResource(uri, true);
				EObject eRootObject = (EObject) r.getContents().get(0);
				resp.setStatus(200);
				resp.setContentType("application/json; charset=UTF-8");
				writer.write("/*");
				writer.write(EMFJSONUtil.write((EObject) eRootObject.eContents().get(0)));
				writer.write("*/");
				return;
			} catch (Exception e) {
				e.printStackTrace(writer);
				resp.setStatus(500);
				return;
			}
		}
	}

	private boolean isAuthenticated(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("org.eclipse.e4.pde.auth")) {
					if (cookies[i].getValue().equals(getSessionId())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String getSessionId() {
		if (sessionID == null) {
			sessionID = UUID.randomUUID().toString();
		}
		return sessionID;
	}

	private ResourceSet createResourceSet(URI siteEcoreURI) {
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource ecoreResource = resourceSet.getResource(siteEcoreURI, true);
		
		final BasicExtendedMetaData extendedMetaData = new BasicExtendedMetaData(new EPackageRegistryImpl(EPackage.Registry.INSTANCE));
		EPackage ecorePackage = (EPackage) ecoreResource.getContents().get(0);
		extendedMetaData.putPackage(null, ecorePackage);
		
		// Register the appropriate resource factory to handle all file extensions.
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
			(Resource.Factory.Registry.DEFAULT_EXTENSION, 
			 new ResourceFactoryImpl(){
				public Resource createResource(URI uri) {
					XMLResource result = new XMLResourceImpl(uri);
					result.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
					result.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);

					result.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

					result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
					result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

					result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
					return result;
				}
			});
		// Register the package to ensure it is available during loading.
		//
		resourceSet.getPackageRegistry().put
			(ecorePackage.getNsURI(), ecorePackage);
		return resourceSet;
	}

}