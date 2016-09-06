package com.mediaplayer.components;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mediaplayer.R;
import com.mediaplayer.activites.PlayFile;
import com.mediaplayer.services.MathService;
import com.mediaplayer.variables.CommonArgs;

public class PopUpDialog {

    public void showAlertDialog(final StringBuilder text) {
        AlertDialog.Builder objAlertDialogBuilder = new AlertDialog.Builder(CommonArgs.playFileCtx, 000);
//        Dialog dialog = objAlertDialogBuilder.create();
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.background_dark);
        objAlertDialogBuilder.setTitle("Resume Playback");
        objAlertDialogBuilder.setMessage("Resume playback from " + new MathService().timeFormatter(Long.parseLong(text.toString().trim())) + "?");
        objAlertDialogBuilder.setIcon(R.drawable.seek_to_time);
        objAlertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                CommonArgs.mediaPlayer.seekTo(Integer.parseInt(text.toString().trim()));
            }
        });
        objAlertDialogBuilder.setNegativeButton(android.R.string.no, null).show();
    }

    public PopupWindow showCustomAlertDialog(View choice_pop_up, LinearLayout.LayoutParams llp) {
        PopupWindow pw = new PopupWindow(choice_pop_up, llp.width, llp.height, true);
        pw.setBackgroundDrawable(new ColorDrawable()); //helped me to hide popup
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        return pw;
    }

}
