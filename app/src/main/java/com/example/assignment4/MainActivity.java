package com.example.assignment4;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView statusBar = findViewById(R.id.statusView);
        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        ServerEmulator server = null;
        try {
            server = new ServerEmulator(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        assert server != null;


        ServerEmulator finalServer = server;
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int outcome = finalServer.attemptLogin(usernameInput.getText().toString(), passwordInput.getText().toString());
                switch (outcome) {
                    case 0:
                        statusBar.setText("ERROR: User does not exist.");
                        break;
                    case 1:
                        statusBar.setText("ERROR: Incorrect Password.");
                        break;
                    case 2:
                        statusBar.setText("Login Successful!");
                        break;
                }
                usernameInput.setText("");
                passwordInput.setText("");
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int outcome = -1;
                try {
                    outcome = finalServer.registerUser(usernameInput.getText().toString(), passwordInput.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                }

                switch (outcome) {
                    case 0:
                        statusBar.setText("Error: Username/Password cannot be empty.");
                        break;
                    case 1:
                        statusBar.setText("ERROR: Username taken");
                        break;
                    case 2:
                        statusBar.setText("Registration Successful!");
                }


            }
        });

    }
}