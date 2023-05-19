package com.example.reportemovial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ReporteAdapterFiltroTipo extends FirestoreRecyclerAdapter<Reporte, ReporteAdapterFiltroTipo.ViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private android.content.Context mContext;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReporteAdapterFiltroTipo(@NonNull FirestoreRecyclerOptions<Reporte> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReporteAdapterFiltroTipo.ViewHolder holder, int position, @NonNull Reporte Reporte) {
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

        StorageReference islandRef = storageRef.child("images/"+Reporte.getImagen());

        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                InputStream is = new ByteArrayInputStream(bytes);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                holder.ImageViewEvidencia.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }


    private void activarEstado(ReporteAdapterFiltroTipo.ViewHolder holder, int position, String estado) {
        if(estado.equals("Pendiente"))
            holder.pendiente.setChecked(true);
        if(estado.equals("En progreso"))
            holder.progreso.setChecked(true);
        if (estado.equals("Atendido"))
            holder.atendido.setChecked(true);
    }

    @NonNull
    @Override
    public ReporteAdapterFiltroTipo.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(mContext).inflate(R.layout.cart_reporte_admin, parent, false);
        return new ViewHolder(vista);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TextViewTitulo;
        TextView TextViewDescripcion;
        ImageView ImageViewEvidencia;
        RadioGroup RadioGroupEstado;
        RadioButton pendiente, progreso, atendido;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TextViewTitulo = itemView.findViewById(R.id.titulo);
            TextViewDescripcion = itemView.findViewById(R.id.descripcion);
            ImageViewEvidencia = itemView.findViewById(R.id.ImagenReporte);
            RadioGroupEstado = itemView.findViewById(R.id.Estado);
            pendiente = itemView.findViewById(R.id.pendienteEstadoCart);
            progreso = itemView.findViewById(R.id.progresoEstadoCart);
            atendido = itemView.findViewById(R.id.atendidoEstadoCart);
        }
    }
}