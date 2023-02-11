package com.example.workeloapp.messages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workeloapp.R;
import com.example.workeloapp.RegisterActivity;
import com.example.workeloapp.chat.ChatActivity;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    private List<MessagesList> messagesLists;
    private final Context context;

    public MessagesAdapter(List<MessagesList> messagesLists, Context context) {
        this.messagesLists = messagesLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adapter_layout, null));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessagesList list2 = messagesLists.get(position);
        holder.username.setText(list2.getUsername());
        holder.lastMessage.setText(list2.getLastMessage());
        if(list2.getUnseenMessages() == 0){
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        }
        else{
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.lastMessage.setText(list2.getLastMessage());
            holder.lastMessage.setTextColor(Color.parseColor("#000000"));

        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("username", list2.getUsername());
                intent.putExtra("chat_key", list2.getChatKey());
                context.startActivity(intent);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<MessagesList> messagesLists){
        this.messagesLists = messagesLists;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return messagesLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView lastMessage;
        private TextView unseenMessages;
        private LinearLayout rootLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usernameMessage);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
