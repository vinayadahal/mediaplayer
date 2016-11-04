package com.ndktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test();

    }

    public void test() {
        String libname = "ffmpeg";
        System.out.println("doing horriable things----------->");
        System.loadLibrary(libname);
        System.out.println("Hope that worked------>");
    }
}
