package com.allen.androidcustomview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;


/**
 * <pre>
 *      @author : Allen
 *      e-mail  : lygttpod@163.com
 *      date    : 2018/04/15
 *      desc    : 涓囪兘鍒嗗壊绾? *      version : 1.0
 * </pre>
 */
public class SuperDividerItemDecoration extends RecyclerView.ItemDecoration {


    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static Context context;


    /**
     * 榛樿鍒嗗壊绾跨殑棰滆壊
     */
    private int dividerDefaultColor = 0xFFE1E5E8;

    /**
     * 鍒嗗壊绾跨殑棰滆壊
     */
    private int dividerColor;
    /**
     * 鍒嗗壊绾跨殑瀹藉害
     */
    private int dividerWidth;
    /**
     * 鍒嗗壊绾胯窛绂诲乏鍙充袱杈圭殑璺濈
     */
    private int dividerPadding;
    /**
     * 鍒嗗壊绾胯窛绂诲乏杈圭殑璺濈
     */
    private int dividerPaddingLeft;
    /**
     * 鍒嗗壊绾胯窛绂诲彸杈圭殑璺濈
     */
    private int dividerPaddingRight;

    /**
     * 鍒嗗壊绾胯窛绂讳笂杈圭殑璺濈
     */
    private int dividerPaddingTop;
    /**
     * 鍒嗗壊绾胯窛绂讳笅杈圭殑璺濈
     */
    private int dividerPaddingBottom;
    /**
     * 鏄惁鏄剧ず鍒楄〃鏈€鍚庝竴鏉″垎鍓茬嚎
     */
    private boolean dividerIsShowLastDivide;


    /**
     * 鍒嗗壊绾縤tem鐨勭敾绗?     */
    private Paint dividerPaint;

    /**
     * 鍒嗗壊绾垮紑濮嬬殑浣嶇疆锛堣В鍐硆ecyclerView娣诲姞澶村竷灞€鐨勬椂鍊欙紝瑕佷粠header涓嬭竟鐨刾osition浣嶇疆绠楄捣锛?     */
    private int dividerFromPosition = 0;


    /**
     * recyclerView甯冨眬鏂瑰紡锛堟按骞虫垨鑰呭瀭鐩达級
     */
    private int orientation;


    public SuperDividerItemDecoration(Builder builder) {

        context = builder.context;

        dividerColor = builder.dividerColor == 0 ? dividerDefaultColor : builder.dividerColor;
        dividerPadding = dp2px(builder.dividerPadding);
        dividerPaddingLeft = dp2px(builder.dividerPaddingLeft);
        dividerPaddingRight = dp2px(builder.dividerPaddingRight);
        dividerPaddingTop = dp2px(builder.dividerPaddingTop);
        dividerPaddingBottom = dp2px(builder.dividerPaddingBottom);
        dividerWidth = builder.dividerWidth == 0 ? dp2px(0.5f) : dp2px(builder.dividerWidth);
        dividerFromPosition = builder.dividerFromPosition;
        dividerIsShowLastDivide = builder.dividerIsShowLastDivide;
        orientation = builder.orientation;
        dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dividerPaint.setColor(dividerColor);

        if (dividerPadding != 0) {
            dividerPaddingLeft = dividerPaddingRight = dividerPadding;
            dividerPaddingTop = dividerPaddingBottom = dividerPadding;
        }
    }

    public static class Builder {

        private Context context;
        private int dividerColor;
        private int dividerWidth;
        private int dividerPadding;
        private int dividerPaddingLeft;
        private int dividerPaddingRight;
        private int dividerPaddingTop;
        private int dividerPaddingBottom;
        private int dividerFromPosition;
        private boolean dividerIsShowLastDivide;
        private int orientation = VERTICAL;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Builder setDividerWidth(int dividerWidth) {
            this.dividerWidth = dividerWidth;
            return this;
        }

        public Builder setDividerPadding(int dividerPadding) {
            this.dividerPadding = dividerPadding;
            return this;
        }

        public Builder setDividerPaddingLeft(int dividerPaddingLeft) {
            this.dividerPaddingLeft = dividerPaddingLeft;
            return this;
        }

        public Builder setDividerPaddingRight(int dividerPaddingRight) {
            this.dividerPaddingRight = dividerPaddingRight;
            return this;
        }

        public Builder setDividerPaddingTop(int dividerPaddingTop) {
            this.dividerPaddingTop = dividerPaddingTop;
            return this;
        }

        public Builder setDividerPaddingBottom(int dividerPaddingBottom) {
            this.dividerPaddingBottom = dividerPaddingBottom;
            return this;
        }

        public Builder setDividerFromPosition(int dividerFromPosition) {
            this.dividerFromPosition = dividerFromPosition;
            return this;
        }

        public Builder setIsShowLastDivide(boolean dividerIsShowLastDivide) {
            this.dividerIsShowLastDivide = dividerIsShowLastDivide;
            return this;
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public SuperDividerItemDecoration build() {
            return new SuperDividerItemDecoration(this);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (orientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }


    private void drawVertical(Canvas c, RecyclerView parent) {
        int count = parent.getChildCount();

        if (count > 0) {
            int showCount = dividerIsShowLastDivide ? count : count - 1;
            for (int i = dividerFromPosition; i < showCount; i++) {
                View view = parent.getChildAt(i);
                int itemBottom = view.getBottom();

                c.drawRect(parent.getPaddingLeft() + dividerPaddingLeft,
                        itemBottom,
                        parent.getWidth() - parent.getPaddingRight() - dividerPaddingRight,
                        itemBottom + dividerWidth,
                        dividerPaint);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int count = parent.getChildCount();
        if (count > 0) {
            int showCount = dividerIsShowLastDivide ? count : count - 1;
            for (int i = dividerFromPosition; i < showCount; i++) {
                View view = parent.getChildAt(i);
                int itemRight = view.getRight();

                c.drawRect(itemRight,
                        parent.getPaddingTop() + dividerPaddingTop,
                        itemRight + dividerWidth,
                        parent.getHeight() - parent.getPaddingBottom() - dividerPaddingBottom,
                        dividerPaint);
            }
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (orientation == VERTICAL) {
            outRect.bottom = dividerWidth;
        } else {
            outRect.right = dividerWidth;
        }
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

}

