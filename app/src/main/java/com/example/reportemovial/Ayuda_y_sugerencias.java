package com.example.reportemovial;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

}
