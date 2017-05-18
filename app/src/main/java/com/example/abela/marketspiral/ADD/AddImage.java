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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.abela.marketspiral.R;

public class AddImage extends AppCompatActivity {
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
        String title=intent.getString("title");
        String category=intent.getString("category");
        String buildup=intent.getString("buildup");
        String price=intent.getString("price");
        String bedroom=intent.getString("bedroom");
        String description=intent.getString("description");
        double lat=intent.getDouble("lat");
        double lng=intent.getDouble("lng");
        Log.d("ab",title+category+" "+buildup+" "+price+" "+bedroom+" "+description+" "+lat+" "+lng);

        imageView = (ImageView) findViewById(R.id.imageView);
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
                if (filePath != null) {
                    imageUpload(filePath);
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
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

    private void imageUpload(final String imagePath) {

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
}
