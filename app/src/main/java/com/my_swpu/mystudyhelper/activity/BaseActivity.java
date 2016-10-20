package com.my_swpu.mystudyhelper.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.view.View.OnClickListener;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;

import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.view.widget.XScrollView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dsx on 2015/12/7 0007.
 */
public class BaseActivity extends FragmentActivity implements OnClickListener, OnItemClickListener,
        OnItemLongClickListener, OnPageChangeListener, XScrollView.IXScrollViewListener {
    //全局点击VIEWID
    protected int clickedViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        BaseApplication.addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(this);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        onInitBaseActivity();

    }

    /**
     * 异步初始化UI和数据
     */
    protected void onInitBaseActivity() {
        // TODO Auto-generated method stub
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                OnInitUiAndData();
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                OnBindDataWithUi();
            }
        }.execute();
    }

    public void setContentView(Activity activity) {

        // 得到class文件的名字
        String activityName = activity.getClass().getName();
        int nIndex = activityName.lastIndexOf(".");
        if (nIndex != -1) {
            // 转换成把名字转换成小写
            final String strResourceName = activityName.substring(nIndex + 1).toLowerCase(Locale.getDefault());
            //
            final int nLayoutId = activity.getResources().getIdentifier(strResourceName, "layout", activity.getPackageName());

            if (nLayoutId != 0) {
                activity.setContentView(nLayoutId);
            }
        }

    }

    /**
     * 加载UI元素和数据
     */
    protected void OnInitUiAndData() {

    }

    /**
     * 绑定数据到UI元素上
     */
    protected void OnBindDataWithUi() {

    }

    /**
     * 返回键当HOME键用
     */
    protected void onBackPressedAsHomePressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (isFinishing()) {
            return;
        }
        if (CommonUtils.isButtonFastClickAtALittleTime()) {
            return;
        }
        clickedViewId = v.getId();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    /**
     * 界面销毁回调函数
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.removeActivity(this);
        setContentView(R.layout.a_a_null_view);
        // 强制清理此Activity对象资源
        System.gc();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
