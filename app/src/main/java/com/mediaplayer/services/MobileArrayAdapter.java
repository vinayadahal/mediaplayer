package com.mediaplayer.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaplayer.R;

import java.util.List;

public class MobileArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> values;
    private final List<Bitmap> thumb;

    public MobileArrayAdapter(Context context, List<String> values, List<Bitmap> thumb) {
        super(context, R.layout.thumb_list, values);
        this.context = context;
        this.values = values;
        this.thumb = thumb;
        System.out.println("Constructor called>>>>>");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("getView Called......");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.thumb_list, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.th_text_view);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values.get(position));

//        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
//        System.out.println("thumbnail::::  " + thumb.get(position));


        imageView.setImageBitmap(thumb.get(position));
        // Change icon based on name
        String s = values.get(position);

        System.out.println("Sdata;;;;;;" + s);

//        if (s.equals("WindowsMobile")) {
//            imageView.setImageResource(R.drawable.windowsmobile_logo);
//        } else if (s.equals("iOS")) {
//            imageView.setImageResource(R.drawable.ios_logo);
//        } else if (s.equals("Blackberry")) {
//            imageView.setImageResource(R.drawable.blackberry_logo);
//        } else {
//            imageView.setImageResource(R.drawable.android_logo);
//        }

        return rowView;
    }
}