package com.example.whatappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatappclone.adapters.ChatAdapter;
import com.example.whatappclone.databinding.ActivityChatBinding;
import com.example.whatappclone.models.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding chatBinding;
    FirebaseDatabase database;
    FirebaseAuth myAuth;
    ChatAdapter adapter;
    List<Message> messages = new ArrayList<>();
    String receiverUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());
        getSupportActionBar().hide();
        controlsHandle();
        eventsHandle();
    }

    private void eventsHandle() {
        String senderRoom = myAuth.getUid() + receiverUID;
        String receiverRoom = receiverUID + myAuth.getUid();
        String senderID = myAuth.getUid();
        chatBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageContent = chatBinding.txtMessageEnter.getText().toString();
                Message message = new Message();
                message.setContent(messageContent);
                message.setuID(senderID);
                message.setTimestamp(new Date().getTime());
                chatBinding.txtMessageEnter.setText("");
                database.getReference().child("Chats")
                        .child(senderRoom).push()
                        .setValue(message)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        database.getReference().child("Chats")
                                .child(receiverRoom).push()
                                .setValue(message)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                    }
                });

            }
        });

        database.getReference().child("Chats").child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            message.setMessageID(dataSnapshot.getKey());
                            messages.add(message);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        chatBinding.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void controlsHandle() {
        database = FirebaseDatabase.getInstance();
        myAuth = FirebaseAuth.getInstance();
        String receiverUserName = getIntent().getStringExtra("usrName");
        String receiverProPicName = getIntent().getStringExtra("proPicLink");
        receiverUID = getIntent().getStringExtra("usrID");
        chatBinding.txtReceiverUserName.setText(receiverUserName);
        Picasso.get().load(receiverProPicName).placeholder(R.drawable.avatar3).into(chatBinding.imgUserDisplayInChat);

        adapter = new ChatAdapter(messages, ChatActivity.this, receiverUID);
        chatBinding.chatDisplay.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        chatBinding.chatDisplay.setLayoutManager(layoutManager);
    }
}