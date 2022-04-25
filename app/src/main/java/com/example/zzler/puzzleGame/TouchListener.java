package com.example.zzler.puzzleGame;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zzler.R;

import java.util.ArrayList;

public class TouchListener implements View.OnTouchListener {
    private float xDelta;
    private float yDelta;

    static Boolean  cantMove[] = new Boolean [PuzzleGameView.dificulty*PuzzleGameView.dificulty];
    int count = 0;
    static boolean flag = false;

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
                lParams.leftMargin = (int) (x - xDelta);
                lParams.topMargin = (int) (y - yDelta);
                view.setLayoutParams(lParams);
                break;
            case MotionEvent.ACTION_UP:
                int xDiff = abs(piece.xCoord - lParams.leftMargin);
                int yDiff = abs(piece.yCoord - lParams.topMargin);
                if (xDiff <= tolerance && yDiff <= tolerance) {
                    lParams.leftMargin = piece.xCoord;
                    lParams.topMargin = piece.yCoord;
                    piece.setLayoutParams(lParams);
                    piece.canMove = false;
                    count++;
                    sendViewToBack(piece);
                    if(count == PuzzleGameView.dificulty*PuzzleGameView.dificulty){
                        PuzzleGameView.resolved();
                    }


                }
                break;



        }



        return true;
    }

    public void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup)child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }

    }

    public static void puzzleFinish(){
        if(cantMove.length==3){
            PuzzleGameView.resolved();
        }
    }

    public boolean isfinish(){

        for (int i=0;i<cantMove.length;i++){
            if(cantMove[i] == null){
                return true;
            }else{
                PuzzleGameView.resolved();
                return true;
            }
        }
        return true;
    }
}



