package com.example.application.views.usermanagement;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import com.example.application.data.Users;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UserManagementViewTest {
 private final Grid<Users> grid = new Grid<>(Users.class, false);

    String url = "jdbc:postgresql://localhost:5432/AttendiftDBS";
    String username = "postgres";
    String password = "password";


     TextField firstName;
     TextField lastName;
     TextField email;
     TextField phone;
     DatePicker dateOfBirth;
     TextField occupation;
     TextField role;

    private ArrayList<Users> usersList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("surname").setAutoWidth(true);
        grid.addColumn("username").setAutoWidth(true);
        grid.addColumn("email").setAutoWidth(true);
        grid.addColumn("id").setAutoWidth(true);
        grid.addColumn("password").setAutoWidth(true);
        grid.addColumn("role").setAutoWidth(true);
    }

    @Test
    void gridIsMade(){
    assertEquals(true, grid.getColumns().size()>1);
    }

    @Test
    void gridIsPopulated(){
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

        assertEquals(true, usersList.size()>1);
    }
}