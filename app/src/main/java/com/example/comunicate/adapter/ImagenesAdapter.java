package com.example.comunicate.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.comunicate.R;
import com.example.comunicate.utils.Imagenes;

import java.util.ArrayList;
import java.util.List;

public class ImagenesAdapter extends ArrayAdapter<Imagenes> {
    private Context context;
    ArrayList<Imagenes> listImagen;

    RequestQueue requestQueue;

    public class ImagenesHolder {

        public TextView nameText;
        public ImageView avatarImage;


    }
    public ImagenesAdapter(Context context,  int id, List<Imagenes> nuevalistaImg) {
        super(context, id, nuevalistaImg);
        this.listImagen = new ArrayList<>();
        this.listImagen.addAll(nuevalistaImg);
        requestQueue = Volley.newRequestQueue(context);

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImagenesAdapter.ImagenesHolder holder = null;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.grid_item, parent, false);

            holder = new ImagenesAdapter.ImagenesHolder();

            holder.nameText = (TextView) convertView.findViewById(R.id.nombre_imagen);
            holder.avatarImage = (ImageView) convertView.findViewById(R.id.imagen);


            convertView.setTag(holder);

        } else {

            holder = (ImagenesAdapter.ImagenesHolder) convertView.getTag();
        }


        Imagenes img = listImagen.get(position);
        holder.nameText.setText(img.getNombre_imagen());
        if (listImagen.get(position).getUrlImagen()!=null){
            cargarImagen(listImagen.get(position).getUrlImagen(), holder);
        }


        return convertView;
    }



    private void cargarImagen(String urlImagen, ImagenesHolder holder) {

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


}
