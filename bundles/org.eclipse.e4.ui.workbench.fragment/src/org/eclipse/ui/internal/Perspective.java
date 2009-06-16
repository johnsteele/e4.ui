/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Markus Alexander Kuppe, Versant GmbH - bug 215797
 *******************************************************************************/

package org.eclipse.ui.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.extensions.ExtensionUtils;
import org.eclipse.e4.extensions.ModelViewReference;
import org.eclipse.e4.ui.model.application.MContributedPart;
import org.eclipse.e4.ui.model.application.MPart;
import org.eclipse.e4.ui.model.application.MStack;
import org.eclipse.e4.ui.model.application.MWindow;
import org.eclipse.e4.ui.model.workbench.MPerspective;
import org.eclipse.e4.ui.model.workbench.WorkbenchFactory;
import org.eclipse.e4.workbench.ui.api.ModeledPageLayout;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.Geometry;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.internal.StartupThreading.StartupRunnable;
import org.eclipse.ui.internal.intro.IIntroConstants;
import org.eclipse.ui.internal.layout.ITrimManager;
import org.eclipse.ui.internal.layout.IWindowTrim;
import org.eclipse.ui.internal.layout.TrimLayout;
import org.eclipse.ui.internal.misc.StatusUtil;
import org.eclipse.ui.internal.registry.ActionSetRegistry;
import org.eclipse.ui.internal.registry.IActionSetDescriptor;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.eclipse.ui.internal.registry.PerspectiveDescriptor;
import org.eclipse.ui.internal.registry.PerspectiveExtensionReader;
import org.eclipse.ui.internal.registry.PerspectiveRegistry;
import org.eclipse.ui.internal.util.PrefUtil;
import org.eclipse.ui.presentations.AbstractPresentationFactory;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.views.IViewDescriptor;
import org.eclipse.ui.views.IViewRegistry;

/**
 * The ViewManager is a factory for workbench views.
 */
public class Perspective {
	protected PerspectiveDescriptor descriptor;

	protected WorkbenchPage page;

	// Editor Area management
	protected LayoutPart editorArea;
	protected PartPlaceholder editorHolder;
	protected boolean editorHidden = false;
	protected boolean editorAreaRestoreOnUnzoom = false;
	protected int editorAreaState = IStackPresentationSite.STATE_RESTORED;

	private ViewFactory viewFactory;

	protected ArrayList alwaysOnActionSets;

	protected ArrayList alwaysOffActionSets;

	protected ArrayList newWizardShortcuts;

	protected ArrayList showViewShortcuts;

	protected ArrayList perspectiveShortcuts;

	/** IDs of menu items the user has chosen to hide */
	protected Collection hideMenuIDs;

	/** IDs of toolbar items the user has chosen to hide */
	protected Collection hideToolBarIDs;

	// private List fastViews;
	protected FastViewManager fastViewManager = null;

	private Map mapIDtoViewLayoutRec;

	protected boolean fixed;

	protected ArrayList showInPartIds;

	protected HashMap showInTimes = new HashMap();

	private IViewReference activeFastView;

	protected IMemento memento;

	protected PerspectiveHelper presentation;

	final static private String VERSION_STRING = "0.016";//$NON-NLS-1$

	private FastViewPane fastViewPane = new FastViewPane();

	// fields used by fast view resizing via a sash
	private static final int FASTVIEW_HIDE_STEPS = 5;

	/**
	 * Reference to the part that was previously active when this perspective
	 * was deactivated.
	 */
	private IWorkbenchPartReference oldPartRef = null;

	protected boolean shouldHideEditorsOnActivate = false;

	protected ModeledPageLayout layout;

	/**
	 * ViewManager constructor comment.
	 */
	public Perspective(PerspectiveDescriptor desc, WorkbenchPage page)
			throws WorkbenchException {
		this(page);
		descriptor = desc;
		if (desc != null) {
			createPresentation(desc);
		}
	}

	/**
	 * ViewManager constructor comment.
	 */
	protected Perspective(WorkbenchPage page) throws WorkbenchException {
		this.page = page;
		this.viewFactory = page.getViewFactory();
		alwaysOnActionSets = new ArrayList(2);
		alwaysOffActionSets = new ArrayList(2);
		hideMenuIDs = new HashSet();
		hideToolBarIDs = new HashSet();

		// We'll only make a FastView Manager if there's a
		// Trim manager in the WorkbenchWindow
		IWorkbenchWindow wbw = page.getWorkbenchWindow();
		if (wbw instanceof WorkbenchWindow) {
			if (((WorkbenchWindow) wbw).getTrimManager() != null)
				fastViewManager = new FastViewManager(this, page);
		}

		mapIDtoViewLayoutRec = new HashMap();
	}

	/**
	 * Sets the fast view attribute. Note: The page is expected to update action
	 * bars.
	 */
	public void makeFastView(IViewReference ref) {
		addFastView(ref, true);
	}

	/**
	 * Sets the fast view attribute. Note: The page is expected to update action
	 * bars.
	 */
	public void addFastView(IViewReference ref, boolean handleLayout) {
		ViewPane pane = (ViewPane) ((WorkbenchPartReference) ref).getPane();
		if (!isFastView(ref)) {
			if (handleLayout) {
				// Only remove the part from the presentation if it
				// is actually in the presentation.
				if (presentation.hasPlaceholder(ref.getId(), ref
						.getSecondaryId())
						|| pane.getContainer() != null) {
					presentation.removePart(pane);
				}
			}

			// We are drag-enabling the pane because it has been disabled
			// when it was removed from the perspective presentation.
			pane.setFast(true);
			Control ctrl = pane.getControl();
			if (ctrl != null) {
				ctrl.setEnabled(false); // Remove focus support.
			}
		}
	}

	/**
	 * Moves a part forward in the Z order of a perspective so it is visible.
	 * 
	 * @param part
	 *            the part to bring to move forward
	 * @return true if the part was brought to top, false if not.
	 */
	public boolean bringToTop(IViewReference ref) {
		if (isFastView(ref)) {
			setActiveFastView(ref);
			return true;
		} else {
			return presentation.bringPartToTop(getPane(ref));
		}
	}

	/**
	 * Returns whether a view exists within the perspective.
	 */
	public boolean containsView(IViewPart view) {
		IViewSite site = view.getViewSite();
		IViewReference ref = findView(site.getId(), site.getSecondaryId());
		if (ref == null) {
			return false;
		}
		return (view == ref.getPart(false));
	}

	/**
	 * Create the initial list of action sets.
	 */
	protected void createInitialActionSets(List outputList, List stringList) {
		ActionSetRegistry reg = WorkbenchPlugin.getDefault()
				.getActionSetRegistry();
		Iterator iter = stringList.iterator();
		while (iter.hasNext()) {
			String id = (String) iter.next();
			IActionSetDescriptor desc = reg.findActionSet(id);
			if (desc != null) {
				outputList.add(desc);
			} else {
				WorkbenchPlugin.log("Unable to find Action Set: " + id);//$NON-NLS-1$
			}
		}
	}

	/**
	 * Create a presentation for a perspective.
	 */
	private void createPresentation(PerspectiveDescriptor persp)
			throws WorkbenchException {
		if (persp.hasCustomDefinition()) {
			loadCustomPersp(persp);
		} else {
			loadPredefinedPersp(persp);
		}
	}

	/**
	 * Dispose the perspective and all views contained within.
	 */
	public void dispose() {
		// Get rid of presentation.
		if (presentation == null) {
			disposeViewRefs();
			return;
		}

		presentation.deactivate();
		presentation.dispose();

		fastViewPane.dispose();

		// Release each view.
		IViewReference refs[] = getViewReferences();
		for (int i = 0, length = refs.length; i < length; i++) {
			getViewFactory().releaseView(refs[i]);
		}

		mapIDtoViewLayoutRec.clear();
	}

	private void disposeViewRefs() {
		if (memento == null) {
			return;
		}
		IMemento views[] = memento.getChildren(IWorkbenchConstants.TAG_VIEW);
		for (int x = 0; x < views.length; x++) {
			// Get the view details.
			IMemento childMem = views[x];
			String id = childMem.getString(IWorkbenchConstants.TAG_ID);
			// skip creation of the intro reference - it's handled elsewhere.
			if (id.equals(IIntroConstants.INTRO_VIEW_ID)) {
				continue;
			}

			String secondaryId = ViewFactory.extractSecondaryId(id);
			if (secondaryId != null) {
				id = ViewFactory.extractPrimaryId(id);
			}
			// Create and open the view.

			if (!"true".equals(childMem.getString(IWorkbenchConstants.TAG_REMOVED))) { //$NON-NLS-1$
				IViewReference ref = viewFactory.getView(id, secondaryId);
				if (ref != null) {
					viewFactory.releaseView(ref);
				}
			}

		}
	}

	/**
	 * Finds the view with the given ID that is open in this page, or
	 * <code>null</code> if not found.
	 * 
	 * @param viewId
	 *            the view ID
	 */
	public IViewReference findView(String viewId) {
		return findView(viewId, null);
	}

	/**
	 * Finds the view with the given id and secondary id that is open in this
	 * page, or <code>null</code> if not found.
	 * 
	 * @param viewId
	 *            the view ID
	 * @param secondaryId
	 *            the secondary ID
	 */
	public IViewReference findView(String id, String secondaryId) {
		IViewReference refs[] = getViewReferences();
		for (int i = 0; i < refs.length; i++) {
			IViewReference ref = refs[i];
			if (id.equals(ref.getId())
					&& (secondaryId == null ? ref.getSecondaryId() == null
							: secondaryId.equals(ref.getSecondaryId()))) {
				return ref;
			}
		}
		return null;
	}

