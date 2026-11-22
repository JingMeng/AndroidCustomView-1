package com.allen.androidcustomview.tagview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.allen.androidcustomview.R;

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends AppCompatActivity {

    private RecyclerView tagRecyclerView;

    private static final int MAX = 9;
    private List<TagBean> tagBeanList = new ArrayList<>();

    private TagAdapter tagAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        initView();
        initData();
        setRecyclerView();
    }

    private void initData() {
        tagBeanList.add(new TagBean("1","鍑嗘椂"));
        tagBeanList.add(new TagBean("2","闈炲父缁呭＋"));
        tagBeanList.add(new TagBean("3","非常有礼貌"));
        tagBeanList.add(new TagBean("4","寰堜細鐓ч【濂崇敓"));
        tagBeanList.add(new TagBean("5","鎴戠殑鐢风鏄釜澶ф殩鐢峰摝"));
        tagBeanList.add(new TagBean("6","璋堝悙浼橀泤"));
        tagBeanList.add(new TagBean("7","送我到楼下"));
        tagBeanList.add(new TagBean("9","杩熷埌"));
        tagBeanList.add(new TagBean("10","鎬佸害鎭跺姡"));
        tagBeanList.add(new TagBean("11","鏈変笉绀艰矊琛屼负"));
        tagBeanList.add(new TagBean("12","鏈変井杈辨€ц瑷€鏈夋毚鍔涘€惧悜"));
        tagBeanList.add(new TagBean("13","浜鸿韩鏀诲嚮"));
        tagBeanList.add(new TagBean("14","涓存椂鏀瑰彉琛岀▼"));
        tagBeanList.add(new TagBean("15","客户迟到并无理要求延长约会时间"));
    }

    private void setRecyclerView() {
        GridLayoutManager layoutManage = new GridLayoutManager(this, 2);
        layoutManage.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (tagBeanList.get(position).getTag_name().length()>MAX)
                    return 2;
                return 1;
            }
        });
        tagRecyclerView.setLayoutManager(layoutManage);
        tagAdapter = new TagAdapter(tagBeanList);
        tagRecyclerView.setAdapter(tagAdapter);

    }

    private void initView() {
        tagRecyclerView = (RecyclerView) findViewById(R.id.tag_rv);
    }
}

