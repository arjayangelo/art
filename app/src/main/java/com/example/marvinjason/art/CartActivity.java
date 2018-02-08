package com.example.marvinjason.art;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ImageButton dltBtn;
    private ListView listView;
    private JSONArray jsonArray;
    public static long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        listView = (ListView) findViewById(R.id.lv_activityCart);
        fetchData();

    }

    public void fetchData()
    {
        new AsyncTask(){
            private List<Order> orders;
            private JSONArray imageArray;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                orders = new ArrayList();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                RestApi rest = new RestApi(CartActivity.this);
                jsonArray = rest.fetchJSONArray("https://art-augmented-retail.herokuapp.com/api/v1/cart?access_token="+getSharedPreferences("data", Context.MODE_PRIVATE).getString("access_token", "error"));

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONObject itemJSON = jsonObject.getJSONObject("item");
                        Order order = new Order();
                        order.orderId = jsonObject.getInt("id");
                        order.orderDate = jsonObject.getString("created_at");
                        order.itemId = itemJSON.getInt("id");
                        order.name = itemJSON.getString("name");
                        order.price = Double.parseDouble(itemJSON.getString("price"));
                        order.shortDescription = itemJSON.getString("short_description");
                        id = order.orderId;
                        JSONArray jsonImages = itemJSON.getJSONArray("images");
                        for (i = 0; i < jsonImages.length(); i++) {
                            order.images.add(jsonImages.getJSONObject(i).getString("url"));
                        }

                        order.model = itemJSON.getJSONObject("model").getString("url");
                        order.userId = itemJSON.getInt("user_id");
                        order.categoryId = itemJSON.getInt("category_id");
                        orders.add(order);
                    }
                }
                catch (Exception ex)
                {
                    Log.d("Error", ex.toString());
                }
                listView.setAdapter(new CustomListAdapter(CartActivity.this, orders));
            }
        }.execute();
    }

    private class CustomListAdapter extends BaseAdapter {

        private Context context;
        private List<Order> orders;

        public CustomListAdapter(Context context, List<Order> orders)
        {
            this.context = context;
            this.orders = orders;
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
        }

        @Override
        public long getItemId(int position) {
            return orders.get(position).orderId;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.custom_cartitem, null);
            Picasso.with(context).load(orders.get(0).images.get(0)).into(((ImageView) view.findViewById(R.id.itemImage)));
            ((TextView) view.findViewById(R.id.itemName)).setText(orders.get(position).name);
            ((TextView) view.findViewById(R.id.itemPrice)).setText(String.format("Php %.2f", orders.get(position).price));

            dltBtn = (ImageButton) view.findViewById(R.id.imageButton);
            dltBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestApi restApi = new RestApi(CartActivity.this);
                    restApi.deleteFromCart(CartActivity.this, orders.get(position).orderId, orders.get(position).name);
                    Intent intent = new Intent(CartActivity.this,CartActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
            return view;
        }

    }
    private class Order
    {
        public int orderId;
        public String orderDate;
        public int itemId;
        public String name;
        public double price;
        public String shortDescription;
        public List<String> images;
        public String model;
        public int userId;
        public int categoryId;

        public Order()
        {
            images = new ArrayList();
        }
    }
}