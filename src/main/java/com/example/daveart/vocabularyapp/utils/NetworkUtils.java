package com.example.daveart.vocabularyapp.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.daveart.vocabularyapp.interfaces.NetworkCheckInterface;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by DaveArt on 7/24/2018.
 */

public class NetworkUtils {

    private static String LOG_TAG = NetworkUtils.class.getName();
    public NetworkCheckInterface networkCheckInterface;
    private RequestQueue requestQueue;
    public static final String NETWORK_TAG = "search_for_meaning";

    public NetworkUtils(NetworkCheckInterface networkCheckInterface){
        this.networkCheckInterface = networkCheckInterface;
    }

    public static String fetchData(String word, String wordType){

        Log.i(LOG_TAG, "Word " + word + " " + wordType);
        URL url = createURL(word);
        String jsonResponse = null;

        try {
            jsonResponse = connectToInternet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retrieveJson(jsonResponse, wordType);

    }

    private static URL createURL(String string) {

        try{

            Uri.Builder builder = new Uri.Builder();

            builder.scheme("https");
            builder.authority("owlbot.info");
            builder.appendPath("api");
            builder.appendPath("v2");
            builder.appendPath("dictionary");
            builder.appendPath(string);
            builder.build();

            Log.i(LOG_TAG, "URL" + builder.build().toString());

            return new URL(builder.toString());

        }catch (Exception e){

            Log.e(LOG_TAG, "Problem Creating the URL ", e);
        }

        return null;

    }

    private static String connectToInternet(URL url) throws IOException {

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        String toString = null;

        try{

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){

                inputStream = httpURLConnection.getInputStream();
                toString = bufferInput(inputStream);

            }else{

                Log.e(LOG_TAG, "The response code is " + String.valueOf(httpURLConnection
                        .getResponseCode()));
            }
        }catch (Exception e){

            Log.e(LOG_TAG, "Problem connecting to internet", e);

        }finally {

            if(inputStream != null){

                inputStream.close();

            }if(httpURLConnection != null){

                httpURLConnection.disconnect();

            }
        }

        return toString;

    }

    private static String bufferInput(InputStream inputStream) {

        StringBuilder builder = new StringBuilder();

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName
                ("UTF-8"));

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        try{

            line = bufferedReader.readLine();

            while(line != null){

                builder.append(line);

                line = bufferedReader.readLine();

            }

        }catch (Exception e){

            Log.e(LOG_TAG, "Problem buffering input", e);

        }

        return builder.toString();

    }

    private static String retrieveJson(String jsonResponse, String wordType) {

        String fullJson = null;

        if(jsonResponse == null){
            return null;
        }

        try{

            Log.e(LOG_TAG, "Json Response " + jsonResponse);

            JSONArray rootJsonArray = new JSONArray(jsonResponse);
            for (int i = 0; i < rootJsonArray.length(); i++) {
                JSONObject object = rootJsonArray.getJSONObject(i);
                if(object.getString("type").equalsIgnoreCase(wordType)){
                    fullJson = object.getString("definition");
                    Log.i("Word and definition", fullJson);
                }
            }
//            fullJson = object.getString("definition");
            return fullJson;
        }catch (JSONException e){

            fullJson = "Sorry, could not find the meaning";
            Log.e(LOG_TAG, "Problem parsing the JSON ", e);
            return fullJson;

        }
    }

    public void searchingForMeaning(String word, Context context, RequestQueue requestQueue){

//        requestQueue = Volley.newRequestQueue(context);
        String url = "https://owlbot.info/api/v2/dictionary/" + word + "?format=json";

        Toast.makeText(context, "Arrived", Toast.LENGTH_SHORT).show();
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, response -> networkCheckInterface.onSuccess(response),
                error -> networkCheckInterface.onError(error));

        requestQueue.add(jsonArrayRequest);
//        jsonArrayRequest.setTag(NETWORK_TAG);

    }

}
