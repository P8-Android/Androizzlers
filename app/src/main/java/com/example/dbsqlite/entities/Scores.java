package com.example.dbsqlite.entities;

public class Scores {

    private String puzzleName;
    private float timeToSolved;

    public String getPuzzleName() {
        return puzzleName;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    public float getTimeToSolved() {
        return timeToSolved;
    }

    public void setTimeToSolved(float timeToSolved) {
        this.timeToSolved = timeToSolved;
    }
}
