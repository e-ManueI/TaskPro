package com.example.taskpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ImageView imageViewLogin;
    private TextView registerTextView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmail =  findViewById(R.id.editTextTextEmail);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        imageViewLogin = findViewById(R.id.imageView5);
        registerTextView = findViewById(R.id.textView5);

        imageViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handle register text click
                Intent signUpIntent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    private void login() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email is Required");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is Required");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()) {
                           // login succesful
                           startActivity(new Intent(LoginActivity.this, MainActivity.class));
                           finish();
                       }else{
                           Toast.makeText(LoginActivity.this, "Login Failed. Please check your credentials", Toast.LENGTH_SHORT).show();
                       }
                    }
                });

    }
}