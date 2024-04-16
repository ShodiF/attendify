package com.example.application.data;

import org.springframework.stereotype.Component;

@Component
public class AttendanceReport {
    private String subject;
    private String courseID;
    private String dateAndTime;
    private String attendanceStatus;
    private String studentName;
    private String studentID;
    private String reasonForAbsence;
    private String supportingDocs;

    public AttendanceReport(String s, String s1, String s2, String s3, String sName, String s4, String s5, String s6) {
        this.subject = s;
        this.dateAndTime = s2;
        this.attendanceStatus = s3;
        this.studentName = sName;
        this.studentID = s4;
        this.reasonForAbsence = s5;
        this.supportingDocs = s6;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getReasonForAbsence() {
        return reasonForAbsence;
    }

    public void setReasonForAbsence(String reasonForAbsence) {
        this.reasonForAbsence = reasonForAbsence;
    }

    public String getSupportingDocs() {
        return supportingDocs;
    }

    public void setSupportingDocs(String supportingDocs) {
        this.supportingDocs = supportingDocs;
    }

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