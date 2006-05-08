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

import org.eclipse.jface.util.Util;

/**
 * <p>
 * An ordering for a location. This is the sort order with respect to other menu
 * elements.
 * </p>
 * <p>
 * Additional position constants may be added in the future.
 * </p>
 * <p>
 * Clients may instantiate this class, but must not extend.
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
 */
public final class SOrder {

	/**
	 * The constant to use if there is no position. This is useful for some
	 * algorithms involved collections of ordering constraints.
	 */
	static final int NO_POSITION = -1;

	/**
	 * The constant to use if the position for the location is the start of the
	 * current <code>IMenuCollection</code>.
	 */
	public static final int POSITION_START = 0;

	/**
	 * The constant to use if the position for the location is the end of the
	 * current <code>IMenuCollection</code>.
	 */
	public static final int POSITION_END = 1;

	/**
	 * The constant to use if the position for the location is to be before a
	 * menu element with a particular identifier.
	 */
	public static final int POSITION_BEFORE = 2;

	/**
	 * The constant to use if the position for the location is to be after a
	 * menu element with a particular identifier.
	 */
	public static final int POSITION_AFTER = 3;

	/**
	 * The position the menu element should be given relative to other menu
	 * elements. This should be one of the position constants given by this
	 * class.
	 */
	private final int position;

	/**
	 * The identifier of the menu element to which the position is relative.
	 * This value is <code>null</code> iff the position is not a relative
	 * position.
	 */
	private final String relativeTo;

	/**
	 * Constructs a new instance of <code>SOrder</code>. This ordering
	 * constraint can be either indicate the start or end. For relative
	 * positioning, use {@link SOrder#SOrder(int, String)}.
	 * 
	 * @param position
	 *            The position in which the menu element should appear with
	 *            respect to other menu elements. This value should be one of
	 *            the position constants defined in this class.
	 * 
	 * @see SOrder#POSITION_END
	 * @see SOrder#POSITION_START
	 */
	public SOrder(final int position) {
		this(position, null);
	}

	/**
	 * Constructs a new instance of <code>SOrder</code>.
	 * 
	 * @param position
	 *            The position in which the menu element should appear with
	 *            respect to other menu elements. This value should be one of
	 *            the position constants defined in this class.
	 * @param relativeTo
	 *            The identifier of the menu element to which the position is
	 *            relative. This value is required if the position is
	 *            <code>POSITION_AFTER</code> or <code>POSITION_BEFORE</code>.
	 *            Otherwise, this value should be <code>null</code>.
	 * 
	 * @see SOrder#POSITION_AFTER
	 * @see SOrder#POSITION_BEFORE
	 * @see SOrder#POSITION_END
	 * @see SOrder#POSITION_START
	 */
	public SOrder(final int position, final String relativeTo) {
		if ((position < POSITION_START) || (position > POSITION_AFTER)) {
			throw new IllegalArgumentException(
					"A location needs a valid position.  Got: " + position); //$NON-NLS-1$
		}

		if ((position == POSITION_AFTER) || (position == POSITION_BEFORE)) {
			if (relativeTo == null) {
				throw new NullPointerException(
						"A location positioned before or after needs an identifier of the menu element to which the position is relative"); //$NON-NLS-1$
			}
		} else if (relativeTo != null) {
			throw new IllegalArgumentException(
					"A relative identifier was provided for a non-relative position"); //$NON-NLS-1$
		}

		this.position = position;
		this.relativeTo = relativeTo;
	}

	/**
	 * Returns the position for this location. The position will be one of the
	 * the position constants defined in this class. Additional position
	 * constants may be added in the future.
	 * 
	 * @return The position.
	 */
	public final int getPosition() {
		return position;
	}

	/**
	 * Returns the identifier of the menu element to which this location is
	 * relative. There will only be such an identifier if the position is a
	 * relative positioning.
	 * 
	 * @return The relative identifier. If the position is not relative, then
	 *         <code>null</code>.
	 * @see SOrder#POSITION_AFTER
	 * @see SOrder#POSITION_BEFORE
	 */
	public final String getRelativeTo() {
		return relativeTo;
	}

	public final String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("SOrder("); //$NON-NLS-1$
		switch (position) {
		case POSITION_AFTER:
			buffer.append("after"); //$NON-NLS-1$
			break;
		case POSITION_BEFORE:
			buffer.append("before"); //$NON-NLS-1$
			break;
		case POSITION_END:
			buffer.append("end"); //$NON-NLS-1$
			break;
		case POSITION_START:
			buffer.append("start"); //$NON-NLS-1$
			break;
		default:
			buffer.append("unknown"); //$NON-NLS-1$
		}
		buffer.append(',');
		buffer.append(relativeTo);
		buffer.append(')');
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof SOrder) {
			SOrder order = (SOrder) obj;
			return position == order.position
					&& Util.equals(relativeTo, order.relativeTo);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return position + Util.hashCode(relativeTo);
	}
}
