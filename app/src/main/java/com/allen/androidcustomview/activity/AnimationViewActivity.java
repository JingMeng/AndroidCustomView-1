package com.allen.androidcustomview.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.allen.androidcustomview.R;
import com.allen.androidcustomview.widget.FadeInTextView;
import com.allen.androidcustomview.widget.LoadingButton;

public class AnimationViewActivity extends AppCompatActivity {

    private FadeInTextView fadeInTextView;
    private LoadingButton loadingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_view);

        fadeInTextView = (FadeInTextView) findViewById(R.id.fade_in_tv);
        loadingButton = (LoadingButton) findViewById(R.id.loading_btn);

        fadeInTextView
                .setTextString("鑷畾涔塿iew瀹炵幇瀛楃涓查€愬瓧鏄剧ず锛屽悗杈圭殑鏂囧瓧鏄负浜嗘祴璇曟崲琛屾槸鍚︽甯告樉绀猴紒")
                .setTextAnimationListener(new FadeInTextView.TextAnimationListener() {
                    @Override
                    public void animationFinish() {
                        loadingButton.stopLoading();
                    }
                });

        loadingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingButton.startLoading();
                fadeInTextView.startFadeInAnimation();
            }
        });
    }
}

