package com.example.daveart.vocabularyapp.model.data_handlers;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class InternalFile {

    private Context context;
    private static final String FILENAME = "text.txt";
//    public static final String TAG = InternalFile.class.getSimpleName();

    public InternalFile(Context context){
        this.context = context;
        new File(FILENAME);
    }

    public void createAndStore(String value){

        FileOutputStream fileOutputStream;

        try{
            fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(value);
            outputStreamWriter.write(" ");
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String readFromFile() {

        StringBuilder s = new StringBuilder();

        try
        {
            FileInputStream fileInputStream = context.openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            char[] inputBuffer = new char[100];
            int charRead;
            while((charRead = inputStreamReader.read(inputBuffer))>0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                s.append(readString);
                inputBuffer = new char[100];
            }
            }catch(IOException e){
            e.printStackTrace();

        }

        return s.toString();
    }

    public void deleteFile(){
        File file = new File(context.getFilesDir(), FILENAME);
        if(file.exists()){
            context.deleteFile(FILENAME);
        }
    }

    public ArrayList<Integer> toArrayList(){

        String data = readFromFile();
        StringBuilder temp = new StringBuilder();
        ArrayList<Integer> arrayList = new ArrayList<>();
        int i = 0;
        if(data.length() > 0) {
            while (i < data.length()) {
                if (!String.valueOf(data.charAt(i)).equals(" ")) {
                    temp.append(data.charAt(i));
                } else {
                    arrayList.add(Integer.parseInt(temp.toString()));
                    temp = new StringBuilder();
                }
                i++;
            }
        }

        return arrayList;

    }


}
