package in.inspert.doctor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PatientDetailsActivity extends AppCompatActivity {
    String name, id, desc, price, age, blood, med;
    TextView textViewName;
    EditText editTextDesc, editTextPrice, editTextAge, editTextBlood, editTextMed;
    Button btnSave;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_patient_details);
        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");
        desc = getIntent().getStringExtra("desc");
        price = getIntent().getStringExtra("price");
        age = getIntent().getStringExtra("age");
        blood = getIntent().getStringExtra("blood");
        med = getIntent().getStringExtra("med");

        textViewName = findViewById(R.id.textViewName);
        editTextDesc = findViewById(R.id.editTextDesc);
        editTextPrice = findViewById(R.id.ediTextPrice);
        editTextAge = findViewById(R.id.ediTextAge);
        editTextBlood = findViewById(R.id.editTextBlood);
        editTextMed = findViewById(R.id.editTextMed);

        btnSave = findViewById(R.id.btnSave);

        textViewName.setText(name);

        if (!desc.isEmpty()){
            editTextDesc.setText(desc);
        }

        if (!price.isEmpty()){
            editTextPrice.setText(price);
        }


        if (!blood.isEmpty()){
            editTextBlood.setText(blood);
        }

        if (!age.isEmpty()){
            editTextAge.setText(age);
        }

        if (!med.isEmpty()){
            editTextMed.setText(med);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                 DatabaseReference myRef = database.getReference("patient");
                 myRef.child(id).child("desc").setValue(editTextDesc.getText().toString());
                 myRef.child(id).child("price").setValue(editTextPrice.getText().toString());
                myRef.child(id).child("age").setValue(editTextAge.getText().toString());
                myRef.child(id).child("blood").setValue(editTextBlood.getText().toString());
                myRef.child(id).child("med").setValue(editTextMed.getText().toString());

                Toast.makeText(PatientDetailsActivity.this, "Data saved",Toast.LENGTH_LONG).show();
                finish();

            }
        });

    }
}
