package com.example.abela.marketspiral.Core;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.Functions;
import com.example.abela.marketspiral.Utility.ServerInfo;
import com.example.abela.marketspiral.Utility.TextWriteRead;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.jar.Pack200;

/**
 * Created by HaZe on 5/2/17.
 * This class performs all the remote tasks (user registration , login ecc.. ) that are needed
 */

public class RemoteTask extends AsyncTask<Void,Void,Integer> {
    ProgressDialog progressDialog;
    /**This interface is used to notify when a task finish*/
    private RemoteResponse delegate;

    private Context mContext;

    /**This is the current action to perform @{@link Actions}**/
    private int ACTION;

    private Object result;
    // private Object responce;
    /** Here is where the data is stored (Using Objects doesn't not matter if are strings or Item or whatever)*/
    private Object args;

    public RemoteTask(int ACTION, Object args, RemoteResponse delegate,Context context ){

        this.ACTION = ACTION;
        this.args = args;
        this.delegate = delegate;
        this.mContext=context;

    }

    public RemoteTask(int ACTION, Object args, RemoteResponse delegate){

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

    public int getACTION() {
        return ACTION;
    }

    public void setACTION(int ACTION) {
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

        progressDialog= ProgressDialog.show((Context) delegate, "","Please Wait", true);


    }

    @Override
    protected Integer doInBackground(Void... voids) {

        switch (ACTION){

            case Actions.USER_REGISTRATION : return userRegistration();

            case Actions.USER_LOGIN : return userLogin();

            case Actions.NEW_ITEM : return newItem();

            case Actions.REMOVE_ITEM : return removeItem();

            case Actions.SEARCH_ITEM: return searchItem();

            case Actions.GEOCDE_LOCATION: return geocode();

            case Actions.REGISTER_WITH_EXTERNAL_SERVICES: return userRegistration();

        }

        return -1;
    }


    @Override
    protected void onPostExecute(Integer integer) {
        progressDialog.dismiss();
        Log.d("ab","   "+integer);


        //free the data structure --> not sure if is 'nice' to do in that manner...
        args = null;

        switch (ACTION){

            case Actions.USER_REGISTRATION: delegate.registerFinished(integer,false);break;

            case Actions.USER_LOGIN : delegate.loginFinished(integer); break;

            case Actions.NEW_ITEM : delegate.itemAdded(integer);break;

            case Actions.REMOVE_ITEM: delegate.itemRemoved(integer);break;

            case Actions.SEARCH_ITEM: delegate.searchFinished(integer,result);
                break;
            case Actions.GEOCDE_LOCATION: delegate.geocodeFinished(integer,result);
                break;

            case Actions.REGISTER_WITH_EXTERNAL_SERVICES: delegate.registerFinished(integer,true); break;
        }

    }

    private Integer removeItem() {


        return 0;
    }

    private Integer newItem() {
        return 0;
    }

    private int userRegistration() {

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        int server_response=-1;
        //--------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------
        URL url ;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.REGISTER);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");      //Data sent via POST method

            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

//            HashMap<String,String> user_data = hashMaps[0]; //Collect data from input
//            String data = "username="+user_data.get("username")+"&password="+user_data.get("password"); // Concatenate data into a request

            HashMap<String,String> user_data = (HashMap<String, String>) args;

            wr.write(Functions.ConcatenateForServer(user_data));
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            server_response = Integer.parseInt(sb.toString());

        }  catch (IOException e) {
            e.printStackTrace();
            return server_response;
        } finally {
            connection.disconnect();
        }

        return server_response;

    }

    private int userLogin(){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        int server_response=-1;
        //----------------------------------------ad    ---------------------------

        //-----------------------------------------------------------------------------------------------
        URL url;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.LOGIN);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");      //Data sent via POST method

            connection.setDoOutput(true);
            connection.setConnectTimeout(7000);

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

            HashMap<String,String> user_data = (HashMap<String, String>) args;

            wr.write(Functions.ConcatenateForServer(user_data));
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            server_response = Integer.parseInt(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return server_response;
        } finally {
            connection.disconnect();
        }

        return server_response;
    }

    private Integer searchItem() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String response ="";
        InputStream inputStream = null;
        //----------------------------------------ad    ---------------------------

        //-----------------------------------------------------------------------------------------------
        URL url = null;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.SEARCH_ITEM);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");      //Data sent via POST method

            connection.setDoOutput(true);
            connection.setConnectTimeout(7000);

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

            HashMap<String,String> search_data = (HashMap<String, String>) args;

            wr.write(Functions.ConcatenateForServer(search_data));
            wr.flush();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();


        } catch (MalformedURLException e) {
            Toast.makeText(mContext,"No Internet check out your connection setting",Toast.LENGTH_LONG);
            e.printStackTrace();
            return 0;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        } finally {
            //                reader.close();
//                inputStream.close();
            if(connection != null)
                connection.disconnect();
        }

        return 1;
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
}
