package com.example.whatappclone.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatappclone.ChatActivity;
import com.example.whatappclone.R;
import com.example.whatappclone.models.Message;
import com.example.whatappclone.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<User> users;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public UserAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.box_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        Picasso.get().load(user.getProfilePictureLink()).placeholder(R.drawable.avatar3).into(holder.imgProfile);
        holder.name.setText(user.getUsername());
        String senderRoom = FirebaseAuth.getInstance().getUid().toString() + user.getUserID();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("usrID", user.getUserID());
                intent.putExtra("usrName", user.getUsername());
                intent.putExtra("proPicLink", user.getProfilePictureLink());
                context.startActivity(intent);
            }
        });

        database.getReference().child("Chats")
                .child(senderRoom).orderByChild("timestamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                            holder.lastMessage.setText(dataSnapshot.child("content").getValue().toString());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProfile;
        TextView name, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            name = itemView.findViewById(R.id.txtUserNameDisplay);
            lastMessage = itemView.findViewById(R.id.txtLastMessage);
        }
    }
}
