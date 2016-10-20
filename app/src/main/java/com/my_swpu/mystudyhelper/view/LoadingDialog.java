package com.my_swpu.mystudyhelper.view;


import android.app.Dialog; 
import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.my_swpu.mystudyhelper.R;


public class LoadingDialog extends Dialog {
    public LoadingDialog(Context context) {
        super(context, R.style.backgroundTransparentDialog);
        // TODO Auto-generated constructor stub
        this.setCanceledOnTouchOutside(false);// 设置点击边缘不关闭
        //  设置监听
        setOnKeyListener(keyListener);
        // 初始化Dialog
        initDialog();
    }

    @SuppressWarnings("deprecation")
    private void initDialog() {
        // 设置布局文件
        setContentView(R.layout.zone_dialog_loading);
        Window dialogWindow = getWindow();
        // 添加动画
        dialogWindow.setWindowAnimations(R.style.mystyle_translation_horizontal_animation_right_to_left);
        WindowManager m = dialogWindow.getWindowManager();
        // 获取屏幕宽、高用
        Display d = m.getDefaultDisplay();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams parm = dialogWindow.getAttributes();
        parm.height = (int) (d.getHeight() * 1.0);
        parm.width = (int) (d.getWidth() * 1.0);
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(parm);
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
    }

    /**
     * 监听事件
     */
    OnKeyListener keyListener = new OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            // 判断是否按下了home、返回、和屏幕
            if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_SEARCH) {
                return true;
            }
            return false;
        }
    };
}