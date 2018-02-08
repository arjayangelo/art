
package com.example.marvinjason.art;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ARt.Augment.UnityPlayerActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailsActivity extends AppCompatActivity {
    public static long itemID;
    public static String itemName = "";
    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    public static JSONArray jsonArray;
    private JSONArray branchesArray;
    private String branchID = "";
    public static boolean isPremium = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_custom_layout);

//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_activityItem);
//        final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (!Utility.isConnected(ItemDetailsActivity.this)) {
//                    Toast.makeText(ItemDetailsActivity.this, "No internet connection!", Toast.LENGTH_LONG).show();
//                }

        fetchData();
//            }
//        };
//
//        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
//
//        swipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//                onRefreshListener.onRefresh();
//            }
//        });
    }

    public void fetchData() {
        new AsyncTask() {
            private JSONObject jsonObject;
            private long id;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                id = (long) getIntent().getExtras().get("id");
                itemID = id;
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                RestApi rest = new RestApi(ItemDetailsActivity.this);
                jsonObject = rest.fetchJSONObject("https://art-augmented-retail.herokuapp.com/api/v1/items/" + id);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {
                    Item item = new Item();
                    item.name = jsonObject.getString("name");
                    item.description = jsonObject.getString("short_description");
                    item.price = Double.parseDouble(jsonObject.getString("price"));
                    item.merchant.email = jsonObject.getJSONObject("user").getString("email");
                    item.merchant.name = jsonObject.getJSONObject("user").getString("name");
                    itemName = item.name;
                    int counter = 0;
                    for( int i=0; i<item.name.length(); i++ ) {
                        counter += 1;
                    }


                    JSONArray jsonArray = jsonObject.getJSONArray("images");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        item.images.add(jsonArray.getJSONObject(i).getString("url"));
                    }
                    branchesArray = jsonObject.getJSONArray("branches");
                    setData(item);
                } catch (Exception ex) {
                    Log.d("Error", ex.toString());
                }
            }
        }.execute();
    }

    private class Item
    {
        String name;
        String description;
        double price;
        Merchant merchant;
        List<String> images;
        String branches;

        public Item()
        {
            images = new ArrayList();
            merchant = new Merchant();
        }
    }

    private class Merchant
    {
        String email;
        String name;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isPremium) {
            getMenuInflater().inflate(R.menu.itemmenupremium, menu);
        } else {
            getMenuInflater().inflate(R.menu.itemmenubasic, menu);
        }
        return true;
    }

    private void setData(Item items) {

//        viewPager = (ViewPager)findViewById(R.id.viewPage);
//        adapter = new ViewPagerAdapter(ItemDetailsActivity.this,items.images);
//        viewPager.setAdapter(adapter);
        Picasso.with(ItemDetailsActivity.this).load(items.images.get(0)).into(((ImageView)findViewById(R.id.imageView1)));
        ((TextView) findViewById(R.id.itemName)).setText(items.name);
        ((TextView) findViewById(R.id.itemPrice)).setText(String.format("Php %.2f", items.price));
        ((TextView) findViewById(R.id.itemDescription)).setText(items.description);
        ((TextView) findViewById(R.id.itemSeller)).setText(items.merchant.name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isPremium) {
            switch (item.getItemId()) {
                case R.id.action_add_cart:
                    if (LoginActivity.isLoggedIn) {
                        RestApi rest = new RestApi(ItemDetailsActivity.this);
                        rest.addToCart("https://art-augmented-retail.herokuapp.com/api/v1/cart?access_token=" + getSharedPreferences("data", Context.MODE_PRIVATE).getString("acccess_token", "error"), itemID, itemName);
                    } else {
                        Intent loginIntent = new Intent(ItemDetailsActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                    return true;
                case R.id.action_maps:
                    Intent mapsIntent = new Intent(ItemDetailsActivity.this, MapsActivity.class);
                    mapsIntent.putExtra("branchesArray", branchesArray.toString());
                    startActivity(mapsIntent);
                    return true;
                case R.id.action_augment:
                    Intent augmentIntent = new Intent(ItemDetailsActivity.this, UnityPlayerActivity.class);
                    augmentIntent.putExtra("id", itemID);
                    startActivity(augmentIntent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        else {
            switch (item.getItemId()) {
                case R.id.action_add_cart:
                    if (LoginActivity.isLoggedIn) {
                        RestApi rest = new RestApi(ItemDetailsActivity.this);
                        rest.addToCart("https://art-augmented-retail.herokuapp.com/api/v1/cart?access_token=" + getSharedPreferences("data", Context.MODE_PRIVATE).getString("access_token", "error"), itemID, itemName);
                    } else {
                        Intent loginIntent = new Intent(ItemDetailsActivity.this, LoginActivity.class);
                        startActivity(loginIntent);
                    }
                    return true;
                case R.id.action_maps:
                    Intent mapsIntent = new Intent(ItemDetailsActivity.this, MapsActivity.class);
                    mapsIntent.putExtra("branchesArray", branchesArray.toString());
                    startActivity(mapsIntent);
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    }
}

