package com.example.application.data;

import org.springframework.stereotype.Component;

@Component
public class Student {

    private String firstName;
    private String surname;
    private String username;
    private String studentID;
    private String email;
    private String birthday;
    private int yearOfStudy;
    private String attendance;

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
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

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void details(){
        System.out.println("First Name: " + this.firstName);
        System.out.println("Surname: " + this.surname);
        System.out.println("Username: " + this.username);
        System.out.println("Student ID: " + this.studentID);
        System.out.println("Email: " + this.email);
        System.out.println("Birthday: " + this.birthday);
        System.out.println("Year Of Study: " + this.yearOfStudy);
    }
}
