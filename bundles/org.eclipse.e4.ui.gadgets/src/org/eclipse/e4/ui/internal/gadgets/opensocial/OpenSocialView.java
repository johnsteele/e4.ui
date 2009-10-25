/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.internal.gadgets.opensocial;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.internal.gadgets.opensocial.servlets.StringServlet;
import org.eclipse.e4.ui.web.BrowserViewPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

public class OpenSocialView extends BrowserViewPart {

	private static final String USERPREFS = "userprefs";

	private final class ModulePropertySourceProvider implements
			IPropertySourceProvider {
		public IPropertySource getPropertySource(final Object object) {
			return new IPropertySource() {

				private IPropertyDescriptor[] propertyDescriptor;

				public void setPropertyValue(Object id, Object value) {
					OSGModule module = (OSGModule) object;
					module.setUserPrefValue((String) id, (String) value);
					OpenSocialView.this.configureBrowser(browser);
				}

				public void resetPropertyValue(Object id) {
					OSGModule module = (OSGModule) object;
					module.setUserPrefValue((String) id, module
							.getUserPrefDefaultValue((String) id));
					OpenSocialView.this.configureBrowser(browser);
				}

				public boolean isPropertySet(Object id) {
					OSGModule module = (OSGModule) object;
					return !(module.getUserPrefValue((String) id).equals(module
							.getUserPrefDefaultValue((String) id)));
				}

				public Object getPropertyValue(Object id) {
					OSGModule module = (OSGModule) object;
					String value = module.getUserPrefValue((String) id);
					return (value == null) ? "" : value;
				}

				public IPropertyDescriptor[] getPropertyDescriptors() {
					if (propertyDescriptor == null) {
						List<IPropertyDescriptor> l = new ArrayList<IPropertyDescriptor>();

						for (OSGUserPref pref : module.getUserPrefs()) {
							TextPropertyDescriptor textPropertyDescriptor = new TextPropertyDescriptor(
									pref.getName(), pref.getName());
							l.add(textPropertyDescriptor);
						}

						propertyDescriptor = l
								.toArray(new IPropertyDescriptor[0]);
					}

					return propertyDescriptor;

				}

				public Object getEditableValue() {
					return object;
				}
			};
		}
	}

	/*
	 * Helper class needed to simulate a selection provider to communicate with
	 * the properties View
	 */
	private class SelectionProviderAdapter implements ISelectionProvider {

		List<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>();

		ISelection theSelection = StructuredSelection.EMPTY;

		public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
			listeners.add(listener);
		}

		public ISelection getSelection() {
			return theSelection;
		}

