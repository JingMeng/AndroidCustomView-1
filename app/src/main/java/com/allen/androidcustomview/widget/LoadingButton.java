package com.allen.androidcustomview.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;

/**
 * Created by allen on 2017/5/23.
 * <p>
 * loading鎸夐挳
 */

public class LoadingButton extends Button {

    public int mWidth;
    public int mHeight;
    private RectF textRect = new RectF();

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    private static final int POINT_COLOR_1 = 0x4CFFFFFF;
    private static final int POINT_COLOR_2 = 0x7FFFFFFF;
    private static final int POINT_COLOR_3 = 0xFFFFFFFF;

    private int duration = 300;

    private float centerX;
    private float centerY;

    private float radius;


    private boolean isLoading = false;

    private int mLoadingIndex = 0;
    private Runnable mRunnable;


    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();

    }


    private void initPaint() {
        initTextPaint();
        circlePaint = getPaint(dp2px(1), Paint.Style.FILL);
        mRunnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    /**
     * 鍒濆鍖栨枃瀛楃敾绗?     */
    private void initTextPaint() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(sp2px(16));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = width;
        mHeight = height;

        centerX = mWidth / 2;
        centerY = mHeight / 2;

        radius = mHeight / 8;
        Log.d("allen", "onMeasure: " + centerX);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (isLoading) {
            drawLoading(canvas, mLoadingIndex);
            mLoadingIndex = (mLoadingIndex + 1) % 3;
            postDelayed(mRunnable, duration);

        } else {
            super.onDraw(canvas);
        }

    }

    private void drawLoading(Canvas canvas, int index) {
        if (index < 0 || index > 2)
            return;
        switch (index) {
            case 0:
                circlePaint.setColor(POINT_COLOR_1);
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_2);
                canvas.drawCircle(centerX, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_3);
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint);
                break;
            case 1:
                circlePaint.setColor(POINT_COLOR_3);
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_1);
                canvas.drawCircle(centerX, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_2);
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint);
                break;
            case 2:
                circlePaint.setColor(POINT_COLOR_2);
                canvas.drawCircle(centerX - radius * 4, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_3);
                canvas.drawCircle(centerX, centerY, radius, circlePaint);
                circlePaint.setColor(POINT_COLOR_1);
                canvas.drawCircle(centerX + radius * 4, centerY, radius, circlePaint);
                break;
        }

    }


    /**
     * 缁樺埗鏂囧瓧
     *
     * @param canvas 鐢诲竷
     */
    private void drawText(Canvas canvas, String textString) {
        textRect.set(0, 0, mWidth, mHeight);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (int) ((textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2);
        //鏂囧瓧缁樺埗鍒版暣涓竷灞€鐨勪腑蹇冧綅缃?        canvas.drawText(textString, textRect.centerX(), baseline, textPaint);
    }


    public void startLoading() {
        if (isLoading)
            return;
        isLoading = true;
        mLoadingIndex = 0;
        invalidate();
    }

    public void stopLoading() {
        if (!isLoading)
            return;
        isLoading = false;
    }

    public boolean isLoading() {
        return isLoading;
    }

    /**
     * 缁熶竴澶勭悊paint
     *
     * @param strokeWidth
     * @param style
     * @return
     */
    public Paint getPaint(int strokeWidth, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(style);
        return paint;
    }


    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

}

