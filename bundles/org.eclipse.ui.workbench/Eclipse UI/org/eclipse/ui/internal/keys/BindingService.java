/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.keys;

import java.io.IOException;
import java.util.Map;

import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.BindingManager;
import org.eclipse.jface.bindings.Scheme;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.ui.keys.IBindingService;

/**
 * <p>
 * Provides services related to the binding architecture (e.g., keyboard
 * shortcuts) within the workbench. This service can be used to access the
 * currently active bindings, as well as the current state of the binding
 * architecture.
 * </p>
 * <p>
 * <em>EXPERIMENTAL</em>. The commands architecture is currently under
 * development for Eclipse 3.1. This class -- its existence, its name and its
 * methods -- are in flux. Do not use this class yet.
 * </p>
 * 
 * @since 3.1
 */
public final class BindingService implements IBindingService {

	/**
	 * The binding manager that supports this service. This value is never
	 * <code>null</code>.
	 */
	private final BindingManager bindingManager;

	/**
	 * Constructs a new instance of <code>BindingService</code> using a JFace
	 * binding manager.
	 * 
	 * @param bindingManager
	 *            The binding manager to use; must not be <code>null</code>.
	 */
	public BindingService(final BindingManager bindingManager) {
		if (bindingManager == null) {
			throw new NullPointerException(
					"Cannot create a binding service with a null manager"); //$NON-NLS-1$
		}
		this.bindingManager = bindingManager;
	}

	public final TriggerSequence[] getActiveBindingsFor(final String commandId) {
		return bindingManager.getActiveBindingsFor(commandId);
	}

	public final Scheme getActiveScheme() {
		return bindingManager.getActiveScheme();
	}

	public final Binding[] getBindings() {
		return bindingManager.getBindings();
	}

	public final String getDefaultSchemeId() {
		return BindingPersistence.getDefaultSchemeId();
	}

	public final Scheme[] getDefinedSchemes() {
		return bindingManager.getDefinedSchemes();
	}

	public final String getLocale() {
		return bindingManager.getLocale();
	}

	public final Map getPartialMatches(final TriggerSequence trigger) {
		return bindingManager.getPartialMatches(trigger);
	}

	public final String getPerfectMatch(final TriggerSequence trigger) {
		return bindingManager.getPerfectMatch(trigger);
	}

	public final String getPlatform() {
		return bindingManager.getPlatform();
	}

	public final Scheme getScheme(final String schemeId) {
		/*
		 * TODO Need to put in place protection against the context being
		 * changed.
		 */
		return bindingManager.getScheme(schemeId);
	}

	public final boolean isPartialMatch(final TriggerSequence sequence) {
		return bindingManager.isPartialMatch(sequence);
	}

	public final boolean isPerfectMatch(final TriggerSequence sequence) {
		return bindingManager.isPerfectMatch(sequence);
	}

	public final void readRegistryAndPreferences() {
		final String activeSchemeId = BindingPersistence.read();
		try {
			bindingManager.setActiveScheme(bindingManager
					.getScheme(activeSchemeId));
		} catch (final NotDefinedException e) {
			// The scheme is undefined, so try to set it to the default default.
			try {
				bindingManager.setActiveScheme(bindingManager
						.getScheme(DEFAULT_DEFAULT_ACTIVE_SCHEME_ID));
			} catch (final NotDefinedException nde) {
				/*
				 * Even the default scheme is undefined. We're in serious
				 * trouble. This should not be possible.
				 */
				throw new Error(
						"The active scheme is not defined, and neither is the default default scheme."); //$NON-NLS-1$
			}
		}
	}

	public final void savePreferences(final Scheme activeScheme,
			final Binding[] bindings) throws IOException {
		BindingPersistence.persist(activeScheme, bindings);
		try {
			bindingManager.setActiveScheme(activeScheme);
		} catch (final NotDefinedException e) {
			// The active scheme is not currently defined.
		}
		bindingManager.setBindings(bindings);
	}
}
