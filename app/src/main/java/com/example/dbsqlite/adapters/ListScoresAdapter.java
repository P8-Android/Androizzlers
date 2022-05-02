package com.example.dbsqlite.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dbsqlite.ListDbActivity;
import com.example.dbsqlite.R;
import com.example.dbsqlite.entities.Scores;

import java.util.ArrayList;

public class ListScoresAdapter extends RecyclerView.Adapter<ListScoresAdapter.ScoreViewHolder> {

    ArrayList<Scores> listScores;

    public ListScoresAdapter(ArrayList<Scores> listScores){
        this.listScores = listScores;
    }


    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_db, null, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        holder.viewPuzzleName.setText(listScores.get(position).getPuzzleName());
        // cast int parameter
        holder.viewTimeToSolved.setText((int) listScores.get(position).getTimeToSolved());
    }

    @Override
    public int getItemCount() {
        return listScores.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        TextView viewPuzzleName, viewTimeToSolved;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            viewPuzzleName = itemView.findViewById(R.id.viewPuzzleName);
            viewTimeToSolved = itemView.findViewById(R.id.viewTimeToSolved);
        }
    }
}
