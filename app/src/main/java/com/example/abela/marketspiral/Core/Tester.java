package com.example.abela.marketspiral.Core;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.abela.marketspiral.CategoryActivity;
import com.example.abela.marketspiral.ImageUpload;
import com.example.abela.marketspiral.Login;
import com.example.abela.marketspiral.MainActivity;
import com.example.abela.marketspiral.R;

import com.example.abela.marketspiral.Registor;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class Tester extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "v5oqtyAcoN3vCwsYTwjcFg9JW";
    private static final String TWITTER_SECRET = "Lb0rVavootrruVY5ZVBu088uwCBGEbE6pmF5u4RKSelOSfKoCL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_tester);
        Button search= (Button) findViewById(R.id.searchbtn);
        Button descritpion = (Button) findViewById(R.id.descriptionbtn);
        Button map= (Button) findViewById(R.id.mapbtn);
        Button main = (Button) findViewById(R.id.mainbtn);
        Button login = (Button) findViewById(R.id.loginTbtn);
        Button registorbtn = (Button) findViewById(R.id.signinTbtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  searchIntent = new Intent(Tester.this, SearchActivity.class);
                //myIntent.putStringArrayListExtra("backFetchList", backFetchList);
                Tester.this.startActivity( searchIntent);
            }
        });
        descritpion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent descIntent = new Intent(Tester.this, DescriptionActivity.class);
                //myIntent.putStringArrayListExtra("backFetchList", backFetchList);
                Tester.this.startActivity(descIntent);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Tester.this, MapsActivity.class);
                //myIntent.putStringArrayListExtra("backFetchList", backFetchList);
                Tester.this.startActivity(mapIntent);
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Tester.this, ImageUpload.class);
                //myIntent.putStringArrayListExtra("backFetchList", backFetchList);
                Tester.this.startActivity(mainIntent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(Tester.this, Login.class);
                //myIntent.putStringArrayListExtra("backFetchList", backFetchList);

                PackageInfo info;
                try {
                    info = getPackageManager().getPackageInfo("com.example.abela.marketspiral", PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md;
                        md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        String something = new String(Base64.encode(md.digest(), 0));
                        //String something = new String(Base64.encodeBytes(md.digest()));
                        Log.d("ab", something);
                    }
                } catch (PackageManager.NameNotFoundException e1) {
                    Log.d("ab", e1.toString());
                } catch (NoSuchAlgorithmException e) {
                    Log.d("ab", e.toString());
                } catch (Exception e) {
                    Log.d("ab", e.toString());
                }





                Tester.this.startActivity(loginIntent);
            }
        });
        registorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(Tester.this, Registor.class);
                //myIntent.putStringArrayListExtra("backFetchList", backFetchList);
                Tester.this.startActivity(mainIntent);
            }
        });


         //new RemoteTask(Actions.USER_LOGIN,"abel", (RemoteResponse) this).execute();
    }
}
