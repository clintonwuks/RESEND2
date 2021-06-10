package com.example.resend;

import android.util.Log;

public class FriendItem {
    private static final String TAG = "APP_TEST";
    private String fullName, user_name, acr="";

        FriendItem(String fullName){
             this.fullName = fullName;
             user_name = fullName.replaceAll(" ", "");
             user_name = user_name.toLowerCase();
             setUserAcronym();
         }

    private void setUserAcronym() {
        String [] SepName = fullName.split(" ");
        char ch1, ch2;
        String sh1, sh2;
        ch1 = SepName[0].charAt(0);
        ch2 = SepName[1].charAt(0);

        sh1 = String.valueOf(ch1);
        sh2 = String.valueOf(ch2);
        acr = sh1.concat(sh2);
        Log.d(TAG, "acronym " + acr);
    }

    public String getFullName() { Log.v(TAG, "full name " + fullName); return fullName; }
    public String getUserName() { Log.v(TAG, "username " + user_name); return user_name; }
    public String getAcr() { Log.v(TAG, "Acronym " + acr); return acr; }




}

