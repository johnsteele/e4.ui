/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.demo.viewer;

import java.util.Iterator;

import org.eclipse.e4.core.services.annotations.In;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class TabAdvanced {
	static private final String[] booleanValues = new String[] { "true", "false" };
	
	private TableViewer tableViewer;

	private class EObjectFeature {
		public EObject eObj;
		public EStructuralFeature feature;

		public EObjectFeature(EObject eObj, EStructuralFeature feature) {
			this.eObj = eObj;
			this.feature = feature;
		}
	}

	private class PropertiesEditingSupport extends EditingSupport {

		private CellEditor textEditor;
		private ComboBoxCellEditor booleanEditor;

		public PropertiesEditingSupport(ColumnViewer viewer) {
			super(viewer);
			textEditor = new TextCellEditor(((TableViewer) viewer).getTable());
			booleanEditor = new ComboBoxCellEditor(((TableViewer) viewer)
					.getTable(), booleanValues, SWT.READ_ONLY) {
				@Override
				public boolean isActivated() {
					return true;
				}
			};
		}

		@Override
		protected boolean canEdit(Object element) {
			Class<?> clazz = ((EObjectFeature) element).feature.getEType().getInstanceClass();
			String clsStr = clazz.getName();
			if (clsStr.equals("java.lang.String"))
				return true;
			if (clsStr.equals("boolean"))
				return true;
			return false;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			if (isBoolean((EObjectFeature) element))
				return booleanEditor;
			else
				return textEditor;
		}

		@Override
		protected Object getValue(Object element) {
			System.out.println("getValue");
			Object result = getPropertyValue((EObjectFeature) element);
			if (isBoolean((EObjectFeature) element)) {
				if (result.equals("true"))
					return new Integer(0);
				else
					return new Integer(1);
			}
			return result;
		}

		@Override
		protected void setValue(Object element, Object value) {
			System.out.println("setValue " + value);
			EObjectFeature dataObject = (EObjectFeature) element;
			if (isBoolean(dataObject)) {
				// NOTE: Workaround for the order of events:
				// combo box first closes, then updates its selection 
				CCombo combo = (CCombo) booleanEditor.getControl();
				int selection = combo.getSelectionIndex();
				value = new Boolean(selection == 0);
			}
			dataObject.eObj.eSet(dataObject.feature, value);
			// this is probably not the most efficient way, but it works:
			tableViewer.refresh(true);
		}
	}

	public TabAdvanced(final Composite parent) {
		Table table = new Table(parent, SWT.NONE | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
		tableViewer = new TableViewer(table);
		TableViewerColumn columnName = new TableViewerColumn(tableViewer,
				SWT.NONE);
		columnName.getColumn().setText("Property");
		columnName.getColumn().setResizable(true);

		TableViewerColumn columnValue = new TableViewerColumn(tableViewer,
				SWT.NONE);
		columnValue.getColumn().setText("Value");
		columnValue.getColumn().setResizable(true);

		columnValue
				.setEditingSupport(new PropertiesEditingSupport(tableViewer));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableLayout layoutLayout = new TableLayout();
		layoutLayout.addColumnData(new ColumnWeightData(1));
		layoutLayout.addColumnData(new ColumnWeightData(2));
		table.setLayout(layoutLayout);

		tableViewer.setContentProvider(new IStructuredContentProvider() {

			private EObject eObj;

			public void dispose() {
				// TODO Auto-generated method stub
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				eObj = (EObject) newInput;
			}

			public Object[] getElements(Object inputElement) {
				EList<EStructuralFeature> features = eObj.eClass()
						.getEAllStructuralFeatures();
				EObjectFeature[] result = new EObjectFeature[features.size()];
				int pos = 0;
				for (Iterator<?> iterator = features.iterator(); iterator
						.hasNext();) {
					EStructuralFeature feature = (EStructuralFeature) iterator
							.next();
					result[pos] = new EObjectFeature(eObj, feature);
					pos++;
				}
				return result;
			}
		});

		tableViewer.setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return ((EObjectFeature) element).feature.getName();
				case 1:
					return getPropertyValue((EObjectFeature) element);
				}
				return null;
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}
		});
		GridLayoutFactory.fillDefaults().generateLayout(parent);
	}

	private Object getProperty(EObject eObj, String id) {
		if (eObj == null)
			return null;

		EStructuralFeature eFeature = eObj.eClass().getEStructuralFeature(id);
		if (eFeature == null)
			return null;
		return eObj.eGet(eFeature);
	}

	@In
	public void setSelection(final EObject selection) {
		if (selection == null)
			return;
		tableViewer.setInput(selection);
	}

	private String getPropertyValue(EObjectFeature eDataObj) {
		Class<?> clazz = eDataObj.feature.getEType().getInstanceClass();
		String clsStr = clazz.getName();
		Object propVal = getProperty(eDataObj.eObj, eDataObj.feature.getName());

		if (clsStr.equals("java.lang.String") || clsStr.equals("int")) {
			if (propVal == null)
				return "<null> ";
			else
				return propVal.toString();
		} else if (clsStr.equals("boolean")) {
			if (propVal == null)
				return "<null> ";
			else
				return propVal.toString();
		}

		if (propVal == null)
			return "<null> ";
		else
			return propVal.getClass().getName();
	}

	private boolean isBoolean(EObjectFeature eDataObj) {
		Class<?> clazz = eDataObj.feature.getEType().getInstanceClass();
		String clsStr = clazz.getName();
		return clsStr.equals("boolean");
	}

	public void dispose() {
		// TBD any cleanup?
	}

}