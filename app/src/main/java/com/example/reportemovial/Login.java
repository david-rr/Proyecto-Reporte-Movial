package com.example.reportemovial;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        mDialog = new ProgressDialog(this);

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
                recuperarPass();
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

    private void recuperarPass() {
        final Dialog dialogPersonalizado = new Dialog(Login.this);
        dialogPersonalizado.setContentView(R.layout.recuperar_contra);
        // Podemos obtener los elementos dentro del layout ;)
        Button btnAlertaPasswordOk = dialogPersonalizado.findViewById(R.id.btnAlertaPasswordEnviar);
        btnAlertaPasswordOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailRec = dialogPersonalizado.findViewById(R.id.emailRecoverPass);
                String email = emailRec.getText().toString();
                if ( email.isEmpty() ) emailRec.setError("Ingresa un correo.");
                else {
                    mDialog.setMessage("Espere un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    mAuth.setLanguageCode("es");
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        Toast.makeText(Login.this, "Se ha enviado un correo para reestablecer tu contraseña", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Login.this, "No se pudo enviar el correo de reestablecimiento", Toast.LENGTH_SHORT).show();
                                    }
                                    mDialog.dismiss();
                                    dialogPersonalizado.dismiss();
                                }
                            });
                }
            }
        });
        Button btnAlertaPasswordCancel = dialogPersonalizado.findViewById(R.id.btnAlertaPasswordCancelar);
        btnAlertaPasswordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  dialogPersonalizado.dismiss();
            }
        });

        // Después mostrarla:
        dialogPersonalizado.show();
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

        mDialog.setMessage("Espere un momento...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
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
                                                mDialog.dismiss();
                                            } else {
                                                System.out.println("Error al recuperar");
                                                mDialog.dismiss();
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });
    }

}