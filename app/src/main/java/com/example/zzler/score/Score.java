package com.example.zzler.score;


import java.io.Serializable;
import java.util.Date;

public class Score implements Serializable {

    private String puzzleLevel;
    private Float scoreTime;
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

    public String getPuzzleLevel() {
        return puzzleLevel;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleLevel = puzzleName;
    }

    public Float getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(Float scoreTime) {
        this.scoreTime = scoreTime;
    }
}
