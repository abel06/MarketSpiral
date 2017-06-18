package com.example.abela.marketspiral.User;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.abela.marketspiral.Activities.SearchActivity;
import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registor extends AppCompatActivity implements RemoteResponse {
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private EditText input_password;
    private  EditText input_email;
    private EditText input_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);


        final EditText input_username= (EditText) findViewById(R.id.input_username);
                       input_email= (EditText) findViewById(R.id.input_email);
                       input_password= (EditText) findViewById(R.id.input_password);

                       input_confirm= (EditText) findViewById(R.id.input_confirm);
        Button btn_signup= (Button) findViewById(R.id.btn_signup);
        CheckBox view_password= (CheckBox) findViewById(R.id.view_password);

        //Show password check box
        view_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    input_password.setTransformationMethod(null);
                }
                else{

                    input_password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });


//when input password loses focus am checking if the password Entered is Correct
        input_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                String password=input_password.getText().toString();

            }
        });

//When the input confirm password loses focus am checking if the password match
        input_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }
            }
        });


        //if the sign up butten is clicked
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String full_name=input_username.getText().toString();
                String email=input_email.getText().toString();
                String password=input_password.getText().toString();
                String confirm=input_confirm.getText().toString();

                HashMap<String, String> user_data = new HashMap<>();
                user_data.put("full_name",full_name);
                     user_data.put("email",email);
                     user_data.put("password",password);
                     user_data.put("type","app");
                     if(isValidEmail(email)) {
                         if(isValidPassword(password)){
                             if(isConfirmMatchs(password,confirm)){
                                 if(!TextUtils.isEmpty(full_name)) {
                                     new RemoteTask(Actions.USER_LOGIN,user_data,Registor.this,getApplicationContext(),false).execute();
                                 } else {
                                     input_username.setError("this field cant be empty");
                                     return;
                 }
                 }
               }
             }
            }
        });
        FloatingActionButton fabLogin= (FloatingActionButton) findViewById(R.id.fab_login);
        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login =new Intent(Registor.this,UserLogin.class);
                Registor.this.startActivity(login);
            }
        });


    }
    public  boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            input_email.setError("this field cant be empty");
            return false;
        } else {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                return true;
            }
            else {
                input_email.setError("Email format not valid,must be on the format someone@example.com");
            return false;}
        }
    }
    public boolean isValidPassword(CharSequence password){
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        Boolean matches=matcher.matches();
        if (TextUtils.isEmpty(password)) {
            input_password.setError("this field cant be empty");
            return false;
        }
        else if (password.length() < 8) {
                input_password.setError("password must be at least 8 characters long");
            return false;
            }
            else if(!matches){
                input_password.setError("Password must contain at least one capital letter ,number and Character");
             return false;
            }
            else{
        return true;
            }
    }
    public boolean isConfirmMatchs(CharSequence password,CharSequence confirm){
        if(password.equals(confirm)){
            return true;
        }
        else if (TextUtils.isEmpty(confirm)) {
            input_confirm.setError("this field cant be empty");
            return false;
        }
        else {
            input_confirm.setError("password doesn't match");
            return false;
        }
    }

    @Override
    public void loginFinished(int id, Object result) {
        try
        {
            String j=result.toString();
            JSONObject jsonObject =new JSONObject(j);
            String action=jsonObject.getString("action");
            String user_id=jsonObject.getString("id");
            String profile=jsonObject.getString("profile");
            String type=jsonObject.getString("type");


           if(!profile.isEmpty()){
                if (profile.equals("not_exist")){
                    Intent profile_intent=new Intent(Registor.this,UserProfile.class);
                    profile_intent.putExtra("id",user_id);
                    profile_intent.putExtra("type",type);
                    Registor.this.startActivity(profile_intent);
                }else if(profile.equals("exist")){
                    Intent search_intent=new Intent(Registor.this, SearchActivity.class);
                    Registor.this.startActivity(search_intent);
                }
            }

        } catch (JSONException e) {
            Log.d("ab_log","obj "+e);
        }

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

    }

    @Override
    public void imageUploaded(int value) {

    }

    @Override
    public void myItems(Integer value,Object result) {

    }
}
