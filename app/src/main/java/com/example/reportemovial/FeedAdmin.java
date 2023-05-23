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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    ///codigo para menu desplegable
    DrawerLayout drawerLayout;
    Button button;
    //Button button2; //boton para el signo de sugerencias
    LinearLayout VisReportes, visSugerencias, Cerrarsesion;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    //fin de codigo para menu

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

        //Boton que te dirige a la ayuda y sugerencias
        //button2 = (Button) findViewById(R.id.button2); // botton para ir a las sugerencias
        //button2.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //       Intent i = new Intent(view.getContext(), SugerenciaLayout.class);//
        //        startActivity(i);
        //   }
        //});
        //Fin del boton que te lleva a ayuda y sugerencias


        //Inicio Codigo para menu desplegable
        drawerLayout = findViewById(R.id.drawer_layout2);
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
                Toast.makeText(FeedAdmin.this, "Usted está en los reportes", Toast.LENGTH_SHORT).show();
                recreate();
            }
        });

        visSugerencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(FeedAdmin.this, SugerenciaLayout.class);
            }
        });


        Cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarSession();
            }
        });
        //fin Codigo para menu desplegable

    }

    @Override
    protected void onStart() {
        super.onStart();
        ReportAdapter.startListening();
        ReportAdapter.stopListening();
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
        startActivity(new Intent(FeedAdmin.this, Login.class));

    }// Fin METODO PARA CERRAR SESION para el menu desplegable
}
