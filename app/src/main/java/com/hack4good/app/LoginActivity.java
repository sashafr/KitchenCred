package com.hack4good.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Sasha on 2/8/14.
 */
public class LoginActivity extends FragmentActivity{

    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        Button loginButton = (Button) findViewById(R.id.btnLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText emailEdt = (EditText) findViewById(R.id.login_email);
                String email = emailEdt.getText().toString();

                EditText pwEdt = (EditText) findViewById(R.id.login_pw);
                String pw = pwEdt.getText().toString();

                //TODO need to get user password and salt from db

                String storedPw = "test";
                byte[] salt = "".getBytes();
                String newPw = "";
                try {
                    newPw = new String(getHashWithSalt(pw, salt));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                    if(newPw.equals(storedPw)) {
                        Intent i = new Intent(getApplicationContext(), GroceryMapActivity.class);
                        startActivity(i);
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        // set title
                        alertDialogBuilder.setTitle("Authentication Failed");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage(newPw)
                                .setCancelable(false)
                                .setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        LoginActivity.this.finish();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }

            }
        });
    }

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    public byte[] getHashWithSalt(String input, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(salt);
        byte[] hashedBytes = digest.digest(input.getBytes());
        return hashedBytes;
    }
}