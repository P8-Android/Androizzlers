package com.example.zzler.score;

import android.widget.ImageView;

import java.util.ArrayList;

public class ScoreView  implements IScoreView{
    private String namePlayer;
    private String scorePuzzle;
    private Integer levelPuzzle;
    private ImageView imageViewStars;
    private ScorePresenterImpl scorePresenter;

    @Override
    public void showScore(ArrayList<Float> score) {

    }

}
