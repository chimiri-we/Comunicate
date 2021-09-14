package com.example.comunicate.sync.login;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.comunicate.R;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.utils.Usuario;
import com.example.comunicate.web.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistroFragment extends Fragment {

    public static final String REGISTER_URL = "https://servicioparanegocio.es/Trabajos/Registro_usuario.php";

    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_CORREO = "correo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NUMEROTELEFONO = "telefono";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_USUARIO = "id_usuario";
    JSONParser jsonParser = new JSONParser();

    String ID, NOMBRE;
    private EditText edtNombreCliente, edtNumeroCliente, edtPasswordCliente, edtUsername;
    TextView sigVista;

    private ProgressDialog progressDialog;

    private static final String TABLE_USUARIO = "Usuario";


    BaseDatos bdLocal;
    Usuario usuarios;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_registro, container, false);
        edtNombreCliente = v.findViewById(R.id.edt_nombre_usuario);
        edtNumeroCliente = v.findViewById(R.id.edt_numero);
        edtPasswordCliente = v.findViewById(R.id.edt_password);
        edtUsername = v.findViewById(R.id.edt_username);
        sigVista = v.findViewById(R.id.siguiente);
       sigVista.setVisibility(View.GONE);

        Button btnRegistroCliente = v.findViewById(R.id.btn_registrarCliente);
     //   btnRegistroCliente.setOnClickListener(v1 ->  new CreateUser().execute());

        btnRegistroCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CreateUser().execute();
            }
        });

        return v;
    }

    class CreateUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Creando usuario...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            int id;

            final String nombre = edtNombreCliente.getText().toString().trim();
            final String password = edtPasswordCliente.getText().toString().trim();
            final String telefono = edtNumeroCliente.getText().toString().trim();
            final String username = edtUsername.getText().toString().trim();
            try {
                // Building Parameters
                List params = new ArrayList();
                params.add(new BasicNameValuePair(KEY_NOMBRE, nombre));
                params.add(new BasicNameValuePair(KEY_CORREO, username));
                params.add(new BasicNameValuePair(KEY_PASSWORD, password));
                params.add(new BasicNameValuePair(KEY_NUMEROTELEFONO, telefono));

                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registering attempt", json.toString());
               // Usuario user = new Usuario();
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    id = json.getInt(TAG_USUARIO);
                    ID = String.valueOf(id);
                  //  guardarUsuario(id);
                    ContentValues values = new ContentValues();
                    values.put("id", id);
                    values.put("nombre", nombre);
                    values.put("correo", username);
                    values.put("password", password);
                    values.put("telefono", telefono);


                    BaseDatos bdLocal = new BaseDatos(getContext().getApplicationContext());
                    SQLiteDatabase db = bdLocal.getReadableDatabase();
                    db.insert(TABLE_USUARIO, null, values);

                  //  Toast.makeText(getContext(), "id retornado "+TAG_USUARIO, Toast.LENGTH_SHORT).show();


                    Log.d("User Created!", json.toString());


                    return json.getString(TAG_USUARIO);

                }else{
                    Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            progressDialog.dismiss();
            if (file_url != null){
                //getActivity().startActivity(Intent.getIntent());
                Toast.makeText(getContext(), file_url, Toast.LENGTH_LONG).show();

                edtPasswordCliente.setText("");
                edtNumeroCliente.setText("");
                edtNombreCliente.setText("");
                edtUsername.setText("");
               // startActivity(new Intent(getActivity(), ContentRegistro.class));
                Intent i = new Intent(getContext(), ContentRegistro.class);
                i.putExtra("id", ID);
                startActivity(i);
            }
        }
    }



    private void guardarUsuario(int id) {
        progressDialog.dismiss();
        usuarios = new Usuario();
        usuarios.setNombre(edtNombreCliente.getText().toString().trim());

        NOMBRE = edtNombreCliente.getText().toString().trim();
        usuarios.setPassword(edtPasswordCliente.getText().toString().trim());
        usuarios.setTelefono(edtNumeroCliente.getText().toString().trim());

        usuarios.setId(id);

        ContentValues values = new ContentValues();
        values.put("id", usuarios.getId());
        values.put("nombre", usuarios.getNombre());
        values.put("telefono", usuarios.getTelefono());
        values.put("password", usuarios.getPassword());
        bdLocal = new BaseDatos(requireContext().getApplicationContext());
        SQLiteDatabase db = bdLocal.getReadableDatabase();

        db.insert(TABLE_USUARIO, null, values);



//       Intent i = new Intent(getContext(), MainActivity.class);
      // getActivity().star;

       // progressDialog.hide();
      /*  edtPasswordCliente.setText("");
        edtNumeroCliente.setText("");
        edtNombreCliente.setText("");
        edtUsername.setText("");*/

     //   Intent i = new Intent(getContext(), ContentRegistro.class);
       //  i.putExtra("id", ID);
       // startActivity(i);


    }
}


      /*  StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                response -> {
                    Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    edtPasswordCliente.setText("");
                    edtNumeroCliente.setText("");
                    edtNombreCliente.setText("");
                    edtEmailCliente.setText("");
                },
                error -> {
                    progressDialog.hide();
                    Toast.makeText(getContext(), "Nose puede Registrar"+error.toString(), Toast.LENGTH_LONG).show();
                    Log.i("ERROR", error.toString());
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(KEY_NOMBRE, nombre);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);
                params.put(KEY_NUMEROTELEFONO, telefono);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }*/

