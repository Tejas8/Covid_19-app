package com.personal.covid_19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    SmoothBottomBar bottomBar;
    SharedPreferences pref;
    static final Integer CALL = 0x2;
    static final Integer LOCATION = 0x1;
    FusedLocationProviderClient mFusedLocationClient;
    double latTextView, lonTextView;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore; List<String> list;


    SharedPreferences.Editor editor;
    private FirebaseAuth mAUTH;
    private List<LatLng> dangerousArea=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("step1","homemain");
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            Log.i("step1","homemain");
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.framecontent,new homemain()).commit();
        }

        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        Log.i("step2","homemain");
        firestore=FirebaseFirestore.getInstance();
        initArea(currentUser);
        pref = getApplicationContext().getSharedPreferences("location", MODE_PRIVATE);
        askForPermission(Manifest.permission.CALL_PHONE,CALL);
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        askForPermission(Manifest.permission.ACCESS_COARSE_LOCATION,LOCATION);

        mAUTH = FirebaseAuth.getInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//
//        getLastLocation();

        bottomBar=findViewById(R.id.bottomBar);

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelect(int i) {
                switch (i)
                {
                    case 0:
                        getSupportFragmentManager().
                                beginTransaction().replace(R.id.framecontent,new homemain()).commit();
                        break;

                    case 2:
                        getSupportFragmentManager().
                                beginTransaction().replace(R.id.framecontent,new homeFragment()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framecontent,new resource()).commit();
                        break;
                    case 1:
                        Log.d("TAG", "onItemSelect: "+dangerousArea.size());
                       Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                        Bundle args = new Bundle();
                        args.putSerializable("ARRAYLIST",(Serializable)dangerousArea);
                        intent.putExtra("BUNDLE",args);
                       startActivity(intent);
                        break;

                }



            }
        });
    }
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }
    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latTextView=location.getLatitude();
                                    lonTextView=location.getLongitude();
                                    Log.d("TAG", "onComplete: "+latTextView);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latTextView=mLastLocation.getLatitude();
            lonTextView=mLastLocation.getLongitude();
            editor = pref.edit();
            editor.putFloat("latitude",(float) latTextView);
            editor.putFloat("long",(float) lonTextView);
            Log.d("TAG", "onComplete: "+latTextView);



        }
    };

//    @Override
//    public void onStart() {
//        super.onStart();
//        FirebaseUser currentUser=mAUTH.getCurrentUser();
//
//
//        // Check if we need to display our OnboardingSupportFragment
////        if (!sharedPreferences.getBoolean(
////                LoginOffline.COMPLETED_ONBOARDING_PREF_NAME, false)) {
////            // The user hasn't seen the OnboardingSupportFragment yet, so show it
////            Intent intent=new Intent(this,LoginActivity.class);
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            startActivity(intent);
//        if (currentUser == null) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }

    private void initArea(FirebaseUser user) {

        if(user!=null) {
            String userid = user.getUid();

            firestore.collection("userid").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Log.d("Hiee1", userid);
                        list = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!userid.equalsIgnoreCase(document.getId().toString())) {
                                list.add(document.getId());
                            }
                        }
                        firebasedata[] city = new firebasedata[list.size()];


                        Log.d("Hiee", list.toString());
                        Log.d("Hiee", list.toString());
                        for (int i = 0; i < list.size(); i++) {
                            DocumentReference docRef = firestore.collection("userid").document(list.get(i));
                            int finalI = i;
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    firebasedata data = documentSnapshot.toObject(firebasedata.class);

                                    if(data.getTest().equalsIgnoreCase("positive")) {
                                        dangerousArea.add(new LatLng(data.getLat(), data.getLongi()));
                                        Log.d("TAG", "onSuccess: "+dangerousArea.get(0).toString());
                                    }



                                }
                            });

                        }
//                       }
                    }
                    else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });


        }


    }






}




