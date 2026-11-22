package com.allen.androidcustomview.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.allen.androidcustomview.R;
import com.allen.androidcustomview.widget.PayPsdInputView;

public class PayPsdViewActivity extends AppCompatActivity {
    private PayPsdInputView passwordInputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_psd_view);

        passwordInputView = (PayPsdInputView) findViewById(R.id.password);

        passwordInputView.setComparePassword( new PayPsdInputView.onPasswordListener() {

            @Override
            public void onDifference(String oldPsd, String newPsd) {
                // TODO: 2018/1/22  鍜屼笂娆¤緭鍏ョ殑瀵嗙爜涓嶄竴鑷? 鍋氱浉搴旂殑涓氬姟閫昏緫澶勭悊
                Toast.makeText(PayPsdViewActivity.this, "涓ゆ瀵嗙爜杈撳叆涓嶅悓" + oldPsd + "!=" + newPsd, Toast.LENGTH_SHORT).show();
                passwordInputView.cleanPsd();
            }

            @Override
            public void onEqual(String psd) {
                // TODO: 2017/5/7 涓ゆ杈撳叆瀵嗙爜鐩稿悓锛岄偅灏卞幓杩涜鏀粯妤?                Toast.makeText(PayPsdViewActivity.this, "瀵嗙爜鐩稿悓" + psd, Toast.LENGTH_SHORT).show();
                passwordInputView.setComparePassword("");
                passwordInputView.cleanPsd();
            }

            @Override
            public void inputFinished(String inputPsd) {
                // TODO: 2018/1/3 杈撳畬閫昏緫
                Toast.makeText(PayPsdViewActivity.this, "输入完毕：" + inputPsd, Toast.LENGTH_SHORT).show();
                passwordInputView.setComparePassword(inputPsd);
            }
        });

    }
}

