package com.allen.androidcustomview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by allen on 2017/6/5.
 * <p>
 * 瀛楃涓查€愬瓧鏄剧ず鐨剉iew
 *   fadeInTextView
 *               .setTextString("鑷繁鐨勫瓧绗︿覆")
 *               .startFadeInAnimation()
 *               .setTextAnimationListener(new FadeInTextView.TextAnimationListener() {
 *                   @Override
 *                   public void animationFinish() {
 *
 *                   }
 *               });
 */

public class FadeInTextView extends TextView {

    private Rect textRect = new Rect();

    private StringBuffer stringBuffer = new StringBuffer();

    private String[] arr;

    private int textCount;

    private int currentIndex = -1;

    /**
     * 姣忎釜瀛楀嚭鐜扮殑鏃堕棿
     */
    private int duration = 300;
    private ValueAnimator textAnimation;

    private TextAnimationListener textAnimationListener;


    public FadeInTextView setTextAnimationListener(TextAnimationListener textAnimationListener) {
        this.textAnimationListener = textAnimationListener;
        return this;
    }

    public FadeInTextView(Context context) {
        this(context, null);
    }

    public FadeInTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
//        浣跨敤setText浠ｆ浛閲嶇粯灏变笉鐢ㄨ嚜宸卞幓缁樺埗text浜?//        if (stringBuffer != null) {
//            drawText(canvas, stringBuffer.toString());
//        }
    }

    /**
     * 缁樺埗鏂囧瓧
     *
     * @param canvas 鐢诲竷
     */
    private void drawText(Canvas canvas, String textString) {
        textRect.left = getPaddingLeft();
        textRect.top = getPaddingTop();
        textRect.right = getWidth() - getPaddingRight();
        textRect.bottom = getHeight() - getPaddingBottom();
        Paint.FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        //鏂囧瓧缁樺埗鍒版暣涓竷灞€鐨勪腑蹇冧綅缃?        canvas.drawText(textString, getPaddingLeft(), baseline, getPaint());
    }

    /**
     * 鏂囧瓧閫愪釜鏄剧ず鍔ㄧ敾  閫氳繃鎻掑€肩殑鏂瑰紡鏀瑰彉鏁版嵁婧?     */
    private void initAnimation() {

        //浠?鍒皌extCount - 1  鏄缃粠绗竴涓瓧鍒版渶鍚庝竴涓瓧鐨勫彉鍖栧洜瀛?        textAnimation = ValueAnimator.ofInt(0, textCount - 1);
        //鎵ц鎬绘椂闂村氨鏄瘡涓瓧鐨勬椂闂翠箻浠ュ瓧鏁?        textAnimation.setDuration(textCount * duration);
        //鍖€閫熸樉绀烘枃瀛?        textAnimation.setInterpolator(new LinearInterpolator());
        textAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int index = (int) valueAnimator.getAnimatedValue();
                //杩囨护鍘婚噸锛屼繚璇佹瘡涓瓧鍙噸缁樹竴娆?                if (currentIndex != index) {
                    stringBuffer.append(arr[index]);
                    currentIndex = index;
                    //鎵€鏈夋枃瀛楅兘鏄剧ず瀹屾垚涔嬪悗杩涘害鍥炶皟缁撴潫鍔ㄧ敾
                    if (currentIndex == (textCount - 1)) {
                        if (textAnimationListener != null) {
                            textAnimationListener.animationFinish();
                        }
                    }

                    //鏂版€濊矾鐨勫仛娉?                    setText(stringBuffer.toString());

                    /**
                     * 涔嬪墠鐨勫仛娉曞埛鏂伴噸缁榯ext,闇€瑕佽嚜宸辨帶鍒舵枃瀛楃殑缁樺埗锛?                     * 鐪嬪埌缃戝弸鐨勮瘎璁哄紑鎷撲簡鎬濊矾锛屾棦鐒舵槸鐩存帴闆嗘垚TextView
                     * 灏卞彲浠ョ洿鎺ヤ娇鐢╯etText()鏂规硶杩涜璁剧疆鍊间簡
                     */
                    //invalidate();鑰佹€濊矾鐨勫仛娉?                }
            }
        });
    }

    /**
     * 璁剧疆閫愭笎鏄剧ず鐨勫瓧绗︿覆
     *
     * @param textString
     * @return
     */
    public FadeInTextView setTextString(String textString) {
        if (textString != null) {
            //鎬诲瓧鏁?            textCount = textString.length();
            //瀛樻斁鍗曚釜瀛楃殑鏁扮粍
            arr = new String[textCount];
            for (int i = 0; i < textCount; i++) {
                arr[i] = textString.substring(i, i + 1);
            }
            initAnimation();
        }

        return this;
    }

    /**
     * 寮€鍚姩鐢?     *
     * @return
     */
    public FadeInTextView startFadeInAnimation() {
        if (textAnimation != null) {
            stringBuffer.setLength(0);
            currentIndex = -1;
            textAnimation.start();
        }
        return this;
    }

    /**
     * 鍋滄鍔ㄧ敾
     *
     * @return
     */
    public FadeInTextView stopFadeInAnimation() {
        if (textAnimation != null) {
            textAnimation.end();
        }
        return this;
    }

    /**
     * 鍥炶皟鎺ュ彛
     */
    public interface TextAnimationListener {
        void animationFinish();
    }
}