	/**
	 * Returns the window's client composite widget which views and editor area
	 * will be parented.
	 */
	public Composite getClientComposite() {
		return page.getClientComposite();
	}

	/**
	 * Returns the perspective.
	 */
	public IPerspectiveDescriptor getDesc() {
		return descriptor;
	}

	/**
	 * Returns the bounds of the given fast view.
	 */
	/* package */Rectangle getFastViewBounds(IViewReference ref) {
		// Copy the bounds of the page composite
		Rectangle bounds = page.getClientComposite().getBounds();
		// get the width ratio of the fast view
		float ratio = getFastViewWidthRatio(ref);
		// Compute the actual width of the fast view.
		bounds.width = (int) (ratio * getClientComposite().getSize().x);
		return bounds;
	}

	/**
	 * Returns the docked views.
	 */
	public IViewReference[] getFastViews() {
		if (fastViewManager == null)
			return new IViewReference[0];

		List trueFVBRefs = fastViewManager
				.getFastViews(FastViewBar.FASTVIEWBAR_ID);
		IViewReference array[] = new IViewReference[trueFVBRefs.size()];
		trueFVBRefs.toArray(array);
		return array;
	}

	/**
	 * Returns the new wizard shortcuts associated with this perspective.
	 * 
	 * @return an array of new wizard identifiers
	 */
	public String[] getNewWizardShortcuts() {
		return (String[]) newWizardShortcuts
				.toArray(new String[newWizardShortcuts.size()]);
	}

	/**
	 * Returns the pane for a view reference.
	 */
	protected ViewPane getPane(IViewReference ref) {
		return null;
	}

	/**
	 * Returns the perspective shortcuts associated with this perspective.
	 * 
	 * @return an array of perspective identifiers
	 */
	public String[] getPerspectiveShortcuts() {
		return (String[]) perspectiveShortcuts
				.toArray(new String[perspectiveShortcuts.size()]);
	}

	/**
	 * Returns the presentation.
	 */
	public PerspectiveHelper getPresentation() {
		return presentation;
	}

	/**
	 * Retrieves the fast view width ratio for the given view. If the ratio is
	 * not known, the default ratio for the view is assigned and returned.
	 */
	public float getFastViewWidthRatio(IViewReference ref) {
		ViewLayoutRec rec = getViewLayoutRec(ref, true);
		if (rec.fastViewWidthRatio == IPageLayout.INVALID_RATIO) {
			IViewRegistry reg = WorkbenchPlugin.getDefault().getViewRegistry();
			IViewDescriptor desc = reg.find(ref.getId());
			rec.fastViewWidthRatio = (desc != null ? desc
					.getFastViewWidthRatio()
					: IPageLayout.DEFAULT_FASTVIEW_RATIO);
		}
		return rec.fastViewWidthRatio;
	}

	/**
	 * Returns the ids of the parts to list in the Show In... dialog. This is a
	 * List of Strings.
	 */
	public ArrayList getShowInPartIds() {
		return showInPartIds;
	}

	/**
	 * Returns the time at which the last Show In was performed for the given
	 * target part, or 0 if unknown.
	 */
	public long getShowInTime(String partId) {
		Long t = (Long) showInTimes.get(partId);
		return t == null ? 0L : t.longValue();
	}

	/**
	 * Returns the show view shortcuts associated with this perspective.
	 * 
	 * @return an array of view identifiers
	 */
	public String[] getShowViewShortcuts() {
		return (String[]) showViewShortcuts
				.toArray(new String[showViewShortcuts.size()]);
	}

	/**
	 * Returns the view factory.
	 */
	public ViewFactory getViewFactory() {
		return viewFactory;
	}

	/**
	 * See IWorkbenchPage.
	 */
	public IViewReference[] getViewReferences() {
		// Get normal views.
		if (presentation == null) {
			return new IViewReference[0];
		}

		List panes = new ArrayList(5);
		presentation.collectViewPanes(panes);

		List fastViews = (fastViewManager != null) ? fastViewManager
				.getFastViews(null) : new ArrayList();
		IViewReference[] resultArray = new IViewReference[panes.size()
				+ fastViews.size()];

		// Copy fast views.
		int nView = 0;
		for (int i = 0; i < fastViews.size(); i++) {
			resultArray[nView] = (IViewReference) fastViews.get(i);
			++nView;
		}

		// Copy normal views.
		for (int i = 0; i < panes.size(); i++) {
			ViewPane pane = (ViewPane) panes.get(i);
			resultArray[nView] = pane.getViewReference();
			++nView;
		}

		return resultArray;
	}

	/**
	 * Hide the editor area if visible
	 */
	protected void hideEditorArea() {
		if (!isEditorAreaVisible()) {
			return;
		}

		// Show the editor in the appropriate location
		if (useNewMinMax(this)) {
			// If it's the currently maximized part we have to restore first
			if (getPresentation().getMaximizedStack() instanceof EditorStack) {
				getPresentation().getMaximizedStack().setState(
						IStackPresentationSite.STATE_RESTORED);
			}

			boolean isMinimized = editorAreaState == IStackPresentationSite.STATE_MINIMIZED;
			if (!isMinimized)
				hideEditorAreaLocal();
			else
				setEditorAreaTrimVisibility(false);
		} else {
			hideEditorAreaLocal();
		}

		editorHidden = true;
	}

	/**
	 * Hide the editor area if visible
	 */
	protected void hideEditorAreaLocal() {
		if (editorHolder != null) {
			return;
		}

		// Replace the editor area with a placeholder so we
		// know where to put it back on show editor area request.
		editorHolder = new PartPlaceholder(editorArea.getID());
		presentation.getLayout().replace(editorArea, editorHolder);
	}

	/**
	 * Hides a fast view. The view shrinks equally <code>steps</code> times
	 * before disappearing completely.
	 */
	private void hideFastView(IViewReference ref, int steps) {
		setFastViewIconSelection(ref, false);

		// Note: We always do at least one step of the animation.
		// Note: This doesn't take into account the overhead of doing
		if (ref == activeFastView) {
			saveFastViewWidthRatio();
			fastViewPane.hideView();
		}
	}

	/**
	 * Hides the fast view sash for zooming in a fast view.
	 */
	void hideFastViewSash() {
		fastViewPane.hideFastViewSash();
	}

	public boolean hideView(IViewReference ref) {
		// If the view is locked just return.
		ModelViewReference mvr = (ModelViewReference) ref;
		mvr.getModel().setVisible(false);
		return true;
	}

	/*
	 * Return whether the editor area is visible or not.
	 */
	protected boolean isEditorAreaVisible() {
		return !editorHidden;
	}

	/**
	 * Returns true if a view is fast.
	 */
	public boolean isFastView(IViewReference ref) {
		if (fastViewManager == null)
			return false;

		return fastViewManager.isFastView(ref);
	}

	/**
	 * Returns the view layout rec for the given view reference, or null if not
	 * found. If create is true, it creates the record if not already created.
	 */
	public ViewLayoutRec getViewLayoutRec(IViewReference ref, boolean create) {
		ViewLayoutRec result = getViewLayoutRec(ViewFactory.getKey(ref), create);
		if (result == null && create == false) {
			result = getViewLayoutRec(ref.getId(), false);
		}
		return result;
	}

	/**
	 * Returns the view layout record for the given view id or null if not
	 * found. If create is true, it creates the record if not already created.
	 */
	private ViewLayoutRec getViewLayoutRec(String viewId, boolean create) {
		ViewLayoutRec rec = (ViewLayoutRec) mapIDtoViewLayoutRec.get(viewId);
		if (rec == null && create) {
			rec = new ViewLayoutRec();
			mapIDtoViewLayoutRec.put(viewId, rec);
		}
		return rec;
	}

	/**
	 * Returns true if a layout or perspective is fixed.
	 */
	public boolean isFixedLayout() {
		// @issue is there a difference between a fixed
		// layout and a fixed perspective?? If not the API
		// may need some polish, WorkbenchPage, PageLayout
		// and Perspective all have isFixed methods.
		// PageLayout and Perspective have their own fixed
		// attribute, we are assuming they are always in sync.
		// WorkbenchPage delegates to the perspective.
		return fixed;
	}

	/**
	 * Returns true if a view is standalone.
	 * 
	 * @since 3.0
	 */
	public boolean isStandaloneView(IViewReference ref) {
		ViewLayoutRec rec = getViewLayoutRec(ref, false);
		return rec != null && rec.isStandalone;
	}

	/**
	 * Returns whether the title for a view should be shown. This applies only
	 * to standalone views.
	 * 
	 * @since 3.0
	 */
	public boolean getShowTitleView(IViewReference ref) {
		ViewLayoutRec rec = getViewLayoutRec(ref, false);
		return rec != null && rec.showTitle;
	}

