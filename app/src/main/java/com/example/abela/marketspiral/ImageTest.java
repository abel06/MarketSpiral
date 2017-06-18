package com.example.abela.marketspiral;

import android.graphics.BitmapFactory;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.misc.AsyncTask;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.ImageInfo;
import com.example.abela.marketspiral.Utility.ServerInfo;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Session;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
public class ImageTest extends AppCompatActivity {
    private ArrayList<ImageInfo> img_data= new ArrayList<>();
    private GridView gridView;
    private CustomImageAdapter adapter;
    static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_test);
        gridView = (GridView) findViewById(R.id.image_list);
        img_data = new ArrayList<>();
        adapter = new CustomImageAdapter(this,img_data);
        gridView.setAdapter(adapter);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);
            cursor.close();

            // Get the Image's file name
            String fileNameSegments[] = imgPath.split("/");
            img_data.add(new ImageInfo(fileNameSegments[fileNameSegments.length - 1].split("\\.")[0],imgPath));
            adapter.notifyDataSetChanged();

            // Put file name in Async Http Post Param which will used in Php web app
//            params

        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void browseImage(View view) {
       Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);


    }



    public void upload(View view) {
        if (!img_data.isEmpty()) {
//            params.put("tag",((EditText)findViewById(R.id.image_tag)).getText().toString());
            for(int i =0 ; i < img_data.size();++i) {
                LinearLayout ly = (LinearLayout) gridView.getChildAt(i);
                EditText tag = (EditText) ly.findViewById(R.id.editText3);
                Profile profile = Profile.getCurrentProfile();
                String fbid=profile.getId();
               // String twid= String.valueOf(Twitter.getSessionManager().getActiveSession().getUserId());
               // String gid=Auth.CREDENTIALS_API.getName();

                Calendar c = Calendar.getInstance();

                int day= c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                int hour=c.get(Calendar.HOUR);
                int minute=c.get(Calendar.MINUTE);
                int second=c.get(Calendar.SECOND);
                String name ="";
                String path = img_data.get(i).getPath();
                //here we can pass the login information so if its from facebook we will use fb id
             if(!fbid.isEmpty()){
                    name= fbid+"_"+year+""+month+""+day+""+hour+""+minute+""+second+"_"+img_data.get(i).getName();
                    uploadMultipart(path,name,tag.getText().toString(),fbid);

                }/*else if (!twid.isEmpty()){
                    name= twid+"_"+year+""+month+""+day+""+hour+""+minute+""+second+"_"+img_data.get(i).getName();
                    uploadMultipart(path,name);
                }
                else if (!gid.isEmpty()){
                    name= gid+"_"+year+""+month+""+day+""+hour+""+minute+""+second+"_"+img_data.get(i).getName();
                    uploadMultipart(path,name);
                }
                else  {

                }*/

            }
        }

    }

    public void uploadMultipart(String path, String name,String tag,String id) {
        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            String b = new MultipartUploadRequest(this, uploadId,ServerInfo.DB_URL+Actions.UPLOAD_IMAGE)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .addParameter("tag",tag)
                    .addParameter("id",id)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, "abela "+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
