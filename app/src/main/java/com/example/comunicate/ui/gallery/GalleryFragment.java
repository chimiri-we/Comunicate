package com.example.comunicate.ui.gallery;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comunicate.R;
import com.example.comunicate.adapter.ImagenesAdapter;
import com.example.comunicate.databinding.FragmentGalleryBinding;
import com.example.comunicate.utils.Imagenes;
import com.example.comunicate.web.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GalleryFragment extends Fragment  implements Response.Listener<JSONObject>, Response.ErrorListener {
    private GridView gridView;
    private static String ID;
    private static final String IDEMISOR = "id";
    private GalleryViewModel galleryViewModel;
    ImagenesAdapter adaptador;
    ArrayList<Imagenes> listaImagenes;
    private FragmentGalleryBinding binding;
    private static final String URL = "https://servicioparanegocio.es/Trabajos/usuarios_lista.php";

    public static Object instantiate(String id) {
        Bundle extras = new Bundle();
        ID = extras.getString(id);
        return extras;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        String id = getArguments().getString(ID);
        cargarImagenes(id);

        gridView = binding.grid;
        //adaptador = new ImagenesAdapter(getContext());
        //gridView.setAdapter(adaptador);
        //gridView.setOnItemClickListener(getContext());

         return root;
    }

    private void cargarImagenes(String id) {
        HashMap<String,String> chat = new HashMap<>();
        chat.put(IDEMISOR, id);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(chat), this, this);
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
        Imagenes imagenes =null;
        JSONArray json=response.optJSONArray("datos");
        // JSONObject jsonObject;

        try {

            for (int i = 0; i< Objects.requireNonNull(json).length(); i++){
              imagenes = new Imagenes();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);

                imagenes.setId_imagen(jsonObject.optInt("id_imagen"));
                imagenes.setNombre_imagen(jsonObject.optString("nombre_imagen"));
                imagenes.setUrlImagen(jsonObject.optString("imagen"));

               listaImagenes.add(imagenes);
                adaptador = new ImagenesAdapter(getContext(), R.layout.grid_item, listaImagenes);
                gridView.setAdapter(adaptador);

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}