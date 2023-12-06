package com.example.agropestapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    EditText emailEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        // Initializing the emailEditText from the layout
        emailEditText = view.findViewById(R.id.emailEdit);
        // Initializing the resetPasswordButton from the layout
        Button resetPasswordButton = view.findViewById(R.id.resetPasswordButton);

        // Setting a click listener for the resetPasswordButton
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the resetPassword method when the button is clicked
                resetPassword();
            }
        });

        return view;
    }

    // Method to handle the password reset functionality
    private void resetPassword() {
        // Get the entered email from the emailEditText
        String email = emailEditText.getText().toString().trim();

        // Check if the email is empty
        if (email.isEmpty()) {
            // Show an error message in the emailEditText if it's empty
            emailEditText.setError(getString(R.string.Email_is_required));
            return;
        }

        // Use Firebase Authentication to send a password reset email
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    // Check if the password reset email was sent successfully
                    if (task.isSuccessful()) {
                        // Show a success message if the email was sent
                        Toast.makeText(getContext(), getString(R.string.password_reset_email), Toast.LENGTH_SHORT).show();
                    } else {
                        // Show an error message if the email sending failed
                        Toast.makeText(getContext(), getString(R.string.password_reset_email_failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
