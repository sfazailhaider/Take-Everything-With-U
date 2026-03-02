package com.example.takeeverythingwithu;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap visibleMap;
    private LatLng userLocation; //this is a variable of type LatLng which stores current location
    private LatLng destinationLocation = null; //this is a variable which stores finallocation
    private LatLng startLocation = null;
    private LatLng homeLocation = null;
    private LatLng workLocation = null;

    private int count = 0;
    private int currentCount = 0;
    private int destinationNumberOfMarkers = 0;
    private int currentNumberOfMarkers = 0;

    private double userLatitude;
    private double userLongitude;
    private double startLatitude = 0;
    private double startLongitude = 0;
    private double destinationLatitude;
    private double destinationLongitude;
    private Marker currentMarker;
    private Marker destinationMarker;
    private Marker finalDestinationMarker;
    private Marker startMarker;
    private Marker homeMarker;
    private Marker workMarker;
    private int goingtowork = 0;
    private int viewChoice;
    private int flag = 0;
    private int notificationFinal = 0;
    private double distance = 1000000;
    private double distanceChoice = 0.1;
    private String notificationMessage;
    private double homeLatitude;
    private double homeLongitude;
    private double workLatitude;
    private double workLongitude;
    public Button showCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        createNotificationChannel();

        SharedPreferences MapsSharedPreferences = getApplicationContext().getSharedPreferences("MyHomeLocation", Context.MODE_PRIVATE);
        homeLatitude = MapsSharedPreferences.getFloat("homeLatitude", 0);
        homeLongitude = MapsSharedPreferences.getFloat("homeLongitude", 0);
        if (homeLocation == null && homeLatitude != 0 && homeLongitude != 0) {
            homeLocation = new LatLng(homeLatitude, homeLongitude);
        }

        MapsSharedPreferences = getApplicationContext().getSharedPreferences("MyWorkLocation", Context.MODE_PRIVATE);
        workLatitude = MapsSharedPreferences.getFloat("workLatitude", 0);
        workLongitude = MapsSharedPreferences.getFloat("workLongitude", 0);
        if (workLocation == null && workLatitude != 0 && workLongitude != 0) {
            workLocation = new LatLng(workLatitude, workLongitude);
        }

        MapsSharedPreferences = getApplicationContext().getSharedPreferences("settingsChoice", Context.MODE_PRIVATE);
        viewChoice = MapsSharedPreferences.getInt("viewchoice", 0);
        distanceChoice = MapsSharedPreferences.getFloat("distance", (float) 0.1);
        notificationMessage = MapsSharedPreferences.getString("notification", "Your journey is ending, so grab all your valuables!");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        Button confirmDestination;
        Button showDestination;
        Button showStart;
        Button showHome;
        Button showWork;
        Button goHome;
        Button goWork;


        confirmDestination = (Button) findViewById(R.id.button_confirm_destination);
        showDestination = (Button) findViewById(R.id.button_show_destination);
        showStart = (Button) findViewById(R.id.workbutton);
        showCurrent = (Button) findViewById(R.id.button_show_currentlocation);
        showCurrent.setEnabled(false);
        showHome = (Button) findViewById(R.id.show_home);
        showHome.setEnabled(false);
        showWork = (Button) findViewById(R.id.showwork);
        showWork.setEnabled(false);
        goHome = (Button) findViewById(R.id.gohome);
        goHome.setEnabled(false);
        goWork = (Button) findViewById(R.id.gowork);
        goWork.setEnabled(false);

        if (workLocation != null){
            showWork.setEnabled(true);
            goWork.setEnabled(true);
        }
        if (homeLocation != null){
            showHome.setEnabled(true);
            goHome.setEnabled(true);
        }

        if (destinationLocation != null){
        }

        confirmDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    destinationLocation = new LatLng(destinationLatitude, destinationLongitude);
                    finalDestinationMarker = visibleMap.addMarker(new MarkerOptions().position(destinationLocation).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    visibleMap.moveCamera(CameraUpdateFactory.newLatLng(destinationLocation));
                    flag = 1;
                    count += 1;
                    showDestination.setEnabled(true);
                    showStart.setEnabled(true);
                    goWork.setEnabled(false);
                    goHome.setEnabled(false);
                    confirmDestination.setEnabled(false);
                }
            }
        });


        showDestination.setEnabled(false);
        showDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 18), 2000, null );
            }
        });

        showStart.setEnabled(false);
        showStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 18), 2000, null );
            }
        });


        showCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker.getPosition(), 18), 2000, null );
            }
        });

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0 ) {
                    visibleMap.clear();
                    startMarker = visibleMap.addMarker(new MarkerOptions().position(startLocation).title("Start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    if (homeLocation != null) {
                        homeMarker = visibleMap.addMarker(new MarkerOptions().position(homeLocation).title("Home").icon(BitmapFromVector(getApplicationContext(), R.drawable.home)));
                    }

                    if (workLocation != null) {
                        workMarker = visibleMap.addMarker(new MarkerOptions().position(workLocation).title("Work").icon(BitmapFromVector(getApplicationContext(), R.drawable.work)));
                    }
                    destinationLocation = new LatLng(homeLatitude, homeLongitude);
                    finalDestinationMarker = visibleMap.addMarker(new MarkerOptions().position(destinationLocation).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 16), 2000, null );
                    flag = 1;
                    count += 1;
                    showDestination.setEnabled(true);
                    showStart.setEnabled(true);
                    goWork.setEnabled(false);
                    goHome.setEnabled(false);
                    confirmDestination.setEnabled(false);
                }
            }
        });

        goWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0 ) {
                    visibleMap.clear();
                    startMarker = visibleMap.addMarker(new MarkerOptions().position(startLocation).title("Start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    if (homeLocation != null) {
                            homeMarker = visibleMap.addMarker(new MarkerOptions().position(homeLocation).title("Home").icon(BitmapFromVector(getApplicationContext(), R.drawable.home)));
                        }

                        if (workLocation != null) {
                            workMarker = visibleMap.addMarker(new MarkerOptions().position(workLocation).title("Work").icon(BitmapFromVector(getApplicationContext(), R.drawable.work)));
                        }
                    destinationLocation = new LatLng(workLatitude, workLongitude);
                    finalDestinationMarker = visibleMap.addMarker(new MarkerOptions().position(destinationLocation).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 16), 2000, null );
                    flag = 1;
                    count += 1;
                    showDestination.setEnabled(true);
                    showStart.setEnabled(true);
                    goWork.setEnabled(false);
                    goHome.setEnabled(false);
                    confirmDestination.setEnabled(false);
                    goingtowork = 1;
                }
            }
        });

        showHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(homeLocation, 18), 2000, null );
            }
        });

        showWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(workLocation, 18), 2000, null );
            }
        });
    }


    private void distancefind(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        distance = dist * 60 * 1.1515;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notifyme";
            String description = "NOTIFICATION FOR LOCATION";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyme", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        visibleMap = googleMap;

        if (viewChoice == 1)
            visibleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.retromode));
        else if (viewChoice == 2)
            visibleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.nightmode));

        if (homeLocation != null) {
            homeMarker = visibleMap.addMarker(new MarkerOptions().position(homeLocation).title("Home").icon(BitmapFromVector(getApplicationContext(), R.drawable.home)));
            visibleMap.moveCamera(CameraUpdateFactory.newLatLng(homeLocation));
        }

        if (workLocation != null) {
            workMarker = visibleMap.addMarker(new MarkerOptions().position(workLocation).title("Work").icon(BitmapFromVector(getApplicationContext(), R.drawable.work)));
            visibleMap.moveCamera(CameraUpdateFactory.newLatLng(workLocation));
        }

        visibleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng destination) {
                if (flag == 0) {
                    //Creating a marker where user taps
                    MarkerOptions markerOptions = new MarkerOptions();
                    //Setting a position for the market
                    markerOptions.position(destination);
                    //Giving lat long values to the LatLng variable userLocation
                    markerOptions.title("Destination");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

                    destinationLatitude = destination.latitude;
                    destinationLongitude = destination.longitude;

                    //Zooming
                    visibleMap.animateCamera(CameraUpdateFactory.newLatLng(destination));
                    destinationMarker = visibleMap.addMarker(markerOptions);
                    destinationNumberOfMarkers += 1;
                    if (destinationNumberOfMarkers > 1) {
                        visibleMap.clear();
                        destinationNumberOfMarkers -= 1;
                        destinationMarker.remove();
                        if (homeLocation!=null)
                            homeMarker = visibleMap.addMarker(new MarkerOptions().position(homeLocation).title("Home").icon(BitmapFromVector(getApplicationContext(), R.drawable.home)));
                        if (workLocation!=null)
                            workMarker = visibleMap.addMarker(new MarkerOptions().position(workLocation).title("Work").icon(BitmapFromVector(getApplicationContext(), R.drawable.work)));
                        if (startLatitude != 0 && startLongitude != 0)
                            startMarker = visibleMap.addMarker(new MarkerOptions().position(startLocation).title("Start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    }

                    if (startLocation == null) {
                        startLatitude = userLatitude;
                        startLongitude = userLongitude;
                        startLocation = new LatLng(userLatitude, userLongitude);
                    }
                    destinationMarker = visibleMap.addMarker(markerOptions);
                }
            }


        });

        LocationListener locationTracker = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (startLatitude == 0 && startLongitude == 0) {
                    startLatitude = userLocation.latitude;
                    startLongitude = userLocation.longitude;
                    startLocation = new LatLng(userLocation.latitude, userLocation.longitude);
                    startMarker = visibleMap.addMarker(new MarkerOptions().position(startLocation).title("Start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                    visibleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 16), 2000, null );
                }
                if (destinationLocation != null) {
                    showCurrent.setEnabled(true);
                    if (currentCount == 0) {
                        currentMarker = visibleMap.addMarker(new MarkerOptions().position(userLocation).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        currentCount++;
                    }
                    visibleMap.moveCamera(CameraUpdateFactory.newLatLng(currentMarker.getPosition()));
                    currentNumberOfMarkers += 1;
                    if (currentNumberOfMarkers >= 1) {
                        currentMarker.remove();
                        currentNumberOfMarkers--;
                        currentMarker = visibleMap.addMarker(new MarkerOptions().position(userLocation).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                    visibleMap.addMarker(new MarkerOptions().position(userLocation)
                            // below line is use to add custom marker on our map.
                            .icon(BitmapFromVector(getApplicationContext(), R.drawable.walk)));
                }

                if (destinationLocation != null) {
                    distancefind(destinationLocation.latitude, destinationLocation.longitude, userLocation.latitude, userLocation.longitude);
                    if (distance < distanceChoice && notificationFinal == 0) {
                        // Get instance of Vibrator from current Context
                        if (Build.VERSION.SDK_INT >= 26) {
                            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(1000);
                        }
                        addNotification();
                        notificationFinal++;
                    }
                }
            }


            public void addNotification() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel();
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this, "notifyme")
                        .setSmallIcon(R.drawable.icon)
                        .setContentTitle("Get Ready!")
                        .setContentText(notificationMessage);

                Intent notificationIntent;

                if (goingtowork == 1) {
                    notificationIntent = new Intent(MapsActivity.this, Work.class);
                }
                else {
                    notificationIntent = new Intent(MapsActivity.this, Home.class);
                }

                PendingIntent contentIntent = PendingIntent.getActivity(MapsActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(contentIntent);

                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.notify(0, builder.build());
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //this is in m. so this is equal to 5m
        float minimumDistance = 5;
        //this is in ms. so this is equal to 0.5s
        long minimumTime = 500;
        locationHandler.requestLocationUpdates(LocationManager.GPS_PROVIDER, minimumTime, minimumDistance, locationTracker);
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
