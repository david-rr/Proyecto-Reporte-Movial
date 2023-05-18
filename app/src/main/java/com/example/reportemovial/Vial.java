package com.example.reportemovial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prioridad_tipo_vial);

        btnAgua =  findViewById(R.id.FiltroAgua);
        btnAlumbrado =  findViewById(R.id.FiltroAlumbrado);
        btnFeed =  findViewById(R.id.FiltroPrioridad);
        btnArbol = findViewById(R.id.FiltroArbol);

        RecyclerViewVial = findViewById(R.id.RecyclerReportVial);
        RecyclerViewVial.setLayoutManager(new LinearLayoutManager(this));

        Query queryV = db.collection("reportes").whereEqualTo("tipo", "Vial");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptionsVial = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(queryV, Reporte.class).build();

        ReportAdapterVial = new ReporteAdapter(firestoreRecyclerOptionsVial, Vial.this);
        ReportAdapterVial.startListening();
        ReportAdapterVial.notifyDataSetChanged();
        RecyclerViewVial.setAdapter(ReportAdapterVial);

        btnFeed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), FeedAdmin.class);//crear ventana de filtro tipo vial
                startActivity(i);
            }
        });

        btnAgua.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), Agua.class);//crear ventana de filtro por tipo agua
                startActivity(i);
            }
        });

        btnAlumbrado.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), Alumbrado.class);//crear ventana de filtro por tipo alumbrado
                startActivity(i);
            }
        });

        btnArbol.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), Arbol.class);//crear ventana de filtro por tipo arbol
                startActivity(i);
            }
        });
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
