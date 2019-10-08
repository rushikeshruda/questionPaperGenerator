    package com.example.rushikesh.qpgadminaccount;

    import android.app.AlertDialog;
    import android.app.ProgressDialog;
    import android.content.DialogInterface;
    import android.provider.ContactsContract;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.View;

    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.EditText;
    import android.widget.ProgressBar;
    import android.widget.Spinner;
    import android.widget.Toast;

    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.ChildEventListener;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import org.w3c.dom.Comment;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;

    public class AddSubject extends AppCompatActivity implements View.OnClickListener{

        Spinner spinnerCourse,spinnerDeleteSubject;
        EditText editTextSubject;
        FirebaseAuth mAuth;
        DatabaseReference databaseReferenceGetCourse,databaseReferenceAddSubject;
        ProgressDialog progressDialog;
        List<Course> courseList;
        List<Subject> subjectList;
        Course course;
        Subject subject;
        String courseId,subjectId;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_subject);

            findViewById(R.id.buttonSubject).setOnClickListener(this);
            findViewById(R.id.buttonDeleteSubject).setOnClickListener(this);
            spinnerCourse = findViewById(R.id.spinnerCourse);
            spinnerDeleteSubject = findViewById(R.id.spinnerDeleteSubject);
            editTextSubject = findViewById(R.id.editTextSubject);
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCanceledOnTouchOutside(false);


            mAuth =FirebaseAuth.getInstance();

            courseList = new ArrayList<>();
            subjectList = new ArrayList<>();

            databaseReferenceGetCourse = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("course");

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

                    CourseList adapter = new CourseList(AddSubject.this,R.layout.list_layout,courseList);
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
                    fillSubject(courseId);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spinnerDeleteSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    subject = subjectList.get(position);

                    subjectId = subject.getSubjectId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        private void fillSubject(String courseId) {

            DatabaseReference databaseReferenceGetSubject = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("subject").child(courseId);

            databaseReferenceGetSubject.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    progressDialog.cancel();


                    subjectList.clear();
                    for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                        Subject subject = courseSnapshot.getValue(Subject.class);
                        subjectList.add(subject);

                    }

                    SubjectList adapterSubject = new SubjectList(AddSubject.this,R.layout.list_layout,subjectList);
                    spinnerDeleteSubject.setAdapter(adapterSubject);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.buttonSubject:
                        addSubject();
                    break;

                case R.id.buttonDeleteSubject:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Deleting Subject will delete all other related information...");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteSubject(subjectId);
                        }
                    });
                    builder.show();

                        break;
            }

        }

        private void deleteSubject(String subjectId) {
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("subject").child(courseId).child(subjectId);
            DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("chapter").child(courseId).child(subjectId);
            DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("question").child(courseId).child(subjectId);
            DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("pattern name").child(courseId).child(subjectId);

            databaseReference1.removeValue();
            databaseReference2.removeValue();
            databaseReference3.removeValue();
            databaseReference4.removeValue();

        }


        private void addSubject() {


                String subjectName = editTextSubject.getText().toString().trim();
                progressDialog.show();
                if(!TextUtils.isEmpty(subjectName)){
                    progressDialog.cancel();
                    databaseReferenceAddSubject = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

                    String id = databaseReferenceAddSubject.push().getKey();

                    Subject subject = new Subject(id,subjectName);


                    databaseReferenceAddSubject.child("subject").child(course.courseId).child(id ).setValue(subject);

                    editTextSubject.setText("");

                    Toast.makeText(this,"Subject Added Successfully",Toast.LENGTH_LONG).show();

                }else {
                    progressDialog.cancel();
                    editTextSubject.setError("Enter Subject");
                    editTextSubject.requestFocus();
                    return;
                }
        }

    }
