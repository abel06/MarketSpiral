package com.example.abela.marketspiral.Decode;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abela on 5/8/2017.    the DataDecoder Will call Property class
 * and Property will call Home and HOme will call Owner And Images class and Images Calss will call image class
 */

public class DataDecoder {
    String mResult="";
    private Villa vila;
    private Residential residential;
    private Commercial commercial;
    public DataDecoder(String result){
      this.mResult=result;

    }
   public boolean decode(){

       JSONObject jsonRootObject = null;
       try {
           jsonRootObject = new JSONObject(mResult);
       } catch (JSONException e) {
           e.printStackTrace();
       }
      if(jsonRootObject!=null) {

          JSONArray villasJArray = null;
          try {
              villasJArray = jsonRootObject.getJSONArray("villas");
              if (villasJArray != null) {
                  vila = new Villa();
                  vila.setVillas(villasJArray);

              }
          } catch (JSONException e) {

          }

          JSONArray residentialsJArray = null;
          try {
              residentialsJArray = jsonRootObject.getJSONArray("residentials");
              if (residentialsJArray != null) {
                  residential = new Residential();
                  residential.setResidentals(residentialsJArray);
                  Log.d("ab_log", " residential " + residential);

              }
          } catch (JSONException e) {
              Log.d("ab_log", "" + e);
          }


          JSONArray commercialsJArray = null;
          try {
              commercialsJArray = jsonRootObject.getJSONArray("commercials");
              if (commercialsJArray != null) {
                  commercial = new Commercial();
                  commercial.setCommercial(commercialsJArray);
              }

          } catch (JSONException e) {
              e.printStackTrace();
          }
        if(residentialsJArray==null&&commercialsJArray==null&&villasJArray==null){
            return false;
        }
        else return true;
      }

       return false;
   }


    public class Residential{
     private ArrayList<Home>homes =new ArrayList<>();
        public Residential(){
        }
      public void setResidentals(JSONArray jsonArray){
            for (int index=0;index<=jsonArray.length();index++){

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonArray.get(index).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null){
                        Property property=new Property();
                        property.setProperty(jsonObject);
                        Home home=new Home(index,property);
                        if(home.getImage().getImagesCount()>0){
                            homes.add(home);}
                    }
            }
        }
        public ArrayList<Home> getHomes(){
            return homes;
        }


    }
    public class Commercial{
        private ArrayList<Home>homes =new ArrayList<>();
        public Commercial(){


        }
        public void setCommercial(JSONArray jsonArray){
            ArrayList<JSONObject>jsonObjectList = new ArrayList<>();
            for (int index=0;index<=jsonArray.length();index++){

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonArray.get(index).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null){
                    Property property=new Property();
                    property.setProperty(jsonObject);
                    Home home=new Home(index,property);
                    if(home.getImage().getImagesCount()>0){
                        homes.add(home);}
                }

            }
        }
        public ArrayList<Home> getHomes(){
            return homes;
        }
    }
    public class Villa{
        private ArrayList<Home> homes =new ArrayList<>();

        public Villa(){

        }
        public void setVillas(JSONArray jsonArray){

            for (int index=0;index<jsonArray.length();index++){

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jsonArray.get(index).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(jsonObject!=null){
                    Property property=new Property();
                    property.setProperty(jsonObject);
                      Home home=new Home(index,property);
                     if(home.getImage().getImagesCount()>0){
                      homes.add(home);}
                }


            }
        }
        public ArrayList<Home> getHomes(){
            return homes;
        }
    }

    public Villa getVila(){
        return vila;
    }
    public Residential getResidential(){
        return residential;
    }
    public Commercial getCommercial(){
        return commercial;
    }

    public ArrayList<Home> getResidentialHomes(){
        if(residential!=null){
            return residential.getHomes();
        }
        Log.d("ab_log", " residential " + residential);
      return null;
    }
    public ArrayList<Home> getCommercialHomes(){
        if(commercial!=null){
            return commercial.getHomes();
        }
        return null;
    }
    public ArrayList<Home> getVillaHomes(){
        if(vila!=null){
            return vila.getHomes();
        }
        return null;
    }
    public ArrayList<Home> getAllHomes(){
        ArrayList<Home> homes=new ArrayList<>();
        if(residential!=null){
           for(int i=0;i<residential.getHomes().size();i++){
               homes.add(residential.getHomes().get(i));
           }
        }
        if(commercial!=null){
            for(int i=0;i<commercial.getHomes().size();i++){
                homes.add(commercial.getHomes().get(i));
            }
        }
        if(vila!=null){
            for(int i=0;i<vila.getHomes().size();i++){
                homes.add(vila.getHomes().get(i));
            }
        }
        return homes;
    }

}
