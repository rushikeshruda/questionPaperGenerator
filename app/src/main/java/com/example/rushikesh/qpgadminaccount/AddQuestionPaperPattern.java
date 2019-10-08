package com.example.rushikesh.qpgadminaccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddQuestionPaperPattern extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerCourse, spinnerSubject;
    EditText editTextPatternName;
    FirebaseAuth mAuth;
    DatabaseReference databaseReferenceGetCourse, databaseReferenceGetSubject, databaseReferenceAddPatternName;
    ProgressDialog progressDialog;
    List<Course> courseList;
    List<Subject> subjectList;
    Course course;
    Subject subject;
    String courseId,subjectName,subjectId;
    public final static String subjectId1 = "Subject Id";
    public final static String courseId1 = "Course Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question_paper_pattern);

        findViewById(R.id.buttonSave).setOnClickListener(this);
        findViewById(R.id.buttonNext).setOnClickListener(this);

        spinnerCourse = findViewById(R.id.spinnerCourse3);
        spinnerSubject = findViewById(R.id.spinnerSubject2);
        editTextPatternName = findViewById(R.id.editTextPattern);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);


        mAuth = FirebaseAuth.getInstance();

        courseList = new ArrayList<>();
        subjectList = new ArrayList<>();
        databaseReferenceGetCourse = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("course");

        progressDialog.show();

        databaseReferenceGetCourse.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                courseList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    courseList.add(course);

                }

                CourseList adapter = new CourseList(AddQuestionPaperPattern.this, R.layout.list_layout, courseList);
                spinnerCourse.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course = courseList.get(position);
                courseId = course.getCourseId();

                fillSubject();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = subjectList.get(position);
                subjectId = subject.subjectId;
                subjectName = subject.subjectName;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonSave:
                addPatternName();
                break;

            case R.id.buttonNext:
                Intent intent = new Intent(getApplicationContext(),SetPattern.class);
                intent.putExtra(subjectId1,subjectId);
                intent.putExtra(courseId1,courseId);
                startActivity(intent);
                break;
        }

    }

    private void addPatternName() {
        String patternNameText = editTextPatternName.getText().toString().trim();
        progressDialog.show();
        if(!TextUtils.isEmpty(patternNameText)){
            progressDialog.cancel();

            String updatedName = subjectName+"-"+patternNameText;
            databaseReferenceAddPatternName = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

            String id = databaseReferenceAddPatternName.push().getKey();

            PatternName patternName = new PatternName(id,updatedName);



            databaseReferenceAddPatternName.child("pattern name").child(course.courseId).child(subject.subjectId).child(id ).setValue(patternName);

            editTextPatternName.setText("");

            Toast.makeText(this,"Pattern Name Added Successfully",Toast.LENGTH_LONG).show();

        }else {
            progressDialog.cancel();
            editTextPatternName.setError("Enter Chapter");
            editTextPatternName.requestFocus();
            return;
        }
    }

    public void fillSubject() {
        databaseReferenceGetSubject = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("subject").child(courseId);

        databaseReferenceGetSubject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();

                subjectList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Subject subject = courseSnapshot.getValue(Subject.class);
                    subjectList.add(subject);

                }

                SubjectList adapterSubject = new SubjectList(AddQuestionPaperPattern.this, R.layout.list_layout, subjectList);
                spinnerSubject.setAdapter(adapterSubject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}