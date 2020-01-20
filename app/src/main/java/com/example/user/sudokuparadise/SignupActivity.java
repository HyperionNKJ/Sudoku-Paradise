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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    private EditText mEditTextEmail;
    private EditText mEditTextPw;
    private EditText mEditTextPwConfirm;
    private Button mBtnSignup;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEditTextEmail = findViewById(R.id.editTextEmail_signup);
        mEditTextPw = findViewById(R.id.editTextPassword_signup);
        mEditTextPwConfirm = findViewById(R.id.editTextPasswordConfirm_signup);
        mBtnSignup = findViewById(R.id.buttonSignup_signup);
        auth = FirebaseAuth.getInstance();

        buttonSignupClickListener();
    }

    private void buttonSignupClickListener() {
        mBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEditTextEmail.getText().toString().trim();
                String password = mEditTextPw.getText().toString().trim();
                String passwordConfirm = mEditTextPwConfirm.getText().toString().trim();
                // Check if email is empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if password is empty
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Please choose your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if password confirmation is empty
                if (TextUtils.isEmpty(passwordConfirm)) {
                    Toast.makeText(SignupActivity.this, "Please re-enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if password contains at least 6 characters
                if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password must have at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                //check if password is alphanumeric
                if(!password.matches("^(?=.*[0-9])(?=.*[a-z])[a-zA-Z0-9]+$")){
                    Toast.makeText(SignupActivity.this, "Password must contain at least one number and small letter", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if password and password confirmation tally
                if (!password.equals(passwordConfirm)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new user
                auth.createUserWithEmailAndPassword(email, password).
                        addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Sign-up failed. Please check your email and ensure it has never been registered", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SignupActivity.this, "Sign-up success! You may now log in", Toast.LENGTH_SHORT).show();
                                    // Need to wait 2 seconds for toast to show. Hence the code below
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() { // actions to do after delay
                                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    }, 2400);
                                }
                            }
                        });
            }
        });
    }
}
