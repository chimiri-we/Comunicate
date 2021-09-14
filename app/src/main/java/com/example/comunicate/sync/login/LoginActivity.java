package com.example.comunicate.sync.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.comunicate.R;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.utils.Usuario;
import com.example.comunicate.web.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Fragment {


    ProgressDialog progreso;
    private EditText user, pass;
    private Button mSubmit, mRegister;

    private ProgressDialog pDialog;
    JsonObjectRequest jsonObjectRequest;
    // Clase JSONParser
    JSONParser jsonParser = new JSONParser();
    private static final String TABLE_USUARIO = "Usuario";


 public static final String URL = "https://servicioparanegocio.es/Trabajos/Login.php";
//    private LoginViewModel loginViewModel;
    private EditText usernameEditText, passwordEditText;
    public static final String Password = "password";
    public static final String CORREO = "correo";

    private static final String TAG_SUCCESS = "success";

    // La respuesta del JSON es
    private static final String DATOS = "datos";
    private static final String TAG_MESSAGE = "message";
    String usuario;
    String nombre;
    Bitmap img;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);


        // setup input fields
        usernameEditText = v.findViewById(R.id.correo);
        passwordEditText = v.findViewById(R.id.password);
        usuario = usernameEditText.getText().toString();
        final Button mSubmit = v.findViewById(R.id.login);
        final ProgressBar loadingProgressBar = v.findViewById(R.id.loading);


        // setup buttons


        // register listeners
       mSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               new AttemptLogin().execute();
           }
       });

        return v;
    }



    @SuppressLint("StaticFieldLeak")
    class AttemptLogin extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;
            String correo = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair("correo", correo));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(URL, "POST",
                        params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                Usuario user = new Usuario();
                JSONArray array = json.optJSONArray("datos");
                JSONObject jsonObject = new JSONObject();
               // success = json.getInt(TAG_SUCCESS);
                if (array != null) {

                    Log.d("Login Successful!", json.toString());
                    // save user data

                    try {
                        jsonObject=array.getJSONObject(0);
                        user.setId(jsonObject.optInt("id"));
                        user.setNombre(jsonObject.optString("nombre"));
                        user.setApellidos(jsonObject.optString("apellido"));

                        user.setUrlImagen(jsonObject.optString("imagen"));
                        user.setCorreo(jsonObject.optString("correo"));
                        user.setPassword(jsonObject.optString("password"));
                        user.setTelefono(jsonObject.optString("telefono"));

                        user.setGenero(jsonObject.optString("genero"));
                        user.setFechaNacimiento(jsonObject.optString("fecha_nacimiento"));
                        user.setCiudad(jsonObject.optString("ciudad_estado"));
                        user.setColonia(jsonObject.optString("Colonia"));
                        user.setCalle(jsonObject.optString("Calle"));

                        String ID = String.valueOf(user.getId());
                    //    img = user.getImagen();
                        ContentValues values = new ContentValues();
                        values.put("id", user.getId());
                        values.put("nombre", user.getNombre());
                        values.put("apellido", user.getApellidos());
                        values.put("uri_imagen", user.getUrlImagen());
                        values.put("correo", user.getCorreo());
                        values.put("password", user.getPassword());
                        values.put("telefono", user.getTelefono());
                        values.put("genero", user.getGenero());
                        values.put("fechadeNacimiento", user.getFechaNacimiento());
                        values.put("ciudad_estado", user.getCiudad());
                        values.put("colonia", user.getColonia());
                        values.put("calle", user.getCalle());

                        BaseDatos bdLocal = new BaseDatos(getContext().getApplicationContext());
                        SQLiteDatabase db = bdLocal.getReadableDatabase();
                        db.insert(TABLE_USUARIO, null, values);
                      /*  Intent i = new Intent(getContext(), MainActivity.class);
                        i.putExtra("id", ID);
                        startActivity(i);*/
                        Intent i = new Intent(getContext(), ContentRegistro.class);
                        i.putExtra("id", ID);
                        startActivity(i);



                    }catch (JSONException e){
                        e.printStackTrace();
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
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(getContext(), file_url, Toast.LENGTH_LONG).show();


            }
        }
    }


/*
    private void obtenerDatosUsuario() {

        String url="https://servicioparanegocio.es/ventasApp/consultas/ConsultarUsuarioNombre.php?usuario="+usuario;


        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);

    }
    @Override
    public void onErrorResponse(VolleyError error) {

        Toast.makeText(getContext(),"No se pudo Consultar "+error.toString(),Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {


        //    Toast.makeText(getContext(),"Mensaje: "+response,Toast.LENGTH_SHORT).show();

        Usuarios user = new Usuarios();
        JSONArray json=response.optJSONArray("usuario");

        JSONObject jsonObject=null;

        try {
            jsonObject=json.getJSONObject(0);
                user.setId_usuario(jsonObject.optInt("id_producto"));
                user.setNombre(jsonObject.optString("Nombre"));
                user.setTelefono(jsonObject.optString("Telefono"));
                user.setUser(jsonObject.optString("Usuario"));
                user.setCorreo(jsonObject.optString("Correo"));
                user.setColonia(jsonObject.optString("Colonia"));
                user.setCalle(jsonObject.optString("Calle"));
                user.setDireccion(jsonObject.optString("Direccion_ciudad"));
                user.setPassword(jsonObject.optString("Password"));
                user.setFoto(jsonObject.optString("Imagen_perfil"));


                ContentValues values = new ContentValues();
                values.put("id_usuario", user.getId_usuario());
                values.put("nombre", user.getNombre());
                values.put("telefono", user.getTelefono());
                values.put("direccion", user.getDireccion());
                values.put("user", user.getUser());
                values.put("password", user.getPassword());
                values.put("uri_imagen", user.getUrlImagen());
                values.put("correo", user.getCorreo());
                values.put("calle", user.getCorreo());
                values.put("direccion", user.getCorreo());

                BaseDatos bdLocal = new BaseDatos(requireContext().getApplicationContext());
                SQLiteDatabase db = bdLocal.getReadableDatabase();
                db.insert(TABLE_USUARIO, null, values);



            Intent nuevaIntent = new Intent(getContext(), FullscreenActivity.class);
            nuevaIntent.putExtra("image", img);
            nuevaIntent.putExtra("nombre", nombre);
            startActivity(nuevaIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
*/
    }



/*

        loginViewModel.getLoginResult().observe((LifecycleOwner) getContext(), new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                ;

                //Complete and destroy login activity once successful
             //   finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v12 -> {
            //loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            Intent intent = new Intent(getActivity(), ActividadPrincipal.class);
            startActivity(intent);
        });



       loginButton.setOnClickListener(v1 -> loginCliente());
        return v;
    }
    private void loginCliente() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando..");
        progressDialog.show();
      //  Intent intent = new Intent(getContext(), ActividadPrincipal.class);
       // startActivity(intent);

        final String password = passwordEditText.getText().toString().trim();
        final String username =  usernameEditText.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {


        }, error -> Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show()){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("email", usernameEditText.getText().toString().trim());
                params.put("password", passwordEditText.getText().toString().trim());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = "Bienvenido"+ model.getDisplayName();

        Toast.makeText(getContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }
*/