package com.allen.androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.text.DecimalFormat;

/**
 * Created by Allen on 2017/5/14.
 * <p>
 * 鑷畾涔夋按骞宠繘搴︽潯
 */

public class HorizontalProgressBar extends View {

    private Paint bgPaint;
    private Paint progressPaint;

    private Paint tipPaint;
    private Paint textPaint;

    private int mWidth;
    private int mHeight;
    private int mViewHeight;
    /**
     * 杩涘害
     */
    private float mProgress;

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

    /**
     * 鐧惧垎姣旀彁绀烘鐢荤瑪鐨勫搴?     */
    private int tipPaintWidth;

    /**
     * 鐧惧垎姣旀彁绀烘鐨勯珮搴?     */
    private int tipHeight;

    /**
     * 鐧惧垎姣旀彁绀烘鐨勫搴?     */
    private int tipWidth;

    /**
     * 鐢讳笁瑙掑舰鐨刾ath
     */
    private Path path = new Path();
    /**
     * 涓夎褰㈢殑楂?     */
    private int triangleHeight;
    /**
     * 杩涘害鏉¤窛绂绘彁绀烘鐨勯珮搴?     */
    private int progressMarginTop;

    /**
     * 杩涘害绉诲姩鐨勮窛绂?     */
    private float moveDis;

    private Rect textRect = new Rect();
    private String textString = "0";
    /**
     * 鐧惧垎姣旀枃瀛楀瓧浣撳ぇ灏?     */
    private int textPaintSize;

    /**
     * 杩涘害鏉¤儗鏅鑹?     */
    private int bgColor = 0xFFe1e5e8;
    /**
     * 杩涘害鏉￠鑹?     */
    private int progressColor = 0xFFf66b12;

    /**
     * 缁樺埗鎻愮ず妗嗙殑鐭╁舰
     */
    private RectF rectF = new RectF();

    /**
     * 鍦嗚鐭╁舰鐨勫渾瑙掑崐寰?     */
    private int roundRectRadius;

    /**
     * 杩涘害鐩戝惉鍥炶皟
     */
    private ProgressListener progressListener;

    public HorizontalProgressBar(Context context) {
        super(context);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initPaint();
    }

    /**
     * 鍒濆鍖栫敾绗斿搴﹀強view澶у皬
     */
    private void init() {
        progressPaintWidth = dp2px(4);
        tipHeight = dp2px(15);
        tipWidth = dp2px(30);
        tipPaintWidth = dp2px(1);
        triangleHeight = dp2px(3);
        roundRectRadius = dp2px(2);
        textPaintSize = sp2px(10);
        progressMarginTop = dp2px(8);

        //view鐪熷疄鐨勯珮搴?        mViewHeight = tipHeight + tipPaintWidth + triangleHeight + progressPaintWidth + progressMarginTop;
    }

    /**
     * 鍒濆鍖栫敾绗?     */
    private void initPaint() {
        bgPaint = getPaint(progressPaintWidth, bgColor, Paint.Style.STROKE);
        progressPaint = getPaint(progressPaintWidth, progressColor, Paint.Style.STROKE);
        tipPaint = getPaint(tipPaintWidth, progressColor, Paint.Style.FILL);

        initTextPaint();
    }

