package com.example.abela.marketspiral.Core;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.Functions;
import com.example.abela.marketspiral.Utility.ServerInfo;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by HaZe on 5/2/17.
 * This class performs all the remote tasks (user registration , login ecc.. ) that are needed
 **/

public class RemoteTask extends AsyncTask<Void,Void,Integer> {
    ProgressDialog progressDialog;
    /**This interface is used to notify when a task finish*/
    private RemoteResponse delegate;

    /**The context is needed to performe some UI action (e.s. progress Dialog)*/
    private Context mContext;

    /**This is the current action to perform @{@link Actions}**/
    private String ACTION;

    /**Dunno what Abel did*/
    private Object result;

    /** Here is where the data is stored (Using Objects doesn't not matter if are strings or Item or whatever)*/
    private Object args;

    private boolean using_external = false;

    public RemoteTask(String ACTION, Object args, RemoteResponse delegate,Context context,boolean using_external ){

        this.using_external = using_external;
        this.ACTION = ACTION;
        this.args = args;
        this.delegate = delegate;
        this.mContext=context;

    }

    public RemoteTask(String ACTION, Object args, RemoteResponse delegate, boolean using_external){

        this.using_external = using_external;
        this.ACTION = ACTION;
        this.args = args;
        this.delegate = delegate;
        this.mContext=null;

    }

    public RemoteResponse getDelegate() {
        return delegate;
    }

    public void setDelegate(RemoteResponse delegate) {
        this.delegate = delegate;
    }

    public String getACTION() {
        return ACTION;
    }

    public void setACTION(String  ACTION) {
        this.ACTION = ACTION;
    }

    public Object getArgs() {
        return args;
    }

    public void setArgs(Object args) {
        this.args = args;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        progressDialog= ProgressDialog.show((Context) delegate, "","Please Wait", true);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return ContactTheServer(ACTION);
    }


    @Override
    protected void onPostExecute(Integer integer) {
//        progressDialog.dismiss();


        //free the data structure --> not sure if is 'nice' to do in that manner...
        args = null;

        switch (ACTION){

            case Actions.USER_REGISTRATION: delegate.registerFinished(integer,using_external);break;

            case Actions.EDIT_PROFILE: delegate.editProfile(integer);break;

            case Actions.USER_LOGIN : delegate.loginFinished(integer); break;

            case Actions.NEW_ITEM : delegate.itemAdded(integer);break;

            case Actions.REMOVE_ITEM: delegate.itemRemoved(integer);break;

            case Actions.SEARCH_ITEM: delegate.searchFinished(integer,result); break;

            case Actions.GEOCODE_LOCATION: delegate.geocodeFinished(integer,result); break;

            case Actions.UPLOAD_IMAGE: delegate.imageUploaded(integer); break;

            case Actions.ADD_ITEM: delegate.addItem(integer); break;

            case Actions.ADD_LOCATION: delegate.addLocation(integer);break;
        }

    }

    private Integer geocode(){
        Geocoder geocoder;
        List<Address> addresses = null;
        Location loc= (Location) args;
        geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            result=addresses;
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }


    private Integer ContactTheServer(String Action) {
        Log.d("ab_log","responce server"+Action);
        HttpURLConnection connection = null;
        BufferedReader reader ;
        int server_response=-1;

        URL url;
        try {
            url = new URL(ServerInfo.DB_URL+Action);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");      //Data sent via POST method

            connection.setDoOutput(true);
            connection.setConnectTimeout(7000);

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

            HashMap<String,String> user_data = (HashMap<String, String>) args;
            if(using_external){
                user_data.put("external","1");
            }
            wr.write(Functions.ConcatenateForServer(user_data));
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
          // server_response = Integer.parseInt(sb.toString());
            Log.d("ab_log","response "+sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ab_log","error "+e);
            return server_response;
        } finally {
            connection.disconnect();
        }
        return server_response;

    }


}