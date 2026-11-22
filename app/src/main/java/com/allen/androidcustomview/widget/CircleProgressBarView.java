package com.allen.androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.allen.androidcustomview.R;

/**
 * Created by Allen on 2017/5/14.
 * <p>
 * 鑷畾涔夊渾褰㈣繘搴︽潯
 */

public class CircleProgressBarView extends View {

    private Context mContext;


    /**
     * 鍦嗗績x鍧愭爣
     */
    private float centerX;
    /**
     * 鍦嗗績y鍧愭爣
     */
    private float centerY;
    /**
     * 鍦嗙殑鍗婂緞
     */
    private float radius;

    /**
     * 杩涘害
     */
    private float mProgress;

    /**
     * 褰撳墠杩涘害
     */
    private float currentProgress;

    /**
     * 鍦嗗舰杩涘害鏉″簳鑹茬敾绗?     */
    private Paint circleBgPaint;
    /**
     * 鍦嗗舰杩涘害鏉¤繘搴︾敾绗?     */
    private Paint progressPaint;

    /**
     * 杩涘害鏉¤儗鏅鑹?     */
    private int circleBgColor = 0xFFe1e5e8;
    /**
     * 杩涘害鏉￠鑹?     */
    private int progressColor = 0xFFf66b12;

    /**
     * 榛樿鍦嗙幆鐨勫搴?     */
    private int defaultStrokeWidth = 10;
    /**
     * 鍦嗗舰鑳屾櫙鐢荤瑪瀹藉害
     */
    private int circleBgStrokeWidth = defaultStrokeWidth;
    /**
     * 鍦嗗舰杩涘害鐢荤瑪瀹藉害
     */
    private int progressStrokeWidth = defaultStrokeWidth;


    /**
     * 鎵囧舰鎵€鍦ㄧ煩褰?     */
    private RectF rectF = new RectF();

    /**
     * 杩涘害鍔ㄧ敾
     */
    private ValueAnimator progressAnimator;

    /**
     * 鍔ㄧ敾鎵ц鏃堕棿
     */
    private int duration = 1000;
    /**
     * 鍔ㄧ敾寤舵椂鍚姩鏃堕棿
     */
    private int startDelay = 500;

    private boolean isDrawCenterProgressText;
    private int centerProgressTextSize = 10;
    private int centerProgressTextColor = Color.BLACK;

    private Paint centerProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ProgressListener progressListener;

    public CircleProgressBarView(Context context) {
        this(context, null);
    }

    public CircleProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        getAttr(attrs);
        initPaint();
        initTextPaint();
    }

    private void getAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CircleProgressBarView);

        circleBgStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_circleBgStrokeWidth, defaultStrokeWidth);
        progressStrokeWidth = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_progressStrokeWidth, defaultStrokeWidth);

        circleBgColor = typedArray.getColor(R.styleable.CircleProgressBarView_circleBgColor, circleBgColor);
        progressColor = typedArray.getColor(R.styleable.CircleProgressBarView_progressColor, progressColor);

        duration = typedArray.getColor(R.styleable.CircleProgressBarView_circleAnimationDuration, duration);

        isDrawCenterProgressText = typedArray.getBoolean(R.styleable.CircleProgressBarView_isDrawCenterProgressText, false);

        centerProgressTextColor = typedArray.getColor(R.styleable.CircleProgressBarView_centerProgressTextColor, centerProgressTextColor);
        centerProgressTextSize = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressBarView_centerProgressTextSize, sp2px(centerProgressTextSize));

        typedArray.recycle();
    }

    private void initPaint() {
        circleBgPaint = getPaint(circleBgStrokeWidth, circleBgColor);

        progressPaint = getPaint(progressStrokeWidth, progressColor);

    }

    private Paint getPaint(int strokeWidth, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);

        return paint;
    }

    /**
     * 鍒濆鍖栨枃瀛楃敾绗?     */
    private void initTextPaint() {
        centerProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerProgressTextPaint.setTextSize(centerProgressTextSize);
        centerProgressTextPaint.setColor(centerProgressTextColor);
        centerProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        centerProgressTextPaint.setAntiAlias(true);
    }

    private void initAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        progressAnimator.setDuration(duration);
        progressAnimator.setStartDelay(startDelay);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                mProgress = value;
                currentProgress = value * 360 / 100;

                if (progressListener != null) {
                    progressListener.currentProgressListener(roundTwo(value));
                }
                invalidate();

            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w / 2;
        centerY = h / 2;

        radius = Math.min(w, h) / 2 - Math.max(circleBgStrokeWidth, progressStrokeWidth);

        rectF.set(centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(centerX, centerY, radius, circleBgPaint);
        canvas.drawArc(rectF, 90, currentProgress, false, progressPaint);
        if (isDrawCenterProgressText) {
            drawCenterProgressText(canvas, (int) mProgress + "%");
        }
    }

    private void drawCenterProgressText(Canvas canvas, String currentProgress) {
        Paint.FontMetricsInt fontMetrics = centerProgressTextPaint.getFontMetricsInt();
        int baseline = (int) ((rectF.bottom + rectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
        //鏂囧瓧缁樺埗鍒版暣涓竷灞€鐨勪腑蹇冧綅缃?        canvas.drawText(currentProgress, rectF.centerX(), baseline, centerProgressTextPaint);
    }


    public void startProgressAnimation() {
        progressAnimator.start();
    }

    public void pauseProgressAnimation() {
        progressAnimator.pause();
    }

    public void resumeProgressAnimation() {
        progressAnimator.resume();
    }

    public void stopProgressAnimation() {
        progressAnimator.end();
    }


    /**
     * 浼犲叆涓€涓繘搴﹀€硷紝浠?鍒皃rogress鍔ㄧ敾鍙樺寲
     *
     * @param progress
     * @return
     */
    public CircleProgressBarView setProgressWithAnimation(float progress) {
        mProgress = progress;
        initAnimation();
        return this;
    }

    /**
     * 瀹炴椂杩涘害锛岄€傜敤浜庝笅杞借繘搴﹀洖璋冩椂鍊欎箣绫荤殑鍦烘櫙
     *
     * @param progress
     * @return
     */
    public CircleProgressBarView setCurrentProgress(float progress) {
        mProgress = progress;
        currentProgress = progress * 360 / 100;
        invalidate();
        return this;
    }


    public interface ProgressListener {
        void currentProgressListener(float currentProgress);
    }

    public CircleProgressBarView setProgressListener(ProgressListener listener) {
        progressListener = listener;
        return this;
    }

    /**
     * 灏嗕竴涓皬鏁板洓鑸嶄簲鍏ワ紝淇濈暀涓や綅灏忔暟杩斿洖
     *
     * @param originNum
     * @return
     */
    public static float roundTwo(float originNum) {
        return (float) (Math.round(originNum * 10) / 10.00);
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

