package org.eclipse.e4.workbench.ui.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MSashForm;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.IViewLayout;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;

public class ModeledPageLayout implements IPageLayout {

	private MPerspective perspModel;
	private IPerspectiveDescriptor descriptor;
	private ArrayList newWizardShortcuts = new ArrayList();
	private ArrayList perspectiveShortcut = new ArrayList();
	private ArrayList showInPart = new ArrayList();
	private ArrayList showViewShortcut = new ArrayList();
	private ArrayList actionSet = new ArrayList();

	public ModeledPageLayout(MPerspective perspModel) {
		// Create the editor area stack
		this.perspModel = perspModel;

		MStack editorArea = ApplicationFactory.eINSTANCE.createMStack();
		editorArea.setId(getEditorArea());
		editorArea.setPolicy("EditorStack"); //$NON-NLS-1$
		// editorArea.setName("Editor Area");

		perspModel.getChildren().add(editorArea);
	}

	public MPerspective getModel() {
		return perspModel;
	}

	public void addActionSet(String actionSetId) {
		actionSet.add(actionSetId);
	}

	public void addFastView(String viewId) {
	}

	public void addFastView(String viewId, float ratio) {
	}

	public void addNewWizardShortcut(String id) {
		newWizardShortcuts.add(id);
	}

	public void addPerspectiveShortcut(String id) {
		perspectiveShortcut.add(id);
	}

	public void addPlaceholder(String viewId, int relationship, float ratio,
			String refId) {
		insertView(viewId, relationship, ratio, refId, false, true);
	}

	public void addShowInPart(String id) {
		showInPart.add(id);
	}

	public void addShowViewShortcut(String id) {
		showViewShortcut.add(id);
	}

	public void addStandaloneView(String viewId, boolean showTitle,
			int relationship, float ratio, String refId) {
		MContributedPart viewModel = insertView(viewId, relationship, ratio,
				refId, true, false);

		// Set the state
		if (viewModel != null) {
			// viewModel.setShowTitle(showTitle);
		}
	}

	public void addStandaloneViewPlaceholder(String viewId, int relationship,
			float ratio, String refId, boolean showTitle) {
		MContributedPart viewModel = insertView(viewId, relationship, ratio,
				refId, false, false);

		// Set the state
		if (viewModel != null) {
			// viewModel.setShowTitle(showTitle);
		}
	}

	public void addView(String viewId, int relationship, float ratio,
			String refId) {
		insertView(viewId, relationship, ratio, refId, true, true);
	}

	public IFolderLayout createFolder(String folderId, int relationship,
			float ratio, String refId) {
		MStack Stack = insertStack(folderId, relationship, ratio, refId, true);
		return new ModeledFolderLayout(Stack);
	}

	public IPlaceholderFolderLayout createPlaceholderFolder(String folderId,
			int relationship, float ratio, String refId) {
		MStack Stack = insertStack(folderId, relationship, ratio, refId, false);
		return new ModeledPlaceholderFolderLayout(Stack);
	}

	public void setDescriptor(IPerspectiveDescriptor desc) {
		descriptor = desc;
	}

	public IPerspectiveDescriptor getDescriptor() {
		return descriptor;
	}

	public static String internalGetEditorArea() {
		return IPageLayout.ID_EDITOR_AREA;
	}

	public String getEditorArea() {
		return internalGetEditorArea();
	}

	public int getEditorReuseThreshold() {
		return 0;
	}

	public IPlaceholderFolderLayout getFolderForView(String id) {
		MPart view = findPart(perspModel, id);
		if (view == null || !(view instanceof MContributedPart))
			return null;

		MStack stack = (MStack) view.getParent();
		if (stack == null)
			return null;

		return new ModeledPlaceholderFolderLayout(stack);
	}

	public IViewLayout getViewLayout(String id) {
		MPart view = findPart(perspModel, id);
		if (view == null || !(view instanceof MContributedPart))
			return null;

		return new ModeledViewLayout((MContributedPart) view);
	}

	public boolean isEditorAreaVisible() {
		return true;
	}

	public boolean isFixed() {
		return false;
	}

	public void setEditorAreaVisible(boolean showEditorArea) {
	}

	public void setEditorReuseThreshold(int openEditors) {
	}

	public void setFixed(boolean isFixed) {
		// perspModel.setFixed(isFixed);
	}

	private static int plRelToSwt(int rel) {
		switch (rel) {
		case IPageLayout.BOTTOM:
			return SWT.BOTTOM;
		case IPageLayout.LEFT:
			return SWT.LEFT;
		case IPageLayout.RIGHT:
			return SWT.RIGHT;
		case IPageLayout.TOP:
			return SWT.TOP;
		default:
			return 0;
		}
	}

	public static MContributedPart createViewModel(String id, boolean visible) {
		MContributedPart viewModel = ApplicationFactory.eINSTANCE
				.createMContributedPart();

		// HACK!! allow Contributed parts in a perspective
		if (id.indexOf("platform:") >= 0) { //$NON-NLS-1$
			viewModel.setURI(id);
			viewModel.setName("Contrib View"); //$NON-NLS-1$
			return viewModel;
		}

		viewModel.setId(id);

		// Get the actual view name from the extension registry
		IConfigurationElement[] views = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_VIEWS);
		IConfigurationElement viewContribution = ExtensionUtils.findExtension(
				views, id);
		if (viewContribution != null) {
			viewModel.setName(viewContribution.getAttribute("name")); //$NON-NLS-1$

			// Convert the relative path into a bundle URI
			String imagePath = viewContribution.getAttribute("icon"); //$NON-NLS-1$
			if (imagePath != null) {
				imagePath = imagePath.replace("$nl$", ""); //$NON-NLS-1$//$NON-NLS-2$
				if (imagePath.charAt(0) != '/') {
					imagePath = '/' + imagePath;
				}
				String bundleId = viewContribution.getContributor().getName();
				String imageURI = "platform:/plugin/" + bundleId + imagePath; //$NON-NLS-1$
				viewModel.setIconURI(imageURI);
			}
		} else
			viewModel.setName(id); // No registered view, create error part?

