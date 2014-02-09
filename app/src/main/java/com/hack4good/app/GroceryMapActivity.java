package com.hack4good.app;

import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.Location;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;


/**
 * Created by Sasha on 2/8/14.
 */
public class GroceryMapActivity extends FragmentActivity
        implements
        OnMarkerClickListener,
        OnInfoWindowClickListener {

    final Context context = this;

    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }

            //TODO add link to edit info for a location
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private GoogleMap mMap;

    private LatLngBounds mBounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        mBounds = mMap.getProjection().getVisibleRegion().latLngBounds;

        // Add lots of markers to the map.
        addMarkersToMap(mBounds);

        // Setting an info window adapter allows us to change the both the contents and look of the
        // info window.
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                mBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
                addMarkersToMap(mBounds);
            }
        });

        Intent intent = getIntent();
        String score = Integer.toString(intent.getIntExtra("SCORE", 0));

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(this);
        tv.setTextSize(40);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setShadowLayer(8, 0, 0, Color.BLUE);
        tv.setTextColor(Color.BLUE);
        tv.setLayoutParams(lparams);
        tv.setText(score);
        LinearLayout ll = (LinearLayout) this.findViewById(R.id.score_box);
        ll.addView(tv);

        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        double latitude = myLocation.getLatitude();
        double longitude = myLocation.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void addMarkersToMap(LatLngBounds bounds) {

        //LatLng coordNE = bounds.northeast;
        //LatLng coordSW = bounds.southwest;

        MapData dataClient = new MapData();
        dataClient.setCoordinates(bounds);
        dataClient.execute();

//        // Uses a colored icon.
//        mBrisbane = mMap.addMarker(new MarkerOptions()
//                .position(BRISBANE)
//                .title("Brisbane")
//                .snippet("Population: 2,074,200")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//
//        // Uses a custom icon with the info window popping out of the center of the icon.
//        mSydney = mMap.addMarker(new MarkerOptions()
//                .position(SYDNEY)
//                .title("Sydney")
//                .snippet("Population: 4,627,300")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
//                .infoWindowAnchor(0.5f, 0.5f));

    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /** Called when the Clear button is clicked. */
    public void onClearMap(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
    }

    /** Called when the Reset button is clicked. */
    public void onResetMap(View view) {
        if (!checkReady()) {
            return;
        }
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
        mBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        addMarkersToMap(mBounds);
    }

    //
    // Marker related listeners.
    //

    @Override
    public boolean onMarkerClick(final Marker marker) {
        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getBaseContext(), "Click Info Window", Toast.LENGTH_SHORT).show();
    }

    private class MapData extends AsyncTask <String,String,String> {

        public String url = "";
        private double ne_lon = 0.00;
        private double  ne_lat = 0.00;
        private double sw_lon = 0.00;
        private double sw_lat = 0.00;
        private ProgressDialog progDialog = null;
        public String json = "";


        /* set coordinates for map data */
        public void setCoordinates(LatLngBounds bounds) {
            if (bounds.northeast != null) {
                ne_lon = bounds.northeast.longitude;
                ne_lat = bounds.northeast.latitude;
            }
            if (bounds.southwest != null) {
                sw_lon = bounds.southwest.longitude;
                sw_lat = bounds.southwest.latitude;
            }
        }

        @Override
        protected String doInBackground(String... objects) {
            // build URL for HTTP request
            String strJSON = "";
            url = getString(R.string.db_url) + getString(R.string.get_location) ;
            // clean up string

            url = url.replace("%3A", ":");
            url = url.replace("%2F", "/");
            url = url.replace("%3", "?");

            url += "ne=" + ne_lon + ";" + ne_lat + "&sw=" + sw_lon + ";" + sw_lat;

            // make request
            InputStream stream = null;
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = httpClient.execute(httpPost);

                stream = httpResponse.getEntity().getContent();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                // parse response
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                // build json string
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                stream.close();

                /* isolate JSON blob */
                strJSON = sb.toString();
                strJSON = sb.substring(strJSON.indexOf("<body>") + "<body>".length() , strJSON.indexOf("</body>"));
                strJSON = strJSON.replace("\t", "");
                strJSON = strJSON.replace("\n", "");

            } catch (Exception e) {
                Log.e("Buffer", "Error converting result " + e.toString());
            }

            json = strJSON;
            return strJSON;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            json = o;
            // ready to go!!
            try {
                JSONArray arrData = new JSONArray(json);
                JSONObject obj;

                // generate map markers from JSON
                for (int i = 0; i < arrData.length(); i++) {
                    obj = arrData.getJSONObject(i);
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(obj.getDouble("lat"), obj.getDouble("lon")))
                            .title(obj.getString("title"))
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
