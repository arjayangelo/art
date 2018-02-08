package com.example.marvinjason.art;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.marvinjason.art.Utility.convertStreamToString;

/**
 * Created by Arjay on 1/28/2017.
 */

public class RestApi {
    private Context context;

    public RestApi (Context context) {
        this.context = context;
    }

    public JSONArray fetchJSONArray(String link) {
        JSONArray json = null;
        try {
            URL url = new URL(link);
            InputStream is = url.openConnection().getInputStream();
            String response = convertStreamToString(is);

            json = new JSONArray(response);
        }
        catch (Exception ex) {
            Log.d("Error", ex.toString());
        }

        return json;
    }

    public JSONObject fetchJSONObject(String link) {
        JSONObject json = null;
        try {
            URL url = new URL(link);
            InputStream is = url.openConnection().getInputStream();
            String response = convertStreamToString(is);

            json = new JSONObject(response);
        }
        catch (Exception ex) {
            Log.d("Error", ex.toString());
        }

        return json;
    }

    public JSONObject authenticate(String link, String email, String password) {
        JSONObject json = null;

        try {
            String jason = "{\"user\" : { \"email\" : \"" + email + "\", \"password\" : \"" + Rijndael.encrypt(email, password) + "\" }}";
            LoginActivity.kek = jason;
            URL url = new URL(link);
            HttpURLConnection conn = ((HttpURLConnection) url.openConnection());
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty( "Content-type", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(jason);
            writer.flush();

            String response = convertStreamToString(conn.getInputStream());

            json = new JSONObject(response);
        }
        catch (Exception ex) {
            Log.d("Error", ex.toString());
        }

        return json;
    }

    public void addToCart(final String link, final long item_id, final String item_name){
        new AsyncTask(){
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    String jason = "{\"order\" : { \"item_id\" : " +item_id+"}}";
                    Log.d("MURV", jason);

                    URL url = new URL(link);
                    HttpURLConnection conn = ((HttpURLConnection) url.openConnection());
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty( "Content-type", "application/json");

                    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(jason);
                    writer.flush();

                    String response = convertStreamToString(conn.getInputStream());
                }
                catch (Exception ex) {
                    Log.d("Error", ex.toString());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Toast.makeText(context, item_name+" has been successfully added to your cart!", Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    public void deleteFromCart(final Context context, final long order_id, final String item_name){
        new AsyncTask(){
            private boolean check;

            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    check = true;
                    String link = "https://art-augmented-retail.herokuapp.com/api/v1/cart/" + order_id + "?access_token=" + context.getSharedPreferences("data", Context.MODE_PRIVATE).getString("access_token", "error");
                    Log.d("LINK", link);
                    URL url = new URL(link);
                    HttpURLConnection conn = ((HttpURLConnection) url.openConnection());
                    conn.setDoOutput(true);
                    conn.setRequestMethod("DELETE");
                    conn.setRequestProperty( "Content-Type", "application/json");
                    conn.getInputStream();
                }
                catch (Exception ex) {
                    Log.d("Error", ex.toString());
                    check = false;
                    Toast.makeText(context, "An error was encountered while removing your item!", Toast.LENGTH_LONG).show();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (check) {
                    Toast.makeText(context, item_name+" has been successfully removed from your cart!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}