package com.zihao.galleryflow.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.zihao.galleryflow.R;

/**
 * CircleView
 * Created by zihao on 2016/6/15 16:27.
 */
public class CircleView  extends View {
    private String txt = "test";
    private int circleColor = Color.parseColor("#2aa1d9");
    private Paint paint;
    private Paint transPaint;
    private int alpha = 255;

    public CircleView(Context context)
    {
        this(context, null);
        initView();
    }

    public CircleView(Context context, AttributeSet attrs)
    {
        super(context, attrs, 0);
        initView();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        int textSize = getContext().getResources().getDimensionPixelSize(R.dimen.gallery_txt_size);
        paint.setTextSize(textSize);

        transPaint = new Paint();
        transPaint.setColor(Color.parseColor("#4dffffff"));
        transPaint.setAntiAlias(true);
        transPaint.setStyle(Paint.Style.STROKE);
        transPaint.setStrokeWidth(6);
    }

    public void setText(String text) {
        this.txt = text;
    }

    protected void onDraw(Canvas canvas)
    {
        int width = this.getWidth();
        int height = this.getHeight();

        int max = width > height ? width : height;

        int radius = max/2 - 10;
        canvas.drawCircle(width/2, height/2, radius, transPaint);

        paint.setColor(circleColor);
        paint.setAlpha(alpha);
        canvas.drawCircle(width/2, height/2, radius - 3, paint);

        int textSize = (int) (0.170886 * width);
        paint.setTextSize(textSize);
        paint.setColor(Color.WHITE);
        //paint.setAlpha(alpha);

        Paint.FontMetrics fm = paint.getFontMetrics();

        if(txt.contains(",")) {
            String[] strArray = txt.split(",");

            float textWidth = paint.measureText(strArray[0]);
            float textCenterVerticalBaselineY = height / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
            float textCenterX = (width - textWidth) / 2;

            canvas.drawText(strArray[0], textCenterX, textCenterVerticalBaselineY - (textSize/2), paint);

            textWidth = paint.measureText(strArray[1]);
            textCenterX = (width - textWidth) / 2;
            canvas.drawText(strArray[1], textCenterX, textCenterVerticalBaselineY + (textSize/2), paint);
        } else {
            float textWidth = paint.measureText(txt);
            float textCenterVerticalBaselineY = height / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
            float textCenterX = (width - textWidth) / 2;
            canvas.drawText(txt, textCenterX, textCenterVerticalBaselineY, paint);
        }
    }

    public void setBackgroundColor(int color) {
        circleColor = color;
        invalidate();
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = (int)(alpha*255);
        invalidate();
    }
}