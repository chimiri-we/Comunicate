package com.example.comunicate.utils;

public class SolicitudAmistad {

    private int id_solicitud;
    private int id_amigo;
    private String nombre;
   // private String apellidos;
    private int estado_solicitud;
    private int tipo_solicitud;
    private String fecha_solicitud;
    private  String url_foto;

    public SolicitudAmistad(){}

    public SolicitudAmistad(int id_solicitud, int id_amigo, String nombre, int estado_solicitud, int tipo_solicitud, String fecha_solicitud) {
        this.id_solicitud = id_solicitud;
        this.id_amigo = id_amigo;
        this.nombre = nombre;
        this.estado_solicitud = estado_solicitud;
        this.tipo_solicitud = tipo_solicitud;
        this.fecha_solicitud = fecha_solicitud;
    }

    public int getId_solicitud() {
        return id_solicitud;
    }

    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }

    public void setId_solicitud(int id_solicitud) {
        this.id_solicitud = id_solicitud;
    }

    public int getId_amigo() {
        return id_amigo;
    }

    public void setId_amigo(int id_amigo) {
        this.id_amigo = id_amigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado_solicitud() {
        return estado_solicitud;
    }

    public void setEstado_solicitud(int estado_solicitud) {
        this.estado_solicitud = estado_solicitud;
    }

    public int getTipo_solicitud() {
        return tipo_solicitud;
    }

    public void setTipo_solicitud(int tipo_solicitud) {
        this.tipo_solicitud = tipo_solicitud;
    }

    public String getFecha_solicitud() {
        return fecha_solicitud;
    }

    public void setFecha_solicitud(String fecha_solicitud) {
        this.fecha_solicitud = fecha_solicitud;
    }
}
