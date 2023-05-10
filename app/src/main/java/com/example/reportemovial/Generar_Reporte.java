package com.example.reportemovial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Generar_Reporte extends AppCompatActivity implements OnMapReadyCallback {

    private RadioButton tipo1;
    private EditText txtdesc;
    private Button btnObtUbi;
    private Button btnSelUbi;
    private Button btnTomarFoto;
    private Button btnCargarFoto;
    private Button btnEnviar;
    private ImageView foto;
    private LatLng point;

    private GoogleMap nMap;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "GenerarReporte";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_reporte);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        txtdesc = (EditText) findViewById(R.id.editDescGenerarR);
        btnObtUbi = (Button) findViewById(R.id.btnObtenerUbGenerarR);
        btnSelUbi = (Button) findViewById(R.id.btnSelUbGenerarR);
        btnCargarFoto = (Button) findViewById(R.id.btnCargarFGenerarR);
        btnTomarFoto = (Button) findViewById(R.id.btnTomarFGenerarR);
        btnEnviar = (Button) findViewById(R.id.btnEnviarReporteGenerarR);
        foto = (ImageView) findViewById(R.id.fotoGenerarR);

        btnCargarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/");
                startActivityForResult(i.createChooser(i, "Seleccione la aplicacion"), 10);
            }
        });

        btnObtUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                if (ActivityCompat.checkSelfPermission(Generar_Reporte.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Generar_Reporte.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    txtdesc.setText("No se han definido los permisos necesarios");
                    return;
                }
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(Generar_Reporte.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object
                                    point = new LatLng(location.getLatitude(), location.getLongitude());
                                    nMap.addMarker(new MarkerOptions().position(point).title("Ubicacion actual"));
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(point, 17);
                                    nMap.animateCamera(cameraUpdate);
                                }
                            }
                        });

            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> user = new HashMap<>();
                user.put("tipo", "Bache");
                user.put("descripcion", txtdesc.getText().toString());
                user.put("imagen", "ruta/imagen2/prueba");
                user.put("ubicacion", point.latitude);


                // Add a new document with a generated ID
                db.collection("reportes")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                db.collection("users_adm")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String desc = document.get("descripcion").toString();
                                    }
                                } else {
                                    System.out.println("Error al recuperar");
                                }
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK ){
            Uri path = data.getData();
            foto.setImageURI(path);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        nMap = googleMap;
    }

}