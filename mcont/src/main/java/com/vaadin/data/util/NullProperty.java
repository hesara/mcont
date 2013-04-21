package com.vaadin.data.util;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Property;

/**
 * A read-only {@link Property} that always returns null.
 * 
 * NullProperty instances are shared per data type. The method get(propertyType)
 * should be used to obtain an instance of NullProperty.
 * 
 * Note that {@link #setReadOnly(boolean)} silently ignores its parameter.
 * 
 * @author Henri Sara
 */
public class NullProperty implements Property {
    private Class propertyType;
    private static Map<Class, NullProperty> instances = new HashMap<Class, NullProperty>();

    protected NullProperty(Class propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * Get a (shared) instance of NullProperty for a given property type.
     * 
     * @param propertyType
     * @return NullProperty instance
     */
    public static NullProperty get(Class propertyType) {
        if (!instances.containsKey(propertyType)) {
            instances.put(propertyType, new NullProperty(propertyType));
        }
        return instances.get(propertyType);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException {
        throw new ReadOnlyException("Property is read-only");
    }

    @Override
    public Class getType() {
        return propertyType;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    /**
     * This method does nothing in a NullProperty, which is always read-only.
     * 
     * @param newStatus
     *            ignored
     */
    @Override
    public void setReadOnly(boolean newStatus) {
        // no exception - this can coexist with other properties that can be
        // set as read-write in the came Item
    }
}