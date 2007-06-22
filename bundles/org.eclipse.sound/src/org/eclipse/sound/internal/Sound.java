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

import javax.sound.sampled.AudioInputStream;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.sound.ISound;
import org.eclipse.sound.SoundSystemException;

/**
 * @since 1.0
 */
public abstract class Sound implements ISound {

	private SoundManager manager;

	private String id;

	private IConfigurationElement element;

	/**
	 * @param manager 
	 * @param id 
	 * @param element 
	 * 
	 */
	public Sound(final SoundManager manager, final String id, final IConfigurationElement element) {
		this.manager = manager;
		this.id = id;
		this.element = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.sound.ISound#play()
	 */
	public final void play() throws SoundSystemException {
		manager.schedule(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.sound.ISound#getId()
	 */
	public final String getId() {
		return id;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.sound.ISound#getName()
	 */
	public final String getName() {
		return element.getAttribute(SoundManager.ATT_NAME);
	}

	/**
	 * 
	 * @return
	 */
	public final boolean isValid() {
		return element.isValid();
	}

	/**
	 * 
	 * @return
	 */
	protected final IConfigurationElement getConfigurationElement() {
		return element;
	}

	/**
	 * @return
	 * @throws SoundSystemException 
	 */
	public abstract AudioInputStream getInputStream()
			throws SoundSystemException;
}
