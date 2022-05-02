package com.example.dbsqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dbsqlite.adapters.ListScoresAdapter;
import com.example.dbsqlite.db.DbScore;
import com.example.dbsqlite.entities.Scores;

import java.util.ArrayList;

public class ListDbActivity extends AppCompatActivity {

    RecyclerView listScores;
    ArrayList<Scores> listArrayScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_db);
        listScores = findViewById(R.id.ListDB);
        listScores.setLayoutManager(new LinearLayoutManager(this));

        DbScore dbScores = new DbScore(ListDbActivity.this);

        listArrayScores = new ArrayList<>();

        ListScoresAdapter adapter = new ListScoresAdapter(dbScores.showScores());
        listScores.setAdapter(adapter);

    }
}