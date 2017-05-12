package com.example.abela.marketspiral.Utility;

import java.util.HashMap;

/**
 * Created by HaZe on 4/11/17.
 */

public final class Functions {

    public static String ConcatenateForServer(HashMap<String,String> data){

        String result = "";

        for (String key: data.keySet()) {

            if(result.isEmpty()) {
                result += key + "=" +data.get(key);
            }else{
                result +="&"+key+"="+data.get(key);
            }

        }

        return result;
    }

}
