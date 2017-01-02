package com.gamedemo;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        imageView.setImageBitmap(BitmapFactory.decodeFile("/storage/sdcard0/saturn.jpg"));
        TranslateAnimation anim = new TranslateAnimation(0.0f,100.0f,0.0f,100.0f);
        anim.setDuration(2000);
        imageView.setAnimation(anim);
    }
}
