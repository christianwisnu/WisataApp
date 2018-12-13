package com.example.project.wisataapp.utilities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by christian on 09/02/18.
 */

public class PrefUtil {

    private Activity activity;
    public static final String ID = "ID";
    public static final String PROFILE = "PICTURE";
    public static final String NAME = "NAME";
    public static final String GENDER = "GENDER";
    public static final String TELP = "TELP";
    public static final String LOGIN = "LOGIN";

    // Constructor
    public PrefUtil(Activity activity) {
        this.activity = activity;
    }

    public void clear() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply(); // This line is IMPORTANT !!!
    }

    public void saveUserInfo(String name, String id, String gender, String foto,
                                  String login, String telp){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NAME, name);
        editor.putString(GENDER, gender);
        editor.putString(PROFILE, foto);
        editor.putString(TELP, telp);
        editor.putString(ID, id);
        editor.putString(LOGIN, login);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public SharedPreferences getUserInfo(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        //Log.d("MyApp", "Name : "+prefs.getString("fb_name",null)+"\nEmail : "+prefs.getString("fb_email",null));
        return prefs;
    }
}
