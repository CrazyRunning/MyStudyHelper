package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.menu.MapActivity;
import com.my_swpu.mystudyhelper.biz.CourseInfoBiz;
import com.my_swpu.mystudyhelper.biz.GlobalInfoBiz;
import com.my_swpu.mystudyhelper.biz.UserDataBiz;
import com.my_swpu.mystudyhelper.entity.CourseEntity;
import com.my_swpu.mystudyhelper.entity.GlobalInfo;
import com.my_swpu.mystudyhelper.entity.UserEntity;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.util.NetUtil;

import java.util.LinkedList;

public class LoginActivity extends BaseActivity {

    private EditText et_username, et_password;
    private Button bt_login;
    private RelativeLayout activity_login;
    private TextView tvLocalCourseTable, tvChooseMyUniversity, tvJumpToMap;
    private AlertDialog alertDialog;
    private LinearLayout ll_swpu_icon, ll_scu_icon;
    private View choose_my_university;

    /**
     * Biz成员变量
     */
    private GlobalInfoBiz gInfoDao;
    private UserDataBiz uDataDao;
    /**
     * 数据模型变量
     */
    private GlobalInfo gInfo;
    private UserEntity uData;
    /**
     * 临时变量
     */
    private String errCodeStr;
    private String username, password;


    private Runnable numErrRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.showToast(LoginActivity.this, "您输入的学号不存在，请重新输入", 1);
        }
    };

    private Runnable passwdErrRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.showToast(LoginActivity.this, "您输入的密码不正确，请重新输入", 1);
        }
    };

    private Runnable connErrRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.showToast(LoginActivity.this, "网络连接错误，错误代码" + errCodeStr, 1);
        }
    };

    private Runnable dbErrRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.showToast(LoginActivity.this, "信息存储错误，错误代码" + errCodeStr, 1);
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        activity_login = (RelativeLayout) findViewById(R.id.activity_login);
        et_username = (EditText) findViewById(R.id.et_login_username);
        et_password = (EditText) findViewById(R.id.et_login_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        tvLocalCourseTable = (TextView) findViewById(R.id.tvLocalCourseTable);
        choose_my_university = LayoutInflater.from(this).inflate(R.layout.choose_my_university, null);
        tvChooseMyUniversity = (TextView) findViewById(R.id.tvChooseMyUniversity);
        tvJumpToMap = (TextView) findViewById(R.id.tvJumpToMap);
        ll_swpu_icon = (LinearLayout) choose_my_university.findViewById(R.id.ll_swpu_icon);
        ll_scu_icon = (LinearLayout) choose_my_university.findViewById(R.id.ll_scu_icon);
        // 初始化Dao成员变量
        gInfoDao = new GlobalInfoBiz(this);
        uDataDao = new UserDataBiz(this);
        // 初始化数据模型变量
//        gInfo = gInfoDao.query();
        gInfo = gInfoDao.query();
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        bt_login.setOnClickListener(this);
        tvLocalCourseTable.setOnClickListener(this);
        ll_swpu_icon.setOnClickListener(this);
        ll_scu_icon.setOnClickListener(this);
        tvChooseMyUniversity.setOnClickListener(this);
        tvJumpToMap.setOnClickListener(this);
        activity_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        alertDialog = new AlertDialog.Builder(this)
//                .setTitle("选择高校")
                .setView(choose_my_university)
                .create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_login:
                if(BaseApplication.getInstance().getUniversity() > 0) {
                    if(BaseApplication.getInstance().getUniversity() == Const.SWPU){
                        DialogUtil.showDialog(this, "西南石油大学教务处服务器正在进行硬件升级，目前暂不支持移动端访问");
                    }else {
                        username = et_username.getText().toString().trim();
                        password = et_password.getText().toString().trim();
                        login(username, password);
                    }
                }else{
                    DialogUtil.showToast(this, "请选择高校", 0);
                }
                break;
            case R.id.tvLocalCourseTable:
                BaseApplication.getInstance().setIsLocal(true);
                CourseActivity.launch(this);
                break;
            case R.id.ll_swpu_icon:
                alertDialog.dismiss();
                BaseApplication.getInstance().setUniversity(Const.SWPU);
                break;
            case R.id.ll_scu_icon:

                alertDialog.dismiss();
                BaseApplication.getInstance().setUniversity(Const.SCU);
                break;
            case R.id.tvChooseMyUniversity:
                alertDialog.show();
                break;
            case R.id.tvJumpToMap:
                MapActivity.launch(this, false);
                break;
            default:
                break;
        }
    }

    private void login(final String username, final String password) {
        if (TextUtils.isEmpty(username)) {
            DialogUtil.showToast(this, "学号不能为空", 0);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            DialogUtil.showToast(this, "密码不能为空", 0);
            return;
        }
        DialogUtil.showLoading(this, Const.LOGIN_EVENT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetUtil netHelper = new NetUtil();
                errCodeStr = netHelper.login(username, password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.dissMissLoading(Const.LOGIN_EVENT);
                        Log.i("登录请求返回码errCodeStr", errCodeStr + "");
                    }
                });
                if (errCodeStr == null || errCodeStr.equals("100") ||
                        errCodeStr.equals("103") || errCodeStr.equals("104")) {
                    runOnUiThread(connErrRunnable);
                } else if (errCodeStr.equals("101")) {
                    runOnUiThread(numErrRunnable);
                } else if (errCodeStr.equals("102")) {
                    runOnUiThread(passwdErrRunnable);
                } else {
                    int returnCode = initUser(errCodeStr);
                    if (returnCode == 0) {
                        MainActivity.launch(LoginActivity.this);
                    } else {
                        errCodeStr = returnCode + "";
                        runOnUiThread(dbErrRunnable);
                    }
                }
            }
        }).start();

    }

    /**
     * 初始化UserData对象，存入数据库，并更新global_info
     *
     * @param session 从NetHelper返回的JSESSIONID的值
     * @return 成功返回0，否则返回错误代码
     */
    protected int initUser(String session) {
        uData = new UserEntity();
        uData.setNum(username);
        uData.setPasswd(password);
        uData.setSession(session);
        Log.i("session", session);
        uData.setLastlogin((int) ((System.currentTimeMillis()) / 1000));
        uData.setLastlogout(0);
//        if (autoBox.isChecked()) {
//            uData.setAutologin(1);
//        }
//        else {
//            uData.setAutologin(0);
//        }
//        if (passwdBox.isChecked()) {
//            uData.setSavepasswd(1);
//        }
//        else {
//            uData.setSavepasswd(0);
//        }
        uData.setHeadshot("");
        int uid = uDataDao.insert(uData);
        if (uid == 0) {
            return 201;
        }

        gInfo.setActiveUserUid(uid);
        if (gInfoDao.insert(gInfo)) {
            return 0;
        } else {
            return 202;
        }
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, LoginActivity.class)) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
////                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.my_swpu.mystudyhelper.activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Login Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://com.my_swpu.mystudyhelper.activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }
}