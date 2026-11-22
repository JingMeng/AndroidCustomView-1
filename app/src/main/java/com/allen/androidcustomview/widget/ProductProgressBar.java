package com.allen.androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.DecimalFormat;

/**
 * Created by Allen on 2017/11/30.
 * <p>
 * 浜у搧璐拱杩涘害鏉? */

public class ProductProgressBar extends View {


    private Paint bgPaint;
    private Paint progressPaint;

    private Paint textPaint;

    private int mWidth;
    private int mHeight;

    private int mViewHeight;
    /**
     * 杩涘害
     */
    private float mProgress;
    private float textHeight;
    private float textWidth;

    /**
     * 褰撳墠杩涘害
     */
    private float currentProgress;

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

    /**
     * 杩涘害鏉＄敾绗旂殑瀹藉害
     */
    private int progressPaintWidth;

    private int progressHeight;

    /**
     * 杩涘害鏉¤窛绂绘彁绀烘鐨勯珮搴?     */
    private int progressMarginTop;

    /**
     * 杩涘害绉诲姩鐨勮窛绂?     */
    private float moveDis;

    private Rect textRect = new Rect();

    private String textString = "宸插敭0%";
    /**
     * 鐧惧垎姣旀枃瀛楀瓧浣撳ぇ灏?     */
    private int textPaintSize;

    /**
     * 杩涘害鏉¤儗鏅鑹?     */
    private int bgColor = 0xFFeaeef0;
    /**
     * 杩涘害鏉￠鑹?     */
    private int progressColor = 0xFFf66b12;


    private RectF bgRectF = new RectF();
    private RectF progressRectF = new RectF();

    /**
     * 鍦嗚鐭╁舰鐨勫渾瑙掑崐寰?     */
    private int roundRectRadius;

    /**
     * 杩涘害鐩戝惉鍥炶皟
     */
    private ProgressListener progressListener;

    public ProductProgressBar(Context context) {
        this(context, null);
    }

    public ProductProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initPaint();
        initTextPaint();
    }

    /**
     * 鍒濆鍖栫敾绗斿搴﹀強view澶у皬
     */
    private void init() {
        progressPaintWidth = dp2px(1);
        progressHeight = dp2px(3);
        roundRectRadius = dp2px(3);
        textPaintSize = sp2px(10);
        textHeight = dp2px(10);
        progressMarginTop = dp2px(4);

        //view鐪熷疄鐨勯珮搴?        mViewHeight = (int) (textHeight + progressMarginTop + progressPaintWidth * 2 + progressHeight);
    }


    private void initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.FILL);
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.FILL);
    }

    /**
     * 鍒濆鍖栨枃瀛楃敾绗?     */
    private void initTextPaint() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textPaintSize);
        textPaint.setColor(progressColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
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

        //缁樺埗鏂囧瓧
        drawText(canvas, textString);
        //鑳屾櫙
        drawBgProgress(canvas);
        //杩涘害鏉?        drawProgress(canvas);
    }

    private void drawBgProgress(Canvas canvas) {
        bgRectF.left = 0;
        bgRectF.top = textHeight + progressMarginTop;
        bgRectF.right = this.getMeasuredWidth();
        bgRectF.bottom = bgRectF.top + progressHeight;
        canvas.drawRoundRect(bgRectF, roundRectRadius, roundRectRadius, bgPaint);
    }

    private void drawProgress(Canvas canvas) {
        progressRectF.left = 0;
        progressRectF.top = textHeight + progressMarginTop;
        progressRectF.right = currentProgress;
        progressRectF.bottom = progressRectF.top + progressHeight;
        canvas.drawRoundRect(progressRectF, roundRectRadius, roundRectRadius, progressPaint);
    }


    /**
     * 缁樺埗鏂囧瓧
     *
     * @param canvas 鐢诲竷
     */
    private void drawText(Canvas canvas, String textString) {
        textRect.left = (int) moveDis;
        textRect.top = 0;
        textRect.right = (int) (textPaint.measureText(textString) + moveDis);
        textRect.bottom = (int) textHeight;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //鏂囧瓧缁樺埗鍒版暣涓竷灞€鐨勪腑蹇冧綅缃?        canvas.drawText(textString, textRect.centerX(), baseline, textPaint);
    }


    /**
     * 杩涘害绉诲姩鍔ㄧ敾  閫氳繃鎻掑€肩殑鏂瑰紡鏀瑰彉绉诲姩鐨勮窛绂?     */
    private void initAnimation() {
        progressAnimator = ValueAnimator.ofFloat(0, mProgress);
        progressAnimator.setDuration(duration);
        progressAnimator.setStartDelay(startDelay);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                textString = "宸插敭" + formatNum((int) value) + "%";
                textWidth = textPaint.measureText(textString);
                currentProgress = value * mWidth / 100;
                if (progressListener != null) {
                    progressListener.currentProgressListener(value);
                }
                //绉诲姩鐧惧垎姣旀彁绀烘锛屽彧鏈夊綋鍓嶈繘搴﹀埌鎻愮ず妗嗕腑闂翠綅缃箣鍚庡紑濮嬬Щ鍔紝褰撹繘搴︽绉诲姩鍒版渶鍙宠竟鐨勬椂鍊欏仠姝㈢Щ鍔紝浣嗘槸杩涘害鏉¤繕鍙互缁х画绉诲姩
                if (currentProgress >= textWidth && currentProgress <= mWidth) {
                    moveDis = currentProgress - textWidth;
                }
                invalidate();
            }
        });
        if (!progressAnimator.isStarted()) {
            progressAnimator.start();
        }
    }

    /**
     * 鍥炶皟鎺ュ彛
     */
    public interface ProgressListener {
        void currentProgressListener(float currentProgress);
    }

    /**
     * 鍥炶皟鐩戝惉浜嬩欢
     *
     * @param listener
     * @return
     */
    public ProductProgressBar setProgressListener(ProgressListener listener) {
        progressListener = listener;
        return this;
    }

    public ProductProgressBar setProgress(float progress) {
        mProgress = progress;
        initAnimation();
        return this;
    }

    /**
     * 鏍煎紡鍖栨暟瀛?淇濈暀涓€浣嶅皬鏁?
     *
     * @param money
     * @return
     */
    public static String formatNum(int money) {
        DecimalFormat format = new DecimalFormat("0");
        return format.format(money);
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

