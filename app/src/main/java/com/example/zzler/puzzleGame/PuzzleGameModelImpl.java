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

import com.example.zzler.score.Score;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PuzzleGameModelImpl  implements IPuzzleGameModel {

    private PuzzleGamePresenterImpl gamePresenter;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "zzlerDB.db";
    public static final String TABLE_NAME = "t_score";
    Context context;

    //prod3
    private DatabaseReference mDatabase;

 

    @Override
    public void NextPuzzle(ImageView img) {

    }

    @Override
    public long saveScoreToBBDD(Score score) {
        mDatabase = FirebaseDatabase.getInstance("https://p8-prod3-7852e-default-rtdb.firebaseio.com")
                .getReference();
        FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = userFirebase.getEmail();
        String user = userEmail.split("@")[0];
        if(user.contains("."))
            user = user.replace(".","-");
        //String user = "UserProbe";
        //save in firebase
        Map<String, Score> scoreMap = new HashMap<>();
        scoreMap.put(user, score);

        mDatabase.setValue(scoreMap);

        return 0;

    }


/*
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

 */


}
