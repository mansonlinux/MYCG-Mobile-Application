package com.example.hisyam.mycgmobileapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login2 extends AppCompatActivity {


    //show password checkbox + password inputbox + email inputbox
    private EditText Emailinput;
    private EditText Passinput;
    private CheckBox ShowPass;
    private Button loginBtn;
    private ProgressBar loginProgress;
    private TextView forgotpassword;
    private TextView signuptext;
    //Firebase declaration
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        hidenavigationBar();

        mAuth = FirebaseAuth.getInstance();

        //Declaration + functionality of checkbox + password inputbox
        Passinput = findViewById(R.id.Login_Pass);
        ShowPass = findViewById(R.id.ShowPass);
        forgotpassword = findViewById(R.id.forgot_pass);
        signuptext = findViewById(R.id.tvsignup);

        ShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ShowPass.isChecked()) {
                    Passinput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    Passinput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //forgot textView password recovery
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login2.this, PasswordRecover.class));
            }
        });

        //sign up page
        signuptext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login2.this, RegisterActivity.class));
            }
        });


        //Declaration of Login variable
        Emailinput = (EditText) findViewById(R.id.Login_email);
        Passinput = (EditText) findViewById(R.id.Login_Pass);
        loginBtn = (Button) findViewById(R.id.Login_btn);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                //Login PROCESS
                String LoginEmail = Emailinput.getText().toString();
                String LoginPass = Passinput.getText().toString();

                //Email & password must be filled .. any user leave it empty will occur an error
                if(TextUtils.isEmpty(Emailinput.getText()) || TextUtils.isEmpty(Passinput.getText())){
                    Toast.makeText(Login2.this,"Please don't leave empty field ",Toast.LENGTH_LONG).show();

                 if(TextUtils.isEmpty(Emailinput.getText()) && TextUtils.isEmpty(Passinput.getText())){
                     Toast.makeText(Login2.this,"Email & Password must be filled",Toast.LENGTH_LONG).show();
                 }
                }else {

                    if (!TextUtils.isEmpty(LoginEmail) && !TextUtils.isEmpty(LoginPass)) {
                        loginProgress.setVisibility(View.VISIBLE);

                        mAuth.signInWithEmailAndPassword(LoginEmail, LoginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    CheckEmailVerification();
                                    finish();
                                    sendtoMain();

                                } else {

                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(Login2.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                }

                                loginProgress.setVisibility(View.INVISIBLE);
                            }

                        });

                    }
                }

            }


        });


    }



    private void CheckEmailVerification(){
        //FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Boolean emailflag = currentUser.isEmailVerified();

        if(emailflag){
            finish();
            sendtoMain();
        }else {
            Toast.makeText(Login2.this,"please verify your email",Toast.LENGTH_LONG).show();
            mAuth.signOut();
            finish();
        }


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

        //get current user login
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){

            sendtoMain();

        }

    }

    private void sendtoMain() {

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}

//Fully Coded by Hisyam(2018)