		// sets its visibility (false == placeholder)
		viewModel.setVisible(visible);

		return viewModel;
	}

	public static MStack createStack(String id, boolean visible) {
		MStack newStack = ApplicationFactory.eINSTANCE.createMStack();
		newStack.setId(id);
		newStack.setPolicy("ViewStack"); //$NON-NLS-1$

		return newStack;
	}

	private MContributedPart insertView(String viewId, int relationship,
			float ratio, String refId, boolean visible, boolean withStack) {
		MPart refModel = findPart(perspModel, refId);
		if (refModel == null || !(refModel instanceof MPart))
			return null;

		MContributedPart viewModel = createViewModel(viewId, visible);

		if (withStack) {
			String stackId = viewId + "MStack"; // Default id...basically unusable //$NON-NLS-1$
			MStack stack = insertStack(stackId, relationship, ratio, refId,
					visible);
			stack.getChildren().add(viewModel);
		} else {
			insert(viewModel, (MPart) refModel, plRelToSwt(relationship), ratio);
		}

		return viewModel;
	}

	private MStack insertStack(String stackId, int relationship, float ratio,
			String refId, boolean visible) {
		MPart refModel = findPart(perspModel, refId);
		if (refModel == null || !(refModel instanceof MPart))
			return null;

		// If the 'refModel' is -not- a stack then find one
		// This covers cases where the defining layout is adding
		// Views relative to other views and relying on the stacks
		// being automatically created.
		if (!(refModel instanceof MStack)) {
			while (refModel.getParent() != null) {
				refModel = refModel.getParent();
				if (refModel instanceof MStack)
					break;
			}
			if (!(refModel instanceof MStack))
				return null;
		}

		MStack Stack = createStack(stackId, visible);
		insert(Stack, (MPart) refModel, plRelToSwt(relationship), ratio);

		return Stack;
	}

	public static void replace(MPart relTo, MPart newParent) {
		if (relTo == null || newParent == null)
			return;

		MPart parent = (MPart) relTo.getParent();
		if (parent == null)
			return;

		List kids = parent.getChildren();
		if (kids == null)
			return;

		kids.add(kids.indexOf(relTo), newParent);
		kids.remove(relTo);
	}

	public static void insertParent(MPart newParent, MPart relTo) {
		if (newParent == null || relTo == null)
			return;

		MPart curParent = (MPart) relTo.getParent();
		if (curParent != null) {
			replace(relTo, newParent);
		}

		// Move the child under the new parent
		newParent.getChildren().add(relTo);
	}

	public static void insert(MPart toInsert, MPart relTo, int swtSide,
			int ratio) {
		if (toInsert == null || relTo == null)
			return;

		MPart relParent = (MPart) relTo.getParent();

		boolean isStack = relParent instanceof MStack;

		// Create the new sash if we're going to need one
		MSashForm newSash = null;
		if ((swtSide == SWT.TOP || swtSide == SWT.BOTTOM) && !isStack) {
			newSash = ApplicationFactory.eINSTANCE.createMSashForm();
			String label = "Vertical Sash[" + toInsert.getId() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
			newSash.setId(label);
			newSash.setPolicy("Vertical"); //horizontal = false //$NON-NLS-1$
		} else if ((swtSide == SWT.LEFT || swtSide == SWT.RIGHT) && !isStack) {
			newSash = ApplicationFactory.eINSTANCE.createMSashForm();
			String label = "Horizontal Sash[" + toInsert.getId() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
			newSash.setId(label);
			newSash.setPolicy("Horizontal"); //horizontal = true //$NON-NLS-1$
		}

		List parts;
		if (newSash == null && relParent != null) {
			parts = relParent.getChildren();
		} else {
			insertParent(newSash, relTo);
			parts = newSash.getChildren();

			List<Integer> weights = newSash.getWeights();
			weights.add(ratio);
			weights.add(100 - ratio);
		}

		// Insert the part in the correct location
		int index = parts.indexOf(relTo);
		if (swtSide == SWT.BOTTOM || swtSide == SWT.RIGHT) {
			index++;

		}

		parts.add(index, toInsert);
	}

	public static void insert(MPart toInsert, MPart relTo, int swtSide,
			float ratio) {
		int pct = (int) (ratio * 100);
		insert(toInsert, relTo, swtSide, pct);
	}

	private static MPart findElementById(MPart part, String id) {
		if (id == null || id.length() == 0)
			return null;

		// is it me?
		if (id.equals(part.getId()))
			return part;

		// Recurse
		EList children = part.getChildren();
		MPart foundPart = null;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			MPart childPart = (MPart) iterator.next();
			foundPart = findElementById(childPart, id);
			if (foundPart != null)
				return foundPart;
		}

		return null;
	}

	public static MPart findPart(MPart toSearch, String id) {
		if (toSearch == null)
			return null;

		MPart found = findElementById(toSearch, id);
		if (found instanceof MPart)
			return (MPart) found;

		return null;
	}

	/**
	 * @return
	 */
	public ArrayList getNewWizardShortcuts() {
		return newWizardShortcuts;
	}

	/**
	 * @return
	 */
	public ArrayList getShowViewShortcuts() {
		return showViewShortcut;
	}

	/**
	 * @return
	 */
	public ArrayList getPerspectiveShortcuts() {
		return perspectiveShortcut;
	}

	/**
	 * @return
	 */
	public ArrayList getShowInPartIds() {
		return showInPart;
	}
}
