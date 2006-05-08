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

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.core.commands.common.HandleObject;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.window.Window;

/**
 * <p>
 * A handle object existing in the menus. This can be either a menu, a group, an
 * item or a widget.
 * </p>
 * <p>
 * For menu elements, there is the concept of "showing" and "visible". "Showing"
 * means that the menu element could potentially be visible to the end user.
 * This property is under control of the user. So, for example, clicking on a
 * top-level menu while make all of its contents "showing". "Visible" means that
 * the menu element should be painted on the display if it is showing. This
 * property is under control of the application. So, for example, an application
 * could decide to hide some items in a context menu if they do not apply to the
 * current selection.
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
 * @see org.eclipse.ui.internal.menus.SMenu
 * @see org.eclipse.ui.internal.menus.SGroup
 * @see org.eclipse.ui.internal.menus.SItem
 * @see org.eclipse.ui.internal.menus.SWidget
 */
public abstract class MenuElement extends HandleObject {

	/**
	 * The array to return from {@link #getLocations()} if there are no
	 * locations.
	 */
	private static final SLocation[] NO_LOCATIONS = new SLocation[0];

	/**
	 * The property for a property change event indicating that the defined
	 * state for this menu element has changed.
	 */
	public static final String PROPERTY_DEFINED = "DEFINED"; //$NON-NLS-1$

	/**
	 * The property for a property change event indicating that the locations
	 * for this menu element have changed.
	 */
	public static final String PROPERTY_LOCATIONS = "LOCATIONS"; //$NON-NLS-1$

	/**
	 * The property for a property change event indicating that the visibility
	 * for this menu element has changed.
	 */
	public static final String PROPERTY_VISIBILITY = "VISIBILITY"; //$NON-NLS-1$

	/**
	 * The locations in which this menu element appears. This value may be empty
	 * or <code>null</code>.
	 */
	protected SLocation[] locations;

	/**
	 * Whether this menu element is currently showing. This is a map of
	 * {@link Window} to {@link Boolean}. This value may be <code>null</code>
	 * if all values are <code>false</code>. The default value for any item
	 * is <code>false</code>.
	 */
	private Map windowToShowingMap = null;

	/**
	 * Whether this menu element is currently visible. This is a map of
	 * {@link Window} to {@link Boolean}. This value may be <code>null</code>
	 * if all values are <code>false</code>. The default value for any item
	 * is <code>false</code>.
	 */
	private Map windowToVisibleMap = null;

	/**
	 * Constructs a new instance of <code>MenuElement</code>.
	 * 
	 * @param id
	 *            The identifier of the element to create; must not be
	 *            <code>null</code>.
	 */
	public MenuElement(final String id) {
		super(id);
	}

	/**
	 * Adds a listener to this menu element that will be notified when this menu
	 * element's state changes.
	 * 
	 * @param listener
	 *            The listener to be added; must not be <code>null</code>.
	 */
	public final void addListener(final IPropertyChangeListener listener) {
		addListenerObject(listener);
	}

