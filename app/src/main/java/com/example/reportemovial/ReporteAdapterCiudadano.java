package com.example.reportemovial;

import static com.example.reportemovial.FeedAdmin.Resumen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReporteAdapterCiudadano extends FirestoreRecyclerAdapter<Reporte, ReporteAdapterCiudadano.ViewHolder> {
    private android.content.Context mContext;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ReporteAdapterCiudadano(@NonNull FirestoreRecyclerOptions<Reporte> options, Context mContext) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull ReporteAdapterCiudadano.ViewHolder holder, int position, @NonNull Reporte Reporte) {
        holder.TextViewTitulo.setText(Reporte.getTipo());
        holder.TextViewDescripcion.setText(Reporte.getDescripcion());

        StorageReference islandRef = storageRef.child("images/"+Reporte.getImagen());

        asignarDireccion(holder, position, Reporte);

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

    private void asignarDireccion(ViewHolder holder, int position, Reporte reporte) {
        Geocoder geocoder = new Geocoder(mContext.getApplicationContext(), Locale.getDefault());
        try {
            Log.e("Latitud: ", +(double)reporte.getUbicacion().get("latitude")+" Longitud: "+(double)reporte.getUbicacion().get("longitude"));
            List<Address> direction = geocoder.getFromLocation((double)reporte.getUbicacion().get("latitude"), (double)reporte.getUbicacion().get("longitude"), 1);
            if ( direction.size() > 0 )
                holder.TextViewDir.setText(direction.get(0).getAddressLine(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(mContext).inflate(R.layout.cart_report_ciudadano, parent, false);
        return new ViewHolder(vista);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TextViewTitulo;
        TextView TextViewDescripcion;
        ImageView ImageViewEvidencia;
        TextView TextViewDir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            TextViewTitulo = itemView.findViewById(R.id.titulo);
            TextViewDescripcion = itemView.findViewById(R.id.descripcion);
            ImageViewEvidencia = itemView.findViewById(R.id.ImagenReporte);
            TextViewDir = itemView.findViewById(R.id.direccion);
        }
    }
}
