package com.example.zzler.puzzleGame;


import static java.lang.Math.abs;
import com.example.zzler.main.MainActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.provider.CalendarContract;
import android.content.ActivityNotFoundException;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.provider.CalendarContract;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

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


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zzler.main.MainActivity;
import com.example.zzler.R;
import com.example.zzler.score.ScoreView;
import com.example.zzler.webView.Info;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TimeZone;

@SuppressLint("HandlerLeak")
public class PuzzleGameView extends AppCompatActivity implements IPuzzleGameView {

    private static final int PERMISSION_WRITE_CALENDAR = 0;
    private Float timeGameSolved;
    private PuzzleGamePresenterImpl gamePresenter;
    protected static Integer dificulty;
    private int count;
    private static int countToTimer;
    static TextView textFinish;
    static TextView txtTimeGame;
    static ImageView imageStarFinish;
    static boolean paused;
    static ArrayList<Timer> afterClickTimerCollection;
    boolean activateDB;
    Context context;
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

    @Override
    public void saveScoreInCalendar(String puzzleName, float timeToSolved) {
        ContentResolver cr = context.getContentResolver();
        int timeTo = (int) timeToSolved;
        String score = " Score: " + (long) timeTo + " seg";
        Log.i("SAVESCOREINCALENDAR", "Starts to save score in mobile calendar");
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Calendar beg = Calendar.getInstance();
        beg.add(Calendar.SECOND, -timeTo);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        beg.set(beg.get(Calendar.YEAR), beg.get(Calendar.MONTH), beg.get(Calendar.DAY_OF_MONTH), beg.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        ContentValues cv = new ContentValues();
        cv.put("calendar_id", 1);
        cv.put("title", puzzleName + " " + score);
        cv.put("description", score);
        TimeZone tz = cal.getTimeZone();

        cv.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        cv.put("dtstart", beg.getTimeInMillis());
        cv.put("dtend", cal.getTimeInMillis());
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSION_WRITE_CALENDAR);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                long eventID = Long.parseLong(uri.getLastPathSegment());
                Toast.makeText(this, "Se ha insetado la puntuación en el calendario", Toast.LENGTH_LONG).show();
                Log.i("SAVESCOREINCALENDAR", "Permission was granted");
            }else{
                Toast.makeText(this, "No se han aceptado los permisos para guardar la puntuación en el calendario", Toast.LENGTH_LONG).show();
                Log.i("SAVESCOREINCALENDAR", "Permission was not granted");
            }
        } catch (Throwable t) {
            Log.i("SAVESCOREINCALENDAR", "Error saving in Calendar");
            Log.i("SAVESCOREINCALENDAR", String.valueOf(t));
            Toast.makeText(this, "No se ha podido grabar la puntuación en el calendario", Toast.LENGTH_LONG).show();
        }
    }

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
                        txtTimeGame.setText(time.toString() + " Segundos");
                        Log.i("interval",time.toString());
                    }else{
                        if(activateDB){
                            long id = (long) saveScore("Level #"+count, time);
                            saveScoreInCalendar ("Level #"+count, time);
                            if(id > 0){
                                Toast.makeText(PuzzleGameView.this, "Values inserted!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(PuzzleGameView.this, "Error when inserting values", Toast.LENGTH_LONG).show();
                            }
                        }

                        time = 0;
                        stop();


                    }

                }

                private void stop() {
                    TouchListener.setCountToShowFinishMsg(0);
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

        if (getIntent().getParcelableExtra("photo")!=null){
            bitmap = getIntent().getParcelableExtra("photo");
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.setImageDrawable(d);
            //imageView.setImageBitmap(bitmap);
        }else if(getIntent().getParcelableExtra("photoGallery")!=null){
            //Log.i("******************************************************dentro",getIntent().getParcelableExtra("photoGallery"));
            Uri uri = getIntent().getParcelableExtra("photoGallery");
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            imageView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            imageView.setImageDrawable(d);
        }else{
            bitmap = BitmapFactory.decodeResource(getResources(), img);
            imageView.setImageDrawable(getResources().getDrawable(img));
        }


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
    public float saveScore(String puzzleName, float timeToSolved) {
        long id = gamePresenter.saveScore(puzzleName, timeToSolved, context);
        this.activateDB = false;
        return id;
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
