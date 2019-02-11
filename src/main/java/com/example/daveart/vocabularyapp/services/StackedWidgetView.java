package com.example.daveart.vocabularyapp.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.daveart.vocabularyapp.R;
import com.example.daveart.vocabularyapp.model.data_handlers.DataSource;
import com.example.daveart.vocabularyapp.model.ItemTables;
import com.example.daveart.vocabularyapp.model.SavedWord;

import java.util.ArrayList;

public class StackedWidgetView extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i("onGetViewFactory", "executed");
        return new StackedWidgetViewFactory(getApplicationContext());
    }

    class StackedWidgetViewFactory implements RemoteViewsFactory{


        private Context context;
        DataSource dataSource;
        ArrayList<Object> arrayList = new ArrayList<>();

        public StackedWidgetViewFactory(Context context){

            this.context = context;
            Log.i("constructor", "executed");

        }

        @Override
        public void onCreate() {
            dataSource = new DataSource(context);
            arrayList = dataSource.savedWords(ItemTables.TABLE_NAME, 1);
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            Log.i("getViewAt", "executed");
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout
                    .widget_layout_item);
            SavedWord savedWord = (SavedWord) arrayList.get(i);
            remoteViews.setTextViewText(R.id.textView_widget_layout_item_word, savedWord.getWord());
            remoteViews.setTextViewText(R.id.textView_widget_layout_item_meaning, savedWord.getMeaning());
            remoteViews.setTextViewText(R.id.textView_widget_layout_item_wordType, savedWord.getWordType
                    ());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
