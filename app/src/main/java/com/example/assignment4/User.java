package com.example.assignment4;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class User {
    private final String username;
    private final String password;
//    private final byte[] userSalt;
    User (String uName, String pass) {
        username = uName;
        password = pass;
//        userSalt = uSalt;
    }

    User (String input) { // straight from the csv
        String[] data = input.split(",");
        username = data[0];
        password = data[1];
//        userSalt = data[2].getBytes(StandardCharsets.UTF_16);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

//    public byte[] getUserSalt() {
//        return userSalt;
//    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof  User) {
            User uObj = (User) obj;
            return username.equals(uObj.getUsername()) && password.equals(uObj.getPassword());
        } else if (obj instanceof String) {
            String sObj = (String) obj;
            return username.equals(sObj);
        } else {
            return false;
        }
    }

//    @NonNull
//    @Override
//    public String toString() {
//        String pHashString = new String(passwordHash, StandardCharsets.UTF_16);
//        String uSaltString = new String(userSalt, StandardCharsets.UTF_16);
//        return username+','+pHashString+','+uSaltString;
//    }
}
