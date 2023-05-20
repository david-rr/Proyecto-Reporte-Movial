package com.example.reportemovial;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.Query;

public class SugerenciaLayout extends AppCompatActivity {

    RecyclerView mRecycler;
    Adapter adapter;
    FirebaseFirestore mfirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sugerencias);

        mfirestore = FirebaseFirestore.getInstance();
        mRecycler= findViewById(R.id.Recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        Query query = mfirestore.collection("sugerencia");

        FirestoreRecyclerOptions<Sugerencia> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Sugerencia>().setQuery(query, Sugerencia.class).build();

        adapter = new Adapter(firestoreRecyclerOptions);
        adapter.notifyDataSetChanged();
        mRecycler.setAdapter(adapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
