package com.example.abela.marketspiral.Decode;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abela on 5/9/2017.
 */

public  class Property{
    private Images  images;
    private Owner owner;
    private Detail detail;

    public void setProperty(JSONObject property ){
        try {
            this.images= new Images(property.getString("images"));

            this.owner=new Owner(property.getString("owner"));
            this.detail=new Detail(property.getString("0"));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("ab_log","owner "+e);
        }
    }

    public Owner getPropertyOwner() {
        return owner;
    }

    public Images getPropertyImages() {

        return images;
    }

    public Detail getDetail() {
        return detail;
    }


}