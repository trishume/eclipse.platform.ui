/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.internal.menus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.MCoreExpression;
import org.eclipse.e4.ui.model.application.ui.impl.UiFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MRenderedMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.services.ServiceLocator;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.IMenuService;

public class ContributionFactoryGenerator extends ContextFunction {
	private AbstractContributionFactory factoryImpl;
	private IConfigurationElement configElement;

	public ContributionFactoryGenerator(AbstractContributionFactory factory) {
		this.factoryImpl = factory;
	}

	public ContributionFactoryGenerator(IConfigurationElement element) {
		configElement = element;
	}

	private AbstractContributionFactory getFactory() {
		if (factoryImpl == null && configElement != null) {
			try {
				factoryImpl = (AbstractContributionFactory) configElement
						.createExecutableExtension("class"); //$NON-NLS-1$
			} catch (CoreException e) {
				WorkbenchPlugin.log(e);
				return null;
			}
		}
		return factoryImpl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.core.contexts.ContextFunction#compute(org.eclipse.e4.core
	 * .contexts.IEclipseContext)
	 */
	@Override
	public Object compute(IEclipseContext context) {
		AbstractContributionFactory factory = getFactory();
		final IMenuService menuService = context.get(IMenuService.class);
		final ContributionRoot root = new ContributionRoot(menuService, new HashSet<Object>(),
				null, factory);
		ServiceLocator sl = new ServiceLocator();
		sl.setContext(context);
		factory.createContributionItems(sl, root);
		final List contributionItems = root.getItems();
		final Map<IContributionItem, Expression> itemsToExpression = root.getVisibleWhen();
		List<MMenuElement> menuElements = new ArrayList<MMenuElement>();
		for (Object obj : contributionItems) {
			if (obj instanceof IContributionItem) {
				IContributionItem ici = (IContributionItem) obj;
				MRenderedMenuItem renderedItem = MenuFactoryImpl.eINSTANCE.createRenderedMenuItem();
				renderedItem.setElementId(ici.getId());
				renderedItem.setContributionItem(ici);
				if (itemsToExpression.containsKey(ici)) {
					final Expression ex = itemsToExpression.get(ici);
					MCoreExpression exp = UiFactoryImpl.eINSTANCE.createCoreExpression();
					exp.setCoreExpressionId("programmatic." + ici.getId()); //$NON-NLS-1$
					exp.setCoreExpression(ex);
					renderedItem.setVisibleWhen(exp);
				}
				menuElements.add(renderedItem);
			}
		}
		context.set(List.class, menuElements);

		// return something disposable
		return new Runnable() {
			public void run() {
				root.release();
			}
		};
	}

}