/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.internal.css.legacy.presentation;

import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultTabFolder;
import org.eclipse.ui.internal.presentations.defaultpresentation.DefaultTabItem;
import org.eclipse.ui.internal.presentations.util.AbstractTabItem;

/**
 * @since 3.5
 */
public class CSSTabFolder extends DefaultTabFolder {

	public CSSTabFolder(Composite parent, int flags, boolean allowMin,
			boolean allowMax) {
		super(parent, flags, allowMin, allowMax);
		// TODO Auto-generated constructor stub
	}
    public void updateColors() {
    	//do nothing, CSS will handle
    }
    
    public AbstractTabItem add(int index, int flags) {
    	DefaultTabItem result = (DefaultTabItem) super.add(index, flags);  
    	Widget widget = result.getWidget();

    	CSSEngine engine = getEngine(widget.getDisplay());
    	if(engine != null) {
    		engine.applyStyles(widget, true);
    	}
    	
        return result;
    }
    
	private CSSEngine getEngine(Display display) {
		//TODO post 0.9 this can be retrieved via API on SWTElement
		return (CSSEngine) display.getData("org.eclipse.e4.ui.css.core.engine");
	}


}
