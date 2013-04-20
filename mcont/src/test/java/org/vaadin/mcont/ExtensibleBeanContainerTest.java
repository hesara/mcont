package org.vaadin.mcont;

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

    private ExtensibleBeanContainer<String, Person> createContainer() {
        ExtensibleBeanContainer<String, Person> container = new ExtensibleBeanContainer<String, Person>(
                Person.class);
        return container;
    }

    @Test
    public void testCreateContainer() {
        createContainer();
    }
}
