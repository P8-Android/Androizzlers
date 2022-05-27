package com.example.zzler.puzzleGame;


import static java.lang.Math.abs;
import com.example.zzler.main.MainActivity;
import android.annotation.SuppressLint;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Color;
import android.os.Build;
import android.provider.CalendarContract;
import android.content.ActivityNotFoundException;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.provider.CalendarContract;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.metrics.Event;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.zzler.main.MainActivity;
import com.example.zzler.R;
import com.example.zzler.score.ScoreView;
import com.example.zzler.score.Score;
import com.example.zzler.webView.Info;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.TimeZone;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TimeZone;

@SuppressLint("HandlerLeak")
public class PuzzleGameView extends AppCompatActivity implements IPuzzleGameView {

    private static final int PERMISSION_WRITE_CALENDAR = 0;
    private static final int PERMISSION_READ_CALENDAR = 0;
    private Float timeGameSolved;
    private PuzzleGamePresenterImpl gamePresenter;
    protected static Integer dificulty;
    private int count;
    private static int countToTimer;
    static TextView textFinish;
    static TextView txtTimeGame;
    static ImageView imageStarFinish;
    static boolean paused;
    boolean isNewRecord;
    static ArrayList<Timer> afterClickTimerCollection;
    boolean activateDB;
    Context context;
    public Context staticcontext = this;
    Integer urlImg;
    ImageView imageView;
    Runnable runnable;

    ArrayList<PuzzlePiece> pieces;

    //Producto2
    public MediaPlayer mediaPlayer;
    private Uri urlSong;
    public SwitchCompat aSwitch;
    private Button btnSelectSong;
    private Context c;
    private MusicManager musicManager;
    private HashMap<Integer, Boolean> mapImgToSplit;


    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "principal";
    private final static int NOTIFICATION_ID = 0;


    //STORAGE
    FirebaseStorage storage = FirebaseStorage.getInstance();

    // Create a storage reference from our app
    StorageReference storageRef = storage.getReference();

