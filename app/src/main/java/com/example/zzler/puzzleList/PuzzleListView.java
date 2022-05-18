package com.example.zzler.puzzleList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.AbsListView;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


public class PuzzleListView extends AppCompatActivity {

    private GridView gridView;
    ImageView imgV;
    FloatingActionButton cameraButton;
    FloatingActionButton galleryButton;



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

        cameraButton = findViewById(R.id.cameraButton);
        galleryButton = findViewById(R.id.galleryButton);
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

        galleryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, 9); //9 para diferenciar photo de camera de photo de galeria

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Intent in = new Intent(this, PuzzleGameView.class);
            in.setType("image/jpeg");
            in.putExtra("photo", imageBitmap);
            startActivity(in);
        }
        if (requestCode == 9 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            //URI imageBitmap = (URI) extras.get("photoGallery");

            Intent in = new Intent(this, PuzzleGameView.class);
            in.setType("image/*");
            in.putExtra("photoGallery", uri);
            startActivity(in);
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


        @Override
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

