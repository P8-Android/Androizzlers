package com.example.dbsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dbsqlite.db.DbHelper;
import com.example.dbsqlite.db.DbScore;

public class MainActivity extends AppCompatActivity {

    Button btnCreateDB;
    Button btnInsertDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCreateDB = findViewById((R.id.btnCreateDB));
        btnInsertDB = findViewById((R.id.btnInsertDB));

        btnCreateDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if(db != null){
                    Toast.makeText(MainActivity.this, "DB created!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error when creating DB", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnInsertDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbScore dbScore = new DbScore(MainActivity.this);
                //Enlazar con variable 'Puzzle #' y 'tiempo resolución'
                long id = dbScore.insertScore("Puzzle #2", 85);

                if(id > 0){
                    Toast.makeText(MainActivity.this, "Values inserted!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error when inserting values", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}