package com.mediaplayer.components;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mediaplayer.R;
import com.mediaplayer.activites.PlayFile;
import com.mediaplayer.listeners.BrightnessOnSeekBarChangeListener;
import com.mediaplayer.listeners.PopUpOnTouchListener;
import com.mediaplayer.listeners.VolumeOnSeekBarChangeListener;
import com.mediaplayer.services.MathService;
import com.mediaplayer.variables.CommonArgs;

public class PopUpDialog {

    private LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private LayoutInflater inflater = (LayoutInflater) CommonArgs.playFileCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    private PopupWindow pw_resume, pw_volume, pw_brightness;
    private PopUpOnTouchListener objPopUpOnTouchListener = new PopUpOnTouchListener();
    private PlayFile playFile = (PlayFile) CommonArgs.playFileCtx;

    public void initCustomPopUp(View popup, final PopupWindow pw, final StringBuilder text) {
        RelativeLayout popup_resume = (RelativeLayout) popup.findViewById(R.id.popup_resume);
        popup_resume.setOnTouchListener(objPopUpOnTouchListener);
        TextView resumeText = (TextView) popup.findViewById(R.id.resume_text);
        resumeText.setText("Do you want to resume playback from " + MathService.timeFormatter(Long.parseLong(text.toString().trim())) + "?");
        Button resumeBtn = (Button) popup.findViewById(R.id.popup_call_btn);
        resumeBtn.setTransformationMethod(null);
        resumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonArgs.mediaPlayer.seekTo(Integer.parseInt(text.toString().trim()));
                pw.dismiss();
            }
        });
        Button cancelBtn = (Button) popup.findViewById(R.id.popup_sms_btn);
        cancelBtn.setTransformationMethod(null);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }

    public void showResumeDialog(final StringBuilder text) {
        View popup = inflater.inflate(R.layout.pop_up_resume, null, true);
        pw_resume = new PopupWindow(popup, llp.width, llp.height, true);
        objPopUpOnTouchListener.pw = pw_resume;
        initCustomPopUp(popup, pw_resume, text); // initialize popup components
        popupShowAt(pw_resume);
    }

    public void showVolumeDialog() {
        View popup = inflater.inflate(R.layout.pop_up_volume, null, true);
        SeekBar volumeSeekBar = (SeekBar) popup.findViewById(R.id.volume_seekbar);
        volumeSeekBar.setMax(15);
        volumeSeekBar.setProgress(CommonArgs.audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));//sets current volume to seekbar
        volumeSeekBar.setOnSeekBarChangeListener(new VolumeOnSeekBarChangeListener());
        RelativeLayout popup_volume = (RelativeLayout) popup.findViewById(R.id.popup_volume);
        pw_volume = new PopupWindow(popup, llp.width, llp.height, true);
        objPopUpOnTouchListener.pw = pw_volume;
        popup_volume.setOnTouchListener(objPopUpOnTouchListener);
        popupShowAt(pw_volume);
    }

    public void showBrightnessDialog() {
        View popup = inflater.inflate(R.layout.pop_up_brightness, null, true);
        SeekBar brightnessSeekBar = (SeekBar) popup.findViewById(R.id.brightness_seekbar);
        brightnessSeekBar.setMax(10);
        WindowManager.LayoutParams lp = playFile.getWindow().getAttributes(); // brightness issue.
        if (lp.screenBrightness > 0) {
            System.out.println("Current lp:::" + (int) lp.screenBrightness * 10);
            brightnessSeekBar.setProgress((int) lp.screenBrightness * 10);
        }
        brightnessSeekBar.setOnSeekBarChangeListener(new BrightnessOnSeekBarChangeListener());
        RelativeLayout popup_brightness = (RelativeLayout) popup.findViewById(R.id.popup_brightness);
        pw_brightness = new PopupWindow(popup, llp.width, llp.height, true);
        objPopUpOnTouchListener.pw = pw_brightness;
        popup_brightness.setOnTouchListener(objPopUpOnTouchListener);
        popupShowAt(pw_brightness);
    }

    public void popupShowAt(PopupWindow pw) {
        pw.setBackgroundDrawable(new ColorDrawable()); //helped me to hide popup
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.showAtLocation(CommonArgs.rl_play_file, Gravity.CENTER, 0, 0);
    }

}
