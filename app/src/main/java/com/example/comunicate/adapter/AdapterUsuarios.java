package com.example.comunicate.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.comunicate.ui.FragmentAmigos;
import com.example.comunicate.ui.FragmentListaChat;
import com.example.comunicate.ui.gallery.FragmentUsuarioLista;

/**
 * Created by user on 14/05/2017.
 */

public class AdapterUsuarios extends FragmentPagerAdapter {


    public AdapterUsuarios(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0) return new FragmentAmigos();
        else if(position==1) return new FragmentUsuarioLista();
        else if(position==2) return new FragmentListaChat();

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0) return "Solicitud Amistad";

       else if(position==1)return "Usuarios";
        else if(position==2)return "Chat";


        return null;
    }
}
