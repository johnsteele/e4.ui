/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package org.eclipse.e4.demo.simpleide.internal;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;

import javax.inject.Inject;

import java.util.List;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.IThemeManager;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class ThemeMenuProcessor {

	@Inject
	@Named("simpleide.mainmenu")
	private MMenu menu;
	
	@Execute
	public void process() {
		MTrimmedWindow window = (MTrimmedWindow) ((EObject) menu).eContainer();
		
		//FIXME Remove once bug 314091 is resolved
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		BundleContext context = bundle.getBundleContext();
		
		ServiceReference reference = context.getServiceReference(IThemeManager.class.getName());
		IThemeManager mgr = (IThemeManager) context.getService(reference);
		IThemeEngine engine = mgr.getEngineForDisplay(Display.getCurrent());
		
		List<ITheme> themes = engine.getThemes();
		if (themes.size() > 0) {
			MApplication application = (MApplication) ((EObject) window)
					.eContainer();
			MCommand switchThemeCommand = null;
			for (MCommand cmd : application.getCommands()) {
				if ("simpleide.command.switchtheme".equals(cmd.getElementId())) { //$NON-NLS-1$
					switchThemeCommand = cmd;
					break;
				}
			}

			if (switchThemeCommand != null) {
				MMenu themesMenu = MMenuFactory.INSTANCE.createMenu();
				themesMenu.setLabel("Themes"); //$NON-NLS-1$

				for (ITheme theme : themes) {
					MHandledMenuItem item = MMenuFactory.INSTANCE
							.createHandledMenuItem();
					item.setLabel(theme.getLabel());
					item.setCommand(switchThemeCommand);
					MParameter parameter = MCommandsFactory.INSTANCE
							.createParameter();
					parameter.setName("simpleide.command.switchtheme.themeid"); //$NON-NLS-1$
					parameter.setValue(theme.getId());
					item.getParameters().add(parameter);
					themesMenu.getChildren().add(item);
				}
				
				menu.getChildren().add(themesMenu);
			}
		}
	}

}
