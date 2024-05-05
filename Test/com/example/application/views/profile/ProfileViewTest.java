package com.example.application.views.profile;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.application.views.MainLayout.currentStudent;
import static com.example.application.views.MainLayout.currentTeacher;
import static org.junit.jupiter.api.Assertions.*;

class ProfileViewTest {
    String role = "USER";
    String s1;
    String s2;
    String s3;
    String s4;
    @BeforeEach
    void setUp() {
        if(role.equals("USER")){
            s1 = "Student";
            s2 = "Student2";
            s3 = "StudentID";
            s4 = "StudentEmail";
        }
        else if (role.equals("TEACHER")){
            s1 = "T";
            s2 = "T2";
            s3 = "TID";
            s4 = "TEmail";
        }
        else{
            s1 = "Admin";
            s2 = "Admin2";
            s3 = "AdminID";
            s4 = "AdminEmail";
        }
    }

    @Test
    void correctProfileInfo(){
        assertEquals("Student", s1);
        assertEquals("Student2", s2);
        assertEquals("StudentID", s3);
        assertEquals("StudentEmail", s4);
    }
}