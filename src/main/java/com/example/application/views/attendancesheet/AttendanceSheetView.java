package com.example.application.views.attendancesheet;

import com.example.application.data.Courses;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.cookieconsent.CookieConsent;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import com.example.application.data.Student;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.application.Application.*;
import static com.example.application.Application.uRoles;
import static com.example.application.views.MainLayout.currentStudent;

@PageTitle("Attendance Sheet")
@Route(value = "attSheet", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("USER")
public class AttendanceSheetView extends Composite<VerticalLayout> {
    String fileName;
    ArrayList<String> courseIDs = new ArrayList<>();
    ArrayList<Courses> courses = new ArrayList<>();

    ArrayList<String> dailyCourses = new ArrayList<>();
    ArrayList<String> dailyCourseIDs = new ArrayList<>();
    ArrayList<Student> sendToStudents = new ArrayList<>();

    Grid otherStudents = new Grid(Student.class);

    public AttendanceSheetView() {
        Button mark = new Button("Take Attendance");
        Button askPeer = new Button("Permit a Friend to Take Your Attendance");
        Button sendRequest = new Button("Send Request to All Selected Students");
        sendRequest.setWidth("100%");
        sendRequest.setVisible(false);
        Student requestStduent = new Student();
        H3 warning = new H3("Please make sure to fill in all required forms.");
        warning.setVisible(false);
        warning.getStyle().set("color", "red");

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setVisible(false);

        upload.addSucceededListener(event -> {
            fileName = event.getFileName();
        });

        getCourses();

        TextField textField = new TextField("Code");
        TextField reasonForAbsence = new TextField("Reason For Absence: ");
        reasonForAbsence.getElement().getStyle().setWidth("300px");
        reasonForAbsence.setVisible(false);

        HorizontalLayout specialReason = new HorizontalLayout(upload, reasonForAbsence);
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
        select2.setLabel("Section");
        select2.setWidth("min-content");
        dateTimePicker.setLabel("Date time picker");
        dateTimePicker.setWidth("min-content");
        select3.setLabel("Attendance");
        select3.setWidth("min-content");
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn2);
        H2 successSend = new H2("Request Sent");
        successSend.getElement().getStyle().setColor("Green");
        successSend.setVisible(false);
        layoutColumn2.add(layoutRow2, warning, specialReason, askPeer, successSend);
        layoutRow2.add(dateTimePicker);
        layoutRow2.add(select);
        layoutRow2.add(select2);
        layoutRow2.add(select3);
        layoutRow2.add(textField);
        layoutRow2.add(mark);
        layoutRow2.setVerticalComponentAlignment(FlexComponent.Alignment.END, mark);
        HorizontalLayout h3 = new HorizontalLayout();
        H2 header2 = new H2("To permit a peer to take your attendance please fill in the date, time, subject and section of the class.");
        TextField peersID = new TextField("Peer ID");
        peersID.setVisible(false);
        otherStudents.setVisible(false);
        otherStudents.setSelectionMode(Grid.SelectionMode.MULTI);

        select.addValueChangeListener(e -> {
           otherStudents.setVisible(false);
        });

        select2.addValueChangeListener(e -> {
            otherStudents.setVisible(false);
        });

        otherStudents.addSelectionListener(selection -> {
            Set<Student> s1 = selection.getAllSelectedItems();
            ArrayList<Student> selectedPeople = new ArrayList<>(s1);
            if(selection.getAllSelectedItems().size() > 0){
                sendRequest.setVisible(true);
                sendToStudents = selectedPeople;
            }
            else{
                sendRequest.setVisible(false);
            }
        });

        askPeer.addClickListener(e -> {
            layoutColumn2.add(header2, otherStudents, sendRequest);
            successSend.setVisible(false);
            if(!dateTimePicker.isEmpty() && !select.isEmpty() && !select2.isEmpty()){
                header2.setVisible(false);
                otherStudents.setVisible(true);
                String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
                String username = "postgres";
                String password = "password";
                String sql = "select * from schedules where \"courseID\" = '" + select2.getValue() + "'";
                String sql2 = "select * from students";
                ArrayList<Student> students = new ArrayList<>();
                ArrayList<String> peerIDs = new ArrayList<>();
                ArrayList<Integer> reqIDs = new ArrayList<>();
                ArrayList<Integer> index = new ArrayList<>();
                otherStudents.setColumns("firstName", "surname", "studentID", "status");
                try {
                    Connection con = DriverManager.getConnection(url, username, password);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){
                        if(rs.getString(1).equals(currentStudent.getStudentID())){
                            System.out.println("This Student");
                        }
                        else{
                            peerIDs.add(rs.getString(1));
                        }
                    }

                    ResultSet rs2 = st.executeQuery(sql2);
                    while(rs2.next()){
                        if(peerIDs.contains(rs2.getString(4))){
                            students.add(new Student(rs2.getString(1), rs2.getString(2), rs2.getString(4), ""));
                        }
                    }
                    for(int i =0; i<students.size(); i++){
                        String sql3 = "Select * from \"peerRequest\" where \"dateTime\" = '" + dateTimePicker.getValue().toString() + "' and section = '" + select2.getValue() + "' and \"recieveID\" = '" + students.get(i).getStudentID() + "'";
                        ResultSet rs3 = st.executeQuery(sql3);
                        while(rs3.next()){
                            reqIDs.add(rs3.getInt(9));
                            index.add(i);
                        }
                    }
                    System.out.println("REACHING THIS");
                    for(int i=0; i<index.size(); i++){
                        if(reqIDs.size()>0){
                            String sql4 = "select * from \"requestStatus\" where reques = " + String.valueOf(reqIDs.get(i));
                            ResultSet rs4 = st.executeQuery(sql4);
                            while(rs4.next()){
                                students.get(index.get(i)).setStatus(rs4.getString(2));
                            }
                        }
                    }

                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                otherStudents.setItems(students);
            }
            else{
                header2.setVisible(true);
            }
        });

        sendRequest.addClickListener(e ->{
            System.out.println(sendToStudents);
            for(int i =0; i<sendToStudents.size(); i++){
                String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
                String username = "postgres";
                String password = "password";

                String sql = "SELECT * from students where \"studentID\" = '" + sendToStudents.get(i).getStudentID() + "'";
                try {
                    Connection con = DriverManager.getConnection(url, username, password);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);

                    while(rs.next()){
                        requestStduent.setFirstName(rs.getString(1));
                        requestStduent.setSurname(rs.getString(2));
                        requestStduent.setUsername(rs.getString(3));
                        requestStduent.setStudentID(rs.getString(4));
                        requestStduent.setEmail(rs.getString(5));
                        requestStduent.setBirthday(rs.getString(6));
                        requestStduent.setYearOfStudy(rs.getInt(7));
                    }

                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    boolean exists = false;
                    Connection con = DriverManager.getConnection(url, username, password);
                    Statement st = con.createStatement();
                    String check = "Select * from \"peerRequest\" where \"dateTime\" = '" + dateTimePicker.getValue().toString() + "' and section = '" + select2.getValue() + "' and \"recieveID\" = '" + requestStduent.getStudentID() + "'" ;
                    ResultSet res = st.executeQuery(check);
                    while (res.next()){
                        exists = true;
                    }
                    if(exists){
                        System.out.println("Exists already");
                    }
                    else{
                        String sendData = "insert into \"peerRequest\" " +
                                "(\"askName\",\"recieveName\",\"dateTime\", \"subject\",\"section\", \"askID\", \"recieveID\", \"askSurname\") values ('"
                                + currentStudent.getFirstName() + "', '" + requestStduent.getFirstName() + requestStduent.getSurname() + "', '"
                                + dateTimePicker.getValue().toString() + "', '" + select.getValue() + "', '" + select2.getValue() + "', '" + currentStudent.getStudentID() + "', '"
                                + requestStduent.getStudentID() + "', '" + currentStudent.getSurname() + "');";


                        st.execute(sendData);
                        String reqInfo = "Select * from \"peerRequest\" where \"dateTime\" = '" + dateTimePicker.getValue().toString() + "' and section = '" + select2.getValue() + "'";

                        String sqlTest = "Select * from \"requestStatus\"";
                        ArrayList<Integer> s = new ArrayList<>();
                        ResultSet r2 = st.executeQuery(sqlTest);
                        while(r2.next()){
                            s.add(r2.getInt(1));
                        }

                        ResultSet r = st.executeQuery(reqInfo);
                        while(r.next()){
                            if(s.contains(r.getInt(9))){
                                System.out.println("Skip");
                            }else{
                                String sendInfo = "insert into \"requestStatus\" (reques, status) values (" + r.getInt(9) + ", 'Sent')";
                                Statement st3 = con.createStatement();
                                st3.execute(sendInfo);
                            }
                        }
                        otherStudents.setVisible(false);
                        sendRequest.setVisible(false);
                        successSend.setVisible(true);
                    }
                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        select3.addValueChangeListener(event -> {
            if(select3.getValue() == "Absent"){
                upload.setVisible(true);
                reasonForAbsence.setVisible(true);
            }
            else{
                upload.setVisible(false);
                reasonForAbsence.setVisible(false);
            }
        });

        dateTimePicker.addValueChangeListener(event -> {
            otherStudents.setVisible(false);
            dailyCourseIDs.clear();
            dailyCourses.clear();
            String currentDateTime = LocalDateTime.now().toString();
            String selectedDateTime = dateTimePicker.getValue().toString();
            if(selectedDateTime.substring(0,10).equals(currentDateTime.substring(0,10)) && Integer.valueOf(selectedDateTime.substring(11,13)) == Integer.valueOf(currentDateTime.substring(11,13))-1 ){
                System.out.println("Same day and time");
                select3.setItems("Present", "Absent");
            }
            else{
                System.out.println("NOT");
                select3.setItems("Absent");
            }
            for(int i=0; i<courses.size(); i++){
                if(courses.get(i).getDayOfStudy().toUpperCase().equals(dateTimePicker.getValue().getDayOfWeek().toString().toUpperCase())){
                    dailyCourses.add(courses.get(i).getCourseName());
                    dailyCourseIDs.add(courses.get(i).getCourseId());
                    System.out.println("HERE");
                }
            }
            select.setItems(dailyCourses);
            select2.setItems(dailyCourseIDs);
        });

        select.addValueChangeListener(e ->{
            ArrayList<String> tempList = new ArrayList<>();
            for(int i = 0; i<dailyCourses.size(); i++){
                if(dailyCourses.get(i).equals(select.getValue())) {
                    tempList.add(dailyCourseIDs.get(i));
                }
            }
            select2.setItems(tempList);
        });

        mark.addClickListener(buttonClickEvent -> {
            if(select.isEmpty() || select2.isEmpty() || select3.isEmpty() || dateTimePicker.isEmpty() || textField.isEmpty()){
                warning.setText("Please make sure to fill in all required forms.");
                warning.setVisible(true);
            }
            else if(textField.getValue().equals("1111")){
                InputStream inputStream = buffer.getInputStream(fileName);

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
                        "' ),"  + attend + ", 'Online')" ;
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
                select.clear();
                select2.clear();
                select3.clear();
                textField.clear();
            }
            else{
                warning.setText("Incorrect Code! Review and try again!");
                warning.setVisible(true);
            }
        });


    }

    private void getCourses(){
        String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
        String username = "postgres";
        String password = "password";

        String sql = "SELECT * FROM schedules where \"studentID\" = '" + currentStudent.getStudentID() + "'";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()){
                courseIDs.add(rs.getString(2));
            }

            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(courseIDs);

        for(int i = 0; i < courseIDs.size(); i++){
            String sql2 = "SELECT * from courses where \"courseID\" = '" + courseIDs.get(i) + "'";
            try {
                Connection con = DriverManager.getConnection(url, username, password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql2);
                while(rs.next()){
                    courses.add(new Courses(rs.getString(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5)));
                }
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(courses);

    }
}
