package com.example.abela.marketspiral.ADD;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
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

import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class AddImage extends AppCompatActivity implements  RemoteResponse{
    private ImageView imageView;
    private Button btnChoose, btnUpload;
    private ProgressBar progressBar;

    public static String BASE_URL = "http://192.168.43.137:80/homespiral/";
    static final int PICK_IMAGE_REQUEST = 1;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        Bundle intent =getIntent().getExtras();
        final String title=intent.getString("title");
        final String category=intent.getString("category");
        final String buildup=intent.getString("buildup");
        final String price=intent.getString("price");
        final String bedroom=intent.getString("bedroom");
        final String description=intent.getString("description");
        final double lat=intent.getDouble("lat");
        final double lng=intent.getDouble("lng");
        Log.d("ab",title+category+" "+buildup+" "+price+" "+bedroom+" "+description+" "+lat+" "+lng);

        imageView = (ImageView) findViewById(R.id.imageView);
        final EditText tag_et = (EditText) findViewById(R.id.image_tag);
        btnChoose = (Button) findViewById(R.id.button_choose);
        btnUpload = (Button) findViewById(R.id.button_upload);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageBrowse();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String tag= tag_et.getText().toString();
                if(tag.matches("")){
                  tag_et.setError("Tag is Required");
                }
               else {
                    if (filePath != null) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("title", title);
                            jsonObject.put("category", category);
                            jsonObject.put("buildup", buildup);
                            jsonObject.put("price", price);
                            jsonObject.put("bedroom", bedroom);
                            jsonObject.put("description", description);
                            jsonObject.put("lat", lat);
                            jsonObject.put("lng", lng);
                            jsonObject.put("image", filePath);
                            jsonObject.put("tag",tag);
                            upload(jsonObject);

                        } catch (JSONException e) {
                            Log.d("ab", "" + e);
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    private void imageBrowse() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST){
                Uri picUri = data.getData();

                filePath = getPath(picUri);

                Log.d("ab", picUri.toString());
                Log.d("ab", filePath);

                imageView.setImageURI(picUri);

            }

        }

    }

    private void upload(Object obj) {
        new RemoteTask(Actions.ADD_ITEM,obj,this,getApplicationContext(), false).execute();
       // Log.d("ab","objects upload"+obj);
    }

    private String getPath(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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
}
