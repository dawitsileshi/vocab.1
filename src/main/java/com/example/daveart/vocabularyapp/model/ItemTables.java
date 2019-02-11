package com.example.daveart.vocabularyapp.model;

/**
 * Created by DaveArt on 7/20/2018.
 */

public class ItemTables {

    public static final String TABLE_NAME = "all_words_table";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "word_type";
    public static final String COLUMN_WORD = "word";
    public static final String COLUMN_MEANING = "meaning";
    public static final String COLUMN_EXAMPLE = "example";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_DIFFICULTY = "difficulty";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_WORD + " TEXT NOT NULL, "
            + COLUMN_DIFFICULTY + " INTEGER NOT NULL, "+ COLUMN_MEANING + " TEXT NOT NULL, " +
            COLUMN_EXAMPLE + " TEXT NOT NULL, " + COLUMN_TYPE + " TEXT NOT NULL, " + COLUMN_TIMESTAMP
            + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String RESULT_TABLE = "result_table";
    public static final String RESULT_COLUMN_ID = "result_id";
    public static final String COLUMN_CORRECTLY_ANSWERED = "correctly_answered";
    public static final String COLUMN_WRONGLY_ANSWERED = "wrongly_answered";
    public static final String COLUMN_HARD_WORDS= "hard_words";

    public static final String CREATE_RESULT_TABLE = "CREATE TABLE " + RESULT_TABLE + "( " +
            RESULT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_HARD_WORDS + " " +
            "INTEGER, " + COLUMN_CORRECTLY_ANSWERED + " INTEGER, " + COLUMN_WRONGLY_ANSWERED +
            " INTEGER);";

    public static final String DELETE_RESULT_TABLE = "DROP TABLE IF EXISTS " + RESULT_TABLE;

    public static final String TEMP_WORDS_TABLE = "temporary_words_table";
    public static final String TEMP_COLUMN_ID = "temporary_column_id";
    public static final String TEMP_COLUMN_WORD = "temporary_column_word";
    public static final String TEMP_COLUMN_TIMESTAMP = "temporary_column_timestamp";

    public static final String CREATE_TEMP_TABLE = "CREATE TABLE " + TEMP_WORDS_TABLE + "( " +
            TEMP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEMP_COLUMN_WORD + " TEXT " +
            "NOT NULL, " + TEMP_COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    public static final String DROP_TEMP_TABLE = "DROP TABLE IF EXISTS " + TEMP_WORDS_TABLE;

}
