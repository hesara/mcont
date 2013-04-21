package org.vaadin.mcont;

import junit.framework.Assert;

import org.junit.Test;

import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.util.NullProperty;

public class NullPropertyTest {
    @Test
    public void testNullProperty() {
        NullProperty p1 = NullProperty.get(String.class);
        Assert.assertEquals(String.class, p1.getType());
        Assert.assertNull(p1.getValue());
        Assert.assertTrue(p1.isReadOnly());
        p1.setReadOnly(false);
    }

    @Test(expected = ReadOnlyException.class)
    public void testSetValue() {
        NullProperty p1 = NullProperty.get(String.class);
        p1.setValue(1);
    }

    @Test
    public void testSharedInstance() {
        NullProperty p1 = NullProperty.get(String.class);
        NullProperty p2 = NullProperty.get(String.class);
        NullProperty p3 = NullProperty.get(Object.class);
        Assert.assertSame(p1, p2);
        Assert.assertNotSame(p1, p3);
    }
}
