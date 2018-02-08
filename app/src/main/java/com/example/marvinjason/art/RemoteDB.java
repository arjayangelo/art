package com.example.marvinjason.art;

import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class RemoteDB
{
    private static final String TAG = "Murv";
    private static URL url;

    static
    {
        try
        {
            url = new URL("http://augmentedretail.esy.es/");
        }
        catch (Exception e)
        {
            Log.d(TAG, "RemoteDB - static: " + e.toString());
        }
    }

    private static String query(String sql)
    {
        String result = "";

        try
        {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
            bufferedWriter.write("sql=" + URLEncoder.encode(sql, "UTF-8"));
            bufferedWriter.flush();
            //bufferedWriter.close();
            Scanner scanner = new Scanner(httpURLConnection.getInputStream()).useDelimiter("\\A");
            result = scanner.hasNext() ? scanner.next() : "";
            httpURLConnection.disconnect();
        }
        catch (Exception e)
        {
            Log.d("TAG", "RemoteDB - query(): " + e.toString());
        }

        return result;
    }

    public static boolean insert(String sql)
    {
        return query(sql).equals("0") ? false : true;
    }

    public static JSONArray select(String sql)
    {
        JSONArray jsonArray = null;

        try
        {
            jsonArray = new JSONArray(query(sql));
        }
        catch (Exception e)
        {
            Log.d("Murv", "RemoteDB - select(): " + e.toString());
        }

        return jsonArray;
    }
}
