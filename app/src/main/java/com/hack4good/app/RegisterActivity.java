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
                EditText nameEdt = (EditText) findViewById(R.id.reg_fullname);
                String fullname = nameEdt.getText().toString();

                EditText emailEdt = (EditText) findViewById(R.id.reg_email);
                String email = emailEdt.getText().toString();

                EditText pwEdt = (EditText) findViewById(R.id.reg_password);
                String pw = pwEdt.getText().toString();
                try {

                    if (email.equals("") || fullname.equals("") || pw.equals("")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        // set title
                        alertDialogBuilder.setTitle("Error");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Please complete all fields")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    } else {

                        String hashedPw = PasswordHash.createHash(pw);

                        //TODO need to check that email isn't in db

                        //TODO set to false
                        boolean uniqueEmail = true;

                        if(uniqueEmail) {

                            //TODO if email is unique, insert name, email, hashedPw
                            //make sure to test that something is actually returned for stored pw because empty string will crash app
                            //must have format X:X:X

                            Intent i = new Intent(getApplicationContext(), GroceryMapActivity.class);
                            i.putExtra("USERNAME", fullname);
                            i.putExtra("USER_EMAIL", email);
                            startActivity(i);

                        } else {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                            // set title
                            alertDialogBuilder.setTitle("Error");

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("This email is already registered: " + email)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();
                        }

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