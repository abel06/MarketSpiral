package com.example.abela.marketspiral.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abela on 6/11/2017.
 */

public class User {
    private String response;
    private String user_id="";
    private String external_id="";
    private String type="";
    private String full_name="";
    private String language="";
    private String phone="";
    private String profile="";

    public User(String response){
        Log.d("ab_log","token class");
        this.response=response;

    }
    public User(){

    }
    public void execute(){
        JSONObject jsonObject = null;
        try {
            jsonObject =new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            this.user_id=jsonObject.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }try {
            this. external_id=jsonObject.getString("external_id");
           } catch (JSONException e) {
        e.printStackTrace();
          }try {
            this.type=jsonObject.getString("type");
         } catch (JSONException e) {
        e.printStackTrace();
        }try {
            this.full_name=jsonObject.getString("full_name");
        } catch (JSONException e) {
        e.printStackTrace();
        }try {
            this. language=jsonObject.getString("languages");
        } catch (JSONException e) {
        e.printStackTrace();
        }try {
            this. phone=jsonObject.getString("phone");
        } catch (JSONException e) {
        e.printStackTrace();
        }try {
            this. profile=jsonObject.getString("profile");
        } catch (JSONException e) {
        e.printStackTrace();
        }



    }

    public String getUser_id() {
        return user_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfile() {
        return profile;
    }
}
