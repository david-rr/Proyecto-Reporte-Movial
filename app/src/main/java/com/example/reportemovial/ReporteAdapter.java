package com.example.reportemovial;


import static com.example.reportemovial.FeedAdmin.Resumen;

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

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReporteAdapter extends FirestoreRecyclerAdapter<Reporte, ReporteAdapter.ViewHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int auxTot, auxPendiente, auxProgreso, auxAtendido;
    private int auxVial, auxAgua, auxAlumbrado, auxArbol;
    private android.content.Context mContext;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReporteAdapter(@NonNull FirestoreRecyclerOptions<Reporte> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Reporte Reporte) {
        DocumentSnapshot ReporteDocument = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = ReporteDocument.getId();
        FirebaseStorage storage = FirebaseStorage.getInstance();
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

         //StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        //Glide.with(mContext).load("images/"+Reporte.getImagen()).into(holder.ImageViewEvidencia);
        //Glide.with(mContext).load(storageReference).into(holder.ImageViewEvidencia);
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child("images/"+Reporte.getImagen());

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(islandRef));
                holder.ImageViewEvidencia.setImageBitmap(bitmap);// Local temp file has been created
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



        auxTot+=1;
        if(Reporte.getEstado().equals("Pendiente")) auxPendiente+=1;
        if(Reporte.getEstado().equals("En progreso")) auxProgreso+=1;
        if(Reporte.getEstado().equals("Atendido")) auxAtendido+=1;
        if(Reporte.getTipo().equals("Vial")) auxVial+=1;
        if(Reporte.getTipo().equals("Agua/Alcantarillado")) auxAgua+=1;
        if(Reporte.getTipo().equals("Alumbrado")) auxAlumbrado+=1;
        if(Reporte.getTipo().equals("Árbol Caído")) auxArbol+=1;

        Resumen.setTotal(auxTot);
        Resumen.setPendiente(auxPendiente);
        Resumen.setProgreso(auxProgreso);
        Resumen.setAtendido(auxAtendido);
        Resumen.setVial(auxVial);
        Resumen.setAgua(auxAgua);
        Resumen.setAlumbrado(auxAlumbrado);
        Resumen.setArbol(auxArbol);
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
        View vista = LayoutInflater.from(mContext).inflate(R.layout.cart_reporte, parent, false);
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
