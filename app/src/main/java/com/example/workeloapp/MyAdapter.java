package com.example.workeloapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private DatabaseReference databaseReference;
    private String assigned;
    private FirebaseAuth mAuth;
    private String firstAssign;
    private String email;
    public static String valueAddress;
    public static String valueTitle;
    public static String employeeName;
    Context context;
    ArrayList<Job> list;

    public MyAdapter(Context context, ArrayList<Job> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Employers");
        mAuth = FirebaseAuth.getInstance();
        int atLocation = mAuth.getCurrentUser().getEmail().indexOf('@');
        email = mAuth.getCurrentUser().getEmail().substring(0,atLocation);
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Job job = list.get(position);
        holder.Title.setText(job.getTitle());
        holder.Desc.setText(job.getDesc());
        holder.Wage.setText(job.getWage());
        holder.Address.setText(job.getAddress());
        holder.Assigned.setText(job.getAssigned());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int atLocation = view.getContext().toString().indexOf('@');
                if (!view.getContext().toString().substring(0, atLocation).equals("com.example.workeloapp.EmployerActivity")) {
                    valueAddress = list.get(position).getAddress();
                    valueTitle = list.get(position).getTitle();
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to apply for this job?");

                    builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if ((MyAdapter.valueTitle != null && MyAdapter.valueAddress != null)) {
                                FirebaseDatabase.getInstance().getReference().child("Employers").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        for (DataSnapshot ds: task.getResult().getChildren()){
                                            assigned = ds.getKey();
                                            for(DataSnapshot ds1: ds.getChildren()){
                                                if(ds1.getKey().equals(valueTitle) && !assigned.equals(email)){
                                                    FirebaseDatabase.getInstance().getReference().child("Employers").child(assigned).child(ds1.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                            for (DataSnapshot dataSnapshot: task.getResult().getChildren()){
                                                                if (dataSnapshot.getKey().equals("Assigned"))
                                                                    firstAssign = dataSnapshot.getValue(String.class);
                                                            }
                                                            if (firstAssign.equals("No one assigned yet") || !firstAssign.equals(email)){
                                                                FirebaseDatabase.getInstance().getReference().child("Employers").child(ds.getKey()).child(valueTitle).child("Assigned").setValue(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(view.getContext(), "Applied!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }else
                                                                Toast.makeText(view.getContext(), "Either you have already applied or job is taken by someone else.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    break;
                                                }
                                                notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                }else{
                    valueAddress = list.get(position).getAddress();
                    valueTitle = list.get(position).getTitle();
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    builder.setCancelable(true);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Do you want to end this job?");

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseDatabase.getInstance().getReference().child("Employers").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    for (DataSnapshot ds: task.getResult().getChildren()){
                                        assigned = ds.getKey();
                                        for(DataSnapshot ds1: ds.getChildren()){
                                            if(ds1.getKey().equals(valueTitle)){
                                                FirebaseDatabase.getInstance().getReference().child("Employers").child(assigned).child(ds1.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                                        for (DataSnapshot dataSnapshot: task.getResult().getChildren()){
                                                            if (dataSnapshot.getKey().equals("Assigned"))
                                                                firstAssign = dataSnapshot.getValue(String.class);
                                                        }
                                                        if (!firstAssign.equals("No one assigned yet")){
                                                            employeeName = firstAssign;
                                                            FirebaseDatabase.getInstance().getReference().child("Employers").child(email).child(valueTitle).removeValue();
//                                                            FirebaseDatabase.getInstance().getReference().child("Chat").
                                                            view.getContext().startActivity(new Intent(view.getContext(), RatingActivity.class));
                                                            Toast.makeText(view.getContext(), "You ended the job!", Toast.LENGTH_SHORT).show();
                                                        }else
                                                            Toast.makeText(view.getContext(), "You cannot end this job!", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                break;
                                            }
                                            notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Title, Desc, Wage, Address, Assigned;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            Title = itemView.findViewById(R.id.titleJob);
            Desc = itemView.findViewById(R.id.descJob);
            Wage = itemView.findViewById(R.id.wageJob);
            Address = itemView.findViewById(R.id.addressJob);
            Assigned = itemView.findViewById(R.id.assignedJob);
        }
    }
}

