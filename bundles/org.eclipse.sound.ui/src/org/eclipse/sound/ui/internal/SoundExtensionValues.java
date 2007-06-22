/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.sound.ui.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;
import org.eclipse.sound.ISound;
import org.eclipse.sound.Activator;

/**
 * @since 1.0
 *
 */
public class SoundExtensionValues implements IParameterValues {

	public Map getParameterValues() {
		ISound [] sounds = Activator.getSoundManager().getSounds();
		Map soundMap = new HashMap();
		for (int i = 0; i < sounds.length; i++) {
			ISound sound = sounds[i];
			soundMap.put(sound.getName(), sound.getId());
		}
		return soundMap;
	}

}
