package com.example.abela.marketspiral.Decode;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Abela on 5/10/2017.
 */

public class Images implements Parcelable {

  ArrayList<Image> images=new ArrayList<>();
    public Images(String images_jArrayList){
        try {
            JSONArray jsonArray =new JSONArray(images_jArrayList);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject =jsonArray.getJSONObject(i);
                  String tmp_tag=jsonObject.getString("tag");
                  String tmp_url=jsonObject.getString("url");

               Image image=  new Image(tmp_tag,tmp_url);
                images.add(image);

            }

        } catch (JSONException e) {
            Log.d("ab","image tag"+e);
        }
    }

    protected Images(Parcel in) {
        images = in.createTypedArrayList(Image.CREATOR);
       // image = in.readParcelable(Image.class.getClassLoader());
    }

    public static final Creator<Images> CREATOR = new Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(images);

    }

    public Image  getImageAtIndex(int index) {
        return images.get(index);
    }
   public int getImagesCount(){
     return images.size();
   }
   public ArrayList<Image>getAllImages(){
      return images;
   }
}
