package com.example.assignment4;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class LocalStorage {
//    private final FileReader reader;
//    private final FileWriter writer;
    private final FileInputStream reader;
    private final FileOutputStream writer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalStorage(Context context) throws IOException {
        String test = context.getFilesDir().toPath().toString();
        File csvFile = new File(context.getFilesDir() , "localStorage.csv");
        csvFile.createNewFile(); //if the file doesn't exist, create one

        reader = context.openFileInput("localStorage.csv");
        writer = context.openFileOutput("localStorage.csv", Context.MODE_APPEND);

//        reader = new FileReader(csvFile);
//        writer = new FileWriter(csvFile);
    }


    public List<String> loadDatabase() throws IOException {
        InputStreamReader isr = new InputStreamReader(reader);
        List<String> rows = new ArrayList<>();
        char[] test1 = new char[66];
//        int test = isr.read(test1);
//        Log.e(test1.toString(), test1.toString());
        String row;
//        BufferedReader testreader = new BufferedReader(isr);
        while ((isr.read(test1 )) > 0) {
            if (test1.length > 2) { //minimum theoretical length is 3. just to ensure any BS is caught.
                rows.add(String.valueOf(test1));
            }
        }
        return rows;
    }

    public void addUser(String username, String password) throws IOException {
        writer.write((username + ',' + password + '\n').getBytes());
    }

    public void close() throws IOException {
        reader.close();
        writer.flush();
        writer.close();
    }
}
