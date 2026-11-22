package com.allen.androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by xiaoyao on 2017/9/7.
 * 浠縌Q娑堟伅绾㈢偣鎷栨嫿鏁堟灉view
 */

public class DragBallView extends View {

    private Paint circlePaint;
    private Paint textPaint;

    private int circleColor = Color.RED;
    private float radiusStart;
    private float radiusEnd;

    private Path path;

    private int startX;
    private int startY;

    private boolean mIsCanDrag = false;
    private boolean isOutOfRang = false;
    //鏈€缁堝渾鏄惁娑堝け
    private boolean disappear = false;

    private float maxDistance;

    private PointF pointA;
    private PointF pointB;
    private PointF pointC;
    private PointF pointD;
    private PointF pointO;

    private PointF pointStart;
    private PointF pointEnd;

    private float currentRadiusStart;
    private float currentRadiusEnd;

    private Rect textRect = new Rect();
    private int msgCount = 0;

    private OnDragBallListener onDragBallListener;

    public DragBallView(Context context) {
        this(context, null);
    }

    public DragBallView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initPoint();

    }

    /**
     * 鍒濆鍖栨墍鏈夌偣
     */
    private void initPoint() {
        pointStart = new PointF(startX, startY);

        pointEnd = new PointF(startX, startY);

        pointA = new PointF();
        pointB = new PointF();
        pointC = new PointF();
        pointD = new PointF();

        pointO = new PointF();

    }

    /**
     * 鍒濆鍖栫敾绗?     */
    private void initPaint() {

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        path = new Path();
        initTextPaint();
    }

    /**
     * 鍒濆鍖栨枃瀛楃敾绗?     */
    private void initTextPaint() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(sp2px(13));
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = w / 2;
        startY = h / 2;
        maxDistance = dp2px(100);
        radiusStart = dp2px(15);
        radiusEnd = dp2px(15);

        currentRadiusEnd = radiusEnd;
        currentRadiusStart = radiusStart;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pointStart.set(startX, startY);
        if (isOutOfRang) {
            if (!disappear) {
                drawEndBall(canvas, pointEnd, currentRadiusEnd);
            }
        } else {
            drawStartBall(canvas, pointStart, currentRadiusStart);
            if (mIsCanDrag) {
                drawEndBall(canvas, pointEnd, currentRadiusEnd);
                drawBezier(canvas);
            }

        }

        if (!disappear) {
            if (msgCount > 0) {
                if (pointEnd.x==0||pointEnd.y==0){
                    drawText(canvas, msgCount, pointStart);
                }else {
                    drawText(canvas, msgCount, pointEnd);
                }
            }
        }
    }

    /**
     * 缁樺埗鏂囧瓧
     *
     * @param canvas 鐢诲竷
     */
    private void drawText(Canvas canvas, int msgCount, PointF point) {
        textRect.left = (int) (point.x - radiusStart);
        textRect.top = (int) (point.y - radiusStart);
        textRect.right = (int) (point.x + radiusStart);
        textRect.bottom = (int) (point.y + radiusStart);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //鏂囧瓧缁樺埗鍒版暣涓竷灞€鐨勪腑蹇冧綅缃?        canvas.drawText(msgCount > 99 ? "99+" : msgCount + "", textRect.centerX(), baseline, textPaint);
    }

    /**
     * 鐢昏捣濮嬪皬鐞?     *
     * @param canvas 鐢诲竷
     * @param pointF 鐐瑰潗鏍?     * @param radius 鍗婂緞
     */
    private void drawStartBall(Canvas canvas, PointF pointF, float radius) {
        canvas.drawCircle(pointF.x, pointF.y, radius, circlePaint);
    }

    /**
     * 鐢绘嫋鎷界粨鏉熺殑灏忕悆
     *
     * @param canvas 鐢诲竷
     * @param pointF 鐐瑰潗鏍?     * @param radius 鍗婂緞
     */
    private void drawEndBall(Canvas canvas, PointF pointF, float radius) {
        canvas.drawCircle(pointF.x, pointF.y, radius, circlePaint);
    }

    /**
     * 鐢昏礉濉炲皵鏇茬嚎
     *
     * @param canvas 鐢诲竷
     */
    private void drawBezier(Canvas canvas) {
        path.reset();
        path.moveTo(pointA.x, pointA.y);
        path.quadTo(pointO.x, pointO.y, pointB.x, pointB.y);
        path.lineTo(pointC.x, pointC.y);
        path.quadTo(pointO.x, pointO.y, pointD.x, pointD.y);
        path.lineTo(pointA.x, pointA.y);
        path.close();

        canvas.drawPath(path, circlePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float currentX;
        float currentY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                setIsCanDrag(event);
                break;
            case MotionEvent.ACTION_MOVE:

                if (mIsCanDrag) {

                    currentX = event.getX();
                    currentY = event.getY();
                    //璁剧疆鎷栨嫿鍦嗙殑鍧愭爣
                    pointEnd.set(currentX, currentY);

                    if (!isOutOfRang) {
                        setCurrentRadius();
                        setABCDOPoint();
                    }

                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mIsCanDrag) {
                    if (isOutOfRang) {
                        //娑堝け鍔ㄧ敾
                        disappear = true;
                        if (onDragBallListener != null) {
                            onDragBallListener.onDisappear();
                        }
                        invalidate();
                    } else {
                        disappear = false;
                        //鍥炲脊鍔ㄧ敾
                        final float a = (pointEnd.y - pointStart.y) / (pointEnd.x - pointStart.x);
                        ValueAnimator valueAnimator = ValueAnimator.ofFloat(pointEnd.x, pointStart.x);
                        valueAnimator.setDuration(500);
                        valueAnimator.setInterpolator(new BounceInterpolator());
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float x = (float) animation.getAnimatedValue();

                                float y = pointStart.y + a * (x - pointStart.x);

                                pointEnd.set(x, y);
                                setCurrentRadius();

                                setABCDOPoint();

                                invalidate();

                            }
                        });
                        valueAnimator.start();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 璁剧疆褰撳墠璁＄畻鐨勫埌鐨勫崐寰?     */
    private void setCurrentRadius() {
        float distance = (float) Math.sqrt(Math.pow(pointStart.x - pointEnd.x, 2) + Math.pow(pointStart.y - pointEnd.y, 2));

        //鎷栨嫿璺濈鍦ㄨ缃殑鏈€澶у€艰寖鍥村唴鎵嶇粯鍒惰礉濉炲皵鍥惧舰
        if (distance <= maxDistance) {
            //姣斾緥绯绘暟  鎺у埗涓ゅ渾鍗婂緞缂╂斁
            float percent = distance / maxDistance;

            //涔嬫墍浠?0.6鍜?.2鍙负浜嗘斁缃嫋鎷借繃绋嬪渾鍙樺寲鐨勮繃澶у拰杩囧皬杩欎釜绯绘暟鏄娆″皾璇曠殑鍑虹殑
            //浣犱篃鍙互閫傚綋璋冩暣绯绘暟杈惧埌鑷繁鎯宠鐨勬晥鏋?            currentRadiusStart = (1 - percent * 0.6f) * radiusStart;
            currentRadiusEnd = (1 + percent * 0.2f) * radiusEnd;

            isOutOfRang = false;
        } else {
            isOutOfRang = true;
            currentRadiusStart = radiusStart;
            currentRadiusEnd = radiusEnd;
        }
    }

    /**
     * 鍒ゆ柇鏄惁鍙互鎷栨嫿
     *
     * @param event event
     */
    private void setIsCanDrag(MotionEvent event) {
        Rect rect = new Rect();
        rect.left = (int) (startX - radiusStart);
        rect.top = (int) (startY - radiusStart);
        rect.right = (int) (startX + radiusStart);
        rect.bottom = (int) (startY + radiusStart);

        //瑙︽懜鐐规槸鍚﹀湪鍦嗙殑鍧愭爣鍩熷唴
        mIsCanDrag = rect.contains((int) event.getX(), (int) event.getY());
    }

    /**
     * 璁剧疆璐濆灏旀洸绾跨殑鐩稿叧鐐瑰潗鏍? 璁＄畻鏂瑰紡鍙傜収缁撶畻鍥惧嵆鍙湅鏄庣櫧
     * 锛坧s涓轰簡鐢讳釜娓呮杩欎釜鍥捐姳浜嗕笉灏戝姛澶摝锛?     */
    private void setABCDOPoint() {
        //鎺у埗鐐瑰潗鏍?        pointO.set((pointStart.x + pointEnd.x) / 2.0f, (pointStart.y + pointEnd.y) / 2.0f);

        float x = pointEnd.x - pointStart.x;
        float y = pointEnd.y - pointStart.y;

        //鏂滅巼 tanA=rate
        double rate;
        rate = x / y;
        //瑙掑害  鏍规嵁鍙嶆鍒囧嚱鏁扮畻瑙掑害
        float angle = (float) Math.atan(rate);

        pointA.x = (float) (pointStart.x + Math.cos(angle) * currentRadiusStart);
        pointA.y = (float) (pointStart.y - Math.sin(angle) * currentRadiusStart);

        pointB.x = (float) (pointEnd.x + Math.cos(angle) * currentRadiusEnd);
        pointB.y = (float) (pointEnd.y - Math.sin(angle) * currentRadiusEnd);

        pointC.x = (float) (pointEnd.x - Math.cos(angle) * currentRadiusEnd);
        pointC.y = (float) (pointEnd.y + Math.sin(angle) * currentRadiusEnd);

        pointD.x = (float) (pointStart.x - Math.cos(angle) * currentRadiusStart);
        pointD.y = (float) (pointStart.y + Math.sin(angle) * currentRadiusStart);
    }

    /**
     * 璁剧疆娑堟伅鏁?     *
     * @param count 娑堟伅涓暟
     */
    public void setMsgCount(int count) {
        msgCount = count;
        invalidate();
    }

    public void reset() {
        msgCount = 0;
        mIsCanDrag = false;
        isOutOfRang = false;
        disappear = false;
        pointStart.set(startX, startY);
        pointEnd.set(startX, startY);

        setABCDOPoint();
        invalidate();
    }

    public void setOnDragBallListener(OnDragBallListener onDragBallListener) {
        this.onDragBallListener = onDragBallListener;
    }

    /**
     * 鍥炶皟浜嬩欢
     */
    public interface OnDragBallListener {
        void onDisappear();
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

