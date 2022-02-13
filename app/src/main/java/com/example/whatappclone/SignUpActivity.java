package com.example.whatappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatappclone.databinding.ActivitySignUpBinding;
import com.example.whatappclone.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {


    ActivitySignUpBinding signUpBinding;
    private FirebaseAuth myAuth;
    FirebaseDatabase database;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());
        getSupportActionBar().hide();

        myAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        controlsHandle();
        eventsHandle();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currenUser = myAuth.getCurrentUser();
        if (currenUser!=null) {
            Intent accessApp = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(accessApp);
        }
    }


    private void eventsHandle() {
        signUpBinding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = signUpBinding.txtSignupUsername.getText().toString();
                String password = signUpBinding.txtSignupPassword.getText().toString();
                String email = signUpBinding.txtSignupEmail.getText().toString();
                if (username.isEmpty() == false &&
                        password.isEmpty() == false &&
                        email.isEmpty() == false){
                    dialog.show();
                    myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                String uid = task.getResult().getUser().getUid();
                                User user = new User(username,uid,password, email);
                                database.getReference().child("User").child(uid).setValue(user);

                                Toast.makeText(SignUpActivity.this, "Create account successfully", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignUpActivity.this, "Missing info", Toast.LENGTH_LONG).show();
                }
            }
        });

        signUpBinding.txtAlreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signIn = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(signIn);
            }
        });

    }

    private void controlsHandle() {
        dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setTitle("Pending");
        dialog.setCanceledOnTouchOutside(false);

    }
}