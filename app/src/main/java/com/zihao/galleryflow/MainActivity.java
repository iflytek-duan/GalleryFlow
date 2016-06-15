package com.zihao.galleryflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.zihao.galleryflow.adapter.GalleryAdapter;
import com.zihao.galleryflow.view.GalleryFlow;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /** gallery */
    private GalleryFlow mSchemGallery;
    private  GalleryAdapter mGalleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGallery();
    }

    private void initGallery(){
        Display display = getWindowManager().getDefaultDisplay();
        int circleWidth = (int) (display.getWidth() / 6.8);
        int spacing = (int) ((1.8 / 4.5) * circleWidth);

        mSchemGallery = (GalleryFlow) findViewById(R.id.gallery_flow);
        mGalleryAdapter = new GalleryAdapter(circleWidth,this);
        mSchemGallery.setSpacing(spacing);
        mSchemGallery.setFadingEdgeLength(0);
        mSchemGallery.setAdapter(mGalleryAdapter);
        mSchemGallery.setOnItemClickListener(this);
        mSchemGallery.setGravity(Gravity.CENTER_VERTICAL);

        mSchemGallery.setSelection(5000 + 2);// 滚动到第5002位,方便左右互划
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mGalleryAdapter.updateOneView(position);
        mSchemGallery.setCanSelected(true);
    }
}
