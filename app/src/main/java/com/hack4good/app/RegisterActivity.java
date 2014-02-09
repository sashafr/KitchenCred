package com.hack4good.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by Sasha on 2/8/14.
 */
public class RegisterActivity extends FragmentActivity{

    final Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);

        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);

        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });

        Button regButton = (Button) findViewById(R.id.btnRegister);

        regButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                EditText emailEdt = (EditText) findViewById(R.id.login_email);
                String email = emailEdt.getText().toString();

                EditText pwEdt = (EditText) findViewById(R.id.login_pw);
                String pw = pwEdt.getText().toString();

                //TODO need to get password, name, total score
                //make sure to test that something is actually returned for stored pw because empty string will crash app
                //must have format X:X:X

                String name = "";
                int score = 0;
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

                        System.out.println(PasswordHash.createHash("test"));

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Incorrect Password")
                                .setCancelable(false)
                                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
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