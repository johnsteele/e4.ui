package org.eclipse.e4.demo.simpleide.navigator.internal;

import java.util.Vector;

import org.eclipse.e4.demo.simpleide.navigator.IProjectService;

public class ServiceRegistryComponent {
	private Vector<IProjectService> creators = new Vector<IProjectService>();
	
	public void addCreator( IProjectService creator ) {
		creators.add(creator);
	}
	
	public void removeCreator(IProjectService creator) {
		creators.remove(creator);
	}
	
	public Vector<IProjectService> getCreators() {
		return creators;
	}
}
