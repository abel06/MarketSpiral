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
    private String added="";
    private double price;
    private Images images;
    private String description="";
    private double lat;
    private double lng;
    private String address="";
    private int position;
    private int bedroom=0;
    private String buildup_area="";


   // intiate instance of the class and load variables
    public Home(int index, Property property){
        this.owner=property.getPropertyOwner();
        this.images=property.getPropertyImages();
        this.index=index;
        this.title=property.getPropertyTitle();
        this.added=property.getPropertyAdded();
        this.description=property.getPropertyDescription();
        this.lat=property.getPropertyLat();
        this.lng=property.getPropertyLng();
        this.address=property.getPropertyAddress();
        this.price=property.getPropertyPrice();
        this.bedroom=property.getPropertyBedroom();
        this.buildup_area=property.getPropertyBuildup_area();

    }

    // read parceble data
    protected Home(Parcel in) {

        owner= (Owner) in.readParcelable(getClass().getClassLoader());
        images = (Images) in.readParcelable(getClass().getClassLoader());
        index = in.readInt();
        title = in.readString();
        added = in.readString();
        price = in.readDouble();
        description = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        address=in.readString();
        position = in.readInt();
        bedroom=in.readInt();
        buildup_area=in.readString();

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
        dest.writeString(added);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(address);
        dest.writeInt(position);
        dest.writeInt(bedroom);
        dest.writeString(buildup_area);


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
    public String getAdded() {
        return added;
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

    public String getAddress() {
        return address;
    }

    public String getBuildup_area() {
        return buildup_area;
    }

    public int getBedroom() {
        return bedroom;
    }
}