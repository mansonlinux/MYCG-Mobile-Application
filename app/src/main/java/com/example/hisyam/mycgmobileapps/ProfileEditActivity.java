package com.example.hisyam.mycgmobileapps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.IOException;

public class ProfileEditActivity extends AppCompatActivity {

    private TextView Editprofile;
    private TextView EditNewuserName;
    private TextView EditNewuserEmail;
    private TextView EditNewuserShortBio;
    private TextView EditNewuserPass;
    private Button goBack;
    private ProgressBar progressbar;
    private ImageView EditNewuserprofilepic;
    //Firebase authentication & database
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private static int PICK_IMAGE = 123;
    Uri imagePath; //Uniform Resource Identifier (URI) is a string of characters used to identify a resource


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //once if result was functional or not (to get data) and it will parse to the Uri
        //and it will converted to a bitmap format which can be process by the Image View
        //try catch will thrown an exception if the images are not supported.
         if(requestCode == PICK_IMAGE  && resultCode == RESULT_OK && data.getData() != null) {
             imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                EditNewuserprofilepic.setImageBitmap(bitmap); //set and parse parameter (bitmap)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        hidenavigationBar();

        //declare variable to unit ID
        Editprofile = findViewById(R.id.EditProfileBtn);
        goBack = findViewById(R.id.Backbtn);
        EditNewuserName = findViewById(R.id.EditUsername);
        EditNewuserEmail = findViewById(R.id.EditUserEmail);
        EditNewuserShortBio = findViewById(R.id.EditUserShortBio);
        EditNewuserPass = findViewById(R.id.EditUserPass);
        EditNewuserprofilepic = findViewById(R.id.EditProfilePic);
        progressbar = findViewById(R.id.progressBar);

        //back button to profile activity
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileEditActivity.this,ProfileActivity.class));
                finish();
            }
        });

        EditNewuserprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if(ContextCompat.checkSelfPermission(ProfileEditActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(ProfileEditActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(ProfileEditActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                    }else {
                        Toast.makeText(ProfileEditActivity.this, "You already have Permission", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        intent.setType("image/*"); //application/* for doc //audio/* for audio
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
                    }
                }
            }
        });

        //Firebase auth & database retrieve
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                EditNewuserName.setText(userProfile.getUserName());
                EditNewuserEmail.setText(userProfile.getUserEmail());
                EditNewuserShortBio.setText(userProfile.getUserShortBio());
                EditNewuserPass.setText(userProfile.getUserpass());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileEditActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        //retrieve information from database to display
        final StorageReference storageReference = firebaseStorage.getReference(); //this for root folder from database
        storageReference.child(mAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(EditNewuserprofilepic);
            }
        });

        Editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Email & password must be filled .. any user leave it empty will occur an error
                if(TextUtils.isEmpty(EditNewuserName.getText()) || TextUtils.isEmpty(EditNewuserPass.getText())) {
                    Toast.makeText(ProfileEditActivity.this, "Please don't leave empty field ", Toast.LENGTH_LONG).show();

                }else{

                    progressbar.setVisibility(View.VISIBLE);

                    String name = EditNewuserName.getText().toString();
                    String email = EditNewuserEmail.getText().toString();
                    String shortbio = EditNewuserShortBio.getText().toString();
                    String pass = EditNewuserPass.getText().toString();

                    //if successful data will insert based on string below Userprofile (string)
                    UserProfile userProfile = new UserProfile(name, email, shortbio, pass);
                    databaseReference.setValue(userProfile);

                    //Uploading process of imagesView
                    StorageReference imageReference = storageReference.child(mAuth.getUid()).child("Images").child("Profile Pic"); //user Id/Images/profile_pic.png or whatever format
                    UploadTask uploadTask = imageReference.putFile(imagePath);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProfileEditActivity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ProfileEditActivity.this, "Upload Successful!", Toast.LENGTH_LONG).show();
                        }
                    });

                    //firebase authentication user and if user exist on current session
                    //it will parse update password process based on if else statement
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileEditActivity.this, "Profile Updated", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            progressbar.setVisibility(View.INVISIBLE);
                        }
                    });

                    finish();
                }

            }
        });


    }

    private void hidenavigationBar() {
        this.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }
}
