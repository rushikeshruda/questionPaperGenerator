package com.example.rushikesh.qpgadminaccount;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rushikesh.qpgadminaccount.Model.Chapter;
import com.example.rushikesh.qpgadminaccount.Model.ChapterList;
import com.example.rushikesh.qpgadminaccount.Model.Course;
import com.example.rushikesh.qpgadminaccount.Model.Level;
import com.example.rushikesh.qpgadminaccount.Model.LevelList;
import com.example.rushikesh.qpgadminaccount.Model.Pattern;
import com.example.rushikesh.qpgadminaccount.Model.PatternName;
import com.example.rushikesh.qpgadminaccount.Model.PatternNameList;
import com.example.rushikesh.qpgadminaccount.Model.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetPattern extends AppCompatActivity implements View.OnClickListener {


    Spinner spinnerPatternName, spinnerChapter, spinnerLevel;
    FirebaseAuth mAuth;
    DatabaseReference databaseReferenceGetPatternName, databaseReferenceGetChapter, databaseReferenceGetLevel, databaseReferenceAddPattern, databaseReferencegetPattern;
    ProgressDialog progressDialog;
    List<PatternName> patternNameList;
    List<Chapter> chapterList;
    List<Level> levelList;
    PatternName patternName;
    Chapter chapter;
    Level level;
    String  chapterId, chapterTitle;
    String questionLevel;
    String patternNameId, subjectId, patternNameText, courseId;
    EditText editTextTotalMarks, editTextSubQuestionMarks, getEditTextSubQuestionNumber;


    private View subQuestionsView;

    ListView listViewQuestions;
    ArrayAdapter<String> adapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pattern);

        Bundle bundle = getIntent().getExtras();
        subjectId = bundle.getString(AddQuestionPaperPattern.subjectId1);
        courseId = bundle.getString(AddQuestionPaperPattern.courseId1);


        findViewById(R.id.addMainQuestion).setOnClickListener(this);
        findViewById(R.id.addSubQuestion).setOnClickListener(this);
        findViewById(R.id.save1).setOnClickListener(this);
        spinnerPatternName = findViewById(R.id.spinnerPatternName);

        mAuth = FirebaseAuth.getInstance();
        patternNameList = new ArrayList<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

        progressDialog.show();


        LayoutInflater inflater = getLayoutInflater();
        subQuestionsView = inflater.inflate(R.layout.fragment_sub_question, null, false);

        editTextSubQuestionMarks = subQuestionsView.findViewById(R.id.edit_text_sub_question_marks);
        getEditTextSubQuestionNumber = subQuestionsView.findViewById(R.id.subQuestionNumber);

        spinnerChapter = subQuestionsView.findViewById(R.id.spinner_chapter_titles);
        chapterList = new ArrayList<>();

        spinnerLevel = subQuestionsView.findViewById(R.id.spinner_question_levels);
        levelList = new ArrayList<>();

        listViewQuestions = findViewById(R.id.listViewQuestion);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        listViewQuestions.setAdapter(adapter);


        fillPatternName();

        spinnerPatternName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                patternName = patternNameList.get(position);
                patternNameText = patternName.getPatternNameText();
                patternNameId = patternName.getPatternNameId();

                /*String[] patternNameText1 = patternNameText.split("-");
                patternNameText2 = patternNameText1[0];
                */
                adapter.clear();
                fillpattern(patternNameId);

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
                chapterTitle = chapter.getChapterName();

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


        listViewQuestions.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    String questionText = adapter.getItem(position);

                    String[] parts = questionText.split("\\-");

                    if (parts.length > 2) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SetPattern.this);
                        builder.setTitle("Select option");

                        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateSubQuestion(position);
                            }
                        });

                        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                list.remove(position);
                                adapter.notifyDataSetChanged();

                                listViewQuestions.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listViewQuestions.smoothScrollToPosition(position);
                                    }
                                });

                            }
                        });

                        builder.show();


                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SetPattern.this);

                        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateMainQuestion(position);
                            }
                        });

                        builder.setNegativeButton("Add sub question", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateSubQuestion1(position);
                            }
                        });

                        builder.setTitle("Select option");
                        builder.show();


                    }


                return false;
            }

        });

    }

    private void fillpattern(String patternNameId) {
        databaseReferencegetPattern = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("question paper pattern").child(patternNameId);

        databaseReferencegetPattern.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pattern pattern = dataSnapshot.getValue(Pattern.class);

                if (pattern != null) {
                    String text = pattern.getPatternText();

                    adapter.clear();
                    String[] tokens = text.split("\\|");

                    list = new ArrayList<>(Arrays.asList(tokens));

                    adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
                    listViewQuestions.setAdapter(adapter);
                } else
                    Toast.makeText(getApplicationContext(), "Empty Pattern", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void fillPatternName() {
        databaseReferenceGetPatternName = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser()
                .getUid()).child("pattern name").child(courseId).child(subjectId);

        final Query query = databaseReferenceGetPatternName;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();

                patternNameList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    PatternName patternName = courseSnapshot.getValue(PatternName.class);

                    patternNameList.add(patternName);

                }
                PatternNameList adapterPatternName = new PatternNameList(SetPattern.this, R.layout.list_layout, patternNameList);
                spinnerPatternName.setAdapter(adapterPatternName);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addMainQuestion:
                addMainQuestion();

                break;

            case R.id.addSubQuestion:
                addSubQuestion();

                break;

            case R.id.save1:
                savePattern();
                break;
        }

    }

    private void savePattern() {

        String allQuestions = "";

        for (int i = 0; i < adapter.getCount(); i++) {
            if (i == 0) {
                allQuestions += adapter.getItem(i);
            } else {
                allQuestions += "|" + adapter.getItem(i);
            }
        }
        databaseReferenceAddPattern = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser()
                .getUid()).child("question paper pattern");

        progressDialog.show();
        if (!TextUtils.isEmpty(allQuestions)) {
            progressDialog.cancel();

            String id = databaseReferenceAddPattern.push().getKey();

            Pattern pattern = new Pattern(id, allQuestions);


            databaseReferenceAddPattern.child(patternNameId).setValue(pattern);

            Toast.makeText(this, "Pattern Added Successfully", Toast.LENGTH_LONG).show();

            adapter.clear();

        } else {
            progressDialog.cancel();
            Toast.makeText(getApplicationContext(), "Please Set Pattern", Toast.LENGTH_LONG).show();
            return;
        }


    }

    public void fillLevel() {
        databaseReferenceGetLevel = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser()
                .getUid()).child("level");

        progressDialog.show();


        databaseReferenceGetLevel.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();

                levelList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Level level = courseSnapshot.getValue(Level.class);
                    levelList.add(level);

                }

                LevelList adapter = new LevelList(SetPattern.this, R.layout.list_layout, levelList);
                spinnerLevel.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void fillChapter() {
        databaseReferenceGetChapter = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser()
                .getUid()).child("chapter").child(courseId).child(subjectId);

        final Query query = databaseReferenceGetChapter;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                chapterList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Chapter chapter = courseSnapshot.getValue(Chapter.class);
                    chapterList.add(chapter);

                }

                ChapterList adapterChapter = new ChapterList(SetPattern.this, R.layout.list_layout, chapterList);
                spinnerChapter.setAdapter(adapterChapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void addSubQuestion() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        fillChapter();
        fillLevel();
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String number = getEditTextSubQuestionNumber.getText().toString();

                String marks = editTextSubQuestionMarks.getText().toString();

                if (number.isEmpty() || marks.isEmpty()){
                    Toast.makeText(SetPattern.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else {
                    adapter.add("      " + number + "-" + chapterTitle + "-" + marks + "-" + questionLevel);
                    getEditTextSubQuestionNumber.setText("");
                }
            }
        });

        if (subQuestionsView.getParent() != null) {
            ((ViewGroup) subQuestionsView.getParent()).removeView(subQuestionsView);
        }

        builder.setView(subQuestionsView);
        builder.show();


    }

    private void updateSubQuestion1(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        fillChapter();
        fillLevel();
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String number = getEditTextSubQuestionNumber.getText().toString();
                final int pos = Integer.parseInt(getEditTextSubQuestionNumber.getText().toString());

                String marks = editTextSubQuestionMarks.getText().toString();
                list.add(position + pos, "      " + number + "-" + chapterTitle + "-" + marks + "-" + questionLevel);
                adapter.notifyDataSetChanged();
                getEditTextSubQuestionNumber.setText("");

                listViewQuestions.post(new Runnable() {
                    @Override
                    public void run() {
                        listViewQuestions.smoothScrollToPosition(position + pos);
                    }
                });

            }
        });

        if (subQuestionsView.getParent() != null) {
            ((ViewGroup) subQuestionsView.getParent()).removeView(subQuestionsView);
        }

        builder.setView(subQuestionsView);
        builder.show();
    }

    private void updateSubQuestion(final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        fillChapter();
        fillLevel();
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String number = getEditTextSubQuestionNumber.getText().toString();

                String marks = editTextSubQuestionMarks.getText().toString();
                list.set(position, "      " + number + "-" + chapterTitle + "-" + marks + "-" + questionLevel);
                adapter.notifyDataSetChanged();
                getEditTextSubQuestionNumber.setText("");

            }
        });

        if (subQuestionsView.getParent() != null) {
            ((ViewGroup) subQuestionsView.getParent()).removeView(subQuestionsView);
        }

        builder.setView(subQuestionsView);
        builder.show();


        String que = list.get(position);
        String[] que1 = que.split("\\-");
        getEditTextSubQuestionNumber.setText(que1[0].trim());
        editTextSubQuestionMarks.setText(que1[2]);
    }

    private void addMainQuestion() {

        LayoutInflater inflater = getLayoutInflater();
        View fragmentView = inflater.inflate(R.layout.fragment_main_question, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editTextMainQuestion = fragmentView.findViewById(R.id.edit_text_main_question);

        editTextMainQuestion.setText("Q");
        Selection.setSelection(editTextMainQuestion.getText(), editTextMainQuestion.getText().length());


        editTextMainQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().startsWith("Q")) {
                    editTextMainQuestion.setText("Q");
                    Selection.setSelection(editTextMainQuestion.getText(), editTextMainQuestion.getText().length());
                }

            }
        });
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String questionText = editTextMainQuestion.getText().toString().trim();
                String marks = editTextTotalMarks.getText().toString().trim();

                if (questionText.isEmpty()||marks.isEmpty())
                {
                    Toast.makeText(SetPattern.this, "All fields are required", Toast.LENGTH_SHORT).show();

                }else {
                    int len = marks.length();
                    if (len <= 1) {
                        marks = "0" + marks;
                    }
                    adapter.add(questionText + "-" + marks);
                }
            }
        });
        builder.setView(fragmentView);
        builder.show();

        editTextTotalMarks = fragmentView.findViewById(R.id.edit_text_main_question_marks);


    }

    private void updateMainQuestion(final int position) {

        LayoutInflater inflater = getLayoutInflater();
        View fragmentView = inflater.inflate(R.layout.fragment_main_question, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editTextMainQuestion = fragmentView.findViewById(R.id.edit_text_main_question);

        editTextMainQuestion.setText("Q");
        Selection.setSelection(editTextMainQuestion.getText(), editTextMainQuestion.getText().length());


        editTextMainQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().startsWith("Q")) {
                    editTextMainQuestion.setText("Q");
                    Selection.setSelection(editTextMainQuestion.getText(), editTextMainQuestion.getText().length());
                }

            }
        });
        builder.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String questionText = editTextMainQuestion.getText().toString().trim();
                String marks = editTextTotalMarks.getText().toString().trim();
                int len = marks.length();
                if (len <= 1) {
                    marks = "0" + marks;
                }

                list.set(position, questionText + "-" + marks);
                adapter.notifyDataSetChanged();
            }
        });
        builder.setView(fragmentView);
        builder.show();

        editTextTotalMarks = fragmentView.findViewById(R.id.edit_text_main_question_marks);

        String que = list.get(position);
        String[] que1 = que.split("\\-");
        editTextMainQuestion.setText(que1[0].trim());
        editTextTotalMarks.setText(que1[1]);


    }


}
