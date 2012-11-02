/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Remy Chi Jian Suen <remy.suen@gmail.com> - bug 201502
 *******************************************************************************/

package org.eclipse.sound.internal;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.sound.Activator;

/**
 * @since 1.0
 */
public final class SoundJob extends Job {

	private AudioInputStream stream;

	private AudioFormat format;

	private float volume;

	private boolean muted;

	/**
	 * @param name
	 * @param format
	 * @param stream
	 * @param volume
	 * @param muted
	 */
	public SoundJob(final String name, final AudioInputStream stream,
			final AudioFormat format, boolean muted, float volume) {
		super(name);
		setSystem(true);
		setPriority(Job.INTERACTIVE);
		this.stream = stream;
		this.format = format;
		this.muted = muted;
		this.volume = Math.min(1, Math.abs(volume)); // doublecheck
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.jobs.InternalJob#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IStatus run(final IProgressMonitor monitor) {
		SourceDataLine line = null;
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					format, AudioSystem.NOT_SPECIFIED);

			try {
				line = (SourceDataLine) AudioSystem.getLine(info);
			} catch (IllegalArgumentException e) {
				// ignore illegal argument, security, and line unavailability
				// exceptions, if we can't access it for whatever reasons (sound
				// card may be in use, etc.), we'll just not play any sounds
				return Status.OK_STATUS;
			} catch (SecurityException e) {
				return Status.OK_STATUS;
			} catch (LineUnavailableException e) {
				return Status.OK_STATUS;
			}

			line.open(format, AudioSystem.NOT_SPECIFIED);
			if (line.isControlSupported(BooleanControl.Type.MUTE)) {
				BooleanControl muteControl = (BooleanControl) line
						.getControl(BooleanControl.Type.MUTE);
				muteControl.setValue(muted);
			}
			if (line.isControlSupported(FloatControl.Type.VOLUME)) {
				FloatControl volumeControl = (FloatControl) line
						.getControl(FloatControl.Type.VOLUME);
				volumeControl.setValue(volume);
			}

		} catch (Exception e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0,
					Messages.SoundJob_0 + format, e);
		}

		line.start();
		IStatus status = Status.OK_STATUS;
		try {
			int numBytes = 0;
			byte[] data = new byte[64000];
			while (numBytes != -1) {
				numBytes = stream.read(data, 0, data.length);

				if (numBytes >= 0) {
					line.write(data, 0, numBytes);
				}
			}
		} catch (Exception e) {
			status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0,
					Messages.SoundJob_1, e);
		} finally {
			line.drain();
			line.close();
		}
		return status;
	}
}
