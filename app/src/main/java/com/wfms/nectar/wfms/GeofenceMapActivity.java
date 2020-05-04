package com.wfms.nectar.wfms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wfms.nectar.Adapter.Geofencelist_Adapter;

import java.util.ArrayList;

public class GeofenceMapActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener, ResultCallback<Status> {

    // ...
    private GoogleApiClient googleApiClient;
    private MapFragment mapFragment;
    private GoogleMap map;
    private static final String TAG = MainActivity.class.getSimpleName();
    LatLng latLng,end_latLng;
    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My CreateGeofenceActivity";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters
    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
  /*  double lat=18.4901,end_lat=18.5089;
    double lon=73.9544,end_longitude=73.9259;*/

    double lat=18.4916933,end_lat=18.508934;
    double lon=73.952275,end_longitude=73.9259101;
    float radius=0;
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lat= Geofencelist_Adapter.latitude;
        lon= Geofencelist_Adapter.longitude;
        radius= Geofencelist_Adapter.radius;
        end_lat= Geofencelist_Adapter.end_latitude;
        end_longitude= Geofencelist_Adapter.end_longitude;

        latlngs.add(new LatLng(lat, lon));
        if(Geofencelist_Adapter.geofencetype.equalsIgnoreCase("SingleLine")) {
            latlngs.add(new LatLng(end_lat, end_longitude));
        }
        radius= Geofencelist_Adapter.radius;
        Log.d("radius",""+radius);
        // ...
        // create GoogleApiClient
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmap_layout);
        initGMaps();
        createGoogleApi();

        latLng = new LatLng(lat, lon);
        end_latLng = new LatLng(end_lat, end_longitude);

    }

    // Initialize GoogleMaps
    private void initGMaps() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d("", "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }

    // GoogleApiClient.ConnectionCallbacks connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("", "onConnected()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            markerForGeofence(latLng);
            startGeofence();
        }
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w("", "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w("", "onConnectionFailed()");
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick(" + latLng + ")");
       // markerForGeofence(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        markerLocation(new LatLng(lat, lon),new LatLng(end_longitude, end_longitude));
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
            drawGeofence();
        } else {
            // inform about fail
        }
    }

    // Draw CreateGeofenceActivity circle on GoogleMap
    private Polyline geoFenceLimits;
    private Circle geoFenceLimits1;
    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();
        if (geoFenceLimits1 != null)
            geoFenceLimits1.remove();
      /*  CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(50, 30, 144, 255))
                .fillColor(Color.argb(100, 30, 144, 255))
                .radius(radius);
        geoFenceLimits = map.addCircle(circleOptions);*/

       /* PolygonOptions p=new PolygonOptions()
                .strokeColor(Color.argb(50, 30, 144, 255))
                .fillColor(Color.argb(100, 30, 144, 255))
                .strokeWidth(7)
                .add(new LatLng(18.491, 73.952),
                        new LatLng(18.491, 73.954),
                        new LatLng(18.493, 73.956),
                        new LatLng(18.493, 73.956),
                        new LatLng(18.491, 73.952));

        geoFenceLimits=map.addPolygon(p);*/
       String a= Geofencelist_Adapter.geofencetype;

        if(Geofencelist_Adapter.geofencetype.equalsIgnoreCase("SingleLine")) {

            PolylineOptions p1 = new PolylineOptions()
                    .color(Color.BLUE)
                    .add(new LatLng(lat, lon),
                            new LatLng(end_lat, end_longitude));


            geoFenceLimits = map.addPolyline(p1);
        }
        else
        {
            CircleOptions circleOptions = new CircleOptions()
                    .center(geoFenceMarker.getPosition())
                    .strokeColor(Color.argb(50, 30, 144, 255))
                    .fillColor(Color.argb(100, 30, 144, 255))
                    .radius(radius);
            geoFenceLimits1 = map.addCircle(circleOptions);
        }
        /*   .strokeColor(Color.argb(50, 30, 144, 255))
                .fillColor(Color.argb(100, 30, 144, 255))*/
    }



    private Marker locationMarker;

    // Create a Location Marker
    private void markerLocation(LatLng latLng, LatLng lng) {
        Log.i(TAG, "markerLocation(" + latLng + ")");
        for (LatLng point : latlngs) {

        String title = point.latitude + ", " + point.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(point)
                .title(title);
        MarkerOptions markerOptions1 = new MarkerOptions()
                .position(lng)
                .title(title);
        if (map != null) {
            // Remove the anterior marker
         //   if (locationMarker != null)
            //    locationMarker.remove();
            locationMarker = map.addMarker(markerOptions);
         //   locationMarker = map.addMarker(markerOptions1);
            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            map.animateCamera(cameraUpdate);
        }
        }
    }

    private Marker geoFenceMarker;

    // Create a marker for the geofence creation
    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence(" + latLng + ")");
        for (LatLng point : latlngs) {
        String title = point.latitude + ", " + point.longitude;
        // Define marker options


        MarkerOptions markerOptions = new MarkerOptions()
                .position(point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        MarkerOptions markerOptions1 = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);
        if (map != null) {
            // Remove last geoFenceMarker
          //  if (geoFenceMarker != null)
          //     geoFenceMarker.remove();

            geoFenceMarker = map.addMarker(markerOptions);
          //  geoFenceMarker = map.addMarker(markerOptions1);
        }
        }
    }
    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius1) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(lat, lon, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Start Geofence creation process
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        if (geoFenceMarker != null) {
            Geofence geofence = (Geofence) createGeofence(geoFenceMarker.getPosition(), GEOFENCE_RADIUS);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(geofenceRequest);
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Add the created GeofenceRequest to the device's monitoring list
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //   int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                googleApiClient,
                request,
                createGeofencePendingIntent()
        ).setResultCallback(this);


}}