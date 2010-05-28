/*******************************************************************************
 * Copyright (c) 2008, 2009 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *     IBM Corporation - ongoing development
 *******************************************************************************/
package org.eclipse.e4.ui.css.nebula.engine;

import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyBackgroundHandler;
import org.eclipse.e4.ui.css.core.dom.properties.css2.ICSSPropertyTextHandler;
import org.eclipse.e4.ui.css.nebula.dom.NebulaElementProvider;
import org.eclipse.e4.ui.css.nebula.properties.css2.CSSPropertyBackgroundNebulaHandler;
import org.eclipse.e4.ui.css.nebula.properties.css2.CSSPropertyTextNebulaHandler;
import org.eclipse.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.eclipse.swt.widgets.Display;

public class CSSNebulaEngineImpl extends CSSSWTEngineImpl {

	public CSSNebulaEngineImpl(Display display) {
		super(display);
		this.initializeNebulaConfig();
	}

	public CSSNebulaEngineImpl(Display display, boolean lazyApplyingStyles) {
		super(display, lazyApplyingStyles);
		this.initializeNebulaConfig();
	}

	protected void initializeNebulaConfig() {
		// Register Nebula Element Provider to retrieve
		// w3c Element NebulaElement which wrap SWT/Nebula widget.
		super.setElementProvider(NebulaElementProvider.INSTANCE);
	}

	protected void initializeCSSPropertyHandlers() {
		// Register Nebula CSS Property Background Handler
		super.registerCSSPropertyHandler(ICSSPropertyBackgroundHandler.class,
				CSSPropertyBackgroundNebulaHandler.INSTANCE);
		// Register Nebula CSS Property Text Handler
		super.registerCSSPropertyHandler(ICSSPropertyTextHandler.class,
				CSSPropertyTextNebulaHandler.INSTANCE);
		super.initializeCSSPropertyHandlers();		
	}

}
