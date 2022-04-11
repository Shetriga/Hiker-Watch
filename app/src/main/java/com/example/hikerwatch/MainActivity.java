package com.example.hikerwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    TextView latitudeTextView, longitudeTextView, accuracyTextView, altitudeTextView, firstAddressDetailTextView,
             secondAddressDetailTextView, thirdAddressDetailTextView, fourthAddressDetailTextView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                    //Log.i("locationInfo", "The permission is granted");
                }

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        firstAddressDetailTextView = findViewById(R.id.firstAddressDetailTextView);
        secondAddressDetailTextView = findViewById(R.id.secondAddressDetailTextView);
        thirdAddressDetailTextView = findViewById(R.id.thirdAddressDetailTextView);
        fourthAddressDetailTextView = findViewById(R.id.fourthAddressDetailTextView);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {

                    List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (listAddresses != null && listAddresses.size() > 0) {

                        //String address = "";
                        latitudeTextView.setText("Latitude: " + listAddresses.get(0).getLatitude());
                        longitudeTextView.setText("Longitude: " + listAddresses.get(0).getLongitude());

                        accuracyTextView.setText("Accuracy: " + location.getAccuracy());

                        altitudeTextView.setText("Altitude: " + location.getAltitude());

                        if (listAddresses.get(0).getAddressLine(0) != null) {
                            firstAddressDetailTextView.setText(listAddresses.get(0).getAddressLine(0));
                        }

                    }

                } catch (IOException e) {

                    e.printStackTrace();

                }
                Log.i("listenerInfo", "Location Updated");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
            //Log.i("listenerInfo", "Gone into the ELSE!");
        }

    }
}