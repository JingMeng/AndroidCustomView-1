package com.allen.androidcustomview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;


/**
 * <pre>
 *      @author : Allen
 *      e-mail  : lygttpod@163.com
 *      date    : 2018/04/15
 *      desc    : 鎮仠鍚搁《鏁堟灉鐨処temDecoration
 *      version : 1.0
 * </pre>
 */
public class HoverItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;

    private int width;

    /**
     * 鍒嗙粍item鐨勯珮搴?     */
    private int itemHeight;
    /**
     * 鍒嗗壊绾跨殑楂樺害
     */
    private int itemDivideHeight;
    /**
     * 鍒嗙粍text璺濈宸﹁竟鐨勮窛绂?     */
    private int itemTextPaddingLeft;

    /**
     * 鍒嗙粍item鐨勭敾绗?     */
    private Paint itemPaint;
    /**
     * 鍒嗙粍item鐨勯鑹?     */
    private int itemHoverPaintColor=0xFFf4f4f4;
    /**
     * 鍒嗙粍鏂囧瓧鐨勯鑹?     */
    private int textPaintColor=0xFF999999;

    /**
     * 鎮仠item鐨勭敾绗?     */
    private Paint itemHoverPaint;
    /**
     * 鏂囧瓧鐨勭敾绗?     */
    private Paint textPaint;
    /**
     * 缁樺埗鏂囧瓧鐨勭煩褰㈣竟妗?     */
    private Rect textRect = new Rect();
    /**
     * 鍒嗙粍瀛楁瘝鐨勫洖璋冿紙涓€鑸槸鍙栫殑鍒嗙粍鐨勫ぇ鍐欏瓧姣嶏級
     */
    private BindItemTextCallback bindItemTextCallback;


    public HoverItemDecoration(Context content, BindItemTextCallback bindItemTextCallback) {
        this.context = content;
        this.bindItemTextCallback = bindItemTextCallback;

        width = content.getResources().getDisplayMetrics().widthPixels;

        itemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemPaint.setColor(itemHoverPaintColor);

        itemHoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        itemHoverPaint.setColor(itemHoverPaintColor);

        itemHeight = dp2px(30);
        itemTextPaddingLeft = dp2px(20);
        itemDivideHeight = dp2px(1);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textPaintColor);
        textPaint.setTextSize(sp2px(15));


    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            //鍒嗙粍item鐨勯《閮ㄥ拰搴曢儴
            int itemTop = view.getTop() - itemHeight;
            int itemBottom = view.getTop();

            //鍙item鍦╝dapter涓湡瀹炵殑浣嶇疆
            int position = parent.getChildAdapterPosition(view);

            String text = bindItemTextCallback.getItemText(position);

            if (isFirstInGroup(position)) {
                c.drawRect(0, itemTop, width, itemBottom, itemPaint);
                drawText(c, itemTop, itemBottom, text);
            } else {
                c.drawRect(0, view.getTop() - itemDivideHeight, width, view.getTop(), itemHoverPaint);
            }

        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //缁樺埗鎮仠鐨剉iew

        int count = parent.getChildCount();

        if (count > 0) {
            //鎮仠鍙槸绗竴涓綅缃偓鍋滐紝鎵€浠ュ彧鍙栫涓€涓獀iew杩涜璁剧疆
            View firstView = parent.getChildAt(0);

            int position = parent.getChildAdapterPosition(firstView);
            String text = bindItemTextCallback.getItemText(position);

            if (firstView.getBottom() <= itemHeight && isFirstInGroup(position + 1)) {
                c.drawRect(0, 0, width, firstView.getBottom(), itemHoverPaint);
                drawText(c, firstView.getBottom() - itemHeight, firstView.getBottom(), text);
            } else {
                c.drawRect(0, 0, width, itemHeight, itemHoverPaint);
                drawText(c, 0, itemHeight, text);
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (isFirstInGroup(position)) {
            outRect.top = itemHeight;
        }else {
            outRect.top = itemDivideHeight;
        }

    }


    /**
     * 缁樺埗鏂囧瓧
     *
     * @param canvas 鐢诲竷
     */
    private void drawText(Canvas canvas, int itemTop, int itemBottom, String textString) {

        textRect.left = itemTextPaddingLeft;
        textRect.top = itemTop;
        textRect.right = textString.length();
        textRect.bottom = itemBottom;

        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (textRect.bottom + textRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(textString, textRect.left, baseline, textPaint);
    }


    private boolean isFirstInGroup(int position) {
        if (position == 0) {
            return true;
        } else {
            String prevItemText = bindItemTextCallback.getItemText(position - 1);
            String currentItemText = bindItemTextCallback.getItemText(position);
            if (prevItemText.equals(currentItemText)) {
                return false;
            } else {
                return true;
            }

        }
    }

    public interface BindItemTextCallback {
        String getItemText(int position);
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());

    }

}

