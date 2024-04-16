package com.example.application.views;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.application.data.Student;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class MainLayoutTest {
    private static final String URL = "jdbc:postgresql://localhost:5432/AttendiftDBS";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "password";

    public Student s = new Student();
    public Student currentStudent = new Student();

    @BeforeEach
    void setUp() {
        s.setFirstName("John");
        s.setSurname("Smith");
        s.setUsername("J.Smith");
        s.setYearOfStudy(3);
        s.setBirthday("March 10, 2000");
        s.setEmail("jSmith@gmail.com");
        s.setStudentID("1111");
        s.setAttendance("Absent");


        String susername = "user";
        currentStudent.setUsername(susername);

        String sql = "select * from students where username = '" + susername + "'";
        System.out.println(sql);
        String username = "postgres";
        String password = "password";
        try {
            Connection con = DriverManager.getConnection(URL, username, password);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            currentStudent.setFirstName(rs.getString(1));
            currentStudent.setSurname(rs.getString(2));
            currentStudent.setStudentID(rs.getString(4));
            currentStudent.setEmail(rs.getString(5));
            currentStudent.setBirthday(rs.getString(6));
            currentStudent.setYearOfStudy(rs.getInt(7));

            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void userCorrectTraits() {
        assertEquals("John", s.getFirstName());
        assertEquals("Smith", s.getSurname());
        assertEquals("J.Smith", s.getUsername());
        assertEquals(3, s.getYearOfStudy());
        assertEquals("March 10, 2000", s.getBirthday());
        assertEquals("jSmith@gmail.com", s.getEmail());
        assertEquals("1111", s.getStudentID());
        assertEquals("Absent", s.getAttendance());
    }

    @Test
    void canAccessDatabase() {
        assertDoesNotThrow(() -> {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.close();
        }, "Connection to the database should be successful");
    }

    @Test
    void databasePopulatesStudent(){
        assertEquals("john", currentStudent.getFirstName());
        assertEquals("smith", currentStudent.getSurname());
        assertEquals("user", currentStudent.getUsername());
        assertEquals(1, currentStudent.getYearOfStudy());
        assertEquals("2005-04-04", currentStudent.getBirthday());
        assertEquals("j.smith@gmail.com", currentStudent.getEmail());
        assertEquals("0001", currentStudent.getStudentID());
    }
}