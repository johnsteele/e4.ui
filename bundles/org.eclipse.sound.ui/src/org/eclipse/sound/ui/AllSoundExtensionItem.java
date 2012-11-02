/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.sound.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.sound.ISound;
import org.eclipse.sound.Activator;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;

/**
 * @since 1.0
 * 
 */
public class AllSoundExtensionItem extends CompoundContributionItem {

	/**
	 * 
	 */
	public AllSoundExtensionItem() {
	}

	/**
	 * @param id
	 */
	public AllSoundExtensionItem(String id) {
		super(id);
	}

	protected IContributionItem[] getContributionItems() {
		List contributions = new ArrayList();
		ISound[] sounds = Activator.getSoundManager().getSounds();
		for (int i = 0; i < sounds.length; i++) {
			Map map = Collections.singletonMap(PlaySoundHandler.ID, sounds[i]
					.getId());
			contributions.add(new CommandContributionItem(PlatformUI
					.getWorkbench(), sounds[i].getId(),
					PlaySoundHandler.COMMAND_ID, map, null, null, null,
					sounds[i].getName(), null, null,
					CommandContributionItem.STYLE_PUSH));
		}

		return (IContributionItem[]) contributions
				.toArray(new IContributionItem[contributions.size()]);
	}

}
