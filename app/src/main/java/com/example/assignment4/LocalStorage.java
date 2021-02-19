package com.example.assignment4;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class LocalStorage {
    private final FileInputStream reader;
    private final FileOutputStream writer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalStorage(Context context) throws IOException {
        File csvFile = new File(context.getFilesDir() , "localStorage.csv");
        csvFile.createNewFile(); //if the file doesn't exist, create one

        reader = context.openFileInput("localStorage.csv");
        writer = context.openFileOutput("localStorage.csv", Context.MODE_APPEND);
    }


    public List<String> loadDatabase() throws IOException {
        InputStreamReader isr = new InputStreamReader(reader);
        List<String> rows = new ArrayList<>();
        char[] test1 = new char[66];
        String row;
        while ((isr.read(test1 )) > 0) {
            rows.add(String.valueOf(test1));
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
