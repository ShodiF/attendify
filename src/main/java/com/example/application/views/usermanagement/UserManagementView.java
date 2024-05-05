package com.example.application.views.usermanagement;

import com.example.application.data.SamplePerson;
import com.example.application.data.Users;
import com.example.application.services.SamplePersonService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("User Management")
@Route(value = "userManage/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ADMIN")
public class UserManagementView extends Div {
    private final Grid<Users> grid = new Grid<>(Users.class, false);

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextField phone;
    private DatePicker dateOfBirth;
    private TextField occupation;
    private TextField role;

    private ArrayList<Users> usersList = new ArrayList<>();


    private final Button cancel = new Button("Cancel");
    private final Button save = new Button("Save");

    //private final BeanValidationBinder<Users> binder;

    private Users user;

    public UserManagementView() {
        addClassNames("user-management-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
       // createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("surname").setAutoWidth(true);
        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("password").setAutoWidth(true);
        grid.addColumn("role").setAutoWidth(true);


        String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
        String username = "postgres";
        String password = "password";

        String sql = "Select * from users";
        String sql2 = "Select * from teachers";
        String sql3 = "Select * from admins";
        String sql4 = "Select * from students";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                if(rs.getString(3).equals("USER")){
                    Statement s2 = con.createStatement();
                    ResultSet rs2 = s2.executeQuery(sql4);
                    while(rs2.next()){
                        if(rs2.getString(3).equals(rs.getString(1))){
                            usersList.add(new Users(
                                    rs2.getString(3),
                                    rs.getString(2),
                                    "User",
                                    rs2.getString(1),
                                    rs2.getString(2),
                                    rs2.getString(5),
                                    rs2.getString(4)));
                        }
                    }
                }
                else if(rs.getString(3).equals("TEACHER")){
                    Statement s3 = con.createStatement();
                    ResultSet rs3 = s3.executeQuery(sql2);
                    while(rs3.next()){
                        if(rs3.getString(3).equals(rs.getString(1))){
                            usersList.add(new Users(
                                    rs3.getString(3),
                                    rs.getString(2),
                                    "Teacher",
                                    rs3.getString(1),
                                    rs3.getString(2),
                                    rs3.getString(5),
                                    rs3.getString(4)
                            ));
                        }
                    }
                }else{
                    Statement s4 = con.createStatement();
                    ResultSet rs4 = s4.executeQuery(sql3);
                    while(rs4.next()){
                        if(rs4.getString(3).equals(rs.getString(1))){
                            usersList.add(new Users(
                                    rs4.getString(3),
                                    rs.getString(2),
                                    "Admin",
                                    rs4.getString(1),
                                    rs4.getString(2),
                                    rs4.getString(5),
                                    rs4.getString(4)
                            ));
                        }
                    }
                }
            }

            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        grid.setItems(usersList);



        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {

        });

        // Configure Form
        //binder = new BeanValidationBinder<>(Users.class);

        // Bind fields. This is where you'd define e.g. validation rules
        //binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {

        });
    }

//    private void createEditorLayout(SplitLayout splitLayout) {
//        Div editorLayoutDiv = new Div();
//        editorLayoutDiv.setClassName("editor-layout");
//
//        Div editorDiv = new Div();
//        editorDiv.setClassName("editor");
//        editorLayoutDiv.add(editorDiv);
//
//        FormLayout formLayout = new FormLayout();
////        firstName = new TextField("First Name");
////        lastName = new TextField("Last Name");
////        email = new TextField("Email");
////        username = new TextField("Phone");
////        id = new DatePicker("Date Of Birth");
////        occupation = new TextField("Occupation");
////        role = new TextField("Role");
////        important = new Checkbox("Important");
////        formLayout.add(firstName, lastName, email, phone, dateOfBirth, occupation, role, important);
//
//        editorDiv.add(formLayout);
//        createButtonLayout(editorLayoutDiv);
//
//        splitLayout.addToSecondary(editorLayoutDiv);
//    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Users value) {
        this.user = value;
        //binder.readBean(this.user);
    }
}
