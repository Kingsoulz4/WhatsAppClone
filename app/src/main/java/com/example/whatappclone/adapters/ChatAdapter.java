package com.example.whatappclone.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatappclone.R;
import com.example.whatappclone.models.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter {

    List<Message> messages;
    Context context;
    String receiverUID;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    String senderRoom;

    final int INCOMING_VIEW_TYPE = 1;
    final int OUTGOING_VIEW_TYPE = 2;

    public ChatAdapter(List<Message> messages, Context context, String receiverUID) {
        this.messages = messages;
        this.context = context;
        this.receiverUID = receiverUID;
    }

    public ChatAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == INCOMING_VIEW_TYPE) {
            view = LayoutInflater.from(context).inflate(R.layout.incoming_message, parent, false);
            return new InComingMessageViewHolder(view);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.outgoing_message, parent, false);
            return new OutGoingMessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        Date date = new Date(message.getTimestamp());
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm a");
        if (holder.getClass() == InComingMessageViewHolder.class) {
            InComingMessageViewHolder viewHolder = ((InComingMessageViewHolder) holder);
            viewHolder.txtInComingMessage.setText(message.getContent());
            viewHolder.txtInComingMessageTime.setText(format.format(date));

        }
        else {
            OutGoingMessageViewHolder viewHolder = ((OutGoingMessageViewHolder) holder);
            viewHolder.txtOutGoingMessage.setText(message.getContent());
            viewHolder.txtOutGoingMessageTime.setText(format.format(date));
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete message");
                alertDialog.setMessage("Are you sure to delete this message?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        senderRoom = myAuth.getUid() + receiverUID;
                        Log.d("Room: ", senderRoom);
                        Log.d("Delete message:" , message.getMessageID());
                        database.getReference().child("Chats")
                                .child(senderRoom).child(message.getMessageID())
                                .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Delete successfull ", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getuID().equals(FirebaseAuth.getInstance().getUid())) {
            return OUTGOING_VIEW_TYPE;
        }
        else return INCOMING_VIEW_TYPE;
    }

    public class InComingMessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtInComingMessage;
        TextView txtInComingMessageTime;

        public InComingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInComingMessage = itemView.findViewById(R.id.txtInComingMessage);
            txtInComingMessageTime = itemView.findViewById(R.id.txtInComingMessageTime);
        }
    }

    public class OutGoingMessageViewHolder extends RecyclerView.ViewHolder  {

        TextView txtOutGoingMessage;
        TextView txtOutGoingMessageTime;

        public OutGoingMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOutGoingMessage = itemView.findViewById(R.id.txtOutgoingMessage);
            txtOutGoingMessageTime = itemView.findViewById(R.id.txtOutgoingMessageTime);
        }
    }

}
