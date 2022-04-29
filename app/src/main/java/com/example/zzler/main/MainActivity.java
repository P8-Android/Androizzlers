package com.example.zzler.main;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;
import com.example.zzler.webView.Info;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Puzzle");
        setSupportActionBar(myToolbar);

    }

    public void goGame (View v){
        Intent i = new Intent (this, PuzzleGameView.class);
        startActivity(i);
    }

    public void showInfo (View v){
        Intent i = new Intent (this, Info.class);
        startActivity(i);
    }
}