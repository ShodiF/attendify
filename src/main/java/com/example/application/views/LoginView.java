package com.example.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Route("login")
@PageTitle("Login")

public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm login = new LoginForm();
    H1 title = new H1("Attendance Management System");
    H1 bannerText = new H1("Attendify");

    public LoginView(){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        HorizontalLayout banner = new HorizontalLayout();
        HorizontalLayout placeHolder = new HorizontalLayout();


        banner.add(bannerText);
        banner.setHeight("100px");
        banner.setAlignItems(Alignment.CENTER);
        banner.getElement().getStyle().setPaddingLeft("30px");
        bannerText.getStyle().set("font-size", "2em");


        banner.setPadding(false);
        banner.setSpacing(false);
        banner.setMargin(false);

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.add(title, login);

        setPadding(false);

        headerLayout.setHeight("1400px");
        banner.getElement().getStyle().set("background-color", "#FFFFFF");
        banner.setWidth("2100px");
        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        add(
                banner,
                headerLayout
        );
        login.getStyle().set("max-width", "none");
        login.getStyle().set("min-width", "400px");
        login.getStyle().set("width", "50%");

        getElement().getStyle().set("background-color", "#e0eeee");


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
