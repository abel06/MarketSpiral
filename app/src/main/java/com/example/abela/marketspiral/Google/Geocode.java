package com.example.abela.marketspiral.Google;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Abela on 4/28/2017.
 */


public class Geocode {
    private double lat;
    private double lng;
    String address;
    String city;
    String state;


    Geocoder geocoder;
    List<Address> addresses;

    public Geocode(double lat , double lng, Context context){
        this.lat=lat;
        this.lng=lng;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
    }
    public String getAdress(){
        if(address!=null){
            return address;}
        else return "";
    }
    public String getCity(){
        if(city!=null){
            return city;}
        else return "";
    }
    public String getState(){
        if(state!=null){
            return state;}
        else return "";
    }
    public String getCountry(){
        if(addresses.get(0)!=null){
            return addresses.get(0).getCountryName();}
        else return "";
    }
    public double getLat(){
        return addresses.get(0).getLatitude();
    }
    public double getLng(){
        return addresses.get(0).getLongitude();
    }
}