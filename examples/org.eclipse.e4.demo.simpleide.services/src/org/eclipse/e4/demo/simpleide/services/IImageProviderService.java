package org.eclipse.e4.demo.simpleide.services;

import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;

public interface IImageProviderService {
	public String[] getImageKeys();
	public InputStream getImageData(String imageKey) throws CoreException;
}
