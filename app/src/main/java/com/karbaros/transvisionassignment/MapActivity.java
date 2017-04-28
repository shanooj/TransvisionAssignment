package com.karbaros.transvisionassignment;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {


    GoogleMap mMap;
    DrawerLayout mRootLyout;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    DBAdapterMap dbAdapterMap;

    FrameLayout flResultPrice;
    TextView tvDistance;
    TextView tvDuration;
    TextView tvPrice;
    Button btnClose;

    String distance;
    String duration;

    int ratePerKm;
    int ratePerMin;
    int hour;
    int min;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServiceAvailable()) {
            setContentView(R.layout.activity_map);
            // mRootLyout = (DrawerLayout) findViewById(R.id.drawer_layout);
            initMap();
        } else {
            Snackbar.make(mRootLyout, "Google Play Service Not Available", Snackbar.LENGTH_LONG).show();
            //layout for map is not available
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Initializing
        flResultPrice = (FrameLayout) findViewById(R.id.flResultPrice);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        btnClose = (Button) findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flResultPrice.setVisibility(View.GONE);
                mMap.clear();
            }
        });

        MarkerPoints = new ArrayList<>();
        dbAdapterMap = new DBAdapterMap(getBaseContext());


    }

    @Override
    protected void onStart() {
        super.onStart();
        Cursor cursor = dbAdapterMap.selectData();
        if (cursor.getCount() == 0) {

            if (dbAdapterMap.insertData("km", 10))
                Log.i("pricekm", "Done");
            if (dbAdapterMap.insertData("min", 1))
                Log.i("pricemin", "Done");
        } else {

            cursor.moveToFirst();
            ratePerKm = cursor.getInt(2);
            cursor.moveToNext();
            ratePerMin = cursor.getInt(2);
            cursor.close();
           /* ratePerKm = dbAdapterMap.selectPrice("km");
            ratePerMin = dbAdapterMap.selectPrice("min");*/

            Log.i("pricekm", ratePerKm + "");
            Log.i("pricemin", ratePerMin + "");

        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void initMap() {
        MapFragment mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.frmntMap);
        mMapFragment.getMapAsync(this);
    }


    public boolean googleServiceAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (apiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog = apiAvailability.getErrorDialog(this, isAvailable, 0);
            dialog.show();

        } else {
            Toast.makeText(getBaseContext(), "Cant connect to play service", Toast.LENGTH_LONG).show();

        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        // setLocation(11.869281, 75.352635);

        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (MarkerPoints.size() > 1) {
                    MarkerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                MarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /*
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (MarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 2) {
                    LatLng origin = MarkerPoints.get(0);
                    LatLng dest = MarkerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionUrl(origin, dest);
                    Log.d("onMapClick", url);
                    FetchUrl FetchUrl = new FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }

            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(null);
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }


    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(9);
                lineOptions.color(Color.RED);

            }
            Log.i("result", "Distance:" + distance + ", Duration:" + duration);

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {

                // Log.d("onPostExecute", "without Polylines drawn");
            }

            // Setting Price

            tvDistance.setText("" + distance);
            tvDuration.setText("" + duration);

            String[] arrDistance = distance.split(" ");

          //  Log.i("result", arrDistance[0] + "");

            double price1 = Double.parseDouble(arrDistance[0]) * ratePerKm;
           // Log.i("result", price1 + "");

            String[] arrDuration = duration.split(" ");
            if (arrDuration.length > 2) {

                hour = Integer.parseInt(arrDuration[0]);
                min = Integer.parseInt(arrDuration[2]);
            } else {
                min = Integer.parseInt(arrDuration[0]);
            }

            long totalTime = hour * 60 + min;
            double price2 = totalTime * ratePerMin;

            if (price1 > price2)
                tvPrice.setText("Rs " + price1);
            else
                tvPrice.setText("Rs " + price2);


            flResultPrice.setVisibility(View.VISIBLE);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String getDirectionUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void setLocation(double lat, double lng) {
        Log.i("mape", "" + lat + "  " + lng);
        LatLng mLatLng = new LatLng(lat, lng);

        mMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title("sample")
                .snippet("sample")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 20));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
