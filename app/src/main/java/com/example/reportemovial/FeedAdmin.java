package com.example.reportemovial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FeedAdmin extends AppCompatActivity {

    private RecyclerView RecyclerViewReporte ;
    private ReporteAdapter ReportAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView totReport, pendiente, progreso, atendido, vial, agua, alumbrado, arbol;
    private int auxTot , auxPendiente, auxProgreso, auxAtendido;
    private int auxVial, auxAgua, auxAlumbrado, auxArbol;

    public static PanelInfo Resumen = new PanelInfo();

    private Button btnVial, btnAgua, btnAlumbrado, btnArbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_admin);//Feed de Ciudadano

        totReport = findViewById(R.id.numReport);
        pendiente = findViewById(R.id.pendienteInfo);
        progreso = findViewById(R.id.progresoInfo);
        atendido = findViewById(R.id.atendidoInfo);
        vial = findViewById(R.id.vialInfo);
        agua = findViewById(R.id.aguaInfo);
        alumbrado = findViewById(R.id.alumbradoInfo);
        arbol = findViewById(R.id.arbolInfo);
        btnVial =  findViewById(R.id.FiltroVial);
        btnAgua =  findViewById(R.id.FiltroAgua);
        btnAlumbrado =  findViewById(R.id.FiltroAlumbrado);
        btnVial =  findViewById(R.id.FiltroVial);
        btnArbol = findViewById(R.id.FiltroArbol);

        RecyclerViewReporte = findViewById(R.id.RecyclerReportAdmin);
        RecyclerViewReporte.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("reportes");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(query, Reporte.class).build();

        ReportAdapter = new ReporteAdapter(firestoreRecyclerOptions, FeedAdmin.this);
        ReportAdapter.notifyDataSetChanged();
        RecyclerViewReporte.setAdapter(ReportAdapter);

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
        ReportAdapter.startListening();
        auxTot = Resumen.getTotal();
        totReport.setText(String.valueOf(auxTot));
        auxPendiente = Resumen.getPendiente();
        pendiente.setText(String.valueOf(auxPendiente));
        auxProgreso = Resumen.getProgreso();
        progreso.setText(String.valueOf(auxProgreso));
        auxAtendido = Resumen.getAtendido();
        atendido.setText(String.valueOf(auxAtendido));
        auxVial = Resumen.getVial();
        vial.setText(String.valueOf(auxVial));
        auxAgua = Resumen.getAgua();
        agua.setText(String.valueOf(auxAgua));
        auxAlumbrado = Resumen.getAlumbrado();
        alumbrado.setText(String.valueOf(auxAlumbrado));
        auxArbol = Resumen.getArbol();
        arbol.setText(String.valueOf(auxArbol));
    }

    @Override
    protected void onStop() {
        super.onStop();
        ReportAdapter.stopListening();
    }

}
