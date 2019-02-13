package com.example.daveart.vocabularyapp.model.data_handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.daveart.vocabularyapp.model.CachedWord;
import com.example.daveart.vocabularyapp.model.ItemTables;
import com.example.daveart.vocabularyapp.model.MemorizedWords;
import com.example.daveart.vocabularyapp.model.SavedWord;
import com.example.daveart.vocabularyapp.model.WordsViewType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.daveart.vocabularyapp.model.ItemTables.COLUMN_DIFFICULTY;
import static com.example.daveart.vocabularyapp.model.ItemTables.COLUMN_EXAMPLE;
import static com.example.daveart.vocabularyapp.model.ItemTables.COLUMN_ID;
import static com.example.daveart.vocabularyapp.model.ItemTables.COLUMN_MEANING;
import static com.example.daveart.vocabularyapp.model.ItemTables.COLUMN_TIMESTAMP;
import static com.example.daveart.vocabularyapp.model.ItemTables.COLUMN_TYPE;
import static com.example.daveart.vocabularyapp.model.ItemTables.COLUMN_WORD;
import static com.example.daveart.vocabularyapp.model.ItemTables.TABLE_NAME;
import static com.example.daveart.vocabularyapp.model.ItemTables.TEMP_COLUMN_ID;
import static com.example.daveart.vocabularyapp.model.ItemTables.TEMP_COLUMN_TIMESTAMP;
import static com.example.daveart.vocabularyapp.model.ItemTables.TEMP_COLUMN_WORD;

/**
 * Created by DaveArt on 7/20/2018.
 */

public class DataSource {

    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    Context context;

    private int previousValue = -1;

    // A Constructor
    public DataSource(Context context){
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        this.context = context;
    }

    // Opens the database to be writable
    public void open(){
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    // Closes any opened databases
    public void close(){
        dbHelper.close();
    }

    // the following 3 methods are for retrieving all values from a particular table
    public Cursor getAllElements(String tableName, String sortMethod){

        String order = " DESC";
        if(sortMethod.equals(COLUMN_WORD)){
            order = " ASC";
        }

        return sqLiteDatabase.query(tableName, null, null, null,
                null, null, sortMethod + order);

    }

    public ArrayList<String> getAllWords(){

        Cursor cursor = getAllElements(ItemTables.TABLE_NAME, ItemTables.COLUMN_TIMESTAMP);

        cursor.moveToFirst();

        ArrayList<String> strings = new ArrayList<>();

        while(!cursor.isAfterLast()){
            strings.add(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_WORD)));
//            strings[cursor.getPosition()] = cursor.getString(cursor.getColumnIndex(ItemTables
//                    .COLUMN_WORD));
            cursor.moveToNext();
        }

        cursor.close();

