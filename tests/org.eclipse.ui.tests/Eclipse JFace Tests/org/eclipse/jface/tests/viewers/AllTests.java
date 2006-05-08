/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.tests.viewers;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		return new AllTests();
	}

	public AllTests() {
		addTestSuite(TreeSelectionTest.class);
		addTestSuite(MultipleEqualElementsTreeViewerTest.class);
		addTestSuite(LazySortedCollectionTest.class);
		addTestSuite(TreeViewerTest.class);
		addTestSuite(VirtualTreeViewerTest.class);
		addTestSuite(SimpleTreeViewerTest.class);
		addTestSuite(SimpleVirtualLazyTreeViewerTest.class);
		addTestSuite(VirtualLazyTreeViewerTest.class);
		addTestSuite(TableViewerTest.class);
		addTestSuite(TreeViewerColumnTest.class);
		addTestSuite(VirtualTableViewerTest.class);
		addTestSuite(VirtualLazyTableViewerTest.class);
		addTestSuite(TableTreeViewerTest.class);
		addTestSuite(TableColorProviderTest.class);
		addTestSuite(TableFontProviderTest.class);
		addTestSuite(ListViewerTest.class);
		addTestSuite(CheckboxTableViewerTest.class);
		addTestSuite(CheckboxTreeViewerTest.class);
		addTestSuite(ComboViewerTest.class);
		addTestSuite(TreeViewerComparatorTest.class);
		addTestSuite(ListViewerComparatorTest.class);
		addTestSuite(TableViewerComparatorTest.class);
	}
}
