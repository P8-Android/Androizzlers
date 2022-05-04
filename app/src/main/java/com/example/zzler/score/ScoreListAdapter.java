package com.example.zzler.score;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zzler.R;
import java.util.ArrayList;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {

    private ArrayList<ScoreView> scoreArrayList;


    public ScoreListAdapter(ArrayList<ScoreView> scoreArrayList){

        this.scoreArrayList = scoreArrayList;

    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        TextView puzzleName, scoreTime;


        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            puzzleName = itemView.findViewById(R.id.puzzle_name);
            scoreTime = itemView.findViewById(R.id.text_time_score);


        }
    }


    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle_score, null);

        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {

        holder.puzzleName.setText(scoreArrayList.get(position).getPuzzleName());
        holder.scoreTime.setText((int) scoreArrayList.get(position).getScoreTime());
    }


    @Override
    public int getItemCount() {
        return scoreArrayList.size();
    }


}
