package com.example.daveart.vocabularyapp.model.data_handlers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.daveart.vocabularyapp.model.ItemTables;

import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Created by DaveArt on 7/20/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vocabularyDb.db";
    private static final int DATABASE_VERSION = 16;
    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(ItemTables.CREATE_TABLE);
        sqLiteDatabase.execSQL(ItemTables.CREATE_TEMP_TABLE);
        sqLiteDatabase.execSQL(ItemTables.CREATE_RESULT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

//        sqLiteDatabase.execSQL(ItemTables.CREATE_TABLE);
//        sqLiteDatabase.execSQL(ItemTables.CREATE_TEMP_TABLE);
//        sqLiteDatabase.execSQL(ItemTables.CREATE_RESULT_TABLE);

//        sqLiteDatabase.execSQL("ALTER TABLE " + ItemTables.TABLE_NAME + " ADD COLUMN " +
//                ItemTables.COLUMN_EXAMPLE + " TEXT NOT NULL");
//        sqLiteDatabase.execSQL(ItemTables.CREATE_TEMP_TABLE);

//        sqLiteDatabase.execSQL(ItemTables.CREATE_TABLE);

//        sqLiteDatabase.execSQL("ALTER TABLE " + ItemTables.TABLE_NAME + " ADD COLUMN " + ItemTables
//                .COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
//        sqLiteDatabase.execSQL("ALTER TABLE " + DATABASE_NAME + " ADD COLUMN " + ItemTables
//                .COLUMN_TYPE + " TEXT NOT NULL;");
//        sqLiteDatabase.execSQL(ItemTables.DROP_TABLE);
//        sqLiteDatabase.execSQL(ItemTables.DROP_HW_TABLE);
//        if (i < i1) {

//        sqLiteDatabase.execSQL("ALTER TABLE result_table ADD COLUMN hard_word INTEGER;");

//        }
//        sqLiteDatabase.execSQL(ItemTables.DELETE_RESULT_TABLE);
//        sqLiteDatabase.execSQL(ItemTables.DROP_TABLE);
//        sqLiteDatabase.execSQL(ItemTables.DROP_TEMP_TABLE);
//        Log.i("Upgrade", "Executed");
//        onCreate(sqLiteDatabase);

    }
}
