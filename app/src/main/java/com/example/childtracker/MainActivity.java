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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;


public class MainActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback, LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    protected LocationManager locationManager;
    double lat = 0;
    double lon = 0;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String [] { android.Manifest.permission.ACCESS_FINE_LOCATION },
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


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

        Firebase myFirebaseRef = new Firebase("https://mobiletracker-d4f90.firebaseio.com/");


        EditText id = (EditText) findViewById(R.id.editText);


        myFirebaseRef.child(id.getText().toString()).child("lat").setValue(lat);
        myFirebaseRef.child(id.getText().toString()).child("lon").setValue(lon);


       // studentid = (EditText) findViewById(R.id.editstudentid);
      //  cellphone = (EditText) findViewById(R.id.editcellphone);
       // schoolemail = (EditText) findViewById(R.id.editschoolemail);


//        if(name.getText().length() == 0 || schoolemail.getText().length() == 0){
//
//            Toast.makeText(getApplicationContext(),"error!!", Toast.LENGTH_SHORT).show();
//        }else{
//
//
//            myFirebaseRef.child(name.getText().toString()).child("studentID").setValue(studentid.getText().toString());
//            myFirebaseRef.child(name.getText().toString()).child("cellphone").setValue(cellphone.getText().toString());
//            myFirebaseRef.child(name.getText().toString()).child("schoolemail").setValue(schoolemail.getText().toString());
//
//
//        }

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                //schoolemail.getText().clear();
               // name.getText().clear();
               // studentid.getText().clear();
               // cellphone.getText().clear();
                Toast.makeText(getApplicationContext(),"uploaded to server!!", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Toast.makeText(getApplicationContext(),"error uploading to server!!", Toast.LENGTH_SHORT).show();

            }


        });






    }

    @Override
    public void onLocationChanged(Location location) {

        System.out.println("lat:" + location.getLatitude());
        System.out.println(location.getLongitude());
        lat = location.getLatitude();
        lon = location.getLongitude();

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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
