package com.example.application.data;

public class Teacher {
    String firstName;
    String surname;
    String username;
    String teacherID;
    String email;

    public Teacher(){

    }

    public Teacher(String firstName, String surname, String username, String teacherID, String email) {
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.teacherID = teacherID;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}