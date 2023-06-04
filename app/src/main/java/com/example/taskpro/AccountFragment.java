package com.example.taskpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AccountFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

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
