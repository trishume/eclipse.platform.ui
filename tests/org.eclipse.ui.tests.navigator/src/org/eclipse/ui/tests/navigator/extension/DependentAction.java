/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tests.navigator.extension;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class DependentAction extends Action implements IAction {
 
	private Shell shell;

	public DependentAction(Shell aShell, String isValid) {
		super("Dependent Action: " + isValid);
		shell = aShell;
		
	}
	
	public void run() {
		MessageDialog.openInformation(shell, "DependentAction", "The dependent action ran!");
	}
}
