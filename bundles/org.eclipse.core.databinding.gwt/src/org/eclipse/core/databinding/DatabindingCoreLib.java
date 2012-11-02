package org.eclipse.core.databinding;

import org.eclipse.core.databinding.gwt.observable.GWTRealm;

import com.google.gwt.core.client.EntryPoint;

public class DatabindingCoreLib implements EntryPoint {
	public void onModuleLoad() {
		GWTRealm.createDefault();
	}
}
