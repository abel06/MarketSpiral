package com.example.abela.marketspiral.Core;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.abela.marketspiral.User.User;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.Functions;
import com.example.abela.marketspiral.Utility.ServerInfo;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import com.google.android.gms.maps.model.LatLng;

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

    private Context mContext;

    /**This is the current action to perform @{@link Actions}**/
    private int ACTION;

    private Object result;

    // private Object responce;
    /** Here is where the data is stored (Using Objects doesn't not matter if are strings or Item or whatever)*/
    private Object args;

    public RemoteTask(int ACTION, Object args, RemoteResponse delegate, Context context, boolean b){

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
        super.onPreExecute();
        progressDialog= ProgressDialog.show((Context) delegate, "","Please Wait", true);


    }

    @Override
    protected Integer doInBackground(Void... voids) {

        switch (ACTION){

            case Actions.USER_REGISTRATION : return userRegistration();

            case Actions.USER_LOGIN : return userLogin();

         //   case Actions.NEW_ITEM : return newItem();

            case Actions.REMOVE_ITEM : return removeItem();

            case Actions.SEARCH_ITEM: return searchItem();

            case Actions.GEOCODE_LOCATION: return geocode();

            case Actions.REGISTER_WITH_EXTERNAL_SERVICES: return userRegistration();

            case Actions.ADD_ITEM: return addItem();

            case Actions.EDIT_PROFILE: return editprofile();

            case Actions.UPLOAD_IMAGE: return imageUpload();

            case Actions.MY_ITEMS:return myItems();


        }

        return -1;
    }



    @Override
    protected void onPostExecute(Integer integer) {
        progressDialog.dismiss();


        //free the data structure --> not sure if is 'nice' to do in that manner...
        args = null;

        switch (ACTION){

            case Actions.USER_REGISTRATION: delegate.registerFinished(integer,false);break;

            case Actions.USER_LOGIN : delegate.loginFinished(integer,result); break;

            case Actions.ADD_ITEM : delegate.itemAdded(integer,result);break;

            case Actions.REMOVE_ITEM: delegate.itemRemoved(integer);break;

            case Actions.SEARCH_ITEM: delegate.searchFinished(integer,result);
                break;
            case Actions.GEOCODE_LOCATION: delegate.geocodeFinished(integer,result);
                break;

            case Actions.REGISTER_WITH_EXTERNAL_SERVICES: delegate.registerFinished(integer,true);
                break;

            case Actions.EDIT_PROFILE: delegate.profileFinished(integer);
                break;
            case Actions.UPLOAD_IMAGE:delegate.imageUploaded(integer);
                break;
                case Actions.MY_ITEMS: delegate.myItems(integer,result);
        }

    }
    private Integer myItems() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String response ="";
        InputStream inputStream = null;
        //----------------------------------------ad    ---------------------------

        //-----------------------------------------------------------------------------------------------
        URL url = null;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.MY_ITEM);
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
            Log.d("ab_log",""+e);
            return -1;
        } catch (ProtocolException e) {
            Log.d("ab_log",""+e);
            e.printStackTrace();
            return -2;
        } catch (IOException e) {
            Log.d("ab_log",""+e);
            e.printStackTrace();
            return -3;
        } finally {
            //                reader.close();
//                inputStream.close();
            if(connection != null)
                connection.disconnect();
        }

        return 1;
    }

    private Integer removeItem() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        int server_response=-1;

        //----------------------------------------ad    ---------------------------

        //-----------------------------------------------------------------------------------------------
        URL url;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.REMOVE_ITEM);

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
            result=sb.toString();

            try {
                Log.d("ab_log","exception result "+result);
                server_response = Integer.parseInt(sb.toString());

            }catch (Exception e){
                Log.d("ab_log","exception "+e);
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ab_log","abbbbbbb"+e);
            return server_response;
        } finally {
            connection.disconnect();
        }

        return server_response;
    }

    private Integer newItem() {
        return 0;
    }

    private int userRegistration() {

      /* HttpURLConnection connection = null;
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

        return server_response;*/
      return 0;
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
            url = new URL(ServerInfo.DB_URL+ServerInfo.REGISTER);
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
            result=sb.toString();
            Log.d("ab_log","result remote"+result);


            User user =new User(sb.toString());
            user.execute();

            Session session=new Session(mContext);
            session.setUserID(user.getUser_id());
            session.setExternalId(user.getExternal_id());
            session.setType(user.getType());
            session.setLanguage(user.getLanguage());
            session.setPhone(user.getPhone());
            session.setProfile(user.getProfile());


            //server_response = Integer.parseInt(sb.toString());
          //  return 1;

        } catch (IOException e) {
            Log.d("ab_log","result remote"+e);
            return 0;
        } finally {
            connection.disconnect();
        }
  return 0;
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
            result = String.valueOf(sb);
            Log.d("ab_log","result"+result);

        } catch (MalformedURLException e) {
            Toast.makeText(mContext,"No Internet check out your connection setting",Toast.LENGTH_LONG);
            e.printStackTrace();
            return -1;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return -2;
        } catch (IOException e) {
            e.printStackTrace();
            return -3;
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
        Location loc= null;
        try {
           loc= (Location) args;
        }catch (Exception e){

        }
        LatLng latLng = null;
        try {
        latLng=(LatLng) args;
        }catch (Exception e){

        }
        geocoder = new Geocoder(mContext, Locale.getDefault());
        if(loc!=null){
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            result=addresses;
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        }else if(latLng!=null){
            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                result=addresses;
                return 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    private Integer addItem(){

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        int server_response=-1;
        //--------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------
        URL url ;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.ADD_ITEM);


            Log.d("ab","remote add"+args);
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
            Log.d("ab_log","additem"+sb.toString()+url);
            //server_response = Integer.parseInt(sb.toString());
            result=sb.toString();

        }  catch (IOException e) {
           return -3;
        } finally {
            connection.disconnect();
        }
        return 1;
    }

    private Integer editprofile(){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        int server_response=-1;

        //----------------------------------------ad    ---------------------------

        //-----------------------------------------------------------------------------------------------
        URL url;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.EDIT_PROFILE);

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
            result=sb.toString();

            try {
                Log.d("ab_log","exception result "+result);
            server_response = Integer.parseInt(sb.toString());

            }catch (Exception e){
              Log.d("ab_log","exception "+e);
            }


        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ab_log","abbbbbbb"+e);
            return server_response;
        } finally {
            connection.disconnect();
        }

        return server_response;

    }

    private Integer imageUpload() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        int server_response=-1;
        //--------------------------------------------------------------------
        //-----------------------------------------------------------------------------------------------
        URL url ;
        try {
            //Login php script location
            url = new URL(ServerInfo.DB_URL+ServerInfo.UPLOAD_IMAGE);


            Log.d("ab","remote add"+args);
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
            Log.d("ab_log","additem"+sb.toString()+url);
            //server_response = Integer.parseInt(sb.toString());
            result=sb.toString();

        }  catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        return server_response;
    }
}
