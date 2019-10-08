package com.example.rushikesh.qpgadminaccount;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddCourse extends AppCompatActivity implements View.OnClickListener{

    EditText editTextCourse;
    FirebaseAuth mAuth;
    DatabaseReference databaseCourse;
    ProgressDialog progressDialog;
    List<Course> courseList;
    Spinner spinnerCourse;
    String courseId;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mAuth=FirebaseAuth.getInstance();
        courseList = new ArrayList<>();
        editTextCourse = findViewById(R.id.editTextCourse);
        spinnerCourse = findViewById(R.id.spinnerDeleteCourse);
        findViewById(R.id.addCourse1).setOnClickListener(this);
        findViewById(R.id.buttonDeleteCourse).setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        fillCourse();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void fillCourse() {
        DatabaseReference databaseReferenceGetCourse = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("course");

        progressDialog.show();

        databaseReferenceGetCourse.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();

                courseList.clear();
                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                    Course course = courseSnapshot.getValue(Course.class);
                    courseList.add(course);

                }

                CourseList adapter = new CourseList(AddCourse.this,R.layout.list_layout,courseList);
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addCourse1:

                addCourse();
                break;

            case R.id.buttonDeleteCourse:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Deleting Course will delete all other related information...");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCourse(courseId);
                    }
                });
                builder.show();
                break;
        }

    }

    private void deleteCourse(String courseId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("course").child(courseId);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("subject").child(courseId);
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("chapter").child(courseId);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("question").child(courseId);
        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("pattern name").child(courseId);

        databaseReference.removeValue();
        databaseReference1.removeValue();
        databaseReference2.removeValue();
        databaseReference3.removeValue();
        databaseReference4.removeValue();

    }

    private void addCourse() {

        String courseName = editTextCourse.getText().toString().trim();
        progressDialog.show();
        if(!TextUtils.isEmpty(courseName)){
            progressDialog.cancel();
            databaseCourse = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

            String id = databaseCourse.push().getKey();

            Course course = new Course(id,courseName);



            databaseCourse.child("course").child(id).setValue(course);

            editTextCourse.setText("");

            Toast.makeText(this,"Course Added Successfully",Toast.LENGTH_LONG).show();

        }else {
            progressDialog.cancel();
            editTextCourse.setError("Enter Course");
            editTextCourse.requestFocus();
            return;
        }
    }
}
