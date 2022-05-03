package com.example.zzler.puzzleList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;



public class PuzzleListView extends AppCompatActivity {

    private GridView gridView;
    ImageView imgV;
    FloatingActionButton cameraButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_list);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Intent i = new Intent(this, PuzzleGameView.class);

        cameraButton = findViewById(R.id.cameraButton);

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

        cameraButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final int REQUEST_IMAGE_CAPTURE = 2;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            imgV.setImageBitmap(imageBitmap);


           // imageView.setImageBitmap(imageBitmap);
        }
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

