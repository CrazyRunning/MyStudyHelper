package com.my_swpu.mystudyhelper.activity;

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
import com.my_swpu.mystudyhelper.biz.GlobalInfoBiz;
import com.my_swpu.mystudyhelper.biz.UserDataBiz;
import com.my_swpu.mystudyhelper.entity.GlobalInfo;
import com.my_swpu.mystudyhelper.entity.UserEntity;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.util.NetUtil;
import com.my_swpu.mystudyhelper.view.widget.XListView;

import java.util.List;
import java.util.Map;

public class ExamActivity extends BaseActivity implements XListView.IXListViewListener{

    private LinearLayout ll_back_on_examactivity;
    private XListView xLvExamInfo;
    /**
     * Dao成员变量
     */
    private GlobalInfoBiz gDao;
    private UserDataBiz uDao;


    /**
     * 数据模型变量
     */
    private GlobalInfo gInfo;
    private UserEntity uData;


    /**
     * 网络模型对象
     */
     private NetUtil netHelper;


    /**
     * 数据存储变量
     */
    List<Map<String, String>> data;
    /**
     * 临时变量
     */
    private int uid;
    private String errCodeStr;
    private SimpleAdapter simpleAdapter;
    private Handler handler;
    /**
     * 线程对象
     */
    private Runnable connection = new Runnable()
    {
        @Override
        public void run()
        {
            data = netHelper.examInfo(uData);

            if (data != null && !data.isEmpty())
            { // 如果刷新成功，更新列表
                runOnUiThread(succRunnable);
            }
            else if (data != null && data.isEmpty()) {
                runOnUiThread(retRunnable);
            }
            else
            { // 刷新失败，显示消息
                errCodeStr = "104";
                runOnUiThread(errRunnable);
            }
        }
    };

    /**
     * 自定义成员对象
     */
    private Runnable succRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            DialogUtil.dissMissLoading(Const.GET_EXAM_INFO_ACTION);
            stopRefreshOrLoadMore();
            updateToListview();
        }
    };

    private Runnable retRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            DialogUtil.dissMissLoading(Const.GET_EXAM_INFO_ACTION);
            stopRefreshOrLoadMore();
            DialogUtil.showToast(ExamActivity.this, "暂时没有考试信息！", 1);

        }
    };

    private Runnable errRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            DialogUtil.dissMissLoading(Const.GET_EXAM_INFO_ACTION);
            stopRefreshOrLoadMore();
            DialogUtil.showToast(ExamActivity.this, "连接错误！错误代码："+errCodeStr, 1);

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
        ll_back_on_examactivity = (LinearLayout) findViewById(R.id.ll_back_on_examactivity);
        xLvExamInfo = (XListView) findViewById(R.id.xLvExamInfo);
        // 初始化网络模型对象
        netHelper = new NetUtil();

        // 初始化Dao成员变量
        gDao = new GlobalInfoBiz(this);
        uDao = new UserDataBiz(this);

        // 初始化数据模型变量
        gInfo = gDao.query();
        uid = gInfo.getActiveUserUid();
        uData = uDao.query(uid);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        ll_back_on_examactivity.setOnClickListener(this);
        xLvExamInfo.setXListViewListener(this);
        xLvExamInfo.setPullRefreshEnable(true);
        xLvExamInfo.setPullLoadEnable(true);
        getExamInfoAction();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (clickedViewId){
            case R.id.ll_back_on_examactivity:
//                onBackPressed();
                finish();
                break;
            default:
                break;
        }
    }

    private void getExamInfoAction(){
        try {
            new Thread(connection).start();
            DialogUtil.showLoading(this, Const.GET_EXAM_INFO_ACTION);
        }catch (Exception e1){
            DialogUtil.dissMissLoading(Const.GET_EXAM_INFO_ACTION);
            DialogUtil.showToast(this, "登录已过期，请重新登录", 0);
            BaseApplication.logout();
            LoginActivity.launch(this);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getExamInfoAction();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRefreshOrLoadMore();
                DialogUtil.showToast(ExamActivity.this, "已加载全部", 0);
            }
        }, 1000);

    }

    private void stopRefreshOrLoadMore(){
        xLvExamInfo.stopRefresh();
        xLvExamInfo.stopLoadMore();
    }

    private void updateToListview()
    {
        // TODO Auto-generated method stub
        simpleAdapter = new SimpleAdapter(this, data, R.layout.list_exam, new String[]
                { "subject", "time", "place" }, new int[]
                { R.id.list_subject, R.id.list_time, R.id.list_place });

        xLvExamInfo.setAdapter(simpleAdapter);
        xLvExamInfo.setDivider(null);
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, ExamActivity.class)) {
            Intent intent = new Intent(activity, ExamActivity.class);
            activity.startActivity(intent);
        }
    }
}
