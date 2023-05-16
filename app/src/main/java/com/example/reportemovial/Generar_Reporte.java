package com.example.reportemovial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Generar_Reporte extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String tipo;
    private RadioButton tipo1;
    private RadioButton tipo2;
    private RadioButton tipo3;
    private RadioButton tipo4;
    private EditText txtdesc;
    private Button btnObtUbi;
    private Button btnSelUbi;
    private Button btnTomarFoto;
    private Button btnCargarFoto;
    private Button btnEnviar;
    private ImageView foto;
    private LatLng point;
    private FusedLocationProviderClient ubicacion;

    private GoogleMap nMap;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    private static final String TAG = "GenerarReporte";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generar_reporte);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        txtdesc = (EditText) findViewById(R.id.editDescGenerarR);
        btnObtUbi = (Button) findViewById(R.id.btnObtenerUbGenerarR);
        btnSelUbi = (Button) findViewById(R.id.btnSelUbGenerarR);
        btnCargarFoto = (Button) findViewById(R.id.btnCargarFGenerarR);
        btnTomarFoto = (Button) findViewById(R.id.btnTomarFGenerarR);
        btnEnviar = (Button) findViewById(R.id.btnEnviarReporteGenerarR);
        foto = (ImageView) findViewById(R.id.fotoGenerarR);

        btnObtUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                obtenerUbicacion();
            }
        });

        btnSelUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nMap.clear();
                Toast.makeText(Generar_Reporte.this, "Manten presionado en el mapa para elegir ubicación", Toast.LENGTH_SHORT).show();
                nMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(@NonNull LatLng latLng) {
                        nMap.clear();
                        nMap.addMarker(new MarkerOptions().anchor(0.0f,1.0f).position(latLng));
                        point = latLng;
                    }
                });
            }
        });

        btnCargarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/");
                startActivityForResult(i.createChooser(i, "Seleccione la aplicacion"), 10);
            }
        });
        
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionRB();
                String img = enviarImagen();

                Map<String, Object> user = new HashMap<>();
                user.put("tipo", tipo);
                user.put("descripcion", txtdesc.getText().toString());
                user.put("imagen", img);
                user.put("ubicacion", point);
                user.put("estado", "Pendiente");


                // Add a new document with a generated ID
                db.collection("reportes")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Intent i = new Intent(v.getContext(), FeedCiudadano.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                                Toast.makeText(Generar_Reporte.this, "No se ha podido guardar el reporte.", Toast.LENGTH_SHORT).show();
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        nMap = googleMap;
    }

    public void seleccionRB(){
        tipo1 = (RadioButton) findViewById(R.id.typeVialGR);
        tipo2 = (RadioButton) findViewById(R.id.typeAguaGR);
        tipo3 = (RadioButton) findViewById(R.id.typeAlumbradoGR);
        tipo4 = (RadioButton) findViewById(R.id.typeAlumbradoGR);

        if(tipo1.isChecked())
            tipo = tipo1.getText().toString();
        if(tipo2.isChecked())
            tipo = tipo2.getText().toString();
        if(tipo3.isChecked())
            tipo = tipo3.getText().toString();
        if(tipo4.isChecked())
            tipo = tipo4.getText().toString();
    }

    private void obtenerUbicacion(){
        if(ContextCompat.checkSelfPermission(Generar_Reporte.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(this, "Tenemos permiso", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(Generar_Reporte.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        ubicacion = LocationServices.getFusedLocationProviderClient(this);
        ubicacion.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    point = new LatLng(location.getLatitude(), location.getLongitude());
                    nMap.addMarker(new MarkerOptions().position(point).title("Ubicación actual"));
                    nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                } else {
                    Toast.makeText(Generar_Reporte.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un carácter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena+".jpg";
    }

    private int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }

    private String enviarImagen(){
        String cadAux = cadenaAleatoria(40);
        StorageReference archivoRef = storageRef.child("images/"+cadAux);

        foto.setDrawingCacheEnabled(true);
        foto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) foto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = archivoRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Generar_Reporte.this, "No se ha podido guardar la imagen.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

        return cadAux;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}