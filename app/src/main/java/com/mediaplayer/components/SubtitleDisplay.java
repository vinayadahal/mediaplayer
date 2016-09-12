package com.mediaplayer.components;


import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.mediaplayer.services.SrtParser;
import com.mediaplayer.variables.CommonArgs;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public class SubtitleDisplay {

    public void showSub() {
        final String srtFile = new FilenameUtils().removeExtension(CommonArgs.currentVideoPath) + ".srt";
        if (!new File(srtFile).exists()) {
            CommonArgs.handler.post(new Runnable() {
                @Override
                public void run() {
                    if (CommonArgs.subArea.getText() != "") {
                        CommonArgs.subArea.setText("");
                    }
                }
            });
            return;
        }
        final SrtParser objSrtParser = new SrtParser();
        final StringBuilder text = objSrtParser.loadSrt(srtFile);
        objSrtParser.parse(text);
        CommonArgs.subtitle_runnable = new Runnable() {
            public void run() {
                if (CommonArgs.isPlaying) {
                    Thread th = Thread.currentThread();
                    th.setPriority(Thread.MIN_PRIORITY);
                    String time = objSrtParser.srtTimeFormatter(CommonArgs.mediaPlayer.getCurrentPosition());
                    final String line = objSrtParser.showMap(time);
                    CommonArgs.handler.post(new Runnable() {
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
                    CommonArgs.handler.postDelayed(this, 500);
                }
            }
        };
        CommonArgs.handler.post(CommonArgs.subtitle_runnable);
    }

    public void styleText(String line) {
        if (line.startsWith("<i>") && line.endsWith("</i>")) {
            CommonArgs.subArea.setTypeface(CommonArgs.subArea.getTypeface(), Typeface.BOLD_ITALIC);
        } else if (line.startsWith("<u>") && line.endsWith("</u>")) {
            CommonArgs.subArea.setPaintFlags(CommonArgs.subArea.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            removeColor();
            defaultText();
        } else if (line.startsWith("<font") && line.endsWith("</font>")) {
            String colorAttrib = line.replaceAll(".*\"#|\".*", ""); // replaces everything before, including and between "# & "
            CommonArgs.subArea.setTextColor(Color.parseColor("#" + colorAttrib));
            defaultText();
        } else {
            removeStyles();
            defaultText();
        }
    }

    public void defaultText() {
        CommonArgs.subArea.setTypeface(CommonArgs.subArea.getTypeface(), Typeface.BOLD);
        CommonArgs.subArea.setPaintFlags(CommonArgs.subArea.getPaintFlags() | Paint.ANTI_ALIAS_FLAG); // makes text smooth
    }

    public void removeStyles() {
        CommonArgs.subArea.setPaintFlags(0);
        removeColor();
    }

    public void removeColor() {
        CommonArgs.subArea.setTextColor(Color.WHITE); // sets default text color white
    }

    public String removeTags(String line) {
//        return line.replaceAll("<i>|</i>|<u>|</u>|<b>|</b>|<font\\s*>|</font>", "");
        return line.replaceAll("<[^>]*>", "");
    }

}
