package com.example.abela.marketspiral.ADD;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.abela.marketspiral.Activities.DescriptionActivity;
import com.example.abela.marketspiral.Decode.Home;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.interfaces.ItemSelect;

import java.util.ArrayList;

/**
 * Created by Abela on 5/10/2017.
 */

public class MyItemsAdapter extends RecyclerView.Adapter<MyItemsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Home> homeList;
    private Activity activity;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title_tv, price_tv,date_tv,owner_tv;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title_tv = (TextView) view.findViewById(R.id.title_home_tv);
            price_tv = (TextView) view.findViewById(R.id.price_tv);
           // owner_tv= (TextView) view.findViewById(R.id.owner_name_tv);
            date_tv=(TextView)view.findViewById(R.id.date_tv);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_iv);
            overflow= (ImageView) view.findViewById(R.id.overflow);

        }
    }


    public MyItemsAdapter(Context mContext, ArrayList<Home> homeList,Activity activity) {
        this.mContext = mContext;
        this.homeList= homeList;
        this.activity=activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Home home= homeList.get(position);

        holder.title_tv.setText(home.getTitle());
        holder.price_tv.setText(""+home.getPrice()+" $");
       // holder.owner_tv.setText(""+home.getOwner().getName());
        holder.date_tv.setText(""+home.getDate());
        //  holder.count.setText(home.getNumOfSongs() + " songs");

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent descriptionActivity =new Intent(mContext,DescriptionActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                descriptionActivity.putExtra("home",home);
                v.getContext().startActivity(descriptionActivity);
                Log.d("ab","home click "+home.getImage().getImageAtIndex(0));
            }
        });

        if(home.getImage().getImagesCount()>0){
            Log.d("ab_log","url"+home.getImage().getImageAtIndex(0).getUrl());
            Glide.with(mContext).load(home.getImage().getImageAtIndex(0).getUrl()).into(holder.thumbnail);
        }

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow,position);
            }
        });


    }

    private void showPopupMenu(View view) {
        // inflate menu
    }

    public void clear(){
        homeList.clear();
    }
    public void addAll(ArrayList<Home>homes){
        homeList=homes;
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(activity, view);
        try{  MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.myitems_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyItemsAdapter.MyMenuItemClickListener(position));
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
                case R.id.action_delete:
                    Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                    ItemSelect itemSelect=(ItemSelect)activity;
                    itemSelect.remove(position);
                    return true;
                default:
            }
            return false;
        }
    }
}
