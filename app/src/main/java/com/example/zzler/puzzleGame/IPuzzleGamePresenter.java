package com.example.zzler.puzzleGame;

import android.content.Context;
import android.widget.ImageView;

public interface IPuzzleGamePresenter {

    void showNextPuzzle(ImageView img);
    void nextPuzzle(ImageView img);
    long saveScore(String puzzleName, float timeToSolved, Context context);

    }
