package com.example.zzler.puzzleList;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class PuzzleListView extends AppCompatActivity {

    private GridView gridView;
    ImageView imgV;
    FloatingActionButton cameraButton;
    FloatingActionButton galleryButton;
    ArrayList<String> imagesPuzzles = new ArrayList<String>();
    private ImageAdapter imageAdapter;
    DatabaseReference databaseReference;




    //STORAGE
    FirebaseStorage storage = FirebaseStorage.getInstance();

    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();

    // Create a reference with an initial file path and name
    StorageReference pathReference = storageRef.child("images/");

    // Create a reference to a file from a Google Cloud Storage URI
    /*
    StorageReference gsReference0 = storage.getReferenceFromUrl("gs://bucket/images/doctor_strange.jpg");
    StorageReference gsReference1 = storage.getReferenceFromUrl("gs://bucket/images/gandalf.jpg");
    StorageReference gsReference2 = storage.getReferenceFromUrl("gs://bucket/gandalf_vs_demond.jpg");
    StorageReference gsReference3 = storage.getReferenceFromUrl("gs://bucket/images/groot.jpg");
    StorageReference gsReference4 = storage.getReferenceFromUrl("gs://bucket/images/hobbit_house.jpg");
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
        gridView = findViewById(R.id.grid_image_puzzle);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        getAllImages();

        ImageAdapter gridAdapter = (new ImageAdapter(this, imagesPuzzles));
        cameraButton = findViewById(R.id.cameraButton);
        galleryButton = findViewById(R.id.galleryButton);

        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                //View imgView = gridAdapter.getItem();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", pos);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });

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

        ///DESCARGA POR PATH
        /*
        storageRef.child("/images/doctor_strange.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
        });

 */

        //CREAR UN ARCHIVO TEMPORAL
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

        ArrayList<String> imagesPuzzleArrayList;
        private Context context;

        public ImageAdapter(Context c, ArrayList<String> imagesPuzzleArrayList) {

            this.context = c;
            this.imagesPuzzleArrayList = imagesPuzzleArrayList;
        }

        public int getCount() {
            return this.imagesPuzzleArrayList.size();
        }


        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.activity_puzzle_list, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_grid);
            //Picasso.with(getApplicationContext()).load(imagesPuzzleArrayList.get(position)).into(imageView);
            return view;
        }
    }

    public void getAllImages() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    //imagesPuzzles.add(ds.child("/images").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}


