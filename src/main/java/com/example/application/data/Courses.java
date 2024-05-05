package com.example.application.data;

public class Courses {
    String courseName;
    String courseId;
    String dayOfStudy;
    String startTime;
    String endTime;
    double absentRate;

    public double getAbsentRate() {
        return absentRate;
    }

    public void setAbsentRate(double absentRate) {
        this.absentRate = absentRate;
    }

    public Courses(String courseName, String courseId, String dayOfStudy, String startTime, String endTime) {
        this.courseName = courseName;
        this.courseId = courseId;
        this.dayOfStudy = dayOfStudy;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Courses(String c, String x, double absentRate){
        this.courseName = c;
        this.courseId = x;
        this.absentRate = absentRate;
    }


    public Courses() {
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getDayOfStudy() {
        return dayOfStudy;
    }

    public void setDayOfStudy(String dayOfStudy) {
        this.dayOfStudy = dayOfStudy;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Courses{" +
                "courseName='" + courseName + '\'' +
                ", courseId='" + courseId + '\'' +
                ", dayOfStudy='" + dayOfStudy + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}