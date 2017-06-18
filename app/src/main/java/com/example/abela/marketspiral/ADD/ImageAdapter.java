package com.example.abela.marketspiral.ADD;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.ImageInfo;
import com.example.abela.marketspiral.interfaces.ItemSelect;

import java.util.ArrayList;

/**
 * Created by Abela on 5/10/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<com.example.abela.marketspiral.ADD.ImageAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ImageInfo> imageInfoArrayList;
    private AddImage addImage ;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public EditText tag_et;
        public ImageView thumbnail,overflow;

        public MyViewHolder(View view) {
            super(view);

            tag_et= (EditText) view.findViewById(R.id.tag_et);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_iv);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }


    public ImageAdapter(Context mContext, ArrayList<ImageInfo> imageInfoArrayList, AddImage addImage) {
        this.addImage=addImage;
        this.mContext = mContext;
        this.imageInfoArrayList= imageInfoArrayList;
    }

    @Override
    public com.example.abela.marketspiral.ADD.ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);

        return new com.example.abela.marketspiral.ADD.ImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final com.example.abela.marketspiral.ADD.ImageAdapter.MyViewHolder holder, final int position) {
        final ImageInfo imageInfo= imageInfoArrayList.get(position);



        //  holder.count.setText(home.getNumOfSongs() + " songs");

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent descriptionActivity =new Intent(mContext,DescriptionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                descriptionActivity.putExtra("home",home);
                v.getContext().startActivity(descriptionActivity);
                Log.d("ab","home click "+home.getImage().getImageAtIndex(0));*/

               ItemSelect itemSelect =(ItemSelect)addImage;
               itemSelect.onItemSelect(position);

            }
        });
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow,position);
            }
        });
      /*  if(home.getImage().getImagesCount()>0){
            Log.d("ab_log","url"+home.getImage().getImageAtIndex(0).getUrl());
            Glide.with(mContext).load(home.getImage().getImageAtIndex(0).getUrl()).into(holder.thumbnail);
        }


*/


        Glide.with(mContext).load(imageInfo.getPath()).into(holder.thumbnail);
    }
      public ImageInfo getItem (int index){
         return imageInfoArrayList.get(index);

      }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(addImage, view);
        try{  MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
       popup.show();}catch (Exception  e){
           Log.d("ab_log",""+e);
       }
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
           int position;
        public MyMenuItemClickListener(int position) {
            this.position=position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_remove:
                    Toast.makeText(mContext, "remove", Toast.LENGTH_SHORT).show();
                    ItemSelect itemSelect=(ItemSelect)addImage;
                    itemSelect.remove(position);
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return imageInfoArrayList.size();
    }
}
