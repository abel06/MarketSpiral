package com.example.abela.marketspiral.Activities;

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
import android.view.View;
import android.widget.Toast;

import com.example.abela.marketspiral.Core.RemoteTask;
import com.example.abela.marketspiral.Decode.DataDecoder;
import com.example.abela.marketspiral.Decode.Home;
import com.example.abela.marketspiral.R;
import com.example.abela.marketspiral.Utility.Actions;
import com.example.abela.marketspiral.Utility.HomesAdapter;
import com.example.abela.marketspiral.interfaces.RemoteResponse;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RemoteResponse{

    private RecyclerView recyclerView;
    private HomesAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    HashMap<String,String>query=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         ArrayList<Home> homesList=new ArrayList<>();
         homesList=getIntent().getParcelableArrayListExtra("homes");
        query= (HashMap<String, String>) getIntent().getSerializableExtra("query");


  //--------------------------------------------------------------------------------------

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

          startSearch(query);
            }

        });

        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        adapter = new HomesAdapter(getApplicationContext(), homesList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       recyclerView.setAdapter(adapter);
        Log.d("ab_log","adapter"+homesList);
    }
    //================================================================================================================

       private void startSearch(  HashMap<String,String>q){
           new RemoteTask(Actions.SEARCH_ITEM,q,this,getApplicationContext(),false).execute();
        }



    @Override
    public void loginFinished(int id, Object result) {

    }

    @Override
    public void registerFinished(int value, boolean externalService) {

    }

    @Override
    public void searchFinished(int value, Object result) {

        switch (value){
            case 1:

                String result_tmp= result.toString();
                Log.d("ab_log","result tmp"+result_tmp);
                if(!result_tmp.isEmpty()){
                    DataDecoder dataDecoder=new DataDecoder(result_tmp);
                    if(dataDecoder.decode()){
                        ArrayList<Home>homeList=dataDecoder.getVillaHomes();
                         adapter.clear();
                         adapter.notifyDataSetChanged();
                         adapter.addAll(homeList);
                         adapter.notifyDataSetChanged();
                         swipeContainer.setRefreshing(false);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Sorry no home rents found at selected location",Toast.LENGTH_SHORT).show();
                    }
                }else {Toast.makeText(getApplicationContext(),"Sorry no home rents found at selected location",Toast.LENGTH_SHORT).show();}


                break;
            case -1:
                swipeContainer.setRefreshing(false);
                break;
            case -3:
                Toast.makeText(getApplicationContext(),"Unable to reach Homespiral server please check you internet",Toast.LENGTH_SHORT).show();
                break;

        }
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
    public void myItems(Integer value,Object result) {

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
}