    ArrayList<StorageReference> remoteFireImg;
    ArrayList<File> files;
    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID);
        builder.setContentTitle("New or equal RECORD!!");
        builder.setContentText("Calendar's Record has been saved!");
        builder.setSmallIcon(R.drawable.puzzleicon);
        builder.setColor(Color.CYAN);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setLights(Color.CYAN, 1000, 1000);
        builder.setVibrate(new long[]{1000L, 1000L, 1000L, 1000L});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        CharSequence name = "notificacion";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
    }
    @Override
    protected void onPause() {
        super.onPause();
        afterClickTimerCollection.get(countToTimer).cancel();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.showInfo:
                openWebView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openWebView() {
        Intent i = new Intent(this, Info.class);
        startActivity(i);
    }

    

    public boolean isNewRecord(int level, int score){
        int bestScoreStore = getScoreCalendar(level);
        return bestScoreStore < score?false: true;
    }

    public JSONObject getBestScoreLevels(int maxLevel){
        JSONObject bestScoreLevels = new JSONObject();

        try {
            for(int i=1; i<=maxLevel; i++){
                String level = "Level " + i;
                bestScoreLevels.put(level, getScoreCalendar(i));
            }
        //Log.i("TAG","" + bestScoreLevels.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bestScoreLevels;
    }

    public int getScoreCalendar(int levelToEvaluate){
        ArrayList <Integer> scoreLevelList = new ArrayList<Integer>();

        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
        };

        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_TITLE_INDEX = 1;
        final int PROJECTION_DTSTART_INDEX = 2;
        final int PROJECTION_DTEND_INDEX = 3;

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2020, 3, 18, 0, 0);
        long startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(2020, 3, 24, 0, 0);
        long endMillis = endTime.getTimeInMillis();

        String where = "( (title LIKE \'Puzzle%\') AND (calendar_id = " + 1 + "))";

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {

            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(CalendarContract.Events.CONTENT_URI, EVENT_PROJECTION, where,null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String fullInfo = cursor.getString(PROJECTION_TITLE_INDEX);
                    String score = fullInfo.split(" ")[3];
                    Log.i("score", score);
                    String level = fullInfo.split(" ")[0].split("#")[1];
                    Log.i("level", level);
                    if (Integer.parseInt(level) == levelToEvaluate){
                        Log.i("GetMaxScore", level);
                        scoreLevelList.add(Integer.parseInt(score));
                    }
                }
                Collections.sort(scoreLevelList);
                Log.i("TAG", "Para el nivel: "+levelToEvaluate+" el record es: " + scoreLevelList.get(0) + " segundos");
                cursor.close();
            }

        }
        Log.i("scoreLevelList", scoreLevelList.toString());
        return scoreLevelList.get(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game);


        Toolbar toolbar = findViewById(R.id.toolbarGame);
        toolbar.setTitle("Puzzle");
        setSupportActionBar(toolbar);
        urlImg = getIntent().getIntExtra("pos",1);
        gamePresenter = new PuzzleGamePresenterImpl();
        textFinish = findViewById(R.id.txtFinish);
        txtTimeGame = findViewById(R.id.txtTimeGame);
        imageStarFinish = findViewById(R.id.scoreStars);
        btnSelectSong = findViewById(R.id.selectSong);
        aSwitch = findViewById(R.id.turnMusic);
        final RelativeLayout layout = findViewById(R.id.layout);
        imageView = findViewById(R.id.imageView);
        paused = false;
        activateDB = true;
        // run image related code after the view was laid out
        // to have all dimensions calculated
        ImageButton btnUpLevel = findViewById(R.id.btnUpLevel);
        context = this;
        dificulty = 2;
        count = 1;
        countToTimer = 0;
        afterClickTimerCollection = new ArrayList<>();
        createNotificationChannel();

        //Producto2
        c = this;
        aSwitch = findViewById(R.id.turnMusic);
        btnSelectSong = findViewById(R.id.selectSong);
        int urlSongFirst = R.raw.officialsong;
        mediaPlayer = MediaPlayer.create(this,urlSongFirst);
        mediaPlayer.start();
        aSwitch.setChecked(true);
        musicManager = new MusicManager(c);
        mapImg = new MapImg();
        mapImgToSplit = mapImg.mapImgToSplit;


        Log.i("FILEEE","Antes de todo");


        ArrayList<String> namesJpg = new ArrayList<>();
        namesJpg.add("doctor_strange.jpg");
        namesJpg.add("gandalf.jpg");
        namesJpg.add("gandalf_vs_demond.jpg");
        namesJpg.add("groot.jpg");
        namesJpg.add("hobbit_house.jpg");
        namesJpg.add("hulk.jpg");
        namesJpg.add("star_lord.jpg");


        remoteFireImg = new ArrayList<>();

        int i = 0;
        for (StorageReference ref: remoteFireImg
             ) {
            remoteFireImg.add(storageRef.child("images/"+namesJpg.get(i)));
            i++;
        }


        files = new ArrayList<>();
        File outputFile = null;
        int b = 1;
        //File outputDir = this.getCacheDir(); // context being the Activity pointer
        try {
            files.add(File.createTempFile("images"+b, "jpg"));
            files.add(new File(context.getCacheDir(), "images"+b));
            Log.i("FILEEEE_toString", files.get(1).toString());
            b++;
        } catch (IOException e) {
            e.printStackTrace();
        }

        //File localFile = new File(this.getFilesDir(), "img1");
        //File fireImg1 = new File(context.getCacheDir(), "img1");
        //Log.i("FILEEEE", fireImg1.toString());
        for (File file: files
             ) {
            downloadFiles(remoteFireImg, file);

        }


        //File fireImg1 = new File(context.getCacheDir(), "images");
        //Log.i("FILEEEE_File", fireImg1.toString());





    btnSelectSong.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final int PICK_MP3_FILE = 2;
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("audio/*");
                startActivityForResult(intent, PICK_MP3_FILE);
            }
        });


        btnUpLevel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                activateDB = true;
                afterClickTimerCollection.get(countToTimer).cancel();
                paused = true;
                countToTimer++;
                startTimer();
                count++;


                for(PuzzlePiece piece : pieces) {
                    layout.removeView(piece);
                }
                TouchListener.countToShowFinishMsg = 0;
                textFinish.setVisibility(View.GONE);



                paused = false;


                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        pieces.removeAll(pieces);
                        try {
                            pieces = splitImage(dificulty+count,urlImg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dificulty = dificulty + count;
                        TouchListener touchListener = new TouchListener(musicManager.mdDrag,musicManager.mdSuccess);
                        for(PuzzlePiece piece : pieces) {
                            piece.setOnTouchListener(touchListener);
                            layout.addView(piece);
                        }

                    }
                });
            }


        });



        imageView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    pieces = splitImage(dificulty,urlImg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                TouchListener touchListener = new TouchListener(musicManager.mdDrag,musicManager.mdSuccess);
                for(PuzzlePiece piece : pieces) {
                    piece.setOnTouchListener(touchListener);
                    layout.addView(piece);
                }
            }
        });

        startTimer();
        turnMusic();


    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2
                && resultCode == Activity.RESULT_OK) {
            Log.i("*******","ha pasado por aqui");
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                urlSong = uri;
                if (urlSong!=null){
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(this,urlSong);
                    mediaPlayer.start();
                }
                // Perform operations on the document using its URI.
            }
        }
    }
    private void turnMusic() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b)
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                    }
                if (b){
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                    }
                }
            }
        });
    }



