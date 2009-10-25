/*******************************************************************************
 * Copyright (c) 2009 Sierra Wireless Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Benjamin Cabe, Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.internal.gadgets.opensocial.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class ProxyServlet extends HttpServlet {

	private static final long serialVersionUID = -1674309905378465575L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = req.getQueryString();
		HttpClient httpClient = new HttpClient();
		HttpMethod httpMethod = new GetMethod(url);
		int status = httpClient.executeMethod(httpMethod);
		resp.setStatus(status);
		for (Header header : httpMethod.getResponseHeaders()) {
			resp.addHeader(header.getName(), header.getValue());
		}
		resp.getWriter().append(httpMethod.getResponseBodyAsString());
	}
}
