package com.example.abela.marketspiral.Decode;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Abela on 5/9/2017.
 */

public class Home implements Parcelable{
    private Owner owner;
    private int index;
    private String title="";
    private String date="";
    private double price;
    private Images images;
    private String description="";
    private double lat;
    private double lng;
    private int position;
    private int bedroom=0;
    private int buildup_area=0;
    private String id="";


   // intiate instance of the class and load variables
    public Home(int index, Property property){
        this.owner=property.getPropertyOwner();
        this.images=property.getPropertyImages();
        this.index=index;
        this.title=property.getDetail().gettitle();
        this.date=property.getDetail().getdate();
        this.description=property.getDetail().getdescription();
        this.lat=property.getDetail().getLat();
        this.lng=property.getDetail().getLng();
        this.price=property.getDetail().getprice();
        this.bedroom=property.getDetail().getBedroom();
        this.buildup_area=property.getDetail().getBuildup();
        this.id=property.getDetail().getid();

    }

    // read parceble data
    protected Home(Parcel in) {

        owner= (Owner) in.readParcelable(getClass().getClassLoader());
        images = (Images) in.readParcelable(getClass().getClassLoader());
        index = in.readInt();
        title = in.readString();
        date = in.readString();
        price = in.readDouble();
        description = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        position = in.readInt();
        bedroom=in.readInt();
        buildup_area=in.readInt();
        id=in.readString();

    }

    public static final Creator<Home> CREATOR = new Creator<Home>() {
        @Override
        public Home createFromParcel(Parcel in) {
            return new Home(in);
        }

        @Override
        public Home[] newArray(int size) {
            return new Home[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    // write parceble data
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(owner,flags);
        dest.writeParcelable(images,flags);
        dest.writeInt(index);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeDouble(lat);
        dest.writeDouble(lng);;
        dest.writeInt(position);
        dest.writeInt(bedroom);
        dest.writeInt(buildup_area);
        dest.writeString(id);


    }

    public Images getImage() {
        return images;
    }
    public Owner getOwner() {
        return owner;
    }
    public int getIndex() {
        return index;
    }
    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }


    public int getBuildup_area() {
        return buildup_area;
    }

    public int getBedroom() {
        return bedroom;
    }

    public String getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }
}