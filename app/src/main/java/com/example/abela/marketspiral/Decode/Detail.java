package com.example.abela.marketspiral.Decode;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Abela on 6/13/2017.
 */


public class Detail implements Parcelable {
    private  String id="";
    private  String title="";
    private  String date="";
    private  double price=0;
    private  String tag="";
    private  String description="";
    private  double lat=0;
    private  double lng=0;
    private  int buildup;
    private  int bedroom;

    public Detail(String owner){
        try {
            JSONObject jsonObject =new JSONObject(owner);
            this.id = jsonObject.getString("id");
            this.title = jsonObject.getString("title");
            this.date = jsonObject.getString("date");
            this.price = jsonObject.getDouble("price");
            this.description = jsonObject.getString("description");
            this.tag = jsonObject.getString("tag");
            this.lat=jsonObject.getDouble("lat");
            this.lng=jsonObject.getDouble("lng");
            this.buildup=jsonObject.getInt("buildup");
            this.bedroom=jsonObject.getInt("bedroom");

        } catch (JSONException e) {

        }

    }

    protected Detail(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        price = in.readDouble();
        description = in.readString();
        tag=in.readString();
        lat=in.readDouble();
        lng=in.readDouble();
        buildup=in.readInt();
        bedroom=in.readInt();

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
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeString(tag);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeInt(buildup);
        dest.writeInt(bedroom);
    }

    public String getid() {
        return id;
    }

    public String gettitle() {
        return title;
    }

    public Double getprice() {
        return price;
    }

    public String getdate() {
        return date;
    }

    public String getdescription() {
        return description;
    }

    public String getTag() {
        return tag;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getBuildup() {
        return buildup;
    }

    public int getBedroom() {
        return bedroom;
    }
}