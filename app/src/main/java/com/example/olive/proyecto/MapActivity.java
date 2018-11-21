package com.example.olive.proyecto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    private Boolean permisoConcedido = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mapa;
    //is for interacting with location using fused location provider.
    private FusedLocationProviderClient client;
    private static final float ZOOM = 15f;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getLocationPermission();
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: Obteniendo la ubicacion del dispositivo");
        client = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (permisoConcedido) {
                Task ubicacion = client.getLastLocation();
                ubicacion.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // si la tarea fue exitosa
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Ubicacion encontrada");
                            Location ubicacion_actual = (Location) task.getResult();

                            moveCamera(new LatLng(ubicacion_actual.getLatitude(), ubicacion_actual.getLongitude()),
                                    ZOOM);

                        } else {
                            Log.d(TAG, "onComplete: Ubicacion no encontrada");
                            Toast.makeText(MapActivity.this, "No fue posible encontrar la ubicacion", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.d(TAG, "getDeviceLocation: Error: " + e.getMessage());
        }


    }

    private void moveCamera(LatLng longitud_latitud, float zoom) {
        Log.d(TAG, "moveCamera: moviendo la camara a latitud: " + longitud_latitud.latitude + ", longitud: " + longitud_latitud.longitude);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(longitud_latitud, zoom));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "El Mapa esta Listo", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: Mapa esta Listo");
        mapa = googleMap;

        if (permisoConcedido) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

    private void initMap(){
        Log.d(TAG, "initMap: Iniciando Mapa");
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);


    }

    private void getLocationPermission(){

        Log.d(TAG, "getLocationPermission: Obteniendo permisos");

        String[] permisos = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                permisoConcedido = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,permisos,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,permisos,LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: llamado.");
        permisoConcedido=false;
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    // se concedio un permiso
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permisoConcedido = false;
                            Log.d(TAG, "onRequestPermissionsResult: permiso fallo");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permiso concedido");
                    permisoConcedido = true;
                    // aqui se inicializa el mapa
                    initMap();

                }
            }
        }
    }
}
