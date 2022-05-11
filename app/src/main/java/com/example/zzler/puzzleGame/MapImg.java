package com.example.zzler.puzzleGame;

import com.example.zzler.R;

import java.util.HashMap;

public class MapImg {
    HashMap<Integer,Boolean> mapImgToSplit;

    public MapImg(){
        mapImgToSplit=new HashMap<>();
        mapImgToSplit.put(R.drawable.img1,true);
        mapImgToSplit.put(R.drawable.img2,true);
        mapImgToSplit.put(R.drawable.img3,true);
        mapImgToSplit.put(R.drawable.img4,true);
        mapImgToSplit.put(R.drawable.img5,true);
        mapImgToSplit.put(R.drawable.img6,true);
        mapImgToSplit.put(R.drawable.img7,true);
    }
}
