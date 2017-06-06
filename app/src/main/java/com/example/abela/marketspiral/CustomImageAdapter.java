package com.example.abela.marketspiral;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.abela.marketspiral.Utility.ImageInfo;

import java.util.ArrayList;

/**
 * Created by HaZe on 5/31/17.
 *
 */

/**This image implements a base adapter for our image grid*/
public class CustomImageAdapter extends BaseAdapter {

    /**Array of imageinfo used to store information about image data*/
    private ArrayList<ImageInfo> images_info;

    private Context context;

    /**Index of last rendered image needed to prevent performance issues*/
    private int last_index = 0;

    public CustomImageAdapter(Context context, ArrayList<ImageInfo> images_info){

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

        if (view == null) {
            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.grid_item, null);

            ImageView imageView = (ImageView) gridView.findViewById(R.id.image_grid_item);
            Bitmap bt = decodeSampledBitmapFromUri(images_info.get(i).getPath(), 200, 200);

            imageView.setImageBitmap(bt);
            last_index = i;
        }else{

            /**In case the grid have already been populated in case of a refresh or a value inside the input text the images don't have to be
             * reload inside the grid, in case a new image is added this HAVE to be rendered*/
            gridView = view;

            //TODO fix perforance issue when scrolling and using keyboard (while editing text)
            if(i > last_index) {
            ImageView imageView = (ImageView) gridView.findViewById(R.id.image_grid_item);
            Bitmap bt = decodeSampledBitmapFromUri(images_info.get(i).getPath(), 200, 200);
            imageView.setImageBitmap(bt);
                last_index = i;
            }
        }

        return gridView;
    }



/**This function is used to resize the images*/
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

    /**This is a utility function used to mantain the same aspect ratio of the original image*/
    //TODO Insert this function inside utility package

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
