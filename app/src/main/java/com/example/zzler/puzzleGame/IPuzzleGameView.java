package com.example.zzler.puzzleGame;

import android.widget.ImageView;

public interface IPuzzleGameView {

    void showNextPuzzle(ImageView img);
    void saveScore(Float timeGameSolved);

}
