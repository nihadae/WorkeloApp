package com.example.workeloapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.workeloapp.databinding.FragmentJobsUserBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobsFragmentUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobsFragmentUser extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public JobsFragmentUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JobsFragmentUser.
     */
    // TODO: Rename and change types and number of parameters
    public static JobsFragmentUser newInstance(String param1, String param2) {
        JobsFragmentUser fragment = new JobsFragmentUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView alertText;
    private Button cancel, ok;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    MyAdapter myAdapter;
    private ArrayList<Job> list1;
    private String email;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        int atLocation = mAuth.getCurrentUser().getEmail().indexOf('@');
        email = mAuth.getCurrentUser().getEmail().substring(0,atLocation);
        databaseReference = FirebaseDatabase.getInstance().getReference("Employers");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobs_user, container, false);
        recyclerView = view.findViewById(R.id.allJobs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list1 = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(),list1);
        recyclerView.setAdapter(myAdapter);
        FirebaseDatabase.getInstance().getReference().child("Employers").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot dataSnapshot: task.getResult().getChildren()){
                    if (!dataSnapshot.getKey().equals(email)){
                        FirebaseDatabase.getInstance().getReference().child("Employers").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                list1.clear();
                                for(DataSnapshot dataSnapshot1: snapshot.getChildren()){
                                    Job job = dataSnapshot1.getValue(Job.class);
                                    list1.add(job);
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

        return view;
    }
}