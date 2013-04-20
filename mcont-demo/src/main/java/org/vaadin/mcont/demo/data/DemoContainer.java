package org.vaadin.mcont.demo.data;

import com.vaadin.data.util.ExtensibleBeanContainer;

public class DemoContainer extends ExtensibleBeanContainer {

    public DemoContainer() {
        super(Person.class, Employee.class, Child.class);

        addBean("person1", new Person("person1"));
        addBean("employee1", new Employee("employee1", "employer1"));
        addBean("child1", new Child("child1", "rubber duck"));
    }

}
