package org.eclipse.e4.demo.simpleide.services;

import org.eclipse.swt.graphics.Image;

public interface IImageService {
	public Image getImage(String image);
	
	public Image fetchPooledImage(String image);
	public void returnPooledImage(Image image);
}