package com.example.workeloapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.workeloapp.messages.MessagesAdapter;
import com.example.workeloapp.messages.MessagesList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InboxFragmentUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragmentUser extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InboxFragmentUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InboxFragmentUser.
     */
    // TODO: Rename and change types and number of parameters
    public static InboxFragmentUser newInstance(String param1, String param2) {
        InboxFragmentUser fragment = new InboxFragmentUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private final List<MessagesList> messagesLists = new ArrayList<>();
    private RecyclerView messagesRecyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    private String email;
    private int unseenMessages = 0;
    private String lastMessage = "";
    private MessagesAdapter messagesAdapter;
    private String chatKey = "";
    private boolean dataSet = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        int atLocation = mAuth.getCurrentUser().getEmail().indexOf('@');
        email = mAuth.getCurrentUser().getEmail().substring(0,atLocation);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Employers");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox_user, container, false);
        messagesRecyclerView = view.findViewById(R.id.messagesRecylerViewUser);
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        messagesAdapter = new MessagesAdapter(messagesLists, view.getContext());
        messagesRecyclerView.setAdapter(messagesAdapter);

        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot dataSnapshot: task.getResult().getChildren()){
                    String employer;
                    if(!dataSnapshot.getKey().equals(email)){
                        employer = dataSnapshot.getKey();
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            for (DataSnapshot dataSnapshot2: dataSnapshot1.getChildren()){
                                if(dataSnapshot2.getKey().equals("Assigned")){
                                    messagesLists.clear();
                                    unseenMessages = 0;
                                    lastMessage = "";
                                    chatKey = "";
                                    if(dataSnapshot2.getValue(String.class).equals(email)){
                                        FirebaseDatabase.getInstance().getReferenceFromUrl("https://workelo-default-rtdb.firebaseio.com").child("Chat").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                int getChatCounts = (int) snapshot.getChildrenCount();

                                                if(getChatCounts > 0){
                                                    for(DataSnapshot dataSnapshot2: snapshot.getChildren()){
                                                        final String getKey = dataSnapshot2.getKey();
                                                        chatKey = getKey;
                                                        if (dataSnapshot2.hasChild("user_1") && dataSnapshot2.hasChild("user_2") && dataSnapshot2.hasChild("Messages")){
                                                            final String getUserOne = dataSnapshot2.child("user_1").getValue(String.class);
                                                            final String getUserTwo = dataSnapshot2.child("user_2").getValue(String.class);

                                                            if((getUserOne.equals(email) && getUserTwo.equals(employer)) || (getUserOne.equals(employer) && getUserTwo.equals(email))){
                                                                for(DataSnapshot chatDataSnapshot: dataSnapshot2.child("Messages").getChildren()){
                                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                                    final long getLastSeenMessage = Long.parseLong(MemoryData.getLastMsgTS(view.getContext(), getKey));

                                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);
                                                                    if(getMessageKey > getLastSeenMessage){
                                                                        unseenMessages++;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if(!dataSet){
                                                    dataSet = true;
                                                    MessagesList messagesList = new MessagesList(employer, lastMessage, unseenMessages, chatKey);
                                                    messagesLists.add(messagesList);
                                                    messagesAdapter.updateData(messagesLists);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
        progressDialog.dismiss();
        return view;
    }
}