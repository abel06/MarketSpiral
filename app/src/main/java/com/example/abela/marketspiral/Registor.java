package com.example.abela.marketspiral;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registor extends AppCompatActivity implements RemoteResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registor);
        final EditText input_username= (EditText) findViewById(R.id.input_username);
        final EditText input_email= (EditText) findViewById(R.id.input_email);
        final EditText input_password= (EditText) findViewById(R.id.input_password);
        final EditText input_confirm= (EditText) findViewById(R.id.input_confirm);
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
                 Pattern pattern;
                 Matcher matcher;
                 final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
                 pattern = Pattern.compile(PASSWORD_PATTERN);
                 matcher = pattern.matcher(password);
                 Boolean matches=matcher.matches();
                 if (!hasFocus) {
                     if (password.length() < 8) {
                         input_password.setError("password must be at least 8 characters long");
                     }
                     else if(!matches){
                      input_password.setError("Password must contain at least one capital letter ,number and Character");

                    }
                 }
             }
         });

//When the input confirm password loses focus am checking if the password match
input_confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                                           @Override
                                           public void onFocusChange(View v, boolean hasFocus) {
                                               if (!hasFocus) {
                                                   String password=input_password.getText().toString();
                                                   String confirm=input_confirm.getText().toString();
                                                   if(!password.equals(confirm)){
                                                       input_confirm.setError("password doesn't match");

                                                   }
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

                if(TextUtils.isEmpty(full_name)) {
                   input_username.setError("this field cant be empty");
                    return;
                }
               else if(TextUtils.isEmpty(email)) {
                    input_email.setError("this field cant be empty");
                    return;
                }
                else if(TextUtils.isEmpty(password)) {
                    input_password.setError("this field cant be empty");
                    return;
                }
                else if(TextUtils.isEmpty(confirm)) {
                    input_confirm.setError("this field cant be empty");
                    return;
                }
                else{
                    new RemoteTask(Actions.USER_REGISTRATION,user_data,Registor.this,false).execute();
                }

            }
        });
        FloatingActionButton fabLogin= (FloatingActionButton) findViewById(R.id.fab_login);
        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login =new Intent(Registor.this,Login.class);
                Registor.this.startActivity(login);
            }
        });


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

    }


    public class CheckingPassword {

        public void main(String[] args) {
            Scanner input = new Scanner(System.in);
            System.out.print("Please enter a Password: ");
            String password = input.next();
            if (isValid(password)) {
                System.out.println("Valid Password");
            } else {
                System.out.println("Invalid Password");
            }
        }

        public  boolean isValid(String password) {
            //return true if and only if password:
            //1. have at least eight characters.
            //2. consists of only letters and digits.
            //3. must contain at least two digits.
            if (password.length() < 8) {
                return false;
            } else {
                char c;
                int count = 0;
                for (int i = 0; i < password.length() - 1; i++) {
                    c = password.charAt(i);
                    if (!Character.isLetterOrDigit(c)) {
                        return false;
                    } else if (Character.isDigit(c)) {
                        count++;
                        if (count < 2)   {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
}
