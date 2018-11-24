package com.example.olive.proyecto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class VentanaPersonalizadaAdapter implements GoogleMap.InfoWindowAdapter {

    private final View vista;
    private Context context;



    private void rendowWindowText(Marker marcador, View view){
         String tit = marcador.getTitle();
        TextView titulo = view.findViewById(R.id.titulo);
        if (!tit.equals("")){
            titulo.setText(tit);
        }
        String info = marcador.getSnippet();
        TextView tvinfo = view.findViewById(R.id.infosnip);
        if (!info.equals("")){
            tvinfo.setText(info);
        }

    }
    //Metodo para inflar nuestra informacion dentro del layout del mapa
    public VentanaPersonalizadaAdapter(Context context) {
        this.context = context;
        vista = LayoutInflater.from(context).inflate(R.layout.ventana_informacion, null);
    }

    public VentanaPersonalizadaAdapter(View vista) {
        this.vista = vista;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, vista);
        return vista;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, vista);
        return vista;
    }
}
