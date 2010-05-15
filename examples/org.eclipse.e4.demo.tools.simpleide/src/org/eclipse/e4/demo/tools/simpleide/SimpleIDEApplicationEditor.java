package org.eclipse.e4.demo.tools.simpleide;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl;
import org.eclipse.e4.tools.emf.ui.common.IModelResource;
import org.eclipse.e4.tools.emf.ui.internal.common.VirtualEntry;
import org.eclipse.e4.tools.emf.ui.internal.common.component.ApplicationEditor;
import org.eclipse.emf.databinding.EMFProperties;

@SuppressWarnings({ "restriction"})
public class SimpleIDEApplicationEditor extends ApplicationEditor {
	private static final String EDITOR_DESCRIPTORS_ID = SimpleIDEApplicationEditor.class.getName() + ".EDITOR_DESCRIPTORS"; 
	private IListProperty EDITOR_DESCRIPTORS = EMFProperties.list(SimpleidePackageImpl.Literals.SIMPLE_IDE_APPLICATION__EDITOR_PART_DESCRIPTORS);
	
	
	@Inject
	public SimpleIDEApplicationEditor(IModelResource modelResource) {
		super(modelResource.getEditingDomain());
	}
	
	@Override
	public IObservableList getChildList(Object element) {
		IObservableList list = super.getChildList(element);
		VirtualEntry<Object> v = new VirtualEntry<Object>(EDITOR_DESCRIPTORS_ID,EDITOR_DESCRIPTORS,element,"Editor Part Descriptors") {

			@Override
			protected boolean accepted(Object o) {
				return true;
			}
		};
		list.add(v);
		return list;
	}
}