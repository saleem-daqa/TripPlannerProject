package com.example.tripplanningmobileapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView goToSignUp;
    private CheckBox rememberMeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        goToSignUp = findViewById(R.id.goToSignUp);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);

        boolean rememberMe = prefs.getBoolean("rememberMe", false);
        if (rememberMe) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            String savedEmail = prefs.getString("email", "");
            String savedPassword = prefs.getString("password", "");

            if (email.equals(savedEmail) && password.equals(savedPassword)) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("rememberMe", rememberMeCheckBox.isChecked());
                editor.apply();

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            } else {
                Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
            }
        });

        goToSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });
    }
}