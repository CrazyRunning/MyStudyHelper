package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * 成绩绩点
 */
public class GradeActivity extends BaseActivity implements XListView.IXListViewListener{

    private LinearLayout ll_back_on_rateactivity;
    private TextView avarageTextView, GPATextView;
    private XListView xLvCourseList;
    /**
     * 数据存储变量
     */
    private List<Map<String, String>> gradeList;
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
     * 临时变量
     */
    int uid;
    float AvaGpa[];
    private SimpleAdapter sAdapter;
    private Handler handler;
    /**
     * 线程对象
     */
    private Runnable connRunnable = new Runnable() {
        @Override
        public void run() {
            try{
            NetUtil nHelper = new NetUtil();
            gradeList = nHelper.getGrades(uData);
            for(int i = 0; i < gradeList.size(); i++) {
                Log.i("成绩绩点获取结果gradeList", gradeList.get(i).toString() + "");
            }
            AvaGpa = nHelper.getAvaGpa(gradeList);
            // 如果连接成功，返回了更新数据
            if (gradeList != null) {
                    runOnUiThread(succRunnable);
            }
            // 连接错误
            else {
                    runOnUiThread(errRunnable);
            }
        }catch (Exception e0){
                //session失效，重新登录
                runOnUiThread(loginRunnable);
            }
        }
    };
    // 连接成功线程
    private Runnable succRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.GET_GRADE_LIST_ACTION);
            updateGData();
        }
    };

    // 连接错误线程
    private Runnable errRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.GET_GRADE_LIST_ACTION);
            DialogUtil.showToast(GradeActivity.this, "连接错误！请检查网络连接！", 1);
        }
    };

    //session失效，重新登录
    private Runnable loginRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.GET_GRADE_LIST_ACTION);
            DialogUtil.showToast(GradeActivity.this, "登录失效，请重新登录", 1);
            BaseApplication.getInstance().removeActivies();
            LoginActivity.launch(GradeActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        ll_back_on_rateactivity = (LinearLayout) findViewById(R.id.ll_back_on_rateactivity);
        avarageTextView = (TextView) findViewById(R.id.Text_Grade_Score);
        GPATextView = (TextView) findViewById(R.id.Text_Grade_GPA);
        xLvCourseList = (XListView) findViewById(R.id.xLvCourseList);
        // 初始化Biz成员变量
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
        ll_back_on_rateactivity.setOnClickListener(this);
        xLvCourseList.setXListViewListener(this);
        xLvCourseList.setPullRefreshEnable(true);
        xLvCourseList.setPullLoadEnable(true);
        getGradeListAction();
    }

    private void getGradeListAction(){
        try {
            new Thread(connRunnable).start();
            DialogUtil.showLoading(this, Const.GET_GRADE_LIST_ACTION);
        }catch (Exception e1){
            DialogUtil.dissMissLoading(Const.GET_GRADE_LIST_ACTION);
            DialogUtil.showToast(this, "登录已过期，请重新登录", 0);
            BaseApplication.logout();
            LoginActivity.launch(this);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (clickedViewId){
            case R.id.ll_back_on_rateactivity:
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
        getGradeListAction();
    }



    @Override
    public void onLoadMore() {
        super.onLoadMore();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtil.showToast(GradeActivity.this, "已加载全部", 0);
                stopRefreshOrLoadMore();
            }
        }, 1000);
    }

    private void stopRefreshOrLoadMore(){
        xLvCourseList.stopRefresh();
        xLvCourseList.stopLoadMore();
    }

    private void updateGData() {
        DecimalFormat dFormat = new DecimalFormat("##0.00");
        avarageTextView.setText(dFormat.format(AvaGpa[0]));
        GPATextView.setText(dFormat.format(AvaGpa[1]));
        sAdapter = new SimpleAdapter(this, gradeList, R.layout.grade_data_item,
                new String[] { "attr", "subject", "credit", "grade", "time"},
                new int[] { R.id.list_grade_attr, R.id.list_grade_subject, R.id.list_grade_credit,
                        R.id.list_grade_grade, R.id.list_grade_time});

        xLvCourseList.setAdapter(sAdapter);
        xLvCourseList.setDivider(null);
        stopRefreshOrLoadMore();
        xLvCourseList.setRefreshTime(getTime());
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, GradeActivity.class)) {
            Intent intent = new Intent(activity, GradeActivity.class);
            activity.startActivity(intent);
        }
    }
}
