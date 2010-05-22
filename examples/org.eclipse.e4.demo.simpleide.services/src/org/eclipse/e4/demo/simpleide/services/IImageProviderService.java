package org.eclipse.e4.demo.simpleide.services;

import java.util.List;

public interface IImageProviderService {
	public List<String> getImageKeys();
	public String getImageUri(String imageKey);
}
