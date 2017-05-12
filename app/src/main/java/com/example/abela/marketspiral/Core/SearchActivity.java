package com.example.abela.marketspiral.Core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abela.marketspiral.CategoryActivity;
import com.example.abela.marketspiral.Google.Geocode;
import com.example.abela.marketspiral.Google.LocationSettingDialog;
import com.example.abela.marketspiral.Google.PlayServiceCheck;
import com.example.abela.marketspiral.MainActivity;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.TextWriteRead;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, LocationListener ,RemoteResponse{
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;



    private Spinner spinnerFrom, spinnerTo,spinnerBedroom;
    private Button btnSearch;
    private FloatingActionButton locationBtn;
//==========================================================
    LocationSettingDialog locationSettingDialog;
    Address address=null;
    String country="";
    String state="";
    String city="";
    String zip="";
    Place place;
  //===================================================
    public static GoogleApiClient mGoogleApiClient;
    public EditText autocomplete_tv;
//======================================================
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext=getApplicationContext();
        locationSettingDialog = new LocationSettingDialog(this);
        addItemsOnSpinnerFrom();
        addItemsOnSpinnerTo();
        addItemsOnSpinnerBedroom();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

        autocomplete_tv    = (EditText) findViewById(R.id.address_tv);
        locationBtn        = (FloatingActionButton) findViewById(R.id.fab_call);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLocationEnabled()){
                   getLastknownLocation();
                }

            }
        });
        autocomplete_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();                        //opens a new activity when textbox is focused
            }
        });
        //-----------------------------------------------------------------------------
        PlayServiceCheck playServiceCheck = new PlayServiceCheck(getApplicationContext());
        if (playServiceCheck.isPlayServiceOk()) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }
