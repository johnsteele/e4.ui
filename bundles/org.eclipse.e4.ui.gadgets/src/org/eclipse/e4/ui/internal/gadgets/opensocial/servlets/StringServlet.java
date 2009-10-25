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

/**
 * A very simple (and temporary?) implementation of a servlet which answers to
 * GET requests with a String it holds
 * 
 */
public class StringServlet extends HttpServlet {
	private static final long serialVersionUID = -1774350465719944953L;
	private String content;

	public StringServlet(String content) {
		this.content = content;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().append(content);
	}
}
