package com.example.reportemovial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FeedCiudadano extends AppCompatActivity {

    private RecyclerView RecyclerViewReporte;
    private ReporteAdapterCiudadano ReportAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ///codigo para menu desplegable
    DrawerLayout drawerLayout;
    Button button; //boton para accionar el menu desplegable
    Button button2; //boton para las sugerencias
    LinearLayout VisReportes, MisReportes, CrearReportes, MiCuenta;
    //fin de codigo para menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_ciudadano); //Feed de Ciudadano

        RecyclerViewReporte = findViewById(R.id.RecyclerReportCiudadano);
        RecyclerViewReporte.setLayoutManager(new LinearLayoutManager(this));

        Query query = db.collection("reportes");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(query, Reporte.class).build();

        ReportAdapter = new ReporteAdapterCiudadano(firestoreRecyclerOptions, FeedCiudadano.this);
        ReportAdapter.notifyDataSetChanged();
        RecyclerViewReporte.setAdapter(ReportAdapter);

        //Inicio Boton que nos debe llevar a las sugerencias
         button2 = (Button) findViewById(R.id.button2); // botton para ir a las sugerencias
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Ayuda_y_sugerencias.class);//
                startActivity(i);
            }
        });

        //Fin de boton  que nos debe llevar a las sugerencias

        //Inicio Codigo para menu desplegable
        drawerLayout = findViewById(R.id.drawer_layout);
        VisReportes = findViewById(R.id.VisReportes); //actividad Principal feed ciudadano
        MisReportes = findViewById(R.id.MisReportes);
        CrearReportes = findViewById(R.id.CrearReportes);
        MiCuenta = findViewById(R.id.MiCuenta);
        button = (Button) findViewById(R.id.button); //boton que activa el menu desplegable


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDrawer(drawerLayout);
            }
        });
        VisReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
                Toast.makeText(FeedCiudadano.this, "Usted está en los reportes", Toast.LENGTH_SHORT).show();
            }
        });


        CrearReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(FeedCiudadano.this, Generar_Reporte.class);
            }
        });

        MisReportes.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View view) {
                redirectActivity(FeedCiudadano.this, mis_reportes.class);
            }
        });

        MiCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(FeedCiudadano.this, miCuenta.class);
            }
        });

        //fin de codigo para menu desplegable
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

    //codigo para menu desplegable
    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
    //fin de codigo para menu

}