package org.eclipse.e4.demo.simpleide.editor.internal;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.e4.demo.simpleide.editor.IInput;

public abstract class AbstractInput implements IInput {
	private ListenerList listeners = new ListenerList();
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
}
