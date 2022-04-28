package com.example.dbsqlite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

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

}
