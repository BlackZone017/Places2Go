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


    /* Este método recibe como parámetro el marcador que se posicionó en el mapa luego de hacer una busqueda,
     * se encarga de asignar la información al view que va a contener la información y luego va a inflarse
     * Nota: El snippet es el resto de informació, es decir, el cuerpo del texto.
     * */
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
    /*Metodo para inflar nuestra informacion dentro del layout del mapa
        Recibe el contexto, en este caso el context del layout en el que se encuentra el mapa e infla el layout de la ventana
        de la información dentro del layout del mapa.
     */
    public VentanaPersonalizadaAdapter(Context context) {
        this.context = context;
        vista = LayoutInflater.from(context).inflate(R.layout.ventana_informacion, null);
    }

    public VentanaPersonalizadaAdapter(View vista) {
        this.vista = vista;
    }

    @Override
    //Ejecutan el metodo rendowwindowtext y retorna la vista con el marcador y la informacion puesta.
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
