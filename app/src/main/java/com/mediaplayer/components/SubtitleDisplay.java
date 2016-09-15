package com.mediaplayer.components;


import android.text.Html;
import android.widget.TextView;

import com.mediaplayer.services.SrtParser;
import com.mediaplayer.variables.CommonArgs;

import java.io.File;

public class SubtitleDisplay {

    public void showSub() {
        final String srtFile = CommonArgs.currentVideoPath.substring(0, CommonArgs.currentVideoPath.lastIndexOf(".")) + ".srt";
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
                                CommonArgs.subArea.setText(Html.fromHtml(line), TextView.BufferType.NORMAL);
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
}
