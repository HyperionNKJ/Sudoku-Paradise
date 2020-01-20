package com.example.user.sudokuparadise;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mBtnLogin;
    private Button mBtnSignup;
    private Button mBtnForgetPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmail = findViewById(R.id.editTextEmail_login);
        mEditTextPassword = findViewById(R.id.editTextPassword_login);
        mBtnLogin = findViewById(R.id.buttonLogin_login);
        mBtnSignup = findViewById(R.id.buttonSignup_login);
        mBtnForgetPassword = findViewById(R.id.buttonForgetPassword_login);
        auth = FirebaseAuth.getInstance();

        buttonSignupClickListener();
        buttonLoginClickListener();
        buttonForgetPasswordClickListener();
    }

    private void buttonSignupClickListener() {
        mBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void buttonLoginClickListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditTextEmail.getText().toString().trim();
                String password = mEditTextPassword.getText().toString().trim();
                // Check if email is empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if password is empty
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // error occurred
                                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Welcome to Sudoku Paradise!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void buttonForgetPasswordClickListener() {
        mBtnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, forgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
