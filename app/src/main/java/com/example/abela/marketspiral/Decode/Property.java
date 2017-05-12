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
    private  String title="";
    private  String added="";
    private  double price;
    private Owner owner;
    private double lat;
    private double lng;
    private String address;
    private  String description="";
    private int bedroom=0;
    private String buildup_area="";

    public void setProperty(JSONObject property ){
        try {
            this.images= new Images(property.getString("images"));
            this.title=property.getString("title");
            this.added=property.getString("added");
            this.price=Double.parseDouble(property.getString("price"));
            this.owner=new Owner(property.getString("owner"));
            this.lat=Double.parseDouble(property.getString("lat"));
            this.lng=Double.parseDouble(property.getString("lng"));
            this.address=property.getString("address");
            this.description=property.getString("description");
            this.bedroom=property.getInt("bedroom");
            this.buildup_area=property.getString("buildup");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Owner getPropertyOwner() {
        return owner;
    }

    public Images getPropertyImages() {

        return images;
    }

    public String getPropertyTitle() {
        return title;
    }

    public String getPropertyAdded() {
        return added;
    }

    public double getPropertyPrice() {
        return price;
    }

    public double getPropertyLat() {
        return lat;
    }

    public double getPropertyLng() {
        return lng;
    }
    public String getPropertyAddress(){
        return address;
    }

    public String getPropertyDescription() {
        return description;
    }

    public int getPropertyBedroom() {
        return bedroom;
    }

    public String getPropertyBuildup_area() {
        return buildup_area;
    }
}