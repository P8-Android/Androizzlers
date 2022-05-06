package com.example.zzler.score;


import java.io.Serializable;

public class Score implements Serializable {

    private String puzzleName;
    private Float scoreTime;
    private String date;

    public Score (){}

    public Score(String puzzleName, Float scoreTime, String date) {
        this.puzzleName = puzzleName;
        this.scoreTime = scoreTime;
        this.date = date;

    }

    public String getDate() {
        return date;
    }

    public String getPuzzleName() {
        return puzzleName;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    public Float getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(Float scoreTime) {
        this.scoreTime = scoreTime;
    }
}
