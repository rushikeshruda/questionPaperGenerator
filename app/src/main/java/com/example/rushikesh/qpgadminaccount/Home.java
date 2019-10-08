package com.example.rushikesh.qpgadminaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.addCourse).setOnClickListener(this);
        findViewById(R.id.addSubject).setOnClickListener(this);
        findViewById(R.id.addChapter).setOnClickListener(this);
        findViewById(R.id.addQuestion).setOnClickListener(this);
        findViewById(R.id.addLevel).setOnClickListener(this);
        findViewById(R.id.addPaperPattern).setOnClickListener(this);
        findViewById(R.id.updateQuestion).setOnClickListener(this);
        findViewById(R.id.uploadCsv).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addCourse:
                startActivity(new Intent(this,AddCourse.class));
                break;

            case R.id.addSubject:
                startActivity(new Intent(this,AddSubject.class));
                break;

            case R.id.addChapter:
                startActivity(new Intent(this,AddChapter.class));
                break;

            case R.id.addLevel:
                startActivity(new Intent(this,AddLevel.class));
                break;

            case R.id.addQuestion:
                startActivity(new Intent(this,AddQuestion.class));
                break;

            case R.id.addPaperPattern:
                startActivity(new Intent(this,AddQuestionPaperPattern.class));
                break;

            case R.id.updateQuestion:
                startActivity(new Intent(this,UpdateQuestion.class));
                break;

            case R.id.uploadCsv:
                startActivity(new Intent(this,UploadCsv.class));
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
