package org.eclipse.e4.demo.simpleide.services;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public interface IImageService {
	public interface IImagePool {
		public Image getImage(String imageKey) throws CoreException;
		public Image getImageUnchecked(String imageKey);
	}

	public interface IDiposeableImagePool extends IImagePool {
		public void dispose();
	}

	public interface IPooledImage {
		public Image getImage();

		public void dipose();
	}

	public Image getImage(Display display, String imageKey)
			throws CoreException;

	public IPooledImage getPooledImage(Display display, String imageKey)
			throws CoreException;

	public IDiposeableImagePool getPool(Display display);

	/**
	 * Get an image pool which is connected to a control and gets disposed when
	 * the control it is connected to is disposed
	 * 
	 * @param control
	 *            the control it is connected to
	 * @return a new pool connected to the control
	 */
	public IImagePool getControlPool(Control control);
}