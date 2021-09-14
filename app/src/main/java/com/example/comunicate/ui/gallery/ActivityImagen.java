package com.example.comunicate.ui.gallery;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.comunicate.R;
import com.example.comunicate.utils.Imagenes;

public class ActivityImagen extends AppCompatActivity {

    public static final String EXTRA_PARAM_ID = "com.example.comunicate.ui.extra.ID";
    public static final String VIEW_NAME_HEADER_IMAGE = "imagen_compartida";
    private Imagenes itemDetallado;
    private ImageView imagenExtendida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_detalle);

        usarToolbar();

        // Obtener el coche con el identificador establecido en la actividad principal
  //      itemDetallado = Imagenes.getItem(getIntent().getIntExtra(EXTRA_PARAM_ID, 0));

    //    imagenExtendida = (ImageView) findViewById(R.id.imagen_extendida);

        cargarImagenExtendida();
    }

    private void cargarImagenExtendida() {
     //   Glide.with(imagenExtendida.getContext())
           //     .load(itemDetallado.getIdDrawable())
           //     .into(imagenExtendida);
    }

    private void usarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}