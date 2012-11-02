/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.sound.internal;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.sound.ISound;
import org.eclipse.sound.SoundSystemException;

/**
 * @since 1.0
 */
public final class URLSound extends Sound implements ISound {
	
	/**
	 * @param manager
	 * @param id
	 * @param element
	 */
	public URLSound(final SoundManager manager, final String id,
			final IConfigurationElement element) {
		super(manager, id, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.internal.sound.Sound#getInputStream()
	 */
	public AudioInputStream getInputStream() throws SoundSystemException {
		try {
			return AudioSystem.getAudioInputStream(getURL());
		} catch (Exception e) {
			throw new SoundSystemException(e);
		}
	}

	/**
	 * @return
	 */
	private URL getURL() {
		return FileLocator.find(Platform.getBundle(getConfigurationElement()
				.getContributor().getName()), new Path(getConfigurationElement()
				.getAttribute(SoundManager.ATT_FILE)), null);
	}
}
