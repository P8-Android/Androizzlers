package com.example.zzler.puzzleGame;

import android.widget.ImageView;

public interface IPuzzleGameModel {

    void NextPuzzle(ImageView img);
    void saveScoreToBBDD(Float timeToSolved);

}
