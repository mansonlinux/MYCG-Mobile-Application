package com.example.hisyam.mycgmobileapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordRecover extends AppCompatActivity {


    private EditText EmailRecover;
    private Button resetpassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recover);
        hidenavigationBar();

        EmailRecover  = findViewById(R.id.input_EmailRecover);
        resetpassword = findViewById(R.id.resetpass_btn);
        mAuth = FirebaseAuth.getInstance();

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserEmail = EmailRecover.getText().toString().trim();

                if (UserEmail.equals("")){
                    Toast.makeText(PasswordRecover.this,"Please enter your registered Email ID",Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.sendPasswordResetEmail(UserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(PasswordRecover.this,"Password Reset Email sent!",Toast.LENGTH_SHORT).show();
                               finish();
                               startActivity(new Intent(PasswordRecover.this, Login2.class));
                           }else{
                               Toast.makeText(PasswordRecover.this,"Error in sending password reset email",Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                }

            }
        });


    }
    //hide nav & status bar .. autoclick to show it back.
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
