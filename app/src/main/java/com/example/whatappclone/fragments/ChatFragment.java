package com.example.whatappclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.whatappclone.R;
import com.example.whatappclone.adapters.UserAdapter;
import com.example.whatappclone.databinding.FragmentChatBinding;
import com.example.whatappclone.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    FragmentChatBinding chatBinding;
    List<User> users = new ArrayList<>();
    FirebaseDatabase database;

    public ChatFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatBinding = FragmentChatBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        UserAdapter userAdapter = new UserAdapter(users, getContext());
        chatBinding.chatRecyclerView.setAdapter(userAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatBinding.chatRecyclerView.setLayoutManager(layoutManager);
        database.getReference().child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot data: snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    //user.setUserID(data.getKey());
                    users.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return chatBinding.getRoot();
    }
}