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
import java.security.spec.InvalidKeySpecException;

import com.hack4good.app.PasswordHash;

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

                //TODO need to get password, name, total score
                //make sure to test that something is actually returned for stored pw because empty string will crash app
                //must have format X:X:X

                String name = "";
                int score = 50;
                String storedPw = "1000:09d6765a6ed0c86bf0003fee87d94e232312940d31db86eb:24772a287be80573c012f661641d0c65e174b4e77b083964";

                try {
                    if(!pw.equals("") && PasswordHash.validatePassword(pw, storedPw)) {
                        Intent i = new Intent(getApplicationContext(), GroceryMapActivity.class);
                        i.putExtra("USERNAME", name);
                        i.putExtra("TOTAL_SCORE", score);
                        i.putExtra("USER_EMAIL", email);
                        startActivity(i);
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        // set title
                        alertDialogBuilder.setTitle("Authentication Failed");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Incorrect Password")
                                .setCancelable(false)
                                .setPositiveButton("Retry",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        LoginActivity.this.finish();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}