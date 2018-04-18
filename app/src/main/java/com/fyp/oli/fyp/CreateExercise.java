package com.fyp.oli.fyp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.util.UUID;

public class CreateExercise extends AppCompatActivity {
    private static final String TAG = "CreateExercise";

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private EditText inputTitle, inputDescr, inputSets, inputReps;
    private String title, descr, reps, sets, userId;
    private ImageButton setupImage;
    private Uri imageURI, mainImageURI = null;
    private Bitmap compressedImageFile;

    private boolean isChanged = true;

    private FloatingActionButton newExercisefab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();

        setupImage = findViewById(R.id.new_exercise_image);
        inputDescr = findViewById(R.id.new_exercise_body);
        inputTitle = findViewById(R.id.new_exercise_title);
        inputReps = findViewById(R.id.new_exercise_reps);
        inputSets = findViewById(R.id.new_exercise_sets);
        newExercisefab = findViewById(R.id.fab_add_new_exercise);

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExerciseImage();
            }
        });
        // Floating Action Button to register the user.
        newExercisefab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    addExercise();
                }
            }
            //progressBar.setVisibility(View.VISIBLE);
        });
    }

    private boolean validate() {
        boolean check = false;
        title = inputTitle.getText().toString().trim();
        descr= inputDescr.getText().toString().trim();
        reps= inputReps.getText().toString().trim();
        sets= inputSets.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            inputTitle.setError(getString(R.string.name_input));
            check = false;
        }

        else if (TextUtils.isEmpty(descr)) {
            inputDescr.setError(getString(R.string.prompt_email));
            check = false;
        }
        else if (TextUtils.isEmpty(reps)) {
            inputReps.setError(getString(R.string.age_input));
            check = false;
        }

        else if (TextUtils.isEmpty(sets)) {
            inputSets.setError(getString(R.string.prompt_password));
            check = false;

        }
        else{check = true;}

        return check;
    }

    /**
     * Was here
     */
    public void addExercise() {
        //setExerciseImage();
        /*if (mainImageURI != null) {


            if (isChanged) {

                userId = firebaseAuth.getCurrentUser().getUid();

                File newImageFile = new File(mainImageURI.getPath());
                try {

                    compressedImageFile = new Compressor(CreateExercise.this)
                            .setMaxHeight(125)
                            .setMaxWidth(125)
                            .setQuality(50)
                            .compressToBitmap(newImageFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] thumbData = baos.toByteArray();

                UploadTask image_path = storageReference.child("profile_images").child(user_id + ".jpg").putBytes(thumbData);

                image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                            storeFirestore(task, user_name);

                        } else {

                            String error = task.getException().getMessage();
                            Toast.makeText(SetupActivity.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();

                            setupProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });

            } else {

                storeFirestore(null, user_name);

            }
*/
        }


        /*Map<String, String> exercise = new HashMap<>();

        StorageReference imagePath = storageReference.child("exercises").child(title + ".jpg");
        imagePath.putFile(downloadURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    storePicture(task);
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(CreateExercise.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                }
            }
        });

        //User userClass = new User(name, email, age, password, heartRate, bloodPressure);
        exercise.put("title", title);
        exercise.put("descr", descr);
        exercise.put("sets", sets);
        exercise.put("reps", reps);
        exercise.put("imageurl", String.valueOf(downloadURI));

        firebaseFirestore.collection("exercises").document(title)
                .set(exercise)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Exercise Added To Firestore");
                        Intent intent = new Intent(CreateExercise.this, ExerciseActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error!");
                    }
                });*/
    //}

    private void storePicture(@NonNull Task<UploadTask.TaskSnapshot> task) {
        Uri download_uri;

        if(task != null) {

            download_uri = task.getResult().getDownloadUrl();

        } else {

            download_uri = mainImageURI;

        }

        Map<String, String> userMap = new HashMap<>();
        //userMap.put("name", user_name);
        userMap.put("image", download_uri.toString());

        firebaseFirestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){

                    Toast.makeText(CreateExercise.this, "The user Settings are updated.", Toast.LENGTH_LONG).show();
                    Intent mainIntent = new Intent(CreateExercise.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(CreateExercise.this, "(FIRESTORE Error) : " + error, Toast.LENGTH_LONG).show();

                }

                //setupProgress.setVisibility(View.INVISIBLE);

            }
        });
        /*if (task != null){
            downloadURI = task.getResult().getDownloadUrl();
        }else{
            downloadURI = imageURI;
        }*/
    }

    private void setExerciseImage() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(CreateExercise.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                /*Toast.makeText(CreateExercise.this, "Permission Denied", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(CreateExercise.this, new String[]{"READ_EXTERNAL_STORAGE"}, 1);*/
                BringImagePicker();

            } else {

                BringImagePicker();

            }
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {

            BringImagePicker();

        }

    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(CreateExercise.this);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                imageURI = result.getUri();
                setupImage.setImageURI(imageURI);
                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}