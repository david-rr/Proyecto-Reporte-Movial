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

public class Alumbrado extends AppCompatActivity {

    private RecyclerView RecyclerViewAlumbrado;
    private ReporteAdapterFiltroTipo ReportAdapterAlumbrado;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnFeed, btnAgua, btnVial, btnArbol;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prioridad_tipo_alumbrado);

        btnAgua =  findViewById(R.id.FiltroAgua);
        btnFeed =  findViewById(R.id.FiltroPrioridad);
        btnVial =  findViewById(R.id.FiltroVial);
        btnArbol = findViewById(R.id.FiltroArbol);

        RecyclerViewAlumbrado = findViewById(R.id.RecyclerReportAlumbrado);
        RecyclerViewAlumbrado.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("reportes").whereEqualTo("tipo", "Alumbrado");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptionsVial = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(query, Reporte.class).build();

        ReportAdapterAlumbrado = new ReporteAdapterFiltroTipo(firestoreRecyclerOptionsVial, Alumbrado.this);
        ReportAdapterAlumbrado.startListening();
        ReportAdapterAlumbrado.notifyDataSetChanged();
        RecyclerViewAlumbrado.setAdapter(ReportAdapterAlumbrado);

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

        btnFeed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(v.getContext(), FeedAdmin.class);//crear ventana de filtro por tipo alumbrado
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
        ReportAdapterAlumbrado.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        ReportAdapterAlumbrado.stopListening();
    }
}
