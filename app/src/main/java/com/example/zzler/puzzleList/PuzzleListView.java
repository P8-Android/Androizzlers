package com.example.zzler.puzzleList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.zzler.R;
import java.io.IOException;
import java.io.InputStream;



public class PuzzleListView extends AppCompatActivity {

    private GridView gridView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_list);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        gridView  =  findViewById(R.id.grid_image_puzzle);
        ImageAdapter gridAdapter =(new ImageAdapter(this));
        gridView.setAdapter(gridAdapter);


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

        public Object getItem(int position) {
        return null;
        }

        public long getItemId(int position) {
        return 0;
        }



        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new GridView.LayoutParams(250, 400));
            } else {
                imageView = (ImageView) convertView;
            }
            try {
                InputStream ims = context.getAssets().open("img/" + list[position]);
                Bitmap bitmap = BitmapFactory.decodeStream(ims);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return imageView;
        }}





}

