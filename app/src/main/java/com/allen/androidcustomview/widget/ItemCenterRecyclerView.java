package com.allen.androidcustomview.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * <pre>
 *      @author : xiaoyao
 *      e-mail  : xiaoyao@51vest.com
 *      date    : 2018/05/07
 *      desc    :
 *      version : 1.0
 * </pre>
 */

public class ItemCenterRecyclerView extends RecyclerView {


    public ItemCenterRecyclerView(Context context) {
        super(context);
    }

    public ItemCenterRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemCenterRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        int childCount = getChildCount();
        Log.e("allen", childCount + "");

        int[] location = new int[2];
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            v.getLocationOnScreen(location);

            int recyclerViewCenterX = getLeft() + getWidth() / 2;
            int itemCenterX = location[0] + v.getWidth() / 2;

            //鈽呬袱杈圭殑鍥剧墖缂╂斁姣斾緥
            float scale = 0.8f;
            //鈽呮煇涓猧tem涓績X鍧愭爣璺漴ecyclerView涓績X鍧愭爣鐨勫亸绉婚噺
            int offX = Math.abs(itemCenterX - recyclerViewCenterX);
            //鈽呭湪涓€涓猧tem鐨勫搴﹁寖鍥村唴锛宨tem浠?缂╂斁鑷硈cale锛岄偅涔堟敼鍙樹簡锛?-scale锛夛紝
            //浠庝笅鍒楀叕寮忕畻鍑洪殢鐫€offX鍙樺寲锛宨tem鐨勫彉鍖栫缉鏀剧櫨鍒嗘瘮
            float percent = offX * (1 - scale) / v.getWidth();
            //鈽呭彇鍙嶅摕
            float interpretateScale = 1 - percent;
            if (interpretateScale < scale) {
                interpretateScale = scale;
            }
            v.setScaleX((interpretateScale));
            v.setScaleY((interpretateScale));
        }
    }


}

