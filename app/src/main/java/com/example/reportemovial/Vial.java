package com.example.reportemovial;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Vial extends AppCompatActivity {

    private RecyclerView RecyclerViewVial;
    private ReporteAdapter ReportAdapterVial;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnFeed, btnAgua, btnAlumbrado, btnArbol;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prioridad_tipo_vial);



        RecyclerViewVial = findViewById(R.id.RecyclerReportVial);
        RecyclerViewVial.setLayoutManager(new LinearLayoutManager(this));

        Query queryV = db.collection("reportes").whereEqualTo("tipo", "Vial");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptionsVial = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(queryV, Reporte.class).build();

        ReportAdapterVial = new ReporteAdapter(firestoreRecyclerOptionsVial);
        ReportAdapterVial.startListening();
        ReportAdapterVial.notifyDataSetChanged();
        RecyclerViewVial.setAdapter(ReportAdapterVial);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReportAdapterVial.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        ReportAdapterVial.stopListening();
    }
}
