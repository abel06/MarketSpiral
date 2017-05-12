package com.example.abela.marketspiral;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.abela.marketspiral.Decode.DataDecoder;
import com.example.abela.marketspiral.Decode.Home;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
  String result="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

         result= (String) getIntent().getExtras().get("result");
        ImageView villaBtn= (ImageView) findViewById(R.id.villaBtn);
        ImageView residencialBtn= (ImageView) findViewById(R.id.residentialBtn);
        ImageView commercialBtn= (ImageView) findViewById(R.id.commercialBtn);


        final DataDecoder dataDecoder =new DataDecoder(result);
        dataDecoder.decode();

        villaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Home>homeList=dataDecoder.getVila().getHomes();
                startMainActivity(homeList );
            }
        });
        residencialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Home>homeList=dataDecoder.getResidential().getHomes();
                startMainActivity(homeList);
            }
        });
        commercialBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Home>homeList=dataDecoder.getCommercial().getHomes();
                startMainActivity(homeList);
            }
        });
    }
    public void startMainActivity(ArrayList<Home> homeList ){
        Intent mainActivity =new Intent(CategoryActivity.this,MainActivity.class);
        mainActivity.putParcelableArrayListExtra("homes",homeList);
        CategoryActivity.this.startActivity(mainActivity);
    }
}