//-----------------------------------------------------------------------------
    }
    synchronized void buildGoogleApiClient() {

        try{
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }catch (Exception e){
        }
    }
    public void getLastknownLocation(){

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location loc=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);;
             if (loc != null) {
                 geocode(loc);
             } else {
                 loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //if google null try from gps provider
                 Log.d("ab", "loc" + loc);

                 if (loc != null) {
                     geocode(loc);
                 } else {
                     loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);// else try from net provider last location for search
                     if (loc != null) {
                         geocode(loc);
                     } else {
                         loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);// else try from passive
                         if (loc != null) {
                             geocode(loc);
                         } else {
                             Toast.makeText(mContext, "Unable to obtain location please check your location setting", Toast.LENGTH_SHORT).show();
                         }
                     }

                 }
             }
    }

    //================================================================
    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e("ab", message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }    //Method for opening an autocomplete acitivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                 place = PlaceAutocomplete.getPlace(this, data);


                if(place.getName()!=null){
                    autocomplete_tv.setText(place.getName());}

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("ab", "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }   //when activity is done set he autocomplate Edit text with result
    //==============================================================
    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean is_GPS_Enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean is_Network_Enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (is_GPS_Enabled == false && is_Network_Enabled == false) {

            if(!locationSettingDialog.isDialogVisible()){
                locationSettingDialog.showSettingsAlert();}

            return false;
        } else {
            return true;
        }
    }

    //===================================================================================
    public void addItemsOnSpinnerFrom() {

        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        String exchangeType=getResources().getString(R.string.exchangeType);
        List<String> list = new ArrayList<String>();
        list.add(getResources().getString(R.string.no_min));
        list.add("300"+" "+exchangeType);
        list.add("500"+" "+exchangeType);
        list.add("700"+" "+exchangeType);
        list.add("900"+" "+exchangeType);
        list.add("1200"+" "+exchangeType);
        list.add("1500"+" "+exchangeType);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(dataAdapter);
    }
    private void addItemsOnSpinnerTo() {
        String exchangeType=getResources().getString(R.string.exchangeType);
        spinnerTo= (Spinner) findViewById(R.id.spinnerTo);
        List<String> list = new ArrayList<String>();
        list.add(getResources().getString(R.string.no_max));
        list.add("500"+" "+exchangeType);
        list.add("700"+" "+exchangeType);
        list.add("900"+" "+exchangeType);
        list.add("1200"+" "+exchangeType);
        list.add("1500"+" "+exchangeType);
        list.add("1700"+" "+exchangeType);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(dataAdapter);
    }
    private void addItemsOnSpinnerBedroom() {
        spinnerBedroom= (Spinner) findViewById(R.id.spinnerBedroom);
        List<String> list = new ArrayList<String>();
        list.add("1+");
        list.add("2+");
        list.add("3+");
        list.add("4+");
        list.add("5+");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBedroom.setAdapter(dataAdapter);
    }
    public void addListenerOnSpinnerItemSelection() {
        spinnerFrom = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerFrom.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinnerTo= (Spinner) findViewById(R.id.spinnerTo);
        spinnerTo.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        spinnerBedroom= (Spinner) findViewById(R.id.spinnerBedroom);
        spinnerBedroom.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    public void addListenerOnButton() {

        spinnerFrom    = (Spinner) findViewById(R.id.spinnerFrom);
        spinnerTo      = (Spinner) findViewById(R.id.spinnerTo);
        spinnerBedroom = (Spinner) findViewById(R.id.spinnerBedroom);
        btnSearch      = (Button) findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(autocomplete_tv!=null) {   //if the location is from location button
                    if (!autocomplete_tv.getText().toString().isEmpty()) {
                        if (address != null) {
                            HashMap<String,String> data=new HashMap<String, String>();
                            data.put("lat",""+address.getLatitude());
                            data.put("lat",""+address.getLongitude());
                            data.put("city",""+city);
                            data.put("state",""+state);
                            data.put("country",""+country);

                            if (spinnerFrom != null) {
                                data.put("price_from",""+spinnerFrom.getSelectedItem().toString());
                            }
                            if (spinnerTo != null) {
                                data.put("price_from",""+spinnerTo.getSelectedItem().toString());
                            }
                            if (spinnerBedroom != null) {
                                data.put("bed_room",""+spinnerBedroom.getSelectedItem().toString());
                            }

                             search(data);    //actual search

                        } else if(place!=null){   //else if the location is from autocomplate window
                            HashMap<String,String> data=new HashMap<String, String>();
                            data.put("lat",""+place.getLatLng().latitude);
                            data.put("lat",""+place.getLatLng().longitude);
                            data.put("city",""+place.getLocale());
                           if(place.getLocale()!=null){
                            data.put("country",""+place.getLocale().getCountry());}

                            if (spinnerFrom != null) {
                                data.put("price_from",""+spinnerFrom.getSelectedItem().toString());
                            }
                            if (spinnerTo != null) {
                                data.put("price_from",""+spinnerTo.getSelectedItem().toString());
                            }
                            if (spinnerBedroom != null) {
                                data.put("bed_room",""+spinnerBedroom.getSelectedItem().toString());
                            }
                            search(data);    //actual search

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "" + getResources().getText(R.string.no_loc_obtained) + autocomplete_tv.getText(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "" + getResources().getText(R.string.select_loc_first), Toast.LENGTH_LONG).show();
                    }
                }
            }

        });
    }                           //search button   check text boxes and location the start new activity with given data

    public void search(HashMap<String,String> data) {
        new RemoteTask(Actions.SEARCH_ITEM,data,this,mContext).execute();  //search method  will be called from addListnerOnButton method
    }
    public void geocode(Location loc){   //geocode from givin location
        new RemoteTask(Actions.GEOCDE_LOCATION,loc,this,mContext).execute();
    };                                                              ///geocode
    //--------------------------------------------------------------------------
    @Override
    public void searchFinished(int value,Object result) {
        String result_tmp= (String) result;
        if(value == 1){
            Intent categoryIntent = new Intent(SearchActivity.this, CategoryActivity.class);
            categoryIntent.putExtra("result",result_tmp);
            SearchActivity.this.startActivity(categoryIntent);   //start Main Activity
            TextWriteRead textWriteRead =new TextWriteRead();
            textWriteRead.writeToFile(result_tmp,getApplicationContext(),"result");
        }else {
            Toast.makeText(mContext,"Unable to reach Homespiral server please check you internet",Toast.LENGTH_SHORT).show();
        }
    }   //deligate search finished if the search is finished open new activity

    @Override
    public void geocodeFinished(int id, Object result) {


        List<Address> addresses= (List<Address>) result;
        if(id==1){
            if(addresses==null){
                Toast.makeText(mContext,"Unable to get address check out ur internet setting",Toast.LENGTH_SHORT);
            }
            else{
                address= addresses.get(0);

                if(address.getLocality()!=null){
                    city=address.getLocality();
                }
                if(address.getCountryName()!=null){
                    country=address.getCountryName();}
                if(address.getAdminArea()!=null){
                    state=address.getAdminArea();}
                if(address.getPostalCode()!=null){
                    zip=address.getPostalCode();}
                if(address.getLocality()!=null){
                    address.getLocality();}
                autocomplete_tv.setText(""+zip+" "+city+" "+state+" "+country);
            }
        }
        else {
            Toast.makeText(mContext,"Unable to get address check out ur internet setting",Toast.LENGTH_SHORT);
        }

    } //when geocode finished set the data to autocomplate Edit text

//================================================================================================================================================
                                                                           //Not used methods  below this
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void loginFinished(int value) {

    }

    @Override
    public void registerFinished(int value) {

    }

    @Override
    public void itemAdded(int id) {

    }

    @Override
    public void itemRemoved(int id) {

    }

    @Override
    public void searchItem(int id) {
    }


//-----------------------------------------------------------------------
}

