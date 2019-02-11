package com.example.daveart.vocabularyapp.interfaces;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

public interface NetworkCheckInterface {

    public void onSuccess(JSONArray jsonArray);
    public void onError(VolleyError volleyError);
}
