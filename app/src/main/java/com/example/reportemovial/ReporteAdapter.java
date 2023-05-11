package com.example.reportemovial;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.example.reportemovial.Reporte;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import javax.annotation.Nonnull;

public class ReporteAdapter extends FirestoreRecyclerAdapter<Reporte, ReporteAdapter.ViewHolder> {
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
        holder.TextViewTitulo.setText(Reporte.getTipo());
        holder.TextViewDescripcion.setText(Reporte.getDescripcion());
        //holder.ImageViewEvidencia;
        //holder.RadioGroupEstado
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
        //RadioGroup RadioGroupEstado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TextViewTitulo = itemView.findViewById(R.id.titulo);
            TextViewDescripcion = itemView.findViewById(R.id.descripcion);
            //ImageViewEvidencia = itemView.findViewById(R.id.ImagenReporte);
            //RadioGroupEstado = itemView.findViewById(R.id.Estado);
        }
    }
}
