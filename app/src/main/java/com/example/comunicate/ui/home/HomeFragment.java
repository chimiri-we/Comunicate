package com.example.comunicate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comunicate.R;
import com.example.comunicate.databinding.FragmentHomeBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView edtPublicar = binding.editTextTextPersonName;
        RecyclerView rv = binding.reciclerPublicacion;

        edtPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertar();
            }
        });

         return root;
    }

    private void insertar() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View viewCon = inflater.inflate(R.layout.activity_insert, null, false);
        TextInputEditText edtContra = viewCon.findViewById(R.id.edt_contras);
        TextInputLayout layout = viewCon.findViewById(R.id.label);
        TextInputLayout layoutdos = viewCon.findViewById(R.id.label_descripcion);
        Button btn = viewCon.findViewById(R.id.boton);

        TextInputEditText edtConfirCon = viewCon.findViewById(R.id.edt_confirmar);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(viewCon);
        builder.create();


        builder.setNegativeButton("CANCELAR", (dialog, which) -> Toast.makeText(getContext(), "Tarea Cancelada",Toast.LENGTH_LONG).show());

        builder.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}