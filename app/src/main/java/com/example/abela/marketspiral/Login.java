package com.example.abela.marketspiral;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import java.util.HashMap;

public class Login extends AppCompatActivity implements RemoteResponse {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FloatingActionButton fabSignup= (FloatingActionButton) findViewById(R.id.fab_signup);
        fabSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent signUp =new Intent(Login.this,Registor.class);
               // Login.this.startActivity(signUp);
            }
        });

        final TextInputEditText input_usernameTv= (TextInputEditText) findViewById(R.id.input_username);
        final TextInputEditText input_passwordTv= (TextInputEditText) findViewById(R.id.input_password);


        Button loginBtn= (Button) findViewById(R.id.btn_login);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String,String> data=new HashMap<String, String>();
                    data.put(input_usernameTv.getText().toString(),input_passwordTv.getText().toString());
                    login(data);
                }
            });
    mContext.getApplicationContext();
    }
    public void login(HashMap<String,String> data) {

        new RemoteTask(Actions.USER_LOGIN,data,this,mContext).execute();
    }
    @Override
    public void loginFinished(int value) {

    }

    @Override
    public void registerFinished(int value) {

    }

    @Override
    public void searchFinished(int value, Object result) {

    }

    @Override
    public void geocodeFinished(int id, Object o) {

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

}
