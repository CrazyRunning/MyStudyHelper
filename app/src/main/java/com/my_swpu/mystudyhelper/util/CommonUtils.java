package com.my_swpu.mystudyhelper.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 *
 * Created by dsx on 2015/12/7 0007.
 */
public class CommonUtils {

    /**
     *判断指定Activity是否已经至少有一个实例被加载了
     * @param context
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> boolean isActivityAreRunningBefore(Context context, Class<T> clazz)
    {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获得当前正在运行的activity
        List<ActivityManager.RunningTaskInfo> appLists = mActivityManager.getRunningTasks(1000);
        for (ActivityManager.RunningTaskInfo running : appLists)
        {
            if (clazz.getClass().getName().equals(running.baseActivity.getClassName()))
            {
                return true;
            }
        }
        return false;
    }

    private static long lastClickTime;
    /**
     * 检测按钮短时间内是否重复点击
     *
     * @return
     */
    public synchronized static boolean isButtonFastClickAtALittleTime()
    {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
