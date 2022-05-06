package com.example.zzler.score;


import java.io.Serializable;

public class Score implements Serializable {

    private String puzzleName;
    private Float scoreTime;

    public Score (){}

    public Score(String puzzleName, Float scoreTime) {
        this.puzzleName = puzzleName;
        this.scoreTime = scoreTime;

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
