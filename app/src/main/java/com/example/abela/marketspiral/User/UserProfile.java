package com.example.abela.marketspiral.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abela.marketspiral.Activities.SearchActivity;
import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Core.Session;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import java.util.HashMap;

public class UserProfile extends AppCompatActivity implements RemoteResponse {
String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Button finish_btn= (Button) findViewById(R.id.finish_btn);
        final EditText phone_number_et= (EditText) findViewById(R.id.phone_et);
        final EditText language_et= (EditText) findViewById(R.id.language_et);


        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone= phone_number_et.getText().toString();
                String language=language_et.getText().toString();
                Intent intent = getIntent();
                String type=intent.getStringExtra("type");
                String id=intent.getStringExtra("id");

                HashMap<String,String> collected_data= new HashMap<>();
                collected_data.put("phone", phone);
                collected_data.put("language", language);
                collected_data.put("id",id);
                collected_data.put("type",type);

                Log.d("ab_log","type "+type);
                Log.d("ab_log","id "+id);
                Log.d("ab_log","type "+type);

                editProfile(collected_data);


            }
        });
    }
    public void editProfile(HashMap<String,String>collected_data){
         new RemoteTask(Actions.EDIT_PROFILE, collected_data,this,getApplicationContext(), false).execute();
    }

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

    }


    @Override
    public void itemRemoved(int id) {

    }

    @Override
    public void searchItem(int id) {

    }

    @Override
    public void profileFinished(int responce) {
        Log.d("ab_log","r"+responce);
         if (responce==1){
            Intent serachintent =new Intent(UserProfile.this,SearchActivity.class);
             UserProfile.this.startActivity(serachintent);
             finish();
        }else {
             Session session=new Session(getApplicationContext());
             session.clearSession();
           //  Intent loginintent =new Intent(UserProfile.this,Login.class);
          //   UserProfile.this.startActivity(loginintent);
             Toast.makeText(getApplicationContext(),"Unable to register please try again",Toast.LENGTH_SHORT);
         }
    }

    @Override
    public void imageUploaded(int value) {

    }

    @Override
    public void myItems(Integer value,Object result) {

    }
}
