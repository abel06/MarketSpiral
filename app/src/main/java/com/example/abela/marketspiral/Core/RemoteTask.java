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

/**
 * Created by HaZe on 5/2/17.
 * This class performs all the remote tasks (user registration , login ecc.. ) that are needed
 */

public class RemoteTask extends AsyncTask<Void,Void,Integer> {
    ProgressDialog progressDialog;
    /**This interface is used to notify when a task finish*/
    private RemoteResponse delegate;
    Context mContext;

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
        progressDialog= ProgressDialog.show((Context) delegate, "","Please Wait", true);
        super.onPreExecute();
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

        }

        return -1;
    }



    @Override
    protected void onPostExecute(Integer integer) {
        progressDialog.dismiss();
        Log.d("ab","   "+integer);

        switch (ACTION){

            case Actions.USER_REGISTRATION: delegate.registerFinished(integer);break;

            case Actions.USER_LOGIN : delegate.loginFinished(integer); break;

            case Actions.NEW_ITEM : delegate.itemAdded(integer);break;

            case Actions.REMOVE_ITEM: delegate.itemRemoved(integer);break;

            case Actions.SEARCH_ITEM: delegate.searchFinished(integer,result);
                break;
            case Actions.GEOCDE_LOCATION: delegate.geocodeFinished(integer,result);
                break;
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
        String response ="";
        // String Search="https://api.data.gov/nrel/alt-fuel-stations/v1/nearest.json?api_key=5cwCk6nhFAkPu9BU3EyxafUN5jqytIGvGD6R4kcO&location=Denver+CO";
        InputStream inputStream = null;
        //--------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------
        URL url = null;
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
            response = sb.toString();

        } catch (MalformedURLException e) {
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
            connection.disconnect();
        }

        return 1;

    }

    private int userLogin(){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String response ="";
        // String Search="https://api.data.gov/nrel/alt-fuel-stations/v1/nearest.json?api_key=5cwCk6nhFAkPu9BU3EyxafUN5jqytIGvGD6R4kcO&location=Denver+CO";
        InputStream inputStream = null;
        //----------------------------------------ad    ---------------------------

        //-----------------------------------------------------------------------------------------------
        URL url = null;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.LOGIN);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");      //Data sent via POST method


            connection.setDoOutput(true);
            connection.setConnectTimeout(7000);

            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());

//            HashMap<String,String> user_data = hashMaps[0]; //Collect data from input
//
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
            response = sb.toString();

        } catch (MalformedURLException e) {
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
            connection.disconnect();
        }

        return 1;
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

//            HashMap<String,String> user_data = hashMaps[0]; //Collect data from input
//
//            String data = "username="+user_data.get("username")+"&password="+user_data.get("password"); // Concatenate data into a request


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

            //TextWriteRead textWriteRead =new TextWriteRead();
           // textWriteRead.writeToFile(response,);
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
