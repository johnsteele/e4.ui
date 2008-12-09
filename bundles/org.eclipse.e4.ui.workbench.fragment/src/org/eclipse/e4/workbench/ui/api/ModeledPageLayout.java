package org.eclipse.e4.workbench.ui.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.e4.ui.model.application.ApplicationFactory;
import org.eclipse.e4.ui.model.application.ContributedPart;
import org.eclipse.e4.ui.model.application.Part;
import org.eclipse.e4.ui.model.application.SashForm;
import org.eclipse.e4.ui.model.application.Stack;
import org.eclipse.e4.ui.model.workbench.Perspective;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.IViewLayout;

public class ModeledPageLayout implements IPageLayout {

	private Perspective perspModel;

	public ModeledPageLayout(Perspective perspModel) {
		// Create the editor area stack
		this.perspModel = perspModel;

		Stack editorArea = ApplicationFactory.eINSTANCE.createStack();
		editorArea.setId(getEditorArea());
		//editorArea.setName("Editor Area");
		
		perspModel.getChildren().add(editorArea);
	}
	
	public void addActionSet(String actionSetId) {
	}

	public void addFastView(String viewId) {
	}

	public void addFastView(String viewId, float ratio) {
	}

	public void addNewWizardShortcut(String id) {
	}

	public void addPerspectiveShortcut(String id) {
	}

	public void addPlaceholder(String viewId, int relationship, float ratio,
			String refId) {
		insertView(viewId, relationship, ratio, refId, false, true);
	}

	public void addShowInPart(String id) {
	}

	public void addShowViewShortcut(String id) {
	}

	public void addStandaloneView(String viewId, boolean showTitle,
			int relationship, float ratio, String refId) {
		ContributedPart viewModel = insertView(viewId, relationship, ratio, refId, true, false);
		
		// Set the state
		if (viewModel != null) {
			//viewModel.setShowTitle(showTitle);
		}
	}

	public void addStandaloneViewPlaceholder(String viewId, int relationship,
			float ratio, String refId, boolean showTitle) {
		ContributedPart viewModel = insertView(viewId, relationship, ratio, refId, false, false);
		
		// Set the state
		if (viewModel != null) {
			//viewModel.setShowTitle(showTitle);
		}
	}

	public void addView(String viewId, int relationship, float ratio,
			String refId) {
		insertView(viewId, relationship, ratio, refId, true, true);
	}

	public IFolderLayout createFolder(String folderId, int relationship,
			float ratio, String refId) {
		Stack Stack = insertStack(folderId, relationship, ratio, refId, true);
		return new ModeledFolderLayout(Stack);
	}

	public IPlaceholderFolderLayout createPlaceholderFolder(String folderId,
			int relationship, float ratio, String refId) {
		Stack Stack = insertStack(folderId, relationship, ratio, refId, false);
		return new ModeledPlaceholderFolderLayout(Stack);
	}

	public IPerspectiveDescriptor getDescriptor() {
		return null;
	}

	public static String internalGetEditorArea() {
		return "org.eclipse.ui.EditorArea"; //$NON-NLS-1$
	}
	
	public String getEditorArea() {
		return internalGetEditorArea();
	}

	public int getEditorReuseThreshold() {
		return 0;
	}

	public IPlaceholderFolderLayout getFolderForView(String id) {
		Part view = findPart(perspModel, id);
		if (view == null || !(view instanceof ContributedPart))
			return null;
		
		Stack stack = (Stack) view.getParent();
		if (stack == null)
			return null;
		
		return new ModeledPlaceholderFolderLayout(stack);
	}

	public IViewLayout getViewLayout(String id) {
		Part view = findPart(perspModel, id);
		if (view == null || !(view instanceof ContributedPart))
			return null;
		
		return new ModeledViewLayout((ContributedPart) view);
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
		//perspModel.setFixed(isFixed);
	}
	
	private static int plRelToSwt(int rel) {
		switch (rel) {
		case IPageLayout.BOTTOM: return SWT.BOTTOM;
		case IPageLayout.LEFT: return SWT.LEFT;
		case IPageLayout.RIGHT: return SWT.RIGHT;
		case IPageLayout.TOP: return SWT.TOP;
		default: return 0;
		}
	}
	

    public static ContributedPart createViewModel(String id, boolean visible) { 
    	ContributedPart viewModel = ApplicationFactory.eINSTANCE.createContributedPart();
    	viewModel.setId(id);
    	viewModel.setName(id);
    	
    	// HACK!! we don't have an attribute
    	viewModel.setPolicy(Boolean.toString(visible));
    	//viewModel.setVisible(visible);
		
		return viewModel;
    }
    
