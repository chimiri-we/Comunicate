package com.example.comunicate.ui.detalle;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.comunicate.R;
import com.example.comunicate.mensajes.chat;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetallePerfilActivity extends AppCompatActivity {
    CollapsingToolbarLayout collapser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_perfil);
        Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) // Habilitar up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapser = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
       String nombreReceptor = getIntent().getStringExtra("nombre");
        String id_receptor = getIntent().getStringExtra("id_receptor");
        collapser.setTitle(nombreReceptor);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intet = new Intent(DetallePerfilActivity.this, chat.class);
                intet.putExtra("id_receptor", id_receptor);
                intet.putExtra("nombre", nombreReceptor);
                startActivity(intet);
            }
        });

      //  String id = getIntent().getStringExtra();

        DetallePerfilFragment fragment = (DetallePerfilFragment)
                getSupportFragmentManager().findFragmentById(R.id.perfil_detail_container);
        if (fragment == null) {
            fragment = DetallePerfilFragment.newInstance(id_receptor);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.perfil_detail_container, fragment)
                    .commit();
        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}


