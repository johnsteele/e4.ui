package org.eclipse.ui.internal.tweaklets.dependencyinjection;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.internal.tweaklets.InterceptContributions;
import org.eclipse.ui.tweaklets.dependencyinjection.DIFactory;

public class DependencyInjectionInterceptor extends InterceptContributions {

	public IEditorPart tweakEditor(Object editorContribution) {
		if (editorContribution instanceof DIFactory) {
			return new DIEditorPart(
					(DIFactory) editorContribution);
		}
		return (IEditorPart) editorContribution;
	}

	public IViewPart tweakView(Object viewContribution) {
		if (viewContribution instanceof DIFactory) {
			return new DIViewPart((DIFactory) viewContribution);
		}
		return (IViewPart) viewContribution;
	}

}
