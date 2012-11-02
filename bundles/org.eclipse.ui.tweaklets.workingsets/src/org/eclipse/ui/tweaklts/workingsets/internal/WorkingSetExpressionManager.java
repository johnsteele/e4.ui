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

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.statushandlers.StatusManager;

import com.sun.tools.javac.resources.compiler;

public class WorkingSetExpressionManager {

	class Record {
		public Pattern pattern;
		public IAdaptable root;
	}

	private static WorkingSetExpressionManager instance;
	private Map map = new HashMap();
	private ListenerList listeners = new ListenerList();

	private WorkingSetExpressionManager() {
		restoreState();
	}

	public static WorkingSetExpressionManager getInstance() {
		if (instance == null) {
			instance = new WorkingSetExpressionManager();

		}

		return instance;
	}

	public void addListener(Object listener) {
		listeners.add(listener);
	}

	public void removeListener(Object listener) {
		listeners.remove(listener);
	}

	public void map(IWorkingSet workingSet, IAdaptable root, Pattern pattern) {
		Record r = new Record();
		r.pattern = pattern;
		r.root = root;
		map.put(workingSet.getName(), r);
		saveState();
	}

	private void restoreState() {
		String mementoString = Activator.getInstance().getPreferenceStore()
				.getString("expressionMemento");
		if (mementoString == null || "".equals(mementoString))
			return;
		StringReader mementoReader = new StringReader(mementoString);
		try {
			XMLMemento memento = XMLMemento.createReadRoot(mementoReader);
			IMemento[] workingSets = memento.getChildren("workingSet");
			for (int i = 0; i < workingSets.length; i++) {
				IMemento workingSetMemento = workingSets[i];
				String workingSetId = workingSetMemento.getID();
				if (workingSetId == null || "".equals(workingSetId)) {
					StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
									"missing working set id"));
					continue;
				}
				String patternString = workingSetMemento.getString("pattern");
				if (patternString == null || "".equals(patternString)) {
					StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
									"missing pattern"));
					continue;
				}

				Pattern pattern = null;
				try {
					pattern = Pattern.compile(patternString);
				} catch (PatternSyntaxException e) {
					StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
									"bad pattern", e));
					continue;
				}

				IMemento rootMemento = memento.getChild("root");
				if (rootMemento == null) {
					StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
									"missing root memento"));
					continue;
				}

				String factoryId = rootMemento.getString("factoryID");
				if (factoryId == null || "".equals(factoryId)) {
					StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
									"missing root memento"));
					continue;
				}

				IElementFactory factory = PlatformUI.getWorkbench()
						.getElementFactory(factoryId);
				if (factory == null) {
					StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
									"cannot create element factory: "
											+ factoryId));
					continue;
				}

				IAdaptable item = factory.createElement(rootMemento);
				if (item == null) {
					StatusManager.getManager().handle(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID,
									"cannot create element from factory: "
											+ factoryId));
					continue;
				}

				Record record = new Record();
				record.pattern = pattern;
				record.root = item;
				map.put(workingSetId, record);
			}
		} catch (WorkbenchException e) {
			StatusManager.getManager().handle(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							"problem restoring working set states", e));
		}

	}

	private void saveState() {
		XMLMemento memento = XMLMemento.createWriteRoot("map");
		for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			String workingSetId = (String) entry.getKey();
			Record record = (Record) entry.getValue();
			IPersistableElement element = (IPersistableElement) record.root
					.getAdapter(IPersistableElement.class);
			if (element == null) {
				StatusManager
						.getManager()
						.handle(
								new Status(
										IStatus.ERROR,
										Activator.PLUGIN_ID,
										"could not persist root element for dynamic resource working set.  No persistable element adapter found."));
			}

			IMemento child = memento.createChild("workingSet", workingSetId);
			child.putString("pattern", record.pattern.pattern());

			IMemento rootItem = memento.createChild("root");
			rootItem.putString("factoryID", element.getFactoryId());
			element.saveState(rootItem);

		}
		StringWriter result = new StringWriter();
		try {
			memento.save(result);
			Activator.getInstance().getPreferenceStore().setValue(
					"expressionMemento", result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Pattern getPattern(IWorkingSet workingSet) {
		Record record = (Record) map.get(workingSet.getName());
		if (record == null)
			return null;
		return record.pattern;
	}

	public IAdaptable getRoot(IWorkingSet workingSet) {
		Record record = (Record) map.get(workingSet.getName());
		if (record == null)
			return null;
		return record.root;
	}
}
