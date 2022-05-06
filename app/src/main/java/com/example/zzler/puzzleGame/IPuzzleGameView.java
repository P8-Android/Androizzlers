package com.example.zzler.puzzleGame;

import android.widget.ImageView;

public interface IPuzzleGameView {

    void showNextPuzzle(ImageView img);
    float saveScore(String puzzleName, float timeToSolved);
    void saveScoreInCalendar (String puzzleName, float timeToSolved);

}
