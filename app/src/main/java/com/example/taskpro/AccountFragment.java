package com.example.taskpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private TextView userNameTextView;
    private TextView userEmailTextView;
    private TextView emailTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        userNameTextView = rootView.findViewById(R.id.userName);
        userEmailTextView = rootView.findViewById(R.id.userEmail);
        emailTextView = rootView.findViewById(R.id.textView27);

        // Retrieve user data from Firebase and update TextViews
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            userEmailTextView.setText(userEmail);
            emailTextView.setText(userEmail);

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        userNameTextView.setText(userName);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }

        // Find password layout and components
        ImageView passwordArrow = rootView.findViewById(R.id.imageChangePasswordArrow);
        LinearLayout passwordLayout = rootView.findViewById(R.id.passwordChange);

        passwordArrow.setOnClickListener(this);
        passwordLayout.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        // Display the change password dialog
        ChangePasswordDialogFragment dialogFragment = new ChangePasswordDialogFragment();
        dialogFragment.show(getParentFragmentManager(), "change_password_dialog");
    }
}