protected void startTimer() {

        afterClickTimerCollection.add(new Timer());
        afterClickTimerCollection.get(countToTimer).scheduleAtFixedRate(new TimerTask(){

            Integer time = 0;

            @Override
            public void run(){ runOnUiThread (runnable = new Runnable() {
                @Override
                public void run() {
                    if(!paused){
                        time++;
                        txtTimeGame.setText(time.toString() + context.getResources().getString(R.string.secs)); //pasamos el texto desde strings.xml para internacionalización
                        Log.i("interval",time.toString());
                    }else{
                        if(activateDB){
                            saveScore("Level #"+count, time, new Date(System.currentTimeMillis()));
                            /*
                            saveScoreInCalendar ("Level #"+count, time);
                            if(id > 0){
                                Toast.makeText(PuzzleGameView.this, "Values inserted!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(PuzzleGameView.this, "Error when inserting values", Toast.LENGTH_LONG).show();
                            }

                             */
                        }
                        stop();
                    }
                }

                private void stop() {

                    TouchListener.setCountToShowFinishMsg(0);
                    time = 0;
                    afterClickTimerCollection.get(countToTimer).cancel();
                }
            });
            }
        },0,1000);


    }



    protected static String resolved(){
        textFinish.setVisibility(View.VISIBLE);
        imageStarFinish.setVisibility(View.VISIBLE);
        paused = true;
//        countToTimer++;
        String timeString = (String) txtTimeGame.getText();
        //Integer finishTime = Integer.parseInt(timeString); se rompe  con parseInt

        txtTimeGame.setText("Tiempo final: "+timeString);
        //txtTimeGame.setText(this.getResources().getString(R.string.finalTime) + timeString); //pasamos el texto desde strings.xml para internacionalización
        return timeString;

    }

    protected static Integer calculateScore(){
        String timeString = TouchListener.getTimeTotal();
        return (int) 1/Integer.parseInt(timeString);
    }

MapImg mapImg;

    @SuppressLint("NewApi")
    protected ArrayList<PuzzlePiece> splitImage(Integer dificulty, Integer posImg) throws IOException {
        int rows = dificulty;
        int cols = dificulty;
        int piecesNumber = rows*cols;
        Bitmap bitmap;

        imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
       // BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        //Bitmap bitmap = drawable.getBitmap(); BUG
        int img = 0;
        do{
            img = getImgToSplit(posImg);

            Log.i("Boooolean",mapImgToSplit.get(img).toString());
        }while(mapImgToSplit.get(img)==false);
        mapImgToSplit.replace(img,false);

        //mapImgToSplit.put(img,false);


            File fireImg1 = new File(context.getCacheDir(), files.get(1).toString());
            String pathName = fireImg1.getPath().replace("jpg",".jpg");
            Drawable d = Drawable.createFromPath(pathName);
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(pathName));
            //Drawable d = new BitmapDrawable(getResources(), bitmap);
            imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.setImageDrawable(d);





