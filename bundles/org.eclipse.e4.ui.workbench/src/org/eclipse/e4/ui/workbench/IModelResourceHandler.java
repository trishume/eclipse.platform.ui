/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.e4.ui.workbench;

import java.io.IOException;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.emf.ecore.resource.Resource;

/**
 *
 */
public interface IModelResourceHandler {

	/**
	 * @return the most recent model state
	 */
	Resource loadMostRecentModel();

	/**
	 * Creates a resource with an app Model, used for saving copies of the main app model.
	 * 
	 * @param theApp
	 *            the application model to add to the resource
	 * @return a resource with a proper save path with the model as contents
	 */
	public Resource createResourceWithApp(MApplication theApp);

	/**
	 * @throws IOException
	 *             if storing fails
	 * 
	 */
	void save() throws IOException;

}
