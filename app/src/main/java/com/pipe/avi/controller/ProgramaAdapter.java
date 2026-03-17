package com.pipe.avi.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pipe.avi.R;
import com.pipe.avi.model.Programa;

import java.util.ArrayList;
import java.util.List;

public class ProgramaAdapter extends RecyclerView.Adapter<ProgramaAdapter.ViewHolder> {

    private List<Programa> programas;
    private OnProgramaClickListener listener;
    private int lastPosition = -1;

    public interface OnProgramaClickListener {
        void onProgramaClick(Programa programa);
    }

    public ProgramaAdapter(List<Programa> programas, OnProgramaClickListener listener) {
        this.programas = (programas != null) ? programas : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_programa, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Programa programa = programas.get(position);

        String nombre = programa.getNombre() != null ? programa.getNombre() : "Sin nombre";
        String nivel = programa.getNivel() != null ? programa.getNivel() : "Sin nivel";
        String descripcion = programa.getDescripcion() != null ? programa.getDescripcion() : "Sin descripción disponible.";

        holder.txtNombre.setText(nombre);
        holder.txtNivel.setText(nivel);
        holder.txtDescripcion.setText(descripcion);

        // Animación al aparecer (usando posición segura)
        int currentPosition = holder.getAdapterPosition();

        if (currentPosition > lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(
                    holder.itemView.getContext(),
                    R.anim.slide_up);

            holder.itemView.startAnimation(animation);

            lastPosition = currentPosition;
        }

        // BOTÓN AR
        holder.btnVerAR.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION && listener != null) {

                Animation press = AnimationUtils.loadAnimation(
                        v.getContext(),
                        R.anim.boton_press);

                v.startAnimation(press);

                listener.onProgramaClick(programas.get(pos));
            }
        });

        // EXPANDIR / CONTRAER TARJETA
        holder.layoutPrincipal.setOnClickListener(v -> {

            Animation press = AnimationUtils.loadAnimation(
                    v.getContext(),
                    R.anim.boton_press);

            v.startAnimation(press);

            if (holder.layoutExpandible.getVisibility() == View.GONE) {

                holder.layoutExpandible.setVisibility(View.VISIBLE);
                holder.txtToggle.setText("Ver menos");

                Animation slide = AnimationUtils.loadAnimation(
                        v.getContext(),
                        R.anim.slide_up);

                holder.layoutExpandible.startAnimation(slide);

            } else {

                holder.layoutExpandible.setVisibility(View.GONE);
                holder.txtToggle.setText("Ver más");
            }
        });
    }

    @Override
    public int getItemCount() {
        return programas.size();
    }

    public void updateList(List<Programa> newList) {

        this.programas = (newList != null) ? newList : new ArrayList<>();
        lastPosition = -1;

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre, txtNivel, txtDescripcion, txtToggle;
        Button btnVerAR;
        LinearLayout layoutExpandible, layoutPrincipal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombrePrograma);
            txtNivel = itemView.findViewById(R.id.txtNivelFormacion);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            txtToggle = itemView.findViewById(R.id.txtToggle);

            btnVerAR = itemView.findViewById(R.id.btnVerAR);

            layoutExpandible = itemView.findViewById(R.id.layoutExpandible);
            layoutPrincipal = itemView.findViewById(R.id.layoutPrincipal);
        }
    }
}