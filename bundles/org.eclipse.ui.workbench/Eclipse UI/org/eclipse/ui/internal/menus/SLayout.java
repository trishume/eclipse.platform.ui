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

/**
 * <p>
 * A layout for a widget element. The layout contains information allowing the
 * workbench layout algorithm to manage the amount of space allocated to a
 * particular trim widget.
 * </p>
 * <p>
 * There are two boolean values defined in this element:
 * <ul>
 * <li><b>fillMajor</b>: If <code>true</code> then the layout will 'stretch'
 * the widget to use up extra space along the <i>major</i> dimension.</li>
 * <li><b>fillMinor</b>: If <code>true</code> then the layout will 'stretch'
 * the widget to use up extra space along the <i>minor</i> dimension.</li>
 * </ul>
 * </p>
 * <p>
 * <b>NOTE</b>: The 'major' dimension is "X" (horiontal) for trim areas at the
 * top or bottom of the workbench and "Y" (vertical) for trim areas on the sides
 * of the workbench.
 * </p>
 * <p>
 * Clients may instantiate this class, but must not extend.
 * </p>
 * <p>
 * <strong>PROVISIONAL</strong>. This class or interface has been added as part
 * of a work in progress. There is a guarantee neither that this API will work
 * nor that it will remain the same. Please do not use this API without
 * consulting with the Platform/UI team.
 * </p>
 * <p>
 * This class will eventually exist in <code>org.eclipse.jface.menus</code>.
 * </p>
 * 
 * @since 3.2
 */
public final class SLayout {

	/**
	 * The value of the 'fillMajor' attribute
	 */
	private final boolean fillMajor;

	/**
	 * The value of the 'fillMinor' attribute
	 */
	private final boolean fillMinor;

	/**
	 * Constructs a new instance of <code>SLayout</code> -- indicating no fill
	 * is desired in either dimension.
	 */
	public SLayout() {
		this(false, false);
	}

	/**
	 * Constructs a new instance of <code>SLayout</code> -- capturing the fill
	 * values as given.
	 * 
	 * @param fillMajor
	 *            <code>true</code> if the layout will 'stretch' the widget to
	 *            use up extra space along the <i>major</i> dimension.
	 * @param fillMinor
	 *            <code>true</code> if the layout will 'stretch' the widget to
	 *            use up extra space along the <i>minor</i> dimension.
	 */
	public SLayout(final boolean fillMajor, final boolean fillMinor) {
		this.fillMajor = fillMajor;
		this.fillMinor = fillMinor;
	}

	/**
	 * Returns whether the widget should fill all available space along the
	 * major axis.
	 * 
	 * @return Returns the fillMajor value.
	 */
	public boolean fillMajor() {
		return fillMajor;
	}

	/**
	 * Returns whether the widget should fill all available space along the
	 * minor axis.
	 * 
	 * @return Returns the fillMinor value.
	 */
	public boolean fillMinor() {
		return fillMinor;
	}

	public final String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("SLayout("); //$NON-NLS-1$
		buffer.append(fillMajor);
		buffer.append(',');
		buffer.append(fillMinor);
		buffer.append(')');
		return buffer.toString();
	}
}
