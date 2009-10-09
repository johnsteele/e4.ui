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
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.ui.web.BrowserViewPart;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;

public class OpenSocialView extends BrowserViewPart {

	private String url;
	private String html;

	protected String getNewWindowViewId() {
		return getSite().getId();
	}

	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		url = site.getSecondaryId();
	}

	protected void configureBrowser(Browser browser) {
		if (url != null) {
			url = url.replace("%3A",":");
			final String uriForIcon = url;
			OSGModule module = OpenSocialUtil.loadModule(url);
			setPartName(module.getTitle());
			setTitleToolTip(module.getDescription());
			url = null;
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
				url = content.getHref();
			} else if ("html".equalsIgnoreCase(content.getType())) {
				String igFunctions = "<head></head><script>\r\n" + 
						"function _IG_Prefs() {\r\n";
				for(OSGUserPref userPref : module.getUserPrefs()) {
					igFunctions += "this." + userPref.getName() + "='" + userPref.getDefaultValue() +"';";
				}
				igFunctions += 
						"}\r\n" + 
						"_IG_Prefs.prototype.set = function (key,value) {\r\n" + 
						"  this[key] = value;\r\n" + 
						"};\r\n" + 
						"_IG_Prefs.prototype.getString = function (key) {\r\n" + 
						"  return this[key] || \"\";\r\n" + 
						"};\r\n" + 
						"_IG_Prefs.prototype.getBool = function (key) {\r\n" + 
						"  return this[key] || true;\r\n" + 
						"};\r\n" + 
						"_IG_Prefs.prototype.getInt = function (key) {\r\n" + 
						"  return 0+this[key] || 0;\r\n" + 
						"};\r\n" + 
						"_IG_Prefs.prototype.getMsg = function (key) {\r\n" + 
						"  return key;\r\n" + 
						"};\r\n" + 
						"_IG_Prefs.prototype.getArray = function (key) {\r\n" + 
						"  return [];\r\n" + 
						"};\r\n" + 
						"function _IG_LoadLibraryDeferred(url, callback) {\r\n" + 
						"  var scriptElement = document.createElement(\"script\");\r\n" + 
						"  scriptElement.src=url;\r\n" + 
						"  document.getElementsByTagName(\"head\")[0].appendChild(scriptElement);\r\n" + 
						"  setTimeout(function(){eval(callback)}, 1000);\r\n" + 
						"}\r\n" + 
						"function _gel(id) {\r\n" + 
						"  if(document.all) {\r\n" + 
						"    return document.all[id];\r\n" + 
						"  } else {\r\n" + 
						"    return document.getElementById(id);\r\n" + 
						"  }\r\n" + 
						"}\r\n" + 
						"function _IG_AddDOMEventHandler(domObject,event,callback){\r\n" + 
						"  if (domObject.addEventListener){\r\n" + 
						"    domObject.addEventListener(event, callback, true);}\r\n" + 
						"  else{\r\n" + 
						"    domObject.attachEvent('on'+event,callback);\r\n" + 
						"  }\r\n" + 
						"}\r\n" +
						"function _IG_AdjustIFrameHeight() {\r\n" + 
						"}\r\n" + 
						"function _IG_Analytics() {\r\n" + 
						"}\r\n" + 
						"function _IG_RegisterOnloadHandler(handler) {\r\n" + 
						"	_IG_AddDOMEventHandler(window, \"load\", function(){handler();});\r\n" + 
						"}\r\n" + 
						"function _IG_RegisterMaximizeHandler(handler) {\r\n" + 
						"}\r\n" + 
						"" + 
						"</script>\r\n" + 
						"";
				html = igFunctions + content.getValue();
			}
			if (url == null && html == null) {
				throw new RuntimeException("could not find Gadget URL");
			} else if (uriForIcon != null){
				Job job = new Job("Retrieve Icon for "
						+ module.getTitle()) {

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
						}
						return Status.OK_STATUS;
					}
				};
				job.schedule();
			}
		} else {
			url = "http://ig.gmodules.com/gadgets/ifr?view=home&url="
					+ "http://hosting.gmodules.com/ig/gadgets/file/104276582316790234013/test-1.xml"
					+ "&nocache=0&up_feed=http://picasaweb.google.com/data/feed/base/user/picasateam/albumid/5114659638793351217%3Fkind%3Dphoto%26alt%3Drss%26hl%3Den_US&up_title=My+Picasa+Photos&up_gallery=&up_desc=1&lang=en&country=us&.lang=en&.country=us&synd=ig&mid=110&ifpctok=-6064860744303900509&exp_split_js=1&exp_track_js=1&exp_ids=17259,300668&parent=http://www.google.com&refresh=3600&libs=core:core.io:core.iglegacy&extern_js=/extern_js/f/CgJlbhICdXMrMNIBOAEs/SH2Zv0WBdfQ.js&is_signedin=1";
		}
		if (html != null) {
			browser.setText(html);
		} else if (url != null) {
			browser.setUrl(url);
		}
	};

}
