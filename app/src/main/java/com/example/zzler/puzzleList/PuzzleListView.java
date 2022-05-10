package com.example.zzler.puzzleList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;

import java.io.IOException;
import java.io.InputStream;



public class PuzzleListView extends AppCompatActivity {

    private GridView gridView;
    ImageView imgV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_list);

        Toolbar toolbarGameList = findViewById(R.id.toolbar_game_list);
        toolbarGameList.setTitle("Puzzle List");
        setSupportActionBar(toolbarGameList);

        Intent i = new Intent(this, PuzzleGameView.class);
        imgV = null;
        gridView  =  findViewById(R.id.grid_image_puzzle);
        ImageAdapter gridAdapter =(new ImageAdapter(this));

        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                View imgView = gridAdapter.getItem();
                Bundle bundle = new Bundle();
                bundle.putInt("pos",pos);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });


    }


    public class ImageAdapter extends BaseAdapter {

    private Context context;
    private String[] list;

    public ImageAdapter(Context c) {
        context = c;
        try {
            list = context.getAssets().list("img");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public int getCount() {
        return list.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        public View getItem() {
            return imgV;
        }

        public long getItemId(int position) {
        return 0;
        }



        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                imgV = new ImageView(context);
                imgV.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imgV.setLayoutParams(new GridView.LayoutParams(250, 400));
            } else {
                imgV = (ImageView) convertView;
            }
            try {
                InputStream ims = context.getAssets().open("img/" + list[position]);
                Bitmap bitmap = BitmapFactory.decodeStream(ims);
                imgV.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imgV;
        }}





}

