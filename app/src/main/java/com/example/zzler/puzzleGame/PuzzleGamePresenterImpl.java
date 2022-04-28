package com.example.zzler.puzzleGame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;

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
    public long saveScore (String puzzleName, float timeToSolved, Context context) {
        gameModel = new PuzzleGameModelImpl(context);
        SQLiteDatabase db = gameModel.getWritableDatabase(); //try catch
        return gameModel.saveScoreToBBDD(puzzleName, timeToSolved);
    }

    //¿? SaveScoreToBBDD ()
}
