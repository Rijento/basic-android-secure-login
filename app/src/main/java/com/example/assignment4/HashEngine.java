package com.example.assignment4;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashEngine {
    private static final int HASH_BYTE_SIZE = 512;
    private static final int PBKDF2_ITERATIONS = 1000;
    private static final int SALT_BYTE_SIZE = 32;
    private static SecureRandom random;
    HashEngine() {
        random = new SecureRandom();
    }

    public byte[][] saltAndHash(char[] input) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte salt[] = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(input, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return new byte[][]{hash, salt};
    }

    public byte[][] saltAndHash(char[] input, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(input, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return new byte[][]{hash, salt};
    }
}
