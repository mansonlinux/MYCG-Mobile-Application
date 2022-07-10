package com.example.hisyam.mycgmobileapps;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private android.support.v7.widget.Toolbar mainToolbar;
    //Firebase Authentication
    private FirebaseAuth mAuthl;

    //declaration
    private CardView gameCard;
    private CardView socialCard;
    private CardView eventsCard;
    private CardView profileCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hidenavigationBar();

        mAuthl = FirebaseAuth.getInstance();

        mainToolbar = findViewById(R.id.main_toolbar);

        //define class CardView
        gameCard = findViewById(R.id.gameCV);
        socialCard = findViewById(R.id.socialCV);
        eventsCard = findViewById(R.id.eventsCV);
        profileCard = findViewById(R.id.profileCV);

        //Add Click Listener to the cards
        gameCard.setOnClickListener(this);
        socialCard.setOnClickListener(this);
        eventsCard.setOnClickListener(this);
        profileCard.setOnClickListener(this);


        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Main Activity");
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


    //current user Auth
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null){

            sendToLogin();

        }

    }


    //menu inflater
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        //getColor(R.menu.main_menu,menu);
        //hidenavigationBar();
        return true;


    }

    //selected logout on top-left
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i;
        switch (item.getItemId()) {

            case R.id.action_logout_btn:
                logOut();
                finish();
                return true;
            case R.id.action_rules_btn:
                i = new Intent(this, RulesActivity.class);
                startActivity(i);
                return true;

            case R.id.action_about_btn:
                i = new Intent(this, About_Activity.class);
                startActivity(i);
                return true;

            default:
                return false;

        }



    }


    //logout Activity / function
    private void logOut() {

        mAuthl.signOut();
        sendToLogin();

    }

    //connected with logOut
    private void sendToLogin() {
        Intent loginIntent = new Intent(this,Login2.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onClick(View v) {

        Intent i;

        switch (v.getId()) {
            case R.id.gameCV: i = new Intent(this,GameActivity.class);
            startActivity(i);
            break;
            case R.id.socialCV: i = new Intent(this,GalleryGo.class);
            startActivity(i);
            break;
            case R.id.eventsCV: i = new Intent(this,EventsActivity.class);
            startActivity(i);
            break;
            case R.id.profileCV: i = new Intent(this,ProfileActivity.class);
            startActivity(i);
            break;

                default:break;


        }
    }

    //yes or close option for back button on this activity.
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("You're pushing back button..stay or logout?");
        builder.setCancelable(true);
        builder.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogInterface.OnCancelListener.class.getClasses();
            }
        });
        builder.setPositiveButton("Logout!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logOut();
                Toast.makeText(MainActivity.this,"Session has been terminated",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}
