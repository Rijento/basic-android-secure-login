package com.example.assignment4;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class User {
    private static char[] username;
    private static byte[] passwordHash;
    private static byte[] userSalt;
    User (char[] uName, byte[] pHash, byte[] uSalt) {
        username = uName;
        passwordHash = pHash;
        userSalt = uSalt;
    }

    User (String input) { // straight from the csv
        String[] data = input.split(",");
        username = data[0].toCharArray();
        passwordHash = data[1].getBytes(StandardCharsets.UTF_16);
        userSalt = data[2].getBytes(StandardCharsets.UTF_16);
    }

    public static char[] getUsername() {
        return username;
    }

    public static byte[] getPasswordHash() {
        return passwordHash;
    }

    public static byte[] getUserSalt() {
        return userSalt;
    }

    @NonNull
    @Override
    public String toString() {
        String pHashString = new String(passwordHash, StandardCharsets.UTF_16);
        String uSaltString = new String(userSalt, StandardCharsets.UTF_16);
        return username.toString()+','+pHashString+','+uSaltString;
    }
}
