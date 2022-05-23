package com.example.zzler.puzzleGame;

import android.widget.ImageView;

import com.example.zzler.score.Score;

public interface IPuzzleGameModel {

    void NextPuzzle(ImageView img);
    long saveScoreToBBDD(Score score);

}
