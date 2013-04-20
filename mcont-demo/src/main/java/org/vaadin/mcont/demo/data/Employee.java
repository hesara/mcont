package org.vaadin.mcont.demo.data;

public class Employee extends Person {
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
