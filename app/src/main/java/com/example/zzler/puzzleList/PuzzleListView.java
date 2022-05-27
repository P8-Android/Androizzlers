package com.example.zzler.puzzleList;

import android.Manifest;

import android.content.ClipData;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;

import android.os.Environment;
import android.os.Parcelable;
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
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.data.BufferedOutputStream;
import com.bumptech.glide.module.AppGlideModule;

import com.example.zzler.R;
import com.example.zzler.puzzleGame.PuzzleGameView;

import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.widget.AbsListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PuzzleListView extends AppCompatActivity {


    ImageView imgV;
    //private ImageAdapter imageAdapter;
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
    private ImageView img6;
    private ImageView img7;


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
    StorageReference gsReference5 = storage.getReferenceFromUrl("gs://p8-prod3-7852e.appspot.com/images/hulk.jpg");
    StorageReference gsReference6 = storage.getReferenceFromUrl("gs://p8-prod3-7852e.appspot.com/images/star_lord.jpg");




    // Create a reference from an HTTPS URL
    // Note that in the URL, characters are URL escaped!
    //StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");

    ArrayList<StorageReference> remoteFireImg;
    static  ArrayList<File> files;


    private static final int PERMISSION_CAMERA = 0;


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
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);

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

        Glide.with(this).load(gsReference5)
                .into(img6);

        Glide.with(this).load(gsReference6)
                .into(img7);

        Intent in = new Intent(this, PuzzleGameView.class);
        //getDataFromFirebase();

        img1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                in.putExtra("pos",1);
                startActivity(in);
            }
        });

        img2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                in.putExtra("pos",2);
                startActivity(in);
            }
        });

        img3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                in.putExtra("pos",3);
                startActivity(in);
            }
        });

        img4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                in.putExtra("pos",4);
                startActivity(in);
            }
        });

        img5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                in.putExtra("pos",5);
                startActivity(in);
            }
        });

        img6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                in.putExtra("pos",6);
                startActivity(in);
            }
        });

        img7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                in.putExtra("pos",7);
                startActivity(in);
            }
        });




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


        ArrayList<String> namesJpg = new ArrayList<>();
        namesJpg.add("doctor_strange.jpg");
        namesJpg.add("gandalf.jpg");
        namesJpg.add("gandalf_vs_demond.jpg");
        namesJpg.add("groot.jpg");
        namesJpg.add("hobbit_house.jpg");
        namesJpg.add("hulk.jpg");
        namesJpg.add("star_lord.jpg");




        StorageReference refImg1 = storageRef.child("images/doctor_strange.jpg");
        StorageReference refImg2 = storageRef.child("images/gandalf.jpg");
        StorageReference refImg3 = storageRef.child("images/gandalf_vs_demond.jpg");
        StorageReference refImg4 = storageRef.child("images/groot.jpg");
        StorageReference refImg5 = storageRef.child("images/hobbit_house.jpg");
        StorageReference refImg6 = storageRef.child("images/hulk.jpg");
        StorageReference refImg7 = storageRef.child("images/star_lord.jpg");


  /*      files = new ArrayList<>();
        File outputFile = null;
        int b = 1;
        //File outputDir = this.getCacheDir(); // context being the Activity pointer
        try {
            files.add(File.createTempFile("images"+b, "jpg", getFilesDir()));
            files.add(new File(getFilesDir(), "images"+b));
            Log.i("FILEEEE_toString", files.get(1).getPath());
            b++;
        } catch (IOException e) {
            e.printStackTrace();
        }

   */

        fireImg1 = new File(getFilesDir(), "images1");
        fireImg2 = new File(getFilesDir(), "images2");
        fireImg3 = new File(getFilesDir(), "images3");
        fireImg4 = new File(getFilesDir(), "images4");
        fireImg5 = new File(getFilesDir(), "images5");
        fireImg6 = new File(getFilesDir(), "images6");
        fireImg7 = new File(getFilesDir(), "images7");
        Log.i("FILEEEE", fireImg2.toString());
        //File localFile = new File(this.getFilesDir(), "img1");
        //File fireImg1 = new File(context.getCacheDir(), "img1");
        //Log.i("FILEEEE", fireImg1.toString());


//        File file = new File(context.getCacheDir(), "img1");
        //Log.i("FILEEEE", files.get(1).toString());


        refImg1.getFile(fireImg1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                Log.i("FILEEE","exito");
                //Aqui ya obtuviste el archivo, asi que puedes chequear su tamaño con un log

                Log.i("Tamanio",""+taskSnapshot); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("FILEEE","failure"); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        });

        // Segunda Imagen DownLoad
        refImg2.getFile(fireImg2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override

            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                Log.i("FILEEE","exito");
                //Aqui ya obtuviste el archivo, asi que puedes chequear su tamaño con un log


                Log.i("Tamanio",""+taskSnapshot); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("FILEEE","failure"); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        });

        refImg3.getFile(fireImg3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                Log.i("FILEEE","exito");
                //Aqui ya obtuviste el archivo, asi que puedes chequear su tamaño con un log

                Log.i("Tamanio",""+taskSnapshot); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("FILEEE","failure"); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        });

        refImg4.getFile(fireImg4).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                Log.i("FILEEE","exito");
                //Aqui ya obtuviste el archivo, asi que puedes chequear su tamaño con un log

                Log.i("Tamanio",""+taskSnapshot); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("FILEEE","failure"); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        });

        refImg5.getFile(fireImg5).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                Log.i("FILEEE","exito");
                //Aqui ya obtuviste el archivo, asi que puedes chequear su tamaño con un log

                Log.i("Tamanio",""+taskSnapshot); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("FILEEE","failure"); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        });

        refImg6.getFile(fireImg6).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                Log.i("FILEEE","exito");
                //Aqui ya obtuviste el archivo, asi que puedes chequear su tamaño con un log

                Log.i("Tamanio",""+taskSnapshot); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("FILEEE","failure"); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        });

        refImg7.getFile(fireImg7).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created

                Log.i("FILEEE","exito");
                //Aqui ya obtuviste el archivo, asi que puedes chequear su tamaño con un log

                Log.i("Tamanio",""+taskSnapshot); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i("FILEEE","failure"); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
            }
        });


    }
    public static File fireImg1;
    public static File fireImg2;
    public static File fireImg3;
    public static File fireImg4;
    public static File fireImg5;
    public static File fireImg6;
    public static File fireImg7;


    private void downloadFiles(ArrayList<StorageReference> remoteFireImg, File outputFile) {
        for (StorageReference ref: remoteFireImg
        ) {
            ref.getFile(outputFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created

                    Log.i("FILEEE","exito");
                    //Aqui ya obtuviste el archivo, asi que puedes chequear su tamaño con un log

                    Log.i("Tamanio",""+taskSnapshot); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.i("FILEEE","failure"); //creo que era byte count pero con un get byte obtenias el tamaño del archivo
                }
            });
        }

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
                //imageView = findViewById(R.id.image_grid);
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

        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                imgV = new ImageView(context);
                imgV.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imgV.setLayoutParams(new GridView.LayoutParams(250, 400));
            } else {
                imgV = (ImageView) convertView;
            }
            try {
                //InputStream ims = context.getAssets().open("img/" + list[position]);
                //Bitmap bitmap = BitmapFactory.decodeStream(ims);
                //imgV.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return imgV;
        }}



