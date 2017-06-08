package com.example.abela.marketspiral;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import java.util.HashMap;

public class Registor extends AppCompatActivity implements RemoteResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);
        FloatingActionButton fabLogin= (FloatingActionButton) findViewById(R.id.fab_login);
        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login =new Intent(Registor.this,Login.class);
                Registor.this.startActivity(login);
            }
        });

        findViewById(R.id.signup_layout).findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

    }

    public void register() {


        String username = ((TextInputEditText)findViewById(R.id.signup_layout).findViewById(R.id.input_username)).getText().toString();
        String password= ((TextInputEditText)findViewById(R.id.signup_layout).findViewById(R.id.input_password)).getText().toString();
        String email = ((TextInputEditText)findViewById(R.id.signup_layout).findViewById(R.id.input_email)).getText().toString();

        HashMap<String,String> data = new HashMap<>(3);
        data.put("username",username);
        data.put("password",password);
        data.put("full_name",email);

        new RemoteTask(Actions.USER_REGISTRATION,data,this,false).execute();

    }

    @Override
    public void loginFinished(int id) {

    }

    @Override
    public void registerFinished(int value, boolean externalService) {

        Toast.makeText(getApplicationContext(),"Id :" + value, Toast.LENGTH_SHORT).show();

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
}
