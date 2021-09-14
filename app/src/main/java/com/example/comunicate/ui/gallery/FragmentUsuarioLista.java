package com.example.comunicate.ui.gallery;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comunicate.R;
import com.example.comunicate.provider.BaseDatos;
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


public class FragmentUsuarioLista extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {


    private RecyclerView recicler;
    private List<Usuario> atributosList;
    private static final String IDEMISOR = "id";
    private LinearLayout layoutVacio;

    UsuariosAdapter adapter;
    JSONParser jsonParser = new JSONParser();

    private static final String TAG_MESSAGE = "message";

    String ID;

    private static final String URL = "https://servicioparanegocio.es/Trabajos/usuarios_lista.php";

    ArrayList<String> listaCliente;
    ArrayList<Usuario> nuevaListaCliente;
   // BaseDatosApp bdLocal;
    ListView listViewOriginal;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    ProgressDialog progress;
    //private FragmentHomeBinding binding;
   // UsuariosAdapter adapter;

    ImageView btn;
    BaseDatos bdLocal;
    Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_amigos,container,false);



        nuevaListaCliente = new ArrayList<>();
        bdLocal = new BaseDatos(getContext().getApplicationContext());
        usuario = bdLocal.validarUsuario();
        ID = String.valueOf(usuario.getId());

        recicler = (RecyclerView) v.findViewById(R.id.amigosRecyclerView);
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
        chat.put(IDEMISOR, ID);

        jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(chat), this, this);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
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
        JSONArray  json=response.optJSONArray("datos");
       // JSONObject jsonObject;

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

                nuevaListaCliente.add(usuario);
                adapter = new UsuariosAdapter(getContext(), nuevaListaCliente);
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

/*

        class ListaUsuarios extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... args) {
            //  int success;
            //  String correo = usernameEditText.getText().toString();
            //   String password = passwordEditText.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                //   params.add(new BasicNameValuePair("correo", correo));
                //     params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                Usuario usuario;
                JSONArray array = json.optJSONArray("datos");
                JSONObject jsonObject;
                // success = json.getInt(TAG_SUCCESS);
                if (array != null) {

                    Log.d("Login Successful!", json.toString());
                    // save user data



                        for (int i = 0; i< Objects.requireNonNull(array).length(); i++){
                            usuario=new Usuario();
                            jsonObject=new JSONObject();
                            jsonObject=array.getJSONObject(i);

                            usuario.setId(jsonObject.optInt("id"));
                            usuario.setNombre(jsonObject.optString("nombre"));
                            usuario.setApellidos(jsonObject.optString("apellido"));
                            usuario.setTelefono(jsonObject.optString("telefono"));

                            atributosList.add(usuario);
                           // adapter = new UsuariosAdapter(getContext(), atributosList);
                            //recicler.setAdapter(adapter);
                            //  ID_PRODUCTO = String.valueOf(usuario.getId_producto());
                        }

                       adapter = new UsuariosAdapter(getContext(), atributosList);
                          recicler.setAdapter(adapter);



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
}*/
