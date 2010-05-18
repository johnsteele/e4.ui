/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.simpleide.navigator.internal;

import java.util.Vector;

import org.eclipse.e4.demo.simpleide.services.IImportResourceService;
import org.eclipse.e4.demo.simpleide.services.IProjectService;

public class ServiceRegistryComponent {
	private Vector<IProjectService> creators = new Vector<IProjectService>();
	private Vector<IImportResourceService> importServices = new Vector<IImportResourceService>();
	
	public void addProjectService( IProjectService creator ) {
		creators.add(creator);
	}
	
	public void removeProjectService(IProjectService creator) {
		creators.remove(creator);
	}
	
	public Vector<IProjectService> getCreators() {
		return creators;
	}
	
	public void addImportService(IImportResourceService importService) {
		importServices.add(importService);
	}
	
	public void removeImportService(IImportResourceService importService) {
		importServices.remove(importService);
	}
	
	public Vector<IImportResourceService> getImportServices() {
		return importServices;
	}
}
