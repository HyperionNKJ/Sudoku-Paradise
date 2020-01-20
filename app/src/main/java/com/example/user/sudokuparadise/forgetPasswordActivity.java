package com.example.user.sudokuparadise;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetPasswordActivity extends AppCompatActivity {
    private EditText mEditTextEmail;
    private Button mBtnResetPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mEditTextEmail = findViewById(R.id.editTextEmail_fp);
        mBtnResetPassword = findViewById(R.id.buttonResetPassword_fp);
        auth = FirebaseAuth.getInstance();

        buttonResetPasswordClickListener();
    }

    private void buttonResetPasswordClickListener() {
        mBtnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditTextEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(forgetPasswordActivity.this, "Please enter your registered email address", Toast.LENGTH_SHORT).show();
                } else {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgetPasswordActivity.this, "Password reset email sent. Please check your email", Toast.LENGTH_LONG).show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        startActivity(new Intent(forgetPasswordActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                }, 2400);
                            } else {
                                Toast.makeText(forgetPasswordActivity.this, "Email address is not registered!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }
}