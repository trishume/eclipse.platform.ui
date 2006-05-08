/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ui.internal.menus;

import org.eclipse.core.commands.common.NotDefinedException;

/**
 * <p>
 * A collection of menu elements. This is given to an implementation of
 * <code>IDynamicMenu</code> to modify.
 * </p>
 * <p>
 * Clients must not implement or extend.
 * </p>
 * <p>
 * <strong>PROVISIONAL</strong>. This class or interface has been added as
 * part of a work in progress. There is a guarantee neither that this API will
 * work nor that it will remain the same. Please do not use this API without
 * consulting with the Platform/UI team.
 * </p>
 * <p>
 * This class will eventually exist in <code>org.eclipse.jface.menus</code>.
 * </p>
 * 
 * @since 3.2
 * @see org.eclipse.ui.internal.menus.IDynamicMenu
 */
public interface IMenuCollection {

	/**
	 * Adds a menu element to the collection. Any menu element contributed
	 * through this method must contain exactly one location. Ordering
	 * constraints are respected; the item will appear in the position indicated
	 * by the ordering constraints.
	 * 
	 * @param element
	 *            The element to append. Must not be null, and must be of the
	 *            appropriate type for the type of collection.
	 * @throws NotDefinedExcpetion
	 *             If the provided element is not defined.
	 * @see MenuElement#getLocations()
	 * @see SLocation#getOrderings()
	 */
	public void add(MenuElement element) throws NotDefinedException;

	/**
	 * Removes all elements from the collection.
	 */
	public void clear();

	/**
	 * Removes the given menu element, if it exists.
	 * 
	 * @param element
	 *            The element to remove.
	 * @return true if the object was removed; false if it could not be found.
	 */
	public boolean remove(MenuElement element);
}
