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
import jakarta.servlet.http.HttpServletRequest;
import com.example.application.views.MainLayout;

import static com.example.application.views.MainLayout.currentStudent;
import static com.example.application.views.MainLayout.currentTeacher;

@PageTitle("Profile")
@Route(value = "profile", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
public class ProfileView extends Composite<VerticalLayout> {

    private final HttpServletRequest request;

    public ProfileView(HttpServletRequest request) {
        this.request = request;
        H1 h1 = new H1();
        H1 h12 = new H1();
        H1 h13 = new H1();
        H1 h15 = new H1();
        H1 h17 = new H1();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        h1.setText("Name:");
        h1.setWidth("max-content");
        h12.setText("Surname:");
        h12.setWidth("max-content");
        h13.setText("ID:");
        h13.setWidth("max-content");
        h15.setText("Email:");
        h15.setWidth("max-content");
        h17.setText("Role:");
        h17.setWidth("max-content");
        getContent().add(h1);
        getContent().add(h12);
        getContent().add(h13);
        getContent().add(h15);
        getContent().add(h17);

        if(request.isUserInRole("USER")){
            String name = "Name: " + currentStudent.getFirstName();
            String sname = "Surname: " + currentStudent.getSurname();
            String id = "ID: " + currentStudent.getStudentID();
            String email = "Email: " + currentStudent.getEmail();
            h1.setText(name);
            h12.setText(sname);
            h13.setText(id);
            h15.setText(email);
            h17.setText("Role: Student");
        }
        else if (request.isUserInRole("TEACHER")){
            String name = "Name: " + currentTeacher.getFirstName();
            String sname = "Surname: " + currentTeacher.getSurname();
            String id = "ID: " + currentTeacher.getTeacherID();
            String email = "Email: " + currentTeacher.getEmail();
            h1.setText(name);
            h12.setText(sname);
            h13.setText(id);
            h15.setText(email);
            h17.setText("Role: Teacher");
        }
        else{
            h1.setText("Name: Maxwell");
            h12.setText("SurnameL Holloway");
            h13.setText("ID: 101");
            h15.setText("Email: max.h@gmail.com");
            h17.setText("Role: Admin");
        }
    }
}