/*
        Uri uri = getIntent().getParcelableExtra("photoGallery");
        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
        Drawable d = new BitmapDrawable(getResources(), bitmap);
        imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        imageView.setImageDrawable(d);

 */


        int[] dimensions = getBitmapPositionInsideImageView(imageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the with and height of the pieces

        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

//        int pieceWidth = croppedImageWidth/cols;
  //      int pieceHeight = croppedImageHeight/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                // calculate offset for each piece
                int offsetX = 0;
                int offsetY = 0;
                if (col > 0) {
                    offsetX = pieceWidth / 3;
                }
                if (row > 0) {
                    offsetY = pieceHeight / 3;
                }

                // apply the offset to each piece
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord - offsetX, yCoord - offsetY, pieceWidth + offsetX, pieceHeight + offsetY);
                PuzzlePiece piece = new PuzzlePiece(getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord - offsetX + imageView.getLeft();
                piece.yCoord = yCoord - offsetY + imageView.getTop();
                piece.pieceWidth = pieceWidth + offsetX;
                piece.pieceHeight = pieceHeight + offsetY;
                // this bitmap will hold our final puzzle piece image
                Bitmap puzzlePiece = Bitmap.createBitmap(pieceWidth + offsetX, pieceHeight + offsetY, Bitmap.Config.ARGB_8888);

                // draw path
                int bumpSize = pieceHeight / 4;
                Canvas canvas = new Canvas(puzzlePiece);
                Path path = new Path();
                path.moveTo(offsetX, offsetY);
                if (row == 0) {
                    // top side piece
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                } else {
                    // top bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3, offsetY);
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5, offsetY - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, offsetY);
                    path.lineTo(pieceBitmap.getWidth(), offsetY);
                }

                if (col == cols - 1) {
                    // right side piece
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                } else {
                    // right bump
                    path.lineTo(pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.cubicTo(pieceBitmap.getWidth() - bumpSize,offsetY + (pieceBitmap.getHeight() - offsetY) / 6, pieceBitmap.getWidth() - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, pieceBitmap.getWidth(), offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.lineTo(pieceBitmap.getWidth(), pieceBitmap.getHeight());
                }

                if (row == rows - 1) {
                    // bottom side piece
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                } else {
                    // bottom bump
                    path.lineTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 3 * 2, pieceBitmap.getHeight());
                    path.cubicTo(offsetX + (pieceBitmap.getWidth() - offsetX) / 6 * 5,pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 6, pieceBitmap.getHeight() - bumpSize, offsetX + (pieceBitmap.getWidth() - offsetX) / 3, pieceBitmap.getHeight());
                    path.lineTo(offsetX, pieceBitmap.getHeight());
                }

                if (col == 0) {
                    // left side piece
                    path.close();
                } else {
                    // left bump
                    path.lineTo(offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3 * 2);
                    path.cubicTo(offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6 * 5, offsetX - bumpSize, offsetY + (pieceBitmap.getHeight() - offsetY) / 6, offsetX, offsetY + (pieceBitmap.getHeight() - offsetY) / 3);
                    path.close();
                }

                // mask the piece
                Paint paint = new Paint();
                paint.setColor(0XFF000000);
                paint.setStyle(Paint.Style.FILL);

                canvas.drawPath(path, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(pieceBitmap, 0, 0, paint);


                // draw a white border
                Paint border = new Paint();
                border.setColor(0X80FFFFFF);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8.0f);
                canvas.drawPath(path, border);

                // draw a black border
                border = new Paint();
                border.setColor(0X80000000);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(3.0f);
                canvas.drawPath(path, border);

                // set the resulting bitmap to the piece
                piece.setImageBitmap(puzzlePiece);


                pieces.add(piece);
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        Collections.shuffle(pieces);

        return pieces;
    }

    @SuppressLint("NewApi")
    private int getImgToSplit(Integer posImg) {
        int img;
            switch (posImg){
                case 0:
                    img =  R.drawable.img1;
                    this.urlImg++;
                    break;
                case 1:
                    img =  R.drawable.img2;
                    this.urlImg++;
                    break;
                case 2:
                    img =  R.drawable.img3;
                    this.urlImg++;
                    break;
                case 3:
                    img =  R.drawable.img4;
                    this.urlImg++;
                    break;
                case 4:
                    img =  R.drawable.img5;
                    this.urlImg++;
                    break;
                case 5:
                    img =  R.drawable.img6;
                    this.urlImg++;
                    break;
                case 6:
                    img =  R.drawable.img7;
                    this.urlImg = 0;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + posImg);
            }

        return img;

    }


    private static int[] getBitmapPositionInsideImageView(ImageView imageView) {
        int[] ret = new int[4];

        if (imageView == null || imageView.getDrawable() == null)
            return ret;

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        imageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        //final float scaleX = (float) 0.5;
        //final float scaleY = (float) 0.5;

        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = imageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        ret[2] = actW;
        ret[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = imageView.getWidth();
        int imgViewH = imageView.getHeight();

        int top = (int) (imgViewH - actH)/2;
        int left = (int) (imgViewW - actW)/2;

        ret[0] = left;
        ret[1] = top;

        return ret;
    }

    public void goHome(View v){
        activateDB = false;
        paused = true;
        dificulty = 2;
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    @Override
    public void showNextPuzzle(ImageView img) {

    }

    @Override
    public void saveScore(String puzzleName, float timeToSolved, Date date) {
        gamePresenter.saveScore(new Score(puzzleName,timeToSolved,date));
        //long id = gamePresenter.saveScore(puzzleName, timeToSolved, context);
        this.activateDB = false;
        //return id;
    }

    public void splitPuzzleImage (ImageView img){

    }

    public boolean isWinner (){
        boolean flag = false;
        for(PuzzlePiece piece : pieces) {
            if (piece.getCanMove()==false){ //si todas las piezas no se pueden mover, se terminó el puzzle
                flag = true;
            }else{
                flag = false;
            }
        }
        return flag;
    }




}
