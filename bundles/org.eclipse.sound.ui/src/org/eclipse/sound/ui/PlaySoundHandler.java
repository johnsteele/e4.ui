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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.sound.ISound;
import org.eclipse.sound.Activator;
import org.eclipse.sound.SoundSystemException;

/**
 * @since 1.0
 *
 */
public class PlaySoundHandler extends AbstractHandler {

	/**
	 * 
	 */
	public static final String COMMAND_ID = "org.eclipse.sound.playSound"; //$NON-NLS-1$
	/**
	 * 
	 */
	public static final String ID = "org.eclipse.sound.id"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String id = event.getParameter(ID);
		if (id == null)
			return null; // TODO : exception?
		ISound sound = Activator.getSoundManager().getSound(id);
		if (sound == null)
			return null; // TODO : exception?

		try {
			sound.play();
		} catch (SoundSystemException e) {
			throw new ExecutionException(e.getMessage(), e);
		}

		return null;
	}

}