	/**
	 * Creates a new presentation from a persistence file. Note: This method
	 * should not modify the current state of the perspective.
	 */
	private void loadCustomPersp(PerspectiveDescriptor persp) {
		// get the layout from the registry
		PerspectiveRegistry perspRegistry = (PerspectiveRegistry) WorkbenchPlugin
				.getDefault().getPerspectiveRegistry();
		try {
			IMemento memento = perspRegistry.getCustomPersp(persp.getId());
			// Restore the layout state.
			MultiStatus status = new MultiStatus(
					PlatformUI.PLUGIN_ID,
					IStatus.OK,
					NLS
							.bind(
									WorkbenchMessages.Perspective_unableToRestorePerspective,
									persp.getLabel()), null);
			status.merge(restoreState(memento));
			status.merge(restoreState());
			if (status.getSeverity() != IStatus.OK) {
				unableToOpenPerspective(persp, status);
			}
		} catch (IOException e) {
			unableToOpenPerspective(persp, null);
		} catch (WorkbenchException e) {
			unableToOpenPerspective(persp, e.getStatus());
		}
	}

	private void unableToOpenPerspective(PerspectiveDescriptor persp,
			IStatus status) {
		PerspectiveRegistry perspRegistry = (PerspectiveRegistry) WorkbenchPlugin
				.getDefault().getPerspectiveRegistry();
		perspRegistry.deletePerspective(persp);
		// If this is a predefined perspective, we will not be able to delete
		// the perspective (we wouldn't want to). But make sure to delete the
		// customized portion.
		persp.deleteCustomDefinition();
		String title = WorkbenchMessages.Perspective_problemRestoringTitle;
		String msg = WorkbenchMessages.Perspective_errorReadingState;
		if (status == null) {
			MessageDialog.openError((Shell) null, title, msg);
		} else {
			ErrorDialog.openError((Shell) null, title, msg, status);
		}
	}

