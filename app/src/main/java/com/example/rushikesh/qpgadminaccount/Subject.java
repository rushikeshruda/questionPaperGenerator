package com.example.rushikesh.qpgadminaccount;

/**
 * Created by Rushikesh on 05/04/2018.
 */

public class Subject {
    public String subjectId;
    public String subjectName;

    public Subject(){

    }

    public Subject(String subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
