package com.example.application.views;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Pane;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.parameters.P;

import javax.swing.border.Border;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.application.views.MainLayout.currentStudent;

@Route(value = "image", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PermitAll
public class ImageView extends VerticalLayout {

    public ImageView(HttpServletRequest request) {
        // Create an image component
        Image image = new Image("https://static.vecteezy.com/system/resources/previews/006/460/116/non_2x/attendance-management-blue-gradient-concept-icon-tracking-discipline-at-work-control-productivity-employee-monitoring-abstract-idea-thin-line-illustration-isolated-outline-color-drawing-vector.jpg", "My Image");
        // Set the width and height of the image
        image.setWidth("1000px");
        image.setHeight("1000px");

        // Add the image component to the layout
        add(image);

        if(request.isUserInRole("USER")){
            VerticalLayout v1 = new VerticalLayout();

            String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
            String username = "postgres";
            String password = "password";

            String sql = "SELECT * FROM \"peerRequest\"";
            try {
                Connection con = DriverManager.getConnection(url, username, password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                while(rs.next()){
                    int reqID = rs.getInt(9);
                    if(rs.getString(7).equals(currentStudent.getStudentID())){
                        H3 message = new H3();
                        H4 warning = new H4("Please Enter the Correct Code");
                        message.setText(rs.getString(1).toUpperCase() + " " + rs.getString(8).toUpperCase() + " has requested that you mark their attendance for " + rs.getString(4) + " " + rs.getString(5));
                        message.getElement().getStyle().setColor("Blue");
                        HorizontalLayout h1 = new HorizontalLayout();
                        Button reject = new Button("Decline Request!");
                        remove(image);
                        Button mark = new Button("Mark Present");
                        DateTimePicker d1 = new DateTimePicker();
                        d1.setLabel("Date and Time");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                        LocalDateTime dateTime = LocalDateTime.parse(rs.getString(3), formatter);
                        d1.setValue(dateTime);
                        TextField subject = new TextField();
                        subject.setLabel("Subject");
                        subject.setValue(rs.getString(4));
                        subject.setReadOnly(true);
                        TextField section = new TextField();
                        section.setLabel("Section");
                        TextField code = new TextField();
                        code.setLabel("Code");
                        h1.setVerticalComponentAlignment(FlexComponent.Alignment.END, mark);
                        h1.setVerticalComponentAlignment(FlexComponent.Alignment.END, reject);

                        section.setValue(rs.getString(5));
                        section.setReadOnly(true);
                        d1.setReadOnly(true);

                        h1.add(d1, subject, section, code, mark, reject);
                        v1.add(message, h1, warning);
                        warning.setVisible(false);
                        String name = rs.getString(4);

                        String dT = rs.getString(3);
                        String askN = rs.getString(1);
                        String sec =rs.getString(5);
                        String subj = rs.getString(4);
                        String askID = rs.getString(6);
                        mark.addClickListener(e ->{
                            if(code.getValue().equals("1111")){
                                warning.setVisible(false);
                                try {
                                    Connection con3 = DriverManager.getConnection(url, username, password);
                                    Statement s3 = con3.createStatement();
                                    String markSQL = "insert into \"attendanceReport\" (\"studentID\", \"courseID\", \"dateTime\", \"courseName\") values ('" +
                                            askID + "', '" + sec +  "', '" + dT +"', '" + subj + "')";
                                    String markAtt = "insert into attendance (\"attendanceID\", \"classAttended\", \"typeOfAttendance\") \n" +
                                            "\tvalues \n" +
                                            "\t((select \"attendanceID\" from \"attendanceReport\" where \"dateTime\" = '" + dT +
                                            "' and \"courseID\" = '" + sec + "'),"  + true + ", 'Peer Marked')" ;
                                    String sqlUpdate = "update attendance set \"classAttended\" = " + true +
                                            " where \"attendanceID\" = (select \"attendanceID\" from \"attendanceReport\" where \"dateTime\" = '" + dT + "')";
                                    String sqlTwo = "select * from \"attendanceReport\" where \"dateTime\" = '" + dT + "'";
                                    ResultSet rs2 = s3.executeQuery(sqlTwo);
                                    if(!rs2.next()){
                                        s3.executeUpdate(markSQL);
                                        s3.executeUpdate(markAtt);
                                        System.out.println("THIS CODE IS RUNNING!");
                                    }else{
                                        System.out.println(rs2.getString(4) + "TESTEST");
                                        s3.executeUpdate(sqlUpdate);
                                    }
                                    con3.close();
                                    message.setText("Attendance Marked");
                                    message.getElement().getStyle().setColor("green");
                                    v1.remove(h1);
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                                try {
                                    Connection con5 = DriverManager.getConnection(url, username, password);
                                    Statement st5 = con5.createStatement();
                                    //String deleteSQL = "delete from \"peerRequest\" where \"requestID\" = " +  String.valueOf(reqID);
                                    String updateStatus = "update \"requestStatus\" set status = 'Marked' where reques = " + reqID;
                                    //st5.execute(deleteSQL);
                                    st5.execute(updateStatus);
                                    con5.close();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                            else{
                                warning.setVisible(true);
                                warning.getElement().getStyle().setColor("red");
                            }
                        });
                        reject.addClickListener(e -> {
                            try {
                                Connection con2 = DriverManager.getConnection(url, username, password);
                                Statement st2 = con2.createStatement();
                                String deleteSQL = "delete from \"peerRequest\" where \"requestID\" = " +  String.valueOf(reqID);
                                st2.execute(deleteSQL);
                                con.close();
                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }
                            v1.remove(h1);
                            message.setText("Request Has Been Denied!");
                            message.getElement().getStyle().setColor("Red");
                        });
                    }
                }

                con.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            add(v1);
        }

    }
}