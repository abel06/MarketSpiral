package com.example.abela.marketspiral.ADD;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Core.Session;
import com.example.abela.marketspiral.Decode.DataDecoder;
import com.example.abela.marketspiral.Decode.Home;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.CheckHomeExist;
import com.example.abela.marketspiral.interfaces.ItemSelect;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Handler;
import android.widget.Toast;

public class MyItems extends AppCompatActivity implements RemoteResponse,ItemSelect {
    private RecyclerView recyclerView;
    private MyItemsAdapter adapter;
    ArrayList<Home> myItemList=new ArrayList<>();
    private SwipeRefreshLayout swipeContainer;
    String type="";
    String user_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);



        myItemList=new ArrayList<>();


        Session session=new Session(getApplicationContext());
       type= session.getType();
       user_id=session.getUserID();

        final HashMap<String,String> user_data= new HashMap<>();
        user_data.put("id",user_id);
        user_data.put("type",type);

       // new RemoteTask(Actions.MY_ITEMS,user_data, this,getApplicationContext(),false).execute();
      //  Log.d("ab_log","adapter"+adapter.getItemCount());

        Log.d("ab_log","adapter"+user_data);

        //Log.d("ab","owner.name "+homes.get(0));
        myHomes(user_data);



        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                myHomes(user_data);
            }

        });

        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        adapter = new MyItemsAdapter(getApplicationContext(),myItemList,MyItems.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
    //================================================================================================================
    private void myHomes(  HashMap<String,String>data){
        new RemoteTask(Actions.MY_ITEMS,data,this,getApplicationContext(),false).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_additem, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_item:

                Intent intent=getIntent();
                String result= intent.getStringExtra("result");
                Intent add_item_intent=new Intent(MyItems.this, AddItem.class);
                add_item_intent.putExtra("result",result);
                startActivity(add_item_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//Not used methods  below this
    @Override
    public void onItemSelect(int position) {

    }

    @Override
    public void remove(int position) {
        String home_id=myItemList.get(position).getId();
        HashMap<String,String>home_data=new HashMap<>();
        home_data.put("id",home_id);
        new RemoteTask(Actions.REMOVE_ITEM,home_data, this,getApplicationContext(),false).execute();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
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
    //=================================================================================================================

    @Override
    public void loginFinished(int id, Object result) {

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
    public void itemAdded(int id, Object result) {

    }

    @Override
    public void itemRemoved(int id) {
        if(id==1){
        HashMap<String,String> user_data= new HashMap<>();
        user_data.put("id",user_id);
        user_data.put("type",type);
        new RemoteTask(Actions.MY_ITEMS,user_data, this,getApplicationContext(),false).execute();

        }else {
            Toast.makeText(getApplicationContext(),"Something Went Wrong Retry",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void searchItem(int id) {

    }

    @Override
    public void profileFinished(int responce) {

    }

    @Override
    public void imageUploaded(int value) {

    }

    @Override
    public void myItems(Integer value,Object object) {
        swipeContainer.setRefreshing(false);
        switch (value){
            case 1:
                CheckHomeExist checkHomeExist =new CheckHomeExist(object,getApplicationContext());
                if(checkHomeExist.isHomeExist()){

                   DataDecoder dataDecoder =new DataDecoder(object.toString());
                    dataDecoder.decode();
                    myItemList= dataDecoder.getAllHomes();
                    adapter.clear();
                    adapter.notifyDataSetChanged();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.addAll(myItemList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    }, 100);

                }else {
                    Toast.makeText(getApplicationContext(),"Sorry no home rents found at selected location",Toast.LENGTH_SHORT).show();
                }
                break;
            case -1:
                Toast.makeText(getApplicationContext(),"Sorry no home rents found at selected location",Toast.LENGTH_SHORT).show();
                break;
            case -3:
                Toast.makeText(getApplicationContext(),"Unable to reach Homespiral server please check you internet",Toast.LENGTH_SHORT).show();
                break;

        }

    }
}
