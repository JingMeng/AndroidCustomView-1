package com.allen.androidcustomview.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.allen.androidcustomview.R;
import com.allen.androidcustomview.adapter.HoverAdapter;
import com.allen.androidcustomview.bean.UserBean;
import com.allen.androidcustomview.utils.CharacterParser;
import com.allen.androidcustomview.utils.PinyinComparator;
import com.allen.androidcustomview.widget.HoverItemDecoration;
import com.allen.androidcustomview.widget.IndexView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *      @author : xiaoyao
 *      e-mail  : xiaoyao@51vest.com
 *      date    : 2018/04/16
 *      desc    :
 *      version : 1.0
 * </pre>
 */

public class HoverItemActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private IndexView indexView;
    private TextView showTextDialog;

    private HoverAdapter adapter;

    private List<UserBean> userBeans = new ArrayList<>();

    private String[] names = new String[]{"阿妹", "打黑牌", "张三", "李四", "王五", "田鸡", "孙五"};

    /**
     * 姹夊瓧杞崲鎴愭嫾闊崇殑绫?     */
    private CharacterParser characterParser;
    /**
     * 鏍规嵁鎷奸煶鏉ユ帓鍒桳istView閲岄潰鐨勬暟鎹被
     */
    private PinyinComparator pinyinComparator;

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hover_item);

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();


        userBeans = filledData(getData());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        indexView = (IndexView) findViewById(R.id.index_view);
        showTextDialog = (TextView) findViewById(R.id.show_text_dialog);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //涓€琛屼唬鐮佸疄鐜板惛椤舵偓娴殑鏁堟灉
        recyclerView.addItemDecoration(new HoverItemDecoration(this, new HoverItemDecoration.BindItemTextCallback() {
            @Override
            public String getItemText(int position) {
                return userBeans.get(position).getSortLetters();
            }
        }));

        adapter = new HoverAdapter(userBeans);

        recyclerView.setAdapter(adapter);

        initIndexView();
    }

    /**
     * 鍒濆鍖栧彸杈瑰瓧骞曠储寮晇iew
     */
    private void initIndexView() {
        indexView.setShowTextDialog(showTextDialog);
        indexView.setOnTouchingLetterChangedListener(new IndexView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                // 璇ュ瓧姣嶉娆″嚭鐜扮殑浣嶇疆
                int position = getPositionForSection(letter);
                if (position != -1) {
                    layoutManager.scrollToPositionWithOffset(position, 0);
                    layoutManager.setStackFromEnd(false);
                }
            }
        });
    }

    public int getPositionForSection(String section) {
        for (int i = 0; i < userBeans.size(); i++) {
            String sortStr = userBeans.get(i).getSortLetters();
            if (sortStr.equals(section)) {
                return i;
            }
        }
        return -1;
    }

    private List<UserBean> getData() {
        List<UserBean> userBeans = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            UserBean userBean = new UserBean();
            userBean.setUserName(names[i % 7]);
            userBeans.add(userBean);
        }

        return userBeans;
    }

    private List<UserBean> filledData(List<UserBean> sortList) {

        for (int i = 0; i < sortList.size(); i++) {

            if ("".equals(sortList.get(i).getUserName())) {
                sortList.get(i).setSortLetters("#");
            } else {
                String pinyin = characterParser.getSelling(sortList.get(i).getUserName());
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 姝ｅ垯琛ㄨ揪寮忥紝鍒ゆ柇棣栧瓧姣嶆槸鍚︽槸鑻辨枃瀛楁瘝
                if (sortString.matches("[A-Z]")) {
                    sortList.get(i).setSortLetters(sortString.toUpperCase());
                } else {
                    sortList.get(i).setSortLetters("#");
                }
            }

        }

        // 鏍规嵁a-z杩涜鎺掑簭婧愭暟鎹?        Collections.sort(sortList, pinyinComparator);

        return sortList;
    }

}

