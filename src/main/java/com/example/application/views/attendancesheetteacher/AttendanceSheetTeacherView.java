package com.example.application.views.attendancesheetteacher;
import com.example.application.data.Courses;
import com.example.application.data.Student;
import com.example.application.data.SamplePerson;
import com.example.application.services.SamplePersonService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.Set;
import java.util.HashSet;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static com.example.application.views.MainLayout.currentStudent;
import static com.example.application.views.MainLayout.currentTeacher;

@PageTitle("Attendance Sheet Teacher")
@Route(value = "attSheetTeacher", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("TEACHER")
public class AttendanceSheetTeacherView extends Composite<VerticalLayout> {
    public ArrayList<String> s2 = new ArrayList<>();
    public ArrayList<String> s1 = new ArrayList<>();

    ArrayList<String> dailyCourses = new ArrayList<>();
    ArrayList<String> dailyCourseIDs = new ArrayList<>();
    ArrayList<Courses> courses = new ArrayList<>();

    ArrayList<Student> sendToStudents = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();


    Grid otherStudents = new Grid(Student.class);
    public AttendanceSheetTeacherView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        Select select = new Select();
        Select select2 = new Select();
        DateTimePicker dateTimePicker = new DateTimePicker();
        Button buttonPrimary = new Button();
        getContent().setHeightFull();
        getContent().setWidthFull();
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        select.setLabel("Subject");
        select.setWidth("min-content");
        //setSelectSampleData(select);
        select2.setLabel("Section");
        select2.setWidth("min-content");
        //setSelectSampleData(select2);
        dateTimePicker.setLabel("Date and Time Picker");
        dateTimePicker.setWidth("min-content");
        buttonPrimary.setText("Take Attendace");
        layoutRow.setAlignSelf(FlexComponent.Alignment.END, buttonPrimary);
        buttonPrimary.getStyle().set("flex-grow", "1");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(layoutRow);
        layoutRow.add(dateTimePicker);
        layoutRow.add(select);
        layoutRow.add(select2);
        layoutRow.getElement().getStyle().setMaxHeight("100px");
        getContent().add(otherStudents);
        getContent().add(buttonPrimary);
        buttonPrimary.setWidth("100%");
        buttonPrimary.setMaxHeight("50px");
        otherStudents.setColumns("firstName", "surname", "studentID");
        otherStudents.setSelectionMode(Grid.SelectionMode.MULTI);
        getCourses();

        otherStudents.addSelectionListener(selection -> {
            Set<Student> s1 = selection.getAllSelectedItems();
            ArrayList<Student> selectedPeople = new ArrayList<>(s1);
            if(selection.getAllSelectedItems().size() > 0){
                sendToStudents = selectedPeople;
            }
            System.out.println(sendToStudents);
        });

        buttonPrimary.addClickListener(e -> {
            for(int i = 0; i<students.size(); i++){
                boolean attend = false;
                if(sendToStudents.contains(students.get(i))){
                    attend = true;
                }
                String selectedDateTime = dateTimePicker.getValue().toString();
                String sql = "insert into \"attendanceReport\" (\"studentID\", \"courseID\", \"dateTime\", \"courseName\") values ('" +
                        students.get(i).getStudentID() + "', '" + select2.getValue()+  "', '" + selectedDateTime +"', '" + select.getValue() + "')";
                String sqlTwo = "select * from \"attendanceReport\" where \"dateTime\" = '" + selectedDateTime + "' and \"courseID\" = '"
                        + select2.getValue() + "' and \"studentID\" = '" + students.get(i).getStudentID() + "'" ;
                String sqlThree = "insert into attendance (\"attendanceID\", \"classAttended\", \"typeOfAttendance\") \n" +
                        "\tvalues \n" +
                        "\t((select \"attendanceID\" from \"attendanceReport\" where \"dateTime\" = '" + selectedDateTime +
                        "' and \"courseID\" = '" + select2.getValue() + "' and \"studentID\" = '" + students.get(i).getStudentID()+ "'),"  + attend + ", 'Teacher Marked')" ;

                String sqlUpdate = "update attendance set \"classAttended\" = " + attend +
                        " where \"attendanceID\" = (select \"attendanceID\" from \"attendanceReport\" where \"dateTime\" = '" + selectedDateTime +
                        "' and \"courseID\" = '" + select2.getValue() + "' and \"studentID\" = '" + students.get(i).getStudentID()+ "')";
                System.out.println(sqlUpdate);
                String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
                String username = "postgres";
                String password = "password";
                try {
                    Connection con = DriverManager.getConnection(url, username, password);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sqlTwo);
                    if(!rs.next()){
                        st.executeUpdate(sql);
                        st.executeUpdate(sqlThree);
                    }
                    else{
                        st.executeUpdate(sqlUpdate);
                    }
                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            dateTimePicker.clear();
            select.clear();
            select2.clear();
            ArrayList<Student> tL = new ArrayList<>();
            otherStudents.setItems(tL);
        });

        dateTimePicker.addValueChangeListener(e -> {
            if(!dateTimePicker.isEmpty()){
                dailyCourseIDs.clear();
                dailyCourses.clear();
                String currentDateTime = LocalDateTime.now().toString();
                String selectedDateTime = dateTimePicker.getValue().toString();
                for(int i=0; i<courses.size(); i++){
                    if(courses.get(i).getDayOfStudy().toUpperCase().equals(dateTimePicker.getValue().getDayOfWeek().toString().toUpperCase())){
                        dailyCourses.add(courses.get(i).getCourseName());
                        dailyCourseIDs.add(courses.get(i).getCourseId());
                    }
                }
                select.setItems(dailyCourses);
                select2.setItems(dailyCourseIDs);
            }
        });

//        select.addValueChangeListener(e -> {
//            ArrayList<String> temp = new ArrayList<>();
//            for(int i = 0; i<s1.size(); i++){
//                if(s1.get(i).equals(select.getValue())){
//                    temp.add(s2.get(i));
//                }
//            }
//            select2.setItems(temp);
//        });

        select2.addValueChangeListener(e -> {
          if(!dateTimePicker.isEmpty() && !select.isEmpty() && !select2.isEmpty()){
                otherStudents.setVisible(true);
                String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
                String username = "postgres";
                String password = "password";
                String sql = "select * from schedules where \"courseID\" = '" + select2.getValue() + "'";
                String sql2 = "select * from students";
                students.clear();
                ArrayList<String> peerIDs = new ArrayList<>();
                try {
                    Connection con = DriverManager.getConnection(url, username, password);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    while(rs.next()){
                        peerIDs.add(rs.getString(1));
                    }

                    ResultSet rs2 = st.executeQuery(sql2);
                    while(rs2.next()){
                        if(peerIDs.contains(rs2.getString(4))){
                            students.add(new Student(rs2.getString(1), rs2.getString(2), rs2.getString(4), ""));
                        }
                    }
                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                otherStudents.setItems(students);
            }
            else{
            }
        });
    }


//     private void setSelectSampleData(Select select) {
//        String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
//        String username = "postgres";
//        String password = "password";
//        String sql = "SELECT * from courses where \"teacherID\" = '" + currentTeacher.getTeacherID() + "'";
//        System.out.println(sql);
//        s1.clear();
//        s2.clear();
//        try {
//            Connection con = DriverManager.getConnection(url, username, password);
//            Statement st = con.createStatement();
//            ResultSet att = st.executeQuery(sql);
//            while(att.next()){
//                s1.add(att.getString(1));
//                s2.add(att.getString(2));
//            }
//            con.close();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        Set<String> set = new HashSet<>(s1);
//        ArrayList<String> temp = new ArrayList<>();
//        temp.addAll(set);
//
//        select.setItems(temp);
//    }

    private void getCourses(){
        String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
        String username = "postgres";
        String password = "password";
        String sql = "SELECT * from courses where \"teacherID\" = '" + currentTeacher.getTeacherID() + "'";
        System.out.println(sql);
        s1.clear();
        s2.clear();
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet att = st.executeQuery(sql);
            while(att.next()){
                courses.add(new Courses(att.getString(1),
                        att.getString(2),
                        att.getString(3),
                        att.getString(4),
                        att.getString(5)));
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
