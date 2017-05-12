package com.example.abela.marketspiral.Core;

/**
 * Created by Abela on 3/22/2017.
 */


public class Item {
    private int ID;
    private String title;
    private String date;
    private double price;
    private String thumbnail;
    private String description;

    private double lat;
    private double lng;
    private int position;


    public Item(int ID) {
        this.ID=ID;
    }

    public Item(int position, String title, double price, String thumbnail, String description, double lat, double lng, String date) {
       // this.name = name;
        this.position=position;
        this.title = title;
        this.date=date;
        this.price = price;
        this.thumbnail = thumbnail;
        this.description= description;
        this.lat= lat;
        this.lng = lng;


    }

    //public String getName() {
        //return name;
   // }

    //public void setName(String name) {
      //  this.name = name;
   // }

   // public int getNumOfSongs() {
        //return numOfSongs;
   // }

    //public void setNumOfSongs(int numOfSongs) {
    //    this.numOfSongs = numOfSongs;
   // }
    public void setPosition(int position){
       this.position=position;
    }
    public int getPosition(){
        return position;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title ;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setLat(double lat) {

        this.lat=lat;
    }
    public double getLat() {
        return lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLng() {
        return lng;
    }
    public void setDate(String date) {
        this.date= date;
    }
    public String getDate() {
        return date;
    }

}
