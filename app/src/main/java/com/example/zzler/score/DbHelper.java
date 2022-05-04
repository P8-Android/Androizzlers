package com.example.zzler.score;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "zzlerDB.db";
    public static final String TABLE_NAME = "t_score";
    private Context context;
    private ScoreView scoreView;

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


    public ArrayList<Score> getScoreFromBBDD () {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Score> scoreList = new ArrayList<Score>();
        Cursor cursor = null;


        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToPosition(1)) {
            do {
                scoreList.add(new Score(cursor.getString(1),cursor.getFloat(2)));
            }while (cursor.moveToNext());

        }

        cursor.close();
        db.close();

        return scoreList;

    }

}

