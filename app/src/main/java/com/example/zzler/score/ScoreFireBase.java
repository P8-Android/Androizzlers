package com.example.zzler.score;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zzler.puzzleList.PuzzleListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ScoreFireBase {

    private Context context;
    private ArrayList<Score> arrayListScore;
    private ScoreListAdapter scoreListAdapter;
    private DatabaseReference storageReference;


    public ScoreFireBase(Context context) {

        this.context = context;

    }


    public void getDataFromFirebase() {

        storageReference = FirebaseDatabase.getInstance().getReference();

        Query query = storageReference.child("arrojorge-dev");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Score score = new Score();
                    score = dataSnapshot.getValue(Score.class);


                    arrayListScore.add(score);

                }
                scoreListAdapter = new ScoreListAdapter(context, arrayListScore);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void clearAll(){

        if (arrayListScore != null) {
            arrayListScore.clear();

            if (scoreListAdapter != null) {
                scoreListAdapter.notifyDataSetChanged();
            }

        }


        arrayListScore = new ArrayList<>();
    }

}
