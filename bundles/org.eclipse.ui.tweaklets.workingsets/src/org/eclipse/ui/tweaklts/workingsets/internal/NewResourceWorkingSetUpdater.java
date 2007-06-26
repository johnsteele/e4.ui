/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.tweaklts.workingsets.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetUpdater;

public class NewResourceWorkingSetUpdater implements IWorkingSetUpdater {

	private Set workingSets = new HashSet();

	public NewResourceWorkingSetUpdater() {
		WorkingSetExpressionManager.getInstance().addListener(this);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				new IResourceChangeListener() {

					public void resourceChanged(IResourceChangeEvent event) {
						for (Iterator i = workingSets.iterator(); i.hasNext();) {
							IWorkingSet workingSet = (IWorkingSet) i.next();
							// TODO work in root info

							final Set content = new HashSet(Arrays
									.asList(workingSet.getElements()));
							Pattern pattern = WorkingSetExpressionManager
									.getInstance().getPattern(workingSet);
							final Matcher matcher = pattern.matcher("");
							try {

								event.getDelta().accept(
										new IResourceDeltaVisitor() {

											public boolean visit(
													IResourceDelta delta)
													throws CoreException {

												if (delta.getResource() instanceof IContainer)
													return true;

												matcher.reset(delta
														.getResource()
														.getFullPath()
														.toString());
												if (matcher.matches()) {
													if (delta.getKind() == IResourceDelta.ADDED) {
														content.add(delta
																.getResource());
													} else if (delta.getKind() == IResourceDelta.REMOVED) {
														content.remove(delta
																.getResource());
													}
												}

												return false;
											}
										});

							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							workingSet.setElements((IAdaptable []) content.toArray(new IAdaptable [content
									.size()]));
						}

					}
				}, IResourceChangeEvent.POST_CHANGE);
	}

	public void add(IWorkingSet workingSet) {
		workingSets.add(workingSet);
		IAdaptable root = WorkingSetExpressionManager.getInstance().getRoot(
				workingSet);
		Pattern pattern = WorkingSetExpressionManager.getInstance().getPattern(
				workingSet);
		Matcher matcher = pattern.matcher("");
		Set contents = new HashSet();
		if (root instanceof IWorkingSet) {
			walkWorkingSet(contents, (IWorkingSet) root, matcher);
		} else if (root instanceof IContainer) {
			walkContainer(contents, (IContainer) root, matcher);
		}
		workingSet.setElements((IAdaptable[]) contents
				.toArray(new IAdaptable[contents.size()]));
	}

	private void walkContainer(final Set contents, IContainer root,
			final Matcher matcher) {
		try {
			root.accept(new IResourceVisitor() {

				public boolean visit(IResource resource) throws CoreException {
					if (resource instanceof IContainer)
						return true;

					matcher.reset(resource.getFullPath().toString());
					if (matcher.matches()) {
						contents.add(resource);
					}
					return true;
				}
			});
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void walkWorkingSet(Set contents, IWorkingSet root, Matcher matcher) {
		// TODO Auto-generated method stub

	}

	public boolean contains(IWorkingSet workingSet) {
		return workingSets.contains(workingSet);
	}

	public void dispose() {
		WorkingSetExpressionManager.getInstance().removeListener(this);
	}

	public boolean remove(IWorkingSet workingSet) {
		return workingSets.remove(workingSet);
	}
}
