package com.example.comunicate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.comunicate.R;
import com.example.comunicate.utils.Usuario;

import java.util.ArrayList;

public class BuscarAdapter extends ArrayAdapter<Usuario>{
        public ArrayList<Usuario> MainList;

    public ArrayList<Usuario> myModels;

    public BuscarAdapter.DataFiltering dataFiltering;

    public BuscarAdapter(Context context, int id, ArrayList<Usuario> studentArrayList) {
        super(context, id, studentArrayList);
        this.myModels = new ArrayList<Usuario>();
        this.myModels.addAll(studentArrayList);
        this.MainList = new ArrayList<Usuario>();
        this.MainList.addAll(studentArrayList);
    }

    @Override
    public Filter getFilter() {

        if (dataFiltering == null) {

            dataFiltering = new BuscarAdapter.DataFiltering();
        }
        return dataFiltering;
    }


    public static class BuscarHolder {
        public TextView nameText, telefono, apellido;
        public ImageView avatarImage;
        public CardView crUsuarios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BuscarAdapter.BuscarHolder holder = null;

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_cliente, null);

            holder = new BuscarHolder();

            holder.crUsuarios = convertView.findViewById(R.id.cardViewUsuarios);
            holder.nameText = (TextView) convertView.findViewById(R.id.tv_name);
            holder.telefono = convertView.findViewById(R.id.tv_telefono);
            holder.apellido = convertView.findViewById(R.id.tv_apellido);
            holder.avatarImage = (ImageView) convertView.findViewById(R.id.iv_avatar);
         //   holder.Name = (TextView) convertView.findViewById(R.id.textviewName);

         //   holder.Number = (TextView) convertView.findViewById(R.id.textviewPhoneNumber);

            convertView.setTag(holder);

        } else {

            holder = (BuscarAdapter.BuscarHolder) convertView.getTag();
        }

        Usuario student = myModels.get(position);
        holder.nameText.setText(student.getNombre());
        holder.telefono.setText(student.getTelefono());
        holder.apellido.setText(student.getApellidos());


        return convertView;

    }

    private class DataFiltering extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            charSequence = charSequence.toString().toLowerCase();

            FilterResults filterResults = new FilterResults();

            if (charSequence.toString().length() > 0) {

                ArrayList<Usuario> arrayList1 = new ArrayList<Usuario>();

                for (int i = 0, l = MainList.size(); i < l; i++) {
                     Usuario subject = MainList.get(i);

                    if (subject.getNombre().toString().toLowerCase().contains(charSequence))

                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;
            } else {
                synchronized (this) {
                    filterResults.values = MainList;

                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            myModels = (ArrayList<Usuario>) filterResults.values;

            notifyDataSetChanged();

            clear();

            for (int i = 0, l = myModels.size(); i < l; i++)
                add(myModels.get(i));

            notifyDataSetInvalidated();
        }
    }
}
