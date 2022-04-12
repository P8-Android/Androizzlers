package com.example.zzler.puzzleGame;

import android.widget.ImageView;

public interface IPuzzleGamePresenter {

    void showNextPuzzle(ImageView img);
    void nextPuzzle(ImageView img);
    void saveScore(Float timeGameSolved);

}
