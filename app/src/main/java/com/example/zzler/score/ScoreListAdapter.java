package com.example.zzler.score;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zzler.R;
import java.util.ArrayList;

public class ScoreListAdapter extends RecyclerView.Adapter<ScoreListAdapter.ScoreViewHolder> {

    private ArrayList<Score> scoreArrayList;
    private Context context;


    public ScoreListAdapter(Context context, ArrayList<Score> scoreArrayList) {
        this.scoreArrayList = scoreArrayList;
        this.context = context;
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        TextView puzzleName, scoreTime;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            puzzleName = (TextView) itemView.findViewById(R.id.puzzle_name);
            scoreTime = (TextView) itemView.findViewById(R.id.text_time_score);

        }
    }


    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_puzzle_score, null);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {

        Score score = scoreArrayList.get(position);

        holder.puzzleName.setText(String.valueOf(score.getPuzzleName()));
        holder.scoreTime.setText(String.valueOf(score.getScoreTime()));
    }


    @Override
    public int getItemCount() {
        return scoreArrayList.size();
    }


}
