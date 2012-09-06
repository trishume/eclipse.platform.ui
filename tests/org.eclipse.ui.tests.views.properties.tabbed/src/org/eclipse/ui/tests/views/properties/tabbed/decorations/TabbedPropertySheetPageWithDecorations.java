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

package org.eclipse.ui.tests.views.properties.tabbed.decorations;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyComposite;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList;
import org.eclipse.ui.views.properties.tabbed.ITabDescriptor;
import org.eclipse.ui.views.properties.tabbed.ITabItem;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class TabbedPropertySheetPageWithDecorations extends
		TabbedPropertySheetPage {

	private boolean useDecorations;

	public TabbedPropertySheetPageWithDecorations(
			ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor) {
		super(tabbedPropertySheetPageContributor);
	}

	protected void updateTabs(ITabDescriptor[] descriptors) {
		super.updateTabs(descriptors);
		if (useDecorations) {
			// Set the number of decoration-images in the TabbedPropertyList
			TabbedPropertyList tabbedPropertyList = ((TabbedPropertyComposite) this
					.getControl()).getList();
			Map tabToImageDecorationsMap = getImageDecorationsForTabs(descriptors);
			tabbedPropertyList.setDynamicImageCount(tabToImageDecorationsMap);
		}
	}

	private Map getImageDecorationsForTabs(ITabItem[] tabItems) {
		Map tabToImageDecorationsMap = new HashMap();
		for (int i = 0; i < tabItems.length; i++) {
			if (tabItems[i].getText().equals("Name")) {
				tabToImageDecorationsMap.put(tabItems[i], new Integer(5));
			} else if (tabItems[i].getText().equals("Message")) {
				tabToImageDecorationsMap.put(tabItems[i], new Integer(3));
			} else {
				tabToImageDecorationsMap.put(tabItems[i], new Integer(0));
			}
		}
		return tabToImageDecorationsMap;
	}

	public void useDecorations(boolean value) {
		this.useDecorations = value;
	}
}
