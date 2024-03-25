package com.example.application.views.record;

import com.example.application.data.AttendanceReport;
import com.example.application.data.SamplePerson;
import com.example.application.data.Student;
import com.example.application.services.SamplePersonService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.util.SuspendResumeLock;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;

import static com.example.application.Application.*;

@PageTitle("Record")
@Route(value = "record", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("USER")
public class RecordView extends Composite<VerticalLayout> {

    public RecordView() {
        Select select = new Select();
        Grid basicGrid = new Grid(AttendanceReport.class);

        ArrayList<AttendanceReport> listOfAtt = new ArrayList<>();
        ArrayList<String> subjects = new ArrayList<>();
        ArrayList<String> cIDs = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Integer> attId = new ArrayList<>();
        ArrayList<String> status = new ArrayList<>();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        select.setLabel("Subject");
        select.setWidth("min-content");
        setSelectSampleData(select);
        basicGrid.setWidth("100%");
        basicGrid.getStyle().set("flex-grow", "0");

        basicGrid.setItems();
        basicGrid.setColumns("subject", "courseID", "dateAndTime", "attendanceStatus");

        //setGridSampleData(basicGrid);
        getContent().add(select);
        getContent().add(basicGrid);

        String sql = "select * from attendance";
        String sql2 = "select * from \"attendanceReport\"";
        String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
        String username = "postgres";
        String password = "password";
        System.out.println(sql);
        System.out.println(sql2);
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            Statement st = con.createStatement();
            ResultSet att = st.executeQuery(sql2);
            while(att.next()){
                String subject = att.getString(6);
                String cID = att.getString(2);
                String dateTime = att.getString(4);
                String n = dateTime.replace('T', ' ');
                subjects.add(subject);
                cIDs.add(cID);
                dates.add(n);
                attId.add(att.getInt(5));
            }
            att = st.executeQuery(sql);
            int count = 0;
            while(att.next()){
                if(att.getInt(3) == attId.get(count)){
                    if(att.getBoolean(1)){
                        status.add("Present");
                    }
                    else{
                        status.add("Absent");
                    }
                }
                count++;
            }
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(subjects);
        System.out.println(cIDs);
        System.out.println(dates);
        System.out.println(attId);
        System.out.println(status);

        for(int i = 0; i< subjects.size(); i++){
            listOfAtt.add(
                    new AttendanceReport(subjects.get(i), cIDs.get(i), dates.get(i), status.get(i)));
        }
        basicGrid.setItems(listOfAtt);
        System.out.println(listOfAtt);

        select.addValueChangeListener(event ->{
            ArrayList<AttendanceReport> tempList = new ArrayList<>();
            for (int i = 0; i<listOfAtt.size(); i++){
                if(listOfAtt.get(i).getSubject().equals(select.getValue())){
                    tempList.add(listOfAtt.get(i));
                }
            }
            basicGrid.setItems(tempList);
        });

    }

    private void setSelectSampleData(Select select) {
        select.setItems("Math", "English", "Computer Science", "History");
    }

    @Autowired()
    private SamplePersonService samplePersonService;
}
