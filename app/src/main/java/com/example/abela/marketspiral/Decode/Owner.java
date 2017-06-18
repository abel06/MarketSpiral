package com.example.abela.marketspiral.Decode;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Abela on 5/9/2017.
 */
public class Owner implements Parcelable{
    private  String name="";
    private List<String> phone=new ArrayList<>();
    private  String email="";
    private  String country="";
    private  String language="";
    private String phoneAsString="";


    public Owner(String owner){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(owner);
        } catch (JSONException e) {
            e.printStackTrace(); }
        try {
            this.name = jsonObject.getString("full_name");
        } catch (JSONException e) {
            }
        try {
            this.phone=split(jsonObject.getString("phone"));
            this.phoneAsString=jsonObject.getString("phone");

        } catch (JSONException e) {

        }
        try {
            this.email = jsonObject.getString("email");
        } catch (JSONException e) {

        }  try {
            this.country = jsonObject.getString("country");
        } catch (JSONException e) {

        }  try {
            this.language = jsonObject.getString("languages");
        } catch (JSONException e) {

        }

    }
  private List<String> split(String string){
      List<String> tmp=new ArrayList<>();
      String[] parts=string.split("/,");
      for(int i=0;i<parts.length;i++){
          tmp.add(parts[i]);
      }

      return tmp;
  }
    protected Owner(Parcel in) {
        name = in.readString();
        in.readStringList(phone);
        email = in.readString();
        country = in.readString();
        language = in.readString();
        phoneAsString=in.readString();
    }

    public static final Creator<Owner> CREATOR = new Creator<Owner>() {
        @Override
        public Owner createFromParcel(Parcel in) {
            return new Owner(in);
        }

        @Override
        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeStringList(phone);
        dest.writeString(email);
        dest.writeString(country);
        dest.writeString(language);
        dest.writeString(phoneAsString);
    }

    public String getName() {
        return name;
    }

    public List<String> getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getLanguage() {
        return language;
    }

    public String getPhoneAsString() {
        return phoneAsString;
    }
}