package com.example.zzler.main;

import androidx.appcompat.app.AppCompatActivity;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.zzler.R;

public class Splash extends AppCompatActivity {


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable(){
            public void run(){
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            };
        }, 2567);
    }

}