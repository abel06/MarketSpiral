package com.example.abela.marketspiral.Activities;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.abela.marketspiral.Decode.DataDecoder;
import com.example.abela.marketspiral.Decode.Home;
import com.example.abela.marketspiral.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryActivity extends AppCompatActivity {
  String result="";
    HashMap<String,String>query=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

         result= (String) getIntent().getExtras().get("result");
        query= (HashMap<String, String>) getIntent().getSerializableExtra("query");
        ImageView villaBtn= (ImageView) findViewById(R.id.villaBtn);
        ImageView residencialBtn= (ImageView) findViewById(R.id.residentialBtn);
        ImageView commercialBtn= (ImageView) findViewById(R.id.commercialBtn);




        villaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DataDecoder dataDecoder =new DataDecoder(result);
                dataDecoder.decode();
                ArrayList<Home>homeList=dataDecoder.getVillaHomes();
                if(!homeList.isEmpty()){
                    query.put("category","Villa");
                    startMainActivity(homeList,query);
                }else {
                    Toast.makeText(getApplicationContext(),"Sorry no Villa homes registered at this location",Toast.LENGTH_SHORT).show();
                }

            }
        });
        residencialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DataDecoder dataDecoder =new DataDecoder(result);
                dataDecoder.decode();
                ArrayList<Home>homeList=dataDecoder.getResidentialHomes();
                Log.d("ab_log","homellist"+homeList);
                if(!homeList.isEmpty()){
                    query.put("category","Residential");
               startMainActivity(homeList,query);
                }else {
                    Toast.makeText(getApplicationContext(),"Sorry no residential homes registered at this location",Toast.LENGTH_SHORT).show();
                }

            }
        });
        commercialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DataDecoder dataDecoder =new DataDecoder(result);
                dataDecoder.decode();
                ArrayList<Home>homeList=dataDecoder.getCommercialHomes();
                if(!homeList.isEmpty()){
                    query.put("category","Commercial");
                startMainActivity(homeList,query);}
               else{ Toast.makeText(getApplicationContext(),"Sorry no commercial homes registered at this location",Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }
    public void startMainActivity(ArrayList<Home> homeList ,HashMap<String,String>q){
        Intent mainActivity =new Intent(this,MainActivity.class);
         mainActivity.putParcelableArrayListExtra("homes",homeList);
        mainActivity.putExtra("query",q);
         startActivity(mainActivity);
    }
}
