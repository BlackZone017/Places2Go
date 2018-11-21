package com.example.olive.proyecto;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //para confirmar que se tiene la version correcta de google play services
    private static  final int ERROR_REQUEST=9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isServicesOK()){
            init();
        }
    }

    public void init(){

        Button btnMapa = findViewById(R.id.btnMapa);
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
            }
        });


    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: Confirmando que tienen la version correcta del play services ");

        int disponible = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        
        if (disponible == ConnectionResult.SUCCESS){
            // TODO ESTA CORRECTO
            Log.d(TAG, "isServicesOK: Google play Services esta funcionando");
            
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(disponible)){
            //Ocurrio un error pero puede resolverse
            Log.d(TAG, "isServicesOK: Ocurrio un error pero puede resolverse");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,disponible,ERROR_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this,"No puedes hacer Peticiones a Maps",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
