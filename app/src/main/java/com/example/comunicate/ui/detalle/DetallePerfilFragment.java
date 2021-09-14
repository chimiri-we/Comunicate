package com.example.comunicate.ui.detalle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.comunicate.R;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.actividades.ActivityGallery;
import com.example.comunicate.utils.Usuario;
import com.example.comunicate.web.ConectActivity;
import com.example.comunicate.web.JSONParser;
import com.example.comunicate.web.VolleySingleton;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class DetallePerfilFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{

    String ID;
    RequestQueue requestQueue;
    private static final String URL = "https://servicioparanegocio.es/Trabajos/usuarios_lista.php";

    private static String ARG_PERFIL_ID = "id_receptor";
    private static final String ARG_PERFIL_NOMBRE = "nombre";
    private CollapsingToolbarLayout mCollapsingView;
    private ImageView mAvatar;
    private TextView mPhoneNumber;
    private TextView mSpecialty;
    private TextView mBio;
    private Button btn;
    private ProgressDialog pDialog;
    BaseDatos bdLocal;
    JsonObjectRequest objectRequest;
    String id;
    String URLIMAGEN;
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_MESSAGE = "message";


    public DetallePerfilFragment() {
        // Required empty public constructor
    }

    public static DetallePerfilFragment newInstance(String id_receptor) {
       DetallePerfilFragment fragment = new DetallePerfilFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PERFIL_ID, id_receptor);
        args.putString(ARG_PERFIL_NOMBRE, nombre);
        fragment.setArguments(args);
        return fragment;
*/
        Bundle extras = new Bundle();
        ARG_PERFIL_ID = extras.getString("id", id_receptor);


        fragment.setArguments(extras);
        return fragment;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getString(ARG_PERFIL_ID);

        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil_detalles, container, false);



        loadLawyer();

        mCollapsingView = (CollapsingToolbarLayout) requireActivity().findViewById(R.id.toolbar_layout);
        mAvatar = (ImageView) requireActivity().findViewById(R.id.iv_avatar);
        mPhoneNumber = root.findViewById(R.id.tv_phone_number);
        mSpecialty = root.findViewById(R.id.tv_specialty);
        mBio = (TextView) root.findViewById(R.id.tv_bio);
        btn = root.findViewById(R.id.btnGaleria);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ActivityGallery.class);
                intent.putExtra("id_receptor", id);
               // intent.putExtra("nombre", nombre);
                v.getContext().startActivity(intent);


            }
        });


        return root;
    }

    private void loadLawyer() {
       obtenerDatos();
    }

    private void obtenerDatos() {
        HashMap<String,String> chat = new HashMap<>();
        chat.put("id", id);

        objectRequest=new JsonObjectRequest(Request.Method.POST,URL,new JSONObject(chat), this, this);
        VolleySingleton.getInstance(getContext()).addToRequestQueue(objectRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_meGusta:
                showEditScreen();
                break;
            case R.id.action_delet:
              //  new DeleteLawyerTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void showLawyer(Usuario usuario) {
        mCollapsingView.setTitle(usuario.getNombre());
        Glide.with(this)
                .asBitmap()
                .load(usuario.getFotoPerfil())
                .error(R.drawable.ic_person_24)
                .centerCrop()
                .into(mAvatar);
        mPhoneNumber.setText(usuario.getTelefono());
        mSpecialty.setText(usuario.getApellidos());
        mBio.setText(usuario.getUrlImagen());
        URLIMAGEN = usuario.getUrlImagen();
    }

    private void showEditScreen() {
        Intent intent = new Intent(getActivity(), ConectActivity.class);
        //  intent.putExtra(ClienteActivity.EXTRA_LAWYER_ID, mLawyerId);
           startActivity(intent);
      //  Intent intent = new Intent(getActivity(), AgregarClienteActivity.class);
     //   intent.putExtra(ClienteActivity.EXTRA_LAWYER_ID, mLawyerId);
     //   startActivityForResult(intent, ListaClientesFragment.REQUEST_UPDATE_DELETE_LAWYER);
    }



    private void showDeleteError() {
        Toast.makeText(getActivity(),
                "Error al eliminar informacion", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(),"No se pudo Consultar "+error.toString(),Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
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



                mPhoneNumber.setText(usuario.getTelefono());
                if (usuario.getUrlImagen()!=null){
                    cargarImagen(usuario.getUrlImagen());
                }
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

    private void cargarImagen(String urlImagen) {
        String URL = "https://servicioparanegocio.es/Trabajos/"+urlImagen;
        URL=URL.replace(" ","%20");

        ImageRequest imgReq = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                mAvatar.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        VolleySingleton.getInstance(getContext()).addToRequestQueue(imgReq);
    }
}


