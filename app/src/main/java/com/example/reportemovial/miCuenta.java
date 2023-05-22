package com.example.reportemovial;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class miCuenta extends AppCompatActivity {

    private TextView txt_name, txt_email, txt_user, txt_tipo;
    private Button Cerrar_sesion;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();// Inicializar Firebase Firestore

    ///codigo para menu desplegable
    DrawerLayout drawerLayout;
    Button button; //boton para accionar el menu desplegable
    Button button2; //boton para las sugerencias
    LinearLayout VisReportes, MisReportes, CrearReportes, MiCuenta;
    //fin de codigo para menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);
        mAuth = FirebaseAuth.getInstance();
        // Obtener referencias a los elementos del layout
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        txt_user = findViewById(R.id.txt_user);
        txt_tipo = findViewById(R.id.txt_tipo);
        Cerrar_sesion = (Button) findViewById(R.id.exit_session);

        // Realizar la consulta a Firestore

                 FirebaseUser user = mAuth.getCurrentUser();
                 db.collection("usuarios_registrados")
                         .document(user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Obtener los datos del documento
                            String nombre = document.getString("nombre");
                            String email = user.getEmail();
                            String usuario = document.getString("usuario");
                            String Tipo = document.getString("tipo");


                            // Mostrar los datos en la interfaz de usuario
                            txt_name.setText(nombre);
                            txt_email.setText(email);
                            txt_user.setText(usuario);
                            txt_tipo.setText(Tipo);

                        }
                    } else {
                        // Manejar el error de la consulta
                    }
                });
        Cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(v.getContext(), Login.class);
                startActivity(i);
                finish();
            }
        });


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
        drawerLayout = findViewById(R.id.drawer_layout10);
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

                redirectActivity(miCuenta.this, FeedCiudadano.class); // te manda de la actividad mis reportes a feed ciudadano
            }
        });


        CrearReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(miCuenta.this, Generar_Reporte.class);
            }
        });

        MisReportes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectActivity(miCuenta.this, mis_reportes.class);
            }
        });

        MiCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
                Toast.makeText(miCuenta.this, "Usted está en su perfil", Toast.LENGTH_SHORT).show();
            }
        });

        //fin de codigo para menu desplegable

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
