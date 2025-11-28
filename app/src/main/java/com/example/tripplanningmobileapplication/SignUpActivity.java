package com.example.tripplanningmobileapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private TextView goToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        goToLogin = findViewById(R.id.goToLogin);

        signUpButton.setOnClickListener(v -> {

            String fullName = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] nameParts = fullName.split(" ");
            if (nameParts.length < 2) {
                Toast.makeText(this, "Please enter your first and last name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8) {
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("name", fullName);
            editor.putString("email", email);
            editor.putString("password", password);

            editor.apply();

            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        goToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }
}