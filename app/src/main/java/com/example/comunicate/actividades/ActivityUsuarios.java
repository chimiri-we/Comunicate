package com.example.comunicate.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.comunicate.R;
import com.example.comunicate.adapter.AdapterUsuarios;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.ui.FragmentBuscar;
import com.example.comunicate.utils.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


/**
 * Created by user on 14/05/2017.
 */

public class ActivityUsuarios extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    BaseDatos bdLocal;
    Usuario usuario;
    String nombreUsuario;
    private static final String TABLE_USUARIO = "Usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_usuarios);
        Toolbar tool = findViewById(R.id.toolbar_chat);
        getSupportActionBar();
        setSupportActionBar(tool);
        setHasOptionsMenu(true);

        setTitle("Mensajeria");

        tabLayout = (TabLayout) findViewById(R.id.tabLayoutUsuarios);
        viewPager = (ViewPager) findViewById(R.id.viewPagerUsuarios);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new AdapterUsuarios(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    //setTitle("Chat");
                }else if(position==1){
                    //setTitle("Registro");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setOffscreenPageLimit(3);
    }

    
    private void setHasOptionsMenu(boolean b) {
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  actualizarSolicitudes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (item.getItemId()) {

            case R.id.action_buscar:
               buscarAmigos();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void buscarAmigos() {
        Intent intet = new Intent(this, FragmentBuscar.class);

        startActivity(intet);


    }




/*    public void actualizarSolicitudes(){
        SolicitudesJson sj = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                bus.post(new LimpiarListaAmigos());
                bus.post(new LimpiarListaSolicitudes());
                bus.post(new LimpiarListaUsuarios());
                try {
                    JSONArray jA = j.getJSONArray("resultado");
                    for(int i=0;i<jA.length();i++){
                        JSONObject json = jA.getJSONObject(i);
                        String id = json.getString("id");
                        int ID = Integer.parseInt(id);
                        if(!id.equals(Preferences.obtenerPreferenceString(ActivityUsuarios.this,Preferences.PREFERENCE_USUARIO_LOGIN))) {
                            String nombreCompleto = json.getString("nombre") + " " + json.getString("apellidos");
                            String estado = json.getString("estado");
                            UsuarioBuscadorAtributos usuario = new UsuarioBuscadorAtributos();
                            usuario.setFotoPerfil(json.getString("imagen"));
                            usuario.setId(ID);
                            usuario.setNombre(nombreCompleto);
                            usuario.setEstado(1);
                            Solicitudes s;
                            switch (estado) {
                                case "2"://solicitudes
                                    usuario.setEstado(2);
                                    s = new Solicitudes();
                                    s.setId(ID);
                                    s.setNombre(nombreCompleto);
                                    s.setFotoPerfil(json.getString("imagen"));
                                    s.setHora(json.getString("fecha_amigos"));
                                    s.setEstado(2);
                                    bus.post(s);
                                    break;
                                case "3"://solicitudes
                                    usuario.setEstado(3);
                                    s = new Solicitudes();
                                    s.setId(ID);
                                    s.setNombre(nombreCompleto);
                                    s.setFotoPerfil(json.getString("imagen"));
                                    s.setHora(json.getString("fecha_amigos"));
                                    s.setEstado(3);
                                    bus.post(s);
                                    break;
                                case "4"://amigos
                                    usuario.setEstado(4);
                                    AmigosAtributos a = new AmigosAtributos();
                                    a.setId(ID);
                                    a.setNombre(nombreCompleto);
                                    a.setFotoPerfil(json.getString("imagen"));
                                    a.setMensaje(json.getString("mensaje"));
                                    a.setType_mensaje(json.getString("tipo_mensaje"));
                                    String hora_mensaje = json.getString("hora_del_mensaje");
                                    String hora_vector[] = hora_mensaje.split("\\,");
                                    a.setHora(hora_vector[0]);
                                    bus.post(a);
                                    break;
                            }
                            bus.post(usuario);
                        }else{
                            URL_USER_FOTO_PERFIL = json.getString("imagen");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void solicitudErronea() {
                Toast.makeText(ActivityUsuarios.this, "ocurrio un error al momento de pedir datos en activity usuarios", Toast.LENGTH_SHORT).show();
            }
        };

       // String usuario = Preferences.obtenerPreferenceString(this,Preferences.PREFERENCE_USUARIO_LOGIN);
        sj.solicitudJsonGET(this, SolicitudesJson.URL_GET_ALL_DATOS+usuario);
    }
*/
}
