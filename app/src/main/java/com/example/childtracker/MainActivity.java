package com.example.childtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback, LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    protected LocationManager locationManager;

    //sets default lat and lon
    double lat = 0;
    double lon = 0;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//checks for permissions
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);






        //-------------------
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



        //checks for permissions
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //loads when map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();


        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in BCC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));



    }

    //enables users location
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }






    public void proceed(View view) {



        Firebase.setAndroidContext(this);
        //gets referneces from firebase
        Firebase myFirebaseRef = new Firebase("https://mobiletracker-d4f90.firebaseio.com/");


        EditText id = (EditText) findViewById(R.id.editText);

        //sends data to server
        myFirebaseRef.child(id.getText().toString()).child("lat").setValue(lat);
        myFirebaseRef.child(id.getText().toString()).child("lon").setValue(lon);



//confirms if database has gotten updated values
        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());

                Toast.makeText(getApplicationContext(),"uploaded to server!!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Toast.makeText(getApplicationContext(),"error uploading to server!!", Toast.LENGTH_SHORT).show();

            }


        });


    }
    //nupdates marker if location has been changes
    @Override
    public void onLocationChanged(Location location) {
        //prints out lat and lon
        System.out.println("lat:" + location.getLatitude());
        System.out.println(location.getLongitude());


        lat = location.getLatitude();
        lon = location.getLongitude();
        LatLng sydney = new LatLng(lat, lon);

        //sets markers on maps
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in BCC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }
//not using this
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    //not using this
    @Override
    public void onProviderEnabled(String provider) {

    }
    //not using this
    @Override
    public void onProviderDisabled(String provider) {

    }
    //not using this
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
    //not using this
    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }


}
