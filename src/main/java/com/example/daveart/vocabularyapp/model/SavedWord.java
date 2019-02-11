package com.example.daveart.vocabularyapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DaveArt on 7/20/2018.
 */

public class SavedWord extends CachedWord implements Parcelable {

    private int difficulty;
    private String wordType;
    private String meaning;
    private String example;

    public SavedWord(){}

//    public SavedWord(String wordType, String word, String meaning, int difficulty){
//
//        this.wordType = wordType;
//        super.word = word;
//        this.meaning = meaning;
//        this.difficulty = difficulty;
//
//    }

    public SavedWord(long id, String wordType, String word, String meaning, int difficulty,
                     String example){

        this.wordType = wordType;
        super.word = word;
        this.meaning = meaning;
        this.difficulty = difficulty;
        super.id = id;
        this.example = example;

    }

    protected SavedWord(Parcel in) {
        word = in.readString();
        difficulty = in.readInt();
        wordType = in.readString();
        meaning = in.readString();
        example = in.readString();
    }

    public static final Creator<SavedWord> CREATOR = new Creator<SavedWord>() {
        @Override
        public SavedWord createFromParcel(Parcel in) {
            return new SavedWord(in);
        }

        @Override
        public SavedWord[] newArray(int size) {
            return new SavedWord[size];
        }
    };

    public String getWordType(){
        return wordType;
    }

    public void setWordType(String wordType){
        this.wordType = wordType;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeInt(difficulty);
        dest.writeString(wordType);
        dest.writeString(meaning);
        dest.writeString(example);
    }
}
