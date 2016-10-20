package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;

public class NewsInfoActivity extends BaseActivity {
    private WebView webView;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        webView = (WebView) findViewById(R.id.webView);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabBackOnNews);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        webView.getSettings().setJavaScriptEnabled(true);
        String url = getIntent().getStringExtra("newsUrl");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){

                return false;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                DialogUtil.dissMissLoading(Const.GET_NEWS_INFO_ACTION);
            }
        });
        DialogUtil.showLoading(this, Const.GET_NEWS_INFO_ACTION);
        webView.loadUrl(url);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(clickedViewId == R.id.fabBackOnNews){
//            onBackPressed();
            finish();
        }
    }


    public static void launch(Activity activity, String url) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, NewsInfoActivity.class)) {
            Intent intent = new Intent(activity, NewsInfoActivity.class);
            intent.putExtra("newsUrl", url);
            activity.startActivity(intent);
        }
    }

}
