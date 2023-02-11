package com.example.workeloapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
 * Use the {@link HomeFragmentUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragmentUser extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragmentUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragmentUser.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragmentUser newInstance(String param1, String param2) {
        HomeFragmentUser fragment = new HomeFragmentUser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView imageView;
    private Button switchTo;
    private String assigned;
    String email;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    MyAdapter myAdapter;
    String firstAssign;
    ArrayList<Job> list;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_user, container, false);
        switchTo = view.findViewById(R.id.switchEmployer);
        switchTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you want to switch to Employer page?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.setPositiveButton("Switch", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(view.getContext(), EmployerActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

        imageView = view.findViewById(R.id.profileImageUser);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        recyclerView = view.findViewById(R.id.yourJobs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        myAdapter = new MyAdapter(getActivity(),list);
        recyclerView.setAdapter(myAdapter);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot ds: task.getResult().getChildren()){
                    assigned = ds.getKey();
                    for (DataSnapshot dataSnapshot1: ds.getChildren()){
                        if (!assigned.equals(email)){
                            FirebaseDatabase.getInstance().getReference().child("Employers").child(assigned).child(dataSnapshot1.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    for (DataSnapshot dataSnapshot: task.getResult().getChildren()){
                                        if (dataSnapshot.getKey().equals("Assigned")){
                                            firstAssign = dataSnapshot.getValue(String.class);
                                                System.out.println(assigned);
                                                FirebaseDatabase.getInstance().getReference().child("Employers").child(ds.getKey()).addValueEventListener(new ValueEventListener() {
                                                    @SuppressLint("NotifyDataSetChanged")
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                                                System.out.println("added");
                                                                    Job job = dataSnapshot.getValue(Job.class);
                                                                    if (job.getAssigned().equals(email))
                                                                        list.add(job);
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
                            break;
                        }
                    }
                }
            }
        });
        return view;
    }
}