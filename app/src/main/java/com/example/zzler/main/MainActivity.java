package com.example.zzler.main;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;
import com.example.zzler.puzzleList.PuzzleListView;
import com.example.zzler.score.ScoreView;
import com.example.zzler.webView.Info;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity{

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.showInfo:
                openWebView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openWebView() {
        Intent i = new Intent(this, Info.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbarMain = findViewById(R.id.toolbar_main);
        toolbarMain.setTitle("Puzzle Game");
        setSupportActionBar(toolbarMain);

    }



    public void goGame (View v){
        Intent i = new Intent (this, PuzzleListView.class);
        startActivity(i);
    }

    public void showInfo (View v){
        Intent i = new Intent (this, Info.class);
        startActivity(i);
    }


    public void puzzleScore (View v) {
        Intent i = new Intent (this, ScoreView.class);
        startActivity(i);

    }
}
