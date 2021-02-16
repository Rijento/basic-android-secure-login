package com.example.assignment4;

public class User {
    private static char[] username;
    private static byte[] passwordHash;
    private static byte[] userSalt;
    User (char[] uName, byte[] pHash, byte[] uSalt) {
        username = uName;
        passwordHash = pHash;
        userSalt = uSalt;
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
}
