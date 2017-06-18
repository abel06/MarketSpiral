package com.example.abela.marketspiral.ADD;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.ImageInfo;
import com.example.abela.marketspiral.Utility.ServerInfo;
import com.example.abela.marketspiral.Utility.Utility;
import com.example.abela.marketspiral.interfaces.ItemSelect;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
public class AddImage extends AppCompatActivity implements ItemSelect {
  ArrayList<ImageInfo> img_data= new ArrayList<>();
    private GridView gridView;
   // private CustomImageAdapter adapter;

    static final int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_CAMERA = 0;

    String home_id="";
    int completed=0;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    Intent galleryIntent;

    private boolean update_image=false;
    private int update_code=-1;
    private String userChoosenTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        //gridView = (GridView) findViewById(R.id.image_list);
        img_data = new ArrayList<>();

        //adapter = new CustomImageAdapter(this,img_data);
       // gridView.setAdapter(adapter);

        Intent intent=getIntent();
        home_id=intent.getStringExtra("home_id");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new ImageAdapter(getApplicationContext(), img_data,AddImage.this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            // Log.d("ab_log",""+requestCode);

            if (requestCode == PICK_IMAGE_REQUEST) {
                updateAdapter(data);

            } else if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                    updateAdapter(data);
                } catch (FileNotFoundException e) {
                    Log.d("ab_log",""+e);
                } catch (IOException e) {
                    Log.d("ab_log",""+e);
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        }else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }


    }
//-----------------------------------------------------------------
     public void browseImage(View view) {
    final CharSequence[] items = { "Take Photo", "Choose from Library",
            "Cancel" };

    AlertDialog.Builder builder = new AlertDialog.Builder(AddImage.this);
    builder.setTitle("Add Photo!");
    builder.setItems(items, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int item) {
            boolean result=Utility.checkPermission(AddImage.this);

            if (items[item].equals("Take Photo")) {
                userChoosenTask ="Take Photo";
                if(result)
                    cameraIntent();

            } else if (items[item].equals("Choose from Library")) {
                userChoosenTask ="Choose from Library";
                if(result)
                { galleryIntent();
                }

            } else if (items[item].equals("Cancel")) {
                Toast.makeText(getApplicationContext(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        }
    });
    builder.show();
}
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library")){
                       galleryIntent();
                } else {
                        Toast.makeText(this, "Permission denied u can't use some functionality like adding image unless you grant permission for camera and storage",
                                Toast.LENGTH_LONG).show();
                }
                break;
        }
        }
    }
    private void cameraIntent()
    {    update_image=false;
        update_code=-1;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {       update_image=false;
        update_code=-1;
        galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);

    }
     public void updateAdapter(Intent data){
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
        if(imgPath.endsWith(".jpeg")||imgPath.endsWith(".jpg")){
      if(update_image==false){

          img_data.add(new ImageInfo(fileNameSegments[fileNameSegments.length - 1].split("\\.")[0],imgPath));}
      else {
          img_data.remove(update_code);
          img_data.add(update_code,new ImageInfo(fileNameSegments[fileNameSegments.length - 1].split("\\.")[0],imgPath));
      }

      adapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getApplicationContext(),"The image must be either on Jpeg or jpg format",Toast.LENGTH_LONG).show();
        }

  }



//---------------------------------------------------------------------------------------------
    public void upload(View view) {
        if (!img_data.isEmpty()) {
//            params.put("tag",((EditText)findViewById(R.id.image_tag)).getText().toString());
            for(int i =0 ; i < img_data.size();++i) {
                LinearLayout ly = (LinearLayout) recyclerView.getChildAt(i);
                EditText tag_et = (EditText) ly.findViewById(R.id.tag_et);

                String tag=tag_et.getText().toString();
                Calendar c = Calendar.getInstance();

                int day= c.get(Calendar.DAY_OF_MONTH);
                int month=c.get(Calendar.MONTH);
                int year=c.get(Calendar.YEAR);
                int hour=c.get(Calendar.HOUR);
                int minute=c.get(Calendar.MINUTE);
                int second=c.get(Calendar.SECOND);
                String name ="";
                String path = img_data.get(i).getPath();

                 Log.d("ab_log","tag"+tag);
                //here we can pass the login information so if its from facebook we will use fb id

                    name= home_id+"_"+"_"+year+""+month+""+day+""+hour+""+minute+""+second+"_"+img_data.get(i).getName();
                    uploadMultipart(path,name,tag,home_id);


            }
        }

    }

    public void uploadMultipart(String path, String name, String tag, String homeId) {

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            String b = new MultipartUploadRequest(this, uploadId,ServerInfo.DB_URL+ServerInfo.UPLOAD_IMAGE)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .addParameter("tag",tag)
                    .addParameter("home_id",homeId)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                            Log.d("ab_log","completed");
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, Exception exception) {
                            Log.d("ab_log","completed");
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                           // img_data.remove(completed);
                           // adapter.notifyDataSetChanged();
                           completed=completed+1;
                            if(completed==img_data.size()){
                                Intent myitemsintent=new Intent(AddImage.this, MyItems.class);
                                startActivity(myitemsintent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.d("ab_log","completed");
                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, "abela "+exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelect(int position) {
        Log.d("ab_log","position"+position);
    // adapter.
        update_image=true;
        update_code=position;
        galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void remove(int position) {
        Log.d("ab_log","position"+position);
        img_data.remove(position);
        adapter.notifyDataSetChanged();
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    //========================================================================


}
