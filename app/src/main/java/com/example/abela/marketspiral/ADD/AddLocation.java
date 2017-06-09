package com.example.abela.marketspiral.ADD;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import com.example.abela.marketspiral.test_activity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class AddLocation extends AppCompatActivity implements OnMapReadyCallback, RemoteResponse {
    private GoogleMap mMap;
    public static SupportMapFragment mapFragment;
    private int item_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        Bundle extras = getIntent().getExtras();

        item_id = (int) extras.get("id");

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
        FloatingActionButton fab_next= (FloatingActionButton) findViewById(R.id.fab_next);
        fab_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double latitude;
                double longitude;
                if(marker!=null){
                  latitude=marker.getPosition().latitude;
                  longitude=marker.getPosition().longitude;
                   // getIntent().getExtras()
                    HashMap<String,String> data = new HashMap<String, String>();
                    data.put("lat",String.valueOf(latitude));
                    data.put("long",String.valueOf(longitude));
                    data.put("id",String.valueOf(item_id));
                    new RemoteTask(Actions.ADD_LOCATION,data,AddLocation.this,false).execute();
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
    public void loginFinished(int id) {

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
    public void itemAdded(int id) {

    }

    @Override
    public void itemRemoved(int id) {

    }

    @Override
    public void searchItem(int id) {

    }

    @Override
    public void addLocation(int return_state) {

        if(return_state == 0){
        Intent i = new Intent(this, test_activity.class);
            i.putExtra("id",item_id);
            startActivity(i);


        }else {
            Toast.makeText(getApplicationContext(),"Could not add/upload location",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void addItem(int id) {

    }

    @Override
    public void imageUploaded(int value) {

    }

    @Override
    public void editProfile(int value) {

    }
}
