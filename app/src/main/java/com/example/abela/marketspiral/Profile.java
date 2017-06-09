package com.example.abela.marketspiral;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import java.util.HashMap;

public class Profile extends AppCompatActivity implements RemoteResponse {
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button finish_btn= (Button) findViewById(R.id.finish_btn);
        final EditText phone_number_et= (EditText) findViewById(R.id.phone_et);
        final EditText language_et= (EditText) findViewById(R.id.language_et);
       mContext=getApplicationContext();
        Intent intent = getIntent();
        final String id=intent.getStringExtra("id");


        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone= phone_number_et.getText().toString();
                String language=language_et.getText().toString();

                HashMap<String,String> collected_data= new HashMap<>();
                collected_data.put("id",id);
                collected_data.put("phone", phone);
                collected_data.put("language", language);

                editProfile(collected_data);

            }
        });
    }
    public void editProfile(HashMap<String,String>collected_data){
        Log.d("ab_log",""+collected_data);
      new RemoteTask(Actions.EDIT_PROFILE, collected_data,Profile.this,mContext, false).execute();
      //  new RemoteTask(Actions.USER_REGISTRATION,collected_data,Profile.this,true).execute();
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
    public void addItem(int id) {

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

    }

    @Override
    public void imageUploaded(int value) {

    }

    @Override
    public void editProfile(int value) {
       // Toast.makeText(getApplicationContext(),"Secssusfully added",Toast.LENGTH_SHORT).show();

    }
}
