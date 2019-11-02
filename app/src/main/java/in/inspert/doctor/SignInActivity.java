package in.inspert.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignInActivity extends AppCompatActivity {
    EditText editTextEmail;
    Button buttonPassword;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonPassword = findViewById(R.id.btnEnterPassword);

        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ImagePasswordActivity.class);
                intent.putExtra("email",editTextEmail.getText().toString().trim());
                intent.putExtra("sign","in");
                startActivity(intent);
            }
        });
    }
}
