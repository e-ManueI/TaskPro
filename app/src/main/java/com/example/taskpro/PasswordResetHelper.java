package com.example.taskpro;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetHelper {
    private FirebaseAuth mAuth;

    public PasswordResetHelper() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void resetPassword(String email, final Activity activity) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Password reset email sent. Please check your email.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "Failed to send password reset email. Please check the email address.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void resetPassword(String email, final Fragment fragment) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(fragment.requireActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(fragment.requireContext(), "Password reset email sent. Please check your email.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(fragment.requireContext(), "Failed to send password reset email. Please check the email address.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
