package com.example.comunicate.mensajes;

/**
 * Created by Yomaira on 06/01/2017.
 */

public class MensajeDeTexto {
    private String id;
    private String emisor;
    private String receptor;
    private String mensaje;
    private int tipoMensaje;
    private String HoraDelMensaje;
    private int estado;

    public MensajeDeTexto() {}

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public MensajeDeTexto(String emisor, String formattedDate, String enviarM, int tipoMensaje) {
        this.emisor = emisor;
        this.HoraDelMensaje = formattedDate;
        this.mensaje = enviarM;
        this.tipoMensaje = tipoMensaje;
    }

    public String getId() {
        return id;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(int tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getHoraDelMensaje() {
        return HoraDelMensaje;
    }

    public void setHoraDelMensaje(String horaDelMensaje) {
        HoraDelMensaje = horaDelMensaje;
    }
}
