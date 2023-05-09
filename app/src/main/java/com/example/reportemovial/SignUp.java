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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignUp extends AppCompatActivity {

    private EditText txtCorreo;
    private EditText txtCCorreo;
    private EditText txtPass;
    private EditText txtCPass;
    private Button btnRegistrar;
    private Button btnIniciarSesion;
    private static final String TAG = "SignUp";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtCorreo = (EditText) findViewById(R.id.txtNomRegistro);
        txtCCorreo = (EditText) findViewById(R.id.txtUserRegistro);
        txtPass = (EditText) findViewById(R.id.txtCorreoRegistro);
        txtCPass = (EditText) findViewById(R.id.txtContrasenaRegistro);
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
                if( txtCorreo.getText().toString().equals(txtCCorreo.getText().toString()) && txtPass.getText().toString()
                        .equals(txtCPass.getText().toString())){ //reglas de registro
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
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}