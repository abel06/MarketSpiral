package com.example.abela.marketspiral.ADD;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.abela.marketspiral.Core.Session;
import com.example.abela.marketspiral.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddItem extends AppCompatActivity  {
    private GoogleMap mMap;
    public static SupportMapFragment mapFragment;
    private HashMap<String,String> data = new HashMap<String, String>();
    private Spinner spinnerCategory;
    private Spinner spinnerBedroom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Button next_btn= (Button) findViewById(R.id.next_btn);

        addItemsOnSpinnerCategory();
        addItemsOnSpinnerBedroom();

        final EditText  title_et = (EditText) findViewById(R.id.title_et);
        final EditText  buildup_et= (EditText) findViewById(R.id.buildup_et);
        final EditText  price_et= (EditText) findViewById(R.id.price_et);
        final EditText  description_et= (EditText) findViewById(R.id.description_et);
       // final EditText  date = (EditText) findViewById(R.id.date);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title=title_et.getText().toString();
                final String buildup=buildup_et.getText().toString();
                final String price=price_et.getText().toString();
                final String description=description_et.getText().toString();
                //final String date_value = date.getText().toString();

                Log.d("ab",""+title+buildup+price+description);
                if(title.matches("")){
                    title_et.setError( "Title is required!" );
                    Toast.makeText(getApplicationContext(),"Title is Required",Toast.LENGTH_SHORT).show();
                }else if(buildup.matches("")){
                    buildup_et.setError( "Buildup is required!" );
                    Toast.makeText(getApplicationContext(),"Buildup is Required",Toast.LENGTH_SHORT).show();
                }else if(price.matches("")){
                    price_et.setError( "Price is required!" );
                    Toast.makeText(getApplicationContext(),"Price is Required",Toast.LENGTH_SHORT).show();
                }else if(description.matches("")){
                    description_et.setError( "Description is required!" );
                    Toast.makeText(getApplicationContext(),"Description is Required",Toast.LENGTH_SHORT).show();
                }else {

                    Session session=new Session(getApplicationContext());
                   String type= session.getType();
                    String id=session.getUserID();
                    data.put("id",id);
                    data.put("type",type);
                    data.put("category",""+spinnerCategory.getSelectedItem());
                    data.put("bedroom",""+spinnerBedroom.getSelectedItem());
                    data.put("title",title);
                    data.put("buildup",buildup);
                    data.put("price",price);
                    data.put("description",description);
                   // data.put("date",date_value);
                    Intent addlocation_intent=new Intent(AddItem.this,AddLocation.class);
                    addlocation_intent.putExtra("data",data);
                    startActivity(addlocation_intent);
                    finish();
                   // new RemoteTask(Actions.ADD_ITEM,data,AddItem.this,getApplicationContext(),false).execute();
                }

            }
        });

      /*  ImageView imageView= (ImageView) findViewById(R.id.camera_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        */
//        //-----------------------------------------------
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
    public void addItemsOnSpinnerCategory() {

        spinnerCategory= (Spinner) findViewById(R.id.spinner_Add_Category);
        String exchangeType=getResources().getString(R.string.exchangeType);
        List<String> list = new ArrayList<String>();
        list.add(getResources().getString(R.string.villa));
        list.add(getResources().getString(R.string.residential));
        list.add(getResources().getString(R.string.commercial));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(dataAdapter);
    }
    private void addItemsOnSpinnerBedroom() {
        spinnerBedroom= (Spinner) findViewById(R.id.spinner_Add_Bedroom);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView mImageView = null;
            mImageView.setImageBitmap(imageBitmap);
        }
    }




}