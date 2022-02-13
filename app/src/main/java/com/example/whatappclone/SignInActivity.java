package com.example.whatappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatappclone.databinding.ActivitySignInBinding;
import com.example.whatappclone.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 23;
    private static final String TAG = "MY_TAG";
    ActivitySignInBinding signInBinding;
    private FirebaseAuth myAuth;
    FirebaseDatabase database;
    ProgressDialog dialog;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(signInBinding.getRoot());
        getSupportActionBar().hide();
        // Configure Google Sign In
        configureGGSignIn();

        controlsHandle();
        eventsHandle();
        
    }

    private void configureGGSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        myAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = myAuth.getCurrentUser();
                            String uid = user.getUid();
                            String userName = user.getDisplayName();
                            String email = user.getEmail();
                            String proPicLink = user.getPhotoUrl().toString();

                            User u = new User();
                            u.setUserID(uid);
                            u.setEmail(email);
                            u.setUsername(userName);
                            u.setProfilePictureLink(proPicLink);
                            database.getReference().child("User").child(uid).setValue(u);
                            Intent signInWithGG = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(signInWithGG);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void eventsHandle() {
        signInBinding.txtCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUp = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(signUp);
            }
        });

        signInBinding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = signInBinding.txtSignupUsername.getText().toString();
                String password = signInBinding.txtSignupPassword.getText().toString();
                if (!userName.isEmpty() && !password.isEmpty()) {
                    dialog.show();
                    myAuth.signInWithEmailAndPassword(userName, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                Intent accessApp = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(accessApp);
                            }
                            else {
                                Toast.makeText(SignInActivity.this,  task.getException().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignInActivity.this, "Missing info", Toast.LENGTH_LONG).show();
                }

            }
        });

        signInBinding.btnSigInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
    }

    private void controlsHandle() {
        dialog = new ProgressDialog(SignInActivity.this);
        dialog.setTitle("Logging...");
        myAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

    }
}