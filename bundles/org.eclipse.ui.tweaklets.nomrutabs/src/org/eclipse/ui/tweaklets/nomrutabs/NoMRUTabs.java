package org.eclipse.ui.tweaklets.nomrutabs;

import org.eclipse.ui.internal.tweaklets.TabBehaviourMRU;

public class NoMRUTabs extends TabBehaviourMRU {

	public boolean enableMRUTabVisibility() {
		return false;
	}

}
