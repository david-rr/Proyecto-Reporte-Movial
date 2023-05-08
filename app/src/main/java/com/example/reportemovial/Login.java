package com.example.reportemovial;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextView txtUser;
    private TextView txtPass;
    private Button btnOlvidar;
    private Button btnIniciar;
    private Button btnRegistro;
    private FirebaseAuth mAuth;

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
                Intent i = new Intent(v.getContext(), Generar_Reporte.class);//crear ventana de recuperacion de contraseña
                startActivity(i);
            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion(txtUser.getText().toString(), txtPass.getText().toString());
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void iniciarSesion(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Intent i = new Intent(Login.this, Generar_Reporte.class);//crear ventana de recuperacion
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null); // reemplazar por nuevo activity
                        }
                    }
                });
    }

    private void reload() {
        Toast.makeText(Login.this, "Vuelve a intentarlo", Toast.LENGTH_SHORT).show();
    }

}