package org.eclipse.ui.tweaklets.dependencyinjection;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.window.IShellProvider;

/**
 * Saveable object.
 * 
 * @since 3.4
 * 
 */
public interface ISaveable {

	/**
	 * Returns an observable value of type boolean whose value is true when this
	 * saveable is dirty.
	 * 
	 * @return
	 */
	public IObservableValue getDirty();

	/**
	 * Saves this saveable. This method is called in the UI thread.
	 * <p>
	 * The supplied shell provider can be used from within this method. Care
	 * should be taken not to open dialogs gratuitously and only if user input
	 * is required for cases where the save cannot otherwise proceed - note that
	 * in any given save operation, many saveable objects may be saved at the
	 * same time. In particular, errors should be signaled by throwing an
	 * exception.
	 * </p>
	 * <p>
	 * If the save is cancelled through user action, or for any other reason,
	 * the part should invoke <code>setCancelled</code> on the
	 * <code>IProgressMonitor</code> to inform the caller.
	 * </p>
	 * <p>
	 * This method is long-running; progress and cancellation are provided by
	 * the given progress monitor.
	 * </p>
	 * 
	 * @param monitor
	 *            a progress monitor used for reporting progress and
	 *            cancellation
	 * @param shellProvider
	 *            an object that can provide a shell for parenting dialogs
	 * 
	 * @throws CoreException
	 *             if the save operation failed
	 * 
	 */
	public void doSave(IProgressMonitor monitor, IShellProvider shellProvider)
			throws CoreException;

	/**
	 * Saves the contents of this saveable to another object.
	 * <p>
	 * Implementors are expected to open a "Save As" dialog where the user will
	 * be able to select a new name for the contents. After the selection is
	 * made, the contents should be saved to that new name. During this
	 * operation a <code>IProgressMonitor</code> should be used to indicate
	 * progress.
	 * </p>
	 * <p>
	 * If the save is successful, the dirty flag's value should be set to
	 * <code>false</code>.
	 * </p>
	 * 
	 * @param shellProvider
	 *            an object that can provide a shell for parenting dialogs
	 * @throws CoreException
	 *             if the save operation failed
	 */
	public void doSaveAs(IShellProvider shellProvider) throws CoreException;

    /**
     * Returns whether the "Save As" operation is supported by this saveable.
     *
     * @return <code>true</code> if "Save As" is supported, and <code>false</code>
     *  if not supported
     */
	public boolean isSaveAsAllowed();

}
