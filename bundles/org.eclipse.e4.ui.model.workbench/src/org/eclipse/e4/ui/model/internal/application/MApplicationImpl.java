/**
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      IBM Corporation - initial API and implementation
 *
 * $Id$
 */
package org.eclipse.e4.ui.model.internal.application;

import java.util.Collection;

import org.eclipse.e4.ui.model.application.ApplicationPackage;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.MCommand;
import org.eclipse.e4.ui.model.application.MWindow;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MApplication</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.e4.ui.model.internal.application.MApplicationImpl#getWindows <em>Windows</em>}</li>
 *   <li>{@link org.eclipse.e4.ui.model.internal.application.MApplicationImpl#getCommand <em>Command</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MApplicationImpl<W extends MWindow<?>> extends MApplicationElementImpl implements MApplication<W> {
	/**
	 * The cached value of the '{@link #getWindows() <em>Windows</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindows()
	 * @generated
	 * @ordered
	 */
	protected EList<W> windows;

	/**
	 * The cached value of the '{@link #getCommand() <em>Command</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommand()
	 * @generated
	 * @ordered
	 */
	protected EList<MCommand> command;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MApplicationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ApplicationPackage.Literals.MAPPLICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<W> getWindows() {
		if (windows == null) {
			windows = new EObjectContainmentEList<W>(MWindow.class, this, ApplicationPackage.MAPPLICATION__WINDOWS);
		}
		return windows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MCommand> getCommand() {
		if (command == null) {
			command = new EObjectContainmentEList<MCommand>(MCommand.class, this, ApplicationPackage.MAPPLICATION__COMMAND);
		}
		return command;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ApplicationPackage.MAPPLICATION__WINDOWS:
				return ((InternalEList<?>)getWindows()).basicRemove(otherEnd, msgs);
			case ApplicationPackage.MAPPLICATION__COMMAND:
				return ((InternalEList<?>)getCommand()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ApplicationPackage.MAPPLICATION__WINDOWS:
				return getWindows();
			case ApplicationPackage.MAPPLICATION__COMMAND:
				return getCommand();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ApplicationPackage.MAPPLICATION__WINDOWS:
				getWindows().clear();
				getWindows().addAll((Collection<? extends W>)newValue);
				return;
			case ApplicationPackage.MAPPLICATION__COMMAND:
				getCommand().clear();
				getCommand().addAll((Collection<? extends MCommand>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ApplicationPackage.MAPPLICATION__WINDOWS:
				getWindows().clear();
				return;
			case ApplicationPackage.MAPPLICATION__COMMAND:
				getCommand().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ApplicationPackage.MAPPLICATION__WINDOWS:
				return windows != null && !windows.isEmpty();
			case ApplicationPackage.MAPPLICATION__COMMAND:
				return command != null && !command.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MApplicationImpl