package com.example.rushikesh.qpgadminaccount;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UploadCsv extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    Spinner spinnerCourse, spinnerSubject, spinnerChapter, spinnerLevel, spinnerDeleteQuestion;
    FirebaseAuth mAuth;
    DatabaseReference databaseReferenceGetCourse, databaseReferenceGetSubject, databaseReferenceGetChapter, databaseReferenceGetLevel, databaseReferenceAddQuestion;
    ProgressBar progressBar;
    List<Course> courseList;
    List<Subject> subjectList;
    List<Chapter> chapterList;
    List<Level> levelList;
    List<Question> questionList;
    Question question;
    Course course;
    Subject subject;
    Level level;
    Chapter chapter;
    String courseId;
    String subjectId;
    String chapterId, questionId;
    String questionLevel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_csv);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);

        }

        findViewById(R.id.importCsv).setOnClickListener(this);
        spinnerCourse = findViewById(R.id.spinnerCourse2);
        spinnerSubject = findViewById(R.id.spinnerSubject1);
        spinnerChapter = findViewById(R.id.spinnerChapter);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);


        listView = findViewById(R.id.lv);
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);


        mAuth =FirebaseAuth.getInstance();

        courseList = new ArrayList<>();
        subjectList = new ArrayList<>();
        chapterList = new ArrayList<>();
        levelList = new ArrayList<>();
        questionList = new ArrayList<>();
        databaseReferenceGetCourse = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("course");

        progressDialog.show();


        databaseReferenceGetCourse.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {


                courseList.clear();
                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                    Course course = courseSnapshot.getValue(Course.class);
                    courseList.add(course);

                }

                CourseList adapter = new CourseList(UploadCsv.this,R.layout.list_layout,courseList);
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
                fillChapter();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerChapter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void fillSubject(){
        databaseReferenceGetSubject = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("subject").child(courseId);

        databaseReferenceGetSubject.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                subjectList.clear();
                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                    Subject subject = courseSnapshot.getValue(Subject.class);
                    subjectList.add(subject);

                }

                SubjectList adapterSubject = new SubjectList(UploadCsv.this,R.layout.list_layout,subjectList);
                spinnerSubject.setAdapter(adapterSubject);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fillChapter() {
        databaseReferenceGetChapter = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("chapter").child(courseId).child(subjectId);

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

                ChapterList adapterChapter = new ChapterList(UploadCsv.this,R.layout.list_layout,chapterList);
                spinnerChapter.setAdapter(adapterChapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.importCsv:
                new MaterialFilePicker()
                        .withActivity(UploadCsv.this)
                        .withRequestCode(1000)
                        .withFilter(Pattern.compile(".*\\.csv")) // Filtering files and directories by file name using regexp
                        .withHiddenFiles(false) // Show hidden files and folders
                        .start();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            final String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are sure you want to import the file?\n\n");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    readQuestionData(filePath);

                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            builder.show();
        }
    }

    private void readQuestionData(String filePath) {

        File file = new File(filePath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            progressDialog.show();

            while ((line = reader.readLine()) != null) {

                String[] token = line.split(",");
                addQuestion(token[0],token[1]);

                    arrayAdapter.add(token[0]+"-"+token[1]);


            }
            progressBar.setVisibility(View.GONE);

        } catch (Exception e) {
        }

    }

    private void addQuestion(String questionText,String questionLevel) {
        if(!TextUtils.isEmpty(questionText)){
            progressDialog.cancel();
            databaseReferenceAddQuestion = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

            String id = databaseReferenceAddQuestion.push().getKey();

            Question question = new Question(id,questionText,questionLevel);

            databaseReferenceAddQuestion.child("question").child(course.courseId).child(subject.subjectId).child(chapter.chapterId).child(id ).setValue(question);


        }else {progressBar.setVisibility(View.GONE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted!", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }
}
