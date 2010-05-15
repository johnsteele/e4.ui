package org.eclipse.e4.demo.simpleide.editor;

import org.eclipse.core.runtime.IStatus;

public interface IInput {
	public static final String DIRTY = IInput.class.getName() + ".dirty";
	
	public interface Listener {
		public void propertyChanged(String property, Object oldValue, Object newValue);
	}
	
	public void addListener(Listener listener);
	public void removeListener(Listener listener);
	
	public IStatus save();
}
