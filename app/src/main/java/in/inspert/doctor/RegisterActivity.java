package in.inspert.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail;
    Button buttonCreatePassword;
    RadioGroup radioGroup;
    RadioButton radioButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextName = findViewById(R.id.editTextName);
        buttonCreatePassword = findViewById(R.id.btnCreatePassword);
        radioGroup = findViewById(R.id.radioGroup);



        buttonCreatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextEmail.getText().toString()==null){
                    Toast.makeText(RegisterActivity.this, "please Enter email", Toast.LENGTH_SHORT).show();
                }
                else if(editTextName.getText().toString()==null){
                    Toast.makeText(RegisterActivity.this, "please Enter email", Toast.LENGTH_SHORT).show();
                }
                if(getUserType()==null){
                    Toast.makeText(RegisterActivity.this, "select role", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(RegisterActivity.this, ImagePasswordActivity.class);
                    intent.putExtra("email", editTextEmail.getText().toString().trim());
                    intent.putExtra("name", editTextName.getText().toString().trim());
                    intent.putExtra("sign", "up");
                    intent.putExtra("user", getUserType());
                    startActivity(intent);
                }
            }
        });

    }

    private String getUserType(){
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);

        if(selectedId==-1){
            Toast.makeText(this,"Nothing selected", Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            Toast.makeText(this,radioButton.getText(), Toast.LENGTH_SHORT).show();
           return radioButton.getText().toString();
        }

    }
}
