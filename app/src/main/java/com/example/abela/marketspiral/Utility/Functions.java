package com.example.abela.marketspiral.Utility;

import android.util.Log;

import java.util.HashMap;

/**
 * Created by HaZe on 4/11/17.
 */

public final class Functions {


    /**This method concatenate data in the URL*/
    public static String ConcatenateForServer(HashMap<String,String> data){

        String result = "";

        for (String key: data.keySet()) {

            String value = data.get(key);

            if(!value.isEmpty()) {
                if (result.isEmpty()) {
                    result += key + "=" + data.get(key);
                } else {
                    result += "&" + key + "=" + data.get(key);
                }
            }

        }

        Log.d("concatenate",result);

        return "?"+result;
    }


    /**TODO */
    public static HashMap<String,String> RetrieveDataFromUI(){
        return null;
    }

}