	/**
	 * Create a presentation for a perspective. Note: This method should not
	 * modify the current state of the perspective.
	 */
	protected void loadPredefinedPersp(PerspectiveDescriptor persp)
			throws WorkbenchException {
		// Create layout engine.
		IPerspectiveFactory factory = null;
		try {
			factory = persp.createFactory();
		} catch (CoreException e) {
			throw new WorkbenchException(NLS.bind(
					WorkbenchMessages.Perspective_unableToLoad, persp.getId()));
		}

		/*
		 * IPerspectiveFactory#createFactory() can return null
		 */
		if (factory == null) {
			throw new WorkbenchException(NLS.bind(
					WorkbenchMessages.Perspective_unableToLoad, persp.getId()));
		}

		MPerspective<?> persModel = WorkbenchFactory.eINSTANCE
				.createMPerspective();
		persModel.setId(persp.getId());
		MWindow e4Window = page.getModelWindow();
		e4Window.getChildren().add(persModel);
		layout = new ModeledPageLayout(persModel);
		((ModeledPageLayout) layout).setDescriptor(descriptor);
		newWizardShortcuts = layout.getNewWizardShortcuts();
		showViewShortcuts = layout.getShowViewShortcuts();
		perspectiveShortcuts = layout.getPerspectiveShortcuts();
		showInPartIds = layout.getShowInPartIds();
		// hideMenuIDs = layout.getHiddenMenuItems();
		// hideToolBarIDs = layout.getHiddenToolBarItems();
		layout.setFixed(descriptor.getFixed());

		// add the placeholders for the sticky folders and their contents

		// Run layout engine.
		factory.createInitialLayout(layout);
		loadExtensions(persModel, ((ModeledPageLayout) layout));
		MPart ea = ModeledPageLayout.findPart(persModel, "bottom"); //$NON-NLS-1$
		if (ea == null) {
			layout
					.createPlaceholderFolder(
							"bottom", IPageLayout.BOTTOM, 0.2f, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
		}
	}

	private void loadExtensions(MPerspective<?> perspModel,
			ModeledPageLayout layout) {
		String perspId = perspModel.getId();
		if (perspId == null)
			return;

		IConfigurationElement[] perspExts = ExtensionUtils
				.getExtensions(IWorkbenchRegistryConstants.PL_PERSPECTIVE_EXTENSIONS);
		for (int i = 0; i < perspExts.length; i++) {
			String extTarget = perspExts[i]
					.getAttribute(IWorkbenchRegistryConstants.ATT_TARGET_ID);
			if (perspId.equals(extTarget)) {
				IConfigurationElement[] viewExts = perspExts[i]
						.getChildren(IWorkbenchRegistryConstants.TAG_VIEW);
				for (int j = 0; j < viewExts.length; j++) {
					if (viewExts[j] == null)
						continue; // this view has already being created
					addView(j, viewExts, perspModel);
				}
			}
		}
	}

	private MPart addView(int position, IConfigurationElement[] viewExts,
			MPerspective<?> perspModel) {
		String id = viewExts[position]
				.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
		// String relationship =
		// viewExts[j].getAttribute("relationship");
		String relative = viewExts[position]
				.getAttribute(IWorkbenchRegistryConstants.ATT_RELATIVE);
		String visible = viewExts[position]
				.getAttribute(IWorkbenchRegistryConstants.ATT_VISIBLE);
		// String closeable = viewExts[j].getAttribute("closeable");
		// String showTitle = viewExts[j].getAttribute("showTitle");

		MPart<?> relPart = ModeledPageLayout.findPart(perspModel, relative);
		if (relPart == null) { // is it declared later in the extensions?
			for (int i = position + 1; i < viewExts.length; i++) {
				if (viewExts[i] == null)
					continue; // this view has already being created
				String otherID = viewExts[i]
						.getAttribute(IWorkbenchRegistryConstants.ATT_ID);
				if (otherID == null)
					continue;
				if (otherID.equals(relative)) {
					relPart = addView(i, viewExts, perspModel);
					if (relPart != null)
						break;
				}
			}
		}
		MStack sm;
		if (relPart != null) {
			// TBD is the parent of a part always a stack?
			// How about a sash?
			sm = (MStack) relPart.getParent();
		} else { // get a default stack
			sm = getDefaultStack(perspModel);
		}

		MContributedPart<?> viewModel = ModeledPageLayout.createViewModel(id,
				Boolean.parseBoolean(visible));
		sm.getChildren().add(viewModel);
		viewExts[position] = null; // only create once
		return viewModel;
	}

	private MStack getDefaultStack(MPart<?> part) {
		// TBD is there a better approach in 3.x?
		// find any stack in the perspective's ascendants
		for (Object child : part.getChildren()) {
			if (child instanceof MStack)
				return (MStack) child;
			if (child instanceof MPart<?>) {
				MStack result = getDefaultStack((MPart<?>) child);
				if (result != null)
					return result;
			}
		}
		return null;
	}

	private void removeAlwaysOn(IActionSetDescriptor descriptor) {
		if (descriptor == null) {
			return;
		}
		if (!alwaysOnActionSets.contains(descriptor)) {
			return;
		}

		alwaysOnActionSets.remove(descriptor);
		if (page != null) {
			page.perspectiveActionSetChanged(this, descriptor,
					ActionSetManager.CHANGE_HIDE);
		}
	}

	protected void addAlwaysOff(IActionSetDescriptor descriptor) {
		if (descriptor == null) {
			return;
		}
		if (alwaysOffActionSets.contains(descriptor)) {
			return;
		}
		alwaysOffActionSets.add(descriptor);
		if (page != null) {
			page.perspectiveActionSetChanged(this, descriptor,
					ActionSetManager.CHANGE_MASK);
		}
		removeAlwaysOn(descriptor);
	}

	protected void addAlwaysOn(IActionSetDescriptor descriptor) {
		if (descriptor == null) {
			return;
		}
		if (alwaysOnActionSets.contains(descriptor)) {
			return;
		}
		alwaysOnActionSets.add(descriptor);
		if (page != null) {
			page.perspectiveActionSetChanged(this, descriptor,
					ActionSetManager.CHANGE_SHOW);
		}
		removeAlwaysOff(descriptor);
	}

	private void removeAlwaysOff(IActionSetDescriptor descriptor) {
		if (descriptor == null) {
			return;
		}
		if (!alwaysOffActionSets.contains(descriptor)) {
			return;
		}
		alwaysOffActionSets.remove(descriptor);
		if (page != null) {
			page.perspectiveActionSetChanged(this, descriptor,
					ActionSetManager.CHANGE_UNMASK);
		}
	}

	/**
	 * activate.
	 */
	protected void onActivate() {
		// Update editor area state.

	}

	/**
	 * deactivate.
	 */
	protected void onDeactivate() {
		presentation.deactivate();
		setActiveFastView(null);
		setAllPinsVisible(false);

		// Update fast views.
		if (fastViewManager != null) {
			List fastViews = fastViewManager.getFastViews(null);
			for (int i = 0; i < fastViews.size(); i++) {
				ViewPane pane = getPane((IViewReference) fastViews.get(i));
				if (pane != null) {
					Control ctrl = pane.getControl();
					if (ctrl != null) {
						ctrl.setEnabled(true); // Add focus support.
					}
				}
			}

			fastViewManager.deActivate();
		}

		// Ensure that the editor area trim is hidden as well
		setEditorAreaTrimVisibility(false);
	}

	/**
	 * Notifies that a part has been activated.
	 */
	public void partActivated(IWorkbenchPart activePart) {
		// If a fastview is open close it.
		if (activeFastView != null
				&& activeFastView.getPart(false) != activePart) {
			setActiveFastView(null);
		}
	}

	/**
	 * The user successfully performed a Show In... action on the specified
	 * part. Update the history.
	 */
	public void performedShowIn(String partId) {
		showInTimes.put(partId, new Long(System.currentTimeMillis()));
	}

	/**
	 * Sets the fast view attribute. Note: The page is expected to update action
	 * bars.
	 */
	public void removeFastView(IViewReference ref) {
		removeFastView(ref, true);
	}

	/**
	 * Sets the fast view attribute. Note: The page is expected to update action
	 * bars.
	 */
	public void removeFastView(IViewReference ref, boolean handleLayout) {
		ViewPane pane = getPane(ref);

		if (activeFastView == ref) {
			setActiveFastView(null);
		}

		pane.setFast(false);
		Control ctrl = pane.getControl();
		if (ctrl != null) {
			ctrl.setEnabled(true); // Modify focus support.
		}

		if (handleLayout) {
			// We are disabling the pane because it will be enabled when it
			// is added to the presentation. When a pane is enabled a drop
			// listener is added to it, and we do not want to have multiple
			// listeners for a pane
			presentation.addPart(pane);
		}
	}

	/**
	 * Fills a presentation with layout data. Note: This method should not
	 * modify the current state of the perspective.
	 */
	public IStatus restoreState(IMemento memento) {
		MultiStatus result = new MultiStatus(PlatformUI.PLUGIN_ID, IStatus.OK,
				WorkbenchMessages.Perspective_problemsRestoringPerspective,
				null);

		// Create persp descriptor.
		descriptor = new PerspectiveDescriptor(null, null, null);
		result.add(descriptor.restoreState(memento));
		PerspectiveDescriptor desc = (PerspectiveDescriptor) WorkbenchPlugin
				.getDefault().getPerspectiveRegistry().findPerspectiveWithId(
						descriptor.getId());
		if (desc != null) {
			descriptor = desc;
		}

		this.memento = memento;
		// Add the visible views.
		IMemento views[] = memento.getChildren(IWorkbenchConstants.TAG_VIEW);
		result.merge(createReferences(views));

		memento = memento.getChild(IWorkbenchConstants.TAG_FAST_VIEWS);
		if (memento != null) {
			views = memento.getChildren(IWorkbenchConstants.TAG_VIEW);
			result.merge(createReferences(views));
		}
		return result;
	}

	IStatus createReferences(IMemento views[]) {
		MultiStatus result = new MultiStatus(PlatformUI.PLUGIN_ID, IStatus.OK,
				WorkbenchMessages.Perspective_problemsRestoringViews, null);

		for (int x = 0; x < views.length; x++) {
			// Get the view details.
			IMemento childMem = views[x];
			String id = childMem.getString(IWorkbenchConstants.TAG_ID);
			// skip creation of the intro reference - it's handled elsewhere.
			if (id.equals(IIntroConstants.INTRO_VIEW_ID)) {
				continue;
			}

			String secondaryId = ViewFactory.extractSecondaryId(id);
			if (secondaryId != null) {
				id = ViewFactory.extractPrimaryId(id);
			}
			// Create and open the view.
			try {
				if (!"true".equals(childMem.getString(IWorkbenchConstants.TAG_REMOVED))) { //$NON-NLS-1$
					viewFactory.createView(id, secondaryId);
				}
			} catch (PartInitException e) {
				childMem.putString(IWorkbenchConstants.TAG_REMOVED, "true"); //$NON-NLS-1$
				result.add(StatusUtil.newStatus(IStatus.ERROR,
						e.getMessage() == null ? "" : e.getMessage(), //$NON-NLS-1$
						e));
			}
		}
		return result;
	}

	/**
	 * Fills a presentation with layout data. Note: This method should not
	 * modify the current state of the perspective.
	 */
	public IStatus restoreState() {
		if (this.memento == null) {
			return new Status(IStatus.OK, PlatformUI.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
		}

		MultiStatus result = new MultiStatus(PlatformUI.PLUGIN_ID, IStatus.OK,
				WorkbenchMessages.Perspective_problemsRestoringPerspective,
				null);

		IMemento memento = this.memento;
		this.memento = null;

		final IMemento boundsMem = memento
				.getChild(IWorkbenchConstants.TAG_WINDOW);
		if (boundsMem != null) {
			final Rectangle r = new Rectangle(0, 0, 0, 0);
			r.x = boundsMem.getInteger(IWorkbenchConstants.TAG_X).intValue();
			r.y = boundsMem.getInteger(IWorkbenchConstants.TAG_Y).intValue();
			r.height = boundsMem.getInteger(IWorkbenchConstants.TAG_HEIGHT)
					.intValue();
			r.width = boundsMem.getInteger(IWorkbenchConstants.TAG_WIDTH)
					.intValue();
			StartupThreading.runWithoutExceptions(new StartupRunnable() {

				public void runWithException() throws Throwable {
					if (page.getWorkbenchWindow().getPages().length == 0) {
						page.getWorkbenchWindow().getShell().setBounds(r);
					}
				}
			});

		}

		// Create an empty presentation..
		final PerspectiveHelper[] presArray = new PerspectiveHelper[1];
		StartupThreading.runWithoutExceptions(new StartupRunnable() {

			public void runWithException() throws Throwable {
				ViewSashContainer mainLayout = new ViewSashContainer(page,
						getClientComposite());
				presArray[0] = new PerspectiveHelper(page, mainLayout,
						Perspective.this);
			}
		});
		final PerspectiveHelper pres = presArray[0];

		// Read the layout.
		result.merge(pres.restoreState(memento
				.getChild(IWorkbenchConstants.TAG_LAYOUT)));

		StartupThreading.runWithoutExceptions(new StartupRunnable() {

			public void runWithException() throws Throwable {
				// Add the editor workbook. Do not hide it now.
				pres.replacePlaceholderWithPart(editorArea);
			}
		});

		// Add the visible views.
		IMemento[] views = memento.getChildren(IWorkbenchConstants.TAG_VIEW);

		for (int x = 0; x < views.length; x++) {
			// Get the view details.
			IMemento childMem = views[x];
			String id = childMem.getString(IWorkbenchConstants.TAG_ID);
			String secondaryId = ViewFactory.extractSecondaryId(id);
			if (secondaryId != null) {
				id = ViewFactory.extractPrimaryId(id);
			}

			// skip the intro as it is restored higher up in workbench.
			if (id.equals(IIntroConstants.INTRO_VIEW_ID)) {
				continue;
			}

			// Create and open the view.
			IViewReference viewRef = viewFactory.getView(id, secondaryId);
			WorkbenchPartReference ref = (WorkbenchPartReference) viewRef;

			// report error
			if (ref == null) {
				String key = ViewFactory.getKey(id, secondaryId);
				result.add(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0,
						NLS.bind(WorkbenchMessages.Perspective_couldNotFind,
								key), null));
				continue;
			}
			boolean willPartBeVisible = pres.willPartBeVisible(ref.getId(),
					secondaryId);
			if (willPartBeVisible) {
				IViewPart view = (IViewPart) ref.getPart(true);
				if (view != null) {
					ViewSite site = (ViewSite) view.getSite();
					ViewPane pane = (ViewPane) site.getPane();
					pres.replacePlaceholderWithPart(pane);
				}
			} else {
				pres.replacePlaceholderWithPart(ref.getPane());
			}
		}

		// Load the fast views
		if (fastViewManager != null)
			fastViewManager.restoreState(memento, result);

		// Load the view layout recs
		IMemento[] recMementos = memento
				.getChildren(IWorkbenchConstants.TAG_VIEW_LAYOUT_REC);
		for (int i = 0; i < recMementos.length; i++) {
			IMemento recMemento = recMementos[i];
			String compoundId = recMemento
					.getString(IWorkbenchConstants.TAG_ID);
			if (compoundId != null) {
				ViewLayoutRec rec = getViewLayoutRec(compoundId, true);
				if (IWorkbenchConstants.FALSE.equals(recMemento
						.getString(IWorkbenchConstants.TAG_CLOSEABLE))) {
					rec.isCloseable = false;
				}
				if (IWorkbenchConstants.FALSE.equals(recMemento
						.getString(IWorkbenchConstants.TAG_MOVEABLE))) {
					rec.isMoveable = false;
				}
				if (IWorkbenchConstants.TRUE.equals(recMemento
						.getString(IWorkbenchConstants.TAG_STANDALONE))) {
					rec.isStandalone = true;
					rec.showTitle = !IWorkbenchConstants.FALSE
							.equals(recMemento
									.getString(IWorkbenchConstants.TAG_SHOW_TITLE));
				}
			}
		}

		final IContextService service = (IContextService) page
				.getWorkbenchWindow().getService(IContextService.class);
		try { // one big try block, don't kill me here
			// defer context events
			if (service != null) {
				service.deferUpdates(true);
			}

			HashSet knownActionSetIds = new HashSet();

			// Load the always on action sets.
			IMemento[] actions = memento
					.getChildren(IWorkbenchConstants.TAG_ALWAYS_ON_ACTION_SET);
			for (int x = 0; x < actions.length; x++) {
				String actionSetID = actions[x]
						.getString(IWorkbenchConstants.TAG_ID);
				final IActionSetDescriptor d = WorkbenchPlugin.getDefault()
						.getActionSetRegistry().findActionSet(actionSetID);
				if (d != null) {
					StartupThreading
							.runWithoutExceptions(new StartupRunnable() {
								public void runWithException() throws Throwable {
									addAlwaysOn(d);
								}
							});

					knownActionSetIds.add(actionSetID);
				}
			}

			// Load the always off action sets.
			actions = memento
					.getChildren(IWorkbenchConstants.TAG_ALWAYS_OFF_ACTION_SET);
			for (int x = 0; x < actions.length; x++) {
				String actionSetID = actions[x]
						.getString(IWorkbenchConstants.TAG_ID);
				final IActionSetDescriptor d = WorkbenchPlugin.getDefault()
						.getActionSetRegistry().findActionSet(actionSetID);
				if (d != null) {
					StartupThreading
							.runWithoutExceptions(new StartupRunnable() {
								public void runWithException() throws Throwable {
									addAlwaysOff(d);
								}
							});
					knownActionSetIds.add(actionSetID);
				}
			}

			// Load "show view actions".
			actions = memento
					.getChildren(IWorkbenchConstants.TAG_SHOW_VIEW_ACTION);
			showViewShortcuts = new ArrayList(actions.length);
			for (int x = 0; x < actions.length; x++) {
				String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
				showViewShortcuts.add(id);
			}

			// Load "show in times".
			actions = memento.getChildren(IWorkbenchConstants.TAG_SHOW_IN_TIME);
			for (int x = 0; x < actions.length; x++) {
				String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
				String timeStr = actions[x]
						.getString(IWorkbenchConstants.TAG_TIME);
				if (id != null && timeStr != null) {
					try {
						long time = Long.parseLong(timeStr);
						showInTimes.put(id, new Long(time));
					} catch (NumberFormatException e) {
						// skip this one
					}
				}
			}

			// Load "show in parts" from registry, not memento
			showInPartIds = getShowInIdsFromRegistry();

			// Load "new wizard actions".
			actions = memento
					.getChildren(IWorkbenchConstants.TAG_NEW_WIZARD_ACTION);
			newWizardShortcuts = new ArrayList(actions.length);
			for (int x = 0; x < actions.length; x++) {
				String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
				newWizardShortcuts.add(id);
			}

			// Load "perspective actions".
			actions = memento
					.getChildren(IWorkbenchConstants.TAG_PERSPECTIVE_ACTION);
			perspectiveShortcuts = new ArrayList(actions.length);
			for (int x = 0; x < actions.length; x++) {
				String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
				perspectiveShortcuts.add(id);
			}

			// Load hidden menu item ids
			actions = memento.getChildren(IWorkbenchConstants.TAG_HIDE_MENU);
			hideMenuIDs = new HashSet();
			for (int x = 0; x < actions.length; x++) {
				String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
				hideMenuIDs.add(id);
			}
			actions = memento.getChildren(IWorkbenchConstants.TAG_HIDE_TOOLBAR);
			hideToolBarIDs = new HashSet();
			for (int x = 0; x < actions.length; x++) {
				String id = actions[x].getString(IWorkbenchConstants.TAG_ID);
				hideToolBarIDs.add(id);
			}

			ArrayList extActionSets = getPerspectiveExtensionActionSets();
			for (int i = 0; i < extActionSets.size(); i++) {
				String actionSetID = (String) extActionSets.get(i);
				if (knownActionSetIds.contains(actionSetID)) {
					continue;
				}
				final IActionSetDescriptor d = WorkbenchPlugin.getDefault()
						.getActionSetRegistry().findActionSet(actionSetID);
				if (d != null) {
					StartupThreading
							.runWithoutExceptions(new StartupRunnable() {
								public void runWithException() throws Throwable {
									addAlwaysOn(d);
								}
							});
					knownActionSetIds.add(d.getId());
				}
			}

			// Add the visible set of action sets to our knownActionSetIds
			// Now go through the registry to ensure we pick up any new action
			// sets
			// that have been added but not yet considered by this perspective.
			ActionSetRegistry reg = WorkbenchPlugin.getDefault()
					.getActionSetRegistry();
			IActionSetDescriptor[] array = reg.getActionSets();
			int count = array.length;
			for (int i = 0; i < count; i++) {
				IActionSetDescriptor desc = array[i];
				if ((!knownActionSetIds.contains(desc.getId()))
						&& (desc.isInitiallyVisible())) {
					addActionSet(desc);
				}
			}
		} finally {
			// restart context changes
			if (service != null) {
				StartupThreading.runWithoutExceptions(new StartupRunnable() {
					public void runWithException() throws Throwable {
						service.deferUpdates(false);
					}
				});
			}
		}

		// Save presentation.
		presentation = pres;

		// Hide the editor area if needed. Need to wait for the
		// presentation to be fully setup first.
		Integer areaVisible = memento
				.getInteger(IWorkbenchConstants.TAG_AREA_VISIBLE);
		// Rather than hiding the editors now we must wait until after their
		// controls
		// are created. This ensures that if an editor is instantiated,
		// createPartControl
		// is also called. See bug 20166.
		shouldHideEditorsOnActivate = (areaVisible != null && areaVisible
				.intValue() == 0);

		// Restore the trim state of the editor area
		IPreferenceStore preferenceStore = PrefUtil.getAPIPreferenceStore();
		boolean useNewMinMax = preferenceStore
				.getBoolean(IWorkbenchPreferenceConstants.ENABLE_NEW_MIN_MAX);
		if (useNewMinMax) {
			Integer trimStateInt = memento
					.getInteger(IWorkbenchConstants.TAG_AREA_TRIM_STATE);
			if (trimStateInt != null) {
				editorAreaState = trimStateInt.intValue() & 0x3; // low order
				// two bits
				// contain
				// the state
				editorAreaRestoreOnUnzoom = (trimStateInt.intValue() & 4) != 0;
			}
		}

		// restore the fixed state
		Integer isFixed = memento.getInteger(IWorkbenchConstants.TAG_FIXED);
		fixed = (isFixed != null && isFixed.intValue() == 1);

		return result;
	}

	/**
	 * Restores a fast view to its corrent presentation structure. This method
	 * is pubilc because the FastViewManager uses it to reconstruct it minimized
	 * stacks on startup.
	 * 
	 * @param fvMemento
	 *            The mement containing the fast view info
	 * @param result
	 *            The result status
	 * @return The reference to the restored view
	 */
	public IViewReference restoreFastView(IMemento fvMemento, MultiStatus result) {
		String viewID = fvMemento.getString(IWorkbenchConstants.TAG_ID);
		String secondaryId = ViewFactory.extractSecondaryId(viewID);
		if (secondaryId != null) {
			viewID = ViewFactory.extractPrimaryId(viewID);
		}

		IViewReference viewRef = getViewFactory().getView(viewID, secondaryId);
		if (viewRef == null) {
			String key = ViewFactory.getKey(viewID, secondaryId);
			WorkbenchPlugin.log("Could not create view: '" + key + "'."); //$NON-NLS-1$ //$NON-NLS-2$
			result.add(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0, NLS
					.bind(WorkbenchMessages.Perspective_couldNotFind, key),
					null));
			return null;
		}

		// Restore fast view width ratio
		Float ratio = fvMemento.getFloat(IWorkbenchConstants.TAG_RATIO);
		if (ratio == null) {
			Integer viewWidth = fvMemento
					.getInteger(IWorkbenchConstants.TAG_WIDTH);
			if (viewWidth == null) {
				ratio = new Float(IPageLayout.DEFAULT_FASTVIEW_RATIO);
			} else {
				ratio = new Float((float) viewWidth.intValue()
						/ (float) getClientComposite().getSize().x);
			}
		}
		ViewLayoutRec rec = getViewLayoutRec(viewRef, true);
		rec.fastViewWidthRatio = ratio.floatValue();

		return viewRef;
	}

