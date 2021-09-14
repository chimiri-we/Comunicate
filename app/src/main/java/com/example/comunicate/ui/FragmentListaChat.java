package com.example.comunicate.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comunicate.R;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.ui.gallery.UsuariosAdapter;
import com.example.comunicate.utils.Usuario;
import com.example.comunicate.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FragmentListaChat extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {



    RecyclerView recicler;
    private static final String IDEMISOR = "id";
    private static final String EMISOR = "emisor";
    private static final String URL = "https://servicioparanegocio.es/Trabajos/chat/lista_chats.php";

    ArrayList<Usuario> listaChat;
    UsuariosAdapter adapter;
    BaseDatos bdLocal;
    JsonObjectRequest jsonRequest;
    Usuario usuario;
    String ID, NOMBREEMISOR;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chats,container,false);



        listaChat = new ArrayList<>();
        bdLocal = new BaseDatos(requireContext().getApplicationContext());
        usuario = bdLocal.validarUsuario();
        ID = String.valueOf(usuario.getId());
        NOMBREEMISOR = usuario.getNombre();


        recicler = (RecyclerView) v.findViewById(R.id.RecyclerChats);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recicler.setLayoutManager(lm);
        recicler.setHasFixedSize(true);
        cargarListaUsuarios();

        //  new ListaUsuarios().execute();
       /* if (nuevaListaCliente.size() > 0) {

            recicler.setVisibility(View.VISIBLE);
             adapter = new UsuariosAdapter(getContext(), nuevaListaCliente);
             recicler.setAdapter(adapter);
        }else {
            recicler.setVisibility(View.GONE);
            Toast.makeText(getContext(),"no hay datos", Toast.LENGTH_SHORT).show();
            //  LayoutInflater inflater = LayoutInflater.from(getContext());


        }*/
        return v;
    }

    private void cargarListaUsuarios() {
        HashMap<String,String> chat = new HashMap<>();
        chat.put(EMISOR, NOMBREEMISOR);

        jsonRequest=new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(chat), this, this);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();

        Log.d("ERROR: ", error.toString());

    }

    @Override
    public void onResponse(JSONObject response) {
        Usuario usuario=null;
        JSONArray json=response.optJSONArray("chat");
        // JSONObject jsonObject;

        try {

            for (int i = 0; i< Objects.requireNonNull(json).length(); i++){
                usuario=new Usuario();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                usuario.setId(jsonObject.optInt("id"));
                usuario.setNombre(jsonObject.optString("nombre"));
               // usuario.setApellidos(jsonObject.optString("apellido"));
              //  usuario.setTelefono(jsonObject.optString("telefono"));
                usuario.setUrlImagen(jsonObject.optString("img"));

                listaChat.add(usuario);
                adapter = new UsuariosAdapter(getContext(), listaChat);
                recicler.setAdapter(adapter);

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
