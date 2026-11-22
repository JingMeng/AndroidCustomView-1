package com.allen.androidcustomview.widget;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.Nullable;
import android.util.AttributeSet;

import android.view.View;
import android.view.animation.DecelerateInterpolator;


/**
 * Created by Allen on 2017/4/10.
 * 闆疯揪姘存尝鎵╂暎鍔ㄧ敾
 */

public class RadarWaveView extends View {

    /**
     * 鍦嗗舰鑳屾櫙鐢荤瑪
     */
    private Paint mBgPaint;
    /**
     * 鍦嗗舰鑳屾櫙棰滆壊
     */
    private int mBgColor = 0x66FFFFFF;
    /**
     * 鍦嗗舰鑳屾櫙鐨勫崐寰?     */
    private float mBgRadius;
    /**
     * 鍦嗗舰鑳屾櫙鏈€澶у崐寰勬瘮渚?     */
    private float mMaxRadiusRate = 0.85f;

    /**
     * 鎵╂暎姘存尝鐨勯鑹?     */
    private int mCircleEdgeColor_green = 0x2fffd1;
    private int mCircleEdgeColor_red = 0xFF4081;
    private int mCircleEdgeColor_yellow = 0xfeff38;

    private int[] colors = {mCircleEdgeColor_green, mCircleEdgeColor_yellow, mCircleEdgeColor_red};

    /**
     * 闃村奖棰滆壊
     */
    private int mCircleShadowColor = 0x3a0909;
    /**
     * 鎵╂暎姘存尝鐨勭敾绗?     */
    private Paint mCircleWavePaint;
    /**
     * 鎵╂暎姘存尝榛樿鍗婂緞
     */
    private float mDefaultWaveRadius = 0;
    /**
     * 褰撳墠鎵╂暎姘存尝鐨勫崐寰?     */
    private float mCurrentWaveRadius;


    /**
     * 鎵╂暎姘存尝鎵ц鏃堕棿
     */
    private long mDuration = 2000;

    /**
     * 姘存尝鍔ㄧ敾
     */
    private ValueAnimator mAnimator_green;
    private ValueAnimator mAnimator_yellow;
    private ValueAnimator mAnimator_red;

    private ValueAnimator[] valueAnimators = new ValueAnimator[3];
    /**
     * 鍔ㄧ敾闆?     */
    private AnimatorSet mAnimatorSet;


    public RadarWaveView(Context context) {
        this(context, null);
    }

    public RadarWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initBgPaint();
        initCirclePaint();

        for (int i = 0; i < valueAnimators.length; i++) {
            initAnimation(i);
        }
//        initAnimation_green();
//        initAnimation_yellow();
//        initAnimation_red();

