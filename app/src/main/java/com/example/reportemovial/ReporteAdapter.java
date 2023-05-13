package com.example.reportemovial;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.example.reportemovial.Reporte;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

public class ReporteAdapter extends FirestoreRecyclerAdapter<Reporte, ReporteAdapter.ViewHolder> {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Activity activity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReporteAdapter(@NonNull FirestoreRecyclerOptions<Reporte> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Reporte Reporte) {
        DocumentSnapshot ReporteDocument = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = ReporteDocument.getId();
        holder.TextViewTitulo.setText(Reporte.getTipo());
        holder.TextViewDescripcion.setText(Reporte.getDescripcion());
        //holder.ImageViewEvidencia;
        activarEstado(holder, position, Reporte.getEstado());
        holder.RadioGroupEstado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(holder.pendiente.isChecked()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Pendiente");
                    db.collection("reportes").document(id).update(map);
                }
                if(holder.progreso.isChecked()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "En progreso");
                    db.collection("reportes").document(id).update(map);
                }
                if(holder.atendido.isChecked()){
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Atendido");
                    db.collection("reportes").document(id).update(map);
                }

            }
        });
    }

    private void activarEstado(ViewHolder holder, int position, String estado) {
        if(estado.equals("Pendiente"))
            holder.pendiente.setChecked(true);
        if(estado.equals("En progreso"))
            holder.progreso.setChecked(true);
        if (estado.equals("Atendido"))
            holder.atendido.setChecked(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_reporte, parent, false);
        return new ViewHolder(vista);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TextViewTitulo;
        TextView TextViewDescripcion;
        //ImageView ImageViewEvidencia;
        RadioGroup RadioGroupEstado;
        RadioButton pendiente, progreso, atendido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TextViewTitulo = itemView.findViewById(R.id.titulo);
            TextViewDescripcion = itemView.findViewById(R.id.descripcion);
            //ImageViewEvidencia = itemView.findViewById(R.id.ImagenReporte);
            RadioGroupEstado = itemView.findViewById(R.id.Estado);
            pendiente = itemView.findViewById(R.id.pendienteEstadoCart);
            progreso = itemView.findViewById(R.id.progresoEstadoCart);
            atendido = itemView.findViewById(R.id.atendidoEstadoCart);
        }
    }
}
