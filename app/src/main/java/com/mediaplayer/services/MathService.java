package com.mediaplayer.services;

public class MathService {

    private float sumx, sumy;

    public String convertFileSize(long bytes) {
        double kilobytes = (double) Math.round((bytes / 1024) * 100) / 100;
        double megabytes = (double) Math.round((kilobytes / 1024) * 100) / 100;
        double gigabytes = (double) Math.round((megabytes / 1024) * 100) / 100;
        if (kilobytes < 1024) {
            return kilobytes + " KB";
        }
        if (megabytes < 1024) {
            return megabytes + " MB";
        }
        if (gigabytes < 1024) {
            return gigabytes + " GB";
        }
        return bytes + " Bytes";
    }

    public String timeFormatter(long duration) {
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = (duration / (1000 * 60 * 60)) % 24;
        if (hour == 0) {
            return String.format("%02d:%02d", minute, second);
        }
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public String getSwipeArea(float startx, float starty, float endx, float endy) {
        System.out.println("start x: " + startx + " end x: " + endx);
        System.out.println("start y: " + starty + " end y: " + endy);

        String actionToDo = null;
        if (startx < endx) {
            sumx = startx - endx;
        } else if (startx > endx) {
            sumx = startx - endx;
        } else if (starty < endy) {
            System.out.println("endy is big-----");
            sumy = starty - endy;
        } else if (starty > endy) {
            System.out.println("starty is big---");
            sumy = starty - endy;
        }
        if (sumy >= 100 && sumx < sumy) {
            System.out.println("You may have swiped bottom to top");
            System.out.println("distance " + (int) sumy / 48);
            actionToDo = "volumeUP";
        }
        if (sumx >= 100 && sumy < sumx) {
            System.out.println("You may have swiped right to left");
        }
        if (sumx <= -100 && sumy > sumx) {
            System.out.println("You may have swiped left to right");
        }
        if (sumy <= -100 && sumx > sumy) {
            System.out.println("You may have swiped top to bottom");
            actionToDo = "volumeDown";
        }
        System.out.println("sumx " + sumx);
        System.out.println("sumy " + sumy);
        return actionToDo;
    }
}
