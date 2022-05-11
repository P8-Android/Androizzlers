package com.example.zzler.puzzleGame;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.zzler.R;

public class AnimationPiece {

    Animation aPiece;


    public AnimationPiece(Context context){

        aPiece = AnimationUtils.loadAnimation(context, R.anim.piece_animator);

    }

}
