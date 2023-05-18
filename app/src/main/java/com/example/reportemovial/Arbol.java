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

public class Arbol extends AppCompatActivity {

    private RecyclerView RecyclerViewArbol;
    private ReporteAdapter ReportAdapterArbol;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnFeed, btnAgua, btnAlumbrado, btnVial;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prioridad_tipo_arbol);

        btnAgua =  findViewById(R.id.FiltroAgua);
        btnAlumbrado =  findViewById(R.id.FiltroAlumbrado);
        btnVial =  findViewById(R.id.FiltroVial);
        btnFeed = findViewById(R.id.FiltroPrioridad);

        RecyclerViewArbol = findViewById(R.id.RecyclerReportArbol);
        RecyclerViewArbol.setLayoutManager(new LinearLayoutManager(this));

        Query queryV = db.collection("reportes").whereEqualTo("tipo", "Árbol Caído");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptionsVial = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(queryV, Reporte.class).build();

        ReportAdapterArbol = new ReporteAdapter(firestoreRecyclerOptionsVial, Arbol.this);
        ReportAdapterArbol.startListening();
        ReportAdapterArbol.notifyDataSetChanged();
        RecyclerViewArbol.setAdapter(ReportAdapterArbol);

        btnVial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), Vial.class);//crear ventana de filtro tipo vial
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

        btnFeed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), FeedAdmin.class);//crear ventana de filtro por tipo arbol
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReportAdapterArbol.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        ReportAdapterArbol.stopListening();
    }
}
