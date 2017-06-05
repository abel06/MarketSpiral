package com.example.abela.marketspiral.ADD;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.misc.AsyncTask;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.EncodeImageToString;
import com.example.abela.marketspiral.Utility.ServerInfo;
import com.example.abela.marketspiral.interfaces.RemoteResponse;
import com.loopj.android.http.*;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class AddImage extends AppCompatActivity implements RemoteResponse {
    private ImageView imageView;
    private Button btnChoose, btnUpload;
    private ProgressBar progressBar;
    RequestParams params = new RequestParams();

    public static String BASE_URL = "http://192.168.43.137:80/homespiral/";
    static final int PICK_IMAGE_REQUEST = 1;
    String filePath;
    private Object img_data;
    private String imgPath;
    private String fileName;
    private Bitmap bitmap;
    private String encodedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        Bundle intent =getIntent().getExtras();
        params.put("title",intent.getString("title"));
        params.put("category",intent.getString("category"));
        params.put("buildup",intent.getString("buildup"));
        params.put("price",intent.getString("price"));
        params.put("bedroom",intent.getString("bedroom"));
        params.put("description",intent.getString("description"));
        params.put("lat",intent.getDouble("lat"));
        params.put("lng",intent.getDouble("lng"));
        params.put("date",intent.getString("date"));

//        Log.d("ab",title+category+" "+buildup+" "+price+" "+bedroom+" "+description+" "+lat+" "+lng);

        imageView = (ImageView) findViewById(R.id.imageView);
        btnChoose = (Button) findViewById(R.id.button_choose);
        btnUpload = (Button) findViewById(R.id.button_upload);
        img_data= new HashMap<String,String>();
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgPath != null) {
                    params.put("tag",((EditText)findViewById(R.id.image_tag)).getText().toString());
                    encodeImagetoString();
//                    imageUpload();
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void imageUpload() {

        new EncodeImageToString(this,img_data,imgPath).execute();

    }



    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {

                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                // Trigger Image upload
                makeHTTPCall();

            }
        }.execute(null, null, null);
    }



    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
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
            imgPath = cursor.getString(columnIndex);
            cursor.close();
            ImageView imgView = (ImageView) findViewById(R.id.imageView);
            // Set the Image in ImageView
            imgView.setImageBitmap(BitmapFactory
                    .decodeFile(imgPath));
            // Get the Image's file name
            String fileNameSegments[] = imgPath.split("/");
            fileName = fileNameSegments[fileNameSegments.length - 1].split("\\.")[0];

            // Put file name in Async Http Post Param which will used in Php web app
//            params
            params.put("filename", fileName);
            ((HashMap<String,String>)img_data).put("filename",fileName);

        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }



    }

    public void makeHTTPCall() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ServerInfo.DB_URL+ Actions.UPLOAD_IMAGE,
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                        Toast.makeText(getApplicationContext(), "DONE",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                        // When Http response code is '404'
                        Toast.makeText(
                                getApplicationContext(),
                                "Error Occured n Most Common Error: n1. Device not connected to Internetn2. Web App is not deployed in App servern3. App server is not runningn HTTP Status code : ", Toast.LENGTH_LONG)
                                .show();
                    }

                });
    }



//    private String getPath(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column_index);
//        cursor.close();
//        ImageView imgView = (ImageView) findViewById(R.id.imageView);
//        // Set the Image in ImageView after decoding the String
//        imgView.setImageBitmap(BitmapFactory
//                .decodeFile(result));
//
//        return result;
//    }

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
    public void itemAdded(int id) {

    }

    @Override
    public void itemRemoved(int id) {

    }

    @Override
    public void searchItem(int id) {

    }

    @Override
    public void addItem(int id) {

    }

    @Override
    public void imageUploaded(int value) {

        Toast.makeText(getApplicationContext(),"Return value" + value,Toast.LENGTH_LONG).show();

    }
}
