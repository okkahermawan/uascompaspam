package com.example.uascompas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_compas;

    MyDatabaseHelper myDB;
    ArrayList<String> id, time, catatan;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.hasil );
        add_compas = findViewById(R.id.add_compas);
        add_compas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, Compas.class);
                startActivity(intent);

            }
        });
        myDB = new MyDatabaseHelper(Menu.this);
        id = new ArrayList<>();
        time = new ArrayList<>();
        catatan = new ArrayList<>();

        storeDatainArrays();
        customAdapter = new CustomAdapter(Menu.this, id,time,catatan);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(Menu.this));
    }

    void storeDatainArrays(){
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Data Kosong", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                id.add(cursor.getString(0));
                time.add(cursor.getString(1));
                catatan.add(cursor.getString(2));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteall){
            Toast.makeText(this, "Delete All", Toast.LENGTH_SHORT).show();
            MyDatabaseHelper myDB = new MyDatabaseHelper(this);
            myDB.deleteAllData();
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}