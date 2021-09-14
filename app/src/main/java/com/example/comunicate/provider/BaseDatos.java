package com.example.comunicate.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.example.comunicate.utils.Usuario;

public class BaseDatos  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;                                                                                                                              ;
    private static final String DATABASE_NAME = "comunicate.db";
    private static final String ID_USUARIO = "id";

    private static final String TABLE_USUARIO = "Usuario";
  private  static final String TABLE_CHAT = "Chat";

    public BaseDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String dbUsuario="create table Usuario("+
                "id INTEGER PRIMARY KEY,"+
                "nombre TEXT,"+
                "apellido TEXT,"+
                "uri_imagen TEXT,"+
                "correo TEXT,"+
                "password TEXT,"+
                "telefono TEXT,"+
                "genero TEXT,"+
                "fechadeNacimiento TEXT,"+
                "ciudad_estado TEXT,"+
                "colonia TEXT,"+
                "calle TEXT)";
        db.execSQL(dbUsuario);

        String dbChat="create table Chat("+
                "id_chat INTEGER PRIMARY KEY,"+
                "emisor TEXT,"+
                "receptor TEXT,"+
                "mensaje TEXT,"+
                "tipoMensaje TEXT,"+
                "horaDelMensaje TEXT,"+
                "estado TEXT)";
        db.execSQL(dbChat);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT);
        onCreate(db);
    }

    public Usuario verdatosUsuario() {
        SQLiteDatabase db = this.getWritableDatabase();
        Usuario usuario = null;
        Cursor cursor;

        cursor = db.rawQuery("select * from Usuario", null);
        if (cursor.moveToFirst()) {

            usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setApellidos(cursor.getString(2));
            usuario.setUrlImagen(cursor.getString(3));
            usuario.setCorreo(cursor.getString(4));
            usuario.setPassword(cursor.getString(5));
            usuario.setTelefono(cursor.getString(6));
            usuario.setGenero(cursor.getString(7));
            usuario.setFechaNacimiento(cursor.getString(8));
            usuario.setCiudad(cursor.getString(9));
            usuario.setColonia(cursor.getString(10));
            usuario.setCalle(cursor.getString(11));


        }
        cursor.close();
        return usuario;
    }

    public void guardarImagen(String s, String path) {

        ContentValues values = new ContentValues();
        values.put("uri_imagen", path);
        SQLiteDatabase db = this.getWritableDatabase();
        //  db.insert(TABLE_USUARIO, null, values);
        db.update(TABLE_USUARIO, values, ID_USUARIO+ " = ?", new String[]{s});
    }

    public void obtenerRutaImagen(Uri miPath, String id) {

        ContentValues values = new ContentValues();
        values.put("uri_imagen", String.valueOf(miPath));
        SQLiteDatabase db = this.getWritableDatabase();
        //  db.insert(TABLE_USUARIO, null, values);
        db.update(TABLE_USUARIO, values, ID_USUARIO+ " = ?", new String[]{id});
    }

    public void actualizarUsuario(Usuario usuarios) {
        ContentValues values = new ContentValues();
        values.put("nombre", usuarios.getNombre());
        values.put("telefono", usuarios.getTelefono());
        values.put("correo", usuarios.getCorreo());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_USUARIO, values, ID_USUARIO + " = ?", new String[]{String.valueOf(usuarios.getId())});
    }

    public void actualizarContrasena(Usuario usuarios) {
        ContentValues values = new ContentValues();

        values.put("password", usuarios.getPassword());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_USUARIO, values, ID_USUARIO + " = ?", new String[]{String.valueOf(usuarios.getId())});
    }

    public void actualizarDireccion(Usuario usuarios) {
        ContentValues values = new ContentValues();
        values.put("direccion", usuarios.getCiudad());
        values.put("colonia", usuarios.getColonia());
        values.put("calle", usuarios.getCalle());

        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_USUARIO, values, ID_USUARIO + " = ?", new String[]{String.valueOf(usuarios.getId())});
    }

    public Usuario validarUsuario() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        Usuario usuario=null;
        cursor = db.rawQuery("select * from Usuario", null);
        if (cursor.moveToFirst()) {

            usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setUrlImagen(cursor.getString(3));
            usuario.setCorreo(cursor.getString(4));



        }
        cursor.close();
        return usuario;
    }
}