    public static Stack createStack(String id, boolean visible) {
    	Stack newStack = ApplicationFactory.eINSTANCE.createStack();
    	newStack.setId(id);
    	newStack.setPolicy(Boolean.toString(visible));
		
		return newStack;
    }
    
    private ContributedPart insertView(String viewId, int relationship, float ratio,
			String refId, boolean visible, boolean withStack) {
		Part refModel = findPart(perspModel, refId);
		if (refModel == null || !(refModel instanceof Part))
			return null;

		ContributedPart viewModel = createViewModel(viewId, visible);

		if (withStack) {
			String stackId = viewId + "Stack"; // Default id...basically unusable //$NON-NLS-1$
			Stack stack = insertStack(stackId, relationship, ratio, refId, visible);			
			stack.getChildren().add(viewModel);
		}
		else {
			insert(viewModel, (Part)refModel, plRelToSwt(relationship), ratio);
		}
		
		return viewModel;
    }
    
    private Stack insertStack(String stackId, int relationship, float ratio,
			String refId, boolean visible) {
		Part refModel = findPart(perspModel, refId);
		if (refModel == null || !(refModel instanceof Part))
			return null;
		
		Stack Stack = createStack(stackId, visible);				
		insert(Stack, (Part)refModel, plRelToSwt(relationship), ratio);
		
		return Stack;
    }
	
	public static void replace(Part relTo, Part newParent) {
		if (relTo == null || newParent == null)
			return;
		
		Part parent = (Part) relTo.getParent();
		if (parent == null)
			return;
		
		List kids = parent.getChildren();
		if (kids == null)
			return;

		kids.add(kids.indexOf(relTo), newParent);
		kids.remove(relTo);
	}
	
	public static void insertParent(Part newParent, Part relTo) {
		if (newParent == null || relTo == null)
			return;

		Part curParent = (Part) relTo.getParent();
		if (curParent != null) {
			replace(relTo, newParent);
		}

		// Move the child under the new parent
		newParent.getChildren().add(relTo);
	}
    
	public static void insert(Part toInsert, Part relTo,
			int swtSide, int ratio) {
		if (toInsert == null || relTo == null)
			return;
		
		Part relParent = (Part) relTo.getParent();
		
		boolean isStack = relParent instanceof Stack;

		// Create the new sash if we're going to need one
		SashForm newSash = null;
		if ((swtSide == SWT.TOP || swtSide == SWT.BOTTOM) && !isStack) {
			newSash = ApplicationFactory.eINSTANCE.createSashForm();
			String label = "Vertical Sash[" + toInsert.getId() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
			newSash.setId(label);
			newSash.setPolicy("Vertical");	//horizontal = false //$NON-NLS-1$
		}
		else if ((swtSide == SWT.LEFT || swtSide == SWT.RIGHT) && !isStack) {
			newSash = ApplicationFactory.eINSTANCE.createSashForm();
			String label = "Horizontal Sash[" + toInsert.getId() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
			newSash.setId(label);
			newSash.setPolicy("Horizontal");	//horizontal = true //$NON-NLS-1$
		}
		
		List parts;
		if (newSash == null && relParent != null) {
			parts = relParent.getChildren();
		}
		else {
			insertParent(newSash, relTo);
			parts = newSash.getChildren();
			
			List<Integer> weights = newSash.getWeights();
			weights.add(ratio);
			weights.add(100-ratio);
		}
		
		// Insert the part in the correct location
		int index = parts.indexOf(relTo);
		if (swtSide == SWT.BOTTOM || swtSide == SWT.RIGHT) {
			index++;
			
		}
		
		parts.add(index, toInsert);
	}

	public static void insert(Part toInsert, Part relTo,
			int swtSide, float ratio) {
		int pct = (int) (ratio * 100);
		insert(toInsert, relTo, swtSide, pct);
	}
	
	private static Part findElementById(Part part, String id) {
		if (id == null || id.length() == 0)
			return null;
		
		// is it me?
		if (id.equals(part.getId()))
			return part;
		
		// Recurse
		EList children = part.getChildren();
		Part foundPart = null;
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			Part childPart = (Part) iterator.next();
			foundPart = findElementById(childPart, id);
			if (foundPart != null)
				return foundPart;
		}
		
		return null;
	}
	
	public static Part findPart(Part toSearch, String id) {
		Part found = findElementById(toSearch, id);
		if (found instanceof Part)
			return (Part) found;
		
		return null;
	}
}
