package org.eclipse.e4.demo.tools.simpleide;

import org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl;
import org.eclipse.e4.tools.emf.ui.common.IEditorDescriptor;
import org.eclipse.emf.ecore.EClass;

public class EditorPartDescriptorEditorDesc implements IEditorDescriptor {

	public EClass getEClass() {
		return SimpleidePackageImpl.Literals.EDITOR_PART_DESCRIPTOR;
	}

	public Class<?> getEditorClass() {
		return EditorPartDescriptorEditor.class;
	}

}
