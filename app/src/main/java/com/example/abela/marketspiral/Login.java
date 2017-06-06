package com.example.abela.marketspiral;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.abela.marketspiral.ADD.AddItem;
import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Core.SearchActivity;
import com.example.abela.marketspiral.Google.PlayServiceCheck;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import com.facebook.AccessToken;
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
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import java.util.HashMap;

public class Login extends AppCompatActivity implements RemoteResponse, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final int RC_SIGN_IN = 0;
    Context mContext;

    private Object user_data;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private CallbackManager callbackManager;
    private TwitterLoginButton twitterLoginButton;
    TwitterSession twitter_session;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount google_session;


    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
          //  nextActivity();
        }
        @Override
        public void onCancel() {        }
        @Override
        public void onError(FacebookException e) {      }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
   FloatingActionButton fabSignup= (FloatingActionButton) findViewById(R.id.fab_signup);
        fabSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent signUp =new Intent(Login.this,Registor.class);
                Login.this.startActivity(signUp);
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
    mContext=getApplicationContext();


        //----------------------------------------------------------

      //---------------------------------------------------------------------------
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                updateWithToken(newToken);
            }
        };
        updateWithToken(AccessToken.getCurrentAccessToken());
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
              //  nextActivity();
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();    }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Loggin canceled...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "Sorry unable to login...", Toast.LENGTH_SHORT).show();
            }
        };
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);

      //----------------------------------------------------------------------
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                  twitter_session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
              //  String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                ///Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                nextActivity();
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
//==============================================================================
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        //------------------------------------------------------------------
        PlayServiceCheck playServiceCheck = new PlayServiceCheck(getApplicationContext());
        if (playServiceCheck.isPlayServiceOk()) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mGoogleApiClient!=null){
                    try{
                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent,RC_SIGN_IN);
                    }catch (Exception e){
                        Log.d("ab",""+e);
                    }

                    //
                }
            }
        });

    }
    private void updateWithToken(AccessToken currentAccessToken) {
        twitter_session= Twitter.getInstance().core.getSessionManager().getActiveSession();


        int SPLASH_TIME_OUT=1000;
        if (currentAccessToken != null|twitter_session!=null|google_session!=null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                  nextActivity();

                    finish();
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    //finish();
                }
            }, SPLASH_TIME_OUT);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
       // nextActivity();
    }
    private void nextActivity(){
        //if(profile != null){
//            Intent search = new Intent(Login.this, ImageTest.class);

//        startActivity(search);
       // }
    }
    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);
        Log.d("ab","result "+requestCode);
        //twitter Login
        twitterLoginButton.onActivityResult(requestCode, responseCode, intent);

        //google Login
        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
                Log.d("ab","result "+result);
            if (result.isSuccess()) {
               google_session = result.getSignInAccount();

                // Get account information
                HashMap<String,String> collected_data= new HashMap<>();


                String full_name =google_session.getDisplayName();
                String email = google_session.getEmail();


                collected_data.put("full_name", full_name);
                collected_data.put("email",email);

                user_data = collected_data;

                new RemoteTask(Actions.USER_REGISTRATION,user_data,this,mContext,true).execute();

            }
    }catch (Exception e){
                Log.d("ab","result "+e);
            }

        }

    }
    public void login(HashMap<String,String> data) {

        new RemoteTask(Actions.USER_LOGIN,data,this,mContext,false).execute();
    }
    @Override
    public void loginFinished(int value) {

        nextActivity();

    }

    @Override
    public void registerFinished(int value, boolean externalService) {

        if(externalService && value > 0){
            nextActivity();
        }else if ( value == 0) {
            Toast.makeText(getApplicationContext(), "Registration done, please go ahead and login with the credentials", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
        }

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

    @Override
    public void addLocation(int return_state) {

    }

    @Override
    public void addItem(int id) {

    }

    @Override
    public void imageUploaded(int value) {


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
