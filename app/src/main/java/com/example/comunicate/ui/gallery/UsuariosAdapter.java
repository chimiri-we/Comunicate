package com.example.comunicate.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.comunicate.R;
import com.example.comunicate.ui.detalle.DetallePerfilActivity;
import com.example.comunicate.utils.Usuario;

import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.UsuariosHolder> {
    List<Usuario> usuarioList;

RequestQueue requestQueue;
    int id_producto;
    int precio;
    String id;
    //  String nombre;
    Context context;
    String piezas;
    String formattedDate;
    int status = 0;
    int id_venta;
    Bitmap img;
    public class UsuariosHolder extends RecyclerView.ViewHolder{

        public TextView nameText, telefono, apellido;
        public ImageView avatarImage;
        public CardView crUsuarios;




        public UsuariosHolder(View itemView) {
            super(itemView);
            crUsuarios = itemView.findViewById(R.id.cardViewUsuarios);
             nameText = (TextView) itemView.findViewById(R.id.tv_name);
             telefono = itemView.findViewById(R.id.tv_telefono);
             apellido = itemView.findViewById(R.id.tv_apellido);
             avatarImage = (ImageView) itemView.findViewById(R.id.iv_avatar);
        }
    }

    public UsuariosAdapter(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
        requestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public UsuariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cliente, parent, false);

        return new UsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(UsuariosHolder holder, int position) {
        Usuario usuario = (Usuario) usuarioList.get(position);

        holder.nameText.setText(usuario.getNombre());
        holder.telefono.setText(usuario.getTelefono());
        holder.apellido.setText(usuario.getApellidos());
        String nombre = usuarioList.get(position).getNombre();

        id = String.valueOf(usuarioList.get(position).getId());
        holder.crUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetallePerfilActivity.class);
                intent.putExtra("id_receptor", id);
                intent.putExtra("nombre", nombre);
                v.getContext().startActivity(intent);

                /*
                Intent chatIntent = new Intent(v.getContext(), chat.class);
                chatIntent.putExtra("nombre", nombre);
                chatIntent.putExtra("id_receptor", id);
                v.getContext().startActivity(chatIntent);
            */
            }
        });
        id_producto = usuarioList.get(position).getId();

     /*   Glide.with(context)
                .asBitmap()
                .load(Uri.parse("file:///android_asset/"))
                .error(R.drawable.ic_persona)
                .centerCrop()
                .into(new BitmapImageViewTarget(holder.avatarImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable
                                = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        drawable.setCircular(true);
                        holder.avatarImage.setImageDrawable(drawable);
                    }
                });
*/
        if (usuarioList.get(position).getUrlImagen()!=null){
            cargarImagen(usuarioList.get(position).getUrlImagen(), holder);
        }

    }

    private void cargarImagen(String urlImagen, UsuariosHolder holder) {

         String URL = "https://servicioparanegocio.es/Trabajos/"+urlImagen;
         URL=URL.replace(" ","%20");

        ImageRequest imgReq = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                holder.avatarImage.setImageBitmap(response);
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
        return usuarioList.size();
    }
}
