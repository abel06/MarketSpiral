package com.example.abela.marketspiral.Google;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by abela on 7/13/2016.
 */
public class PlayServiceCheck extends Service {
    private GoogleApiClient mGoogleApiClient;

    private final Context mContext;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    //boolean isPlayServiceOk=false;
    public PlayServiceCheck(Context context) {
        this.mContext = context;
    }
    public boolean isPlayServiceOk() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(mContext);
        if (result != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(result)) {
                googleApiAvailability.getErrorDialog((Activity) mContext, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(mContext, "This device is not supported", Toast.LENGTH_LONG).show();
            }
            return false;
        } else {
            //Toast.makeText(getApplicationContext(), "CheckPlayServices() Ok", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
