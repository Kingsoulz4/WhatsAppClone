package com.example.whatappclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.whatappclone.databinding.ActivityProfileDetailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileDetailActivity extends AppCompatActivity {

    ActivityProfileDetailBinding profileDetailBinding;
    FirebaseAuth myAuth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileDetailBinding = ActivityProfileDetailBinding.inflate(getLayoutInflater());
        setContentView(profileDetailBinding.getRoot());
        getSupportActionBar().hide();
        controlsHandle();
        eventsHandle();
    }

    private void eventsHandle() {
        profileDetailBinding.btnChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 23);
            }
        });
    }

    private void controlsHandle() {
        myAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        //Log.d("Profile pic", database.getReference().child("User").child(myAuth.getUid()).child("profilePictureLink").);
        DatabaseReference ref = database.getReference().child("User").child(myAuth.getUid()).child("profilePictureLink");
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful() && task.getResult().getValue() != null) {
                    String pic = task.getResult().getValue().toString();
                    Picasso.get().load(pic)
                            .placeholder(R.drawable.avatar3).into(profileDetailBinding.imgProfilePictureInProfileDetail);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null) {
            Uri file = data.getData();
            profileDetailBinding.imgProfilePictureInProfileDetail.setImageURI(file);
            StorageReference ref = storage.getReference().child("ProfilePicture")
                                            .child(myAuth.getUid());
            ref.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("User").child(myAuth.getUid())
                                    .child("profilePictureLink").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}