package com.example.abela.marketspiral.Core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.abela.marketspiral.Decode.Home;
import com.example.abela.marketspiral.Decode.Image;
import com.example.abela.marketspiral.Google.Geocode;
import com.example.abela.marketspiral.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class DescriptionActivity extends AppCompatActivity implements OnMapReadyCallback,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
      private Context mContext;
      private Activity mActivity;
      Home home;
    private GoogleMap mMap;
    private SliderLayout mDemoSlider;
    public static SupportMapFragment mapFragment;

    double lat;
    double lng;
    String phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
      //-----------------------------------------------
        mContext =getApplicationContext();
        home     = getIntent().getParcelableExtra("home");
        lat      =home.getLat();
        lng      =home.getLng();
        phone    =home.getOwner().getPhone();
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

        TextView address_tv = (TextView) findViewById(R.id.address_tv);
        TextView owner_name_tv= (TextView) findViewById(R.id.owner_name_tv);
        TextView email_tv= (TextView) findViewById(R.id.email_tv);
        TextView phone_number_tv = (TextView) findViewById(R.id.phone_number_tv);
        TextView language_tv = (TextView) findViewById(R.id.lanuage_tv);

        //-----------------------------------------------
        owner_name_tv.setText(home.getOwner().getName());
        email_tv.setText(home.getOwner().getEmail());
        phone_number_tv.setText(home.getOwner().getPhone()+" "+home.getOwner().getPhone());        //address and owner info
        language_tv.setText(home.getOwner().getLanguage());
        address_tv.setText(home.getAddress());
        //------------------------------------------------------
        FloatingActionButton loc_on_mmap_btn= (FloatingActionButton) findViewById(R.id.fab_loc_on_map);
        loc_on_mmap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(DescriptionActivity.this, MapsActivity.class);
                mapIntent.putExtra("lat",lat);
                mapIntent.putExtra("lng",lng);
                DescriptionActivity.this.startActivity(mapIntent);
            }
        });
        FloatingActionButton call_btn= (FloatingActionButton) findViewById(R.id.fab_call);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent call_intent=new Intent(Intent.ACTION_DIAL);
                call_intent.setData(Uri.parse("tel:"+phone));
                DescriptionActivity.this.startActivity(call_intent);
            }
        });
        //-----------------------------------------------

        TextView  price_tv= (TextView) findViewById(R.id.price_tv);
        price_tv.setText(""+home.getPrice());
        TextView  bedroom_tv= (TextView) findViewById(R.id.bedroom_tv);
        bedroom_tv.setText(""+home.getBedroom());
        TextView  builup_tv= (TextView) findViewById(R.id.price_tv);
        builup_tv.setText(""+home.getBuildup_area());
        TextView description= (TextView) findViewById(R.id.description_tv);
        description.setText(home.getDescription());
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
      //-----------------------------------------------


        ArrayList<Image>images =home.getImage().getAllImages();

        HashMap<String,String> url_maps = new HashMap<String, String>();
        for(int i=0;i<images.size();i++) {
            url_maps.put(images.get(i).getTag(), images.get(i).getUrl());
       }

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getApplicationContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);   ///>>
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //if (mListener != null) {
        // mListener.onFragmentInteraction(uri);
        // }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
         mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        MarkerOptions markerOpt =new MarkerOptions()
                .position(new LatLng(lat,lng))
                .anchor(.5f,.5f);

        Marker marker=mMap.addMarker(markerOpt);

        CameraPosition cameraPosition=new CameraPosition.Builder().target(new LatLng(lat,lng))
                .zoom(14).tilt(30).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
