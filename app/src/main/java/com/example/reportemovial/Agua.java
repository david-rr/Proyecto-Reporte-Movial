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

public class Agua extends AppCompatActivity {

    private RecyclerView RecyclerViewAgua;
    private ReporteAdapter ReportAdapterAgua;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnFeed, btnVial, btnAlumbrado, btnArbol;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prioridad_tipo_agua);

        btnFeed =  findViewById(R.id.FiltroPrioridad);
        btnAlumbrado =  findViewById(R.id.FiltroAlumbrado);
        btnVial =  findViewById(R.id.FiltroVial);
        btnArbol = findViewById(R.id.FiltroArbol);

        RecyclerViewAgua = findViewById(R.id.RecyclerReportAgua);
        RecyclerViewAgua.setLayoutManager(new LinearLayoutManager(this));

        Query queryV = db.collection("reportes").whereEqualTo("tipo", "Agua/Alcantarillado");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptionsVial = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(queryV, Reporte.class).build();

        ReportAdapterAgua = new ReporteAdapter(firestoreRecyclerOptionsVial, Agua.this);
        ReportAdapterAgua.startListening();
        ReportAdapterAgua.notifyDataSetChanged();
        RecyclerViewAgua.setAdapter(ReportAdapterAgua);

        btnVial.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), Vial.class);//crear ventana de filtro tipo vial
                startActivity(i);
            }
        });

        btnFeed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), FeedAdmin.class);//crear ventana de filtro por tipo agua
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
        ReportAdapterAgua.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        ReportAdapterAgua.stopListening();
    }
}
