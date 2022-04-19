package com.example.zzler.puzzleGame;

import static com.example.zzler.puzzleGame.PuzzleGameModelImpl.*;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.example.zzler.R;

import java.util.ArrayList;

public class PuzzleGameView extends AppCompatActivity implements IPuzzleGameView {

    private Float timeGameSolved;
    private PuzzleGamePresenterImpl gamePresenter;

    ArrayList<Bitmap> pieces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game);

        final RelativeLayout layout = findViewById(R.id.layout);
        ImageView imageView = findViewById(R.id.imageView);
        // Handler handler = new Handler();
        // run image related code after the view was laid out
        // to have all dimensions calculated

        imageView.post(new Runnable() {
            @Override
            public void run() {
                pieces = splitImage(imageView);
                TouchListener touchListener = new TouchListener();
                for(Bitmap piece : pieces) {
                    ImageView iv = new ImageView(getApplicationContext());
                    iv.setImageBitmap(piece);
                    iv.setOnTouchListener(touchListener);
                    layout.addView(iv);
                }
            }
        });
    }



    @Override
    public void showNextPuzzle(ImageView img) {

    }

    @Override
    public void saveScore(Float timeGameSolved) {

    }

    public void splitPuzzleImage (ImageView img){

    }

    public boolean isWinner (){
        return false;
    }




}