	/**
	 * Does this menu element exist at this location.
	 * 
	 * @param location
	 *            the location to check
	 * @return <code>true</code> if this menu element contains this location.
	 */
	public boolean containsLocation(final SLocation location) {
		if (locations!=null && location!=null) {
			for (int i = 0; i < locations.length; i++) {
				if (location.equals(locations[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Adds a location to an existing menu element.
	 * 
	 * @param location
	 *            The location to add to this element; must not be
	 *            <code>null</code>.
	 */
	public final void addLocation(final SLocation location) {
		if (location == null) {
			throw new NullPointerException("The location cannot be null"); //$NON-NLS-1$
		}
		
		if (containsLocation(location)) {
			return;
		}

		final SLocation[] newLocations;
		if ((locations == null) || (locations.length == 0)) {
			newLocations = new SLocation[] { location };
		} else {
			newLocations = new SLocation[locations.length + 1];
			System.arraycopy(locations, 0, newLocations, 0, locations.length);
			newLocations[locations.length] = location;
		}
		setLocations(newLocations);
	}

	/**
	 * Notifies listeners to this menu element that it has changed in some way.
	 * 
	 * @param event
	 *            The event to fire; may be <code>null</code>.
	 */
	protected final void firePropertyChangeEvent(final PropertyChangeEvent event) {
		if (event == null) {
			return;
		}

		final Object[] listeners = getListeners();
		for (int i = 0; i < listeners.length; i++) {
			final IPropertyChangeListener listener = (IPropertyChangeListener) listeners[i];
			listener.propertyChange(event);
		}
	}

	/**
	 * Indicates that the visible property has changed.
	 * 
	 * @param visible
	 *            Whether this menu element is now visible.
	 */
	protected final void fireVisibleChanged(final boolean visible) {
		if (isListenerAttached()) {
			final PropertyChangeEvent event;
			if (visible) {
				event = new PropertyChangeEvent(this, PROPERTY_VISIBILITY,
						Boolean.FALSE, Boolean.TRUE);
			} else {
				event = new PropertyChangeEvent(this, PROPERTY_VISIBILITY,
						Boolean.TRUE, Boolean.FALSE);
			}
			firePropertyChangeEvent(event);
		}
	}

	/**
	 * Returns the locations for this menu collection. This performs a copy of
	 * the internal data structure.
	 * 
	 * @return The locations for this menu collection; never <code>null</code>.
	 * @throws NotDefinedException
	 *             If the handle is not currently defined.
	 */
	public final SLocation[] getLocations() throws NotDefinedException {
		if (!isDefined()) {
			throw new NotDefinedException(
					"Cannot get the locations from an undefined menu element"); //$NON-NLS-1$
		}

		if (locations == null) {
			return NO_LOCATIONS;
		}

		final SLocation[] result = new SLocation[locations.length];
		System.arraycopy(locations, 0, result, 0, locations.length);
		return result;
	}

	/**
	 * Returns whether this menu element is showing within the given window. A
	 * menu element is showing if it could be visible to the user. A menu
	 * element must be showing before the <code>isVisible</code> is checked.
	 * 
	 * @param window
	 *            The window in which to check if this menu element is showing;
	 *            must not be <code>null</code>.
	 * @return <code>true</code> if the menu element is showing;
	 *         <code>false</code> otherwise.
	 */
	public final boolean isShowing(final Window window) {
		if (windowToShowingMap != null) {
			Object value = windowToShowingMap.get(window);
			if (value == null) {
				value = windowToShowingMap.get(null);
			}
			if (value == Boolean.TRUE) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns whether this menu element is visible within the given menu. A
	 * menu element is visible if it will be visible to the user if showing.
	 * 
	 * @param window
	 *            The window in which to check if this menu element is showing;
	 *            must not be <code>null</code>.
	 * @return <code>true</code> if the menu element is visible;
	 *         <code>false</code> otherwise.
	 */
	public final boolean isVisible(final Window window) {
		if (windowToVisibleMap != null) {
			Object value = windowToVisibleMap.get(window);
			if (value == null) {
				value = windowToVisibleMap.get(null);
			}
			if (value == Boolean.FALSE) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Removes a listener from this menu element.
	 * 
	 * @param listener
	 *            The listener to be removed; must not be <code>null</code>.
	 */
	public final void removeListener(final IPropertyChangeListener listener) {
		removeListenerObject(listener);
	}

	/**
	 * Sets whether this menu element is defined. This will fire a property
	 * change event if anyone cares.
	 * 
	 * @param defined
	 *            Whether the menu element is defined.
	 */
	protected final void setDefined(final boolean defined) {
		if (this.defined != defined) {
			PropertyChangeEvent event = null;
			if (isListenerAttached()) {
				event = new PropertyChangeEvent(this, PROPERTY_DEFINED, 
						(this.defined ? Boolean.TRUE : Boolean.FALSE),
						(defined ? Boolean.TRUE : Boolean.FALSE));
			}
			this.defined = defined;
			firePropertyChangeEvent(event);
		}
	}

	/**
	 * Sets the locations in which this menu element will appear. This will fire
	 * a property change event if anyone cares.
	 * 
	 * @param locations
	 *            The locations in which this menu element will appear; may be
	 *            <code>null</code> or empty.
	 */
	protected final void setLocations(final SLocation[] locations) {
		if (!Util.equals(this.locations, locations)) {
			PropertyChangeEvent event = null;
			if (isListenerAttached()) {
				event = new PropertyChangeEvent(this, PROPERTY_LOCATIONS,
						this.locations, locations);
			}
			this.locations = locations;
			firePropertyChangeEvent(event);
		}
	}

	/**
	 * Sets the showing property for this menu element within the given window.
	 * Changing this property does not trigger a property change event.
	 * Developers interested in listening to changes in this property should
	 * attach a listener to the <code>SMenuManager</code>.
	 * 
	 * @param window
	 *            The window in which to change if this menu element is showing;
	 *            must not be <code>null</code>.
	 * @param showing
	 *            Whether the menu element should be showing.
	 * @see SMenuManager#addListener(IMenuManagerListener)
	 */
	final void setShowing(final Window window, final boolean showing) {
		if (window == null) {
			throw new NullPointerException(
					"The window in which a menu element is showing must not be null"); //$NON-NLS-1$
		}

		if (windowToShowingMap == null) {
			windowToShowingMap = new WeakHashMap(3);
		}
		windowToShowingMap.put(window, showing ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Sets the visible property. Changing this property triggers an event
	 * appropriate for the subclasses. If no window is provided, then this is
	 * the default visibility.
	 * 
	 * @param window
	 *            The window in which to change if this menu element is visible;
	 *            may be <code>null</code>.
	 * @param visible
	 *            Whether the menu element should be visible.
	 */
	public final void setVisible(final Window window, final boolean visible) {
		final boolean initialVisible = isVisible(window);
		if (initialVisible != visible) {
			if (windowToVisibleMap == null) {
				windowToVisibleMap = new WeakHashMap(3);
			}
			windowToVisibleMap.put(window, visible ? Boolean.TRUE
					: Boolean.FALSE);
			fireVisibleChanged(visible);
		}
	}
}
