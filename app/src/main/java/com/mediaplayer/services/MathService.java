package com.mediaplayer.services;

import java.util.ArrayList;
import java.util.List;

public class MathService {

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
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public List<Float> getSwipeArea(float startx, float starty, float endx, float endy) {
        System.out.println("start x: " + startx + "end x: " + endx);
        float sumx = 0, sumy = 0;
        List<Float> sums = new ArrayList<>();
        if (startx < endx) {
            sumx = startx - endx;
        } else if (startx > endx) {
            sumx = startx - endx;
        } else if (starty < endy) {
            sumy = starty - endy;
        } else if (starty > endy) {
            sumy = starty - endy;
        }
        sums.add(sumx);
        sums.add(sumy);

        return sums;
    }
}
