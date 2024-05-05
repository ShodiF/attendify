package com.example.application.views.attendancesheet;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import java.sql.*;
import java.time.LocalDateTime;

import static com.example.application.views.MainLayout.currentStudent;
import static org.junit.jupiter.api.Assertions.*;

class AttendanceSheetTeacherViewTest {
    public String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
    public Select select;
    public Select select2;
    public Select select3;
    public DateTimePicker dateTimePicker;
    public H3 warning;
    public Button mark;
    public Button peerHelp = new Button("Ask Peer");
    public boolean ButtonWork;
    public TextField reasonForAbsence = new TextField("Reason For Absence: ");
    public Upload upload = new Upload();

    @BeforeEach
    void setUp() {
        select = new Select();
        select2 = new Select();
        dateTimePicker = new DateTimePicker();
        select3 = new Select();


        reasonForAbsence.setVisible(false);
        reasonForAbsence.setVisible(false);

        setSelectSampleDataSubject(select);
        setSelectSampleDataSectionSection(select2);
        select3.setItems("Absent", "Present");

        warning = new H3("Please make sure to fill in all required forms.");
        warning.setVisible(false);

        select3.addValueChangeListener(e ->{
            if(select3.getValue() == "Present"){
                reasonForAbsence.setVisible(false);
                upload.setVisible(false);
            }
            else{
                reasonForAbsence.setVisible(true);
                upload.setVisible(true);
            }
        });

        mark = new Button("Take Attendance");
        mark.addClickListener(buttonClickEvent -> {
            if(select.isEmpty() || select2.isEmpty() || select3.isEmpty() || dateTimePicker.isEmpty()){
                warning.setVisible(true);
                ButtonWork = false;
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
                String username = "postgres";
                String password = "password";
                try {
                    Connection con = DriverManager.getConnection(url, username, password);
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sqlTwo);
                    if(!rs.next()){
                        st.executeUpdate(sql);
                        st.executeUpdate(sqlThree);
                    }else{
                        st.executeUpdate(sqlUpdate);
                    }
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            ButtonWork = true;
        });

        dateTimePicker.addValueChangeListener(event -> {
            String currentDateTime = LocalDateTime.now().toString();
            String selectedDateTime = dateTimePicker.getValue().toString();
            if(selectedDateTime.substring(0,10).equals(currentDateTime.substring(0,10)) && Integer.valueOf(selectedDateTime.substring(11,13)) == Integer.valueOf(currentDateTime.substring(11,13)) ){
                select3.setItems("Present", "Absent");
            }
            else{
                select3.setItems("Absent");
            }
        });
    }

    @Test
    void correctItemSetAtt(){
        dateTimePicker.setValue(LocalDateTime.now());

        assertEquals(0, select3.getItemPosition("Present"));
        assertEquals(1, select3.getItemPosition("Absent"));
    }



    @Test
    void takeAttendanceWorks(){
        mark.click();
        assertEquals(true, ButtonWork);
    }


    private void setSelectSampleDataSubject(Select select) {
        select.setItems("Math", "English", "Computer Science", "History");
    }


    @Test
    void canAccessDatabase() {
        String username = "postgres";
        String password = "password";
        assertDoesNotThrow(() -> {
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.close();
        }, "Connection to the database should be successful");
    }



    private void setSelectSampleDataSectionSection(Select select) {
        select.setItems("4A", "3A", "3P", "5P");
    }

}