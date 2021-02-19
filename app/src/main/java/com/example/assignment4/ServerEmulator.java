package com.example.assignment4;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class ServerEmulator { // Didn't want to actually set up a server, so I made this.
    private final LocalStorage storage;
    private final EncryptEngine encryptEngine;
    private List<User> db;


    @RequiresApi(api = Build.VERSION_CODES.O)
    ServerEmulator(Context context) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        storage = new LocalStorage(context);
        encryptEngine = new EncryptEngine(context);
        db = decodeAndLoad();
        Log.e("DB", String.valueOf(db.size()));
    }

    private User getUser(String username) {
        for (User user : db) {
            if (user.equals(username)) {
                return user;
            }
        }
        return null;
    }

    private List<User> decodeAndLoad() throws IOException, NoSuchAlgorithmException, BadPaddingException,
            NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
        List<String> rows = storage.loadDatabase();
        List<User> users = new ArrayList<>();
        for (String row : rows) {
            String clippedRow = row.substring(0, 65);
            String[] data = clippedRow.split(",");
            byte[] uNameBytes = encryptEngine.decodeHex(data[0]);
            byte[] passBytes = encryptEngine.decodeHex(data[1]);
            String uName = encryptEngine.decrypt(uNameBytes);
            String pass  = encryptEngine.decrypt(passBytes);
            users.add(new User(uName, pass));
        }
        return users;
    }

//    private byte[] getUserSalt(String username) {
//        User user = getUser(username);
//        if (user != null) {
//            return user.getUserSalt();
//        }
//        return null;
//    }

    public int registerUser (String username, String password) throws IOException,
            IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException {
        if(username.length() < 1 || password.length() < 1) {return 0;}
        if (getUser(username)!= null) {
            return 1;
        }
        byte[] uNameEncrypted = encryptEngine.encrypt(username);
        String uNameHex = encryptEngine.encodeHex(uNameEncrypted);
        byte[] passEncrypted = encryptEngine.encrypt(password);
        String passHex = encryptEngine.encodeHex(passEncrypted);
        storage.addUser(uNameHex, passHex);
        db = decodeAndLoad(); // I know this isn't the best way to do this. but I'm not expecting more than 100 users, so any lag should be negligible.
        return 2;
    }

    public int attemptLogin (String username, String password) {
        User tempUser = new User(username, password);
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
