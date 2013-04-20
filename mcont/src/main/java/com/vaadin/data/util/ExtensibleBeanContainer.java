package com.vaadin.data.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.vaadin.data.Property;

/**
 * Extensible subclass of AbstractBeanContainer. BeanItem properties are
 * initialized lazily.
 * 
 * @author Henri Sara
 */
public class ExtensibleBeanContainer<IDTYPE, BEANTYPE> extends
        AbstractBeanContainer<IDTYPE, BEANTYPE> {

    private Field modelField;

    // map from bean class to its property model
    private Map<Class<?>, Map<String, VaadinPropertyDescriptor<BEANTYPE>>> properties = new HashMap<Class<?>, Map<String, VaadinPropertyDescriptor<BEANTYPE>>>();

    // final property types for each property id
    // If different subclasses have a different type with same id, a common
    // base class of the two is used.
    private Map<String, Class<?>> propertyIdToType = new HashMap<String, Class<?>>();

    public ExtensibleBeanContainer(Class<? super BEANTYPE> beanBaseClass,
            Class<? extends BEANTYPE>... subclasses) {
        super(beanBaseClass);

        try {
            modelField = AbstractBeanContainer.class.getDeclaredField("model");
            modelField.setAccessible(true);
        } catch (SecurityException e) {
            throw new RuntimeException(ExtensibleBeanContainer.class.getName()
                    + " is incompatible with the Vaadin version used");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(ExtensibleBeanContainer.class.getName()
                    + " is incompatible with the Vaadin version used");
        }

        // get property descriptors for subclasses
        Map<String, VaadinPropertyDescriptor<BEANTYPE>> model = getModel();
        properties.put(beanBaseClass, model);
        for (Class<? extends BEANTYPE> cls : subclasses) {
            LinkedHashMap<String, VaadinPropertyDescriptor<BEANTYPE>> submodel = BeanItem
                    .getPropertyDescriptors((Class<BEANTYPE>) cls);
            properties.put(cls, submodel);

            for (String id : submodel.keySet()) {
                Class<?> propertyType = submodel.get(id).getPropertyType();
                if (propertyIdToType.containsKey(id)) {
                    Class<?> oldPropertyType = propertyIdToType.get(id);
                    if (!oldPropertyType.isAssignableFrom(propertyType)) {
                        // TODO should use closest common ancestor
                        propertyIdToType.put(id, Object.class);
                    }
                } else {
                    propertyIdToType.put(id, propertyType);
                }
            }
        }

        for (String id : propertyIdToType.keySet()) {
            if (!model.containsKey(id)) {
                addContainerProperty(id,
                        new MultiClassVaadinPropertyDescriptor(id));
            }
        }
    }

    protected class MultiClassVaadinPropertyDescriptor implements
            VaadinPropertyDescriptor<BEANTYPE> {
        private String name;

        public MultiClassVaadinPropertyDescriptor(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<?> getPropertyType() {
            return propertyIdToType.get(name);
        }

        @Override
        public Property<?> createProperty(BEANTYPE bean) {
            // find the property in the closest suitable superclass
            VaadinPropertyDescriptor<BEANTYPE> pd = getPropertyDescriptor(bean,
                    name);
            // note that this does not support subclass specific nested
            // properties
            if (pd != null
                    && !(pd instanceof ExtensibleBeanContainer.MultiClassVaadinPropertyDescriptor)) {
                return pd.createProperty(bean);
            } else {
                return NullProperty.get(getPropertyType());
            }
        }
    }

    protected VaadinPropertyDescriptor<BEANTYPE> getPropertyDescriptor(
            BEANTYPE bean, String propertyId) {
        Class cls = bean.getClass();
        VaadinPropertyDescriptor<BEANTYPE> pd = null;
        Map<String, VaadinPropertyDescriptor<BEANTYPE>> map = properties
                .get(cls);
        if (map != null) {
            pd = map.get(propertyId);
        }
        while (pd == null && cls != null && getBeanType().isAssignableFrom(cls)) {
            cls = cls.getSuperclass();
            map = properties.get(cls);
            if (map != null) {
                pd = map.get(propertyId);
            }
        }
        return pd;
    }

    public static class NullProperty implements Property {
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

        @Override
        public void setReadOnly(boolean newStatus) {
            // no exception - this can coexist with other properties that can be
            // set as read-write in the came Item
        }
    }

    /**
     * This variant of BeanItem creates property instances only when requested
     * and tries to minimize memory overhead by sharing metadata between them.
     */
    public class CompactBeanItem extends BeanItem<BEANTYPE> {

        public CompactBeanItem(BEANTYPE bean) {
            super(bean,
                    new HashMap<String, VaadinPropertyDescriptor<BEANTYPE>>());
        }

        @Override
        public Collection<?> getItemPropertyIds() {
            return Collections
                    .unmodifiableCollection(propertyIdToType.keySet());
        }

        @Override
        public Property getItemProperty(Object id) {
            Property property = super.getItemProperty(id);
            if (property == null) {
                // create and add a property only when requested
                property = createProperty(id);
                if (property != null) {
                    addItemProperty(id, property);
                }
            }
            return property;
        }

        protected Property createProperty(Object id) {
            // get from the whole model including subclasses
            VaadinPropertyDescriptor pd = getPropertyDescriptor(getBean(),
                    (String) id);
            // TODO optimize by sharing metadata for
            // MethodPropertyDescriptor and NestedPropertyDescriptor
            if (pd != null) {
                Property property = pd.createProperty(getBean());
                return property;
            } else {
                return null;
            }
        }
    }

    @Override
    protected BeanItem<BEANTYPE> createBeanItem(BEANTYPE bean) {
        return bean == null ? null : new CompactBeanItem(bean);
    }

    protected Map<String, VaadinPropertyDescriptor<BEANTYPE>> getModel() {
        try {
            return (Map<String, VaadinPropertyDescriptor<BEANTYPE>>) modelField
                    .get(this);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Container model not accessible");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Container model not accessible");
        }
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException {
        // TODO implement
        return false;
    }

    // TODO remove container property

    public BeanItem addBean(IDTYPE id, BEANTYPE bean) {
        return super.addItem(id, bean);
    }

    // TODO implement more API for adding beans, ID resolver etc.

}
