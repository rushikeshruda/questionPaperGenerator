package com.example.rushikesh.qpgadminaccount.Model;

/**
 * Created by Rushikesh on 04/04/2018.
 */

public class Course {

    public String courseName;
    public String courseId;

    public Course(){

    }

    public Course(String courseId, String courseName) {
        this.courseName = courseName;
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseId() {
        return courseId;
    }
}
