package com.example.zzler.score;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zzler.R;
import java.util.ArrayList;


public class ScoreView extends AppCompatActivity {

    private ArrayList<ScoreView> scoreList;
    private RecyclerView recyclerViewScore;
    private SQLiteOpenHelper conn;
    private DbHelper dbHelper;
    public int id;
    private String puzzleName;
    private float scoreTime;


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "zzlerDB.db";
    public static final String TABLE_NAME = "t_score";


    public ScoreView() {

    }

    public ScoreView(String puzzleName, float scoreTime) {
        this.puzzleName = puzzleName;
        this.scoreTime = scoreTime;

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_score);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        recyclerViewScore = (RecyclerView) findViewById(R.id.recycler_list_score);
        recyclerViewScore.setLayoutManager(new LinearLayoutManager(this));


        DbHelper dbScoreView = new DbHelper(ScoreView.this, DATABASE_NAME, null,1);
        scoreList = new ArrayList<>();


        ScoreListAdapter adapter = new ScoreListAdapter(dbScoreView.getScoreFromBBDD());
        recyclerViewScore.setAdapter(adapter);



    }


    public String getPuzzleName() {
        return puzzleName;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    public float getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(float scoreTime) {
        this.scoreTime = scoreTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

