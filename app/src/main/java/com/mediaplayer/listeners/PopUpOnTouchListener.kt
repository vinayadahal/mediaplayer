package com.mediaplayer.listeners


import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.widget.PopupWindow

class PopUpOnTouchListener : View.OnTouchListener {

    var pw: PopupWindow? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        pw!!.dismiss()
        return true
    }
}
