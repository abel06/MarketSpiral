package com.example.abela.marketspiral.ADD;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AddLocation extends AppCompatActivity implements OnMapReadyCallback ,RemoteResponse{
    private GoogleMap mMap;
    public static SupportMapFragment mapFragment;
    HashMap <String,String> data=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        //-----------------------------------------------
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment =fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();     //map in description activity
            mapFragment = (SupportMapFragment) fragment;
            mapFragment.getMapAsync(this);

        }
        //-----------------------------------------------
        Intent intent=getIntent();
       data= (HashMap<String, String>) intent.getSerializableExtra("data");
        Log.d("ab_log","data"+data);
        //------------------------------------------------
        FloatingActionButton fab_next= (FloatingActionButton) findViewById(R.id.fab_next);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude;
                double longitude;
                if(marker!=null){
                  latitude=marker.getPosition().latitude;
                  longitude=marker.getPosition().longitude;
                    data.put("lat",String.valueOf(latitude));
                    data.put("lng",String.valueOf(longitude));

                    new RemoteTask(Actions.ADD_ITEM,data,AddLocation.this,getApplicationContext(),false).execute();

                }
               else {
                    Toast.makeText(getApplicationContext(),"Please put marker on your home location",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Marker marker =null;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions markerOptions=new MarkerOptions().position(latLng);
                if(marker==null){
                marker=mMap.addMarker(markerOptions);}
                else {
               marker.setPosition(latLng);
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_maps, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                return true;
            case R.id.action_sattelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.action_hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.action_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.action_none:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//Not used methods  below this
    @Override
    public void loginFinished(int id, Object result) {

    }

    @Override
    public void registerFinished(int value, boolean externalService) {

    }

    @Override
    public void searchFinished(int value, Object result) {

    }

    @Override
    public void geocodeFinished(int id, Object result) {

    }

    @Override
    public void addItem(int id) {

    }

    @Override
    public void itemAdded(int id, Object result) {

        switch (id){
            case 1:

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String home_id= null;
        try {
            home_id = jsonObject.getString("home_id");
            Intent i = new Intent(this, AddImage.class);
            i.putExtra("home_id",home_id);

            startActivity(i);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        break;
            case -3:
                Toast.makeText(getApplicationContext(),"Unable to reach Homespiral server please check your internet",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void itemRemoved(int id) {

    }

    @Override
    public void searchItem(int id) {

    }

    @Override
    public void profileFinished(int responce) {

    }

    @Override
    public void imageUploaded(int value) {

    }

    @Override
    public void myItems(Integer value,Object result) {

    }
}
