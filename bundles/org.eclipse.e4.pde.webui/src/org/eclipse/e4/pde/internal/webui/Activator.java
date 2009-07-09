/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Simon Kaegi, Boris Bokowski, IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.pde.internal.webui;

import java.util.Hashtable;

import org.eclipse.equinox.http.jetty.JettyConfigurator;
import org.eclipse.equinox.http.jetty.JettyConstants;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    private ServiceTracker httpServiceTracker;
    protected static volatile BundleContext bundleContext;
    static boolean DEBUG;
    {
    	String debug = System.getProperty("org.eclipse.e4.pde.webui.debug");
		if (debug != null && !"false".equals(debug.toLowerCase())) {
			DEBUG = true;
		}
    }

    public void start(BundleContext context) throws Exception {
    	Hashtable settings = new Hashtable();
    	
    	if (DEBUG) {
    		settings.put(JettyConstants.HTTP_HOST,"127.0.0.1");
    	} else {
    		// in non-debug mode, only accept connections from localhost:
    		settings.put(JettyConstants.HTTP_HOST,"0.0.0.0");
    	}
    	
    	settings.put(JettyConstants.HTTP_PORT,Integer.valueOf(0));
    	settings.put(JettyConstants.OTHER_INFO, "e4.pde.webui");    	
    	JettyConfigurator.startServer("e4.pde.webui", settings);
    	
    	
        bundleContext = context;
        Filter filter = context.createFilter("(&(" + Constants.OBJECTCLASS + "="	+ HttpService.class.getName() + ")(other.info=e4.pde.webui))");
        httpServiceTracker = new HttpServiceTracker(context, filter);
        httpServiceTracker.open();
    }

    public void stop(BundleContext context) throws Exception {
        httpServiceTracker.close();
        httpServiceTracker = null;
        bundleContext = null;
        JettyConfigurator.stopServer("e4.pde.webui");
    }

    public static Object PORT;

    private class HttpServiceTracker extends ServiceTracker {


		public HttpServiceTracker(BundleContext context, Filter filter) {
        	super(context, filter, null);
        }


		public Object addingService(ServiceReference reference) {
            HttpService httpService = (HttpService) super.addingService(reference); // calls context.getService(reference);
            PORT = reference.getProperty(JettyConstants.HTTP_PORT);
            if (DEBUG) {
				System.out.println("listening on: " + PORT);
            }
            if (httpService == null)
                return null;

            HttpContext httpContext = new BundleEntryHttpContext(context.getBundle());
            Bundle dojoBundle = getDojoBundle();
            HttpContext dojoHttpContext = new BundleEntryHttpContext(dojoBundle);

            try {
                httpService.registerResources("/org.dojotoolkit", "/", dojoHttpContext);
                httpService.registerResources("/", "/static", httpContext);
                httpService.registerServlet("/pde", new PDEServlet(), null, httpContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpService;
        }

        private Bundle getDojoBundle() {
            Bundle[] bundles = context.getBundles();
            for (int i = 0; i < bundles.length; i++) {
                if (bundles[i].getSymbolicName().equals("org.dojotoolkit"))
                    return bundles[i];
            }
            throw new IllegalStateException("Couldn't find the 'org.dojotoolkit' bundle.");
        }

        public void removedService(ServiceReference reference, Object service) {
            HttpService httpService = (HttpService) service;
            httpService.unregister("/org.dojotoolkit");
            httpService.unregister("/");
            super.removedService(reference, service); // calls context.ungetService(reference);
        }
    }
}
