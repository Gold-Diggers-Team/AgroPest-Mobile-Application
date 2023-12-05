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

    private EditText emailEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        emailEditText = view.findViewById(R.id.emailEdit);
        Button resetPasswordButton = view.findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        return view;
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {
           emailEditText.setError(getString(R.string.Email_is_required));
            return;
        }


        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(),getString(R.string.password_reset_email),Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),getString(R.string.password_reset_email_failed),Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