	/**
	 * Returns the ActionSets read from perspectiveExtensions in the registry.
	 */
	protected ArrayList getPerspectiveExtensionActionSets() {
		PerspectiveExtensionReader reader = new PerspectiveExtensionReader();
		reader
				.setIncludeOnlyTags(new String[] { IWorkbenchRegistryConstants.TAG_ACTION_SET });
		PageLayout layout = new PageLayout();
		reader.extendLayout(null, descriptor.getOriginalId(), layout);
		return layout.getActionSets();
	}

	/**
	 * Returns the Show In... part ids read from the registry.
	 */
	protected ArrayList getShowInIdsFromRegistry() {
		PerspectiveExtensionReader reader = new PerspectiveExtensionReader();
		reader
				.setIncludeOnlyTags(new String[] { IWorkbenchRegistryConstants.TAG_SHOW_IN_PART });
		PageLayout layout = new PageLayout();
		reader.extendLayout(null, descriptor.getOriginalId(), layout);
		return layout.getShowInPartIds();
	}

	/**
	 * Save the layout.
	 */
	public void saveDesc() {
		saveDescAs(descriptor);
	}

	/**
	 * Save the layout.
	 */
	public void saveDescAs(IPerspectiveDescriptor desc) {
		PerspectiveDescriptor realDesc = (PerspectiveDescriptor) desc;
		// get the layout from the registry
		PerspectiveRegistry perspRegistry = (PerspectiveRegistry) WorkbenchPlugin
				.getDefault().getPerspectiveRegistry();
		// Capture the layout state.
		XMLMemento memento = XMLMemento.createWriteRoot("perspective");//$NON-NLS-1$
		IStatus status = saveState(memento, realDesc, false);
		if (status.getSeverity() == IStatus.ERROR) {
			ErrorDialog.openError((Shell) null,
					WorkbenchMessages.Perspective_problemSavingTitle,
					WorkbenchMessages.Perspective_problemSavingMessage, status);
			return;
		}
		// save it to the preference store
		try {
			perspRegistry.saveCustomPersp(realDesc, memento);
			descriptor = realDesc;
		} catch (IOException e) {
			perspRegistry.deletePerspective(realDesc);
			MessageDialog.openError((Shell) null,
					WorkbenchMessages.Perspective_problemSavingTitle,
					WorkbenchMessages.Perspective_problemSavingMessage);
		}
	}

