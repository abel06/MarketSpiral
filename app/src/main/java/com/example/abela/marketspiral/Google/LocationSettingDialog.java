package com.example.abela.marketspiral.Google;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.abela.marketspiral.R;

public class LocationSettingDialog extends Service {
    private Context mContext;
    AlertDialog alertDialog;
    public LocationSettingDialog(Context context){
        mContext=context;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle(R.string.loc_dialog_title);
        String mssg=mContext.getString(R.string.app_name)+" "+mContext.getString(R.string.loc_dialog_message);
        alertDialogBuilder.setMessage(mssg);

        alertDialogBuilder.setPositiveButton(R.string.loc_dialog_positive_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myintent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(myintent);
                    }

                }
        ).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //((MainActivity)mContext).respond(null);
               // ((MainActivity)mContext).finish();
                Toast.makeText(mContext,"Unable to fetch location information ", Toast.LENGTH_LONG).show();
            }
        });
         alertDialog = alertDialogBuilder.create();
        if(!alertDialog.isShowing()){
        alertDialog.show();
        }
    }
 public boolean isDialogVisible(){
      if(alertDialog!=null){
       if(alertDialog.isShowing()){
         return true;
       }
       else {
           return false;
       }
      }
      return false;
  }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
