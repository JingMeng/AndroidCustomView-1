package com.allen.androidcustomview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * <pre>
 *      @author : xiaoyao
 *      e-mail  : xiaoyao@51vest.com
 *      date    : 2018/02/27
 *      desc    : 棣栭〉banner鎸囩ず鍣? *      version : 1.0
 * </pre>
 */

public class IndicatorView extends View {

    private int mWidth;
    private int mHeight;

    private float startX;

    /**
     * 鍔ㄧ敾璧风偣x鍧愭爣
     */
    private int centerX;

    /**
     * 鍔ㄧ敾璧风偣y鍧愭爣
     */
    private int centerY;

    /**
     * view鐪熷疄楂樺害
     */
    private int mViewHeight;

    private int mViewWidth;

    /**
     * 鐢荤瑪瀹藉害锛堢瓑浜巚ie楂樺害锛?     */
    private int paintWidth;

    private int radius;
    private int R;

    /**
     * 鍦嗙殑棰滆壊
     */
    private int circleBgColor = 0xFFCED3D6;

    /**
     * 褰撳墠鎸囩ず鍣ㄧ殑棰滆壊
     */
    private int currentColor = 0xFFA0946C;


    /**
     * 鍦嗙敾绗?     */
    private Paint circlePaint;
    /**
     * 褰撳墠鎸囩ず鍣ㄧ殑鐢荤瑪
     */
    private Paint indicatorPaint;


    private int pointNum = 0;
    private int currentIndex = 0;
    /**
     * 鍦嗕箣闂寸殑闂磋窛
     */
    private int dis;

    private RectF rectF;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rectF = new RectF();
        paintWidth = dp2px(1);
        mViewHeight = dp2px(8);
        radius = dp2px(3);
        R = 2 * radius;
        dis = 2 * radius;
        circlePaint = getPaint(paintWidth, circleBgColor, Paint.Style.FILL);
        indicatorPaint = getPaint(paintWidth, currentColor, Paint.Style.FILL);
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

        mViewWidth = (2 * pointNum + 1) * R;

        startX = (mWidth - mViewWidth) / 2;

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
        for (int i = 0; i < pointNum + 1; i++) {
            drawCircle(canvas, i);
        }
        drawCurrentIndicator(canvas, currentIndex);
    }


    private void drawCircle(Canvas canvas, int i) {
        float x = startX + radius + i * 2 * dis;
        canvas.drawCircle(x, centerY, radius, circlePaint);
    }


    private void drawCurrentIndicator(Canvas canvas, int i) {
        float x = startX + 2 * i * dis;
        rectF.left = x;
        rectF.top = centerY - radius;
        rectF.right = x + 3 * dis;
        rectF.bottom = centerY + radius;
        canvas.drawRoundRect(rectF, radius, radius, indicatorPaint);
    }


    public IndicatorView setPointNum(int pointNum) {
        this.pointNum = pointNum;
        return this;
    }

    public IndicatorView setCurrentPosition(int position) {
        currentIndex = position;
        invalidate();
        return this;
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

