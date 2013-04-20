package org.vaadin.mcont.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("demo")
@Title("MCont Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        Table table = new Table();
        // TODO create and set container

        final VerticalLayout layout = new VerticalLayout();
        layout.setStyleName("demoContentLayout");
        layout.setSizeFull();
        layout.addComponent(table);
        setContent(layout);

    }

}
