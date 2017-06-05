package com.example.abela.marketspiral;

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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.misc.AsyncTask;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.ImageInfo;
import com.example.abela.marketspiral.Utility.ServerInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import java.io.ByteArrayOutputStream;
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

    public void upload(View view) {

        if (!img_data.isEmpty()) {

//            params.put("tag",((EditText)findViewById(R.id.image_tag)).getText().toString());
            for(int i =0 ; i < img_data.size();++i){

                LinearLayout ly = (LinearLayout) gridView.getChildAt(i);
                EditText tag = (EditText) ly.findViewById(R.id.editText3);
                Log.d("TEST","Valore tag :" + tag.getText().toString());


//                encodeImagetoString(i);
            }
//                    imageUpload();
        } else {
            Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
        }


    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString(final int position) {

        new AsyncTask<Integer, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Integer... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                Bitmap bitmap = BitmapFactory.decodeFile(img_data.get(params[0]).getPath(),
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                return Base64.encodeToString(byte_arr, 0);
            }

            @Override
            protected void onPostExecute(String msg) {
                RequestParams params = new RequestParams();
                // Put converted Image string into Async Http Post param
                params.put("image", msg);
                params.put("filename",img_data.get(position).getName());
                // Trigger Image upload
                makeHTTPCall(params);

            }
        }.execute(position, null, null);
    }

    public void makeHTTPCall(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(ServerInfo.DB_URL+ Actions.UPLOAD_IMAGE,
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                        Toast.makeText(getApplicationContext(), "DONE",
                                Toast.LENGTH_SHORT).show();
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


}
