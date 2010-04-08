package org.eclipse.e4.demo.log;

import java.util.Set;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;

public class ViewCurrentContextsHandler {
	public void execute(
			@Named(IServiceConstants.ACTIVE_CONTEXTS) @Optional Set<String> set) {
		System.out.println("Active Contexts (unordered):");
		System.out.println(set);
	}
}
