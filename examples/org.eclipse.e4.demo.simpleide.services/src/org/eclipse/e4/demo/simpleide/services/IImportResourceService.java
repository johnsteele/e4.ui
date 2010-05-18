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
package org.eclipse.e4.demo.simpleide.services;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.swt.widgets.Shell;

public interface IImportResourceService {
	public String getCategoryName();
	public String getIconURI();
	public String getLabel();
	public void importResource(Shell shell, IWorkspace workspace, StatusReporter statusReporter, Logger logger);
}
