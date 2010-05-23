package org.eclipse.e4.demo.simpleide.services;

import java.io.IOException;
import java.io.InputStream;

public interface IImageProviderService {
	public String[] getImageKeys();
	public InputStream getImageData(String imageKey) throws IOException, IllegalArgumentException;
}
