package com.hack4good.app;

<<<<<<< HEAD
/**
 * Created by Sasha on 2/8/14.
 */
public class RegisterActivity {
}
=======
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Sasha on 2/8/14.
 */
public class RegisterActivity extends FragmentActivity{

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
                Intent i = new Intent(getApplicationContext(), GroceryMapActivity.class);
                startActivity(i);
            }
        });

    }
}
>>>>>>> 7c91a9d099cc983c98cf24b9d32a39ef878f7da9
