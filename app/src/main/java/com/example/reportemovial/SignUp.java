package com.example.reportemovial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
    private EditText txtPassConf;
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
        txtPassConf = (EditText) findViewById(R.id.txtContrasenaRegistroConf);
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
                if ( validarCampos() && validarEmail() && validarPass() )
                    registro(txtCorreo.getText().toString(), txtPass.getText().toString());
            }
        });

    }

    private boolean validarPass(){
        String pass = txtPass.getText().toString();
        String passC = txtPassConf.getText().toString();

        if ( pass.isEmpty() ){ txtPass.setError("El campo no puede ir vacio"); return false; }
        if ( passC.isEmpty() ){ txtPassConf.setError("El campo no puede ir vacio"); return false; }
        if ( pass.length() < 8 ) { txtPass.setError("Debe tener al menos 8 caracteres"); return false; }
        if ( passC.length() < 8 ) { txtPassConf.setError("Debe tener al menos 8 caracteres"); return false; }
        if ( !passC.equals(pass) ) { txtPassConf.setError("Las contraseñas no coinciden"); return false; }

        txtPass.setError(null);
        txtPassConf.setError(null);
        return true;
    }

    private boolean validarEmail(){
        String email = txtCorreo.getText().toString().trim();

        if ( email.isEmpty() ) { txtCorreo.setError("El campo no puede ir vacio"); return false; }
        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches() ) { txtCorreo.setError("Ingresa un correo valido"); return false; }

        txtCorreo.setError(null);
        return true;
    }

    private boolean validarCampos() {
        String nombre = txtNom.getText().toString();
        String user = txtUser.getText().toString();

        if ( nombre.isEmpty() ) { txtNom.setError("El campo no puede ir vacio"); return false; }
        if ( user.isEmpty() ) { txtUser.setError("El campo no puede ir vacio"); return false; }

        txtNom.setError(null);
        txtUser.setError(null);
        return true;
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
                            db.collection("usuarios_registrados")
                                    .document(correo)
                                    .set(datos)
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUp.this, "No se ha podido registrar cuenta.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog dialogo = new AlertDialog
                                    .Builder(SignUp.this) // NombreDeTuActividad.this, o getActivity() si es dentro de un fragmento
                                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(SignUp.this, FeedCiudadano.class);
                                            startActivity(i);
                                            finish();
                                        }
                                    })
                                    .setTitle("Usuario Registrado") // El título
                                    .setMessage("Los datos se han registrado correctamente.") // El mensaje
                                    .create();// No olvides llamar a Create, ¡pues eso crea el AlertDialog!
                            dialogo.show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Error al iniciar sesión",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}