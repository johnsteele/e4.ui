package org.eclipse.ui.tools;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class SelectionView extends ViewPart {

	public class SelectionLabelProvider extends BaseLabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 2:
				return element instanceof IAdaptable ? "true" : "false"; //$NON-NLS-1$ //$NON-NLS-2$
			case 1:
				return element.getClass().getName();
			default:
				return element.toString();
			}
		}
	}

	private TableViewer viewer;
	private ISelectionListener selectionListener;

	public class SelectionContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IStructuredSelection) {
				return ((IStructuredSelection) inputElement).toArray();
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	public SelectionView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		TableColumnLayout layout = new TableColumnLayout();
		composite.setLayout(layout);
		viewer = new TableViewer(composite);
		viewer.getTable().setHeaderVisible(true);
		TableColumn stringCol = new TableColumn(viewer.getTable(), SWT.LEFT);
		stringCol.setText(Messages.SelectionView_toStringHeader);
		layout.setColumnData(stringCol, new ColumnWeightData(40, 200));
		
		TableColumn classCol = new TableColumn(viewer.getTable(), SWT.LEFT);
		classCol.setText(Messages.SelectionView_classHeader);
		layout.setColumnData(classCol, new ColumnWeightData(40, 200));
		
		TableColumn adaptableCol = new TableColumn(viewer.getTable(), SWT.LEFT);
		adaptableCol.setText(Messages.SelectionView_adaptableHeader);
		layout.setColumnData(adaptableCol, new ColumnWeightData(10, 50));
		
		viewer.setContentProvider(new SelectionContentProvider());
		viewer.setLabelProvider(new SelectionLabelProvider());
		viewer.setInput(new StructuredSelection());

	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		selectionListener = new ISelectionListener() {

			public void selectionChanged(IWorkbenchPart part,
					ISelection selection) {
				if (viewer != null) {
					viewer.setInput(selection);
				}

			}
		};
		((ISelectionService) getSite().getService(ISelectionService.class))
				.addSelectionListener(selectionListener);
	}

	@Override
	public void dispose() {
		((ISelectionService) getSite().getService(ISelectionService.class))
				.addSelectionListener(selectionListener);
		super.dispose();
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
