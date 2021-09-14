package com.example.comunicate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.comunicate.R;
import com.example.comunicate.mensajes.chat;
import com.example.comunicate.ui.FragmentAmigos;
import com.example.comunicate.utils.SolicitudAmistad;

import java.util.List;

//import com.squareup.picasso.Picasso;


/**
 * Created by user on 8/05/2017.
 */

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.HolderAmigos> {

    private List<SolicitudAmistad> atributosList;
    private Context context;
    private FragmentAmigos f;
    RequestQueue requestQueue;
  //  Picasso picaso;
  String id;

    public AmigosAdapter(List<SolicitudAmistad> atributosList, Context context){
        this.atributosList = atributosList;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
       // this.f=f;
    }

    @Override
    public HolderAmigos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos,parent,false);
        return new AmigosAdapter.HolderAmigos(v);
    }

    @Override
    public void onBindViewHolder(HolderAmigos holder, @SuppressLint("RecyclerView") final int position) {
 //       Glide.get().load(atributosList.get(position).getFotoPerfil()).error(R.drawable.ic_edit).into(holder.imageView);
       SolicitudAmistad sl = (SolicitudAmistad) atributosList.get(position);
        String nombre = atributosList.get(position).getNombre();

        int tipo = 1;
        int estado = 1;
       if (atributosList.get(position).getTipo_solicitud() != tipo){
           holder.nombre.setText(sl.getNombre());
           holder.hora.setText(sl.getFecha_solicitud());
           if (atributosList.get(position).getEstado_solicitud() != estado){
               holder.mensaje.setText("Tienes una solicitud de "+nombre+" por confirmar");
           }else{
               holder.mensaje.setVisibility(View.GONE);
           }
           if (atributosList.get(position).getUrl_foto()!=null){
               verfoto(atributosList.get(position).getUrl_foto(), holder);
           }

       }else {
           holder.cardView.setVisibility(View.GONE);
       }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, chat.class);
                i.putExtra("nombre", nombre);
                i.putExtra("id_receptor",atributosList.get(position).getId_amigo());
                context.startActivity(i);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).
                        setMessage("¿Estas seguro que quieres eliminar a este amigo?").
                        setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //f.eliminarAmigo(atributosList.get(position).getId());
                                Toast.makeText(context, "Se elimino el amigo correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(context, "Cancelando solicitud de eliminacion", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                return true;
            }
        });



    }

    private void verfoto(String url_foto, HolderAmigos holder) {
        String URL = "https://servicioparanegocio.es/Trabajos/"+url_foto;
        URL=URL.replace(" ","%20");

        ImageRequest imgReq = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                holder.imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(imgReq);

    }



    @Override
    public int getItemCount() {
        return atributosList.size();
    }

        private void cargarImagen(String urlImagen, AmigosAdapter.HolderAmigos holder) {



            String URL = "https://servicioparanegocio.es/Trabajos/"+urlImagen;
            URL=URL.replace(" ","%20");

            ImageRequest imgReq = new ImageRequest(URL, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {

                    holder.imageView.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            requestQueue.add(imgReq);
        }

    static class HolderAmigos extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView nombre;
        TextView mensaje;
        TextView hora;

        public HolderAmigos(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewAmigos);
            imageView = (ImageView) itemView.findViewById(R.id.fotoDePerfilAmigos);
            nombre = (TextView) itemView.findViewById(R.id.nombreUsuarioAmigo);
            mensaje = (TextView) itemView.findViewById(R.id.mensajeAmigos);
            hora = (TextView) itemView.findViewById(R.id.horaAmigos);
        }
    }

}
