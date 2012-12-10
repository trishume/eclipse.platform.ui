/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.e4.ui.workbench.renderers.swt;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.AboutToHide;
import org.eclipse.e4.ui.model.application.ui.menu.MDynamicMenuContribution;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.swt.factories.IRendererFactory;
import org.eclipse.jface.action.IMenuListener2;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;

/**
 * <code>MenuManagerHideProcessor</code> provides hooks for renderer processing
 * before and after the <code>MenuManager</code> calls out to its
 * <code>IMenuManagerListener2</code> for the <code>menuAboutToHide</code>
 * events.
 */
public class MenuManagerHideProcessor implements IMenuListener2 {

	@Inject
	private EModelService modelService;

	@Inject
	private IRendererFactory rendererFactory;

	@Inject
	private MenuManagerRenderer renderer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface
	 * .action.IMenuManager)
	 * 
	 * SWT.Hide pre-processing method for MenuManager
	 */
	public void menuAboutToShow(IMenuManager manager) {
		if (!(manager instanceof MenuManager)) {
			return;
		}
		MenuManager menuManager = (MenuManager) manager;
		final MMenu menuModel = renderer.getMenuModel(menuManager);
		final Menu menu = menuManager.getMenu();
		if (menuModel instanceof MPopupMenu) {
			hidePopup(menu, (MPopupMenu) menuModel, menuManager);
		}
		if (menuModel != null && menu != null)
			processDynamicElements(menu, menuModel);
	}

	/**
	 * Process dynamic menu contributions provided by
	 * {@link MDynamicMenuContribution} application model elements
	 * 
	 * @param menu
	 * @param menuModel
	 * 
	 */
	private void processDynamicElements(Menu menu, final MMenu menuModel) {
		if (!menu.isDisposed()) {
			menu.getDisplay().asyncExec(new Runnable() {
				public void run() {

					MMenuElement[] ml = menuModel.getChildren().toArray(
							new MMenuElement[menuModel.getChildren().size()]);
					for (int i = 0; i < ml.length; i++) {

						MMenuElement currentMenuElement = ml[i];
						if (currentMenuElement instanceof MDynamicMenuContribution) {
							Object contribution = ((MDynamicMenuContribution) currentMenuElement)
									.getObject();

							IEclipseContext dynamicMenuContext = EclipseContextFactory
									.create();
							@SuppressWarnings("unchecked")
							ArrayList<MMenuElement> mel = (ArrayList<MMenuElement>) currentMenuElement
									.getTransientData()
									.get(MenuManagerShowProcessor.DYNAMIC_ELEMENT_STORAGE_KEY);
							dynamicMenuContext.set(List.class, mel);
							ContextInjectionFactory.invoke(contribution,
									AboutToHide.class, dynamicMenuContext);
						}

					}

				}
			});
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.action.IMenuListener2#menuAboutToHide(org.eclipse.jface
	 * .action.IMenuManager)
	 */
	public void menuAboutToHide(IMenuManager manager) {
	}

	private void hidePopup(Menu menu, MPopupMenu menuModel,
			MenuManager menuManager) {
		final IEclipseContext popupContext = menuModel.getContext();
		final IEclipseContext originalChild = (IEclipseContext) popupContext
				.get(MenuManagerRendererFilter.TMP_ORIGINAL_CONTEXT);
		popupContext.remove(MenuManagerRendererFilter.TMP_ORIGINAL_CONTEXT);
		if (!menu.isDisposed()) {
			menu.getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (originalChild == null) {
						popupContext.deactivate();
					} else {
						originalChild.activate();
					}
				}
			});
		}
	}
}
