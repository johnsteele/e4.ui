package org.eclipse.e4.ui.examples.css.nebula;

import org.eclipse.e4.ui.css.core.dom.IElementProvider;
import org.eclipse.e4.ui.css.core.engine.CSSEngine;
import org.eclipse.e4.ui.css.nebula.dom.NebulaElementProvider;
import org.eclipse.e4.ui.css.nebula.engine.CSSNebulaEngineImpl;
import org.eclipse.e4.ui.examples.css.editor.AbstractCSSSWTEditor;

/**
 * Abstract CSS SWT Editor.
 */
public abstract class AbstractCSSNebulaEditor extends AbstractCSSSWTEditor {

	public AbstractCSSNebulaEditor() {
		super("nebula", null);
	}
	
	protected CSSEngine createCSSEngine() {
		return new CSSNebulaEngineImpl(shell.getDisplay());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.core.css.examples.csseditors.AbstractCSSEditor#getNativeWidgetElementProvider()
	 */
	protected IElementProvider getNativeWidgetElementProvider() {
		return NebulaElementProvider.INSTANCE;
	}
}
