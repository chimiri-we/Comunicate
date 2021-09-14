package com.example.comunicate.sync.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.comunicate.MainActivity;
import com.example.comunicate.R;


public class ContentLogin extends AppCompatActivity {


    private Fragment fragmentR, fragmentImagen, fragmentRegistroDireccion, fragmentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
        Fragment fragmentL = new LoginActivity();
        fragmentR = new RegistroFragment();
       // fragmentImagen = new RegistrarImagenFragment();
      //  fragmentRegistroDireccion = new ActualizarDatos();
      //  fragmentLocation = new RegistroPerfilFragment();
       getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragment, fragmentL).commit();

    }
    public void onClick (View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (view.getId())
        {
            case R.id.registrarme: transaction.replace(R.id.contenedorFragment, fragmentR);
               transaction.addToBackStack(null);
                break;

            case R.id.buttonRegistro:
                Intent newintent = new Intent(this, MainActivity.class);
                startActivity(newintent);
                break;

        }
        transaction.commit();

    }
}