        return strings;
    }

    private Cursor getAllFromSpecificColumn(String columnToFilterWith){
        return sqLiteDatabase.query(ItemTables.RESULT_TABLE, null, columnToFilterWith, null,
                null, null, null);
    }

    private Cursor getSpecificValueFromColumn(int integer){
        return sqLiteDatabase.query(ItemTables.RESULT_TABLE, new String[]{"hard_words"},
                "hard_words" + "=" + integer, null, null, null, null);
    }

    public Object getSingleData(long id, String tableName){

        Cursor cursor;

        if(tableName.equals(ItemTables.TABLE_NAME)){
            cursor = sqLiteDatabase.query(tableName, null, COLUMN_ID + " = " + id, null,
                    null, null, null);
//            cursor = sqLiteDatabase.query(tableName, new String[]{COLUMN_ID, COLUMN_WORD, COLUMN_MEANING,
//            COLUMN_TYPE, COLUMN_TIMESTAMP, COLUMN_DIFFICULTY}, COLUMN_ID + "=?" + new
//                    String[]{idToString}, null, null, null, null);

            SavedWord savedWord = new SavedWord();

            if(cursor != null){

                cursor.moveToFirst();

                savedWord.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                savedWord.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
                savedWord.setMeaning(cursor.getString(cursor.getColumnIndex(COLUMN_MEANING)));
                savedWord.setWordType(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));
                savedWord.setTimestamp(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP)));
                savedWord.setDifficulty(cursor.getInt(cursor.getColumnIndex(COLUMN_DIFFICULTY)));
                savedWord.setExample(cursor.getString(cursor.getColumnIndex(COLUMN_EXAMPLE)));

                cursor.close();
            }
            return savedWord;
        }else{
            cursor = sqLiteDatabase.query(tableName, null, TEMP_COLUMN_ID + " = " + id, null,
                    null, null, null);

            CachedWord cachedWord = new CachedWord();

            if(cursor != null){
                cursor.moveToFirst();

                cachedWord.setId(cursor.getLong(cursor.getColumnIndex(TEMP_COLUMN_ID)));
                cachedWord.setWord(cursor.getString(cursor.getColumnIndex(TEMP_COLUMN_WORD)));
                cachedWord.setTimestamp(cursor.getString(cursor.getColumnIndex(TEMP_COLUMN_TIMESTAMP)));

                cursor.close();

            }

            return cachedWord;

        }

    }

    public Cursor CheckIfSpecificData(String word) {
        return sqLiteDatabase.query(ItemTables.TEMP_WORDS_TABLE, new String[]{ItemTables
                .TEMP_COLUMN_WORD}, ItemTables.TEMP_COLUMN_WORD + " = '" + word + "'", null, null,
                null,
                null);
    }

    public ArrayList<Object> getWordsAndTimestamp() {
        ArrayList<Object> objects = savedWords(ItemTables.TABLE_NAME, 1);
        ArrayList<Object> memorizedWords = new ArrayList<>();

        for (Object object :
                objects) {

            SavedWord savedWord = (SavedWord) object;
            MemorizedWords memorizedWord = new MemorizedWords();
            memorizedWord.setId(savedWord.getId());
            memorizedWord.setWord(savedWord.getWord());
            memorizedWords.add(memorizedWord);

        }
        return memorizedWords;
    }

    public SavedWord checkIfExistsAndRetrieve(String word){

        SavedWord savedWord = new SavedWord();
        Cursor cursor = getAllElements(ItemTables.TABLE_NAME, ItemTables.COLUMN_TIMESTAMP);

        boolean found = false;

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){

            if(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_WORD)).equals(word)){

                found = true;

                break;

            }

            cursor.moveToNext();

        }

        if(found){

            savedWord.setId(cursor.getLong(cursor.getColumnIndex(ItemTables.COLUMN_ID)));
            savedWord.setWord(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_WORD)));
            savedWord.setMeaning(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_MEANING)));
            savedWord.setDifficulty(cursor.getInt(cursor.getColumnIndex(ItemTables.COLUMN_DIFFICULTY)));
            savedWord.setWordType(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_TYPE)));
            savedWord.setTimestamp(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_TIMESTAMP)));
            savedWord.setExample(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_EXAMPLE)));
            cursor.close();

            return savedWord;

        }else{

            return null;

        }

    }

    // the following 3 methods are for checking for exception during insertion of data
    public boolean checkIfExists(SavedWord savedWord) {

        Cursor cursor = getAllElements(TABLE_NAME, ItemTables.COLUMN_TIMESTAMP);

        cursor.moveToLast();

            while (!cursor.isBeforeFirst()) {

                if (savedWord.getWord().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(
                        COLUMN_WORD))) && savedWord.getWordType().equalsIgnoreCase(cursor
                        .getString(cursor.getColumnIndex(ItemTables.COLUMN_TYPE)))) {

                    return false;

                }
//
                cursor.moveToPrevious();
