package com.example.taskpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword, editTextPassword2;
    private ImageView imageViewSignUp;
    private TextView loginTextView;
    private FirebaseAuth firebaseAuth;
    private boolean isNetworkAvailable = true;
    private SignupActivity.NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

//        Find views by ID
        editTextName = findViewById(R.id.editTextTextName);
        editTextEmail = findViewById(R.id.editTextTextEmail);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextPassword2 = findViewById(R.id.editTextTextPassword2);
        imageViewSignUp = findViewById(R.id.imageView5);

        loginTextView = findViewById(R.id.textView5);

        imageViewSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                handle login text click
                Intent loginIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        // Register network change receiver
        networkChangeReceiver = new SignupActivity.NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister network change receiver
        unregisterReceiver(networkChangeReceiver);
    }

    private void signUp() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();

        if (!isNetworkAvailable) {
            // No network connection, disable input fields
            editTextName.setEnabled(false);
            editTextEmail.setEnabled(false);
            editTextPassword.setEnabled(false);
            editTextPassword2.setEnabled(false);
            Toast.makeText(SignupActivity.this, "No internet connection. Please check your network settings.", Toast.LENGTH_LONG).show();
            return;
        }

//        Validate the input fields
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(password2)) {
            Toast.makeText(SignupActivity.this, "Password do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user in Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get the user ID of the newly created user
                            String userId = firebaseAuth.getCurrentUser().getUid();

                            // Save the name to Firebase Realtime Database under the user's ID
                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                            usersRef.child(userId).child("name").setValue(name);

                            // sign-up process
                            Toast.makeText(SignupActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));

                            // I can add logic here, such as navigating to the home screen.
                        }else {
                            // Sign-up failed
                            Toast.makeText(SignupActivity.this, "Sign-up Failed:" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isNetworkAvailable()) {
                isNetworkAvailable = true;
                editTextName.setEnabled(true);
                editTextEmail.setEnabled(true);
                editTextPassword.setEnabled(true);
                editTextPassword2.setEnabled(true);
                Toast.makeText(SignupActivity.this, "Network connection available", Toast.LENGTH_SHORT).show();
            } else {
                isNetworkAvailable = false;
                editTextName.setEnabled(false);
                editTextEmail.setEnabled(false);
                editTextPassword.setEnabled(false);
                editTextPassword2.setEnabled(false);
                Toast.makeText(SignupActivity.this, "No internet connection. Please check your network settings.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}