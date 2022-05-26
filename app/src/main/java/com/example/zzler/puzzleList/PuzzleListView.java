package com.example.zzler.puzzleList;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PuzzleListView extends AppCompatActivity {


    ImageView imgV;
    private ImageAdapter imageAdapter;
    StorageReference storageReference;
    Context context;
    private RecyclerView recyclerView;
    private StorageReference mDatabaseRef;
    private List<ItemImage> itemImages;




    private GridView gridView;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;


    StorageReference imagesRef = FirebaseStorage.getInstance().getReference();




    //STORAGE
    FirebaseStorage storage = FirebaseStorage.getInstance();

    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();

    // Create a reference with an initial file path and name
    StorageReference pathReference = storageRef.child("images/");

    // Create a reference to a file from a Google Cloud Storage URI

    StorageReference gsReference0 = storage.getReferenceFromUrl("gs://p8-prod3-7852e.appspot.com/images/doctor_strange.jpg");

    StorageReference gsReference1 = storage.getReferenceFromUrl("gs://p8-prod3-7852e.appspot.com/images/gandalf.jpg");

    StorageReference gsReference2 = storage.getReferenceFromUrl("gs://p8-prod3-7852e.appspot.com/images/gandalf_vs_demond.jpg");
    StorageReference gsReference3 = storage.getReferenceFromUrl("gs://p8-prod3-7852e.appspot.com/images/groot.jpg");
    StorageReference gsReference4 = storage.getReferenceFromUrl("gs://p8-prod3-7852e.appspot.com/images/hobbit_house.jpg");
    /*
    StorageReference gsReference5 = storage.getReferenceFromUrl("gs://bucket/images/hulk.jpg");
    StorageReference gsReference6 = storage.getReferenceFromUrl("gs://bucket/images/star_lord.jpg");

     */


    // Create a reference from an HTTPS URL
    // Note that in the URL, characters are URL escaped!
    //StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_list);

        Toolbar toolbarGameList = findViewById(R.id.toolbar_game_list);
        toolbarGameList.setTitle(this.getResources().getString(R.string.puzzleList)); //pasamos el texto desde strings.xml para internacionalización
        setSupportActionBar(toolbarGameList);

        Intent i = new Intent(this, PuzzleGameView.class);
        imgV = null;

        //itemImages = new ArrayList<>();


        //recyclerView = findViewById(R.id.recycler_list_puzzle);

        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setHasFixedSize(true);

//        gridView = findViewById(R.id.gridList);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);

        mDatabaseRef = FirebaseStorage.getInstance().getReference();

        Glide.with(this).load(gsReference0)
               .into(img1);

        Glide.with(this).load(gsReference1)
                .into(img2);

        Glide.with(this).load(gsReference2)
                .into(img3);

        Glide.with(this).load(gsReference3)
                .into(img4);

        Glide.with(this).load(gsReference4)
                .into(img5);


        //getDataFromFirebase();






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



        ///DESCARGA POR PATH

     /*   storageRef.child("/images/doctor_strange.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                Log.i("STORAGEEEE", "onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("STORAGEEEE", "onFailure");
            }
        });*/



       /* //CREAR UN ARCHIVO TEMPORAL
        StorageReference doctorStrange = storageRef.child("images/doctor_strange.jpg");

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        doctorStrange.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Log.i("STORAGEEEE", "onSuccess");

                //MANEJAR EL ARCHIVO TEMPORAL PARA INCLUIRLO EN EL GRID
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("STORAGEEEE", "onFailure");
            }
        });*/






    }

    private void getDataFromFirebase() {
        ImageAdapter imgAdapter = new ImageAdapter(context);
        recyclerView.setAdapter(imgAdapter);

        StorageReference query = mDatabaseRef.child("/images");

        Log.i("NAME_FIREBASE",query.getBucket());
                /*
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ItemImage itemImage = new ItemImage();
                    itemImage.setImgUrl(dataSnapshot.child("image").getValue().toString());
                    itemImages.add(itemImage);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       */

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
        //private List<ItemImage> imagesUrls;

        public ImageAdapter(Context context) {
            this.context = context;


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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_puzzle_list, parent, false);

            return new ImageViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

            Glide.with(context).load(gsReference0)
                    .into(holder.imageView);

        }

        @Override
        public int getItemCount() {
            return 0;
        }


    }




}








