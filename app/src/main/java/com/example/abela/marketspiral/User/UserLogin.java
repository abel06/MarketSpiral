package com.example.abela.marketspiral.User;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.abela.marketspiral.Activities.SearchActivity;
import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Core.Session;
import com.example.abela.marketspiral.Google.PlayServiceCheck;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

public class UserLogin extends AppCompatActivity implements RemoteResponse,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private Object user_data;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    private TwitterLoginButton twitterLoginButton;
    TwitterSession twitter_session;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount google_session;
    private static final  int RC_SIGN_IN = 0;
    private static final String TWITTER_KEY="zulglQfS27mqsgLoL1QgBaE11";
    private static final String TWITTER_SECRETE="aL8ZJ3vOVSx487lJgumJCBOht862uBc9OuOZ2x44QkQpB2sJzq";
    private String result ;

    private Session session;
    private User user;
    String type="";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig twitterAuthConfig=new TwitterAuthConfig(TWITTER_KEY,TWITTER_SECRETE);
        Fabric.with(this,new Twitter(twitterAuthConfig));
        setContentView(R.layout.activity_user_login);


        session =new Session(getApplicationContext());
        type=session.getType();

        Log.d("ab_log","login");

        FloatingActionButton fabSignup= (FloatingActionButton) findViewById(R.id.fab_signup);
        fabSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp =new Intent(UserLogin.this,Registor.class);
                UserLogin.this.startActivity(signUp);
            }
        });
        //----------------------------------------------------------------------
        final TextInputEditText input_usernameTv= (TextInputEditText) findViewById(R.id.input_username);
        final TextInputEditText input_passwordTv= (TextInputEditText) findViewById(R.id.input_password);
        Button loginBtn= (Button) findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> user_data=new HashMap<String, String>();
                //app login
                user_data.put("email",input_usernameTv.getText().toString());
                user_data.put("password",input_passwordTv.getText().toString());
                user_data.put("type","app");
                login(user_data);


                if(isUserExist()){

                    if(isProfileExist()){
                        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                        // nextActivity();
                    }else {
                        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                        editProfile();
                    }
                }
                else {
                    login(user_data);

                }
            }
        });
        //----------------------------------------------------------------------
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);


        callbackManager = CallbackManager.Factory.create();
        FacebookCallback<LoginResult> facebookCallback= new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Profile profile=Profile.getCurrentProfile();
                HashMap<String,String> user_data=new HashMap<>();
                user_data.put("external_id",profile.getId());
                user_data.put("type","facebook");
                user_data.put("full_name",profile.getName());

                if(isUserExist()){

                    if(isProfileExist()){
                        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                        nextActivity();
                    }else {
                        Toast.makeText(getApplicationContext(), "edit...", Toast.LENGTH_SHORT).show();
                        editProfile();
                    }
                }
                else {
                    login(user_data);
                }

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Loggin canceled...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "Sorry unable to login...", Toast.LENGTH_SHORT).show();
            }
        };  //facebook login
        loginButton.registerCallback(callbackManager, facebookCallback);


        //----------------------------------------------------------------------
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitter_session = result.data;
                HashMap<String, String> user_data = new HashMap<>();

                user_data.put("external_id",String.valueOf(twitter_session.getUserId()));
                user_data.put("full_name",twitter_session.getUserName());
                user_data.put("type","twitter");


                if(isUserExist()){
                    if(type.equals("twitter")){
                        if(isProfileExist()){
                            Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                            nextActivity();

                        }else {
                            Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                            editProfile();
                        }
                    }
                    else { login(user_data);}
                }
                else {
                    login(user_data);
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("ab_log", "Login with Twitter failure"+exception);
            }
        });                         //twitter login
        //----------------------------------------------------------------------
        PlayServiceCheck playServiceCheck = new PlayServiceCheck(getApplicationContext());
        if (playServiceCheck.isPlayServiceOk()) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(
                new View.OnClickListener() {                                              //google login
                    @Override
                    public void onClick(View v) {
                        if(mGoogleApiClient!=null){
                            try{
                                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                                startActivityForResult(signInIntent,RC_SIGN_IN);

                            }catch (Exception e){
                                Log.d("ab",""+e);
                            }
                        }
                    }
                });
        //----------------------------------------------------------------------
    }

    synchronized void buildGoogleApiClient() {

        try{
            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    requestScopes(new Scope(Scopes.PLUS_LOGIN)).
                    requestEmail().
                    build();

            mGoogleApiClient = new GoogleApiClient.Builder(this).
                    enableAutoManage(this, this).
                    addApi(Auth.GOOGLE_SIGN_IN_API, gso).
                    addApi(Plus.API).
                    build();
        }catch (Exception e){
        }
    }
    public void login(HashMap<String,String> data) {

        new RemoteTask(Actions.USER_LOGIN,data, this,getApplicationContext(),false).execute();

    }


    private void nextActivity(){
        Intent search = new Intent(UserLogin.this, SearchActivity.class);
        search.putExtra("result",result);
        startActivity(search);
    }
    private boolean isUserExist(){
        Session session =new Session(getApplicationContext());
        if(session!=null){
            String user_id= session.getUserID();
            String type=session.getType();
            if(!user_id.isEmpty()){
                if(!type.isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isProfileExist(){
        Session session =new Session(getApplicationContext());

        Log.d("ab_log","isprofile"+session.getProfile());
        if(session!=null){
            if(!session.getProfile().isEmpty()){
                if (session.getProfile().equals("exist")){
                    return true;

                }else if(session.getProfile().equals("not_exist")){
                    return false;
                }
            }
        }
        return false;
    }
    private void editProfile(){
        User user =new User(result);
        user.execute();
        if(user!=null){
            String user_id=user.getUser_id();
            String type=user.getType();
            Intent profile_intent=new Intent(UserLogin.this,UserProfile.class);
            profile_intent.putExtra("id",user_id);
            profile_intent.putExtra("type",type);
            UserLogin.this.startActivity(profile_intent);}
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        if(FacebookSdk.isFacebookRequestCode(requestCode)){
            callbackManager.onActivityResult(requestCode, responseCode, intent);
        }
        else if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);

                if (result.isSuccess()) {
                    google_session = result.getSignInAccount();

                    HashMap<String,String> user_data= new HashMap<>();
                    String full_name =google_session.getDisplayName();
                    String external_id=google_session.getId();
                    String email = google_session.getEmail();

                    user_data.put("external_id",external_id);
                    user_data.put("full_name", full_name);
                    user_data.put("email",email);
                    user_data.put("type","google");

                    if(isUserExist()){
                        if(type.equals("google")){
                            if(isProfileExist()){
                                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                                nextActivity();
                            }else {
                                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                                editProfile();
                            }
                        }
                        else { login(user_data);}
                    }
                    else {
                        login(user_data);
                    }

                }
            }catch (Exception e){
                Log.d("ab","result "+e);
            }

        } else if(requestCode==TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE){
            twitterLoginButton.onActivityResult(requestCode, responseCode, intent);

        }
    }

    @Override
    public void loginFinished(int id, Object object) {
        Log.d("ab_log","result"+object);

        if(object!=null){

            result=object.toString();
            if(isProfileExist()){
                nextActivity();
            }else {
                editProfile();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Unable to reach Homespiral server please check you internet",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        if(isUserExist()){
            if(isProfileExist()){
                nextActivity();
            }
            else {
                editProfile();
            }
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
