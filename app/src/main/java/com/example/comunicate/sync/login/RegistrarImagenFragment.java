package com.example.comunicate.sync.login;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.comunicate.MainActivity;
import com.example.comunicate.R;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.utils.Usuario;
import com.example.comunicate.web.VolleySingleton;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrarImagenFragment extends Fragment {
    private static final String TABLE_USUARIO = "Usuario";
    private static final String ID_USUARIO = "id";
    Location location;
    double describeContents;
    List<Address> addresses;
    Geocoder geocoder;
    CircleImageView imagenPerfil;
    TextView ponerNombre, ponerId;
    Button btnSubir;
    ImageButton tomarfoto;
    TextView txtDireccion;
    private Bitmap bitmap;
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_CODE = "code";

    BaseDatos bdLocal;
    String id;
    String nombre;
    private final int PICK_IMAGE_REQUEST = 1;

    private final String UPLOAD_URL ="https://servicioparanegocio.es/Trabajos/registro_imagenPerfil.php";

    private final String KEY_IMAGEN = "Imagen_perfil";
    private final String KEY_ID = "id";
    private final String KEY_NOMBRE = "nombre";
    Usuario usuario;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_image_upload, container, false);
      bdLocal = new BaseDatos(getContext());
      usuario = new Usuario();
      usuario = bdLocal.verdatosUsuario();

      if (usuario != null) {
          id = String.valueOf(usuario.getId());
          nombre = usuario.getNombre();

          obtenerDireccion();
      }
           // id = String.valueOf(usuario.getId());
        //    nombre = usuario.getNombre();

        txtDireccion =v.findViewById(R.id.txtDireccion);
            ponerNombre = v.findViewById(R.id.nombreImagen);
            ponerId = v.findViewById(R.id.nombreid);

            ponerId.setText(id);
           ponerNombre.setText(nombre);



            imagenPerfil = v.findViewById(R.id.imagen_perfil);
            btnSubir = v.findViewById(R.id.button1);
            tomarfoto = v.findViewById(R.id.camara);
          //  txtSig = v.findViewById(R.id.sigReg);
            btnSubir.setVisibility(View.GONE);

        tomarfoto.setOnClickListener(v1 -> cambiarImagen());

btnSubir.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        uploadImage();
    }
});
        return v;
    }



    private void cambiarImagen() {
     /*   Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
   */
        @SuppressLint("IntentReset") Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent,"Seleccione"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                Toast.makeText(getContext(),"La ruta es "+filePath,Toast.LENGTH_SHORT).show();
                String url = String.valueOf(filePath);
                String ID = ponerId.getText().toString().trim();
                guardarImagen(filePath, ID);
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                imagenPerfil.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarImagen(Uri filePath, String id) {

        String url = String.valueOf(filePath);
        ContentValues values = new ContentValues();
        values.put("uri_imagen", url);

        BaseDatos bdLocal = new BaseDatos(getContext().getApplicationContext());
        SQLiteDatabase db = bdLocal.getReadableDatabase();
        db.update(TABLE_USUARIO, values, ID_USUARIO+ " = ?", new String[]{id});
        btnSubir.setVisibility(View.VISIBLE);


    }

    private void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Subiendo...","Espere por favor...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                     //   guardarImagen(url, ID);
                        //Mostrando el mensaje de la respuesta
                        Log.i("response", s);
                        Toast.makeText(getContext(), "Haz actualizado tu foto "+s.trim(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen=convertirImgString(bitmap);
               // String imagen = getStringImagen(bitmap);

                //Obtener el nombre de la imagen
                String id = ponerNombre.getText().toString().trim();
                String nombre = ponerId.getText().toString().trim();

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();

                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);
               params.put(KEY_ID, id);

                //Parámetros de retorno
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
        //Creación de una cola de solicitudes
        //  RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Agregar solicitud a la cola
        //    requestQueue.add(stringRequest);

    }

    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }

    private String getStringImagen(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void obtenerDireccion() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        geocoder = new Geocoder(getContext());
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                        Address address = addresses.get(0);
                        txtDireccion.setText("" + address.getAddressLine(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //not granted
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
            Address address = addresses.get(0);
            txtDireccion.setText("" + address.getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}