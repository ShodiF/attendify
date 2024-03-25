package com.example.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm login = new LoginForm();
    H1 title = new H1("Attendance Management System");

    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(title, login);

        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        add(
                headerLayout
        );
        login.getStyle().set("max-width", "none");
        login.getStyle().set("min-width", "400px");
        login.getStyle().set("width", "50%");

        getElement().getStyle().set("background-color", "#ADD8E6");

        title.getStyle().set("font-size", "5em");

        setAlignSelf(Alignment.CENTER, headerLayout);
        setAlignSelf(Alignment.CENTER, login);
        setAlignSelf(Alignment.CENTER, title);


    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        System.out.println("BEFORE ENTER");
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
        else{
            login.setError(false);
        }
    }

}
