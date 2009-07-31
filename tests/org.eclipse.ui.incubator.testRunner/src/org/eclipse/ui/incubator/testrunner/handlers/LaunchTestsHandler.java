package org.eclipse.ui.incubator.testrunner.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.ui.incubator.testrunner.Activator;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class LaunchTestsHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public LaunchTestsHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		new TestsLauncherJob().schedule();
		return null;
	}

	class TestsLauncherJob extends Job {

		public TestsLauncherJob() {
			super("Tests Launcher Job");
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {

			ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
			ILaunchConfigurationType junitLaunch = launchManager.getLaunchConfigurationType("org.eclipse.pde.ui.JunitLaunchConfig");
			ILaunchConfiguration[] launchConfigurations;
			MultiStatus testStatus = new MultiStatus(Activator.PLUGIN_ID, 0, "Test results", null);
			try {
				launchConfigurations = launchManager.getLaunchConfigurations(junitLaunch);
				monitor.beginTask("Launching tests...", launchConfigurations.length);
				outer:
				for (ILaunchConfiguration iLaunchConfiguration : launchConfigurations) {
					try {
						ILaunch launch = iLaunchConfiguration.launch(ILaunchManager.RUN_MODE, null);
						monitor.subTask(iLaunchConfiguration.getName() + " running.");
						do {
							Thread.sleep(5 * 1000); // sleep for 5 secs
							if (monitor.isCanceled()) {
								if (launch.canTerminate()) {
									monitor.subTask("Terminating the launch");
									launch.terminate();
								}
								break outer;
							}
						} while (!launch.isTerminated());
						monitor.worked(1);
					} catch (Exception e) {
						IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error launching test: " + iLaunchConfiguration.getName(), e);
						testStatus.add(status);
					}
				}
			} catch (CoreException e) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error launching tests", e);
				testStatus.add(status);
			}
			return testStatus;
		}
	}
}
