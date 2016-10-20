package com.my_swpu.mystudyhelper.util;

import android.app.AlertDialog; 
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.my_swpu.mystudyhelper.view.LoadingDialog;

/**
 * 对话框工具类（UTF-8）
 */
public class DialogUtil {
	
	private static LoadingDialog loadDialog = null;
	private static int eventCode;
	/** 自定义对话框 */
	public static void showDialog(final Context context, String msg) {
		//
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setMessage(msg).setPositiveButton("好的", null);
		builder.create().show();
	}

	/** 自定义对话框 */
	public static void showDialog(Context context, View view) {
		new AlertDialog.Builder(context)
		.setView(view).setCancelable(false)
		.setPositiveButton("好的", null)
		.create()
		.show();
	}
	
	public static void showToast(Context context, String text, int duration){
		Toast.makeText(context, text, duration).show();	
	}
	
	/**
	 * 显示进度条
	 * @param context
	 */
	public static void showLoading(Context context) {
        if (context == null) {
            return;
        }
        try {
            if (loadDialog != null) {
                // 取消
                loadDialog.dismiss();
                loadDialog = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            loadDialog = null;
        } finally {    
            loadDialog = new LoadingDialog(context);
			loadDialog.setCanceledOnTouchOutside(true);
            loadDialog.show();
        }

    }
	/**
	 * 显示进度条
	 * @param context
	 */
	public static void showLoading(Context context, int eventCoder) {
        if (context == null) {
            return;
        }
        try {
            if (loadDialog != null) {
                // 取消
                loadDialog.dismiss();
                loadDialog = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            loadDialog = null;
        } finally {
			eventCode = eventCoder;
            loadDialog = new LoadingDialog(context);
			loadDialog.setCanceledOnTouchOutside(true);
            loadDialog.show();
        }

    }

	/**
	 * 取消进度条
	 */
	public static void dissMissLoading() {
        try {
            if (loadDialog != null) {
                // 取消
                loadDialog.dismiss();
                loadDialog = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            loadDialog = null;
        }
    }

	public static void dissMissLoading(int eventCoder)
	{
		try
		{
			if (loadDialog != null && eventCoder == eventCode)
			{
				// 取消进度对话框
				loadDialog.dismiss();
				loadDialog = null;
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			loadDialog = null;
		}

	}
	
}
