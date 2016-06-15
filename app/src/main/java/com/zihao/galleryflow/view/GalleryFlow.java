package com.zihao.galleryflow.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

/**
 * GalleryFlow
 * Created by zihao on 2016/6/15 16:08.
 */
public class GalleryFlow extends Gallery{

    private static final String TAG = "GalleryFlow";

    private Camera mCamera = new Camera();// 相机类用于3D转换矩阵
    private int mMaxRotationAngle = 180;// 最大旋转角度
    private int mMaxZoom = -120;// 最大缩放值
    private int mCoveflowCenter = 0;// 半径值
    private boolean mCanSelected = false;// 标识选中区域是否高亮显示

    public GalleryFlow(Context context) {
        this(context, null);
        this.setStaticTransformationsEnabled(true);// 支持转换
        this.setChildrenDrawingOrderEnabled(true);// 启用设置children绘画顺序
    }

    public GalleryFlow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.setStaticTransformationsEnabled(true);// 支持转换
        this.setChildrenDrawingOrderEnabled(true);// 启用设置children绘画顺序
    }

    @SuppressWarnings("deprecation")
    public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStaticTransformationsEnabled(true);// 支持转换
        this.setChildrenDrawingOrderEnabled(true);// 启用设置children绘画顺序
    }

    public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }

    public void setMaxRotationAngle(int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }

    public int getMaxZoom() {
        return mMaxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        mMaxZoom = maxZoom;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int selectedIndex = getSelectedItemPosition()
                - getFirstVisiblePosition();// 当前选定索引
        if (selectedIndex < 0) {
            return i;
        }

        if (i < selectedIndex) {
            return i;
        } else if (i >= selectedIndex) {
            return childCount - 1 - i + selectedIndex;
        } else {
            return i;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        Log.d(TAG, "onFling:" + velocityX);
        if (Math.abs(velocityX) > 3000) // 限制最大滚动速度
            velocityX = velocityX > 0 ? 3000 : -3000;
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        Log.d(TAG, "onScroll");
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    /**
     * 控制gallery中每个图片的旋转(重写的gallery中方法)
     * @return boolean
     */
    @SuppressWarnings("deprecation")
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        super.getChildStaticTransformation(child, t);

        final int childCenter = getCenterOfView(child);// 取得当前子view的半径值
        final int childWidth = child.getWidth();

        int rotationAngle;// 旋转角度
        t.clear();// 重置转换状态
        t.setTransformationType(Transformation.TYPE_MATRIX);// 设置转换类型

        if (childCenter == mCoveflowCenter && mCanSelected) {// 如果图片位于中心位置不需要进行旋转
            transformImageBitmap(child, t, 0);
            child.setAlpha(1);
            invalidate();
        } else {
            // Calculate the rotation angle.
            // 根据图片在gallery中的位置来计算图片的旋转角度
            rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);

            if (Math.abs(rotationAngle) > mMaxRotationAngle) {// 使角度不大于最大值限制
                rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
                        : mMaxRotationAngle;
            }

            transformImageBitmap(child, t, rotationAngle);
            child.setAlpha(0.5f);
            invalidate();
        }

        return true;
    }

    private int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
                + getPaddingLeft();
    }

    private void transformImageBitmap(View child, Transformation t, int rotationAngle) {
        mCamera.save();// 对效果进行保存

        final Matrix imageMatrix = t.getMatrix();
        final int imageHeight = child.getHeight();// 图片高度
        final int imageWidth = child.getWidth();// 图片宽度

        if (!mCanSelected)
            rotationAngle = 360;

        final int rotation = Math.abs(rotationAngle);// 返回旋转角度的绝对值
        mCamera.translate(0, 0, mMaxZoom);// 缩放Z轴

        if (rotation < mMaxRotationAngle) {
            float zoomAmount = mMaxZoom + rotation * 1.5f;
            mCamera.translate(0, 0, zoomAmount);
        }

        if (Math.abs(rotationAngle) == mMaxRotationAngle) // 静止时，不让视图翻转
            rotationAngle = 360;

        mCamera.rotateY(rotationAngle);// 在Y轴上旋转相机
        // Get the matrix from the camera, in fact, the matrix is S (scale)
        // transformation.
        mCamera.getMatrix(imageMatrix);

        // The matrix final is T2 * S * T1, first translate the center point to
        // (0, 0),
        // then scale, and then translate the center point to its original
        // point.
        // T * S * T

        // S * T1
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
        // (T2 * S) * T1
        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));

        mCamera.restore();
    }

    /**
     * 设置是否高亮显示中间选中项
     *
     * @param canSelected
     *  // 选中区域是否高亮
     */
    public void setCanSelected(boolean canSelected) {
        this.mCanSelected = canSelected;
    }

}
