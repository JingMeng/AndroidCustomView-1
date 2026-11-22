package com.allen.androidcustomview.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.allen.androidcustomview.R;
import com.allen.androidcustomview.utils.DisplayUtils;
import com.allen.androidcustomview.widget.ItemCenterRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * <pre>
 *      @author : xiaoyao
 *      e-mail  : xiaoyao@51vest.com
 *      date    : 2018/05/07
 *      desc    :
 *      version : 1.0
 * </pre>
 */

public class ItemCenter2Adapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private ItemCenterRecyclerView recyclerView;
    private ViewGroup.MarginLayoutParams p;
    private int mRecyclerViewWidth;
    private List<String> data;

    public ItemCenter2Adapter(Context context, ItemCenterRecyclerView recyclerView, @Nullable List<String> data) {
        super(R.layout.adapter_item_center2, data);
        mRecyclerViewWidth = DisplayUtils.getDisplayWidth(context);
        this.data = data;
        this.recyclerView = recyclerView;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        View itemRoot = helper.getView(R.id.item_root);
        p = (ViewGroup.MarginLayoutParams) itemRoot.getLayoutParams();
        // 涓轰簡灞呬腑锛?绗竴涓潯鐩甽eftMagrin銆佹渶鍚庝竴涓潯鐩殑rightMargin鏄紙recyclerView瀹藉害鍑忓幓涓€涓潯鐩殑瀹藉害锛?2
        int margin = (mRecyclerViewWidth - p.width) / 2;
        if (helper.getLayoutPosition() == 0) {
            p.leftMargin = margin;
            p.rightMargin = 0;
            itemRoot.setLayoutParams(p);
        } else if (helper.getLayoutPosition() == data.size() - 1) {
            p.leftMargin = 0;
            p.rightMargin = margin;
            itemRoot.setLayoutParams(p);
        } else {
            p.leftMargin = 0;
            p.rightMargin = 0;
            itemRoot.setLayoutParams(p);
        }

        itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClickToCenter(v);
            }
        });
    }

    private void setClickToCenter(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int currentX = location[0];
        int currentCenterX = (int) (currentX + p.width / 2 * 0.8f);
        int recyclerViewCenterX = mRecyclerViewWidth / 2;
        int offX = currentCenterX - recyclerViewCenterX;

        if (Math.abs(offX) > p.width / 2 * 0.21f) {//鍥犱负宸茬粡灞呬腑鐨処tem锛屽凡缁忚鏀惧ぇ鍒版瘮渚?浜?            recyclerView.smoothScrollBy(offX, 0);
        }
    }
}

