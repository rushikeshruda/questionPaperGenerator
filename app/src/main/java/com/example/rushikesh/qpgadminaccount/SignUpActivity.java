package com.example.rushikesh.qpgadminaccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail,editTextPassword;
    private FirebaseAuth mAuth;
    DatabaseReference databaseReferenceAdminUser;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.textViewLogin).setOnClickListener(this);
        findViewById(R.id.buttonSignUp).setOnClickListener(this);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);


        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                finish();
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }
    }

    private void registerUser() {

        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            editTextEmail.setError("Enter vaild email");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length()<6){

            editTextPassword.setError("Minimum length should be 6");
            editTextPassword.requestFocus();
            return;
        }
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.cancel();
                if (task.isSuccessful()){
                    finish();
                    databaseReferenceAdminUser = FirebaseDatabase.getInstance().getReference().child("admin users");
                    String id = databaseReferenceAdminUser.push().getKey();
                    String uid = mAuth.getCurrentUser().getUid().toString();
                    String userName = email;

                    AdminUsers adminUsers = new AdminUsers(id,userName,uid);

                    databaseReferenceAdminUser.child(id).setValue(adminUsers);

                    startActivity(new Intent(SignUpActivity.this,Home.class));
                    Toast.makeText(SignUpActivity.this,"Admin register successfully",Toast.LENGTH_LONG).show();
                }else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(SignUpActivity.this,"You are already register",Toast.LENGTH_LONG).show();

                    }else {
                        Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                    }

                }
            }
        });


    }
}
