package com.example.zzler.puzzleGame;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.zzler.R;

public class TouchListener implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;

    static int countToShowFinishMsg = 0;
    static String timeTotal;

    boolean flagMusic = true;
    MediaPlayer mdDrag;
    MediaPlayer mdSuccess;

    public TouchListener(MediaPlayer mdDrag, MediaPlayer mdSuccess) {
        this.mdDrag = mdDrag;
        this.mdSuccess = mdSuccess;
    }

    public static String getTimeTotal() {
        return timeTotal;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        final double tolerance = sqrt(pow(view.getWidth(), 2) + pow(view.getHeight(), 2)) / 10;
        PuzzlePiece piece = (PuzzlePiece) view;


        if (!piece.canMove) {
            return true;
        }


        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                piece.bringToFront();
                break;
            case MotionEvent.ACTION_MOVE:
                //mdSuccess.pause();
                if(flagMusic)
                    playMusic(mdDrag);
                flagMusic=false;
                lParams.leftMargin = (int) (x - xDelta);
                lParams.topMargin = (int) (y - yDelta);
                view.setLayoutParams(lParams);
                break;
            case MotionEvent.ACTION_UP:
                if(!flagMusic&&mdDrag!=null)
                    mdDrag.pause();
                flagMusic = true;
                int xDiff = abs(piece.xCoord - lParams.leftMargin);
                int yDiff = abs(piece.yCoord - lParams.topMargin);
                if (xDiff <= tolerance && yDiff <= tolerance) {

                    lParams.leftMargin = piece.xCoord;
                    lParams.topMargin = piece.yCoord;
                    piece.setLayoutParams(lParams);
                    piece.canMove = false;
                    mdSuccess.start();
                    countToShowFinishMsg++;
                    sendViewToBack(piece);
                    if(countToShowFinishMsg == PuzzleGameView.dificulty*PuzzleGameView.dificulty){
                        countToShowFinishMsg = 0;
                        timeTotal = PuzzleGameView.resolved();

                    }


                }
                break;



        }
        return true;
    }

    private void playMusic(MediaPlayer mdDrag) {
        mdDrag.start();
        flagMusic = false;
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }

    }

}



