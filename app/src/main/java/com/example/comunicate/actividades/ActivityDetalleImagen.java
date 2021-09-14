package com.example.comunicate.actividades;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.comunicate.R;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.ui.FotoManagerGotev;
import com.example.comunicate.utils.Usuario;

import net.gotev.uploadservice.data.UploadInfo;
import net.gotev.uploadservice.network.ServerResponse;

public class ActivityDetalleImagen extends AppCompatActivity {

    private ImageButton camara;
    private ImageView fotoPerfil;
    private EditText nombreImagen;
    private Button upload;
    private Uri output;

      private String URL_USER_FOTO_PERFIL = "https://servicioparanegocio.es/Trabajos/actualizarDatos.php";
    private FotoManagerGotev fotoManagerGotev;
    private Button btnGaleria;
    private Button btnCamara;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir_foto);
        fotoPerfil = (ImageView) findViewById(R.id.fotoPerfil);
        btnGaleria = (Button) findViewById(R.id.btnGaleria);
        btnCamara = (Button) findViewById(R.id.btnCamara);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        BaseDatos bdLocal = new BaseDatos(getApplicationContext());
        Usuario usuario = new Usuario();
        bdLocal.validarUsuario();

        Intent intent = getIntent();
        Bitmap b = intent.getParcelableExtra("imagen");
        Glide.with(this)
                .load(b)
                .error(R.drawable.ic_person_24)
                .centerCrop()
                .into(fotoPerfil);
/*
        //  String extras = getIntent().getStringExtra("urlImagen");
        Glide.with(this)
                .asBitmap()
                .load(getIntent().getStringExtra("urlImagen"))
                .error(R.drawable.ic_person_24)
                .centerCrop()
                .into(fotoPerfil);
*/
      //  Picasso.get().load(URL_USER_FOTO_PERFIL).error(R.drawable.ic_persona).into(fotoPerfil);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fotoManagerGotev = new FotoManagerGotev(this, URL_USER_FOTO_PERFIL) {
            @Override
            public void onProgress(UploadInfo uploadInfo) {

            }

            @Override
            public void onError(UploadInfo uploadInfo, Exception exception) {

            }

            @Override
            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {

            }

            @Override
            public void onCancelled(UploadInfo uploadInfo) {

            }
        };
        fotoManagerGotev.setParameterNamePhoto("file");
        fotoManagerGotev.setEliminarFoto(false);
       // fotoManagerGotev.setIdUser(Preferences.obtenerPreferenceString(this,Preferences.PREFERENCE_USUARIO_LOGIN));

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotoManagerGotev.subirFotoGaleria();
            }
        });

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotoManagerGotev.subirFotoCamara();
            }
        });
        FotoManagerGotev.verifyStoragePermissions(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri u = fotoManagerGotev.onActivityResult(requestCode,resultCode,data);
        if(u!=null)fotoPerfil.setImageURI(u);
    }
}
