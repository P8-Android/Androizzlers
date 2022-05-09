package com.example.zzler.score;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zzler.R;
import java.util.ArrayList;

import android.widget.ImageView;

public class ScoreView extends AppCompatActivity {

    private ArrayList<ScoreView> scoreList;
    private RecyclerView recyclerViewScore;
    DbHelper dbHelper = new DbHelper(ScoreView.this);


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_score);

        Toolbar toolbarGameScore = findViewById(R.id.toolbar_game_score);
        toolbarGameScore.setTitle("Game Score");
        setSupportActionBar(toolbarGameScore);

        recyclerViewScore = (RecyclerView) findViewById(R.id.recycler_list_score);
        recyclerViewScore.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewScore.setHasFixedSize(true);

        ScoreListAdapter adapter = new ScoreListAdapter(ScoreView.this, dbHelper.getScoreFromBBDD());
        recyclerViewScore.setAdapter(adapter);



    }

}

