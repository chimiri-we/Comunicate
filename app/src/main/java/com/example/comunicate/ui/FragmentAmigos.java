package com.example.comunicate.ui;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comunicate.R;
import com.example.comunicate.adapter.AmigosAdapter;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.utils.SolicitudAmistad;
import com.example.comunicate.utils.Usuario;
import com.example.comunicate.web.JSONParser;
import com.example.comunicate.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by user on 8/05/2017.
 */

public class FragmentAmigos extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener  {
    private static final String IDEMISOR = "id";
    private static final String EMISOR = "emisor";
    private RecyclerView rv;
    private List<SolicitudAmistad> atributosList;

    private LinearLayout layoutVacio;

    AmigosAdapter adapter;
    JSONParser jsonParser = new JSONParser();
    JsonObjectRequest jsonObject;
    private static final String TAG_MESSAGE = "message";

    String ID, NOMBREEMISOR;

    private static final String URL = "https://servicioparanegocio.es/Trabajos/lista_amigos.php";
BaseDatos bdLocal;

    Usuario usuario;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_chats,container,false);

        atributosList = new ArrayList<>();
        bdLocal = new BaseDatos(getContext().getApplicationContext());
        usuario = bdLocal.validarUsuario();
        ID = String.valueOf(usuario.getId());
        NOMBREEMISOR = usuario.getNombre();

        rv = (RecyclerView) v.findViewById(R.id.amigosRecycler);
       // layoutVacio = (LinearLayout) v.findViewById(R.id.layoutVacio);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(lm);
       // cargarListaUsuarios();

        ListaUsuarios();
       /* if (atributosList.size() > 0) {

            rv.setVisibility(View.VISIBLE);
            // adapter = new UsuariosAdapter(getContext(), atributosList);
           // rv.setAdapter(adapter);
        }else {
            rv.setVisibility(View.GONE);
            Toast.makeText(getContext(),"no hay datos", Toast.LENGTH_SHORT).show();
            //  LayoutInflater inflater = LayoutInflater.from(getContext());


        }*/
        //  adapter = new UsuariosAdapter(getContext(), atributosList);
        //  rv.setAdapter(adapter);

        // verificarSiTenemosAmigos();

        return v;
    }

    public void ListaUsuarios(){
            HashMap<String,String> chat = new HashMap<>();
            chat.put(EMISOR, NOMBREEMISOR);

            jsonObject=new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(chat), this, this);
            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObject);


        }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();

        Log.d("ERROR: ", error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        SolicitudAmistad soliAmistad;
        JSONArray  json=response.optJSONArray("amigo");
        // JSONObject jsonObject;

        try {

            for (int i = 0; i< Objects.requireNonNull(json).length(); i++){
                soliAmistad = new SolicitudAmistad();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                soliAmistad.setId_amigo(jsonObject.optInt("id_amigo"));
                soliAmistad.setNombre(jsonObject.optString("nombre"));
             //   u.setApellidos(jsonObject.optString("apellido"));
                soliAmistad.setEstado_solicitud(jsonObject.optInt("estado_solicitud"));
                soliAmistad.setTipo_solicitud(jsonObject.optInt("tipo_solicitud"));
                soliAmistad.setFecha_solicitud(jsonObject.optString("hora_dela_solicitud"));
                soliAmistad.setUrl_foto(jsonObject.optString("url_imagen"));


                atributosList.add(soliAmistad);
                adapter = new AmigosAdapter(atributosList, getContext());
                rv.setAdapter(adapter);
                Toast.makeText(getContext(), "chats "+response, Toast.LENGTH_LONG).show();

                Log.i("respuesta", String.valueOf(response));

            }
            //   adapter = new UsuariosAdapter(getContext(), nuevaListaCliente);
            //  recicler.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Verifica tu coneccion", Toast.LENGTH_LONG).show();
            Log.i("respuesta", String.valueOf(response));
        }
    }
}
/*
        @Override
        protected String doInBackground(String... args) {
          //  int success;
          //  String correo = usernameEditText.getText().toString();
         //   String password = passwordEditText.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("id", ID));
           //     params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                Usuario user;
                JSONArray array = json.optJSONArray("datos");
                JSONObject jsonObject;
                // success = json.getInt(TAG_SUCCESS);
                if (array != null) {

                    Log.d("Login Successful!", json.toString());
                    // save user data

                    try {

                        for (int i = 0; i< Objects.requireNonNull(array).length(); i++){
                            user=new Usuario();
                             jsonObject=new JSONObject();
                            jsonObject=array.getJSONObject(i);

                            user.setId(jsonObject.optInt("id"));
                            user.setNombre(jsonObject.optString("nombre"));
                            user.setApellidos(jsonObject.optString("apellido"));
                            user.setTelefono(jsonObject.optString("telefono"));
                            user.setUrlImagen(jsonObject.optString("imagen"));

                            atributosList.add(user);
                            //  ID_PRODUCTO = String.valueOf(usuario.getId_producto());
                        }

                         adapter = new UsuariosAdapter(getContext(), atributosList);
                        rv.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "No se ha podido establecer conexión con el servidor" , Toast.LENGTH_LONG).show();

                    }


                } else {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }


        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
//            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(getContext(), file_url, Toast.LENGTH_LONG).show();


            }
        }
    }




    public void verificarSiTenemosAmigos(){
        if(atributosList.isEmpty()){
            layoutVacio.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        }else{
            layoutVacio.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private void actualizarTarjetas(){
        adapter.notifyDataSetChanged();
        verificarSiTenemosAmigos();
    }

    //id
    //nombre
    //ultimo_mensaje
    //hora
    @SuppressLint("NotifyDataSetChanged")
    public void agregarAmigo(String fotoDePerfil, String nombre, String ultimoMensaje, String hora, int id){
        AmigosAtributos amigosAtributos = new AmigosAtributos();
        amigosAtributos.setFotoPerfil(fotoDePerfil);
        amigosAtributos.setNombre(nombre);
        amigosAtributos.setMensaje(ultimoMensaje);
        amigosAtributos.setHora(hora);
        amigosAtributos.setId(id);
        atributosList.add(amigosAtributos);
        adapter.notifyDataSetChanged();
        verificarSiTenemosAmigos();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void agregarAmigo(AmigosAtributos a){
        atributosList.add(0,a);
        adapter.notifyDataSetChanged();
        verificarSiTenemosAmigos();
    }

   /* public void SolicitudJSON(){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL_USUARIOS,null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String TodosLosDatos = datos.getString("datos");
                    JSONArray jsonArray = new JSONArray(TodosLosDatos);


                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject js = jsonArray.getJSONObject(i);
                        if(!js.getString("id").equals(ID)){
                            agregarAmigo(String.valueOf(R.drawable.ic_persona),js.getString("nombre"),"mensaje "+i,"00:00",js.getInt("id"));
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Ocurrio un error al descomponer el JSON",Toast.LENGTH_SHORT).show();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Ocurrio un error, por favor contactese con el administrador",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(solicitud);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ejecutarLLamada(AmigosAtributos a){
        agregarAmigo(a);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eliminarAmigo(EliminarFragmentAmigos a){
        for(int i=0;i<atributosList.size();i++){
            if(Objects.equals(atributosList.get(i).getId(), a.getId())){
                atributosList.remove(i);
                actualizarTarjetas();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void limpiarLista(LimpiarListaAmigos a){
        atributosList.clear();
        actualizarTarjetas();
    }

    public void eliminarAmigo(final String id){

        String usuarioEmisor = Preferences.obtenerPreferenceString(getContext(),Preferences.PREFERENCE_USUARIO_LOGIN);

        SolicitudesJson s = new SolicitudesJson() {
            @Override
            public void solicitudCompletada(JSONObject j) {
                try {
                    String respuesta = j.getString("respuesta");
                    if(respuesta.equals("200")){

                        bus.post(new EliminarAmigoFragmentUsuarios(id));
                        for(int i=0;i<atributosList.size();i++){
                            if(Objects.equals(atributosList.get(i).getId(), id)){
                                atributosList.remove(i);
                                actualizarTarjetas();
                            }
                        }
                    }else if(respuesta.equals("-1")){
                        //solicitud fallida
                        Toast.makeText(getContext(), "Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"Error al eliminar el usuario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void solicitudErronea() {
                Toast.makeText(getContext(), "Ocurrio un error al eliminar el usuario", Toast.LENGTH_SHORT).show();
            }
        };
        HashMap<String,String> hs = new HashMap<>();
        hs.put("emisor",usuarioEmisor);
        hs.put("receptor",id);
        s.solicitudJsonPOST(getContext(),SolicitudesJson.URL_ELIMINAR_USUARIO,hs);
    }
*/
   /* @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }*/

