package com.example.abela.marketspiral;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.abela.marketspiral.Utility.ImageInfo;
import java.util.ArrayList;

/**
 * Created by HaZe on 5/31/17.
 */

public class CustomImageAdapter extends BaseAdapter {

    private ArrayList<ImageInfo> images_info;
    private Context context;

    public CustomImageAdapter (Context context, ArrayList<ImageInfo> images_info){

        this.images_info = images_info;
        this.context = context;
    }


    public CustomImageAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return images_info.size();
    }

    @Override
    public Object getItem(int i) {
        return images_info.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

//        if (view == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.grid_item, null);

            ImageView imageView = (ImageView) gridView.findViewById(R.id.image_grid_item);

            Log.d("TEST", "position" + i+"image path" + images_info.get(i).getPath() + " image name " + images_info.get(i).getName());
            Bitmap bt = decodeSampledBitmapFromUri(images_info.get(i).getPath(), 200, 200);

            imageView.setImageBitmap(bt);
//            imageView.setImageResource(R.drawable.com_facebook_favicon_blue);
//        }else{
//            gridView = view;
//        }

        return gridView;
    }


    private Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }

        return inSampleSize;
    }

}
