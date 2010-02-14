/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.e4photo.flickr.service.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.demo.e4photo.flickr.service.FlickrPhoto;
import org.eclipse.e4.demo.e4photo.flickr.service.FlickrSearch;
import org.eclipse.e4.demo.e4photo.flickr.service.IFlickrService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Custom implementation to access flickr through its rest API
 */
public class RestFlickrService implements IFlickrService {
	
	public RestFlickrService() {
//		System.err.println("Service created");
	}
	
	public InputStream getPhoto(FlickrPhoto photo) throws RemoteException {
		try {
			String path = "http://farm" + photo.getFarm() + ".static.flickr.com/" + photo.getServer() + "/"+ photo.getId() + "_" + photo.getSecret() + ".jpg";
			URL url = new URL(path);
			return url.openStream();
		} catch (MalformedURLException e) {
			throw new RemoteException("Failed to fetch Flickr-Image.", e);
		} catch (IOException e) {
			throw new RemoteException("Failed to fetch Flickr-Image.", e);
		}
	}
	
	public FlickrSearch createTagSearch(String apiKey, String tags) throws RemoteException {
		JSONObject obj = searchByTagsRequest("flickr.photos.search", apiKey, tags, 1);
		if( obj != null ) {
			try {
				JSONObject o = obj.getJSONObject("photos");
				int pages = Integer.parseInt(o.getString("pages"));
				int pageSize = Integer.parseInt(o.getString("perpage"));
				int total = Integer.parseInt(o.getString("total"));
				return new RestFlickrTagSearch( apiKey, pages, pageSize, total, tags);
			} catch (JSONException e) {
				throw new RemoteException("Failure while parsing response", e);
			}	
		}
		
		return null;
	}

	public List<FlickrPhoto> getPhotos(FlickrSearch search, int page) throws RemoteException {
		if( search instanceof RestFlickrTagSearch ) {
			RestFlickrTagSearch tmp = (RestFlickrTagSearch) search;
			JSONObject root = searchByTagsRequest("flickr.photos.search", tmp.getApiKey(), tmp.getTags(), page);
			if( root != null ) {
				try {
					JSONArray list = root.getJSONObject("photos").getJSONArray("photo");
					ArrayList<FlickrPhoto> rv = new ArrayList<FlickrPhoto>();
					
					for( int i = 0; i < list.length(); i++ ) {
						JSONObject o = list.getJSONObject(i);
						FlickrPhoto photo = new FlickrPhoto();
						photo.setFamily(o.getInt("isfamily") != 0);
						photo.setFarm(o.getInt("farm"));
						photo.setFriend(o.getInt("isfriend") != 0);
						photo.setId(o.getString("id"));
						photo.setOwner(o.getString("owner"));
						photo.setPublic(o.getInt("ispublic") != 0);
						photo.setSecret(o.getString("secret"));
						photo.setServer(o.getString("server"));
						photo.setTitle(o.getString("title"));
						rv.add(photo);
					}
					return rv;
				} catch (JSONException e) {
					throw new RemoteException("Failure while parsing response", e);
				}	
			}
		}
		
		throw new IllegalArgumentException("The search type '"+search.getClass().getName()+"' is not supported.");
	}

	private JSONObject searchByTagsRequest(String method, String apiKey, String tags, int page) throws RemoteException {
		String request = "http://api.flickr.com/services/rest/";
		request += "?tags=" + tags;
		request += "&method=flickr.photos.search";
		request += "&api_key="+apiKey;
		request += "&format=json";
		request += "&page=" + page;
		
		try {
			URL url = new URL(request);
			InputStream in = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			StringBuilder b = new StringBuilder();
			while( (line = reader.readLine()) != null ) {
				b.append(line);
			}
			
			String result = b.toString().substring("jsonFlickrApi(".length(), b.toString().length() - 1);
//			System.err.println(result);
			JSONObject o = new JSONObject(result);
			if( ! "ok".equals(o.getString("stat")) ) {
				throw new RemoteException(o.getString("message"));
			}
			return o;
		} catch (RemoteException e) {
			throw e;
		} catch (MalformedURLException e) {
			throw new RemoteException("Failure fetching page '"+page+"'",e); 
		} catch (IOException e) {
			throw new RemoteException("Failure fetching page '"+page+"'",e);
		} catch (JSONException e) {
			throw new RemoteException("Failure fetching page '"+page+"'",e);
		}
	}
	
	public static void main(String[] args) {
		try {
			RestFlickrService s = new RestFlickrService();
			FlickrSearch search = s.createTagSearch("46d3d5269fe6513602b3f0f06d9e2b2e", "eclipsecon");
			
			for( int page = 1; page <= search.getPages(); page++ ) {
				System.err.println("--------------------------------");
				System.err.println("Page " + page);
				System.err.println("--------------------------------");
				List<FlickrPhoto> photos = s.getPhotos(search, page);
				for( FlickrPhoto p : photos ) {
					System.err.println("	* " + p);
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
