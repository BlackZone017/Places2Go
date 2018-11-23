package com.example.olive.proyecto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olive.proyecto.modelos.PlaceInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import android.support.v4.app.FragmentActivity;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));

    private Boolean permisoConcedido = false;
    private GoogleMap mapa;
    private AutoCompletarLugares completar; //gg

    //is for interacting with location using fused location provider.
    private FusedLocationProviderClient client;
    private GoogleApiClient mGoogleApiClient;
    private static final float ZOOM = 15f;

    private Button buton;
    private AutoCompleteTextView buscador;
    private ImageView gps;

    private PlaceInfo lugar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        buscador=findViewById(R.id.buscador);
        gps=findViewById(R.id.ic_gps);
        buton = findViewById(R.id.button);

        getLocationPermission();

    }

    private void init(){
        Log.d(TAG, "init: Iniciando");


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        //Esto hace que cuando le de click a una de las opciones que te tira el
        // autocompletar te lleve al lugar directamente
        buscador.setOnItemClickListener(mAutoCompletarClickListener);

        //llenar el constructor con contexto, google API cliente, latitud-longitud, filtro
        completar = new AutoCompletarLugares(this, mGoogleApiClient, LAT_LNG_BOUNDS,null);

        buscador.setAdapter(completar); //Le paso la referencia de la Clase AutoCompletarLugares al textView buscador

        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                geoLocate();
            }
        });
        buscador.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID==EditorInfo.IME_ACTION_SEARCH || actionID == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction()== KeyEvent.ACTION_DOWN
                        || keyEvent.getAction()==KeyEvent.KEYCODE_ENTER){

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Pulsaste el icono de gps");
                getDeviceLocation();
            }
        });
    }

    //METODO GEOLOCATE
    private void geoLocate(){
        Log.d(TAG, "geoLocate: ubicandooo");

        String searchString = buscador.getText().toString();

        Geocoder geocoder = new Geocoder((MapActivity.this));
        List<Address> list = new ArrayList<>();
        try{

            list = geocoder.getFromLocationName(searchString,1);

        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage());
        }

        if (list.size()>0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: Ubicacion encontrada: " + address.toString());

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), ZOOM,address.getAddressLine(0));

            //Toast.makeText(this,address.toString(), Toast.LENGTH_SHORT).show();

        }



    }

    //Metodo para adquirir la localizacion actual del dispositivo
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
                                    ZOOM,"Mi ubicacion");

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

    //METODO PARA MOVER LA CAMARA
    private void moveCamera(LatLng longitud_latitud, float zoom, String titulo) {
        Log.d(TAG, "moveCamera: moviendo la camara a latitud: " + longitud_latitud.latitude + ", longitud: " + longitud_latitud.longitude);
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(longitud_latitud, zoom));
        if(!titulo.equals("Mi ubicacion")){
            MarkerOptions opciones = new MarkerOptions()
                    .position(longitud_latitud)
                    .title(titulo);
            mapa.addMarker(opciones);
        }
    EsconderTeclado();
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

            init();
            EsconderTeclado();
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

    //METODO PARA ESCONDER EL TECLADO MIENTRAS SE MUEVE LA CAMARA
    private void EsconderTeclado(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    /*
        ---------------------------- GOOGLE PLACES API: AutoCompletar Sitios------------------------------
     */
    //completar = mPlaceAutoCompleteAdapter
    // Clase PlaceInfo = PlaceInfo.java
    //METODO mAutoCompletarClickListener:
    private AdapterView.OnItemClickListener mAutoCompletarClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            EsconderTeclado();

            final AutocompletePrediction item = completar.getItem(i);
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient,placeId);
            placeResult.setResultCallback(detalleLugaresCallback);
        }
    };

    //Metodo para traer toda la informacion del lugar buscado
    private ResultCallback<PlaceBuffer> detalleLugaresCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                Log.d(TAG, "onResult: error consultando el lugar: "+ places.getStatus().toString());
                places.release(); //Para prevenir perdida de memoria debemos soltar el <PlaceBuffer>
                return;
            }
            final Place place = places.get(0);

            //Para prevenir de que algunas de las informaciones del lugar este null
            try{

            lugar = new PlaceInfo();

            lugar.setId(place.getId());                                 //id del lugar
                Log.d(TAG, "onResult: id: "+place.getId());
            lugar.setNombre(place.getName().toString());                //Nombre del lugar
                Log.d(TAG, "onResult: nombre: "+place.getName());
            /*
            lugar.setAtributos(place.getAttributions().toString());     //atributos del lugar
                Log.d(TAG, "onResult: atributos: "+place.getAttributions());
                */
            lugar.setVista(place.getViewport().toString());             //vista del lugar
                Log.d(TAG, "onResult: vista: "+place.getViewport());
            lugar.setTelefono(place.getPhoneNumber().toString());       //Numero Telefono del lugar
                Log.d(TAG, "onResult: tel: "+place.getPhoneNumber());
            lugar.setDireccion(place.getAddress().toString());          //Direccion del lugar
                Log.d(TAG, "onResult: direccion: "+place.getAddress());
            lugar.setWeb(place.getWebsiteUri());                        //Pagina Web del lugar
                Log.d(TAG, "onResult: web: "+place.getWebsiteUri());
            lugar.setLatlong(place.getLatLng());                        //Latitud y Longitud del lugar
                Log.d(TAG, "onResult: latitud, long: "+place.getLatLng());
            lugar.setTipoLugar(place.getPlaceTypes().toString());       //Que tipo de lugar es
                Log.d(TAG, "onResult: tipo Lugar: "+place.getPlaceTypes());

            Log.d(TAG, "onResult: Lugar: "+lugar.toString());

            }catch(NullPointerException ex){
                Log.d(TAG, "onResult: NullPointerExcepction: " + ex.getMessage());
            }

            moveCamera(new LatLng(place.getViewport().getCenter().latitude,place.getViewport().getCenter().longitude)
                    , ZOOM, lugar.getNombre()); //movemos la camara hasta la direccion elegida

            places.release(); //Para prevenir perdida de memoria debemos soltar el <PlaceBuffer>
        }
    };
}
