/*******************************************************************************
 * Copyright (c) 2008 Matthew Hall and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 195222)
 *     Matthew Hall - bug 264307
 ******************************************************************************/

package org.eclipse.core.internal.databinding.beans;

import java.beans.PropertyDescriptor;

import org.eclipse.core.databinding.beans.IBeanMapProperty;
import org.eclipse.core.databinding.beans.IBeanValueProperty;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.map.IMapProperty;
import org.eclipse.core.databinding.property.map.MapProperty;

/**
 * @since 3.3
 * 
 */
public class PojoMapPropertyDecorator extends MapProperty implements
		IBeanMapProperty {
	private final IMapProperty delegate;
	private final PropertyDescriptor propertyDescriptor;

	/**
	 * @param delegate
	 * @param propertyDescriptor
	 */
	public PojoMapPropertyDecorator(IMapProperty delegate,
			PropertyDescriptor propertyDescriptor) {
		this.delegate = delegate;
		this.propertyDescriptor = propertyDescriptor;
	}

	public Object getKeyType() {
		return delegate.getKeyType();
	}

	public Object getValueType() {
		return delegate.getValueType();
	}

	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public IBeanMapProperty values(String propertyName) {
		return values(propertyName, null);
	}

	public IBeanMapProperty values(String propertyName, Class valueType) {
		Class beanClass = (Class) delegate.getValueType();
		return values(PojoProperties.value(beanClass, propertyName, valueType));
	}

	public IBeanMapProperty values(IBeanValueProperty property) {
		return new PojoMapPropertyDecorator(super.values(property), property
				.getPropertyDescriptor());
	}

	public IObservableMap observe(Object source) {
		return new BeanObservableMapDecorator(delegate.observe(source),
				propertyDescriptor);
	}

	public IObservableMap observe(Realm realm, Object source) {
		return new BeanObservableMapDecorator(delegate.observe(realm, source),
				propertyDescriptor);
	}

	public IObservableFactory mapFactory() {
		return delegate.mapFactory();
	}

	public IObservableFactory mapFactory(Realm realm) {
		return delegate.mapFactory(realm);
	}

	public IObservableMap observeDetail(IObservableValue master) {
		return new BeanObservableMapDecorator(delegate.observeDetail(master),
				propertyDescriptor);
	}

	public String toString() {
		return delegate.toString();
	}
}