        initAnimatorSet();


    }


    /**
     * 鍒濆鍖栬儗鏅敾绗?     */
    private void initBgPaint() {
        mBgPaint = new Paint();
        mBgPaint.setStrokeWidth(5);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mBgColor);
    }

    /**
     * 鍒濆鍖栨按娉㈠渾鍦堢殑鐢荤瑪
     */
    private void initCirclePaint() {
        mCircleWavePaint = new Paint();
        mCircleWavePaint.setStrokeWidth(3);
        mCircleWavePaint.setStyle(Paint.Style.STROKE);
        mCircleWavePaint.setAntiAlias(true);
        mCircleWavePaint.setColor(mCircleEdgeColor_green);

        //璁剧疆鍦嗙幆鍛ㄥ洿闃村奖鏁堟灉  鍔犱笂闃村奖蹇呴』鍙栨秷纭欢鍔犻€? 娌′簡纭欢鍔犻€熷埛鏂癠I瀵艰嚧鍗″崱鐨勬劅瑙?瀹樻柟鎺ㄨ崘闃村奖閫傚悎鍔犲湪闈欐€佺殑鍥惧儚涓?//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        mCircleWavePaint.setShadowLayer(30, 0, 0, mCircleShadowColor);
    }


    private void initAnimation(final int animationP) {

        valueAnimators[animationP] = ValueAnimator.ofFloat(0, 1);
        valueAnimators[animationP].setDuration(mDuration);

        valueAnimators[animationP].setInterpolator(new DecelerateInterpolator());
        valueAnimators[animationP].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float v = (float) valueAnimator.getAnimatedValue();
                mCurrentWaveRadius = mDefaultWaveRadius + mBgRadius * v;

                mCircleWavePaint.setColor(colors[animationP]);
                if (v > 0.9) {
                    mCircleWavePaint.setAlpha((int) (((1 - v) + 0.4) * 100));
                } else {
                    mCircleWavePaint.setAlpha((int) ((v + 0.2) * 100));
                }

                mCircleWavePaint.setStrokeWidth(2 + 3 * v);
                invalidate();
            }
        });

    }


    /**
     * 鍒濆鍖栧姩鐢?     */
    private void initAnimation_green() {
        mAnimator_green = ValueAnimator.ofFloat(0, 1);
        mAnimator_green.setDuration(mDuration);

        mAnimator_green.setInterpolator(new DecelerateInterpolator());
        mAnimator_green.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float v = (float) valueAnimator.getAnimatedValue();
                mCurrentWaveRadius = mDefaultWaveRadius + mBgRadius * v;

                mCircleWavePaint.setColor(mCircleEdgeColor_green);
                if (v > 0.9) {
                    mCircleWavePaint.setAlpha((int) (((1 - v) + 0.4) * 100));
                } else {
                    mCircleWavePaint.setAlpha((int) ((v + 0.2) * 100));
                }

                mCircleWavePaint.setStrokeWidth(2 + 3 * v);
                invalidate();
            }
        });

    }

    /**
     * 鍒濆鍖栧姩鐢?     */
    private void initAnimation_yellow() {
        mAnimator_yellow = ValueAnimator.ofFloat(0, 1);
        mAnimator_yellow.setDuration(mDuration);

        //鏃犵嚎寰幆
//        mAnimator_yellow.setRepeatCount(3);

        mAnimator_yellow.setInterpolator(new DecelerateInterpolator());
        mAnimator_yellow.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float v = (float) valueAnimator.getAnimatedValue();
                mCurrentWaveRadius = mDefaultWaveRadius + mBgRadius * v;
                mCircleWavePaint.setColor(mCircleEdgeColor_yellow);

                if (v > 0.9) {
                    mCircleWavePaint.setAlpha((int) (((1 - v) + 0.4) * 100));
                } else {
                    mCircleWavePaint.setAlpha((int) ((v + 0.2) * 100));
                }

                mCircleWavePaint.setStrokeWidth(2 + 3 * v);
                invalidate();
            }
        });

    }

    /**
     * 鍒濆鍖栧姩鐢?     */
    private void initAnimation_red() {
        mAnimator_red = ValueAnimator.ofFloat(0, 1);
        mAnimator_red.setDuration(mDuration);

        //鏃犵嚎寰幆
//        mAnimator_red.setRepeatCount(3);

        mAnimator_red.setInterpolator(new DecelerateInterpolator());
        mAnimator_red.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float v = (float) valueAnimator.getAnimatedValue();
                mCurrentWaveRadius = mDefaultWaveRadius + mBgRadius * v;

                mCircleWavePaint.setColor(mCircleEdgeColor_red);

                if (v > 0.9) {
                    mCircleWavePaint.setAlpha((int) (((1 - v) + 0.4) * 100));
                } else {
                    mCircleWavePaint.setAlpha((int) ((v + 0.2) * 100));
                }

                mCircleWavePaint.setStrokeWidth(2 + 3 * v);
                invalidate();
            }
        });

    }


    /**
     * 鍒濆鍖栧姩鐢婚泦
     */
    private void initAnimatorSet() {
        mAnimatorSet = new AnimatorSet();

        mAnimatorSet.play(valueAnimators[1]).before(valueAnimators[2]).after(valueAnimators[0]);

        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBgRadius = Math.min(w, h) * mMaxRadiusRate / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawWaveCircle(canvas);
    }

    /**
     * 鐢绘按娉㈠渾鍦?     *
     * @param canvas 鐢诲竷
     */
    private void drawWaveCircle(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mCurrentWaveRadius, mCircleWavePaint);
    }

    /**
     * 鐢诲渾褰㈢殑鑳屾櫙
     *
     * @param canvas 鐢荤瑪
     */
    private void drawBg(Canvas canvas) {
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBgRadius, mBgPaint);
    }

    /**
     * 寮€鍚姩鐢?     */
    public void start() {
        mAnimatorSet.start();
    }

    /**
     * 鏆傚仠鍔ㄧ敾
     */
    public void pause() {
        mAnimatorSet.pause();
    }

    /**
     * 鍋滄鍔ㄧ敾
     */
    public void stop() {
        mAnimatorSet.end();
    }

}

