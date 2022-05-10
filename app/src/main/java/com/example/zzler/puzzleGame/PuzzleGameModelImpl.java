package com.example.zzler.puzzleGame;


import android.content.ContentValues;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.widget.ImageView;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.Locale;


public class PuzzleGameModelImpl extends SQLiteOpenHelper implements IPuzzleGameModel {

    private PuzzleGamePresenterImpl gamePresenter;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "zzlerDB.db";
    public static final String TABLE_NAME = "t_score";
    Context context;

    public PuzzleGameModelImpl(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION );
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "puzzleName TEXT NOT NULL," +
                "timeToSolved REAL NOT NULL," +
                "fecha TEXT NOT NULL" + ")"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }



    @Override
    public void NextPuzzle(ImageView img) {

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long saveScoreToBBDD(String puzzleName, float timeToSolved) {
        long id = 0;
        SimpleDateFormat dateFormmat = new SimpleDateFormat(

                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        String dateString = dateFormmat.format(date);

        try {
            PuzzleGameModelImpl dbHelper = new PuzzleGameModelImpl(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("puzzleName", puzzleName);
            values.put("timeToSolved", timeToSolved);
            values.put("fecha", dateString);

            id = db.insert(TABLE_NAME, null, values);
        } catch (Exception ex) {
            ex.toString();
        }
        return id;
    }


}