		public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
			listeners.remove(listener);
		}

		public void setSelection(ISelection selection) {
			theSelection = selection;
			final SelectionChangedEvent e = new SelectionChangedEvent(this,
					selection);
			Object[] listenersArray = listeners.toArray();

			for (int i = 0; i < listenersArray.length; i++) {
				final ISelectionChangedListener l = (ISelectionChangedListener) listenersArray[i];
				l.selectionChanged(e);
			}
		}
	}

	private String url;
	private String html;
	private SelectionProviderAdapter ss;
	private OSGModule module;
	private IMemento memento;
	private final String[] scripts = new String[] { "util.js", "io.js",
			"misc.js", "prefs.js", "window.js" }; // util.js must always be
	// loaded first
	private Bundle bundle;

	protected String getNewWindowViewId() {
		return getSite().getId();
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		url = site.getSecondaryId();
		this.memento = memento;
		ss = new SelectionProviderAdapter();
		site.setSelectionProvider(ss);
		bundle = FrameworkUtil.getBundle(this.getClass());
	}

	@Override
	public void dispose() {
		unregisterModuleProxyServlet();
		super.dispose();
	}

	@Override
	public void saveState(IMemento memento) {
		saveUserPreferences(memento);
	}

	private void saveUserPreferences(IMemento memento) {
		IMemento node = memento.createChild(USERPREFS);
		for (OSGUserPref pref : module.getUserPrefs()) {
			node.putString(pref.getName(), pref.getValue());
		}
	}

	protected void configureBrowser(Browser browser) {
		String localUrl = null;
		if (url != null) {
			url = url.replace("%3A", ":");
			final String uriForIcon = url;
			if (module == null) {
				module = OpenSocialUtil.loadModule(url);
				loadUserPreferences(module, memento);
			}
			setPartName(module.getTitle());
			setTitleToolTip(module.getDescription());
			Map<String, OSGContent> contents = module.getContents();
			OSGContent content = contents.get("home");
			if (content == null) {
				content = contents.get("default");
			}
			if (content == null) {
				content = contents.get("canvas");
			}
			if (content == null) {
				content = contents.get("");
			}
			if ("url".equalsIgnoreCase(content.getType())) {
				localUrl = content.getHref();
			} else if ("html".equalsIgnoreCase(content.getType())) {
				String igFunctions = "<head></head><script>\r\n"
						+ "var gadgets = gadgets || {};\r\n"
						+ "gadgets.Prefs = function() {\r\n";
				for (OSGUserPref userPref : module.getUserPrefs()) {
					igFunctions += "this." + userPref.getName() + "='"
							+ userPref.getValue() + "';";
				}
				igFunctions += "\r\n}\r\n\r\n";
				try {
					// inject Javascript gadgets.* functions
					for (String script : scripts) {
						igFunctions += IOUtils.toString(bundle.getEntry(
								"js/" + script).openStream());
					}
					ServiceReference httpServiceReference = bundle
							.getBundleContext().getServiceReference(
									HttpService.class.getName());
					String proxyPort = httpServiceReference.getProperty(
							"http.port").toString();
					igFunctions = igFunctions.replace("%%%PROXY_URL%%%",
							"http://localhost:" + proxyPort);
				} catch (IOException e) {
					// TODO
				}
				igFunctions += "\r\n</script>\r\n";
				html = igFunctions + content.getValue();
			}
			if (localUrl == null && html == null) {
				throw new RuntimeException("could not find Gadget URL");
			} else if (uriForIcon != null) {
				Job job = new Job("Retrieve Icon for " + module.getTitle()) {

					@Override
					protected IStatus run(IProgressMonitor monitor) {
						URI uri;
						try {
							uri = new URI(uriForIcon);
						} catch (URISyntaxException e1) {
							return Status.CANCEL_STATUS;
						}
						String host = uri.getHost();
						try {
							URL favicon = new URL("http://" + host
									+ "/favicon.ico");
							InputStream is = favicon.openConnection()
									.getInputStream();
							Display display = getSite().getWorkbenchWindow()
									.getShell().getDisplay();
							final Image icon = new Image(display, is);
							display.asyncExec(new Runnable() {

								public void run() {
									setTitleImage(icon);
								}
							});
						} catch (MalformedURLException e) {
							return Status.CANCEL_STATUS;
						} catch (IOException e) {
							return Status.CANCEL_STATUS;
						} catch (SWTException e) {
							// the Image might be malformed or not readable by
							// SWT
							return Status.CANCEL_STATUS;
						}
						return Status.OK_STATUS;
					}
				};
				job.schedule();
			}
			ss.setSelection(new StructuredSelection(module));
		} else {
			localUrl = "http://ig.gmodules.com/gadgets/ifr?view=home&url="
					+ "http://hosting.gmodules.com/ig/gadgets/file/104276582316790234013/test-1.xml"
					+ "&nocache=0&up_feed=http://picasaweb.google.com/data/feed/base/user/picasateam/albumid/5114659638793351217%3Fkind%3Dphoto%26alt%3Drss%26hl%3Den_US&up_title=My+Picasa+Photos&up_gallery=&up_desc=1&lang=en&country=us&.lang=en&.country=us&synd=ig&mid=110&ifpctok=-6064860744303900509&exp_split_js=1&exp_track_js=1&exp_ids=17259,300668&parent=http://www.google.com&refresh=3600&libs=core:core.io:core.iglegacy&extern_js=/extern_js/f/CgJlbhICdXMrMNIBOAEs/SH2Zv0WBdfQ.js&is_signedin=1";
		}
		if (html != null) {
			registerModuleProxyServlet();
			url = "http://localhost:8089/"
					+ getViewSite().getSecondaryId().hashCode();
		}

		if (url != null)
			browser.setUrl(url);
	}

	private void loadUserPreferences(OSGModule module, IMemento memento) {
		if (memento == null)
			return;

		IMemento node = memento.getChild(USERPREFS);
		if (node == null)
			return;

		for (String key : node.getAttributeKeys()) {
			for (OSGUserPref pref : module.getUserPrefs()) {
				if (pref.getName().equals(key))
					pref.setValue(node.getString(key));
			}
		}
	}

	private HttpService getHttpService() {
		ServiceReference httpServiceReference = bundle.getBundleContext()
				.getServiceReference(HttpService.class.getName());
		HttpService httpService = (HttpService) bundle.getBundleContext()
				.getService(httpServiceReference);
		return httpService;
	};

	private void registerModuleProxyServlet() {
		HttpService httpService = getHttpService();
		if (httpService != null) {
			try {
				// always unregister any servlet already mapped on the path we
				// want to use
				try {
					httpService.unregister("/"
							+ getViewSite().getSecondaryId().hashCode());
				} catch (IllegalArgumentException e) {
					// this is normal, and happens when we unregister()
					// non-existing aliases
				}
				httpService.registerServlet("/"
						+ getViewSite().getSecondaryId().hashCode(),
						new StringServlet(html), null, null);
			} catch (ServletException e) {
				// TODO log properly
				e.printStackTrace();
			} catch (NamespaceException e) {
				// TODO log properly
				e.printStackTrace();
			}
		}
	}

	private void unregisterModuleProxyServlet() {
		HttpService httpService = getHttpService();
		if (httpService != null) {
			httpService.unregister("/"
					+ getViewSite().getSecondaryId().hashCode());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySheetPage.class) {
			PropertySheetPage psp = new PropertySheetPage();
			psp.setPropertySourceProvider(new ModulePropertySourceProvider());
			return psp;
		}
		return super.getAdapter(adapter);
	}
}
