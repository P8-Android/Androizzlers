package com.example.zzler.puzzleList;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;
import com.example.zzler.score.ScoreListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PuzzleListView extends AppCompatActivity {

    private GridView gridView;
    ImageView imgV;
    FloatingActionButton cameraButton;
    FloatingActionButton galleryButton;
    ArrayList<String> imagesPuzzles;
    private ImageAdapter imageAdapter;
    StorageReference storageReference;

    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar progressBar;

    private DatabaseReference mDatabaseRef;
    private List<ItemImage> itemImages;

   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_list);

        Toolbar toolbarGameList = findViewById(R.id.toolbar_game_list);
        toolbarGameList.setTitle(this.getResources().getString(R.string.puzzleList)); //pasamos el texto desde strings.xml para internacionalización
        setSupportActionBar(toolbarGameList);

        Intent i = new Intent(this, PuzzleGameView.class);
        imgV = null;

        mRecyclerView=findViewById(R.id.recycler_list_puzzle);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(PuzzleListView.this));

        itemImages =new ArrayList<>();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("images");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    ItemImage itemimg = postSnapshot.getValue(ItemImage.class);
                    itemImages.add(itemimg);
                }
                mAdapter = new ImageAdapter(PuzzleListView.this, itemImages);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        cameraButton = findViewById(R.id.cameraButton);
        galleryButton = findViewById(R.id.galleryButton);






      /*  gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //View imgView = gridAdapter.getItem();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", pos);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });*/

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int REQUEST_IMAGE_CAPTURE = 2;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }

            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
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

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

        private Context context;
        private List<ItemImage> imagesItems;

        public ImageAdapter(Context context, List<ItemImage> imagesItems) {
            this.context = context;
            this.imagesItems = imagesItems;

        }


        public class ImageViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = findViewById(R.id.image_grid);
            }
        }


        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v=LayoutInflater.from(context).inflate(R.layout.activity_puzzle_list, parent,false);
            return  new ImageViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            ItemImage item = imagesItems.get(position);
            Picasso.with(context).
                    load(item.getImgUrl()).
                    into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return imagesItems.size();
        }


    }




}






