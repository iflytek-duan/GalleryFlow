package com.zihao.galleryflow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;

/**
 * GalleryAdapter
 * Created by zihao on 2016/6/15 16:25.
 */
public class GalleryAdapter extends BaseAdapter{
    private String[] text;
    private int selectedSchema = -1;
    private int circleWidth;

    public GalleryAdapter(int circleWidth, Context context) {
        this.circleWidth = circleWidth;
        text = new String[] { "1","2","3","4","5" };
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position) {
        return text[position % text.length].replace(",", "");
    }

    @Override
    public long getItemId(int position) {
        return position % text.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = new CircleView(parent.getContext());
            convertView.setLayoutParams(new Gallery.LayoutParams(circleWidth,
                    circleWidth));
        }

        CircleView circleView = (CircleView) convertView;
        circleView.setText(text[position % text.length]);

        if (selectedSchema == position % text.length)
            circleView.setBackgroundColor(Color.parseColor("#ff5d3c"));

        return convertView;
    }

    public void updateOneView(int position) {
        selectedSchema = position % text.length;
        notifyDataSetChanged();
    }
}
