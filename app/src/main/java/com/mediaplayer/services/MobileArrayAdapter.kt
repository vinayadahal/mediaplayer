package com.mediaplayer.services

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.mediaplayer.R

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class MobileArrayAdapter(context: Context, private val videos: List<String>, private val thumb: List<String>, private val time: List<Long>, private val filesize: List<Long>) : ArrayAdapter<String>(context, R.layout.thumb_list, videos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(R.layout.thumb_list, parent, false)
        val textView = rowView.findViewById<View>(R.id.th_text_view) as TextView
        val imageView = rowView.findViewById<View>(R.id.icon) as ImageView
        val time_text = rowView.findViewById<View>(R.id.time_text) as TextView
        val text_file_size = rowView.findViewById<View>(R.id.text_file_size) as TextView
        textView.text = File(videos[position]).name
        time_text.text = MathService.timeFormatter(time[position])
        text_file_size.text = MathService.convertFileSize(filesize[position])
        loadImageInThread(position, imageView)
        return rowView
    }

    fun loadImageInThread(position: Int, imageView: ImageView) {
        var bitmap: Bitmap?
        if (thumb[position] != null) {
            val f = File(thumb[position])
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            try {
                bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            } catch (e: FileNotFoundException) {
                bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.play_circle_outline)
                println("File not found exception:::: $e")
            }

        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.play_circle_outline)
        }
        imageView.setImageBitmap(bitmap)
    }
}