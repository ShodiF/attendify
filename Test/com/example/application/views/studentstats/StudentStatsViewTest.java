package com.example.application.views.studentstats;

import com.example.application.data.Courses;
import com.example.application.data.Student;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StudentStatsViewTest {

    ArrayList<Courses> courses = new ArrayList<>();
    ArrayList<Student> students = new ArrayList<>();

    @BeforeEach
    void setUp() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        H1 h1 = new H1();

        layoutRow.setWidthFull();

        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("100px");
        h1.setText("Statistics:");
        h1.setWidth("max-content");
        h1.setHeight("100px");


        Grid courseStat = new Grid(Courses.class);
        courseStat.setColumns("courseName", "courseId", "absentRate");
        String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
        String username = "postgres";
        String password = "password";
        String sql = "Select * from courses";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet r = st.executeQuery(sql);
            while(r.next()){
                courses.add(new Courses(r.getString(1), r.getString(2), 0.0));
            }

            for(int i=0; i< courses.size(); i++){
                ArrayList<Integer> attID = new ArrayList<>();
                double total = 0;
                double absent = 0;

                String sql2 = "select * from \"attendanceReport\" where \"courseID\" = '" + courses.get(i).getCourseId() + "'";
                Statement st2 = con.createStatement();
                ResultSet rs = st2.executeQuery(sql2);
                while(rs.next()){
                    attID.add(rs.getInt(5));
                }

                String sql3 = "select * from attendance";
                ResultSet rs2 = st2.executeQuery(sql3);
                while(rs2.next()){
                    if(attID.contains(rs2.getInt(3))){
                        if(rs2.getBoolean(1)){
                            total++;
                        }
                        else{
                            total++;
                            absent++;
                        }
                    }
                }
                double avg = (absent/total)*100;
                if(absent == 0 || total == 0){
                    avg = 0;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String s = decimalFormat.format(avg);
                courses.get(i).setAbsentRate(Double.parseDouble(s));
            }
            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        courseStat.setItems(courses);
        layoutRow.add(courseStat);
        courseStat.setWidth("50%");


        Grid studentStat = new Grid(Student.class);
        studentStat.setColumns("firstName", "surname", "studentID","absentRate");

        String allSt = "Select * from students";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet r = st.executeQuery(allSt);
            while(r.next()){
                students.add(new Student(r.getString(1), r.getString(2), r.getString(4), 0.0));
            }

            for(int i=0; i< students.size(); i++){
                ArrayList<Integer> attID = new ArrayList<>();
                double total = 0;
                double absent = 0;

                String sql2 = "select * from \"attendanceReport\" where \"studentID\" = '" + students.get(i).getStudentID() + "'";
                Statement st2 = con.createStatement();
                ResultSet rs = st2.executeQuery(sql2);
                while(rs.next()){
                    attID.add(rs.getInt(5));
                }

                String sql3 = "select * from attendance";
                ResultSet rs2 = st2.executeQuery(sql3);
                while(rs2.next()){
                    if(attID.contains(rs2.getInt(3))){
                        if(rs2.getBoolean(1)){
                            total++;
                        }
                        else{
                            total++;
                            absent++;
                        }
                    }
                }
                double avg = (absent/total)*100;
                if(absent == 0 || total == 0){
                    avg = 0;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String s = decimalFormat.format(avg);
                students.get(i).setAbsentRate(Double.parseDouble(s));
            }
            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        studentStat.setItems(students);
        layoutRow.add(studentStat);
        layoutRow.setHeight("100%");
        studentStat.setWidth("50%");
    }

    @Test
    void courseGridIsPopulated(){
        assertEquals(true, courses.size()>0);
    }

    @Test
    void studentGridIsPopulated(){
        assertEquals(true, students.size()>0);
    }

}