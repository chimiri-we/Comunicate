package com.example.comunicate.adapter;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comunicate.R;
import com.example.comunicate.mensajes.MensajeDeTexto;

import java.util.List;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<MensajeDeTexto> mensajeDeTextos;
    private Context context;

    public ChatAdapter(List<MensajeDeTexto> mensajeDeTexto,Context context) {
        this.mensajeDeTextos = mensajeDeTexto;
        this.context = context;
    }



    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_mensajes,parent,false);
        return new ChatAdapter.ChatViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ChatViewHolder holder, int position) {

        MensajeDeTexto msj = mensajeDeTextos.get(position);
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) holder.mensajeBG.getLayoutParams();

        LinearLayout.LayoutParams llMensaje = (LinearLayout.LayoutParams) holder.TvMensaje.getLayoutParams();
        LinearLayout.LayoutParams llHora = (LinearLayout.LayoutParams) holder.TvHora.getLayoutParams();

        if(mensajeDeTextos.get(position).getTipoMensaje()==1){//EMISOR
            holder.tvNombre.setText(msj.getEmisor());

            holder.mensajeBG.setBackgroundResource(R.drawable.in_message_bg);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            llMensaje.gravity = Gravity.RIGHT;
            llHora.gravity = Gravity.RIGHT;
            fl.gravity = Gravity.RIGHT;
            holder.TvMensaje.setGravity(Gravity.RIGHT);
        }else if(mensajeDeTextos.get(position).getTipoMensaje()==2){//RECEPTOR
            holder.tvNombre.setText(msj.getId());

            holder.mensajeBG.setBackgroundResource(R.drawable.out_message_bg);
            rl.addRule(0,RelativeLayout.ALIGN_PARENT_RIGHT);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            llMensaje.gravity = Gravity.LEFT;
            llHora.gravity = Gravity.LEFT;
            fl.gravity = Gravity.LEFT;
            holder.TvMensaje.setGravity(Gravity.LEFT);
        }

        holder.cardView.setLayoutParams(rl);
        holder.mensajeBG.setLayoutParams(fl);
        holder.TvMensaje.setLayoutParams(llMensaje);
        holder.TvHora.setLayoutParams(llHora);

        holder.TvMensaje.setText(msj.getMensaje());
        holder.TvHora.setText(msj.getHoraDelMensaje());
        // holder.tvNombre.setText(msj.getId());
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) holder.cardView.getBackground().setAlpha(0);
        else holder.cardView.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
    }

    @Override
    public int getItemCount() {
        return mensajeDeTextos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mensajeDeTextos.get(position).getTipoMensaje();
    }

    public void addNewMessage(@NonNull MensajeDeTexto mensajeDeTexto) {
        mensajeDeTextos.add(mensajeDeTexto);
        notifyItemInserted(mensajeDeTextos.size() - 1);
    }


    static class ChatViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        LinearLayout mensajeBG;
        TextView TvMensaje;
        TextView TvHora;
        TextView tvNombre;

        ChatViewHolder(View itemView){
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cvMensaje);
            mensajeBG = (LinearLayout) itemView.findViewById(R.id.mensajeBG);
            TvMensaje = (TextView) itemView.findViewById(R.id.msTexto);
            TvHora = (TextView) itemView.findViewById(R.id.msHora);
            tvNombre = itemView.findViewById(R.id.msNombre);
        }
    }
}


