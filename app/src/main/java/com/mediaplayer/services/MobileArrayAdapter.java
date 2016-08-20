package com.mediaplayer.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaplayer.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class MobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> videos;
    private final List<String> thumb;
    private final List<Long> time;
    private final List<Long> filesize;

    public MobileArrayAdapter(Context context, List<String> videos, List<String> thumb, List<Long> time, List<Long> filesize) {
        super(context, R.layout.thumb_list, videos);
        this.context = context;
        this.videos = videos;
        this.thumb = thumb;
        this.time = time;
        this.filesize = filesize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.thumb_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.th_text_view);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView time_text = (TextView) rowView.findViewById(R.id.time_text);
        TextView text_file_size = (TextView) rowView.findViewById(R.id.text_file_size);
        textView.setText(new File(videos.get(position)).getName());
        time_text.setText(new MathService().timeFormatter(time.get(position)));
        text_file_size.setText(new MathService().convertFileSize(filesize.get(position)));
        Bitmap bitmap = null;
        File f = new File(thumb.get(position));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
        return rowView;
    }
}