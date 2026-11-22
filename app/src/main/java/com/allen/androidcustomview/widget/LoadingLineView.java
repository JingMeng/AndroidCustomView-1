package com.allen.androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Allen on 2018/1/31.
 * <p>
 * 绾挎€у姞杞藉姩鐢? */

public class LoadingLineView extends View {

    private int mWidth;
    private int mHeight;

    /**
     * 鍔ㄧ敾璧风偣x鍧愭爣
     */
    private int centerX;
    /**
     * 鍔ㄧ敾璧风偣y鍧愭爣
     */
    private int centerY;

    /**
     * 鍋忕Щ璺濈
     */
    private float dis;

    /**
     * view鐪熷疄楂樺害
     */
    private int mViewHeight;

    /**
     * 鑳屾櫙鑹茬敾绗?     */
    private Paint bgPaint;
    /**
     * loading鐢荤瑪
     */
    private Paint loadingPaint;

    /**
     * 鐢荤瑪瀹藉害锛堢瓑浜巚ie楂樺害锛?     */
    private int paintWidth;

    /**
     * 搴曡壊
     */
    private int bgColor = 0xFFe1e5e8;

    /**
     * loading棰滆壊
     */
    private int loadingColor = 0xFFf66b12;


    /**
     * 鍔ㄧ敾
     */
    private ValueAnimator loadingAnimator;
    /**
     * 鍔ㄧ敾鎵ц鏃堕棿
     */
    private int duration = 800;
    /**
     * 鍔ㄧ敾寤舵椂鍚姩鏃堕棿
     */
    private int startDelay = 0;

    /**
     * 鏄惁鍋滄鍔ㄧ敾锛堟仮澶嶅垵濮嬬姸鎬侊級
     */
    private boolean isStopAnimation = false;

    public LoadingLineView(Context context) {
        this(context, null);
    }

    public LoadingLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAtt(attrs);
        init();
    }

    /**
     * 鎯冲疄鐜伴鑹茬瓑鍙傛暟鍙厤缃紝鍦ㄨ繖閲屽疄鐜伴厤缃嵆鍙?绗旇€呭氨涓嶅鍐欎簡
     *
     * @param attrs attrs
     */
    private void getAtt(AttributeSet attrs) {

    }

    private void init() {
        paintWidth = dp2px(2);
        mViewHeight = paintWidth;
        bgPaint = getPaint(paintWidth, bgColor, Paint.Style.FILL);
        loadingPaint = getPaint(paintWidth, loadingColor, Paint.Style.FILL);
    }


    /**
     * 缁熶竴澶勭悊paint
     *
     * @param strokeWidth 鐢荤瑪瀹藉害
     * @param color       棰滆壊
     * @param style       椋庢牸
     * @return paint
     */
    private Paint getPaint(int strokeWidth, int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStyle(style);
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;

        centerX = w / 2;
        centerY = h / 2;

        initLoadingAnimation();
        startLoading();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(measureWidth(widthMode, width), measureHeight(heightMode, height));
    }

    /**
     * 娴嬮噺瀹藉害
     *
     * @param mode
     * @param width
     * @return
     */
    private int measureWidth(int mode, int width) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
        }
        return mWidth;
    }

    /**
     * 娴嬮噺楂樺害
     *
     * @param mode
     * @param height
     * @return
     */
    private int measureHeight(int mode, int height) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                mHeight = mViewHeight;
                break;
            case MeasureSpec.EXACTLY:
                mHeight = height;
                break;
        }
        return mHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 鐢诲簳鑹?         */
        canvas.drawLine(0, centerY, mWidth, centerY, bgPaint);

        if (!isStopAnimation) {
            /**
             * loading鍚戝乏鎵╂暎
             */
            canvas.drawLine(centerX, centerY, centerX - dis, centerY, loadingPaint);
            /**
             * loading鍚戝彸鎵╂暎
             */
            canvas.drawLine(centerX, centerY, centerX + dis, centerY, loadingPaint);
        }

    }

    /**
     * 鍒濆鍖栧姩鐢?     */
    private void initLoadingAnimation() {
        final float loadingMoveDistance = mWidth / 2;
        loadingAnimator = ValueAnimator.ofFloat(0, loadingMoveDistance);
        loadingAnimator.setDuration(duration);
        loadingAnimator.setStartDelay(startDelay);
        loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnimator.setInterpolator(new LinearInterpolator());
        loadingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                dis = value;
                if (value <= loadingMoveDistance / 2) {
                    loadingPaint.setAlpha((int) ((255 * value) * 2 / loadingMoveDistance));
                } else {
                    loadingPaint.setAlpha((int) (255 - (255 * value) * 2 / loadingMoveDistance));
                }
                invalidate();
            }
        });
    }

    /**
     * 寮€鍚姩鐢?     */
    public void startLoading() {
        if (loadingAnimator != null) {
            loadingAnimator.start();
        }
        isStopAnimation = false;
    }

    /**
     * 缁撴潫鍔ㄧ敾
     */
    public void stopLoading() {
        if (loadingAnimator != null) {
            loadingAnimator.cancel();
        }
        isStopAnimation = true;
        invalidate();
    }


    /**
     * dp 2 px
     *
     * @param dpVal dp
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}

