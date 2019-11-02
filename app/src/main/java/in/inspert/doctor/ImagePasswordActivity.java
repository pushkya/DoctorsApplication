package in.inspert.doctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ImagePasswordActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterPasswordImage adapterPasswordImage;
    Button buttonContinue, buttonReset;
    TextView textViewEmail;

    boolean isSinUp = true;
    String email;
    String password = "";
    String userType = "";
    String name = "";


    ArrayList<Integer> images;

    int images1[] = {R.drawable.a9, R.drawable.a8, R.drawable.a7, R.drawable.a6, R.drawable.a5, R.drawable.a4, R.drawable.a3, R.drawable.a2, R.drawable.a1};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_image_password);
        recyclerView = findViewById(R.id.recyclerViewImage);
        textViewEmail = findViewById(R.id.textViewEmail);

        email = getIntent().getStringExtra("email");
        textViewEmail.setText("WelCome"+email);

        if (getIntent().getStringExtra("user") != null){
            userType = getIntent().getStringExtra("user");
        }

        if (getIntent().getStringExtra("name") != null){
            name = getIntent().getStringExtra("name");
        }

        isSinUp = getIntent().getStringExtra("sign").equals("up");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(false);
        images = new ArrayList<>();
        for (int i: images1){
            images.add(i);
        }
        if (!isSinUp) {
            Collections.shuffle(images);
        }

        adapterPasswordImage = new AdapterPasswordImage(this, images);
        recyclerView.setAdapter(adapterPasswordImage);

        buttonContinue = findViewById(R.id.btnImagePasswordContinue);
        buttonReset = findViewById(R.id.btnImagePasswordReset);

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("user");

                if (isSinUp){
                    DatabaseReference userRef = myRef.push();
                    userRef.child("email").setValue(email);
                    userRef.child("password").setValue(password);
                    userRef.child("type").setValue(userType);
                    userRef.child("name").setValue(name);
                    Toast.makeText(ImagePasswordActivity.this, "Success", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(ImagePasswordActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                } else {
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                                if (dataSnapshot1.child("email").getValue(String.class).equals(email)) {
                                    String em = dataSnapshot1.child("email").getValue(String.class);
                                    String pass = dataSnapshot1.child("password").getValue(String.class);
                                    String type = dataSnapshot1.child("type").getValue(String.class);
                                    String name = dataSnapshot1.child("name").getValue(String.class);

                                    if (pass.equals(password)) {

                                        SharedPreferences.Editor editor = getSharedPreferences("doc", MODE_PRIVATE).edit();
                                        editor.putString("email", em);
                                        editor.putString("pass", pass);
                                        editor.putString("name",name);
                                        editor.putString("type",type);
                                        editor.putString("id",dataSnapshot1.getKey());
                                        editor.apply();

                                        Toast.makeText(ImagePasswordActivity.this, "Success", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(ImagePasswordActivity.this, ActivityDashboard.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(ImagePasswordActivity.this, "Password Not match", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });



        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = "";
                adapterPasswordImage.notifyDataSetChanged();

            }
        });


        adapterPasswordImage.setOnItemClick(new AdapterPasswordImage.OnItemClick() {
            @Override
            public void onClick(int position) {

                int pos = getClick(images.get(position));

                if (!password.contains("" + pos)){
                    password = password+ pos;
                }
                if(password.length() <3 ){
                    Toast.makeText(ImagePasswordActivity.this, "select at least 3", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


    private int getClick(int image){
        switch (image){
            case R.drawable.a1:
                return 0;
            case R.drawable.a2:
                return 1;
            case R.drawable.a3:
                return 2;
            case R.drawable.a4:
                return 3;
            case R.drawable.a5:
                return 4;
            case R.drawable.a6:
                return 5;
            case R.drawable.a7:
                return 6;
            case R.drawable.a8:
                return 7;
            case R.drawable.a9:
                return 8;
        }

        return 0;
    }
}
