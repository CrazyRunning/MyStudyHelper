package com.my_swpu.mystudyhelper.activity.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.BaseActivity;
import com.my_swpu.mystudyhelper.biz.GlobalInfoBiz;
import com.my_swpu.mystudyhelper.biz.UserDataBiz;
import com.my_swpu.mystudyhelper.entity.GlobalInfo;
import com.my_swpu.mystudyhelper.entity.UserEntity;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.util.NetUtil;

public class ChangePasswordActivity extends BaseActivity {

    private EditText oldPasswdEditT;
    private EditText newPasswdEditT1;
    private EditText newPasswdEditT2;
    private Button submitButton;
    private LinearLayout ll_back_on_changepasswordactivity;
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
     * 数据存储变量
     */
    private String oldPasswdStr;
    private String newPasswdStr1;
    private String newPasswdStr2;
    /**
     * 临时变量
     */
    private int uid;
    private String errCodeStr;

    /**
     * 线程对象
     */


    private Runnable submitRunnable = new Runnable() {
        @Override
        public void run() {
            NetUtil netHelper = new NetUtil();
            errCodeStr = netHelper.changePassword(uData,oldPasswdStr, newPasswdStr1,newPasswdStr2);
                if (errCodeStr == null || errCodeStr.equals("100") ||
                        errCodeStr.equals("103") || errCodeStr.equals("104")) {
                    runOnUiThread(connErrRunnable);
                }
                else if (errCodeStr.equals("101")) {
                    //runOnUiThread(numErrRunnable);
                }
                else if (errCodeStr.equals("102")) {
                    //runOnUiThread(passwdErrRunnable);
                }
                else {
                    int returnCode = correctUser(errCodeStr);
                    if (returnCode == 0) {
                        runOnUiThread(successRunnable);
                        finish();
                    }
                    else {
                        errCodeStr = returnCode+"";
                        runOnUiThread(dbErrRunnable);
                    }
                }
        }
    };

    /**
     * 输出返回的提示信息
     */
    private Runnable successRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.CHANGE_PASSWORD_ACTION);
            DialogUtil.showToast(ChangePasswordActivity.this, "密码修改成功！", 1);
        }
    };

    private Runnable connErrRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.CHANGE_PASSWORD_ACTION);
            DialogUtil.showToast(ChangePasswordActivity.this, "网络连接错误，错误代码"+errCodeStr, 1);
        }
    };

    private Runnable dbErrRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.CHANGE_PASSWORD_ACTION);
            DialogUtil.showToast(ChangePasswordActivity.this, "信息存储错误，错误代码"+errCodeStr, 1);
        }
    };

    /**
     * 定义文本编辑框监视器：watcher
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable arg0)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            // TODO Auto-generated method stub
            submitButton.setEnabled(true); //编辑后登录按钮可用
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        // 初始化Dao成员变量
        gDao = new GlobalInfoBiz(this);
        uDao = new UserDataBiz(this);
        // 初始化数据模型变量
        gInfo = gDao.query();
        uid = gInfo.getActiveUserUid();
        uData = uDao.query(uid);
        oldPasswdEditT = (EditText) findViewById(R.id.edittext_change_password_oldpasswd);
        newPasswdEditT1 = (EditText) findViewById(R.id.edittext_change_password_newpasswd1);
        newPasswdEditT2 = (EditText)findViewById(R.id.edittext_change_password_newpasswd2);
        submitButton = (Button) findViewById(R.id.button_change_password_submit);
        ll_back_on_changepasswordactivity = (LinearLayout) findViewById(R.id.ll_back_on_changepasswordactivity);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        oldPasswdEditT.addTextChangedListener(watcher);
        newPasswdEditT1.addTextChangedListener(watcher);
        newPasswdEditT2.addTextChangedListener(watcher);
        submitButton.setOnClickListener(this);
        ll_back_on_changepasswordactivity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (clickedViewId){
            case R.id.ll_back_on_changepasswordactivity:
                onBackPressed();
                break;
            case R.id.button_change_password_submit:
                oldPasswdStr = oldPasswdEditT.getText().toString().trim();
                newPasswdStr1 = newPasswdEditT1.getText().toString().trim();
                newPasswdStr2 = newPasswdEditT2.getText().toString().trim();
                if(validate()){
                    submitConn();
                }
                break;
            default:
                break;
        }
    }

    private boolean validate(){
        if (oldPasswdStr.length()<1) {
            Toast.makeText(getApplicationContext(), "请输入旧密码！", Toast.LENGTH_SHORT).show();
            submitButton.setEnabled(false);  //使登录按钮不可用
            newPasswdEditT1.clearFocus();
            newPasswdEditT2.clearFocus();
            oldPasswdEditT.requestFocus();
        }
        else if (newPasswdStr1.length()<1||newPasswdStr2.length()<1) {
            Toast.makeText(getApplicationContext(), "请输入新密码！", Toast.LENGTH_SHORT).show();
            submitButton.setEnabled(false);  //使登录按钮不可用
            if (newPasswdStr1.length()<1)
            {
                oldPasswdEditT.clearFocus();
                newPasswdEditT2.clearFocus();
                newPasswdEditT1.requestFocus();
            }
            else {
                oldPasswdEditT.clearFocus();
                newPasswdEditT1.clearFocus();
                newPasswdEditT2.requestFocus();
            }
        }
        else if (!newPasswdStr1.equals(newPasswdStr2)) {
            Toast.makeText(getApplicationContext(), "密码输入不一致，新重新输入！", Toast.LENGTH_SHORT).show();
            submitButton.setEnabled(false);  //使登录按钮不可用
            newPasswdEditT1.setText("");
            newPasswdEditT2.setText("");

            oldPasswdEditT.clearFocus();
            newPasswdEditT2.clearFocus();
            newPasswdEditT1.requestFocus();
        }
        else {
            return true;
        }
        return false;
    }

    private void submitConn(){
        DialogUtil.showLoading(this, Const.CHANGE_PASSWORD_ACTION);
        new Thread(submitRunnable).start();
    }

    /**
     * 修正UserData对象，存入数据库，并更新global_info
     * @param session 从NetHelper返回的JSESSIONID的值
     * @return 成功返回0，否则返回错误代码
     */
    private int correctUser(String session) {
        uData.setSession(session);

        if (uDao.update(uData)) {
            return 0;
        }
        else {
            return 202;
        }
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, ChangePasswordActivity.class)) {
            Intent intent = new Intent(activity, ChangePasswordActivity.class);
            activity.startActivity(intent);
//            activity.finish();
        }
    }
}
