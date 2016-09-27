package com.mediaplayer.codectest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private SurfaceHolder Surface;
    private Uri mVideoUri = Uri.parse("/storage/sdcard0/Videos/Roar.mp4");
    int frameNumber = 0;
    Frame frame;
    ImageView imageView;
    SurfaceView surfaceView;
    FFmpegFrameGrabber fGrabber = new FFmpegFrameGrabber("/storage/sdcard0/Videos/Roar.mp4");
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        playFile();
        imageView = (ImageView) findViewById(R.id.image_view);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                test();
            }
        }, 1000);
    }

//    public void playFile() {
////        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface_view);
////        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
////            @Override
////            public void surfaceCreated(SurfaceHolder holder) {
////                Surface = holder;
////                if (mVideoUri != null) {
////                    mMediaPlayer = MediaPlayer.create(getApplicationContext(), mVideoUri, Surface);
////                    mMediaPlayer.start();
////                }
////
////            }
////
////            @Override
////            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
////
////            }
////
////            @Override
////            public void surfaceDestroyed(SurfaceHolder holder) {
////
////            }
////        });
//    }

//    public void test() {
//
//
//        final Handler handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    System.out.println("showing image::::: ");
//                    Bitmap frame = FrameGrab.getFrame(new File("/storage/sdcard0/AVSEQ02.mpg"), frameNumber++);
//                    ImageView imageView = (ImageView) findViewById(R.id.image_view);
//                    imageView.setImageBitmap(frame);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JCodecException e) {
//                    e.printStackTrace();
//                }
//                handler.postDelayed(this, 40);
//            }
//        });
//    }


    public void test() {
        try {
            fGrabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    frame = fGrabber.grabImage();
                    System.out.println("Frame number-----------------> " + fGrabber.getFrameNumber());
                    imageView.setImageBitmap(new AndroidFrameConverter().convert(frame));
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 40);
            }
        });
    }

}
