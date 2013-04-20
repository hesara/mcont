package org.vaadin.mcont;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;

import com.vaadin.data.util.ExtensibleBeanContainer;

// JUnit tests here
public class ExtensibleBeanContainerTest {

    public static class Person {
        private String name;

        public Person(String name) {
            setName(name);
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Secretary extends Employee {
        public Secretary(String name, String employer) {
            super(name, employer);
        }
    }

    public static class Employee extends Person {
        private String employer;

        public Employee(String name, String employer) {
            super(name);
            setEmployer(employer);
        }

        public String getEmployer() {
            return employer;
        }

        public void setEmployer(String employer) {
            this.employer = employer;
        }
    }

    public static class Child extends Person {
        private String toy;

        public Child(String name, String favoriteToy) {
            super(name);
            setToy(favoriteToy);
        }

        public String getToy() {
            return toy;
        }

        public void setToy(String toy) {
            this.toy = toy;
        }
    }

    public static class MiddleAgedMan extends Person {
        public static class Vehicle {
        }

        private Vehicle toy;

        public MiddleAgedMan(String name, Vehicle favoriteToy) {
            super(name);
            setToy(favoriteToy);
        }

        public Vehicle getToy() {
            return toy;
        }

        public void setToy(Vehicle toy) {
            this.toy = toy;
        }
    }

    private ExtensibleBeanContainer<String, Person> createContainer() {
        ExtensibleBeanContainer<String, Person> container = new ExtensibleBeanContainer<String, Person>(
                Person.class, Employee.class, Child.class);
        return container;
    }

    private ExtensibleBeanContainer<String, Person> initializeContainer() {
        ExtensibleBeanContainer<String, Person> container = createContainer();
        container.addBean("person1", new Person("person1"));
        container.addBean("employee1", new Employee("employee1", "employer1"));
        container.addBean("child1", new Child("child1", "rubber duck"));
        return container;
    }

    @Test
    public void testCreateContainer() {
        createContainer();
    }

    @Test
    public void testAddBean() {
        ExtensibleBeanContainer<String, Person> container = initializeContainer();
        Assert.assertEquals(3, container.size());
        Assert.assertEquals("person1", container.getIdByIndex(0));
        Assert.assertEquals("employee1", container.getIdByIndex(1));
        Assert.assertEquals("child1", container.getIdByIndex(2));
    }

    @Test
    public void testNonNullProperties() {
        ExtensibleBeanContainer<String, Person> container = initializeContainer();
        Assert.assertEquals("person1",
                container.getContainerProperty("person1", "name").getValue());
        Assert.assertEquals("employee1",
                container.getContainerProperty("employee1", "name").getValue());
        Assert.assertEquals("employer1",
                container.getContainerProperty("employee1", "employer")
                        .getValue());
        Assert.assertEquals("rubber duck",
                container.getContainerProperty("child1", "toy").getValue());
    }

    @Test
    public void testNullProperties() {
        ExtensibleBeanContainer<String, Person> container = initializeContainer();
        Assert.assertNull(container.getContainerProperty("person1", "employer")
                .getValue());
        Assert.assertNull(container.getContainerProperty("person1", "toy")
                .getValue());
        Assert.assertNull(container.getContainerProperty("employee1", "toy")
                .getValue());
        Assert.assertNull(container.getContainerProperty("child1", "employer")
                .getValue());
    }

    @Test
    public void testPropertyInUnlistedSubclassUnavailable() {
        ExtensibleBeanContainer<String, Person> container = new ExtensibleBeanContainer<String, Person>(
                Person.class, Employee.class);
        Assert.assertTrue(container.getContainerPropertyIds().contains("name"));
        Assert.assertTrue(container.getContainerPropertyIds().contains(
                "employer"));
        Assert.assertFalse(container.getContainerPropertyIds().contains("toy"));

        container.addBean("person1", new Person("person1"));
        container.addBean("employee1", new Employee("employee1", "employer1"));
        container.addBean("child1", new Child("child1", "rubber duck"));
        Assert.assertNull(container.getContainerProperty("child1", "toy"));
    }

    @Test
    public void testGetItemPropertyIds() {
        ExtensibleBeanContainer<String, Person> container = initializeContainer();
        Collection<?> ids = container.getItem("person1").getItemPropertyIds();
        Assert.assertEquals(3, ids.size());
        Assert.assertTrue(ids.contains("name"));
        Assert.assertTrue(ids.contains("employer"));
        Assert.assertTrue(ids.contains("toy"));
    }

    @Test
    public void testSamePropertyInstance() {
        ExtensibleBeanContainer<String, Person> container = initializeContainer();
        Assert.assertSame(container.getContainerProperty("person1", "name"),
                container.getContainerProperty("person1", "name"));
    }

    @Test
    public void testSimpleSubclass() {
        ExtensibleBeanContainer<String, Person> container = new ExtensibleBeanContainer<String, Person>(
                Person.class, Secretary.class);
        Assert.assertTrue(container.getContainerPropertyIds().contains("name"));
        Assert.assertTrue(container.getContainerPropertyIds().contains(
                "employer"));

        container.addBean("e1", new Employee("employee1", "employer1"));
        container.addBean("s1", new Secretary("secretary1", "employer2"));
        Assert.assertEquals("employee1",
                container.getContainerProperty("e1", "name").getValue());
        Assert.assertEquals("secretary1",
                container.getContainerProperty("s1", "name").getValue());
        // the field "employer" is only available for Secretary and its
        // subclasses
        Assert.assertEquals(null,
                container.getContainerProperty("e1", "employer").getValue());
        Assert.assertEquals("employer2",
                container.getContainerProperty("s1", "employer").getValue());
    }

    @Test
    public void testOverloadedField() {
        ExtensibleBeanContainer<String, Person> container = new ExtensibleBeanContainer<String, Person>(
                Person.class, Child.class, MiddleAgedMan.class);
        Assert.assertEquals(Object.class, container.getType("toy"));
    }
}
