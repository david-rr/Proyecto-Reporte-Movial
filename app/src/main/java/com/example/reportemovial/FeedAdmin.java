package com.example.reportemovial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FeedAdmin extends AppCompatActivity {

    private RecyclerView RecyclerViewReporte;
    private ReporteAdapter ReportAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_admin);//Feed de Ciudadano

        RecyclerViewReporte = findViewById(R.id.RecyclerReport);
        RecyclerViewReporte.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("reportes");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(query, Reporte.class).build();

        ReportAdapter = new ReporteAdapter(firestoreRecyclerOptions);
        ReportAdapter.notifyDataSetChanged();
        RecyclerViewReporte.setAdapter(ReportAdapter);

        //getReport();
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

    /**private void getReport(){
        db.collection("reportes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document:task.getResult()){
                        String descrip = document.get("descripcion").toString();

                        // TextViewDescripcion.setText(descrip);
                    }
                }else {
                    System.out.println("Error al recuperar la información");
                }
            }
        });
    }*/

}
