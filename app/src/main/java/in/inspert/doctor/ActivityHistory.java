package in.inspert.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityHistory extends AppCompatActivity {
    RecyclerView recyclerViewList;
    AdapterPatient adapterPatient;

    ArrayList<Patient> patients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);
        recyclerViewList = findViewById(R.id.recyclerViewHistory);
        patients = new ArrayList<>();
        adapterPatient = new AdapterPatient(this, patients, "History");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerViewList.setLayoutManager(linearLayoutManager);

        recyclerViewList.setAdapter(adapterPatient);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("patient");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patients.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String status = dataSnapshot1.child("status").getValue(String.class);
                    if (status != null && status.equals("done")) {

                        patients.add(new Patient(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue(String.class),
                                dataSnapshot1.child("number").getValue(String.class),
                                dataSnapshot1.child("desc").getValue(String.class),
                                dataSnapshot1.child("price").getValue(String.class),
                                dataSnapshot1.child("time").getValue(String.class),
                                dataSnapshot1.child("age").getValue(String.class),
                                dataSnapshot1.child("blood").getValue(String.class),
                                dataSnapshot1.child("med").getValue(String.class)));
                    }
                }
                adapterPatient.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        adapterPatient.setOnItemClickListener(new AdapterPatient.OnItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(ActivityHistory.this, PatientDetailsActivity.class);
                intent.putExtra("id",patients.get(pos).getId());
                intent.putExtra("desc",patients.get(pos).getDesc());
                intent.putExtra("name",patients.get(pos).getName());
                intent.putExtra("price",patients.get(pos).getPrice());
                intent.putExtra("age",patients.get(pos).getAge());
                intent.putExtra("blood",patients.get(pos).getBlood());
                intent.putExtra("med",patients.get(pos).getMed());

                startActivity(intent);
            }
        });

    }
}
