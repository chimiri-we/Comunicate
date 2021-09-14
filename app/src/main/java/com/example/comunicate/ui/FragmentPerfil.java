package com.example.comunicate.ui;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.comunicate.MainActivity;
import com.example.comunicate.R;
import com.example.comunicate.actividades.ActivityDetalleImagen;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.utils.Usuario;
import com.example.comunicate.web.VolleySingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentPerfil extends Fragment {

    public static final String REGISTER_URL = "https://servicioparanegocio.es/Trabajos/Actualizar_Datos.php";
    public static final String REGISTER_URL_IMGEN = "https://servicioparanegocio.es/Trabajos/Imagen_Actualizar.php";

    public static final String KEY_NOMBRE = "nombre";
    public static final String KEY_APELLIDO = "apellidos";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CIUDAD = "ciudad";
    public static final String KEY_COLONIA = "colonia";
    public static final String KEY_CALLE = "calle";
    public static final String KEY_CORREO = "correo";
    public static final String KEY_ID = "id";
    public static final String KEY_IMAGEN = "imagen";
    public static final String KEY_NUMEROTELEFONO = "telefono";
    private static final String TAG_CODE = "code";
    private static final String CARPETA_PRINCIPAL = "fotosApp/";//directorio principal
    private static final String CARPETA_IMAGEN = "imagenes";//carpeta donde se guardan las fotos
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;//ruta carpeta de directorios
    private String path;//almacena la ruta de la imagen
    File fileImagen;
    Bitmap bitmap, img;
    ImageView imgFoto, imgEdit, imgEditdirec, imgEditContra;
    CircleImageView imgperfil;
    ProgressDialog progreso;
    private final int MIS_PERMISOS = 100;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    BaseDatos bdLocal;

    JsonObjectRequest jsonObjectRequest;

    TextView txtnombre, correo, tvusuario, direccion,  telefono, idUser, nombreURL;
    StringRequest stringRequest;
    Usuario usuarios;
    String calle;
    String ciudad;
    String colonia;
    TextInputLayout textInputLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmento_perfil, container, false);

        txtnombre = v.findViewById(R.id.texto_nombre);
        nombreURL = v.findViewById(R.id.tv_nombre_user);
        idUser = v.findViewById(R.id.texto_foto_perfil);
        correo = v.findViewById(R.id.texto_email);
        tvusuario = v.findViewById(R.id.texto_usuario);
        direccion = v.findViewById(R.id.texto_direccion_usuario);
        telefono = v.findViewById(R.id.texto_telefono);
        imgFoto = v.findViewById(R.id.ic_cambiar_imagen);
        imgperfil = v.findViewById(R.id.img_perfil);
        imgEdit = v.findViewById(R.id.icono_edit_datos);
        imgEditContra = v.findViewById(R.id.icono_indicador_derecho);
        imgEditdirec = v.findViewById(R.id.icono_editar_derecho);
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA, WRITE_EXTERNAL_STORAGE}, 0);
        }
        usuarios = new Usuario();
        verdatosUsuario();

        if (usuarios != null) {
            idUser.setText(String.valueOf(usuarios.getId()));
            txtnombre.setText(usuarios.getNombre());
            tvusuario.setText(usuarios.getApellidos());
            correo.setText(usuarios.getCorreo());
            calle = usuarios.getCalle();
            colonia = usuarios.getColonia();
            ciudad = usuarios.getCiudad();
          //  nombreURL.setText(usuarios.getFotoPerfil());
            direccion.setText("CAlle; "+calle+", Colonia; "+colonia+", Ciudad; "+ciudad);

            telefono.setText(usuarios.getTelefono());

            //  imgperfil.setImageURI(Uri.parse(usuarios.getUrlImagen()));
            //   imgperfil.setImageBitmap(usuarios.getUrlImagen());
//            String urlImagen = usuarios.getUrlImagen().toLowerCase();

            Glide.with(requireContext())
                    .asBitmap()
                    .load(usuarios.getFotoPerfil())
                    .error(R.drawable.ic_person_24)
                    .centerCrop()
                    .into(imgperfil);
        }else{
            editarDatosUsuario(usuarios);
        }

        imgFoto.setOnClickListener(v14 -> tomarFoto());

        imgEdit.setOnClickListener(v1 -> editarDatosUsuario(usuarios));
        imgEditdirec.setOnClickListener(v12 -> editarDireccion(usuarios));

        imgEditContra.setOnClickListener(v13 -> editarContrasena(usuarios));

        imgperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String urlImagen = usuarios.getFotoPerfil();
                Bitmap imagen = usuarios.getImagen();
                Toast.makeText(v.getContext(), "el id es que estoy pasando es "+imagen, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), ActivityDetalleImagen.class);
                i.putExtra("imagen", imagen);
                i.putExtra("urlImagen", urlImagen);
                //    Intent i = new Intent(ActivityUsuarios.this, MainActivity.class);
                Toast.makeText(v.getContext(), "la URL es que estoy pasando es "+urlImagen, Toast.LENGTH_SHORT).show();

                startActivity(i);
            }
        });

        return v;
    }



    private void editarContrasena(Usuario usuarios) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View viewCon = inflater.inflate(R.layout.dialog_contrasena, null, false);
        TextInputEditText edtContra = viewCon.findViewById(R.id.edt_contras);
        TextInputLayout layout = viewCon.findViewById(R.id.label_password);
        TextInputEditText edtConfirCon = viewCon.findViewById(R.id.edt_confirmar);
        if (usuarios != null){
            edtContra.setText(usuarios.getPassword());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(viewCon);
        builder.create();

        builder.setPositiveButton("GUARDAR DATOS", (dialog, which) -> {

            final String contra = Objects.requireNonNull(edtContra.getText()).toString();
            final String confirmar = edtContra.getText().toString();
            if (TextUtils.isEmpty(confirmar)) {
                Toast.makeText(getContext(), "Algo salió mal. Verifique sus valores de entrada", Toast.LENGTH_LONG).show();
            } else {
                bdLocal = new BaseDatos(requireContext().getApplicationContext());
                assert usuarios != null;
                bdLocal.actualizarContrasena(new
                        Usuario(usuarios.getId(), contra));

            }
        });
        builder.setNegativeButton("CANCELAR", (dialog, which) -> Toast.makeText(getContext(), "Tarea Cancelada",Toast.LENGTH_LONG).show());

        builder.show();
    }

    private void editarDireccion(Usuario usuarios) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TextInputLayout inputLayout;
        View viewDir = inflater.inflate(R.layout.dialog_direccion, null, false);
        TextInputEditText edtDireccionColonia = viewDir.findViewById(R.id.edt_direcci_colonia);
        TextInputEditText edtDireccionCiudad = viewDir.findViewById(R.id.edt_direcci_ciudad);
        TextInputEditText edtDireccionCalle = viewDir.findViewById(R.id.edt_direcci_calle);

        if (usuarios != null){
            edtDireccionCiudad.setText(usuarios.getCiudad());
            edtDireccionCalle.setText(usuarios.getCalle());
            edtDireccionColonia.setText(usuarios.getColonia());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(viewDir);
        builder.create();

        builder.setPositiveButton("GUARDAR DATOS", (dialog, which) -> {
            // actualizarDatos();
            String newciudad = Objects.requireNonNull(edtDireccionCiudad.getText()).toString().trim();
            String newcalle = Objects.requireNonNull(edtDireccionCalle.getText()).toString().trim();
            String newcolonia = Objects.requireNonNull(edtDireccionColonia.getText()).toString().trim();

            if (TextUtils.isEmpty(newciudad)) {
                Toast.makeText(getContext(), "Algo salió mal. Verifique sus valores de entrada", Toast.LENGTH_LONG).show();
            } else {
                bdLocal = new BaseDatos(requireContext().getApplicationContext());
                assert usuarios != null;
                bdLocal.actualizarDireccion(new
                        Usuario(usuarios.getId(), newciudad, newcalle, newcolonia));


            }
        });
        builder.setNegativeButton("CANCELAR", (dialog, which) -> Toast.makeText(getContext(), "Tarea Cancelada",Toast.LENGTH_LONG).show());

        builder.show();
    }

    private void editarDatosUsuario(Usuario usuarios) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_registro_perfil, null, false);
        TextInputLayout inputLayout;
        TextView edtNombreCliente = view.findViewById(R.id.edt_nombre_usuario);
        TextInputEditText edtEmailCliente = view.findViewById(R.id.edt_correo);
        TextInputEditText edtApellidos = view.findViewById(R.id.edt_username);
        TextInputEditText edtTelefono = view.findViewById(R.id.edt_telefono_user);
        if (usuarios != null){
            edtEmailCliente.setText(usuarios.getCorreo());
            edtNombreCliente.setText(usuarios.getNombre());
            edtApellidos.setText(usuarios.getApellidos());
            edtTelefono.setText(usuarios.getTelefono());

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        builder.create();

        builder.setPositiveButton("GUARDAR DATOS", (dialog, which) -> {

            final String nombre = Objects.requireNonNull(edtNombreCliente.getText()).toString();
            final String telefono = Objects.requireNonNull(edtTelefono.getText()).toString();
            final String correo = Objects.requireNonNull(edtEmailCliente.getText()).toString();
            final String apellido = Objects.requireNonNull(edtApellidos.getText()).toString();
            if (TextUtils.isEmpty(nombre)) {
                Toast.makeText(getContext(), "Algo salió mal. Verifique sus valores de entrada", Toast.LENGTH_LONG).show();
            } else {
                //    Toast.makeText(getContext(), "Daatos guardados", Toast.LENGTH_LONG).show();
                bdLocal = new BaseDatos(requireContext().getApplicationContext());
                assert usuarios != null;
                bdLocal.actualizarUsuario(new
                        Usuario(usuarios.getId(), nombre, telefono, correo, apellido));
                String user =String.valueOf(usuarios.getId());
                actualizarDatos(user, nombre, telefono, correo, apellido);
            }

        });
        builder.setNegativeButton("CANCELAR", (dialog, which) -> Toast.makeText(getContext(), "Tarea Cancelada",Toast.LENGTH_LONG).show());
        builder.show();
    }


    @SuppressLint("Recycle")
    private void verdatosUsuario() {
        bdLocal = new BaseDatos(getContext());
        SQLiteDatabase db = bdLocal.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("select * from Usuario", null);
        if (cursor.moveToFirst()) {

            usuarios.setId(cursor.getInt(0));
            //usuarios.setId_R(cursor.getInt(1));
            usuarios.setNombre(cursor.getString(1));
            usuarios.setApellidos(cursor.getString(2));
            usuarios.setFotoPerfil(cursor.getString(3));
            usuarios.setCorreo(cursor.getString(4));
            usuarios.setPassword(cursor.getString(5));
            usuarios.setTelefono(cursor.getString(6));
            usuarios.setCiudad(cursor.getString(9));
            usuarios.setColonia(cursor.getString(10));
            usuarios.setCalle(cursor.getString(11));
        }
    }
    private void actualizarDatos(String user, String nombre, String telefono, String correo, String apellido) {
        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();
        Map<String,String> params = new HashMap<>();
        params.put(KEY_ID, user);
        params.put(KEY_NOMBRE, nombre);
        params.put(KEY_CORREO, correo);
        params.put(KEY_APELLIDO, apellido);
        params.put(KEY_NUMEROTELEFONO, telefono);

        JsonObjectRequest jsonO=new JsonObjectRequest(Request.Method.POST, REGISTER_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    progreso.hide();
                    Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progreso.hide();
                    Toast.makeText(getContext(), "Nose puede Registrar"+error.toString(), Toast.LENGTH_LONG).show();
                    Log.i("ERROR", error.toString());

                }
            });

        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonO);
        onStart();
 }
    private String convertirImgString(Bitmap bitmap) {
        ByteArrayOutputStream array=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }


    public void tomarFoto() {
        mostrarDialogOpciones();
    }

    private void mostrarDialogOpciones() {
        solicitaPermisosVersionesSuperiores();

        final CharSequence[] opciones={"Tomar Foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder=new AlertDialog.Builder(requireContext());
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, (dialogInterface, i) -> {
            if (opciones[i].equals("Tomar Foto")){
                abriCamara();
            }else{
                if (opciones[i].equals("Elegir de Galeria")){
                    @SuppressLint("IntentReset") Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(Intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);
                }else{
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void abriCamara() {
        File miFile=new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada=miFile.exists();

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }

        if(isCreada==true){
            Long consecutivo= System.currentTimeMillis()/1000;
            String nombre=consecutivo.toString()+".jpg";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    +File.separator+nombre;//indicamos la ruta de almacenamiento

            fileImagen=new File(path);

            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));

            ////
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
            {
                String authorities=getContext().getPackageName()+".provider";
                Uri imageUri= FileProvider.getUriForFile(getContext(),authorities,fileImagen);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }else
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagen));
            }
            startActivityForResult(intent,COD_FOTO);

            ////

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath=data.getData();
             //   imgperfil.setImageURI(miPath);
                BaseDatos bdLocal = new BaseDatos(requireContext().getApplicationContext());
                String id = idUser.getText().toString().trim();
                String url = String.valueOf(miPath);
                bdLocal.guardarImagen(url, id);


                try {
                    bitmap=MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(),miPath);
                    imgperfil.setImageBitmap(bitmap);
                    bdLocal = new BaseDatos(requireContext().getApplicationContext());
                    bdLocal.obtenerRutaImagen(miPath, id);
                    enviarimagen();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("Path",""+path);
                                imgperfil.setImageURI(uri);
                            }
                        });

                bitmap= BitmapFactory.decodeFile(path);
                imgperfil.setImageBitmap(bitmap);
                bdLocal = new BaseDatos(requireContext().getApplicationContext());
                id = idUser.getText().toString().trim();
                bdLocal.guardarImagen(path, id);

                break;
        }
        bitmap=redimensionarImagen(bitmap,600,800);
    }

    private void enviarimagen() {
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Subiendo...","Espere por favor...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL_IMGEN,
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
                String id = idUser.getText().toString().trim();
                String nombre = txtnombre.getText().toString().trim();

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

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {

        int ancho=100;
        int alto=100;

        if(ancho>anchoNuevo || alto>altoNuevo){
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto= altoNuevo/alto;

            Matrix matrix=new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);

            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);

        }else{
            return bitmap;
        }


    }


    //permisos
    ////////////////

    private boolean solicitaPermisosVersionesSuperiores() {
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){//validamos si estamos en android menor a 6 para no buscar los permisos
            return true;
        }

        //validamos si los permisos ya fueron aceptados
        if((getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&getContext().checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED){
            return true;
        }


        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)||(shouldShowRequestPermissionRationale(CAMERA)))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MIS_PERMISOS);
        }

        return false;//implementamos el que procesa el evento dependiendo de lo que se defina aqui
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==MIS_PERMISOS){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){//el dos representa los 2 permisos
                Toast.makeText(getContext(),"Permisos aceptados",Toast.LENGTH_SHORT);
             //   btnFoto.setEnabled(true);
            }
        }else{
            solicitarPermisosManual();
        }
    }


    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }


    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }
    //permisos
    ////////////////




}

   /* private void solicitarPermisosManual() {
        final CharSequence[] opciones={"si","no"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(getContext());//estamos en fragment
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("si")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }


    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }
}
*/



