package com.example.rushikesh.qpgadminaccount;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rushikesh.qpgadminaccount.Model.Chapter;
import com.example.rushikesh.qpgadminaccount.Model.ChapterList;
import com.example.rushikesh.qpgadminaccount.Model.Course;
import com.example.rushikesh.qpgadminaccount.Model.CourseList;
import com.example.rushikesh.qpgadminaccount.Model.Level;
import com.example.rushikesh.qpgadminaccount.Model.LevelList;
import com.example.rushikesh.qpgadminaccount.Model.Question;
import com.example.rushikesh.qpgadminaccount.Model.QuestionList;
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

public class UpdateQuestion extends AppCompatActivity implements View.OnClickListener{

    Spinner spinnerCourse,spinnerSubject,spinnerChapter,spinnerLevel,spinnerDeleteQuestion;
    EditText editTextQuestion;
    FirebaseAuth mAuth;
    DatabaseReference databaseReferenceGetCourse,databaseReferenceGetSubject,databaseReferenceGetChapter,databaseReferenceGetLevel,databaseReferenceAddQuestion;
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
    String chapterId,questionId;
    String questionLevel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_question);

        findViewById(R.id.buttonUpdateQuestion).setOnClickListener(this);

        spinnerCourse = findViewById(R.id.spinnerCourse2);
        spinnerSubject = findViewById(R.id.spinnerSubject1);
        spinnerChapter = findViewById(R.id.spinnerChapter);
        spinnerLevel = findViewById(R.id.spinnerLevel);
        spinnerDeleteQuestion = findViewById(R.id.spinnerDeleteQuestion);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);



        editTextQuestion = findViewById(R.id.editTextQuestion);

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

                CourseList adapter = new CourseList(UpdateQuestion.this,R.layout.list_layout,courseList);
                spinnerCourse.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReferenceGetLevel = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("level");




        databaseReferenceGetLevel.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                levelList.clear();
                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                    Level level = courseSnapshot.getValue(Level.class);
                    levelList.add(level);

                }

                LevelList adapter = new LevelList(UpdateQuestion.this,R.layout.list_layout,levelList);
                spinnerLevel.setAdapter(adapter);

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
                fillQuestion(chapterId);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level = levelList.get(position);
                questionLevel = level.getLevelName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerDeleteQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question = questionList.get(position);
                questionId = question.getQuestionId();

                editTextQuestion.setText("");
                editTextQuestion.setText(question.getQuestionText());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void fillQuestion(String chapterId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("question").child(courseId).child(subjectId).child(chapterId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                progressDialog.cancel();


                questionList.clear();
                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                    Question question = courseSnapshot.getValue(Question.class);
                    questionList.add(question);

                }

                QuestionList question = new QuestionList(UpdateQuestion.this,R.layout.list_layout,questionList);
                spinnerDeleteQuestion.setAdapter(question);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonUpdateQuestion:

                updateQuestion(questionId);

        }

    }

    private void updateQuestion(String questionId) {

        String questionText = editTextQuestion.getText().toString().trim();
        Question question = new Question(questionId,questionText,questionLevel);

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());
        databaseReference2.child("question").child(courseId).child(subjectId).child(chapterId).child(questionId).setValue(question);

        editTextQuestion.setText("");

        Toast.makeText(this,"Question Updated Successfully",Toast.LENGTH_LONG).show();


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

                SubjectList adapterSubject = new SubjectList(UpdateQuestion.this,R.layout.list_layout,subjectList);
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

                ChapterList adapterChapter = new ChapterList(UpdateQuestion.this,R.layout.list_layout,chapterList);
                spinnerChapter.setAdapter(adapterChapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
