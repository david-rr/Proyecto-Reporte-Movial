package com.example.reportemovial;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Map;

public class Reporte {
    private String descripcion;
    private String estado;
    private String imagen;
    private String tipo;
    private Map<String, Object> ubicacion;
    private String usuario;
    public Reporte(){}

    public Reporte(String descripcion, String estado, String imagen, String tipo, String usuario, Map<String, Object>ubicacion){
        this.descripcion = descripcion;
        this.estado = estado;
        this.imagen = imagen;
        this.tipo = tipo;
        this.usuario = usuario;
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

     public String getImagen() {return imagen;}

    public void setImagen(String imagen) {this.imagen = imagen;}

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUsuario(){return usuario;}

    public void setUsuario(String usuario){this.usuario = usuario;}

    public Map<String, Object> getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Map<String, Object> ubicacion) {
        this.ubicacion = ubicacion;
    }
}
