package com.example.comunicate.utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 16/06/2017. 16
 */

/*id
nombreCompleto
apellido
estado
fecha_amigos
mensaje
hora_del_mensaje*/

public class Usuario {

    private int id;
    private String nombre;
    private String apellidos;
    private int estado;
    private String mensaje;
    private String hora;
    private String FotoPerfil;
    private String genero;
    private String ciudad;
    private String urlImagen;
    private String colonia;
    private String calle;
    private String fechaNacimiento;
    private String telefono;
    private String correo;
    private String password;
    private Bitmap imagen;

    public Usuario(int id, String contra) {
        this.id = id;
        this.password = contra;
    }

    public Usuario(int id, String newciudad, String newcalle, String newcolonia) {
        this.id = id;
        this.ciudad = newciudad;
        this.colonia = newcolonia;
        this.calle = newcalle;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




    public Usuario(){

    }

    public Usuario(int id, String nombre, int estado, String hora, String fotoPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
        this.hora = hora;
        FotoPerfil = fotoPerfil;
    }

    public Usuario(int id, String nombre, String mensaje, String hora, String fotoPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.mensaje = mensaje;
        this.hora = hora;
        FotoPerfil = fotoPerfil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFotoPerfil() {
        return FotoPerfil;
    }

    public CircleImageView setFotoPerfil(String fotoPerfil) {

            this.FotoPerfil = fotoPerfil;


            try {
                byte[] byteCode= Base64.decode(fotoPerfil,Base64.DEFAULT);
                //this.imagen= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);

                int alto=250;//alto en pixeles
                int ancho=300;//ancho en pixeles

                Bitmap foto= BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
                this.imagen=Bitmap.createScaledBitmap(foto,alto,ancho,true);


            }catch (Exception e){
                e.printStackTrace();
            }

        return null;
    }

        public Bitmap getImagen() {
            return imagen;
        }

        public void setImagen(Bitmap imagen) {
            this.imagen = imagen;
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

            Bitmap foto=BitmapFactory.decodeByteArray(byteCode,0,byteCode.length);
            this.imagen=Bitmap.createScaledBitmap(foto,alto,ancho,true);


        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
