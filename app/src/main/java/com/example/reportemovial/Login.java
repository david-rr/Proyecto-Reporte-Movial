package com.example.reportemovial;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



public class Login extends AppCompatActivity {

    private TextView txtUser;
    private TextView txtPass;
    private Button btnOlvidar;
    private Button btnIniciar;
    private Button btnRegistro;
    int b = 0;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //login
        mAuth = FirebaseAuth.getInstance();

        txtUser = (TextView) findViewById(R.id.textUserLogin);
        txtPass = (TextView) findViewById(R.id.textPassLogin);
        btnOlvidar = (Button) findViewById(R.id.btnPassForgetLogin);
        btnIniciar = (Button) findViewById(R.id.btnIniciarLogin);
        btnRegistro = (Button) findViewById(R.id.btnRegistroLogin);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SignUp.class);
                startActivity(i);
            }
        });

        btnOlvidar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), mis_reportes.class);//crear ventana de recuperacion de contraseña
                startActivity(i);
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( validarEmailPass() )
                    iniciarSesion(txtUser.getText().toString(), txtPass.getText().toString());
            }
        });


    }

    private boolean validarEmailPass(){
        String email = txtUser.getText().toString().trim();
        String pass = txtPass.getText().toString();

        if ( email.isEmpty() ) { txtUser.setError("El campo no puede ir vacio"); return false; }
        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches() ) { txtUser.setError("Ingresa un correo valido"); return false; }
        if ( pass.isEmpty() ) { txtPass.setError("El campo no puede ir vacio"); return false; }

        txtUser.setError(null);
        txtPass.setError(null);
        return true;
    }

    private void iniciarSesion(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.collection("users_adm")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if (document.getId().equals(user.getEmail())) {
                                                        Intent i = new Intent(Login.this, FeedAdmin.class);
                                                        startActivity(i);
                                                        b = 1;
                                                        break;
                                                    }
                                                }
                                                if (b != 1) {
                                                    //Toast.makeText(Login.this, "ban: " + b, Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(Login.this, FeedCiudadano.class);
                                                    startActivity(i);
                                                }
                                            } else {
                                                System.out.println("Error al recuperar");
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}