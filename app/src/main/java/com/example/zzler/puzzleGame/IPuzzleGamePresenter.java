package com.example.zzler.puzzleGame;

import android.content.Context;
import android.widget.ImageView;

import com.example.zzler.score.Score;

public interface IPuzzleGamePresenter {

    void showNextPuzzle(ImageView img);
    void nextPuzzle(ImageView img);
    void saveScore(Score score);

}
