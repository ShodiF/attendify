package com.example.application.data;

import org.springframework.stereotype.Component;

@Component
public class AttendanceReport {
    private String subject;
    private String courseID;
    private String dateAndTime;
    private String attendanceStatus;

    public AttendanceReport(){}

    public AttendanceReport(String subject, String courseID, String dateAndTime, String attendanceStatus) {
        this.subject = subject;
        this.courseID = courseID;
        this.dateAndTime = dateAndTime;
        this.attendanceStatus = attendanceStatus;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}