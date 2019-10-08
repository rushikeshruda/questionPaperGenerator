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

public class AddLevel extends AppCompatActivity implements View.OnClickListener{

    EditText editTextLevel;
    FirebaseAuth mAuth;
    DatabaseReference databaseLevel;
    Spinner spinnerDeleteLevel;
    List<Level> levelList;
    Level level;
    String levelId;


    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_level);

        mAuth=FirebaseAuth.getInstance();
        editTextLevel = findViewById(R.id.editTextLevel);

        spinnerDeleteLevel = findViewById(R.id.spinnerDeleteLevel);
        findViewById(R.id.buttonDeleteLevel).setOnClickListener(this);

        levelList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);

        findViewById(R.id.addLevel1).setOnClickListener(this);

        fillLevel();
        spinnerDeleteLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                level = levelList.get(position);
                levelId = level.getLevelId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillLevel() {
        progressDialog.show();
        DatabaseReference databaseReferenceGetLevel = FirebaseDatabase.getInstance().getReference(mAuth.getCurrentUser().getUid()).child("level");




        databaseReferenceGetLevel.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                progressDialog.cancel();

                levelList.clear();
                for (DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
                    Level level = courseSnapshot.getValue(Level.class);
                    levelList.add(level);

                }

                LevelList adapter = new LevelList(AddLevel.this,R.layout.list_layout,levelList);
                spinnerDeleteLevel.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.addLevel1:
                addLevel();
                break;
            case R.id.buttonDeleteLevel:
                deleteLevel(levelId);

        }

    }

    private void deleteLevel(String levelId) {
        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid()).child("level").child(levelId);
        databaseReference2.removeValue();

    }

    private void addLevel() {

        String levelName = editTextLevel.getText().toString().trim();
        progressDialog.show();
        if(!TextUtils.isEmpty(levelName)){
            progressDialog.cancel();
            databaseLevel = FirebaseDatabase.getInstance().getReference().child(mAuth.getCurrentUser().getUid());

            String id = databaseLevel.push().getKey();

            Level level = new Level(id,levelName);



            databaseLevel.child("level").child(id).setValue(level);

            editTextLevel.setText("");

            Toast.makeText(this,"Level Added Successfully",Toast.LENGTH_LONG).show();

        }else {
            progressDialog.cancel();
            editTextLevel.setError("Enter Level");
            editTextLevel.requestFocus();
            return;
        }
    }
}
