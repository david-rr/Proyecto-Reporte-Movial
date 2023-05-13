package com.example.reportemovial;

import java.util.Date;

public class Reporte {
    private String descripcion;
    private String estado;
    //private String imagen;
    private String tipo;
    //private Date fecha;
    public Reporte(){}

    public Reporte(String descripcion, String estado, /**String imagen,*/ String tipo /**, Date fecha*/){
        this.descripcion = descripcion;
        this.estado = estado;
        //this.imagen = imagen;
        this.tipo = tipo;
        //this.fecha = fecha;
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

     /**public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }*/

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }*/
}
