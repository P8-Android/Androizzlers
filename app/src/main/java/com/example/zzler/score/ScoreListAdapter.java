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



    public ScoreListAdapter(Context context, ArrayList<Score> arrayListScore) {
        this.scoreArrayList = arrayListScore;
        this.context = context;
    }



    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        TextView puzzleLevel, scoreTime, textTime;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);

            puzzleLevel = (TextView) itemView.findViewById(R.id.puzzle_lvl);
            scoreTime = (TextView) itemView.findViewById(R.id.text_time_score);
            textTime = (TextView)  itemView.findViewById(R.id.text_time);
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

        holder.puzzleLevel.setText(scoreArrayList.get(position).getPuzzleLevel());
        holder.scoreTime.setText((int) scoreArrayList.get(position).getScoreTime());
        holder.textTime.setText((CharSequence) scoreArrayList.get(position).getDate());
    }


    @Override
    public int getItemCount() {
        return scoreArrayList.size();
    }


}
