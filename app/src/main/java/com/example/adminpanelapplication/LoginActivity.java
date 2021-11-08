package com.example.adminpanelapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    String email, password;
    EditText etEmail, etPassword;
    TextView error;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String aemail="admin@admin.com";
        String bemail="admin@gmail.com";
        String cemail="admin@live.com";

        String pass="admin";
        String pass1="1234";
        String pass2="Admin";

        TextView textView = findViewById(R.id.textView1);

        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        etEmail = findViewById(R.id.email);

        etPassword = findViewById(R.id.password);

        error = findViewById(R.id.tvError);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(this, MainActivity.class));

                email = etEmail.getText().toString();

                password = etPassword.getText().toString();

                if (email.equals(aemail) || email.equals(bemail) || email.equals(cemail) && password.equals(pass) || password.equals(pass1) || password.equals(pass2) ){
                    Intent intent = new Intent(LoginActivity.this, Splash.class);
                    Toast.makeText(LoginActivity.this, "LOGIN SUCCESS", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "INCORRECT LOGIN INFO ENTERED, PLEASE TRY AGAIN", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}