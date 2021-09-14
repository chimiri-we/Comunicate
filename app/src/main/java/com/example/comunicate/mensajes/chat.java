package com.example.comunicate.mensajes;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.comunicate.R;
import com.example.comunicate.adapter.ChatAdapter;
import com.example.comunicate.provider.BaseDatos;
import com.example.comunicate.utils.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class chat extends AppCompatActivity  {
    JSONArray jsonArray;
    private static final String RECEPTOR = "receptor";
    private static final String EMISOR = "emisor";
    private static final String IDRECEPTOR = "id_receptor";
    private static final String IDEMISOR = "id_emisor";

    private static final String URL_MENSAJE = "https://servicioparanegocio.es/Trabajos/chat/chat.php";

    private static final String IP_MENSAJE = "https://servicioparanegocio.es/Trabajos/chat/Enviar_Mensajes.php";
    RequestQueue requestQueue;
  //  private ProgressDialog progreso;
    private BroadcastReceiver bR;

    private static final long TYPING_TIMER_LENGTH = 3000;
    private RecyclerView.LayoutManager mLayoutManager;
    List<MensajeDeTexto> listMensajes;
    List<MensajeDeTexto> nuevalistMensajes;
    ChatAdapter adapter;
    JsonObjectRequest jsonObjectRequest;
    public static final String MENSAJE = "mensaje";
    private String MENSAJE_ENVIAR = "";
    MensajeDeTexto msj;
    RecyclerView rv;
    ImageView btnEnviar;
    EditText edtMensaje;
    BaseDatos bdLocal;
    Usuario usuario;
    String nombreEmsor;
    String nombreReceptor;
    String id_receptor;
    String id_emisor;
    private LinearLayout llTyping;
    private TextView tvTyping, tvAlert;

    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 2000;
   // StringRequest stringRequest;
  //  JSONParser jsonParser = new JSONParser();
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_mensajes);

       initView();
   }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void initView() {
        nombreReceptor = getIntent().getStringExtra("nombre");
        id_receptor = getIntent().getStringExtra("id_receptor");
       // Toolbar toolbar = findViewById(R.id.toolbarChat);
      //  setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nombreReceptor);
       // if (getSupportActionBar() != null)
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_person_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listMensajes = new ArrayList<>();



        bdLocal = new BaseDatos(this);
        usuario = bdLocal.validarUsuario();
        nombreEmsor = usuario.getNombre();
        id_emisor = String.valueOf(usuario.getId());
        actualizarChat(id_receptor, id_emisor, nombreEmsor, nombreReceptor);

        btnEnviar = findViewById(R.id.bTenviarMensaje);
        edtMensaje = findViewById(R.id.eTEsribirMensaje);
        //rv = findViewById(R.id.rvMensajes);
        rv = (RecyclerView) findViewById(R.id.rvMensajes);
        rv.setLayoutManager(new LinearLayoutManager(this));

        setupChatMessages();
        setupSendButton();
        setupTextWatcher();


    }


    private void actualizarChat(String id_receptor, String id_emisor, String user, String nombreReceptor) {
        String receptor = nombreReceptor.toLowerCase().trim();
        String emisor = user.toLowerCase().trim();
        Toast.makeText(this,"receptor  es "+receptor, Toast.LENGTH_SHORT).show();

        HashMap<String,String> chat = new HashMap<>();
        chat.put(EMISOR, emisor);
        chat.put(RECEPTOR, receptor);

        jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL_MENSAJE, new JSONObject(chat), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                MensajeDeTexto msj =null;
                JSONArray json=response.optJSONArray("chat");

                try {

                    for (int i = 0; i< Objects.requireNonNull(json).length(); i++){
                        msj = new MensajeDeTexto();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);
                        msj.setEmisor(jsonObject.optString("emisor"));
                        msj.setId(jsonObject.optString("nombre"));
                        msj.setMensaje(jsonObject.optString("mensaje"));
                        msj.setHoraDelMensaje(jsonObject.optString("hora"));
                        msj.setTipoMensaje(jsonObject.optInt("tipo_mensaje"));

                        listMensajes.add(msj);
                        //    edtMensaje.setText("");

                    }
                    adapter = new ChatAdapter(listMensajes, chat.this);
                    mLayoutManager = new LinearLayoutManager(chat.this);

                    rv.setAdapter(adapter);
                    rv.setLayoutManager(mLayoutManager);
                    rv.scrollToPosition(adapter.getItemCount()-1);
                    //  Toast.makeText(chat.this, "respuesta"+response, Toast.LENGTH_LONG).show();
                    Log.i("respuesta", String.valueOf(response));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(chat.this, "Nose puede Registrar"+error.toString(), Toast.LENGTH_LONG).show();
                Log.i("ERROR", error.toString());

            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void setupTextWatcher() {
        edtMensaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!mTyping) {
                    mTyping = true;
                    // mPresenter.onTyping();
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setupChatMessages() {
        adapter = new ChatAdapter(listMensajes, chat.this);
        mLayoutManager = new LinearLayoutManager(chat.this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(mLayoutManager);

    }

    private void setupSendButton() {
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMensaje();
            }
        });
    }

    private void sendMensaje() {
        final String enviarM = edtMensaje.getText().toString().trim();
        int tipoMensaje = 1;
        Date fechaActual= Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        String formattedDate = df.format(fechaActual);
        String emisor = nombreEmsor.toLowerCase().trim();
        HashMap<String,String> params = new HashMap<>();
        params.put(IDEMISOR, id_emisor);
        params.put(IDRECEPTOR, id_receptor);
        params.put(EMISOR, emisor);
        params.put(RECEPTOR, nombreReceptor);
        params.put(MENSAJE, enviarM);
        MensajeDeTexto msj = new MensajeDeTexto(emisor, formattedDate, enviarM, tipoMensaje);
        CreateMensaje(msj);

        jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, IP_MENSAJE, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                edtMensaje.setText("");



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(chat.this, "Nose puede Registrar"+error.toString(), Toast.LENGTH_LONG).show();
                Log.i("ERROR", error.toString());

            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }



    @SuppressLint("NotifyDataSetChanged")
    private void CreateMensaje(MensajeDeTexto msj) {
        adapter.addNewMessage(msj);
        rv.scrollToPosition(adapter.getItemCount()-1);

    }
    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            //  mPresenter.onStopTyping();
        }
    };


    private void actualizar(String user, String nombreReceptor) {

        HashMap<String,String> chat = new HashMap<>();
        chat.put(EMISOR, user);
        chat.put(RECEPTOR, nombreReceptor);
        nuevalistMensajes = new ArrayList<>();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL_MENSAJE, new JSONObject(chat), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                MensajeDeTexto msj =null;
                JSONArray json=response.optJSONArray("chat");

                try {

                    for (int i = 0; i< Objects.requireNonNull(json).length(); i++){
                        msj = new MensajeDeTexto();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);
                        msj.setEmisor(jsonObject.optString("emisor"));
                        msj.setId(jsonObject.optString("nombre"));
                        msj.setMensaje(jsonObject.optString("mensaje"));
                        msj.setHoraDelMensaje(jsonObject.optString("hora"));
                        msj.setTipoMensaje(jsonObject.optInt("tipo_mensaje"));

                        nuevalistMensajes.add(msj);
                        //    edtMensaje.setText("");

                    }

                    adapter = new ChatAdapter(nuevalistMensajes, chat.this);
                    // rv.setAdapter(adapter);
                    //   rv.addNewMessage(nuevalistMensajes);
                    rv.scrollToPosition(adapter.getItemCount()-1);
                    //  Toast.makeText(chat.this, "respuesta"+response, Toast.LENGTH_LONG).show();
                    Log.i("respuesta", String.valueOf(response));
                    Log.i("peticion", String.valueOf(requestQueue));

                    onResume();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(chat.this, "Nose puede Registrar"+error.toString(), Toast.LENGTH_LONG).show();
                Log.i("ERROR", error.toString());

            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }


}
/*

    private void actualizarChat(String user, String nombreReceptor) {
//        Toast.makeText(this,"receptor  es "+receptor, Toast.LENGTH_SHORT).show();

        HashMap<String,String> chat = new HashMap<>();
        chat.put(EMISOR, user);
        chat.put(RECEPTOR, nombreReceptor);

        jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL_MENSAJE, new JSONObject(chat), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                MensajeDeTexto msj =null;
                JSONArray json=response.optJSONArray("chat");

                try {

                    for (int i = 0; i< Objects.requireNonNull(json).length(); i++){
                        msj = new MensajeDeTexto();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);
                        msj.setEmisor(jsonObject.optString("emisor"));
                        msj.setId(jsonObject.optString("nombre"));
                        msj.setMensaje(jsonObject.optString("mensaje"));
                        msj.setHoraDelMensaje(jsonObject.optString("hora"));
                        msj.setTipoMensaje(jsonObject.optInt("tipo_mensaje"));

                        listMensajes.add(msj);
                    //    edtMensaje.setText("");

                    }
                    adapter = new ChatAdapter(listMensajes, chat.this);
                    rv.setAdapter(adapter);
                  //  Toast.makeText(chat.this, "respuesta"+response, Toast.LENGTH_LONG).show();
                    Log.i("respuesta", String.valueOf(response));
                    Log.i("peticion", String.valueOf(requestQueue));

                    onResume();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(chat.this, "Nose puede Registrar"+error.toString(), Toast.LENGTH_LONG).show();
                Log.i("ERROR", error.toString());

            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void CreateMensaje(String enviarM, String s, int i) {


        String emisor = user.toLowerCase().trim();
        MensajeDeTexto msj = new MensajeDeTexto();
        msj.setId(emisor);
        msj.setMensaje(enviarM);
        msj.setTipoMensaje(i);
        msj.setHoraDelMensaje(s);
        listMensajes.add(msj);
     //   adapter.notifyDataSetChanged();
     //   setScrollbarChat();
    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                actualizar(user, nombreReceptor);

                Toast.makeText(chat.this, "This method is run every 10 seconds",
                        Toast.LENGTH_SHORT).show();
            }
        }, delay);
        super.onResume();
    }

    private void actualizar(String user, String nombreReceptor) {

        HashMap<String,String> chat = new HashMap<>();
        chat.put(EMISOR, user);
        chat.put(RECEPTOR, nombreReceptor);
        nuevalistMensajes = new ArrayList<>();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URL_MENSAJE, new JSONObject(chat), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                MensajeDeTexto msj =null;
                JSONArray json=response.optJSONArray("chat");

                try {

                    for (int i = 0; i< Objects.requireNonNull(json).length(); i++){
                        msj = new MensajeDeTexto();
                        JSONObject jsonObject=null;

                        jsonObject=json.getJSONObject(i);
                        msj.setEmisor(jsonObject.optString("emisor"));
                        msj.setId(jsonObject.optString("nombre"));
                        msj.setMensaje(jsonObject.optString("mensaje"));
                        msj.setHoraDelMensaje(jsonObject.optString("hora"));
                        msj.setTipoMensaje(jsonObject.optInt("tipo_mensaje"));

                        nuevalistMensajes.add(msj);
                        //    edtMensaje.setText("");

                    }

                    adapter = new ChatAdapter(nuevalistMensajes, chat.this);
                    rv.setAdapter(adapter);
                    //  Toast.makeText(chat.this, "respuesta"+response, Toast.LENGTH_LONG).show();
                    Log.i("respuesta", String.valueOf(response));
                    Log.i("peticion", String.valueOf(requestQueue));

                    onResume();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(chat.this, "Nose puede Registrar"+error.toString(), Toast.LENGTH_LONG).show();
                Log.i("ERROR", error.toString());

            }
        });


        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
    }



    private void setScrollbarChat() {
        rv.scrollToPosition(adapter.getItemCount()-1);
    }

    private void ActualizarDatos() {
        final String enviarM = edtMensaje.getText().toString().trim();
      //  progreso=new ProgressDialog(this);
       // progreso.setMessage("Cargando...");
      //  progreso.show();
        String receptor = nombreReceptor.toLowerCase().trim();
        String emisor = user.toLowerCase().trim();
        HashMap<String,String> params = new HashMap<>();
        params.put(EMISOR, emisor);
          params.put(RECEPTOR, receptor);
           params.put(MENSAJE, enviarM);

jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, IP_MENSAJE, new JSONObject(params), new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
           // progreso.hide();
MensajeDeTexto msj =null;
            JSONArray json=response.optJSONArray("chat");



            try {

                for (int i = 0; i<Objects.requireNonNull(json).length(); i++){
                    msj = new MensajeDeTexto();
                    JSONObject jsonObject=null;

                    jsonObject=json.getJSONObject(i);
                    msj.setEmisor(jsonObject.optString("emisor"));

                    msj.setId(jsonObject.optString("nombre"));
                    msj.setMensaje(jsonObject.optString("mensaje"));
                    msj.setHoraDelMensaje(jsonObject.optString("hora"));

                    msj.setTipoMensaje(jsonObject.optInt("tipo_mensaje"));

                    listMensajes.add(msj);
                   // edtMensaje.setText("");

                }
                adapter = new ChatAdapter(listMensajes, chat.this);
                rv.setAdapter(adapter);
                 //  Toast.makeText(chat.this, "respuesta"+response, Toast.LENGTH_LONG).show();
              //  Log.i("respuesta", String.valueOf(response));
              //  Log.i("peticion", String.valueOf(requestQueue));

            } catch (JSONException e) {
                e.printStackTrace();
            }
          }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progreso.hide();
                 Toast.makeText(chat.this, "Nose puede Registrar"+error.toString(), Toast.LENGTH_LONG).show();
                 Log.i("ERROR", error.toString());

            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

}
*/