package com.example.zzler.main;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;
import com.example.zzler.puzzleList.PuzzleListView;
import com.example.zzler.webView.Info;

public class MainActivity extends AppCompatActivity {




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

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Puzzle");
        setSupportActionBar(myToolbar);





    }





    public void goGame (View v){
        Intent i = new Intent (this, PuzzleListView.class);
        startActivity(i);
    }

    public void showInfo (View v){
        Intent i = new Intent (this, Info.class);
        startActivity(i);
    }
}