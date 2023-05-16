package com.example.reportemovial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText txtNom;
    private EditText txtUser;
    private EditText txtCorreo;
    private EditText txtPass;
    private Button btnRegistrar;
    private Button btnIniciarSesion;
    private static final String TAG = "SignUp";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtNom = (EditText) findViewById(R.id.txtNomRegistro);
        txtUser = (EditText) findViewById(R.id.txtUserRegistro);
        txtCorreo = (EditText) findViewById(R.id.txtCorreoRegistro);
        txtPass = (EditText) findViewById(R.id.txtContrasenaRegistro);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrarRegistro);
        btnIniciarSesion = (Button) findViewById(R.id.btnIniciarSesRegistro);

        mAuth = FirebaseAuth.getInstance();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
                finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( txtCorreo.getText().toString().equals(txtCorreo.getText().toString()) && txtPass.getText().toString()
                        .equals(txtPass.getText().toString())){ //reglas de registro
                    registro(txtCorreo.getText().toString(), txtPass.getText().toString());

                }
            }
        });

    }

    private void registro(String correo, String pass){
        mAuth.createUserWithEmailAndPassword(correo, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> datos = new HashMap<>();
                            datos.put("usuario", txtUser.getText().toString());
                            datos.put("nombre", txtNom.getText().toString());
                            datos.put("tipo", "ciudadano");
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            finish();
                            Toast.makeText(SignUp.this, "Usuario registrado correctamente",
                                    Toast.LENGTH_LONG).show();
                            db.collection("usuarios_registrados")
                                    .document(correo)
                                    .set(datos)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUp.this, "No se ha podido registrar cuenta.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Error al crear cuenta",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}