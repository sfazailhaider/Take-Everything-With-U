package com.example.takeeverythingwithu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap visibleMap;
    private LatLng homeLocation = null;
    private LatLng newLocation = null;
    private LatLng stored = null;
    private float homeLatitude = 0;
    private float homeLongitude = 0;
    private double latitude = 0;
    private double longitude = 0;
    private int viewChoice;
    private double newLatitude = 0;
    private double newLongitude = 0;
    private Marker homeMarker;
    private int numberofHomeMarkers = 0;
    private int count = 0;
    public SharedPreferences mySharedPreference1;
    private Button confirmHome;
    private Button goBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mySharedPreference1 = getSharedPreferences("MyHomeLocation", Context.MODE_PRIVATE);
        SharedPreferences MapsSharedPreferences = getApplicationContext().getSharedPreferences("settingsChoice", Context.MODE_PRIVATE);
        viewChoice = MapsSharedPreferences.getInt("viewchoice", 0);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_home_page);
        mapFragment.getMapAsync(HomeActivity.this);

        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        confirmHome = (Button) findViewById(R.id.workbutton);

        confirmHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeLocation = new LatLng(homeLatitude, homeLongitude);
                SharedPreferences.Editor HomeEditor = mySharedPreference1.edit();
                HomeEditor.putFloat("homeLatitude", homeLatitude);
                HomeEditor.putFloat("homeLongitude", homeLongitude);
                HomeEditor.apply();
                Toast.makeText(HomeActivity.this, "Home Location Set", Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        visibleMap = googleMap;
        if (viewChoice == 1)
            visibleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.retromode));
        else if (viewChoice == 2)
            visibleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.nightmode));

        SharedPreferences MapsSharedPreferences = getApplicationContext().getSharedPreferences("MyHomeLocation", Context.MODE_PRIVATE);
        if (MapsSharedPreferences.getFloat("homeLatitude", 0) != 0 && MapsSharedPreferences.getFloat("homeLongitude", 0) != 0){
            latitude = MapsSharedPreferences.getFloat("homeLatitude", 0);
            longitude = MapsSharedPreferences.getFloat("homeLongitude", 0);
            stored = new LatLng(latitude, longitude);
            if (stored!= null) {
                MarkerOptions markerOptions = new MarkerOptions();
                //Setting a position for the market
                markerOptions.position(stored);
                //Giving lat long values to the LatLng variable userLocation
                markerOptions.title("Home");
                markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.home));
                homeMarker = visibleMap.addMarker(markerOptions);
                visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stored, 16), 2000, null);
            }
        }
        visibleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
             @Override
             public void onMapClick(@NonNull LatLng home) {
                 visibleMap.clear();
                 //Creating a marker where user taps
                 MarkerOptions markerOptions = new MarkerOptions();
                 //Setting a position for the market
                 markerOptions.position(home);
                 //Giving lat long values to the LatLng variable userLocation
                 markerOptions.title("Home");
                 markerOptions.icon(BitmapFromVector(getApplicationContext(), R.drawable.home));

                 homeLatitude = (float) home.latitude;
                 homeLongitude = (float) home.longitude;

                 //Zooming
                 visibleMap.animateCamera(CameraUpdateFactory.newLatLng(home));
                 homeMarker = visibleMap.addMarker(markerOptions);
                 numberofHomeMarkers += 1;
                 if (numberofHomeMarkers > 1) {
                     visibleMap.clear();
                     numberofHomeMarkers -= 1;
                     homeMarker.remove();
                     if (homeLatitude != 0 && homeLongitude != 0) {
                         homeMarker = visibleMap.addMarker(markerOptions);
                     }
                 }
             }
        });
            LocationListener locationTracker = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    newLatitude = location.getLatitude();
                    newLongitude = location.getLongitude();
                    newLocation = new LatLng(newLatitude, newLongitude);
                    if (count == 0 && stored == null) {
                        visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 16), 2000, null );
                        count++;
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };


            //must be defined after locationlistener. Manager calls for location updates
            LocationManager locationHandler = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
                return;
            }

        //this is in m. so this is equal to 5m
        float minimumDistance = 5;
        //this is in ms. so this is equal to 0.5s
        long minimumTime = 500;
        locationHandler.requestLocationUpdates(LocationManager.GPS_PROVIDER,minimumTime,minimumDistance,locationTracker);
        }

        public BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
            // below line is use to generate a drawable.
            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

            // below line is use to set bounds to our vector drawable.
            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

            // below line is use to create a bitmap for our
            // drawable which we have added.
            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            // below line is use to add bitmap in our canvas.
            Canvas canvas = new Canvas(bitmap);

            // below line is use to draw our
            // vector drawable in canvas.
            vectorDrawable.draw(canvas);

            // after generating our bitmap we are returning our bitmap.
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
    };

