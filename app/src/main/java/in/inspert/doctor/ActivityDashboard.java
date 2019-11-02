package in.inspert.doctor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Arrays;

public class ActivityDashboard extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    RecyclerView recyclerViewList;
    AdapterPatient adapterPatient;
    ArrayList<Patient> patients;
    String userType = "Doctor";

    String[] timeSlot = {"09:00am","09:30am","10:00am","10:30am","11:00am","11:30am","12:00pm","12:30pm",
            "2:00pm","2:30pm","3:00pm","3:30pm","4:00am","4:30pm", "5:00pm","5:30pm", "6:00pm","6:30pm",};
    ArrayList<String> timeS;
    String currentTime = "";
    EditText editTextName, editTextNumber;
    Button buttonAdd, buttonHistory;
    Spinner spinner;
    String SENDER_ID = null;

    ArrayAdapter adSpinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);
        recyclerViewList = findViewById(R.id.recyclerViewPatient);
        editTextName = findViewById(R.id.editAddTextName);
        editTextNumber = findViewById(R.id.editAddNumber);
        buttonAdd = findViewById(R.id.btnAddPatient);
        buttonHistory = findViewById(R.id.btnHistory);
        spinner = findViewById(R.id.spinnerTime);

        spinner.setOnItemSelectedListener(this);

        timeS = new ArrayList<>();
        timeS.addAll(Arrays.asList(timeSlot));
        adSpinner = new ArrayAdapter(this,android.R.layout.simple_spinner_item,timeS);
        adSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adSpinner);

        SharedPreferences sharedPreferences = getSharedPreferences("doc", MODE_PRIVATE);
        userType = sharedPreferences.getString("type","Doctor");

        patients = new ArrayList<>();
        adapterPatient = new AdapterPatient(this, patients, userType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerViewList.setLayoutManager(linearLayoutManager);

        recyclerViewList.setAdapter(adapterPatient);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("patient");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                patients.clear();
                timeS.clear();
                timeS.addAll(Arrays.asList(timeSlot));

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String status = dataSnapshot1.child("status").getValue(String.class);
                    if (status != null && status.equals("pending")) {
                        String fbTime = dataSnapshot1.child("time").getValue(String.class);
                        timeS.remove(fbTime);
                        Log.e("keyyyy",dataSnapshot1.getKey());
                        patients.add(new Patient(dataSnapshot1.getKey(), dataSnapshot1.child("name").getValue(String.class),
                                dataSnapshot1.child("number").getValue(String.class),
                                dataSnapshot1.child("desc").getValue(String.class),
                                dataSnapshot1.child("price").getValue(String.class),
                                dataSnapshot1.child("time").getValue(String.class),
                                dataSnapshot1.child("age").getValue(String.class),
                                dataSnapshot1.child("med").getValue(String.class),
                                dataSnapshot1.child("blood").getValue(String.class)));
                    }
                }
                if (timeS.size()>0) {
                    currentTime = timeS.get(0);
                } else {
                    currentTime = "--";
                }
                adSpinner.notifyDataSetChanged();
                adapterPatient.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        FirebaseDatabase.getInstance().getReference("doctor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SENDER_ID = dataSnapshot.child("id").getValue(String.class);
                Log.e("idddd",SENDER_ID+" --");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        adapterPatient.setOnClickListener(new AdapterPatient.OnClickListener() {
            @Override
            public void onClick(int position) {
                if (userType.equals("Doctor")){
                    Intent intent = new Intent(ActivityDashboard.this, PatientDetailsActivity.class);
                    intent.putExtra("id",patients.get(position).getId());
                    intent.putExtra("desc",patients.get(position).getDesc());
                    intent.putExtra("name",patients.get(position).getName());
                    intent.putExtra("price",patients.get(position).getPrice());
                    intent.putExtra("age",patients.get(position).getAge());
                    intent.putExtra("med",patients.get(position).getMed());
                    intent.putExtra("blood",patients.get(position).getBlood());

                    startActivity(intent);
                } else {
                    if (SENDER_ID != null) {
                        FirebaseMessaging fm = FirebaseMessaging.getInstance();
                        fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
                                .setMessageId(Integer.toString(10))
                                .addData("my_message", "Hello World")
                                .build());
                    }
                        sendSMS(patients.get(position).getNumber(),"Hi "+patients.get(position).getName()+", Your appointment is schedule on "+ patients.get(position).getTime());

                }
            }

            @Override
            public void onDoneClick(int position) {
                myRef.child(patients.get(position).getId()).child("status").setValue("done");
                sendSMS(patients.get(position).getNumber(),"Hi "+patients.get(position).getName()+", thanks for visiting, your total bill amount is "+ patients.get(position).getPrice());
                Toast.makeText(ActivityDashboard.this, "Sent Billing info and patient can be seen in History ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {

                myRef.child(patients.get(position).getId()).setValue(null);
            }


        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = myRef;
                if (editTextName.getText().toString().isEmpty() || editTextNumber.getText().toString().isEmpty()){
                    Toast.makeText(ActivityDashboard.this, "Enter all fields",Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child(currentTime).child("status").setValue("pending");
                    databaseReference.child(currentTime).child("name").setValue(editTextName.getText().toString());
                    databaseReference.child(currentTime).child("number").setValue(editTextNumber.getText().toString());
                    databaseReference.child(currentTime).child("time").setValue(currentTime);
                    databaseReference.child(currentTime).child("price").setValue("");
                    databaseReference.child(currentTime).child("desc").setValue("");
                    databaseReference.child(currentTime).child("age").setValue("");
                    databaseReference.child(currentTime).child("med").setValue("");
                    databaseReference.child(currentTime).child("blood").setValue("");

                }
            }
        });

        buttonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityDashboard.this, ActivityHistory.class));
            }
        });


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, 1);

            }
        }


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("FCM", "getInstanceId failed", task.getException());
                    return;
                }
                if (userType.equals("Doctor")){
                    FirebaseDatabase.getInstance().getReference("doctor").child("token").setValue(task.getResult().getToken());
                    FirebaseDatabase.getInstance().getReference("doctor").child("id").setValue(task.getResult().getId());
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currentTime = timeS.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
    }
}
