package com.my_swpu.mystudyhelper.activity.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.BaseActivity;
import com.my_swpu.mystudyhelper.activity.LoginActivity;
import com.my_swpu.mystudyhelper.biz.GlobalInfoBiz;
import com.my_swpu.mystudyhelper.biz.RollInfoBiz;
import com.my_swpu.mystudyhelper.biz.UserDataBiz;
import com.my_swpu.mystudyhelper.entity.GlobalInfo;
import com.my_swpu.mystudyhelper.entity.UserEntity;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.util.NetUtil;
import com.my_swpu.mystudyhelper.view.widget.XListView;

import java.util.ArrayList;
import java.util.Map;

public class RollActivity extends BaseActivity {

    private LinearLayout ll_back_on_rollactivity;
    private XListView xLvPlan;
    private SimpleAdapter sAdapter;
    /**
     * Dao成员变量
     */
    private GlobalInfoBiz gDao;
    private UserDataBiz uDao;
    private RollInfoBiz prDao;
    /**
     * 数据模型变量
     */
    private GlobalInfo gInfo;
    private UserEntity uData;
    /**
     * 临时变量
     */
    private int uid;

    /**
     * 数据存储变量
     */
    ArrayList<Map<String, String>> rollList;
    private Handler handler;
    /**
     * 线程对象
     */
    // 连接线程
    private Runnable connRunnable = new Runnable() {
        @Override
        public void run() {
            NetUtil nHelper = new NetUtil();
            rollList = nHelper.getRoll(uData);
            // 如果连接成功，返回了更新数据
            if (rollList != null) {
                    if (prDao.update(rollList, uid)) {
                        runOnUiThread(succRunnable);
                    }
                    else {
                        runOnUiThread(errURunnable);
                    }
            }
            // 连接错误
            else {
                    runOnUiThread(errRunnable);
            }
        }
    };
    // 连接成功线程
    private Runnable succRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.GET_PLANS_ACTION);
            updateRoll();
            stopRefreshOrLoadmore();
        }
    };

    // 连接错误线程
    private Runnable errRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.GET_PLANS_ACTION);
            stopRefreshOrLoadmore();
            DialogUtil.showToast(RollActivity.this, "连接错误！请检查网络连接！", 1);
        }
    };
    // 更新错误线程
    private Runnable errURunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.GET_PLANS_ACTION);
            stopRefreshOrLoadmore();
            DialogUtil.showToast(RollActivity.this, "更新错误！", 1);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        ll_back_on_rollactivity = (LinearLayout) findViewById(R.id.ll_back_on_rollactivity);
        xLvPlan = (XListView) findViewById(R.id.xLvPlans);
        // 初始化Dao成员变量
        gDao = new GlobalInfoBiz(this);
        uDao = new UserDataBiz(this);
        prDao = new RollInfoBiz(this);

        // 初始化数据模型变量
        gInfo = gDao.query();
        uid = gInfo.getActiveUserUid();
        uData = uDao.query(uid);
        rollList = prDao.query(uid);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        ll_back_on_rollactivity.setOnClickListener(this);
        xLvPlan.setPullRefreshEnable(true);
        xLvPlan.setPullLoadEnable(false);
        getPlansAction();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(clickedViewId){
            case R.id.ll_back_on_rollactivity:
//                onBackPressed();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getPlansAction();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRefreshOrLoadmore();
                DialogUtil.showToast(RollActivity.this, "已加载全部", 0);
            }
        }, 1000);
    }

    private void stopRefreshOrLoadmore(){
        xLvPlan.stopRefresh();
        xLvPlan.stopLoadMore();
    }

    private void updateRoll() {
        if (rollList == null) {
            return;
        }
        sAdapter = new SimpleAdapter(this, rollList, R.layout.list_item_personal_roll,
                new String[] { "key", "value" },
                new int[] { R.id.Text_Roll_List_Key, R.id.Text_Roll_List_Value});

        xLvPlan.setAdapter(sAdapter);
        xLvPlan.setDivider(null);
    }

    private void getPlansAction(){
        try {
            DialogUtil.showLoading(this, Const.GET_PLANS_ACTION);
            new Thread(connRunnable).start();
        }catch (Exception e1){
            DialogUtil.dissMissLoading(Const.GET_PLANS_ACTION);
            DialogUtil.showToast(this, "登录已过期，请重新登录", 0);
            BaseApplication.logout();
            LoginActivity.launch(this);

        }
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, RollActivity.class)) {
            Intent intent = new Intent(activity, RollActivity.class);
            activity.startActivity(intent);
//            activity.finish();
        }
    }
}
