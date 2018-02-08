package com.example.marvinjason.art;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewFragment extends Fragment {
    public static String extension = "";
    public static SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private JSONArray jsonArray;
    public static int itemID;
    public NewFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_fragmentSale);
        listView = (ListView) view.findViewById(R.id.lv_fragmentSale);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent itemActivity = new Intent(getActivity(),ItemDetailsActivity.class);
                itemActivity.putExtra("id",id);
                if (id == 2 || id == 3 || id == 4 || id == 5 || id == 6){
                    ItemDetailsActivity.isPremium = true;
                }
                else { ItemDetailsActivity.isPremium = false; }
                startActivity(itemActivity);
            }
        });
        final SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                if (!Utility.isConnected(getContext()))
                {
                    Toast.makeText(getContext(), "No internet connection!", Toast.LENGTH_LONG).show();
                }

                fetchData();
            }
        };

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
                onRefreshListener.onRefresh();
            }
        });
        return view;
    }

    public void fetchData()
    {
        new AsyncTask(){
            private List<Item> list;
            private JSONArray imageArray;
            private String[] images;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                list = new ArrayList();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                RestApi rest = new RestApi(getActivity().getApplicationContext());
                jsonArray = rest.fetchJSONArray("https://art-augmented-retail.herokuapp.com/api/v1/items?sort=new");

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                try {
                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Item item = new Item();
                        item.id = jsonObject.getInt("id");
                        item.name = jsonObject.getString("name");
                        item.price = Double.parseDouble(jsonObject.getString("price"));

                        imageArray = jsonObject.getJSONArray("images");
                        item.imageUrl = imageArray.getJSONObject(0).getString("url");

                        list.add(item);
                    }
                }
                catch (Exception ex)
                {
                    Log.d("Error", ex.toString());
                }
                listView.setAdapter(new CustomListAdapter(getContext(), list));
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }

    private class CustomListAdapter extends BaseAdapter {

        private Context context;
        private List<Item> items;

        public CustomListAdapter(Context context, List<Item> items)
        {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return items.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.custom_listitem, null);
            Picasso.with(context).load(items.get(position).imageUrl).into(((ImageView) view.findViewById(R.id.custom_listitem_iv)));
            if (items.get(position).name.length() > 16) {
                ((TextView) view.findViewById(R.id.custom_listitem_tv1)).setText(items.get(position).name.substring(0, 16) + "...");
            }
            else {
                ((TextView) view.findViewById(R.id.custom_listitem_tv1)).setText(items.get(position).name + "...");
            }
            ((TextView) view.findViewById(R.id.custom_listitem_tv2)).setText(String.format("Php %.2f", items.get(position).price));
            return view;
        }

    }
    private class Item
    {
        public int id;
        public String name;
        public double price;
        public String imageUrl;
    }
}
