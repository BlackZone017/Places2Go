package com.example.olive.proyecto.modelos;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {

    private String id;
    private String nombre;
    private String atributos;
    private String vista;
    private String telefono;
    private String direccion;
    private Uri web;
    private LatLng latlong;
    private String tipoLugar;

    public PlaceInfo(String id, String nombre, String atributos, String vista,
                     String telefono, String direccion, Uri web, LatLng latlong, String tipoLugar) {
        this.id = id;
        this.nombre = nombre;
        this.atributos = atributos;
        this.vista = vista;
        this.telefono = telefono;
        this.direccion = direccion;
        this.web = web;
        this.latlong = latlong;
        this.tipoLugar = tipoLugar;
    }

    public PlaceInfo() {

    }

    //----------METODOS GETTER----------\\
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getAtributos() {
        return atributos;
    }

    public String getVista() {
        return vista;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public Uri getWeb() {
        return web;
    }

    public LatLng getLatlong() {
        return latlong;
    }

    public String getTipoLugar() {
        return tipoLugar;
    }

    //----------METODOS SETTER----------\\


    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAtributos(String atributos) {
        this.atributos = atributos;
    }

    public void setVista(String vista) {
        this.vista = vista;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setWeb(Uri web) {
        this.web = web;
    }

    public void setLatlong(LatLng latlong) {
        this.latlong = latlong;
    }

    public void setTipoLugar(String tipoLugar) {
        this.tipoLugar = tipoLugar;
    }

    //----------METODO TOSTRING----------\\

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", atributos='" + atributos + '\'' +
                ", vista='" + vista + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", web=" + web +
                ", latlong=" + latlong +
                ", tipoLugar='" + tipoLugar + '\'' +
                '}';
    }
}
