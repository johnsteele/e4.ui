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

package org.eclipse.sound;

/**
 * @since 3.1
 */
public interface ISound {

	/**
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * @return
	 */
	public String getName();
	
	public String getDescription();

	/**
	 * Plays the sound. This method returns immediately - the actual playing of
	 * the sound occurs in a background job.
	 * 
	 * @throws SoundSystemException
	 */
	public void play() throws SoundSystemException;

	/**
	 * Answers whether this sound is currently muted.
	 * 
	 * @return the mute state
	 */
	public boolean isMuted();

	/**
	 * Answers the current volume of this sound in the range [0, 1].
	 * 
	 * @return the volume
	 */
	public float getVolume();

	/**
	 * Set whether or not this sound is currently muted.
	 * 
	 * @param muted
	 *            the mute state
	 */
	public void setMuted(boolean muted);

	/**
	 * Sets the current volume of this sound. This value must be in the range
	 * [0, 1].
	 * 
	 * @param volume
	 *            the volume
	 */
	public void setVolume(float volume);
}