    /**
     * 鍒濆鍖栨枃瀛楃敾绗?     */
    private void initTextPaint() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textPaintSize);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    /**
     * 缁熶竴澶勭悊paint
     *
     * @param strokeWidth
     * @param color
     * @param style
     * @return
     */
    private Paint getPaint(int strokeWidth, int color, Paint.Style style) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
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

        canvas.drawLine(getPaddingLeft(),
                tipHeight + progressMarginTop,
                getWidth(),
                tipHeight + progressMarginTop,
                bgPaint);

        canvas.drawLine(getPaddingLeft(),
                tipHeight + progressMarginTop,
                currentProgress,
                tipHeight + progressMarginTop,
                progressPaint);

        drawTipView(canvas);
        drawText(canvas, textString);

    }

    /**
     * 缁樺埗杩涘害涓婅竟鎻愮ず鐧惧垎姣旂殑view
     *
     * @param canvas
     */
    private void drawTipView(Canvas canvas) {
        drawRoundRect(canvas);
        drawTriangle(canvas);
    }


    /**
     * 缁樺埗鍦嗚鐭╁舰
     *
     * @param canvas
     */
    private void drawRoundRect(Canvas canvas) {
        rectF.set(moveDis, 0, tipWidth + moveDis, tipHeight);
        canvas.drawRoundRect(rectF, roundRectRadius, roundRectRadius, tipPaint);
    }

    /**
     * 缁樺埗涓夎褰?     *
     * @param canvas
     */
    private void drawTriangle(Canvas canvas) {
        path.moveTo(tipWidth / 2 - triangleHeight + moveDis, tipHeight);
        path.lineTo(tipWidth / 2 + moveDis, tipHeight + triangleHeight);
        path.lineTo(tipWidth / 2 + triangleHeight + moveDis, tipHeight);
        canvas.drawPath(path, tipPaint);
        path.reset();

    }

    /**
     * 缁樺埗鏂囧瓧
     *
     * @param canvas 鐢诲竷
     */
    private void drawText(Canvas canvas, String textString) {
        textRect.left = (int) moveDis;
        textRect.top = 0;
        textRect.right = (int) (tipWidth + moveDis);
        textRect.bottom = tipHeight;
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //鏂囧瓧缁樺埗鍒版暣涓竷灞€鐨勪腑蹇冧綅缃?        canvas.drawText(textString + "%", textRect.centerX(), baseline, textPaint);
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
                //杩涘害鏁板€煎彧鏄剧ず鏁存暟锛屾垜浠嚜宸辩殑闇€姹傦紝鍙互蹇界暐
                textString = formatNum(format2Int(value));
                //鎶婂綋鍓嶇櫨鍒嗘瘮杩涘害杞寲鎴恦iew瀹藉害瀵瑰簲鐨勬瘮渚?                currentProgress = value * mWidth / 100;
                //杩涘害鍥炶皟鏂规硶
                if (progressListener != null) {
                    progressListener.currentProgressListener(value);
                }
                //绉诲姩鐧惧垎姣旀彁绀烘锛屽彧鏈夊綋鍓嶈繘搴﹀埌鎻愮ず妗嗕腑闂翠綅缃箣鍚庡紑濮嬬Щ鍔紝
                //褰撹繘搴︽绉诲姩鍒版渶鍙宠竟鐨勬椂鍊欏仠姝㈢Щ鍔紝浣嗘槸杩涘害鏉¤繕鍙互缁х画绉诲姩
                //moveDis鏄痶ip妗嗙Щ鍔ㄧ殑璺濈
                if (currentProgress >= (tipWidth / 2) &&
                        currentProgress <= (mWidth - tipWidth / 2)) {
                    moveDis = currentProgress - tipWidth / 2;
                }
                invalidate();
            }
        });
        progressAnimator.start();
    }


    /**
     * 璁剧疆杩涘害鏉″甫鍔ㄧ敾鏁堟灉
     *
     * @param progress
     * @return
     */
    public HorizontalProgressBar setProgressWithAnimation(float progress) {
        mProgress = progress;
        initAnimation();
        return this;
    }

    /**
     * 瀹炴椂鏄剧ず杩涘害
     *
     * @param progress
     * @return
     */
    public HorizontalProgressBar setCurrentProgress(float progress) {
        mProgress = progress;
        currentProgress = progress * mWidth / 100;
        textString = formatNum(format2Int(progress));
        invalidate();
        return this;
    }

    /**
     * 寮€鍚姩鐢?     */
    public void startProgressAnimation() {
        if (progressAnimator != null &&
                !progressAnimator.isRunning() &&
                !progressAnimator.isStarted())
            progressAnimator.start();
    }

    /**
     * 鏆傚仠鍔ㄧ敾
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void pauseProgressAnimation() {
        if (progressAnimator != null) {
            progressAnimator.pause();
        }
    }

    /**
     * 鎭㈠鍔ㄧ敾
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resumeProgressAnimation() {
        if (progressAnimator != null)
            progressAnimator.resume();
    }

    /**
     * 鍋滄鍔ㄧ敾
     */
    public void stopProgressAnimation() {
        if (progressAnimator != null) {
            progressAnimator.end();
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
    public HorizontalProgressBar setProgressListener(ProgressListener listener) {
        progressListener = listener;
        return this;
    }

    /**
     * 鏍煎紡鍖栨暟瀛?淇濈暀涓や綅灏忔暟)
     *
     * @param money
     * @return
     */
    public static String formatNumTwo(double money) {
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(money);
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

    public static int format2Int(double i) {
        return (int) i;
    }
}

