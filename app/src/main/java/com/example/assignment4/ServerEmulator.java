package com.example.assignment4;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ServerEmulator { // Didn't want to actually set up a server, so I made this.
    private final LocalStorage storage;
    private List<User> db;


    ServerEmulator() throws IOException {
        storage = new LocalStorage();
        db = storage.loadDatabase();
    }

    private User getUser(String username) {
        for (User user : db) {
            if (user.equals(username)) {
                return user;
            }
        }
        return null;
    }

    private byte[] getUserSalt(String username) {
        User user = getUser(username);
        if (user != null) {
            return user.getUserSalt();
        }
        return null;
    }

    public void registerUser (String username, byte[] hashedPassword, byte[] userSalt) throws IOException {
        User toAdd = new User(username, hashedPassword, userSalt);
        storage.addUser(toAdd);
        db = storage.loadDatabase(); // I know this isn't the best way to do this. but I'm not expecting more than 100 users, so any lag should be negligible.
    }

    public int attemptLogin (String username, byte[] hashedPassword) {
        User tempUser = new User(username, hashedPassword, new byte[]{});
        User attempting = getUser(username);
        if (attempting != null) {
            if (attempting.equals(tempUser)) {
                return 2; // User successfully logged in.
            } else {
                return 1; // Password Incorrect
            }

        } else {
            return 0; // user does not exist
        }
    }

    public void shutdown() throws IOException {
        storage.close();
    }
}
