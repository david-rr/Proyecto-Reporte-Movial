package com.example.reportemovial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FeedCiudadano extends AppCompatActivity {

    private RecyclerView RecyclerViewReporte;
    private ReporteAdapter ReportAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_ciudadano); //Feed de Ciudadano

        RecyclerViewReporte = findViewById(R.id.RecyclerReportCiudadano);
        RecyclerViewReporte.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("reportes");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(query, Reporte.class).build();

        ReportAdapter = new ReporteAdapter(firestoreRecyclerOptions);
        ReportAdapter.notifyDataSetChanged();
        RecyclerViewReporte.setAdapter(ReportAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReportAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ReportAdapter.stopListening();
    }
}