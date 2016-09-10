package com.mediaplayer.listeners;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.TextView;

import com.mediaplayer.services.MathService;
import com.mediaplayer.services.SrtParser;
import com.mediaplayer.variables.CommonArgs;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class VideoOnPreparedListener implements MediaPlayer.OnPreparedListener {

    public TextView totalTime;
    private Handler handler = new Handler();

    @Override
    public void onPrepared(MediaPlayer mp) {
        CommonArgs.mediaPlayer = mp; // used to pause video
        System.out.println("duration before onPrepared:::: " + CommonArgs.duration);
        CommonArgs.duration = mp.getDuration();
        totalTime.setText(MathService.timeFormatter(CommonArgs.duration));
        CommonArgs.seekBar.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
        CommonArgs.isPlaying = true;
        Thread th = new Thread() {
            public void run() {
                showSub();
            }
        };
        th.start();
        th.setPriority(Thread.MIN_PRIORITY);
    }

    public void showSub() {
        final String srtFile = new FilenameUtils().removeExtension(CommonArgs.currentVideoPath) + ".srt";
        if (!new File(srtFile).exists()) {
            return;
        }
        final SrtParser objSrtParser = new SrtParser();
        final StringBuilder text = objSrtParser.loadSrt(srtFile);
        objSrtParser.parse(text);
        Runnable runnable = new Runnable() {
            public void run() {
                if (CommonArgs.isPlaying) {
                    Thread th = Thread.currentThread();
                    th.setPriority(Thread.MIN_PRIORITY);
                    String time = objSrtParser.srtTimeFormatter(CommonArgs.mediaPlayer.getCurrentPosition());
                    final String line = objSrtParser.showMap(time);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (line != null && line != "") {
                                styleText(line);
                                CommonArgs.subArea.setText(removeTags(line));
                            } else if (line == "") {
                                CommonArgs.subArea.setText("");
                            }
                        }
                    });
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.post(runnable);
    }


    public void styleText(String line) {
        if (line.startsWith("<i>") && line.endsWith("</i>")) {
            CommonArgs.subArea.setTypeface(CommonArgs.subArea.getTypeface(), Typeface.BOLD_ITALIC);
        } else if (line.startsWith("<u>") && line.endsWith("</u>")) {
            CommonArgs.subArea.setPaintFlags(CommonArgs.subArea.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else if (line.startsWith("<font") && line.endsWith("</font>")) {
            String colorAttrib = line.substring(line.indexOf("#"), line.lastIndexOf("\"")); // test phase
            System.out.println("Color:::: " + colorAttrib);
//            colorAttrib.endsWith();
//            colorParse(line);
//            Parse(line);
            CommonArgs.subArea.setTextColor(Color.parseColor(colorAttrib));
        } else {
            CommonArgs.subArea.setTypeface(CommonArgs.subArea.getTypeface(), Typeface.BOLD);
            CommonArgs.subArea.setPaintFlags(0);
            CommonArgs.subArea.setTextColor(Color.WHITE); // sets default text color white
        }
        CommonArgs.subArea.setPaintFlags(CommonArgs.subArea.getPaintFlags() | Paint.ANTI_ALIAS_FLAG); // makes text smooth
    }

    public String removeTags(String line) {
        return line.replaceAll("<i>|</i>|<u>|</u>|<b>|</b>", "");
    }

}
