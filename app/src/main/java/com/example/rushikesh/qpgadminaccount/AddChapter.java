package com.example.rushikesh.qpgadminaccount;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rushikesh.qpgadminaccount.Model.Chapter;
import com.example.rushikesh.qpgadminaccount.Model.ChapterList;
import com.example.rushikesh.qpgadminaccount.Model.Course;
import com.example.rushikesh.qpgadminaccount.Model.CourseList;
import com.example.rushikesh.qpgadminaccount.Model.Subject;
import com.example.rushikesh.qpgadminaccount.Model.SubjectList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddChapter extends AppCompatActivity implements View.OnClickListener {

    Spinner spinnerCourse,spinnerSubject,spinnerDeleteChapter;
    EditText editTextChapter;
    FirebaseAuth mAuth;
    DatabaseReference databaseReferenceGetCourse,databaseReferenceGetSubject,databaseReferenceAddChapter;
    List<Course> courseList;
    List<Subject> subjectList;
    List<Chapter> chapterList;
    Course course;
    Subject subject;
    Chapter chapter;
    String courseId;
    String subjectId,chapterId;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chapter);

        findViewById(R.id.addChapter1).setOnClickListener(this);
        findViewById(R.id.buttonDeleteChapter).setOnClickListener(this);

        spinnerCourse = findViewById(R.id.spinnerCourse1);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        editTextChapter = findViewById(R.id.editTextChapter);
        spinnerDeleteChapter = findViewById(R.id.spinnerDeleteChapter);


        mAuth =FirebaseAuth.getInstance();

        courseList = new ArrayList<>();
        subjectList = new ArrayList<>();
        chapterList = new ArrayList<>();
        databaseReferenceGetCourse = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("course");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        databaseReferenceGetCourse.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                courseList.clear();
                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                    Course course = courseSnapshot.getValue(Course.class);
                    courseList.add(course);

                }

                CourseList adapter = new CourseList(AddChapter.this,R.layout.list_layout,courseList);
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
                subjectId = subject.getSubjectId();
                fillChapter(subjectId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDeleteChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chapter = chapterList.get(position);
                chapterId = chapter.getChapterId();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillChapter(String subjectId) {
        {
            DatabaseReference databaseReferenceGetChapter = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("chapter").child(courseId).child(subjectId);

            final Query query = databaseReferenceGetChapter;
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressDialog.cancel();

                    chapterList.clear();
                    for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                        Chapter chapter = courseSnapshot.getValue(Chapter.class);
                        chapterList.add(chapter);

                    }

                    ChapterList adapterChapter = new ChapterList(AddChapter.this,R.layout.list_layout,chapterList);
                    spinnerDeleteChapter.setAdapter(adapterChapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.addChapter1:
                addChapter();
                break;

            case R.id.buttonDeleteChapter:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Deleting Chapter will delete all other related information...");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteChapter(chapterId);
                    }
                });
                builder.show();

        }

    }

    private void deleteChapter(String chapterId) {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("chapter").child(courseId).child(subjectId).child(chapterId);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("question").child(courseId).child(subjectId).child(chapterId);

        databaseReference2.removeValue();
        databaseReference3.removeValue();

    }

    public void fillSubject(){
        databaseReferenceGetSubject = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("subject").child(courseId);

        databaseReferenceGetSubject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.cancel();


                subjectList.clear();
                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                    Subject subject = courseSnapshot.getValue(Subject.class);
                    subjectList.add(subject);

                }

                SubjectList adapterSubject = new SubjectList(AddChapter.this,R.layout.list_layout,subjectList);
                spinnerSubject.setAdapter(adapterSubject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void addChapter() {

        String chapterName = editTextChapter.getText().toString().trim();

        progressDialog.show();
        if(!TextUtils.isEmpty(chapterName)){
            progressDialog.cancel();
            databaseReferenceAddChapter = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

            String id = databaseReferenceAddChapter.push().getKey();

            Chapter chapter = new Chapter(id,chapterName);



            databaseReferenceAddChapter.child("chapter").child(course.courseId).child(subject.subjectId).child(id ).setValue(chapter);

            editTextChapter.setText("");

            Toast.makeText(this,"Chapter Added Successfully",Toast.LENGTH_LONG).show();

        }else {
            progressDialog.cancel();
            editTextChapter.setError("Enter Chapter");
            editTextChapter.requestFocus();
            return;
        }
    }
}
