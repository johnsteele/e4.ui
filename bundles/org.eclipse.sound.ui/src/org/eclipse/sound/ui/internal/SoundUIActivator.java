package org.eclipse.sound.ui.internal;

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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.sound.Activator;
import org.eclipse.sound.ISound;
import org.eclipse.sound.SoundSystemException;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @since 1.0
 * 
 */
public class SoundUIActivator extends AbstractUIPlugin implements IStartup {

	private static String PLUGIN_ID = "org.eclipse.sound.ui"; //$NON-NLS-1$
	private int lastMarkerCount = 0;
	private IResourceChangeListener resourceChangeListener = new IResourceChangeListener() {

		public void resourceChanged(IResourceChangeEvent event) {
			try {
				int newMarkerCount = 0;
				IMarker[] markers = ResourcesPlugin.getWorkspace().getRoot()
						.findMarkers(IMarker.PROBLEM, true,
								IResource.DEPTH_INFINITE);

				newMarkerCount = countErrors(markers, IMarker.SEVERITY_ERROR);

				if (newMarkerCount != lastMarkerCount) {
					if (newMarkerCount == 0) {
						playSound("org.eclipse.sound.ui.noErrors"); //$NON-NLS-1$
					} else {
						if (newMarkerCount > lastMarkerCount) {
							playSound("org.eclipse.sound.ui.newErrors"); //$NON-NLS-1$
						}
					}
					lastMarkerCount = newMarkerCount;
				}
			} catch (CoreException e) {
				getLog()
						.log(
								new Status(IStatus.ERROR, PLUGIN_ID, e
										.getMessage(), e));
			}
		}

		private void playSound(String id) {
			ISound sound = Activator.getSoundManager().getSound(id);
			if (sound == null)
				return;
			try {
				sound.play();
			} catch (SoundSystemException e) {
				getLog()
						.log(
								new Status(IStatus.ERROR, PLUGIN_ID, e
										.getMessage(), e));
			}
		}
	};

	/**
	 * 
	 */
	public SoundUIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				resourceChangeListener);
		IMarker[] markers = ResourcesPlugin.getWorkspace().getRoot().findMarkers(
				IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		lastMarkerCount = countErrors(markers, IMarker.SEVERITY_ERROR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);

		ResourcesPlugin.getWorkspace().removeResourceChangeListener(
				resourceChangeListener);
	}

	public void earlyStartup() {

	}
	
	/**
	 * Cound the number of markers that have the given severity.
	 * 
	 * @param markers
	 * @param severity
	 * @return
	 * @throws CoreException
	 */
	private int countErrors(final IMarker[] markers, int severity) throws CoreException {
		int newMarkerCount = 0;
		for (int i = 0; i < markers.length; i++) {
			IMarker marker = markers[i];
			Object o = marker.getAttribute(IMarker.SEVERITY);
			if (o instanceof Integer
					&& ((Integer) o).intValue() == severity)
				newMarkerCount++;
		}
		return newMarkerCount;
	}

}
