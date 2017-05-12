package com.example.abela.marketspiral.Decode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abela on 5/8/2017.    the DataDecoder Will call Property class
 * and Property will call Home and HOme will call Owner And Images class and Images Calss will call image class
 */

public class DataDecoder {
    String mResult;
    private Villa vila;
    private Residential residential;
    private Commercial commercial;
    public DataDecoder(String result){
      this.mResult=result;


    }
   public void decode(){
        try {
            JSONObject jsonRootObject = new JSONObject(mResult);
            if(jsonRootObject!=null){
                try {
                    JSONArray villasJArray = jsonRootObject.getJSONArray("villas");
                    vila=new Villa();
                    vila.setVillas(villasJArray);
                    JSONArray residentialsJArray = jsonRootObject.getJSONArray("residentials");
                    residential =new Residential();
                    residential.setResidentals(residentialsJArray);
                    JSONArray commercialsJArray = jsonRootObject.getJSONArray("commercials");
                    commercial=new Commercial();
                    commercial.setCommercial(commercialsJArray);

                }catch (JSONException j){

                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class Residential{
     private ArrayList<Home>homes =new ArrayList<>();
        public Residential(){
        }
      public void setResidentals(JSONArray jsonArray){
            for (int index=0;index<=jsonArray.length();index++){
                try {
                    JSONObject jsonObject =new JSONObject(jsonArray.get(index).toString());
                    Property property=new Property();
                    property.setProperty(jsonObject);
                    Home home=new Home(index,property);
                    homes.add(index,home);
                } catch (JSONException e) {
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
                try {
                    JSONObject jsonObject =new JSONObject(jsonArray.get(index).toString());
                    Property property=new Property();
                    property.setProperty(jsonObject);
                    Home home=new Home(index,property);

                    homes.add(index,home);

                } catch (JSONException e) {
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

            for (int index=0;index<=jsonArray.length();index++){
                try {
                    JSONObject jsonObject =new JSONObject(jsonArray.get(index).toString());
                    Property property=new Property();
                    property.setProperty(jsonObject);
                    Home home=new Home(index,property);
                    homes.add(index,home);

                } catch (JSONException e) {
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



}
