package com.example.workeloapp.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.workeloapp.MemoryData;
import com.example.workeloapp.R;
import com.example.workeloapp.messages.MessagesList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://workelo-default-rtdb.firebaseio.com");
    private String chatKey;
    private final List<ChatList> chatLists = new ArrayList<>();
    FirebaseAuth mAuth;
    String email;
    private RecyclerView chattingRecyclerView;
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        int atLocation = mAuth.getCurrentUser().getEmail().indexOf('@');
        email = mAuth.getCurrentUser().getEmail().substring(0, atLocation);
        final ImageView backBtn = findViewById(R.id.backButtonChat);
        final TextView usernamePerson = findViewById(R.id.usernameChat);
        final EditText messageEditText = findViewById(R.id.messageEditText);
        final ImageView sendBtn = findViewById(R.id.sendMessage);

        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);

        final String username = getIntent().getStringExtra("username");
        chatKey = getIntent().getStringExtra("chat_key");

        usernamePerson.setText(username);
        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        chatAdapter = new ChatAdapter(chatLists, ChatActivity.this);
        chattingRecyclerView.setAdapter(chatAdapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (chatKey.isEmpty()) {
                    chatKey = "1";
                    if (snapshot.hasChild("Chat")) {
                        chatKey = String.valueOf(snapshot.child("Chat").getChildrenCount() + 1);
                        System.out.println(chatKey);
                    }
                }

                if(snapshot.hasChild("Chat")){
                    if (snapshot.child("Chat").child(chatKey).hasChild("Messages")){
                        chatLists.clear();
                        for(DataSnapshot messageSnapshot: snapshot.child("Chat").child(chatKey).child("Messages").getChildren()){
                            if(messageSnapshot.hasChild("msg") && messageSnapshot.hasChild("mobile")){
                                final String messageTimeStamps = messageSnapshot.getKey();
                                final String getMobile = messageSnapshot.child("mobile").getValue(String.class);
                                final String getMsg =messageSnapshot.child("msg").getValue(String.class);
                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimeStamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.CANADA);
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.CANADA);
                                ChatList chatList = new ChatList(getMobile, getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                chatLists.add(chatList);
                                if(Long.parseLong(messageTimeStamps) > Long.parseLong(MemoryData.getLastMsgTS(ChatActivity.this, chatKey))){
                                    MemoryData.saveLastMsgTS(messageTimeStamps, chatKey, ChatActivity.this);
                                    chatAdapter.updateChatList(chatLists);
                                    chattingRecyclerView.scrollToPosition(chatLists.size() - 1);
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getTxtMessage = messageEditText.getText().toString();

                final String currentTimeStamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                databaseReference.child("Chat").child(chatKey).child("user_1").setValue(email);
                databaseReference.child("Chat").child(chatKey).child("user_2").setValue(username);
                databaseReference.child("Chat").child(chatKey).child("Messages").child(currentTimeStamp).child("msg").setValue(getTxtMessage);
                databaseReference.child("Chat").child(chatKey).child("Messages").child(currentTimeStamp).child("mobile").setValue(email);

                messageEditText.setText("");
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}