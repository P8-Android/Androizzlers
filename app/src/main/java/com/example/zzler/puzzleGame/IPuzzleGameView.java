package com.example.zzler.puzzleGame;

import android.widget.ImageView;

import java.util.Date;

public interface IPuzzleGameView {

    void showNextPuzzle(ImageView img);
    void saveScore(String puzzleName, float timeToSolved, Date date);


}
