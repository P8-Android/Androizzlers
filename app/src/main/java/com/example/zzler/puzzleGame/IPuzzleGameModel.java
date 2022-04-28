package com.example.zzler.puzzleGame;

import android.widget.ImageView;

public interface IPuzzleGameModel {

    void NextPuzzle(ImageView img);
    long saveScoreToBBDD(String puzzleName, float timeToSolved);

}
