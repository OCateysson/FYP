package com.fyp.oli.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    /**
     * Add Firebase Authentication
     */
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    /**
     *  Firestore Database
     */
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    // UI references.
    private EditText inputName;
    private EditText inputEmail;
    private FloatingActionButton registerButton;
    private EditText inputPassword, inputAge, inputConfirmPassword;

    private String name, email, password, age, cpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        // Database authentication
        //mDatabase = FirebaseDatabase.getInstance().getReference();

        registerButton = findViewById(R.id.fab_register);
        inputName = findViewById(R.id.name_text);
        inputEmail = findViewById(R.id.email_text);
        inputPassword = findViewById(R.id.password_text);
        inputAge = findViewById(R.id.age_text);
        inputConfirmPassword = findViewById(R.id.password_confimation);

        // Floating Action Button to register the user.
        registerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    createUser();
                }
            }
            //progressBar.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Method to create the user into the authentication
     * system of Firebase.
     */
    private void createUser() {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            addUserToDatabase();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                    }
                });

    }

    /**
     *  Method to check if all the fields are correct
     *  then if boolean is true then it creates the user
     * @return boolean
     */
    private boolean validate() {
        boolean check = false;
        name = inputName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        age = inputAge.getText().toString().trim();
        cpassword = inputConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            inputName.setError(getString(R.string.name_input));
            check = false;
        }

        else if (TextUtils.isEmpty(email)) {
            inputEmail.setError(getString(R.string.prompt_email));
            check = false;
        }
        else if (TextUtils.isEmpty(age)) {
            inputAge.setError(getString(R.string.age_input));
            check = false;
        }

        else if (TextUtils.isEmpty(password)) {
            inputPassword.setError(getString(R.string.prompt_password));
            check = false;

        }
        else if (password.length() < 6) {
            inputPassword.setError("Password is too short!");
            check = false;
        }
        else if (TextUtils.isEmpty(cpassword)) {
            inputConfirmPassword.setError("Enter confirmation Password");
            check = false;
        }

        else if(!password.equals(cpassword)){
            check = false;
        }
        else{check = true;}

        return check;
    }
    /**
     * Method to add User to the database using credentials entered
     * by the user.
     */
    private void addUserToDatabase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


        if (currentUser != null) {
            StorageReference filePath = storageReference.child("users");
            Map<String, String> user = new HashMap<>();
            String uid = currentUser.getUid();
            String heartRate = "--";
            String bloodPressure = "--";

            //User userClass = new User(name, email, age, password, heartRate, bloodPressure);
            user.put("name", name);
            user.put("email", email);
            user.put("age", age);
            user.put("bloodPressure", bloodPressure);
            user.put("heartRate", heartRate);

            firebaseFirestore.collection("users").document(uid)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User Added to Firestore");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error!");
                        }
                    });

        } else {        }
    }

    /**
     * Method to add User to the database using credentials entered
     * by the user.
     */
   /* private void addUserToDatabase() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String heartRate = "--";
            String bloodPressure = "--";

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

            User user = new User(name, email, age, password, heartRate, bloodPressure);
            mDatabase.child(uid).setValue(user);
        } else {        }
    }*/
}
