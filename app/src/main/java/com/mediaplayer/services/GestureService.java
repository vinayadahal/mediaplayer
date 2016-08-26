package com.mediaplayer.services;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureService extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        System.out.println("e1 GetY " + e1.getY());
        System.out.println("e2 GetY " + e2.getY());
        try {
//            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
//                return false;
//            }

            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // right to left swipe
                System.out.println("R to L swipe");
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // left to right swipe
                System.out.println("L to R swipe");
            } else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // down to up swipe
                System.out.println("D to U swipe");
                new MediaControl().setVolumeUp();
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                // up to down swipe
                System.out.println("U to D swipe");
                new MediaControl().setVolumeDown();
            }
        } catch (Exception e) {

        }
        return false;
    }
}
