/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.sound.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.sound.ISound;
import org.eclipse.sound.ISoundManager;
import org.eclipse.sound.Activator;
import org.eclipse.sound.SoundSystemException;

/**
 * @since 1.0
 */
public final class SoundManager implements ISoundManager {

	static final String ATT_ID = "id"; //$NON-NLS-1$

	static final String ATT_NAME = "name"; //$NON-NLS-1$

	static final String ATT_FILE = "file"; //$NON-NLS-1$

	private final Map sounds = new HashMap();

	private static SoundManager instance;

	/**
	 * 
	 * @return
	 */
	public static synchronized SoundManager getInstance() {
		if (instance == null) {
			instance = new SoundManager();
		}
		return instance;
	}

	/**
	 * 
	 */
	private SoundManager() {
		// no-op
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.sound.ISoundManager#getSound(java.lang.String)
	 */
	public ISound getSound(final String id) {
		Sound sound = (Sound) sounds.get(id);
		if (sound == null) {
			sound = findSound(id);
			if (sound != null) {
				sounds.put(id, sound);
			}
		}
		if (sound != null && !sound.isValid()) {
			sounds.remove(sound.getId());
			return null;
		}
		return sound;
	}

	public ISound[] getSounds() {
		Set sounds = new HashSet();
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(Activator.PLUGIN_ID, Activator.PL_SOUND);
		IExtension[] extensions = point.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = extensions[i]
					.getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				Sound sound = makeSound(elements[j]);
				if (sound != null)
					sounds.add(sound);
			}
		}
		return (ISound[]) sounds.toArray(new ISound[sounds.size()]);
	}

	/**
	 * @param configurationElement
	 * @return
	 */
	private Sound makeSound(IConfigurationElement configurationElement) {
		String targetId = configurationElement.getAttribute(ATT_ID);
		if (targetId == null)
			return null;
		return new URLSound(this, targetId, configurationElement);	
	}

	/**
	 * @param id
	 * @return
	 */
	private Sound findSound(final String id) {
		IExtensionPoint point = Platform.getExtensionRegistry()
				.getExtensionPoint(Activator.PLUGIN_ID, Activator.PL_SOUND);
		IExtension[] extensions = point.getExtensions();
		for (int i = 0; i < extensions.length; i++) {
			IConfigurationElement[] elements = extensions[i]
					.getConfigurationElements();
			for (int j = 0; j < elements.length; j++) {
				Sound sound = findSound(id, elements[j]);
				if (sound != null)
					return sound;
			}
		}
		return null;
	}

	/**
	 * @param id
	 * @param element
	 * @return
	 */
	private Sound findSound(final String id, final IConfigurationElement element) {
		String targetId = element.getAttribute(ATT_ID);
		if (id.equals(targetId)) {
			return new URLSound(this, targetId, element);
		}
		return null;
	}

	/**
	 * 
	 * @param sound
	 * @throws SoundSystemException
	 */
	void schedule(final Sound sound) throws SoundSystemException {
		AudioInputStream stream = sound.getInputStream();
		AudioFormat format = stream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format,
				AudioSystem.NOT_SPECIFIED);
		boolean isSupported = AudioSystem.isLineSupported(info);
		if (!isSupported) {
			AudioFormat sourceFormat = format;
			AudioFormat targetFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED, sourceFormat
							.getSampleRate(), 16, sourceFormat.getChannels(),
					sourceFormat.getChannels() * (16 / 8), sourceFormat
							.getSampleRate(), true);
			stream = AudioSystem.getAudioInputStream(targetFormat, stream);
			format = stream.getFormat();
		}
		SoundJob job = new SoundJob(sound.getName(), stream, format);
		job.schedule();
	}
}
