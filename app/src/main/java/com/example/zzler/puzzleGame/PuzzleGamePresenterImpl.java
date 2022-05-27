package com.example.zzler.puzzleGame;

import android.content.Context;

import android.widget.ImageView;
import com.example.zzler.score.Score;

import androidx.appcompat.app.AppCompatActivity;

public class PuzzleGamePresenterImpl implements IPuzzleGamePresenter {

    private PuzzleGameView gameView;
    private PuzzleGameModelImpl gameModel;


    @Override
    public void showNextPuzzle(ImageView img) {

    }

    @Override
    public void nextPuzzle(ImageView img) {

    }

    @Override
    public void saveScore (Score score) {
        gameModel = new PuzzleGameModelImpl();
        gameModel.saveScoreToBBDD(score);
        //gameModel = new PuzzleGameModelImpl(context);
        //SQLiteDatabase db = gameModel.getWritableDatabase(); //try catch
        //return gameModel.saveScoreToBBDD(puzzleName, timeToSolved);
    }

    //¿? SaveScoreToBBDD ()
}