//
            }
            Log.i("Result value", "exists");
            cursor.close();

            return true;

    }

    public boolean doesExist(String word) {

        Cursor cursor = CheckIfSpecificData(word);
        if(cursor.getCount() == 0) {
            return false;
        }
        return true;
    }

    public boolean checkEmpty(SavedWord savedWord) {

        return !TextUtils.isEmpty(savedWord.getWord()) && !TextUtils.isEmpty(savedWord.getMeaning());
    }

    public boolean checkNumber(SavedWord savedWord){

        return true;

    }

    // checks if a certain number is repeated for about 3 times or more
    public void checkFrequency(String column){

        ArrayList<Integer> arrayList = toArrayList(column);

        Collections.sort(arrayList);

        if(arrayList.size() > 1) {
            int counter = 1;
            int i = 0, j = 0;
            while (i < arrayList.size()-1 && j < arrayList.size()-1) {
                j++;
                if (arrayList.get(i).equals(arrayList.get(j))) {
                    counter++;
                }else {
                    i = j;
                    counter = 1;
                }
                if (counter > 1) {

                    findItemOnDB(arrayList.get(j - 1), column);

                    counter = 1;

                    }

            }
        }

    }

    // retrieves an item from a table and updates it accordingly
    private void findItemOnDB(int item, String column){
        List<Object> arrayList = savedWords(ItemTables.TABLE_NAME, 0);
        SavedWord savedWord = (SavedWord) arrayList.get(item);

        if(previousValue != item) {
            long id = savedWord.getId();

            if (column.equals(ItemTables.COLUMN_WRONGLY_ANSWERED)) {
                insertSingleValue(item);
                updateOnDifficultyItem(id, 1);
                removeItemByValue(item, column);
            } else {
                Cursor cursor = getSpecificValueFromColumn(item
                );
                if (cursor.getCount() > 0) {
                    updateOnDifficultyItem(id, 0);
                    removeItemByValue(item, column);
                    removeItemByValue(item, "hard_word");
                }

            }
            previousValue = item;

        }

    }

    // the following 4 methods are for inserting values into a table
    private void insertSingleValue(int integer) {

        Cursor result = getSpecificValueFromColumn(integer);

        if(result.getCount() == 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("hard_word", integer);
            createItem(contentValues, ItemTables.RESULT_TABLE);
        }else {
            result.close();
        }
    }

    public long insertSingleWord(String word){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemTables.TEMP_COLUMN_WORD, word);
        return createItem(contentValues, ItemTables.TEMP_WORDS_TABLE);
    }

    public void insertToResultDb(boolean isCorrect, int questionID){

        ContentValues contentValues = new ContentValues();

        if(getSpecificValueFromColumn(questionID).getCount
                () > 0 && isCorrect){

            contentValues.put(ItemTables.COLUMN_CORRECTLY_ANSWERED, questionID);
            createItem(contentValues, ItemTables.RESULT_TABLE);

        } else if(!isCorrect){

            contentValues.put(ItemTables.COLUMN_WRONGLY_ANSWERED, questionID);
            createItem(contentValues, ItemTables.RESULT_TABLE);

        }


    }

    public long insertData(boolean checkExistence, boolean checkEmptiness, boolean checkNumber,
                              SavedWord savedWord, int position, boolean isDeleted) {

        long result = 0;

        if(checkEmptiness && checkExistence && checkNumber) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(ItemTables.COLUMN_WORD, savedWord.getWord());
            contentValues.put(ItemTables.COLUMN_MEANING, savedWord.getMeaning());
            contentValues.put(ItemTables.COLUMN_DIFFICULTY, savedWord.getDifficulty());
            contentValues.put(ItemTables.COLUMN_TYPE, savedWord.getWordType());
            if(savedWord.getExample().compareToIgnoreCase("null") == 0) {
                contentValues.put(ItemTables.COLUMN_EXAMPLE, "No Example");
            }else {
                contentValues.put(ItemTables.COLUMN_EXAMPLE, savedWord.getExample());
            }

            result = createItem(contentValues, ItemTables.TABLE_NAME);
            Log.i("Result value", String.valueOf(result));
            if(!isDeleted){
                savedWord = (SavedWord) getSingleData(result, ItemTables.TABLE_NAME);
            }
            savedWords(ItemTables.TABLE_NAME, 0);

        }else if(!checkEmptiness && !checkExistence){
            Toast.makeText(context, "Text field is empty and word already exists", Toast
                    .LENGTH_SHORT).show();
            result = -1;
        }else if(!checkEmptiness && !checkNumber){
            result = -1;
            Toast.makeText(context, "Text field is empty and number exists", Toast
                    .LENGTH_SHORT).show();
        }else if(!checkExistence){
            Toast.makeText(context, "Already exists", Toast.LENGTH_SHORT).show();
            result = -1;
        }else if(!checkEmptiness){
            result = -1;
            Toast.makeText(context, "Edit Text is empty", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    private long createItem(ContentValues contentValues, String tableName){

        long id;

        id = sqLiteDatabase.insert(tableName, null, contentValues);

        return id;

    }

    // the following 4 methods are for retrieving values from db and store them in an arraylist
    public ArrayList<Object> savedWords(String tableName, int position){

        String sortMethod = sortType(position);

        Cursor cursor = getAllElements(tableName, sortMethod);
        cursor.moveToFirst();

        ArrayList<Object> savedWords = new ArrayList<>();

        while(!cursor.isAfterLast()){

            SavedWord savedWord = new SavedWord();

            savedWord.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
            savedWord.setWord(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_WORD)));
            savedWord.setMeaning(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_MEANING)));
            savedWord.setDifficulty(cursor.getInt(cursor.getColumnIndex(ItemTables.COLUMN_DIFFICULTY)));
            savedWord.setWordType(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_TYPE)));
            savedWord.setExample(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_EXAMPLE)));
            savedWord.setTimestamp(cursor.getString(cursor.getColumnIndex(ItemTables.COLUMN_TIMESTAMP)));
            savedWords.add(savedWord);
            cursor.moveToNext();

        }
        cursor.close();
        return savedWords;
    }

    public ArrayList<Object> cachedWords(){

        Cursor cursor = getAllElements(ItemTables.TEMP_WORDS_TABLE, ItemTables.TEMP_COLUMN_TIMESTAMP);

        cursor.moveToFirst();

        ArrayList<Object> cachedWordArrayList = new ArrayList<>();

        while(!cursor.isAfterLast()){

            CachedWord cachedWord = new CachedWord();
            cachedWord.setId(cursor.getLong(cursor.getColumnIndex(ItemTables.TEMP_COLUMN_ID)));
            cachedWord.setWord(cursor.getString(cursor.getColumnIndex(ItemTables.TEMP_COLUMN_WORD)));
            cachedWordArrayList.add(cachedWord);
            cursor.moveToNext();
        }

        cursor.close();

        return cachedWordArrayList;
    }

    public ArrayList<Integer> toArrayList(String columnToFilterWith){

        ArrayList<Integer> arrayList = new ArrayList<>();

        Cursor cursor = getAllFromSpecificColumn(columnToFilterWith);

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            int currentValue = cursor.getInt(cursor.getColumnIndex(columnToFilterWith));
            arrayList.add(currentValue);
            cursor.moveToNext();
        }
        cursor.close();

        return arrayList;
    }

    public ArrayList<String> randomWord() {

        Random random = new Random();
        ArrayList<String> NotEmptyArrayList = new ArrayList<>();
        ArrayList<String> EmptyArrayList = new ArrayList<>();

        EmptyArrayList.add("No Word");
        EmptyArrayList.add("No Meaning");

        Cursor cursor = getAllElements(TABLE_NAME, ItemTables.COLUMN_TIMESTAMP);

        if (cursor.getCount() > 0) {

            int randomNumber = random.nextInt(cursor.getCount());

            cursor.moveToPosition(randomNumber);

            NotEmptyArrayList.add(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
            NotEmptyArrayList.add(cursor.getString(cursor.getColumnIndex(COLUMN_MEANING)));
            NotEmptyArrayList.add(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)));

            return NotEmptyArrayList;
        }

        cursor.close();
        return EmptyArrayList;
    }

    public ArrayList<Object> wordsViewTypes(){

        ArrayList<Object> wordsViewTypes = new ArrayList<>();

        Cursor cursor = getAllElements(ItemTables.TABLE_NAME, ItemTables.COLUMN_WORD);

        ArrayList<Object> savedWords = savedWords(ItemTables.TABLE_NAME, 0);

        String initialization = "";

        for (Object object :
                savedWords) {

            SavedWord savedWord = (SavedWord) object;

            String firstLetter = String.valueOf(savedWord.getWord().charAt(0));

            if(!initialization.equals(firstLetter)) {

                WordsViewType wordsViewType = new WordsViewType(firstLetter);
                wordsViewTypes.add(wordsViewType);

                initialization = firstLetter;

            }

            WordsViewType wordsViewType = new WordsViewType((SavedWord) object); // we can also
            // use savedWord instead of object
            wordsViewTypes.add(wordsViewType);

        }
        cursor.moveToFirst();


        return wordsViewTypes;

    }

    private String sortType(int position) {
        if(position == 0){
            return ItemTables.COLUMN_WORD;
        }else{
            return ItemTables.COLUMN_TIMESTAMP;
        }
    }

    // the following 3 methods are for deleting values from db
    public void removeAllFromTable(String tableName){
        sqLiteDatabase.execSQL("DELETE FROM " + tableName + " WHERE 1");

    }

    public void removeItemById(long id, String tableName, String column) {

        long result = sqLiteDatabase.delete(tableName, column + " = " + id, null);

        Log.i("Delete id", String.valueOf(result));

    }

    public void removeItemByName(String name, String tableName, String column){

        sqLiteDatabase.delete(tableName, column + "=?", new String[]{name});

    }

    private void removeItemByValue(int value, String column) {

        sqLiteDatabase.delete(ItemTables.RESULT_TABLE, column + " =  " + value, null);
    }

    // the following 3 methods are for updating values from db
    public void updateAll(){

        Cursor cursor = getAllElements(ItemTables.TABLE_NAME, ItemTables.COLUMN_TIMESTAMP);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            updateOnDifficultyItem(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)), 0);
            cursor.moveToNext();
        }
    }

    public void updateItem(SavedWord savedWord){

        Toast.makeText(context, "Editing", Toast.LENGTH_SHORT).show();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_WORD, savedWord.getWord());
        contentValues.put(COLUMN_MEANING, savedWord.getMeaning());
        contentValues.put(COLUMN_TYPE, savedWord.getWordType());
        contentValues.put(COLUMN_DIFFICULTY, savedWord.getDifficulty());

