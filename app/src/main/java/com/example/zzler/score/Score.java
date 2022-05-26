package com.example.zzler.score;


import java.io.Serializable;
import java.util.Date;

public class Score implements Serializable {

    private String puzzleLevel;
    private float scoreTime;
    private Date date;

    public Score (){}

    public Score(String puzzleName, Float scoreTime, Date date) {
        this.puzzleLevel = puzzleName;
        this.scoreTime = scoreTime;
        this.date = date;

    }

    public Date getDate() {
        return date;
    }

    public void setDate (Date date) { this.date = date; }

    public String getPuzzleLevel() {
        return puzzleLevel;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleLevel = puzzleName;
    }

    public float getScoreTime() { return this.scoreTime;
    }

    public void setScoreTime(Float scoreTime) {
        this.scoreTime = scoreTime;
    }


    //prod 3


}
