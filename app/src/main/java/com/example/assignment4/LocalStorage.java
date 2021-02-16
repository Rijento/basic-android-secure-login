package com.example.assignment4;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class LocalStorage {
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public LocalStorage() throws IOException {
        File csvFile = new File("localStorage.csv");
        csvFile.createNewFile(); //if the file doesn't exist, create one

        reader = new BufferedReader(new FileReader(csvFile));
        writer = new BufferedWriter(new FileWriter(csvFile));
    }


    public List<User> loadDatabase() throws IOException {
        List<User> users = new ArrayList<>();
        String row;
        while ((row = reader.readLine()) != null) {
            if (row.length() > 4) { //minimum theoretical length is 5. just to ensure any BS is caught.
                users.add(new User(row));
            }
        }
        return users;
    }

    public void addUser(User user) throws IOException {
        writer.append(user.toString());
        writer.newLine();
    }

    public void close() throws IOException {
        reader.close();
        writer.flush();
        writer.close();
    }
}
