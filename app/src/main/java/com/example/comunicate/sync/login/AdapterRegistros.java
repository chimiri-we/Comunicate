package com.example.comunicate.sync.login;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AdapterRegistros extends FragmentPagerAdapter {


    public AdapterRegistros(FragmentManager spf) {
        super(spf);
    }


    @Override
    public Fragment getItem(int position) {
        if(position==0) return new RegistrarImagenFragment();
        else if(position==1) return new ActualizarDatos();
        // else if(position==2) return new FragmentUsuarioLista();

        return null;
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) return "Registro Imagen";

        else if(position==1)return "Registro Datos";
        // else if(position==2)return "Amigos";


        return null;
    }
}