	/**
	 * Save the layout.
	 */
	public IStatus saveState(IMemento memento) {
		MultiStatus result = new MultiStatus(PlatformUI.PLUGIN_ID, IStatus.OK,
				WorkbenchMessages.Perspective_problemsSavingPerspective, null);

		result.merge(saveState(memento, descriptor, true));

		return result;
	}

	/**
	 * Save the layout.
	 */
	private IStatus saveState(IMemento memento, PerspectiveDescriptor p,
			boolean saveInnerViewState) {
		MultiStatus result = new MultiStatus(PlatformUI.PLUGIN_ID, IStatus.OK,
				WorkbenchMessages.Perspective_problemsSavingPerspective, null);

		if (this.memento != null) {
			memento.putMemento(this.memento);
			return result;
		}

		// Save the version number.
		memento.putString(IWorkbenchConstants.TAG_VERSION, VERSION_STRING);
		result.add(p.saveState(memento));
		if (!saveInnerViewState) {
			Rectangle bounds = page.getWorkbenchWindow().getShell().getBounds();
			IMemento boundsMem = memento
					.createChild(IWorkbenchConstants.TAG_WINDOW);
			boundsMem.putInteger(IWorkbenchConstants.TAG_X, bounds.x);
			boundsMem.putInteger(IWorkbenchConstants.TAG_Y, bounds.y);
			boundsMem.putInteger(IWorkbenchConstants.TAG_HEIGHT, bounds.height);
			boundsMem.putInteger(IWorkbenchConstants.TAG_WIDTH, bounds.width);
		}

		// Save the "always on" action sets.
		Iterator itr = alwaysOnActionSets.iterator();
		while (itr.hasNext()) {
			IActionSetDescriptor desc = (IActionSetDescriptor) itr.next();
			IMemento child = memento
					.createChild(IWorkbenchConstants.TAG_ALWAYS_ON_ACTION_SET);
			child.putString(IWorkbenchConstants.TAG_ID, desc.getId());
		}

		// Save the "always off" action sets.
		itr = alwaysOffActionSets.iterator();
		while (itr.hasNext()) {
			IActionSetDescriptor desc = (IActionSetDescriptor) itr.next();
			IMemento child = memento
					.createChild(IWorkbenchConstants.TAG_ALWAYS_OFF_ACTION_SET);
			child.putString(IWorkbenchConstants.TAG_ID, desc.getId());
		}

		// Save "show view actions"
		itr = showViewShortcuts.iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			IMemento child = memento
					.createChild(IWorkbenchConstants.TAG_SHOW_VIEW_ACTION);
			child.putString(IWorkbenchConstants.TAG_ID, str);
		}

		// Save "show in times"
		itr = showInTimes.keySet().iterator();
		while (itr.hasNext()) {
			String id = (String) itr.next();
			Long time = (Long) showInTimes.get(id);
			IMemento child = memento
					.createChild(IWorkbenchConstants.TAG_SHOW_IN_TIME);
			child.putString(IWorkbenchConstants.TAG_ID, id);
			child.putString(IWorkbenchConstants.TAG_TIME, time.toString());
		}

