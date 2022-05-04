package com.example.zzler.puzzleGame;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.zzler.R;

public class MusicManager {
    MediaPlayer mdDrag;
    MediaPlayer mdSuccess;

    public MusicManager(Context c){
        mdDrag = MediaPlayer.create(c, R.raw.drag1);
        mdSuccess = MediaPlayer.create(c,R.raw.success2);
    }
}
