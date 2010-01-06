/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Boris Bokowski, IBM Corporation - initial API and implementation
 *     Benjamin Cabe, Sierra Wireless - ongoing improvements
 *     Sebastien Moran, Sierra Wireless - bug 298291
 *******************************************************************************/
package org.eclipse.e4.ui.internal.gadgets.opensocial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.internal.gadgets.opensocial.browserfunctions.MakeXmlHttpRequest;
import org.eclipse.e4.ui.web.BrowserViewPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.PropertyDialogAction;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class OpenSocialView extends BrowserViewPart implements
		IResourceChangeListener {

	public static final String NO_HEADER_VALUE = "undefined";

	public static final String HEADER_NAME_VALUE_SEPARATOR = "#";

	public static final String HEADERS_SEPARATOR = "\n";

	public static final String GET_METHOD = "GET";

	public static final String USERPREFS = "userprefs";

	private String url;
	private String html;
	private OSGModule module;
	private IMemento memento;
	private final String[] scripts = new String[] { "util.js", "io.js",
			"misc.js", "prefs.js", "window.js" }; // util.js must always be
	// loaded first
	private Bundle bundle;
	private Action editPropertiesAction;
	private Action refreshAction;

	private IFile moduleFile;

	private BrowserFunction makeRequestBrowserFunction;

	protected String getNewWindowViewId() {
		return getSite().getId();
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		url = site.getSecondaryId();
		this.memento = memento;
		bundle = FrameworkUtil.getBundle(this.getClass());

		url = url.replace("%3A", ":");
		URI uri = URI.create(url);
		if (!uri.isAbsolute())
			return;
		IFile[] files = ResourcesPlugin.getWorkspace().getRoot()
				.findFilesForLocationURI(uri);
		if (files.length > 0) {
			moduleFile = files[0];
			moduleFile.getWorkspace().addResourceChangeListener(this,
					IResourceChangeEvent.POST_CHANGE);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		makeActions();
		contributeToActionBars();
		contributeToToolBars();
	}

	private void makeActions() {
		editPropertiesAction = new Action() {
			public void run() {
				new PropertyDialogAction(getSite(), new ISelectionProvider() {
					public void setSelection(ISelection selection) {
					}

					public void removeSelectionChangedListener(
							ISelectionChangedListener listener) {
					}

					public ISelection getSelection() {
						return new StructuredSelection(module);
					}

					public void addSelectionChangedListener(
							ISelectionChangedListener listener) {
					}
				}).createDialog().open();
				configureBrowser(browser);
			}
		};
		editPropertiesAction.setText("Module settings");
		editPropertiesAction.setToolTipText("Edit Module Settings");

		refreshAction = new Action() {
			public void run() {
				module = null;
				configureBrowser(browser);
			}
		};
		refreshAction.setText("Refresh");
		refreshAction.setDisabledImageDescriptor(ImageDescriptor
				.createFromURL(bundle
						.getEntry("/icons/full/dlcl16/refresh.gif")));
		refreshAction.setImageDescriptor(ImageDescriptor.createFromURL(bundle
				.getEntry("/icons/full/elcl16/refresh.gif")));
		refreshAction.setToolTipText("Refresh module");
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(editPropertiesAction);
		bars.getMenuManager().update(true);
	}

	private void contributeToToolBars() {
		IActionBars bars = getViewSite().getActionBars();
		bars.getToolBarManager().add(refreshAction);
		bars.getToolBarManager().update(true);
	}

	private void registerBrowserFunctions() {
		if (makeRequestBrowserFunction != null)
			makeRequestBrowserFunction.dispose();

		makeRequestBrowserFunction = new MakeXmlHttpRequest(browser);
	}

	@Override
	public void dispose() {
		if (moduleFile != null)
			moduleFile.getWorkspace().removeResourceChangeListener(this);
		if (makeRequestBrowserFunction != null)
			makeRequestBrowserFunction.dispose();
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
		registerBrowserFunctions();

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
				StringBuilder igFunctions = new StringBuilder();
				igFunctions.append("<head></head><script>\r\n");
				igFunctions.append("var gadgets = gadgets || {};\r\n");
				igFunctions.append("gadgets.Prefs = function() {\r\n");
				for (OSGUserPref userPref : module.getUserPrefs()) {
					igFunctions.append("this." + userPref.getName());
					igFunctions.append("='");
					igFunctions.append(userPref.getValue());
					igFunctions.append("';");
				}
				igFunctions.append("\r\n}\r\n\r\n");
				try {
					// inject Javascript gadgets.* functions
					for (String script : scripts) {
						// append the script to igFunctions
						InputStream scriptInputStream = bundle.getEntry(
								"js/" + script).openStream();
						BufferedReader scriptReader = new BufferedReader(
								new InputStreamReader(scriptInputStream));
						String line = null;
						while ((line = scriptReader.readLine()) != null) {
							igFunctions.append(line + "\r\n");
						}
						scriptReader.close();
					}
				} catch (IOException e) {
					// TODO log
				}
				igFunctions.append("\r\n</script>\r\n");
				html = igFunctions.toString() + content.getValue();
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
		} else {
			localUrl = "http://ig.gmodules.com/gadgets/ifr?view=home&url="
					+ "http://hosting.gmodules.com/ig/gadgets/file/104276582316790234013/test-1.xml"
					+ "&nocache=0&up_feed=http://picasaweb.google.com/data/feed/base/user/picasateam/albumid/5114659638793351217%3Fkind%3Dphoto%26alt%3Drss%26hl%3Den_US&up_title=My+Picasa+Photos&up_gallery=&up_desc=1&lang=en&country=us&.lang=en&.country=us&synd=ig&mid=110&ifpctok=-6064860744303900509&exp_split_js=1&exp_track_js=1&exp_ids=17259,300668&parent=http://www.google.com&refresh=3600&libs=core:core.io:core.iglegacy&extern_js=/extern_js/f/CgJlbhICdXMrMNIBOAEs/SH2Zv0WBdfQ.js&is_signedin=1";
		}
		if (html != null) {
			browser.setText(html);
		}
		if (localUrl != null) {
			browser.setUrl(localUrl);
		}

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

	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDelta rootDelta = event.getDelta();
		if (rootDelta.findMember(moduleFile.getFullPath()) == null)
			return;

		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					if (delta.getResource() instanceof IContainer)
						return true;

					if (moduleFile.equals(delta.getResource())) {
						if (delta.getKind() == IResourceDelta.REMOVED) {
							Display.getDefault().asyncExec(new Runnable() {
								public void run() {
									getViewSite().getPage().hideView(
											OpenSocialView.this);
								}
							});
						} else {
							module = null;
							configureBrowser(browser);
						}
					}
					return false;
				}
			});
		} catch (CoreException e) {
			// TODO log correctly
		}

	}
}