//        int updateResult = sqLiteDatabase.update(ItemTables.TABLE_NAME, contentValues, COLUMN_ID
//                        + " = ?",new String[]{String.valueOf(savedWord.getId())});

        int updateResult = sqLiteDatabase.update(TABLE_NAME, contentValues, "id = ?", new
                String[] {String.valueOf(savedWord.getId())});
        Toast.makeText(context, String.valueOf(savedWord.getId()), Toast.LENGTH_SHORT).show();
    }

    private void updateOnDifficultyItem(long id, int value) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemTables.COLUMN_DIFFICULTY, value);
        Log.i("Item id to be updated", String.valueOf(id));
        long result;
        result = sqLiteDatabase.update(ItemTables.TABLE_NAME, contentValues,
                COLUMN_ID + " = ?", new String[]{String.valueOf(id)});

        Log.i("Item id to be updated1", String.valueOf(result));
    }

    public int findNewWordPosition(String word) {

        ArrayList<Object> wordsViewType = wordsViewTypes();

        String firstLetter = String.valueOf(word.charAt(0));

        /*
        * In the following algorithm, I tried to make some adjustments from the first one. Rather
         * than looping through half of the list and check the case. I tried to compare the first
          * letter of each elements, and when I get what I want, I will again loop checking the
          * condition starting from the index where we find the similarities of the first letter
        * */

        int j = 0;
        for (int i = 0; i < wordsViewType.size(); i++) {
            Object object = wordsViewType.get(i);
            WordsViewType wordsViewType2 = (WordsViewType) object;
            if(wordsViewType2.getFirstLetter() != null) {
                if(firstLetter.compareToIgnoreCase(wordsViewType2.getFirstLetter()) == 0) {
                    for(j = i + 1; j < wordsViewType.size(); j++) {
                        WordsViewType wordsViewType1 = (WordsViewType) wordsViewType.get(j);
//                        WordsViewType wordsViewType1 = (WordsViewType) object;
                        if(wordsViewType1.getSavedWord() != null) {
                            if (wordsViewType1.getSavedWord().getWord().compareToIgnoreCase(word) > 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        
//        ArrayList<Object> savedWords = savedWords(ItemTables.TABLE_NAME, 0);
//        ArrayList<Object> wordsViewTypes = wordsViewTypes();
//        int arrayListLength = savedWords.size();
//        int halfTheList = arrayListLength / 2;
//        int startPosition = 0;

        /*
        *   using compareToIgnoreCase
        *   a negative integer - if the first string is greater than the second
        *   zero - if equal
        *   a positive integer - if the second is greater
        *   */

//        WordsViewType halfWordsViewType = (WordsViewType) wordsViewTypes.get(halfTheList);
//        if(halfWordsViewType.getFirstLetter() != null) {
//            if(halfWordsViewType.getFirstLetter().compareToIgnoreCase(String.valueOf(word.charAt
//                    (0))) < 0){
//                startPosition = halfTheList;
//                halfTheList = arrayListLength;
//            }
//
//        }else {
//            if(halfWordsViewType.getSavedWord().getWord().compareToIgnoreCase(word) < 0){
//                startPosition = halfTheList;
//                halfTheList = arrayListLength;
//            }
//        }
//        SavedWord halfSavedWord = (SavedWord) savedWords.get(halfTheList);
//        if(halfSavedWord.getWord().compareToIgnoreCase(word) < 0){
//            startPosition = halfTheList;
//            halfTheList = arrayListLength;
//        }

//        int i;
//        for (i = startPosition; i < halfTheList; i++) {
//            WordsViewType wordsViewType = (WordsViewType) wordsViewTypes.get(i);
//            if(wordsViewType.getFirstLetter() == null) {
//                if(wordsViewType.getSavedWord().getWord().compareToIgnoreCase(word) > 0) {
//                    break;
//                }
//            }
//            SavedWord savedWord = (SavedWord) savedWords.get(i);
//            if(savedWord.getWord().compareToIgnoreCase(word) > 0){
//                break;
//            }
//        }

        return j;

    }
}
