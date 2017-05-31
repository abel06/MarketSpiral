package com.example.abela.marketspiral;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;
import com.example.abela.marketspiral.Utility.ImageInfo;
import java.util.ArrayList;

public class test_activity extends AppCompatActivity {

    private ArrayList<ImageInfo> img_data= new ArrayList<>();
    private GridView gridView;
    private CustomImageAdapter adapter;
    static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_image_test_layout);
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
}
