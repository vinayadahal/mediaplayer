package com.mediaplayer.components


import android.content.Context
import android.os.Handler
import android.widget.Toast

class MessageAlert {

    fun showToast(msg: String, ctx: Context) {
        val handler = Handler(ctx.mainLooper)
        handler.post { Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show() }
    }

}
