package com.example.zzler.score;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "zzlerBD.db";
    public static final String TABLE_NAME = "t_score";
    private Context context;

    public DbHelper (Context context){
        super(context, DATABASE_NAME, null,1);
        this.context = context;
    }

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }


    public ArrayList<ScoreView> getScoreFromBBDD () {

        DbHelper dbHelper = new DbHelper(context, DATABASE_NAME, null,1);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<ScoreView> scoreList = new ArrayList<>();

        ScoreView scoreView = null;
        Cursor cursor = null;


        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {

            do {
                scoreView.setPuzzleName(cursor.getString(1));
                scoreView.setScoreTime(cursor.getFloat(2));
                scoreList.add(scoreView);

            }while (cursor.moveToNext());

        }

        cursor.close();
        dbHelper.close();

        return scoreList;

    }

}

