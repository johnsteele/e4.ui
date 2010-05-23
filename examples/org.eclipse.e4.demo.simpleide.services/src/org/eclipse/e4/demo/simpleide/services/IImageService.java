package org.eclipse.e4.demo.simpleide.services;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

public interface IImageService {
	public interface IImagePool {
		public Image getImage(String imageKey);
		public void connect(Control control);
		public void dispose();
	}
	
	public Image getImage(String imageKey);
	
	public Image fetchPooledImage(String imageKey);
	public void returnPooledImage(Image image);
	
	public IImagePool getPool();
	public IImagePool getPool(Control control);
}