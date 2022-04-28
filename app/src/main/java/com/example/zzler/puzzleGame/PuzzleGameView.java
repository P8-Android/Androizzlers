package com.example.zzler.puzzleGame;


import static java.lang.Math.abs;
import com.example.zzler.main.MainActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;



import com.example.zzler.R;
import com.example.zzler.main.MainActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("HandlerLeak")
public class PuzzleGameView extends AppCompatActivity implements IPuzzleGameView {

    private Float timeGameSolved;
    private PuzzleGamePresenterImpl gamePresenter;
    protected static Integer dificulty;
    private int count;
    private static int countToTimer;
    static TextView textFinish;
    static TextView txtTimeGame;
    static Timer myTimer = new Timer();
    static boolean paused;
    static ArrayList<Timer> afterClickTimerCollection;

    ArrayList<PuzzlePiece> pieces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_game);

        textFinish = findViewById(R.id.txtFinish);
        txtTimeGame = findViewById(R.id.timeGame);
        final RelativeLayout layout = findViewById(R.id.layout);
        ImageView imageView = findViewById(R.id.imageView);
        paused = false;
        // run image related code after the view was laid out
        // to have all dimensions calculated
        ImageButton btnUpLevel = findViewById(R.id.btnUpLevel);
        dificulty = 2;
        count = 1;
        countToTimer = 0;
        afterClickTimerCollection = new ArrayList<>();

        startTimer();


        btnUpLevel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                paused = true;
                afterClickTimerCollection.get(countToTimer).cancel();
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
                        pieces = splitImage(dificulty+count);
                        dificulty = dificulty + count;
                        TouchListener touchListener = new TouchListener();
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
                pieces = splitImage(dificulty);
                TouchListener touchListener = new TouchListener();
                for(PuzzlePiece piece : pieces) {
                    piece.setOnTouchListener(touchListener);
                    layout.addView(piece);
                }
            }
        });

    }


    protected void startTimer() {

        afterClickTimerCollection.add(new Timer());
        afterClickTimerCollection.get(countToTimer).scheduleAtFixedRate(new TimerTask(){

            Integer time = 0;

            @Override
            public void run(){ runOnUiThread (new Runnable() {
                @Override
                public void run() {
                    if(!paused){
                        time++;
                        txtTimeGame.setText(time.toString() + " Segundos");
                        //Log.i("interval",time.toString());
                    }else{
                        time = 0;
                    }
                }
            });
            }
        },0,1000);
    }



    protected static String resolved(){
        textFinish.setVisibility(View.VISIBLE);
        paused = true;
        afterClickTimerCollection.get(countToTimer).purge();
        String timeString = (String) txtTimeGame.getText();
        //Integer finishTime = Integer.parseInt(timeString); se rompe  con parseInt
        txtTimeGame.setText("Tiempo final: "+timeString);
        return timeString;

    }

    protected static Integer calculateScore(){
        String timeString = TouchListener.getTimeTotal();
        return (int) 1/Integer.parseInt(timeString);
    }



    protected ArrayList<PuzzlePiece> splitImage(Integer dificulty) {
        int rows = dificulty;
        int cols = dificulty;
        int piecesNumber = rows*cols;

        ImageView imageView = findViewById(R.id.imageView);
        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        //Bitmap bitmap = drawable.getBitmap(); BUG
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
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

        return pieces;
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
        paused = true;
        dificulty = 2;
        myTimer.purge();
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    @Override
    public void showNextPuzzle(ImageView img) {

    }

    @Override
    public void saveScore(Float timeGameSolved) {

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
