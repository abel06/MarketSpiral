package com.example.abela.marketspiral.Utility;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.abela.marketspiral.Core.DescriptionActivity;
import com.example.abela.marketspiral.Decode.Home;
import com.example.abela.marketspiral.R;

import java.util.ArrayList;

/**
 * Created by Abela on 5/10/2017.
 */

public class HomesAdapter extends RecyclerView.Adapter<HomesAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Home> homeList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title_tv, price_tv,date_tv,owner_tv;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title_tv = (TextView) view.findViewById(R.id.title_home_tv);
            price_tv = (TextView) view.findViewById(R.id.price_tv);
            owner_tv= (TextView) view.findViewById(R.id.owner_name_tv);
            date_tv=(TextView)view.findViewById(R.id.date_tv);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_iv);

        }
    }


    public HomesAdapter(Context mContext, ArrayList<Home> homeList) {
        this.mContext = mContext;
        this.homeList= homeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Home home= homeList.get(position);

        holder.title_tv.setText(home.getTitle());
        holder.price_tv.setText(""+home.getPrice()+" $");
        holder.owner_tv.setText(""+home.getOwner().getName());
        holder.date_tv.setText(""+home.getAdded());
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
        // loading home cover using Glide library

        Glide.with(mContext).load(home.getImage().getImageAtIndex(0).getUrl()).into(holder.thumbnail);

    }

    private void showPopupMenu(View view) {
        // inflate menu
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }
}
