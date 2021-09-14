package com.example.comunicate.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Imagenes {

    private String FotoPerfil;
    private String urlImagen;
private int id_usuario;
    private int id_imagen;
    private String nombre_imagen;
    private Bitmap imagen;

    public Imagenes(String fotoPerfil, String urlImagen, int id_usuario, int id_imagen, String nombre_imagen, Bitmap imagen) {
        FotoPerfil = fotoPerfil;
        this.urlImagen = urlImagen;
        this.id_usuario = id_usuario;
        this.id_imagen = id_imagen;
        this.nombre_imagen = nombre_imagen;
        this.imagen = imagen;
    }

    public Imagenes() {

    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;

        try {
            byte[] byteCode= Base64.decode(urlImagen,Base64.DEFAULT);
            //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

            int alto=100;//alto en pixeles
            int ancho=150;//ancho en pixeles

            Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen=Bitmap.createScaledBitmap(foto,alto,ancho,true);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getFotoPerfil() {
        return FotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        FotoPerfil = fotoPerfil;
    }



    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_imagen() {
        return id_imagen;
    }

    public void setId_imagen(int id_imagen) {
        this.id_imagen = id_imagen;
    }

    public String getNombre_imagen() {
        return nombre_imagen;
    }

    public void setNombre_imagen(String nombre_imagen) {
        this.nombre_imagen = nombre_imagen;
    }

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }
}
