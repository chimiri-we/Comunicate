package com.example.comunicate.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comunicate.R;
import com.example.comunicate.adapter.BuscarAdapter;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.utils.Usuario;
import com.example.comunicate.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FragmentBuscar extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private static final String URL = "https://servicioparanegocio.es/Trabajos/usuarios_lista.php";
    ArrayList<String> arrayListString;
    ArrayList<Usuario> arrayListBuscarUsuario = new ArrayList<>();
    BaseDatos bdLocal;
    private static final String IDEMISOR = "id";
    private static final String EMISOR = "emisor";
    ListView rvBuscarUsuario;
    BuscarAdapter adapter;
    Usuario usuario;
    String ID, NOMBREEMISOR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_buscar);

       // arrayListBuscarUsuario = new ArrayList<>();
        bdLocal = new BaseDatos(this.getApplicationContext());
        usuario = bdLocal.validarUsuario();
        ID = String.valueOf(usuario.getId());
        NOMBREEMISOR = usuario.getNombre();
        EditText txtBuscar = findViewById(R.id.searchUsuarios);
        rvBuscarUsuario = findViewById(R.id.rvUsuarios);
        LinearLayout layout = findViewById(R.id.layoutVacio);
      //  LinearLayoutManager lm = new LinearLayoutManager(this);

     /*   if (arrayListBuscarUsuario.size() > 0){
            layout.setVisibility(View.GONE);
            rvBuscarUsuario.setVisibility(View.VISIBLE);
            adapter = new BuscarAdapter(FragmentBuscar.this, R.layout.list_item_cliente, arrayListBuscarUsuario);
           // rvBuscarUsuario.setLayoutManager(lm);
            rvBuscarUsuario.setAdapter(adapter);
        }else {
            layout.setVisibility(View.VISIBLE);
            rvBuscarUsuario.setVisibility(View.GONE);
        }*/
        txtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    private void buscarUsuario() {
        HashMap<String,String> chat = new HashMap<>();
        chat.put(IDEMISOR, ID);

        JsonObjectRequest nuevoRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(chat), this, this);
        VolleySingleton.getInstance(this).addToRequestQueue(nuevoRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se puede conectar "+error.toString(), Toast.LENGTH_LONG).show();
        System.out.println();

        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {

        Usuario usuario=null;
        JSONArray  json=response.optJSONArray("datos");
        // JSONObject jsonObject;
       // adapter = new BuscarAdapter();
        arrayListBuscarUsuario = new ArrayList<>();
        try {


            for (int i = 0; i< Objects.requireNonNull(json).length(); i++){
                usuario=new Usuario();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                usuario.setId(jsonObject.optInt("id"));
                usuario.setNombre(jsonObject.optString("nombre"));
                usuario.setApellidos(jsonObject.optString("apellido"));
                usuario.setTelefono(jsonObject.optString("telefono"));
                usuario.setUrlImagen(jsonObject.optString("imagen"));

                arrayListBuscarUsuario.add(usuario);


                Log.i("respuesta", String.valueOf(response));

            }
            //   adapter = new UsuariosAdapter(getContext(), nuevaListaCliente);
            //  recicler.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Verifica tu coneccion", Toast.LENGTH_LONG).show();
            Log.i("respuesta", String.valueOf(response));
        }
        adapter = new BuscarAdapter(FragmentBuscar.this, R.layout.list_item_cliente, arrayListBuscarUsuario);
        rvBuscarUsuario.setAdapter(adapter);
    }

    @Override
    protected void onResume() {

        buscarUsuario();
        super.onResume();
    }
}
