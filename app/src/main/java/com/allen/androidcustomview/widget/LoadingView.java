package com.allen.androidcustomview.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Allen on 2017/12/4.
 * <p>
 * 鍔犺浇view甯﹀姩鐢? */

public class LoadingView extends View {

    private int mWidth;
    private int mHeight;

    private int centerX;
    private int centerY;

    private float radius;

    private Paint bgCirclePaint;
    private Paint arcPaint;
    private Paint okPaint;

    private int paintWidth;

    /**
     * 缁樺埗鍦嗗姬鍖哄煙
     */
    private RectF rectF = new RectF();

    /**
     * 榛樿鍦嗚儗鏅鑹?     */
    private int bgColor = 0xFFe1e5e8;
    /**
     * 鍦嗗姬杩涘害鏉￠鑹?     */
    private int progressColor = 0xFFf66b12;

    /**
     * 鍔ㄧ敾鎵ц鏃堕棿
     */
    private int duration = 800;
    /**
     * 鍔ㄧ敾寤舵椂鍚姩鏃堕棿
     */
    private int startDelay = 0;

    /**
     * 鍦嗗姬寮€濮嬪亸绉昏搴?     */
    private float startAngle = 0;
    /**
     * 鍋忕Щ闀垮害
     */
    private float sweepAngle = 20;

    /**
     * 鍦嗗姬杞湀
     */
    private ValueAnimator animatorDrawLoading;

    /**
     *
     */
    private ValueAnimator animatorDrawArcToCircle;
    /**
     * 缁樺埗瀵瑰嬀锛堚垰锛夌殑鍔ㄧ敾
     */
    private ValueAnimator animatorDrawOk;
    /**
     * 璺緞--鐢ㄦ潵鑾峰彇瀵瑰嬀鐨勮矾寰?     */
    private Path path = new Path();
    /**
     * 鍙栬矾寰勭殑闀垮害
     */
    private PathMeasure pathMeasure;
    /**
     * 瀵硅矾寰勫鐞嗗疄鐜扮粯鍒跺姩鐢绘晥鏋?     */
    private PathEffect effect;


    /**
     * 鏄惁寮€濮嬬粯鍒跺鍕?     */
    private boolean startDrawOk = false;

    /**
     * 鍔ㄧ敾闆?     */
    private AnimatorSet animatorSet = new AnimatorSet();


    /**
     * 鐐瑰嚮浜嬩欢鍙婂姩鐢讳簨浠?瀹屾垚鍥炶皟
     */
    private LoadingViewListener loadingViewListener;

    public void setLoadingViewListener(LoadingViewListener loadingViewListener) {
        this.loadingViewListener = loadingViewListener;
    }


    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paintWidth = dp2px(3);
        initPaint();
        initAnimatorSet();
    }

    private void initPaint() {
        bgCirclePaint = getPaint(paintWidth, bgColor, Paint.Style.STROKE);
        arcPaint = getPaint(paintWidth, progressColor, Paint.Style.STROKE);
        okPaint = getPaint(paintWidth, progressColor, Paint.Style.STROKE);
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

        radius = Math.min(w, h) / 2 - paintWidth;

        rectF.left = centerX - radius;
        rectF.top = centerY - radius;
        rectF.right = centerX + radius;
        rectF.bottom = centerY + radius;

        initOk();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, radius, bgCirclePaint);
        canvas.drawArc(rectF, startAngle, sweepAngle, false, arcPaint);
        if (startDrawOk) {
            canvas.drawPath(path, okPaint);
        }
    }

    /**
     * 缁樺埗瀵瑰嬀
     */
    private void initOk() {
        //瀵瑰嬀鐨勮矾寰?        path.moveTo(mWidth / 8 * 3, mHeight / 2);
        path.lineTo(mWidth / 2, mHeight / 5 * 3);
        path.lineTo(mWidth / 3 * 2, mHeight / 5 * 2);

        pathMeasure = new PathMeasure(path, true);
    }

    /**
     * 鍒濆鍖栧姬搴﹁浆鍦堢殑鍔ㄧ敾
     */
    private void initLoadingAnimation() {
        animatorDrawLoading = ValueAnimator.ofFloat(0, 360);
        animatorDrawLoading.setDuration(duration);
        animatorDrawLoading.setStartDelay(startDelay);
        animatorDrawLoading.setRepeatCount(2);
        animatorDrawLoading.setInterpolator(new LinearInterpolator());
        animatorDrawLoading.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                startAngle = value;
                if (startAngle <= 180) {
                    sweepAngle = sweepAngle + 5;
                } else {
                    sweepAngle = sweepAngle - 5;
                }
                invalidate();
            }
        });
    }

    /**
     * 鍒濆鍖栧姬搴﹀彉鍦嗗姩鐢?     */
    private void initArcToCircleAnimation() {
        animatorDrawArcToCircle = ValueAnimator.ofFloat(sweepAngle, 360);
        animatorDrawArcToCircle.setDuration(duration);
        animatorDrawArcToCircle.setStartDelay(0);
        animatorDrawArcToCircle.setInterpolator(new LinearInterpolator());
        animatorDrawArcToCircle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                sweepAngle = value;
                invalidate();
            }
        });
    }


    /**
     * 缁樺埗瀵瑰嬀鐨勫姩鐢?     */
    private void initOkAnimation() {
        animatorDrawOk = ValueAnimator.ofFloat(1, 0);
        animatorDrawOk.setDuration(duration);
        animatorDrawOk.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startDrawOk = true;
                float value = (Float) animation.getAnimatedValue();

                effect = new DashPathEffect(new float[]{pathMeasure.getLength(), pathMeasure.getLength()}, value * pathMeasure.getLength());
                okPaint.setPathEffect(effect);
                invalidate();
            }
        });
    }

    /**
     * 鍒濆鍖栨墍鏈夊姩鐢?     */
    private void initAnimatorSet() {
        initLoadingAnimation();
        initArcToCircleAnimation();
        initOkAnimation();
        animatorSet
                .play(animatorDrawArcToCircle)
                .before(animatorDrawOk)
                .after(animatorDrawLoading);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (loadingViewListener != null) {
                    loadingViewListener.animationFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    /**
     * 浼犲叆涓€涓繘搴﹀€硷紝浠?鍒皃rogress鍔ㄧ敾鍙樺寲
     */
    public LoadingView startAnimation() {
        startDrawOk = false;
        startAngle = 0;
        sweepAngle = 20;
        path = new Path();
        initOk();
        animatorSet.cancel();
        animatorSet.start();
        return this;
    }

    /**
     * 鎺ュ彛鍥炶皟
     */
    public interface LoadingViewListener {
        /**
         * 鍔ㄧ敾瀹屾垚鍥炶皟
         */
        void animationFinish();
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
}

