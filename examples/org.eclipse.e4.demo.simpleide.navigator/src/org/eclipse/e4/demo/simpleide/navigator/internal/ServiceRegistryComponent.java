package org.eclipse.e4.demo.simpleide.navigator.internal;

import java.util.Vector;

import org.eclipse.e4.demo.simpleide.navigator.IProjectCreator;

public class ServiceRegistryComponent {
	private Vector<IProjectCreator> creators = new Vector<IProjectCreator>();
	
	public void addCreator( IProjectCreator creator ) {
		creators.add(creator);
	}
	
	public void removeCreator(IProjectCreator creator) {
		creators.remove(creator);
	}
	
	public Vector<IProjectCreator> getCreators() {
		return creators;
	}
}
