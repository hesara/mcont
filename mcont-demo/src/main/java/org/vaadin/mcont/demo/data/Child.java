package org.vaadin.mcont.demo.data;

public class Child extends Person {
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
