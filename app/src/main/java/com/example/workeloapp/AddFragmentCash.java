package com.example.workeloapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragmentCash#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragmentCash extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFragmentCash() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFragmentCash.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFragmentCash newInstance(String param1, String param2) {
        AddFragmentCash fragment = new AddFragmentCash();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private EditText titleAdd;
    private EditText descAdd;
    private EditText wageAdd;
    private EditText addressAdd;
    private Button addButton;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_cash, container, false);
        titleAdd = view.findViewById(R.id.titleAdd);
        descAdd = view.findViewById(R.id.descAdd);
        wageAdd = view.findViewById(R.id.wageAdd);
        addressAdd = view.findViewById(R.id.addressAdd);
        addButton = view.findViewById(R.id.addJob);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleAdd.getText().toString();
                String desc = descAdd.getText().toString();
                String wage = wageAdd.getText().toString();
                String address = addressAdd.getText().toString();
                if (title.equals("") || desc.equals("") || wage.equals("") || address.equals("")){
                    Toast.makeText(view.getContext(), "Please type in all the required fields.", Toast.LENGTH_SHORT).show();
                }else{
                    int atLocation = mAuth.getCurrentUser().getEmail().indexOf('@');
                    String email = mAuth.getCurrentUser().getEmail().substring(0,atLocation);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Title", title);
                    hashMap.put("Desc", desc);
                    hashMap.put("Wage", wage);
                    hashMap.put("Address", address);
                    hashMap.put("Assigned", "No one assigned yet");

                    databaseReference.child("Employers")
                            .child(email).child(title).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getActivity(), "Job added succesfully.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Error happened: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        return view;
    }

}