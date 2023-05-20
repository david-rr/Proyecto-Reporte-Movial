package com.example.reportemovial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Adapter extends FirestoreRecyclerAdapter<Sugerencia,Adapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Adapter(@NonNull FirestoreRecyclerOptions<Sugerencia> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Sugerencia model) {
     holder.suge.setText(model.getSuge());
     holder.numero.setText(String.valueOf(position+1));


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sugerencia_card, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView suge, numero;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            suge = itemView.findViewById(R.id.sugerencia);
            numero = itemView.findViewById(R.id.numero);

        }
    }
}
