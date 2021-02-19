package com.example.assignment4;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class EncryptEngine {
    private final SecretKey key;
//    private final File keyFile;
    private final Cipher encrypter;
    private final Cipher decrypter;
    private final FileInputStream reader;
    private final FileOutputStream writer;

    @RequiresApi(api = Build.VERSION_CODES.O)
    EncryptEngine(Context context) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {
        File test =  new File(context.getFilesDir(), "key.key");
        boolean existsFlag = test.exists();
        test.createNewFile();

        reader = context.openFileInput("key.key");
        writer = context.openFileOutput("key.key", Context.MODE_APPEND);

        if (!existsFlag) {
            key = genKey();
            saveKey(key);
        } else {
            key = loadKey();
        }
        encrypter = Cipher.getInstance("AES");
        encrypter.init(Cipher.ENCRYPT_MODE, key);
        decrypter = Cipher.getInstance("AES");
        decrypter.init(Cipher.DECRYPT_MODE, key);
    }

    public String encodeHex(byte[] input) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < input.length; i++) {
            hexStringBuffer.append(byteToHex(input[i]));
        }
        return hexStringBuffer.toString();
    }

    private String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    private byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }


    public byte[] decodeHex(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }

    private SecretKey genKey() throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        SecretKey theKey = generator.generateKey();
        return theKey;
    }

    private void saveKey(SecretKey key) throws IOException
    {
        byte[] encoded = key.getEncoded();
        String hex = encodeHex(encoded);
        writer.write(hex.getBytes());
        writer.flush();
        writer.close();
    }

    private SecretKey loadKey() throws IOException
    {
        char[] hex = new char[64];
        InputStreamReader isr = new InputStreamReader(reader);

        isr.read(hex);
//        String hex = reader.readLine(); //should all be on one line
        byte[] decoded = decodeHex(String.valueOf(hex));
        SecretKey theKey = new SecretKeySpec(decoded, "AES");
        reader.close();
        return theKey;
    }

    public byte[] encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return encrypter.doFinal(input.getBytes());
    }

    public String decrypt(byte[] input) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        return new String(decrypter.doFinal(input));
    }


}
