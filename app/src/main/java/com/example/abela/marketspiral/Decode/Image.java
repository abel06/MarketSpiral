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

public class Image implements Parcelable{
    private String tag;
    private String url;
    public Image(String tag, String url) {
         this.tag=tag;
         this.url=url;
    }

    protected Image(Parcel in) {
        tag = in.readString();
        url = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
        dest.writeString(url);
    }

    public String getTag() {
        return tag;
    }

    public String getUrl() {
        return url;
    }
}
