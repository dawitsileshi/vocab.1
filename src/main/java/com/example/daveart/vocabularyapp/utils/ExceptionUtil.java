package com.example.daveart.vocabularyapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;

public class ExceptionUtil {

    Context context;

    public ExceptionUtil(Context context){
        this.context = context;
    }

    public boolean checkConnection(){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                        .CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();

    }

    public boolean checkIfDataExists(String tableName, String timeStamp){
        Cursor cursor = new DataSource(context).getAllElements(tableName, timeStamp);
        return cursor.getCount() > 0;
    }

}
