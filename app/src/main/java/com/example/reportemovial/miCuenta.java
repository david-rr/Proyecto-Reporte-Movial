package com.example.reportemovial;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class miCuenta extends AppCompatActivity {

    private TextView txt_name, txt_email, txt_address, txt_phone;
    private Button Cerrar_sesion;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();// Inicializar Firebase Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        // Obtener referencias a los elementos del layout
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        txt_address = findViewById(R.id.txt_address);
        txt_phone = findViewById(R.id.txt_phone);
        Cerrar_sesion = (Button) findViewById(R.id.exit_session);

        // Realizar la consulta a Firestore
        db.collection("usuarios_registrados")
                .document("ciudadano@gmail.com")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Obtener los datos del documento
                            String nombre = document.getString("nombre");
                            String email = document.getString("email");
                            String direccion = document.getString("address");
                            String telefono = document.getString("telf");


                            // Mostrar los datos en la interfaz de usuario
                            txt_name.setText(nombre);
                            txt_email.setText(email);
                            txt_address.setText(direccion);
                            txt_phone.setText(telefono);

                        }
                    } else {
                        // Manejar el error de la consulta
                    }
                });
        Cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Login.class);
                startActivity(i);
            }
        });
    }
}
