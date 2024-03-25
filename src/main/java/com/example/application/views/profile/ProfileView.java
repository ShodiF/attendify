package com.example.application.views.profile;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class ProfileView extends Composite<VerticalLayout> {

    public ProfileView() {
        H1 h1 = new H1();
        H1 h12 = new H1();
        H1 h13 = new H1();
        H1 h14 = new H1();
        H1 h15 = new H1();
        H1 h16 = new H1();
        H1 h17 = new H1();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h1.setText("Name:");
        h1.setWidth("max-content");
        h12.setText("Surname:");
        h12.setWidth("max-content");
        h13.setText("ID:");
        h13.setWidth("max-content");
        h14.setText("Phone Number:");
        h14.setWidth("max-content");
        h15.setText("Email:");
        h15.setWidth("max-content");
        h16.setText("Faculty:");
        h16.setWidth("max-content");
        h17.setText("Role:");
        h17.setWidth("max-content");
        getContent().add(h1);
        getContent().add(h12);
        getContent().add(h13);
        getContent().add(h14);
        getContent().add(h15);
        getContent().add(h16);
        getContent().add(h17);
    }
}
