package com.example.zzler.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.zzler.R;
import com.example.zzler.puzzleList.PuzzleListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Button buttonListPuzzle = findViewById(R.id.btnGame);
        buttonListPuzzle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intentPuzzleList = new Intent(MainActivity.this, PuzzleListView.class);
                startActivity(intentPuzzleList);

            }



        });


    }
}