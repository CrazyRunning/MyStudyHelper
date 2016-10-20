package com.my_swpu.mystudyhelper.activity.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.BaseActivity;
import com.my_swpu.mystudyhelper.activity.LoginActivity;
import com.my_swpu.mystudyhelper.biz.GlobalInfoBiz;
import com.my_swpu.mystudyhelper.biz.PersonalInfoBiz;
import com.my_swpu.mystudyhelper.biz.UserDataBiz;
import com.my_swpu.mystudyhelper.entity.GlobalInfo;
import com.my_swpu.mystudyhelper.entity.PersonalInfo;
import com.my_swpu.mystudyhelper.entity.UserEntity;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.util.NetUtil;
import com.my_swpu.mystudyhelper.view.widget.XScrollView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PersonalActivity extends BaseActivity {

    private XScrollView personal_xscrollview;
    private View child_view_of_personal_xscrollview = null;
    private TextView tv_personal_name, tv_personal_num, tv_has_pass, tv_plan_completed, tv_average_grade, tv_average_rate;
    private LinearLayout ll_back_on_personalactivity, rollView, passwdView;
    private Handler handler;
    /**
     * Dao成员变量
     */
    private GlobalInfoBiz gDao;
    private UserDataBiz uDao;
    private PersonalInfoBiz pDao;

    /**
     * 数据模型变量
     */
    private GlobalInfo gInfo;
    private UserEntity uData;
    private PersonalInfo pInfo;
    private boolean isRefresh = false;

    /**
     * 临时变量
     *
     * @param
     */
    private int uid;

    /**
     * 线程对象
     */
    // 连接线程
    private Runnable connRunnable = new Runnable() {
        @Override
        public void run() {
            NetUtil nHelper = new NetUtil();
            pInfo = nHelper.getPersonalInfo(uData);
            Log.i("获取个人信息pInfo", pInfo.toString());
            // 如果连接成功，返回了更新数据
            if (pInfo != null) {
                if (pDao.update(pInfo)) {
                    runOnUiThread(succRunnable);
                } else {
                    runOnUiThread(errURunnable);
                }
            } else {  //连接错误
                runOnUiThread(errRunnable);
            }
        }
    };
    // 连接成功线程
    private Runnable succRunnable = new Runnable() {
        @Override
        public void run() {
            updatePInfo();
            DialogUtil.dissMissLoading(Const.PERSONALINFO_EVENT);
            if (isRefresh) {
                stopRefreshOrLoadMore();
                DialogUtil.showToast(PersonalActivity.this, "刷新成功", 0);
            }
        }
    };

    // 连接错误线程
    private Runnable errRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.showToast(PersonalActivity.this, "连接错误！请检查网络连接！", 1);
            DialogUtil.dissMissLoading(Const.PERSONALINFO_EVENT);
            if (isRefresh) {
                stopRefreshOrLoadMore();
            }
        }
    };
    // 更新错误线程
    private Runnable errURunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.showToast(PersonalActivity.this, "更新错误！", 1);
            DialogUtil.dissMissLoading(Const.PERSONALINFO_EVENT);
            if (isRefresh) {
                stopRefreshOrLoadMore();
            }
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
        personal_xscrollview = (XScrollView) findViewById(R.id.personal_xscrollview);
        child_view_of_personal_xscrollview = LayoutInflater.from(this).inflate(R.layout.child_view_of_personal_xscrollview, null);
        tv_personal_name = (TextView) child_view_of_personal_xscrollview.findViewById(R.id.tv_personal_name);
        tv_personal_num = (TextView) child_view_of_personal_xscrollview.findViewById(R.id.tv_personal_num);
        tv_has_pass = (TextView) child_view_of_personal_xscrollview.findViewById(R.id.tv_has_pass);
        tv_plan_completed = (TextView) child_view_of_personal_xscrollview.findViewById(R.id.tv_plan_completed);
        tv_average_grade = (TextView) child_view_of_personal_xscrollview.findViewById(R.id.tv_average_grade);
        tv_average_rate = (TextView) child_view_of_personal_xscrollview.findViewById(R.id.tv_average_rate);
        ll_back_on_personalactivity = (LinearLayout) findViewById(R.id.ll_back_on_personalactivity);
        rollView = (LinearLayout) child_view_of_personal_xscrollview.findViewById(R.id.Btn_Personal_Roll);
        passwdView = (LinearLayout) child_view_of_personal_xscrollview.findViewById(R.id.Btn_Personal_Passwd);
        // 初始化Biz成员变量
        gDao = new GlobalInfoBiz(this);
        uDao = new UserDataBiz(this);
        pDao = new PersonalInfoBiz(this);
        // 初始化数据模型变量
        gInfo = gDao.query();
        uid = gInfo.getActiveUserUid();
        uData = uDao.query(uid);
        pInfo = pDao.query(uid);

    }


    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        personal_xscrollview.setIXScrollViewListener(this);
        personal_xscrollview.setPullRefreshEnable(true);
        personal_xscrollview.setPullLoadEnable(false);
        ll_back_on_personalactivity.setOnClickListener(this);
        rollView.setOnClickListener(this);
        passwdView.setOnClickListener(this);
        personal_xscrollview.setView(child_view_of_personal_xscrollview);
        // 初始化数据模型变量
        gInfo = gDao.query();
        uid = gInfo.getActiveUserUid();
        uData = uDao.query(uid);
        pInfo = pDao.query(uid);
        if (uData != null) {
            tv_personal_num.setText("学号：" + uData.getNum());
            updatePInfo();
            getPersonalInfoAction();
        } else {

        }
    }

    private void getPersonalInfoAction() {
        try {
            DialogUtil.showLoading(this, Const.PERSONALINFO_EVENT);
            new Thread(connRunnable).start();
        }catch (Exception e1){
            DialogUtil.dissMissLoading(Const.PERSONALINFO_EVENT);
            DialogUtil.showToast(this, "登录已过期，请重新登录", 0);
            BaseApplication.logout();
            LoginActivity.launch(this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back_on_personalactivity:
//                onBackPressed();
                finish();
                break;
            case R.id.Btn_Personal_Roll:
                RollActivity.launch(this);
                break;
            case R.id.Btn_Personal_Passwd:
                ChangePasswordActivity.launch(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        isRefresh = true;
        getPersonalInfoAction();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtil.showToast(PersonalActivity.this, "已加载全部", 0);
                stopRefreshOrLoadMore();
            }
        }, 1000);
    }

    private void stopRefreshOrLoadMore() {
        personal_xscrollview.stopLoadMore();
        personal_xscrollview.stopRefresh();
    }

    private void updatePInfo() {
        if (pInfo == null) {
            return;
        }
        DecimalFormat dFormat = new DecimalFormat("##0.00");
        tv_personal_name.setText(pInfo.getName() + "，你好");
        tv_has_pass.setText(pInfo.getDays() + "");
        tv_plan_completed.setText((int) (pInfo.getPercent()) + "%");
        tv_average_grade.setText(dFormat.format(pInfo.getAvarage()));
        tv_average_rate.setText(dFormat.format(pInfo.getGpa()));
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, PersonalActivity.class)) {
            Intent intent = new Intent(activity, PersonalActivity.class);
            activity.startActivity(intent);
        }
    }
}
