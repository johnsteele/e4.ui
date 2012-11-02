package org.eclipse.core.databinding.gwt.observable;

import org.eclipse.core.databinding.observable.Realm;

public class GWTRealm extends Realm {
	public static void createDefault() {
		setDefault(new GWTRealm());
	}

	public boolean isCurrent() {
		return true;
	}

}
