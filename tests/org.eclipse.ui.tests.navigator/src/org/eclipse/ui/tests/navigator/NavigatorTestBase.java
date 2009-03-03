/*******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tests.navigator;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.navigator.NavigatorFilterService;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.eclipse.ui.navigator.NavigatorActionService;
import org.eclipse.ui.tests.harness.util.EditorTestHelper;
import org.eclipse.ui.tests.navigator.extension.TestDataSorter;
import org.eclipse.ui.tests.navigator.extension.TestLabelProvider;
import org.eclipse.ui.tests.navigator.extension.TestResourceContentProvider;
import org.eclipse.ui.tests.navigator.util.TestWorkspace;

public class NavigatorTestBase extends TestCase {

	public static final String COMMON_NAVIGATOR_RESOURCE_EXT = "org.eclipse.ui.navigator.resourceContent";

	public static final String COMMON_NAVIGATOR_JAVA_EXT = "org.eclipse.jdt.java.ui.javaContent";

	
	public static final String TEST_VIEWER = "org.eclipse.ui.tests.navigator.TestView";
	public static final String TEST_VIEWER_PROGRAMMATIC = "org.eclipse.ui.tests.navigator.ProgrammaticTestView";
	public static final String TEST_VIEWER_PIPELINE = "org.eclipse.ui.tests.navigator.PipelineTestView";
	public static final String TEST_VIEWER_HIDE_EXTENSIONS = "org.eclipse.ui.tests.navigator.HideAvailableExtensionsTestView";
	
	public static final String TEST_CONTENT = "org.eclipse.ui.tests.navigator.testContent";
	public static final String TEST_CONTENT2 = "org.eclipse.ui.tests.navigator.testContent2";	
	public static final String TEST_CONTENT_OVERRIDDEN1 = "org.eclipse.ui.tests.navigator.testContentOverridden1";
	public static final String TEST_CONTENT_OVERRIDDEN2 = "org.eclipse.ui.tests.navigator.testContentOverridden2";
	public static final String TEST_CONTENT_OVERRIDE1 = "org.eclipse.ui.tests.navigator.testContentOverride1";
	public static final String TEST_CONTENT_OVERRIDE2 = "org.eclipse.ui.tests.navigator.testContentOverride2";
	public static final String TEST_CONTENT_SORTER = "org.eclipse.ui.tests.navigator.testContentSorter";
	public static final String TEST_CONTENT_REDLABEL = "org.eclipse.ui.tests.navigator.testContentRedLabel";
	public static final String TEST_CONTENT_MISSINGLABEL = "org.eclipse.ui.tests.navigator.testContentMissingLabel";
	public static final String TEST_CONTENT_DROP_COPY = "org.eclipse.ui.tests.navigator.testContentDropCopy";
	public static final String TEST_CONTENT_HAS_CHILDREN = "org.eclipse.ui.tests.navigator.testContentHasChildren";

	protected static final String TEST_ACTIVITY = "org.eclipse.ui.tests.navigator.testActivity";


	protected String _navigatorInstanceId;

	protected Set _expectedChildren = new HashSet();

	protected IProject _project;
	protected IProject _p1;
	protected IProject _p2;

	protected static final int _p1Ind = 0;
	protected static final int _p2Ind = 1;
	protected static final int _projectInd = 2;

	protected static int _projectCount;

	protected CommonViewer _viewer;

	protected CommonNavigator _commonNavigator;

	protected INavigatorContentService _contentService;
	protected NavigatorActionService _actionService;

	protected boolean _initTestData = true;

	protected void setUp() throws Exception {

		if (_navigatorInstanceId == null) {
			throw new RuntimeException(
					"Set the _navigatorInstanceId in the constructor");
		}

		TestResourceContentProvider.resetTest();
		TestDataSorter.resetTest();
		TestLabelProvider.resetTest();

		if (_initTestData) {
			clearAll();
			TestWorkspace.init();

			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			_project = root.getProject("Test"); //$NON-NLS-1$

			_expectedChildren.add(_project.getFolder("src")); //$NON-NLS-1$
			_expectedChildren.add(_project.getFolder("bin")); //$NON-NLS-1$
			_expectedChildren.add(_project.getFile(".project")); //$NON-NLS-1$
			_expectedChildren.add(_project.getFile(".classpath")); //$NON-NLS-1$ 
			_expectedChildren.add(_project.getFile("model.properties")); //$NON-NLS-1$

			_p1 = ResourcesPlugin.getWorkspace().getRoot().getProject("p1");
			_p1.open(null);
			_p2 = ResourcesPlugin.getWorkspace().getRoot().getProject("p2");
			_p2.open(null);
			_projectCount = 3;
		}

		showNavigator();
		refreshViewer();

		_contentService = _viewer.getNavigatorContentService();
		_actionService = _commonNavigator.getNavigatorActionService();
		
		((NavigatorFilterService)_contentService.getFilterService()).resetFilterActivationState();

	}

	protected void showNavigator() throws PartInitException {
		EditorTestHelper.showView(_navigatorInstanceId, true);

		IWorkbenchWindow activeWindow = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = activeWindow.getActivePage();

		_commonNavigator = (CommonNavigator) activePage
				.findView(_navigatorInstanceId);
		_commonNavigator.setFocus();
		_viewer = (CommonViewer) _commonNavigator
				.getAdapter(CommonViewer.class);
	}
	
	protected void tearDown() throws Exception {
		clearAll();
		// Hide it, we want a new one each time
		EditorTestHelper.showView(_navigatorInstanceId, false);
	}

	protected void clearAll() throws Exception {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; i++) {
			projects[i].delete(true, null);
		}
	}

	// Need this to workaround a problem of the
	// DecoratingStyledCellLabelProvider.
	// The method returns early because there is a (background) decoration
	// pending.
	protected void refreshViewer() {
		try {
			TreeItem[] rootItems = _viewer.getTree().getItems();
			if (rootItems.length > 0)
				rootItems[0].setText("");
		} catch (Exception ex) {
			// Ignore
		}
		_viewer.refresh();
	}

}