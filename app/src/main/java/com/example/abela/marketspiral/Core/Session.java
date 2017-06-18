package com.example.abela.marketspiral.Core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Abela on 6/12/2017.
 */

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setUsername(String usename) {
        prefs.edit().putString("username", usename).commit();
       // prefs.co
    }

    public String getUsername() {
        String user_name = prefs.getString("user_name","");
        return user_name;
    }
    public void setUserID(String userID) {
        prefs.edit().putString("user_id", userID).commit();
        // prefs.co
    }

    public String getUserID() {
        String userId = prefs.getString("user_id","");
        return userId;
    }
    public void setExternalId(String externalId) {
        prefs.edit().putString("external_id", externalId).commit();
        // prefs.co
    }

    public String getExternalId() {
        String external_id = prefs.getString("external_id","");
        return external_id;
    }
    public void setType(String type) {
        prefs.edit().putString("type", type).commit();
        // prefs.co
    }

    public String getType() {
        String type = prefs.getString("type","");
        return type;
    }
    public void setLanguage(String usename) {
        prefs.edit().putString("username", usename).commit();
        // prefs.co
    }

    public String getLanguage() {
        String usename = prefs.getString("username","");
        return usename;
    } public void setPhone(String usename) {
        prefs.edit().putString("username", usename).commit();
        // prefs.co
    }

    public String getPhone() {
        String usename = prefs.getString("username","");
        return usename;
    }
    public void setProfile(String usename) {
        prefs.edit().putString("username", usename).commit();
        // prefs.co
    }

    public String getProfile() {
        String usename = prefs.getString("username","");
        return usename;
    }
    public void clearSession(){
        prefs.edit().clear().commit();
    }

}