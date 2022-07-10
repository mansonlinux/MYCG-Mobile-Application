package com.example.hisyam.mycgmobileapps;

import android.view.View;

public class UserProfile {

    public String userEmail;
    public String userName;
    public String userShortBio;
    public String userpass;

    public UserProfile (){

    }


    public UserProfile(String userName, String userEmail , String userShortBio ,String userPass) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userShortBio = userShortBio;
        this.userpass = userPass;
    }

    //username
    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }


    //userEmail
    public String getUserEmail () {
        return userEmail;
    }

    public void setUserEmail (String userEmail) {
        this.userEmail = userEmail;
    }

    //usershortbio
    public String getUserShortBio () {
        return userShortBio;
    }

    public void setUserShortBio (String userShortBio) {
        this.userShortBio = userShortBio;
    }

    //userpass
    public String getUserpass () {
        return userpass;
    }

    public void setUserpass (String userpass) {
        this.userpass = userpass;
    }

}

