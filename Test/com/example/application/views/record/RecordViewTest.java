package com.example.application.views.record;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.select.Select;
import org.junit.jupiter.api.BeforeEach;
import com.example.application.data.AttendanceReport;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RecordViewTest {
    public String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
    public ArrayList<AttendanceReport> listOfAtt = new ArrayList<>();
    public ArrayList<String> subjects = new ArrayList<>();
    public ArrayList<String> cIDs = new ArrayList<>();
    public ArrayList<String> dates = new ArrayList<>();
    public ArrayList<Integer> attId = new ArrayList<>();
    public ArrayList<String> status = new ArrayList<>();
    public Grid basicGrid = new Grid(AttendanceReport.class);
    public Select select = new Select();
    public ArrayList<AttendanceReport> tempList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        basicGrid.setItems();
        basicGrid.setColumns("subject", "courseID", "dateAndTime", "attendanceStatus");

        String sql = "select * from attendance";
        String sql2 = "select * from \"attendanceReport\"";
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

        for(int i = 0; i< subjects.size(); i++){
            listOfAtt.add(
                    new AttendanceReport(subjects.get(i), cIDs.get(i), dates.get(i), status.get(i)));
        }
        basicGrid.setItems(listOfAtt);
        System.out.println(listOfAtt);

        select.addValueChangeListener(event ->{
            tempList.clear();
            for (int i = 0; i<listOfAtt.size(); i++){
                if(listOfAtt.get(i).getSubject().equals(select.getValue())){
                    tempList.add(listOfAtt.get(i));
                }
            }
            basicGrid.setItems(tempList);
        });
    }

    @Test
    void gridIsPopulated(){
        assertEquals("English", listOfAtt.get(0).getSubject());
        assertEquals("3P", listOfAtt.get(0).getCourseID());
        assertEquals("2024-03-15 10:00", listOfAtt.get(0).getDateAndTime());
        assertEquals("Present", listOfAtt.get(0).getAttendanceStatus());
    }

    @Test
    void selectionIsWorking(){
        select.setValue("English");
        assertEquals("English", tempList.get(0).getSubject());
        assertEquals("3P", tempList.get(0).getCourseID());
        assertEquals("2024-03-15 10:00", tempList.get(0).getDateAndTime());
        assertEquals("Present", tempList.get(0).getAttendanceStatus());

        select.setValue("Computer Science");
        assertEquals("Computer Science", tempList.get(0).getSubject());
        assertEquals("5P", tempList.get(0).getCourseID());
        assertEquals("2024-03-15 13:00", tempList.get(0).getDateAndTime());
        assertEquals("Present", tempList.get(0).getAttendanceStatus());

        select.setValue("History");
        assertEquals("History", tempList.get(0).getSubject());
        assertEquals("5P", tempList.get(0).getCourseID());
        assertEquals("2024-03-22 02:00", tempList.get(0).getDateAndTime());
        assertEquals("Absent", tempList.get(0).getAttendanceStatus());

        select.setValue("Math");
        assertEquals("Math", tempList.get(0).getSubject());
        assertEquals("4A", tempList.get(0).getCourseID());
        assertEquals("2024-03-25 14:00", tempList.get(0).getDateAndTime());
        assertEquals("Absent", tempList.get(0).getAttendanceStatus());

        select.setValue("Jibberish");
        assertEquals(true, tempList.isEmpty());
    }
    @Test
    void timeIsCorrect(){
        select.setValue("Math");
        assertEquals("2024-03-25 14:00", tempList.get(0).getDateAndTime());
        select.setValue("History");
        assertEquals("2024-03-22 02:00", tempList.get(0).getDateAndTime());
    }

}