		// Save "new wizard actions".
		itr = newWizardShortcuts.iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			IMemento child = memento
					.createChild(IWorkbenchConstants.TAG_NEW_WIZARD_ACTION);
			child.putString(IWorkbenchConstants.TAG_ID, str);
		}

		// Save "perspective actions".
		itr = perspectiveShortcuts.iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			IMemento child = memento
					.createChild(IWorkbenchConstants.TAG_PERSPECTIVE_ACTION);
			child.putString(IWorkbenchConstants.TAG_ID, str);
		}

		// Save hidden menu item ids
		itr = hideMenuIDs.iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			IMemento child = memento
					.createChild(IWorkbenchConstants.TAG_HIDE_MENU);
			child.putString(IWorkbenchConstants.TAG_ID, str);
		}
		itr = hideToolBarIDs.iterator();
		while (itr.hasNext()) {
			String str = (String) itr.next();
			IMemento child = memento
					.createChild(IWorkbenchConstants.TAG_HIDE_TOOLBAR);
			child.putString(IWorkbenchConstants.TAG_ID, str);
		}

		// Get visible views.
		List viewPanes = new ArrayList(5);
		presentation.collectViewPanes(viewPanes);

		// Save the views.
		itr = viewPanes.iterator();
		int errors = 0;
		while (itr.hasNext()) {
			ViewPane pane = (ViewPane) itr.next();
			IViewReference ref = pane.getViewReference();
			boolean restorable = page.getViewFactory().getViewRegistry().find(
					ref.getId()).isRestorable();
			if (restorable) {
				IMemento viewMemento = memento
						.createChild(IWorkbenchConstants.TAG_VIEW);
				viewMemento.putString(IWorkbenchConstants.TAG_ID, ViewFactory
						.getKey(ref));
			}
		}

		// save all fastview state
		if (fastViewManager != null)
			fastViewManager.saveState(memento);

		// Save the view layout recs.
		for (Iterator i = mapIDtoViewLayoutRec.keySet().iterator(); i.hasNext();) {
			String compoundId = (String) i.next();
			ViewLayoutRec rec = (ViewLayoutRec) mapIDtoViewLayoutRec
					.get(compoundId);
			if (rec != null
					&& (!rec.isCloseable || !rec.isMoveable || rec.isStandalone)) {
				IMemento layoutMemento = memento
						.createChild(IWorkbenchConstants.TAG_VIEW_LAYOUT_REC);
				layoutMemento.putString(IWorkbenchConstants.TAG_ID, compoundId);
				if (!rec.isCloseable) {
					layoutMemento.putString(IWorkbenchConstants.TAG_CLOSEABLE,
							IWorkbenchConstants.FALSE);
				}
				if (!rec.isMoveable) {
					layoutMemento.putString(IWorkbenchConstants.TAG_MOVEABLE,
							IWorkbenchConstants.FALSE);
				}
				if (rec.isStandalone) {
					layoutMemento.putString(IWorkbenchConstants.TAG_STANDALONE,
							IWorkbenchConstants.TRUE);
					layoutMemento.putString(IWorkbenchConstants.TAG_SHOW_TITLE,
							String.valueOf(rec.showTitle));
				}
			}
		}

		if (errors > 0) {
			String message = WorkbenchMessages.Perspective_multipleErrors;
			if (errors == 1) {
				message = WorkbenchMessages.Perspective_oneError;
			}
			MessageDialog.openError(null, WorkbenchMessages.Error, message);
		}

		// Save the layout.
		IMemento childMem = memento.createChild(IWorkbenchConstants.TAG_LAYOUT);
		result.add(presentation.saveState(childMem));

		// Save the editor visibility state
		if (isEditorAreaVisible()) {
			memento.putInteger(IWorkbenchConstants.TAG_AREA_VISIBLE, 1);
		} else {
			memento.putInteger(IWorkbenchConstants.TAG_AREA_VISIBLE, 0);
		}

		// Save the trim state of the editor area if using the new min/max
		IPreferenceStore preferenceStore = PrefUtil.getAPIPreferenceStore();
		boolean useNewMinMax = preferenceStore
				.getBoolean(IWorkbenchPreferenceConstants.ENABLE_NEW_MIN_MAX);
		if (useNewMinMax) {
			int trimState = editorAreaState;
			trimState |= editorAreaRestoreOnUnzoom ? 4 : 0;
			memento.putInteger(IWorkbenchConstants.TAG_AREA_TRIM_STATE,
					trimState);
		}

		// Save the fixed state
		if (fixed) {
			memento.putInteger(IWorkbenchConstants.TAG_FIXED, 1);
		} else {
			memento.putInteger(IWorkbenchConstants.TAG_FIXED, 0);
		}

		return result;
	}

	public void turnOnActionSets(IActionSetDescriptor[] newArray) {
		for (int i = 0; i < newArray.length; i++) {
			IActionSetDescriptor descriptor = newArray[i];

			addAlwaysOn(descriptor);
		}
	}

	public void turnOffActionSets(IActionSetDescriptor[] toDisable) {
		for (int i = 0; i < toDisable.length; i++) {
			IActionSetDescriptor descriptor = toDisable[i];

			turnOffActionSet(descriptor);
		}
	}

	public void turnOffActionSet(IActionSetDescriptor toDisable) {
		addAlwaysOff(toDisable);
	}

	/**
	 * Return the active fast view or null if there are no fast views or if
	 * there are all minimized.
	 */
	public IViewReference getActiveFastView() {
		return activeFastView;
	}

	/**
	 * Sets the active fast view. If a different fast view is already open, it
	 * shrinks equally <code>steps</code> times before disappearing completely.
	 * Then, <code>view</code> becomes active and is shown.
	 */
	/* package */void setActiveFastView(IViewReference ref, int steps) {
		if (activeFastView == ref) {
			return;
		}

		if (activeFastView != null) {
			ViewPane pane = getPane(activeFastView);
			if (pane != null) {
				if (pane.isZoomed()) {
					presentation.zoomOut();
				}
				hideFastView(activeFastView, steps);
			}
		}
		activeFastView = ref;
		try {
			if (activeFastView != null) {
				if (!showFastView(activeFastView)) {
					activeFastView = null;
				}
			}
		} catch (RuntimeException e) {
			activeFastView = null;
		}
	}

	/**
	 * Sets the active fast view.
	 */
	/* package */void setActiveFastView(IViewReference ref) {
		setActiveFastView(ref, FASTVIEW_HIDE_STEPS);
	}

	/**
	 * Sets the visibility of all fast view pins.
	 */
	protected void setAllPinsVisible(boolean visible) {
		if (fastViewManager == null)
			return;

		Iterator iter = fastViewManager.getFastViews(null).iterator();
		while (iter.hasNext()) {
			ViewPane pane = getPane((IViewReference) iter.next());
			if (pane != null) {
				pane.setFast(visible);
			}
		}
	}

	/**
	 * Sets the selection for the shortcut bar icon representing the givevn fast
	 * view.
	 */
	private void setFastViewIconSelection(IViewReference ref, boolean selected) {
		if (fastViewManager == null)
			return;

		fastViewManager.setFastViewIconSelection(ref, selected);
	}

	/**
	 * Sets the new wizard actions for the page. This is List of Strings.
	 */
	public void setNewWizardActionIds(ArrayList newList) {
		newWizardShortcuts = newList;
	}

	/**
	 * Sets the perspective actions for this page. This is List of Strings.
	 */
	public void setPerspectiveActionIds(ArrayList list) {
		perspectiveShortcuts = list;
	}

	/**
	 * Sets the ids of the parts to list in the Show In... prompter. This is a
	 * List of Strings.
	 */
	public void setShowInPartIds(ArrayList list) {
		showInPartIds = list;
	}

	/**
	 * Sets the ids of the views to list in the Show View shortcuts. This is a
	 * List of Strings.
	 */
	public void setShowViewActionIds(ArrayList list) {
		showViewShortcuts = list;
	}

	/**
	 * Show the editor area if not visible
	 */
	protected void showEditorArea() {
		if (isEditorAreaVisible()) {
			return;
		}

		editorHidden = false;

		// Show the editor in the appropriate location
		if (useNewMinMax(this)) {
			boolean isMinimized = editorAreaState == IStackPresentationSite.STATE_MINIMIZED;
			if (!isMinimized) {
				// If the editor area is going to show then we have to restore
				if (getPresentation().getMaximizedStack() != null)
					getPresentation().getMaximizedStack().setState(
							IStackPresentationSite.STATE_RESTORED);

				showEditorAreaLocal();
			} else
				setEditorAreaTrimVisibility(true);
		} else {
			showEditorAreaLocal();
		}
	}

	/**
	 * Show the editor area if not visible
	 */
	protected void showEditorAreaLocal() {
		if (editorHolder == null || editorHidden) {
			return;
		}

		// Replace the part holder with the editor area.
		presentation.getLayout().replace(editorHolder, editorArea);
		editorHolder = null;
	}

	private EditorAreaTrimToolBar getEditorAreaTrim(boolean createIfNecessary) {
		WorkbenchWindow wbw = (WorkbenchWindow) page.getWorkbenchWindow();
		ITrimManager tbm = wbw.getTrimManager();
		if (tbm == null)
			return null;

		// Create if necesary
		EditorAreaTrimToolBar editorAreaTrim = (EditorAreaTrimToolBar) tbm
				.getTrim(IPageLayout.ID_EDITOR_AREA);
		if (editorAreaTrim == null && createIfNecessary) {
			int suggestedSide = SWT.RIGHT;
			int cachedSide = ((TrimLayout) tbm)
					.getPreferredArea(IPageLayout.ID_EDITOR_AREA);
			if (cachedSide != -1)
				suggestedSide = cachedSide;

			IWindowTrim beforeMe = ((TrimLayout) tbm)
					.getPreferredLocation(IPageLayout.ID_EDITOR_AREA);

			// Gain access to the trim manager
			editorAreaTrim = new EditorAreaTrimToolBar(wbw, editorArea);
			editorAreaTrim.dock(suggestedSide);
			tbm.addTrim(suggestedSide, editorAreaTrim, beforeMe);
		}

		return editorAreaTrim;
	}

	public void setEditorAreaState(int newState) {
		if (newState == editorAreaState)
			return;

		editorAreaState = newState;

		// reset the restore flag if we're not minimized
		if (newState != IStackPresentationSite.STATE_MINIMIZED)
			editorAreaRestoreOnUnzoom = false;

		refreshEditorAreaVisibility();
	}

	public int getEditorAreaState() {
		return editorAreaState;
	}

	/**
	 * 
	 */
	public void refreshEditorAreaVisibility() {
		// Nothing shows up if the editor area isn't visible at all
		if (editorHidden) {
			hideEditorAreaLocal();
			setEditorAreaTrimVisibility(false);
			return;
		}

		EditorStack editorStack = ((EditorSashContainer) editorArea)
				.getUpperRightEditorStack(null);
		if (editorStack == null)
			return;

		// Whatever we're doing, make the current editor stack match it
		editorStack.setStateLocal(editorAreaState);

		// If it's minimized then it's in the trim
		if (editorAreaState == IStackPresentationSite.STATE_MINIMIZED) {
			// Hide the editor area and show its trim
			hideEditorAreaLocal();
			setEditorAreaTrimVisibility(true);
		} else {
			// Show the editor area and hide its trim
			setEditorAreaTrimVisibility(false);
			showEditorAreaLocal();

			if (editorAreaState == IStackPresentationSite.STATE_MAXIMIZED)
				getPresentation().setMaximizedStack(editorStack);
		}
	}

	protected EditorAreaTrimToolBar setEditorAreaTrimVisibility(boolean visible) {
		WorkbenchWindow wbw = (WorkbenchWindow) page.getWorkbenchWindow();
		ITrimManager tbm = wbw.getTrimManager();
		if (tbm == null)
			return null;

		// Only create the trim element if it's going to be visible
		EditorAreaTrimToolBar editorAreaTrim = getEditorAreaTrim(visible);
		if (editorAreaTrim == null)
			return null;

		tbm.setTrimVisible(editorAreaTrim, visible);
		tbm.forceLayout();

		return editorAreaTrim;
	}

	/**
	 * Shows a fast view.
	 * 
	 * @return whether the view was successfully shown
	 */
	boolean showFastView(IViewReference ref) {
		if (fastViewManager == null)
			return false;

		// Make sure the part is restored.
		IWorkbenchPart refPart = ref.getPart(true);
		if (refPart == null) {
			return false;
		}

		ViewPane pane = getPane(ref);
		if (pane == null) {
			return false;
		}

		saveFastViewWidthRatio();

		// Special check to ensure that a 'minimized' intro view shows
		// as 'standby'
		if (ref.getId().equals("org.eclipse.ui.internal.introview")) { //$NON-NLS-1$
			if (refPart instanceof ViewIntroAdapterPart) {
				((ViewIntroAdapterPart) refPart).setStandby(true);
			}
		}

		// Determine the display orientation
		int side = fastViewManager.getViewSide(ref);
		fastViewPane.showView(getClientComposite(), pane, side,
				getFastViewWidthRatio(ref));

		setFastViewIconSelection(ref, true);

		return true;
	}

	private void saveFastViewWidthRatio() {
		ViewPane pane = fastViewPane.getCurrentPane();
		if (pane != null) {
			ViewLayoutRec rec = getViewLayoutRec(pane.getViewReference(), true);
			rec.fastViewWidthRatio = fastViewPane.getCurrentRatio();
		}
	}

	/**
	 * Resolves a view's id into its reference, creating the view if necessary.
	 * 
	 * @param viewId
	 *            The primary id of the view (must not be <code>null</code>
	 * @param secondaryId
	 *            The secondary id of a multiple-instance view (may be
	 *            <code>null</code>).
	 * 
	 * @return The reference to the specified view. This may be null if the view
	 *         fails to create (i.e. thrown a PartInitException)
	 */
	public IViewReference getViewReference(String viewId, String secondaryId) {
		IViewReference ref = page.findViewReference(viewId, secondaryId);
		if (ref == null) {
			ViewFactory factory = getViewFactory();
			try {
				ref = factory.createView(viewId, secondaryId);
			} catch (PartInitException e) {
				IStatus status = StatusUtil.newStatus(IStatus.ERROR, e
						.getMessage() == null ? "" : e.getMessage(), //$NON-NLS-1$
						e);
				StatusUtil.handleStatus(status,
						"Failed to create view: id=" + viewId, //$NON-NLS-1$
						StatusManager.LOG);
			}
		}
		return ref;
	}

	/**
	 * Shows the view with the given id and secondary id.
	 */
	public IViewPart showView(String viewId, String secondaryId)
			throws PartInitException {
		// Is it already there?
		final MPerspective perspectiveModel = ((ModeledPageLayout) layout)
				.getModel();
		MPart part = ModeledPageLayout.findPart(perspectiveModel, viewId);

		// if not, add it
		MPart theStack = null;
		if (part == null) {
			// Place it in the 'bottom' stack
			theStack = ModeledPageLayout.findPart(perspectiveModel, "bottom"); //$NON-NLS-1$
			part = ModeledPageLayout.createViewModel(viewId, true);
			theStack.getChildren().add(part);
		} else {
			// Its stack is where it already is
			theStack = part.getParent();

			// create it if necessary
			if (!part.isVisible()) {
				// Move it to the end of the stack
				theStack.getChildren().remove(part);
				theStack.getChildren().add(part);
				part.setVisible(true);
			}
		}

		// OK, make it active
		theStack.setActiveChild(part);

		return (IViewPart) ((MContributedPart) part).getObject();
	}

	/**
	 * Toggles the visibility of a fast view. If the view is active it is
	 * deactivated. Otherwise, it is activated.
	 */
	public void toggleFastView(IViewReference ref) {
		if (ref == activeFastView) {
			setActiveFastView(null);
		} else {
			setActiveFastView(ref);
		}
	}

	/**
	 * Returns the old part reference. Returns null if there was no previously
	 * active part.
	 * 
	 * @return the old part reference or <code>null</code>
	 */
	public IWorkbenchPartReference getOldPartRef() {
		return oldPartRef;
	}

	/**
	 * Sets the old part reference.
	 * 
	 * @param oldPartRef
	 *            The old part reference to set, or <code>null</code>
	 */
	public void setOldPartRef(IWorkbenchPartReference oldPartRef) {
		this.oldPartRef = oldPartRef;
	}

	// for dynamic UI
	protected void addActionSet(IActionSetDescriptor newDesc) {
		IContextService service = (IContextService) page.getWorkbenchWindow()
				.getService(IContextService.class);
		try {
			service.deferUpdates(true);
			for (int i = 0; i < alwaysOnActionSets.size(); i++) {
				IActionSetDescriptor desc = (IActionSetDescriptor) alwaysOnActionSets
						.get(i);
				if (desc.getId().equals(newDesc.getId())) {
					removeAlwaysOn(desc);
					removeAlwaysOff(desc);
					break;
				}
			}
			addAlwaysOn(newDesc);
		} finally {
			service.deferUpdates(false);
		}
	}

	// for dynamic UI
	/* package */void removeActionSet(String id) {
		IContextService service = (IContextService) page.getWorkbenchWindow()
				.getService(IContextService.class);
		try {
			service.deferUpdates(true);
			for (int i = 0; i < alwaysOnActionSets.size(); i++) {
				IActionSetDescriptor desc = (IActionSetDescriptor) alwaysOnActionSets
						.get(i);
				if (desc.getId().equals(id)) {
					removeAlwaysOn(desc);
					break;
				}
			}

			for (int i = 0; i < alwaysOffActionSets.size(); i++) {
				IActionSetDescriptor desc = (IActionSetDescriptor) alwaysOffActionSets
						.get(i);
				if (desc.getId().equals(id)) {
					removeAlwaysOff(desc);
					break;
				}
			}
		} finally {
			service.deferUpdates(false);
		}
	}

	void removeActionSet(IActionSetDescriptor toRemove) {
		removeAlwaysOn(toRemove);
		removeAlwaysOff(toRemove);
	}

	public void setFastViewState(int newState) {
		fastViewPane.setState(newState);
	}

	public int getFastViewState() {
		return fastViewPane.getState();
	}

	/**
	 * Returns whether the given view is closeable in this perspective.
	 * 
	 * @since 3.0
	 */
	public boolean isCloseable(IViewReference reference) {
		ViewLayoutRec rec = getViewLayoutRec(reference, false);
		if (rec != null) {
			return rec.isCloseable;
		}
		return true;
	}

	/**
	 * Returns whether the given view is moveable in this perspective.
	 * 
	 * @since 3.0
	 */
	public boolean isMoveable(IViewReference reference) {
		ViewLayoutRec rec = getViewLayoutRec(reference, false);
		if (rec != null) {
			return rec.isMoveable;
		}
		return true;
	}

	/**
	 * Writes a description of the layout to the given string buffer. This is
	 * used for drag-drop test suites to determine if two layouts are the same.
	 * Like a hash code, the description should compare as equal iff the layouts
	 * are the same. However, it should be user-readable in order to help debug
	 * failed tests. Although these are english readable strings, they should
	 * not be translated or equality tests will fail.
	 * <p>
	 * This is only intended for use by test suites.
	 * </p>
	 * 
	 * @param buf
	 */
	public void describeLayout(StringBuffer buf) {
		IViewReference[] fastViews = getFastViews();

		if (fastViews.length != 0) {
			buf.append("fastviews ("); //$NON-NLS-1$
			for (int idx = 0; idx < fastViews.length; idx++) {
				IViewReference ref = fastViews[idx];

				if (idx > 0) {
					buf.append(", "); //$NON-NLS-1$
				}

				buf.append(ref.getPartName());
			}
			buf.append("), "); //$NON-NLS-1$
		}

		getPresentation().describeLayout(buf);
	}

	/**
	 * Sanity-checks the LayoutParts in this perspective. Throws an Assertation
	 * exception if an object's internal state is invalid.
	 */
	public void testInvariants() {
		getPresentation().getLayout().testInvariants();
	}

	public IActionSetDescriptor[] getAlwaysOnActionSets() {
		return (IActionSetDescriptor[]) alwaysOnActionSets
				.toArray(new IActionSetDescriptor[alwaysOnActionSets.size()]);
	}

	public IActionSetDescriptor[] getAlwaysOffActionSets() {
		return (IActionSetDescriptor[]) alwaysOffActionSets
				.toArray(new IActionSetDescriptor[alwaysOffActionSets.size()]);
	}

	/* package */FastViewPane getFastViewPane() {
		return fastViewPane;
	}

	/**
	 * Restores a part in the trim to the actual layout
	 * 
	 * @param part
	 *            The part to restore
	 */
	public void restoreTrimPart(LayoutPart part) {
		if (fastViewManager == null)
			return;

		// Remove any current fastview
		setActiveFastView(null);

		// Set the part's state to place it back in the layout
		if (part instanceof ViewStack) {
			ViewStack vs = (ViewStack) part;
			fastViewManager.restoreToPresentation(vs.getID());
		}

		if (part == editorArea) {
			setEditorAreaState(IStackPresentationSite.STATE_RESTORED);
			editorAreaRestoreOnUnzoom = false;
		}
	}

	/**
	 * Determine the correct side to initially dock a new trim part on. We do
	 * this by checking its rect against the editor area.
	 * 
	 * @param stackBounds
	 *            The bounds of the stack we want to create trim for
	 * @return the SWT side to dock the trim element on
	 */
	public int calcStackSide(Rectangle stackBounds) {
		// Where is the stack in relation to the EditorArea?
		Rectangle editorAreaBounds = editorArea.getBounds();

		// Is this the Editor Area
		if (editorAreaBounds.equals(stackBounds))
			return SWT.TOP;

		Point stackCenter = Geometry.centerPoint(stackBounds);
		Point editorAreaCenter = Geometry.centerPoint(editorAreaBounds);

		int dx = editorAreaCenter.x - stackCenter.x;
		int dy = editorAreaCenter.y - stackCenter.y;

		if (Math.abs(dx) > Math.abs(dy)) {
			return (dx > 0) ? SWT.LEFT : SWT.RIGHT;
		}

		if (dy > 0) {
			return (dx > 0) ? SWT.LEFT : SWT.RIGHT;
		}

		return SWT.BOTTOM;
	}

	/**
	 * Restore any parts that are showing in the trim as a result of a 'zoom'
	 * operation
	 */
	public void restoreZoomedParts() {
		if (fastViewManager == null)
			return;

		// Remove any current fastview
		setActiveFastView(null);

		// have the layout restore the parts
		fastViewManager.restoreZoomedViewStacks();

		if (editorAreaRestoreOnUnzoom) {
			restoreTrimPart(editorArea);
		}
	}

	/**
	 * @return Returns the fastViewManager.
	 */
	public FastViewManager getFastViewManager() {
		return fastViewManager;
	}

	/**
	 * Sets the restore on unzoom state for the editor area
	 * 
	 * @param restore
	 *            the new state
	 */
	public void setEditorAreaRestoreOnUnzoom(boolean restore) {
		editorAreaRestoreOnUnzoom = restore;
	}

	/**
	 * @return the restore on unzoom state
	 */
	public boolean getEditorAreaRestoreOnUnzoom() {
		return editorAreaRestoreOnUnzoom;
	}

	/**
	 * Used to restrict the use of the new min/max behavior to envoronments in
	 * which it has a chance of working...
	 * 
	 * @param activePerspective
	 *            We pass this in as an arg so others won't have to check it for
	 *            'null' (which is one of the failure cases)
	 * 
	 */
	public static boolean useNewMinMax(Perspective activePerspective) {
		// We need to have an active perspective
		if (activePerspective == null)
			return false;

		// We need to have a trim manager (if we don't then we
		// don't create a FastViewManager because it'd be useless)
		if (activePerspective.getFastViewManager() == null)
			return false;

		// Make sure we don't NPE anyplace
		WorkbenchWindow wbw = (WorkbenchWindow) activePerspective.page
				.getWorkbenchWindow();
		if (wbw == null)
			return false;

		WorkbenchWindowConfigurer configurer = wbw.getWindowConfigurer();
		if (configurer == null)
			return false;

		AbstractPresentationFactory factory = configurer
				.getPresentationFactory();
		if (factory == null)
			return false;

		// Ok, we should be good to go, return the pref
		IPreferenceStore preferenceStore = PrefUtil.getAPIPreferenceStore();
		boolean useNewMinMax = preferenceStore
				.getBoolean(IWorkbenchPreferenceConstants.ENABLE_NEW_MIN_MAX);
		return useNewMinMax;
	}

	/** @return a Collection of IDs of items to be hidden from the menu bar */
	public Collection getHiddenMenuItems() {
		return hideMenuIDs;
	}

	/** @return a Collection of IDs of items to be hidden from the tool bar */
	public Collection getHiddenToolbarItems() {
		return hideToolBarIDs;
	}

	public void updateActionBars() {
		page.getActionBars().getMenuManager().updateAll(true);
		page.resetToolBarLayout();
	}

}
