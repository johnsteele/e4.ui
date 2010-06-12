package org.eclipse.e4.demo.tools.simpleide;

import org.eclipse.emf.ecore.EClass;

import java.util.Collections;

import org.eclipse.e4.ui.model.fragment.impl.FragmentPackageImpl;

import org.eclipse.e4.demo.simpleide.model.simpleide.impl.SimpleidePackageImpl;

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.e4.tools.emf.ui.common.IEditorFeature;

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
