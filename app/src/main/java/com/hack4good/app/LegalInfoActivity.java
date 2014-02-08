package com.hack4good.app;

import com.google.android.gms.common.GooglePlayServicesUtil;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Sasha on 2/8/14.
 */
public class LegalInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legal_info);

        TextView legalInfoTextView = (TextView) findViewById(R.id.legal_info);
        String openSourceSoftwareLicenseInfo =
                GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(this);
        if (openSourceSoftwareLicenseInfo != null) {
            legalInfoTextView.setText(openSourceSoftwareLicenseInfo);
        } else {
            legalInfoTextView.setText(R.string.play_services_not_installed);
        }
    }

}
