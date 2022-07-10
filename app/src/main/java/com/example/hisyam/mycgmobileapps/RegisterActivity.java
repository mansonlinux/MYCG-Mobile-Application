package com.example.hisyam.mycgmobileapps;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmail;
    private Button signupbtn;
    private ProgressBar registerProgressbar;
    private EditText registerpass;
    private CheckBox PassShow;
    private EditText registerpassconfirm;
    private EditText registerUsername;
    private ImageView RegisterUserProfilePic;
    private FirebaseAuth mAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private static int PICK_IMAGE = 123;
    String username,email,pass,confirm_pass,usershortbio;
    Uri imagePath; //Uniform Resource Identifier (URI) is a string of characters used to identify a resource


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //once if result was functional or not (to get data) and it will parse to the Uri
        //and it will converted to a bitmap format which can be process by the Image View
        //try catch will thrown an exception if the images are not supported.
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                RegisterUserProfilePic.setImageBitmap(bitmap); //set and parse parameter (bitmap)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        hidenavigationBar();

        registerpass = findViewById(R.id.Register_Pass);
        PassShow = findViewById(R.id.ShowPass);
        registerpassconfirm = findViewById(R.id.Register_PassConfirm);

        //show password and hide from checkbox.
        PassShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PassShow.isChecked()){
                    registerpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    registerpassconfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                } else {
                    registerpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    registerpassconfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();

        registerUsername = findViewById(R.id.Register_Username);
        registerEmail = findViewById(R.id.Register_email);
        registerpass = findViewById(R.id.Register_Pass);
        registerpassconfirm = findViewById(R.id.Register_PassConfirm);
        registerProgressbar = findViewById(R.id.Register_progress);
        signupbtn = findViewById(R.id.Signup_btn);
        RegisterUserProfilePic = findViewById(R.id.Register_profilepic);

        //if user click on image profile a process will run and pick image process
        RegisterUserProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*"); //application/* for doc //audio/* for audio
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });




        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 username = registerUsername.getText().toString();
                 email = registerEmail.getText().toString();
                 pass = registerpass.getText().toString();
                 confirm_pass = registerpassconfirm.getText().toString();


                if( TextUtils.isEmpty(registerUsername.getText()) ||TextUtils.isEmpty(registerEmail.getText()) || TextUtils.isEmpty(registerpass.getText()) || TextUtils.isEmpty(registerpassconfirm.getText())){
                     Toast.makeText(RegisterActivity.this,"Please don't leave empty field",Toast.LENGTH_LONG).show();

                }else {


                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) & !TextUtils.isEmpty(confirm_pass)) {

                        if (pass.equals(confirm_pass)) {

                            registerProgressbar.setVisibility(View.VISIBLE);

                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        sendEmailVerification();
                                        sendUserData();
                                        //Toast.makeText(RegisterActivity.this,"Successfully registered, upload completed!",Toast.LENGTH_SHORT).show();
                                        sendToMain();
                                        finish();
                                    } else {

                                        String errormessage = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, "Error = " + errormessage, Toast.LENGTH_LONG).show();

                                    }


                                    registerProgressbar.setVisibility(View.INVISIBLE);

                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "Confirm Password and Password field doesn't match", Toast.LENGTH_LONG).show();
                        }


                    }

                }




            }
        });



    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser !=null){

            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegisterActivity.this,"Successfully registered, verification mail has been sent!",Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        sendToMain();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,"Verification Mail Has'nt been sent!",Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myref = firebaseDatabase.getReference(mAuth.getUid());
        StorageReference imageReference = storageReference.child(mAuth.getUid()).child("Images").child("Profile Pic"); //user Id/Images/profile_pic.png or whatever format
        UploadTask uploadTask = imageReference.putFile(imagePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(RegisterActivity.this, "Upload Successful!", Toast.LENGTH_LONG).show();
            }
        });
        UserProfile userProfile = new UserProfile(username,email,usershortbio,pass);
        myref.setValue(userProfile);

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

    @Override
    protected void onStart() {
        super.onStart();

       FirebaseUser currentUser = mAuth.getCurrentUser();
       if(currentUser !=null){

           sendToMain();

       }
    }

    private void sendToMain() {

        Intent mainIntent = new Intent(this,Login2.class);
        startActivity(mainIntent);
        finish();

    }
}
