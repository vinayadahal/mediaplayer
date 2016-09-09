package com.mediaplayer.components;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mediaplayer.R;
import com.mediaplayer.services.MathService;
import com.mediaplayer.variables.CommonArgs;

public class PopUpDialog {

    public void showAlertDialog(final StringBuilder text) {
        AlertDialog.Builder objAlertDialogBuilder = new AlertDialog.Builder(CommonArgs.playFileCtx, 000);
        objAlertDialogBuilder.setTitle("Resume Playback");
        objAlertDialogBuilder.setMessage("Resume playback from " + MathService.timeFormatter(Long.parseLong(text.toString().trim())) + "?");
        objAlertDialogBuilder.setIcon(R.drawable.seek_to_time);
        objAlertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                CommonArgs.mediaPlayer.seekTo(Integer.parseInt(text.toString().trim()));
            }
        });
        objAlertDialogBuilder.setNegativeButton(android.R.string.no, null).show();
    }

    public void initCustomPopUp(View choice_pop_up, final PopupWindow pw, final StringBuilder text) {
        TextView resumeText = (TextView) choice_pop_up.findViewById(R.id.resume_text);
        resumeText.setText("Do you want to resume playback from " + MathService.timeFormatter(Long.parseLong(text.toString().trim())) + "?");

        Button call_btn = (Button) choice_pop_up.findViewById(R.id.popup_call_btn);
        call_btn.setTransformationMethod(null);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonArgs.mediaPlayer.seekTo(Integer.parseInt(text.toString().trim()));
                pw.dismiss();
            }
        });
        Button sms_btn = (Button) choice_pop_up.findViewById(R.id.popup_sms_btn);
        sms_btn.setTransformationMethod(null);
        sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }

    public void showCustomAlertDialog(final StringBuilder text) {
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutInflater inflater = (LayoutInflater) CommonArgs.playFileCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View choice_pop_up = inflater.inflate(R.layout.pop_up_layout, null, true);
        final PopupWindow pw = new PopupWindow(choice_pop_up, llp.width, llp.height, true);
        initCustomPopUp(choice_pop_up, pw, text); // initialize popup components
        pw.setBackgroundDrawable(new ColorDrawable()); //helped me to hide popup
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.showAtLocation(CommonArgs.rl_play_file, Gravity.CENTER, 0, 0);
    }

}
