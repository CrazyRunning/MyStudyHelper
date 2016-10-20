package com.my_swpu.mystudyhelper.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.biz.GlobalInfoBiz;
import com.my_swpu.mystudyhelper.entity.GlobalInfo;
import com.my_swpu.mystudyhelper.view.BallView;

import java.util.Calendar;

public class WelcomeActivity extends BaseActivity {

    private BallView ball;
    /**
     * Dao成员变量
     */
    private GlobalInfoBiz gInfoDao;


    /**
     * 数据模型变量
     */
    private GlobalInfo gInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ball = (BallView) findViewById(R.id.ball);
        // 初始化Dao成员变量
        gInfoDao = new GlobalInfoBiz(this);
        // 初始化数据模型变量
        gInfo = gInfoDao.query();
        // 自定义函数
        initGInfo(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ball.animCancel();
                LoginActivity.launch(WelcomeActivity.this);
                finish();
            }
        }, 3000);
    }

    /**
     * 自定义方法
     */

    private void initGInfo(Context context) {
        if (gInfo == null) {
            int version = 0;
            String vsersionStr = "";
            try {
                PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                version = pi.versionCode;
                vsersionStr = pi.versionName;
            } catch (Exception e) {
                version = 1;
                vsersionStr = "1.0";
            }

            Calendar calendar = Calendar.getInstance();
            int month = calendar.get(Calendar.MONTH)+1;
            int year = calendar.get(Calendar.YEAR);

            gInfo = new GlobalInfo();
            gInfo.setVersion(version);
            gInfo.setVersionStr(vsersionStr);

            // TODO 改为相应学期开学时间
            gInfo.setTermBegin("2016-02-29");

            // 下半学期
            if (month < 8) {
                gInfo.setYearFrom(year-1);
                gInfo.setYearTo(year);
                gInfo.setTerm(2);
            }
            // 上半学期
            else {
                gInfo.setYearFrom(year);
                gInfo.setYearTo(year+1);
                gInfo.setTerm(1);
            }

//            gInfo.setFirstUse(1);
            gInfo.setActiveUserUid(0);

            gInfoDao.insert(gInfo);
        }
    }

}
