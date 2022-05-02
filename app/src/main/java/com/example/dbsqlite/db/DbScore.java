package com.example.dbsqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.dbsqlite.entities.Scores;

import java.util.ArrayList;

public class DbScore extends DbHelper {

    Context context;

    public DbScore(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertScore(String puzzleName, float timeToSolved) {

        long id = 0;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("puzzleName", puzzleName);
            values.put("timeToSolved", timeToSolved);

            id = db.insert(TABLE_NAME, null, values);
        } catch (Exception ex) {
            ex.toString();
        }
        return id;

    }

    public ArrayList<Scores> showScores() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Scores> listScores = new ArrayList<>();
        Scores score = null;
        Cursor cursorScore = null;

        cursorScore = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY timeToSolved ASC", null);

        if(cursorScore.moveToFirst()){
            do{
                score = new Scores();
                score.setPuzzleName(cursorScore.getString(0));
                score.setTimeToSolved(cursorScore.getFloat(1));
                listScores.add(score);
            } while (cursorScore.moveToNext());
        }

        cursorScore.close();

        return listScores;
    }
}
