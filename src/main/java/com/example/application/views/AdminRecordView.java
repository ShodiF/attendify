package com.example.application.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

import  com.example.application.data.AttendanceReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.example.application.views.MainLayout.currentTeacher;

@PageTitle("Admin Record")
@Route(value = "adminRecord", layout = MainLayout.class)
@Uses(Icon.class)
@PermitAll
@RolesAllowed("ADMIN")

public class AdminRecordView extends Composite<VerticalLayout> {

    public ArrayList<String> s2 = new ArrayList<>();
    public ArrayList<String> s1 = new ArrayList<>();

    public AdminRecordView() {
        Select select = new Select();
        Select select2 = new Select();
        Grid basicGrid = new Grid(AttendanceReport.class);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        select.setLabel("Subject");
        select.setWidth("min-content");
        setSelectSampleData(select);
        select2.setLabel("Course ID");
        select2.setWidth("min-content");
        basicGrid.setWidth("100%");
        basicGrid.getStyle().set("flex-grow", "0");

        basicGrid.setItems();
        basicGrid.setColumns("studentName", "studentID", "dateAndTime", "attendanceStatus", "typeOfAtt","reasonForAbsence");

        //setGridSampleData(basicGrid);
        HorizontalLayout h1 = new HorizontalLayout();
        h1.add(select);
        h1.add(select2);
        getContent().add(h1);
        getContent().add(basicGrid);

        select.addValueChangeListener(e -> {
            ArrayList<String> temp = new ArrayList<>();
            for(int i = 0; i<s1.size(); i++){
                if(s1.get(i).equals(select.getValue())){
                    temp.add(s2.get(i));
                }
            }
            select2.setItems(temp);
        });

        select2.addValueChangeListener(e ->{
            ArrayList<String> subs = new ArrayList<>();
            ArrayList<String> couID = new ArrayList<>();
            ArrayList<String> dateT = new ArrayList<>();
            ArrayList<Integer> atID = new ArrayList<>();
            ArrayList<String> sID = new ArrayList<>();
            ArrayList<String> rID = new ArrayList<>();
            ArrayList<String> stats = new ArrayList<>();
            ArrayList<String> firstName = new ArrayList<>();
            ArrayList<String> lastName = new ArrayList<>();
            ArrayList<String> type = new ArrayList<>();

            ArrayList<AttendanceReport> reports = new ArrayList<>();
            String sql2 = "select * from \"attendanceReport\" where \"courseID\" = '" + select2.getValue() + "'";
            String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
            String username = "postgres";
            String password = "password";


            try {
                Connection con = DriverManager.getConnection(url, username, password);
                Statement st = con.createStatement();
                ResultSet att = st.executeQuery(sql2);
                while(att.next()){
                    String subject = att.getString(6);
                    String cID = att.getString(2);
                    String dateTime = att.getString(4);
                    String n = dateTime.replace('T', ' ');
                    subs.add(subject);
                    couID.add(cID);
                    dateT.add(n);
                    atID.add(att.getInt(5));
                    sID.add(att.getString(1));
                    rID.add(att.getString(3));
                }

                for(int i=0; i<atID.size(); i++){
                    String sql = "SELECT * FROM attendance where \"attendanceID\" = '" + atID.get(i) + "'";
                    att = st.executeQuery(sql);
                    att.next();
                    if(att.getBoolean(1)){
                        stats.add("Present");
                        type.add(att.getString(2));
                    }
                    else{
                        stats.add("Absent");
                        type.add(att.getString(2));
                    }
                }

                for(int i=0; i<sID.size(); i++){
                    att = st.executeQuery("SELECT * FROM students where \"studentID\" = '" + sID.get(i) + "'");
                    while(att.next()){
                        firstName.add(att.getString(1));
                        lastName.add(att.getString(2));
                    }
                }
                con.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            for(int i = 0; i<couID.size(); i++){
                String sName = firstName.get(i) + " " + lastName.get(i);
                AttendanceReport r = new AttendanceReport(subs.get(i), couID.get(i), dateT.get(i),stats.get(i), sName,  sID.get(i), rID.get(i), null, type.get(i));
                reports.add(r);
            }
            basicGrid.setItems(reports);
        });
    }

    private void setSelectSampleData(Select select) {
        String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
        String username = "postgres";
        String password = "password";
        String sql = "SELECT * from courses";
        System.out.println(sql);
        s1.clear();
        s2.clear();
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet att = st.executeQuery(sql);
            while(att.next()){
                s1.add(att.getString(1));
                s2.add(att.getString(2));
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Set<String> set = new HashSet<>(s1);
        ArrayList<String> temp = new ArrayList<>();
        temp.addAll(set);

        select.setItems(temp);
    }
}