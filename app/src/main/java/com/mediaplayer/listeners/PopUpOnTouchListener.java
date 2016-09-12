package com.mediaplayer.listeners;


import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

public class PopUpOnTouchListener implements View.OnTouchListener {

    public PopupWindow pw;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        pw.dismiss();
        return true;
    }
}
