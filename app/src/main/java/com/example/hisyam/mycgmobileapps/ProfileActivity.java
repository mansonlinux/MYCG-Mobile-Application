package com.example.hisyam.mycgmobileapps;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfileActivity extends AppCompatActivity {

    private ImageView displayprofilepic;
    private TextView displayprofileUserName;
    private TextView displayprofileEmail;
    private TextView displayprofileBio;
    private TextView displayuserpass;
    private Button EditUserbtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        hidenavigationBar();

        //Set variable object from id
        EditUserbtn = findViewById(R.id.EditUserbtn);
        displayprofilepic = findViewById(R.id.DisplayUserProfilePic);
        displayprofileUserName = findViewById(R.id.DisplayUsername);
        displayprofileEmail = findViewById(R.id.DisplayUserEmail);
        displayprofileBio = findViewById(R.id.DisplayShortBio);
        displayuserpass = findViewById(R.id.DisplayUserpass);

        //Firebase set variable object
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //retrieve information from database to display
        final DatabaseReference databaseReference = firebaseDatabase.getReference(mAuth.getUid());
        StorageReference storageReference = firebaseStorage.getReference(); //this for root folder from database
        storageReference.child(mAuth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(displayprofilepic);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                displayprofileUserName.setText(userProfile.getUserName());
                displayprofileEmail.setText(userProfile.getUserEmail());
                displayprofileBio.setText(userProfile.getUserShortBio());
                displayuserpass.setText(userProfile.getUserpass());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_LONG).show();
            }
        });

        EditUserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,ProfileEditActivity.class));
                finish();
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
