package com.example.reportemovial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private TextView txtUser;
    private TextView txtPass;
    private Button btnOlvidar;
    private Button btnIniciar;
    private Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //login

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

    }



}