package org.eclipse.e4.demo.tools.simpleide;

import java.util.Collections;
import java.util.List;
import org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl;
import org.eclipse.e4.tools.emf.ui.common.IEditorFeature;
import org.eclipse.e4.ui.model.fragment.impl.FragmentPackageImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

public class EditorFeature implements IEditorFeature {

	public List<FeatureClass> getFeatureClasses(EClass eClass,
			EStructuralFeature feature) {
		if( eClass == FragmentPackageImpl.Literals.MODEL_FRAGMENT ) {
			if( feature == FragmentPackageImpl.Literals.MODEL_FRAGMENT__ELEMENTS ) {
				return Collections.singletonList(new FeatureClass(SimpleidePackageImpl.Literals.EDITOR_PART_DESCRIPTOR.getName(), SimpleidePackageImpl.Literals.EDITOR_PART_DESCRIPTOR));
			}
		}
		return Collections.emptyList();
	}

}
