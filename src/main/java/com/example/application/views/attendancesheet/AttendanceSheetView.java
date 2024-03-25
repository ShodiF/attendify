package com.example.application.views.attendancesheet;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.application.Application.*;
import static com.example.application.Application.uRoles;
import static com.example.application.views.MainLayout.currentStudent;

@PageTitle("Attendance Sheet")
@Route(value = "attSheet", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("USER")
public class AttendanceSheetView extends Composite<VerticalLayout> {

    public AttendanceSheetView() {
        Button mark = new Button("Take Attendance");
        H3 warning = new H3("Please make sure to fill in all required forms.");
        warning.setVisible(false);
        warning.getStyle().set("color", "red");

        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Select select = new Select();
        Select select2 = new Select();
        DateTimePicker dateTimePicker = new DateTimePicker();
        Select select3 = new Select();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        select.setLabel("Subject");
        select.setWidth("min-content");
        setSelectSampleDataSubject(select);
        select2.setLabel("Section");
        select2.setWidth("min-content");
        setSelectSampleDataSectionSection(select2);
        dateTimePicker.setLabel("Date time picker");
        dateTimePicker.setWidth("min-content");
        select3.setLabel("Attendance");
        select3.setWidth("min-content");
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        layoutColumn2.add(layoutRow2, warning);
        layoutRow2.add(select);
        layoutRow2.add(select2);
        layoutRow2.add(dateTimePicker);
        layoutRow2.add(select3);
        layoutRow2.add(mark);
        layoutRow2.setVerticalComponentAlignment(FlexComponent.Alignment.END, mark);

        select3.addValueChangeListener(event -> {
            System.out.println(dateTimePicker.getValue());
            LocalDateTime now = LocalDateTime.now();

            System.out.println(now);
        });

        dateTimePicker.addValueChangeListener(event -> {
            String currentDateTime = LocalDateTime.now().toString();
            String selectedDateTime = dateTimePicker.getValue().toString();
            if(selectedDateTime.substring(0,13).equals(currentDateTime.substring(0,13))){
                System.out.println("Same day and time");
                select3.setItems("Present", "Absent");
            }
            else{
                System.out.println("NOT");
                select3.setItems("Absent");
            }
        });

        mark.addClickListener(buttonClickEvent -> {
            if(select.isEmpty() || select2.isEmpty() || select3.isEmpty() || dateTimePicker.isEmpty()){
                warning.setVisible(true);
            }
            else{
                boolean attend = false;
                warning.setVisible(false);
                if(select3.getValue() == "Present"){
                    attend = true;
                }
                else{
                    attend = false;
                }
                String selectedDateTime = dateTimePicker.getValue().toString();
                String sql = "insert into \"attendanceReport\" (\"studentID\", \"courseID\", \"dateTime\", \"courseName\") values ('" +
                        currentStudent.getStudentID() + "', '" + select2.getValue()+  "', '" + selectedDateTime +"', '" + select.getValue() + "')";
                String sqlTwo = "select * from \"attendanceReport\" where \"dateTime\" = '" + selectedDateTime + "'";
                String sqlThree = "insert into attendance (\"attendanceID\", \"classAttended\", \"typeOfAttendance\") \n" +
                        "\tvalues \n" +
                        "\t((select \"attendanceID\" from \"attendanceReport\" where \"dateTime\" = '" + selectedDateTime +
                        "' ),"  + attend + ", 'Online')";
                String sqlUpdate = "update attendance set \"classAttended\" = " + attend +
                        " where \"attendanceID\" = (select \"attendanceID\" from \"attendanceReport\" where \"dateTime\" = '" + selectedDateTime + "')";
                System.out.println(sqlUpdate);
                String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
                String username = "postgres";
                String password = "password";
                System.out.println(sql);
                try {
                    Connection con = DriverManager.getConnection(url, username, password);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sqlTwo);
                    if(!rs.next()){
                        st.executeUpdate(sql);
                        st.executeUpdate(sqlThree);
                        System.out.println("THIS CODE IS RUNNING!");
                    }else{
                        System.out.println(rs.getString(4) + "TESTEST");
                        st.executeUpdate(sqlUpdate);
                    }
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setSelectSampleDataSubject(Select select) {
        select.setItems("Math", "English", "Computer Science", "History");
    }

    private void setSelectSampleDataSectionSection(Select select) {
        select.setItems("4A", "3A", "3P", "5P");
    }
    private void setSelectSampleDataSectionAttendance(Select select) {
        select.setItems("Present", "Absent");
    }
}
