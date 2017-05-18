package com.example.abela.marketspiral.ADD;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.example.abela.marketspiral.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddLocation extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    public static SupportMapFragment mapFragment;
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
                    Intent add_image=new Intent(AddLocation.this,AddImage.class);
                    add_image.putExtra("lat",latitude);
                    add_image.putExtra("lng",longitude);
                    add_image.putExtras(getIntent().getExtras());
                    AddLocation.this.startActivity(add_image);
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
}
