package org.eclipse.e4.demo.modifier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class EMFScriptable extends ScriptableObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EObject eObject;

	public EMFScriptable(EObject eObject) {
		this.eObject = eObject;
	}

	@Override
	public String getClassName() {
		return eObject.getClass().getName();
	}

	private EStructuralFeature findFeature(String name) {
		for (EStructuralFeature f : eObject.eClass()
				.getEAllStructuralFeatures()) {
			if (f.getName().equals(name)) {
				return f;
			}
		}
		return null;
	}

	@Override
	public Object get(String arg0, Scriptable arg1) {
		EStructuralFeature f = findFeature(arg0);

		if (f == null)
			return super.get(arg0, arg1);

		Object rv = eObject.eGet(f);

		// FIXME java.util.Date, ...
		if (rv instanceof String || rv instanceof Boolean
				|| rv instanceof Number) {
			return rv;
		} else if (rv instanceof EObject) {
			return new EMFScriptable(eObject);
		}

		return super.get(arg0, arg1);
	}

	@Override
	public void put(String arg0, Scriptable arg1, Object arg2) {
		EStructuralFeature f = findFeature(arg0);
		
		// We always get a double so we have to convert to the appropriate value
		if( arg2 instanceof Number ) {
			if( f.getEType().getInstanceClass() == int.class ) {
				arg2 = ((Number)arg2).intValue();
			}
		}
		
		if (f == null)
			super.put(arg0, arg1, arg2);
		else
			eObject.eSet(f, arg2);
	}
}
