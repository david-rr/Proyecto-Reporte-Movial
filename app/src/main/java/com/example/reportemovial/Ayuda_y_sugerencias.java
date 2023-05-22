package com.example.reportemovial;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Ayuda_y_sugerencias extends AppCompatActivity {

    Button btn_add;

    private FirebaseFirestore mfirestore;

    EditText sugerencia;

    ///codigo para menu desplegable
    DrawerLayout drawerLayout;
    Button button; //boton para accionar el menu desplegable
    Button button2; //boton para las sugerencias
    LinearLayout VisReportes, MisReportes, CrearReportes, MiCuenta;
    //fin de codigo para menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayuda_y_sugerencias);

        mfirestore = FirebaseFirestore.getInstance();
        btn_add = findViewById(R.id.btn_add);
        sugerencia = findViewById(R.id.numero);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suge= sugerencia.getText().toString();

                if(suge.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Favor de ingresar su sugerencia",Toast.LENGTH_SHORT).show();
                }else{
                    PostSug(suge);
                }
            }


        });

         button2 = (Button) findViewById(R.id.button2); // botton para ir a las sugerencias
         button2.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View view) {

                recreate();
                Toast.makeText(Ayuda_y_sugerencias.this, "Usted está en ayuda y sugerencias", Toast.LENGTH_SHORT).show();
            }
         });

        ///Fin de Boton de sugerencias


        //Inicio Codigo para menu desplegable
        drawerLayout = findViewById(R.id.drawer_layout7);
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
                Intent i = new Intent(view.getContext(), FeedCiudadano.class);//te lleva de la ventana actual al feed ciudadano
                startActivity(i);
            }
        });


        CrearReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Ayuda_y_sugerencias.this, Generar_Reporte.class);
            }
        });

        MisReportes.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                redirectActivity(Ayuda_y_sugerencias.this, mis_reportes.class);
           }
        });

        MiCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(Ayuda_y_sugerencias.this, miCuenta.class);
            }
        });
        //fin de codigo para menu desplegable

    }
    private void PostSug(String suge) {

        Map<String, Object> map = new HashMap<>();
        map.put("suge",suge);
        mfirestore.collection("sugerencia").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getApplicationContext(),"Sugerencia añadida",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Sugerencia No añadida",Toast.LENGTH_SHORT).show();
            }
        });

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
