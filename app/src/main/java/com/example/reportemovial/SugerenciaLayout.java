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
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;

public class SugerenciaLayout extends AppCompatActivity {

    RecyclerView mRecycler;
    Adapter adapter;
    FirebaseFirestore mfirestore;

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
        setContentView(R.layout.sugerencias);

        mfirestore = FirebaseFirestore.getInstance();
        mRecycler= findViewById(R.id.Recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mfirestore.collection("sugerencia");

        FirestoreRecyclerOptions<Sugerencia> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Sugerencia>().setQuery(query, Sugerencia.class).build();

        adapter = new Adapter(firestoreRecyclerOptions);
        adapter.notifyDataSetChanged();
        mRecycler.setAdapter(adapter);

        //Inicio Codigo para menu desplegable
        drawerLayout = findViewById(R.id.drawer_layout11);
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
                redirectActivity(SugerenciaLayout.this, FeedAdmin.class);
            }
        });

        visSugerencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SugerenciaLayout.this, "Usted está en las sugerencias", Toast.LENGTH_SHORT).show();
                recreate();
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
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
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
        startActivity(new Intent(SugerenciaLayout.this, Login.class));

    }// Fin METODO PARA CERRAR SESION para el menu desplegable
}
