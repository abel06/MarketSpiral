package com.example.abela.marketspiral.Decode;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abela on 5/9/2017.
 */
public class Owner implements Parcelable{
    private  String name="";
    private  String phone="";
    private  String email="";
    private  String country="";
    private  String language="";


    public Owner(String owner){
        try {
            JSONObject jsonObject =new JSONObject(owner);
            this.name = jsonObject.getString("name");
            this.phone = jsonObject.getString("phone");
            this.email = jsonObject.getString("email");
            this.country = jsonObject.getString("country");
            this.language = jsonObject.getString("language");


        } catch (JSONException e) {

        }

    }

    protected Owner(Parcel in) {
        name = in.readString();
        phone = in.readString();
        email = in.readString();
        country = in.readString();
        language = in.readString();
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
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(country);
        dest.writeString(language);
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
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
}