package com.fyp.oli.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity{

    private static final String TAG = "LoginActivity.class";
    /**
     * Add Firebase Authentication
     */
    private FirebaseAuth auth;

    // UI references.
    private AutoCompleteTextView inputEmail;
    private Button signInButton, registerButton, resetButton;
    private ProgressBar progressBar;
    private EditText inputPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        auth = FirebaseAuth.getInstance();

        /**
         * Checking the user Session
         */

        if(auth.getCurrentUser()!= null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        signInButton = findViewById(R.id.login_button);
        registerButton = (Button) findViewById(R.id.register_button);
        resetButton = (Button) findViewById(R.id.reset_button);
        inputEmail = (AutoCompleteTextView) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                //Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                //startActivity(intent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signIn();
            }

            private void signIn() {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError(getString(R.string.error_invalid_email));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError(getString(R.string.error_incorrect_password));
                    return;
                }

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.error_incorrect_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.error_invalid_password), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    private void resetPassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String emailAddress = inputEmail.getText().toString();

        auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Email sent.");
                }
            }
        });
    }


}
