package com.my_swpu.mystudyhelper.activity;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.my_swpu.mystudyhelper.R;

public class RemindActivity extends BaseActivity {

    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pre = getSharedPreferences("time", Context.MODE_MULTI_PROCESS);
        int advance_time = pre.getInt("time_choice", 30);

        //获取系统的vibrator服务，并设置手机振动2秒
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(2000);

        // 创建一个对话框
        final AlertDialog.Builder builder = new AlertDialog.Builder(RemindActivity.this);
        builder.setTitle("温馨提示");
        builder.setMessage("童鞋，还有" + advance_time + "分钟就要上课了哦！");
        builder.setPositiveButton(
                "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 结束该Activity
                        RemindActivity.this.finish();
                    }
                }
        )
                .show();
    }

}
