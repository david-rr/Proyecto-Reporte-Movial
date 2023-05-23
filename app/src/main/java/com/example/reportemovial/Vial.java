package com.example.reportemovial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Vial extends AppCompatActivity {

    private RecyclerView RecyclerViewVial;
    private ReporteAdapterFiltroTipo ReportAdapterVial;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btnFeed, btnAgua, btnAlumbrado, btnArbol;

    ///codigo para menu desplegable
    DrawerLayout drawerLayout;
    Button button;
    Button button2; //boton para el signo de sugerencias
    LinearLayout VisReportes, visSugerencias, Cerrarsesion;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    //fin de codigo para menu

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

        Query query = db.collection("reportes").whereEqualTo("tipo", "Vial");

        FirestoreRecyclerOptions<Reporte> firestoreRecyclerOptionsVial = new FirestoreRecyclerOptions.Builder<Reporte>()
                .setQuery(query, Reporte.class).build();

        ReportAdapterVial = new ReporteAdapterFiltroTipo(firestoreRecyclerOptionsVial, Vial.this);
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

        //Boton que te dirige a la ayuda y sugerencias
        // button2 = (Button) findViewById(R.id.button2); // botton para ir a las sugerencias
       //  button2.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         Intent i = new Intent(view.getContext(), SugerenciaLayout.class);//
        //         startActivity(i);
        //     }
        // });
        //Fin del boton que te lleva a ayuda y sugerencias



        //Inicio Codigo para menu desplegable
        drawerLayout = findViewById(R.id.drawer_layout6);
        VisReportes = findViewById(R.id.VisReportes); //actividad Principal feed Admin
        visSugerencias = findViewById(R.id.visSugerencias); // texto que te lleva a las sugerencias
        Cerrarsesion = findViewById(R.id.Cerrarsesion);
        button = (Button) findViewById(R.id.button); //boton para desplegar el menu


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openDrawer(drawerLayout);
            }
        });
        VisReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), FeedAdmin.class);//te lleva de la ventana actual al feed admin
                startActivity(i);

            }
        });

        visSugerencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Vial.this, SugerenciaLayout.class);
            }
        });



        Cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CerrarSession();
            }
        });//fin Codigo para menu desplegable
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

    //METODO PARA CERRAR SESION para el menu desplegable
    private void CerrarSession(){
        firebaseAuth.signOut();
        Toast.makeText(this, "Ha cerrado sesión", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Vial.this, Login.class));

    }// Fin METODO PARA CERRAR SESION para el menu desplegable
}
