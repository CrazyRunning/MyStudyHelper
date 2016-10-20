package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.biz.CourseDataBiz;
import com.my_swpu.mystudyhelper.biz.CourseInfoBiz;
import com.my_swpu.mystudyhelper.biz.GlobalInfoBiz;
import com.my_swpu.mystudyhelper.biz.UserDataBiz;
import com.my_swpu.mystudyhelper.entity.CourseData;
import com.my_swpu.mystudyhelper.entity.CourseEntity;
import com.my_swpu.mystudyhelper.entity.GlobalInfo;
import com.my_swpu.mystudyhelper.entity.UserEntity;
import com.my_swpu.mystudyhelper.service.RemindReceiver;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.util.NetUtil;

import java.util.LinkedList;

/**
 * 我的课表
 */
public class CourseActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    /**
     * 第一个无内容的格子
     */
    protected TextView empty;
    /**
     * 星期一的格子
     */
    protected TextView monColum;
    /**
     * 星期二的格子
     */
    protected TextView tueColum;
    /**
     * 星期三的格子
     */
    protected TextView wedColum;
    /**
     * 星期四的格子
     */
    protected TextView thrusColum;
    /**
     * 星期五的格子
     */
    protected TextView friColum;
    /**
     * 星期六的格子
     */
    protected TextView satColum;
    /**
     * 星期日的格子
     */
    protected TextView sunColum;
    /**
     * 课程表body部分布局
     */
    protected RelativeLayout course_table_layout;
    /**
     * 屏幕宽度
     **/
    protected int screenWidth;
    /**
     * 课程格子平均宽度
     **/
    protected double aveWidth;
    private LinearLayout ll_back_on_courseactivity;
    private TextView tvCourseSetting;
    private AlertDialog alertDialog;
    private View settingView;
    //声明一个SharedPreferences对象，用来保存switch组件的开关信息
    private SharedPreferences preferences = null;
    //editor对象用来向preferences中写入数据
    private SharedPreferences.Editor editor = null;

    //声明一个SharedPreferences对象，用来保存time_choice的值
    private SharedPreferences pre = null;
    //pre_editor对象用来向pre中写入数据
    private SharedPreferences.Editor pre_editor = null;

    //声明一个AlarmManager对象，用来开启课前提醒服务
    private AlarmManager alarmManager = null;
    //声明一个PendingIntent对象，用来指定alarmManager要启动的组件
    private PendingIntent pi = null;
    private Intent alarm_receiver = null;

    //定义单选列表对话狂的id，该对话框用于显示课前提醒时间的可选项
    final int SINGLE_DIALOG = 0x113;
    //定义选中的时间
    private int time_choice = 0;

    private Switch switch_quietButton;    //静音
    private Switch switch_saveButton;   //上课提醒

    private int gridHeight;
    /**
     * 临时变量
     */
    private NetUtil nHelper;
    private int uid;
    private int currIndex;
    private int currentWeek;
    /**
     * Biz成员变量
     */
    private GlobalInfoBiz gDao;
    private UserDataBiz uDao;
    private CourseInfoBiz cInfoDao;
    private CourseDataBiz cDataDao;

    /**
     * 数据模型变量
     */
    private GlobalInfo gInfo;
    private UserEntity uData;
    private LinkedList<CourseEntity> courseInfoList;        //课程信息链表
    private CourseEntity courseEntity;
    /**
     * 声明一个获取系统音频服务的类的对象
     */
//    private AudioManager audioManager;
    /**
     * 线程对象
     */
    private Runnable connRunnable = new Runnable() {
        @Override
        public void run() {
            courseInfoList = nHelper.getCourse(uData);
            if(courseInfoList != null) {
                for (int i = 0; i < courseInfoList.size(); i++) {
                    Log.i("获取课表信息courseInfoList", courseInfoList.get(i).getName() +
                            "\n" + "day：" + courseInfoList.get(i).getDay() + "\n" +
                            "节次：" + courseInfoList.get(i).getLessonfrom() + "\n" +
                            "节数：" + (courseInfoList.get(i).getLessonto() - courseInfoList.get(i).getLessonfrom() + 1));
                }
            }else{
                DialogUtil.showToast(CourseActivity.this, "更新失败，请稍后再试", 0);
            }
            // 如果连接成功，返回了更新数据
            if (courseInfoList != null) {

                runOnUiThread(succRunnable);
            }
            // 连接错误
            else {
                runOnUiThread(errRunnable);
            }
        }
    };
    // 连接成功线程
    private Runnable succRunnable = new Runnable() {
        @Override
        public void run() {

            // TODO 更新显示
//            myAdapter.notifyDataSetChanged();
            DialogUtil.dissMissLoading(Const.GET_COURSE_LIST_ACTION);
            DialogUtil.showToast(CourseActivity.this, "更新成功", 1);
            if (BaseApplication.getInstance().getOthers().size() > 0) {
                showOthers();
            }
            showMyCourseList();
        }
    };
    // 连接错误线程
    private Runnable errRunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.GET_COURSE_LIST_ACTION);
            DialogUtil.showToast(CourseActivity.this, "连接错误！请检查网络连接！", 1);
        }
    };
    // 更新错误线程
    private Runnable errURunnable = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading(Const.GET_COURSE_LIST_ACTION);
            DialogUtil.showToast(CourseActivity.this, "更新错误！", 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        //获得列头的控件
        ll_back_on_courseactivity = (LinearLayout) findViewById(R.id.ll_back_on_courseactivity);
        tvCourseSetting = (TextView) findViewById(R.id.tvCourseSetting);
        empty = (TextView) this.findViewById(R.id.test_empty);
        monColum = (TextView) this.findViewById(R.id.test_monday_course);
        tueColum = (TextView) this.findViewById(R.id.test_tuesday_course);
        wedColum = (TextView) this.findViewById(R.id.test_wednesday_course);
        thrusColum = (TextView) this.findViewById(R.id.test_thursday_course);
        friColum = (TextView) this.findViewById(R.id.test_friday_course);
        satColum = (TextView) this.findViewById(R.id.test_saturday_course);
        sunColum = (TextView) this.findViewById(R.id.test_sunday_course);
        course_table_layout = (RelativeLayout) this.findViewById(R.id.test_course_rl);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
        double aveWidth = width / 8;
        //第一个空白格子设置为25宽
        empty.setWidth((int) aveWidth * 3 / 4);
        monColum.setWidth((int) aveWidth * 33 / 32 + 1);
        tueColum.setWidth((int) aveWidth * 33 / 32 + 1);
        wedColum.setWidth((int) aveWidth * 33 / 32 + 1);
        thrusColum.setWidth((int) aveWidth * 33 / 32 + 1);
        friColum.setWidth((int) aveWidth * 33 / 32 + 1);
        satColum.setWidth((int) aveWidth * 33 / 32 + 1);
        sunColum.setWidth((int) aveWidth * 33 / 32 + 1);
        this.screenWidth = width;
        this.aveWidth = aveWidth;
        int height = dm.heightPixels;
        gridHeight = height / 12;
        // 初始化Dao成员变量
        gDao = new GlobalInfoBiz(this);
        uDao = new UserDataBiz(this);
        cInfoDao = new CourseInfoBiz(this);
        cDataDao = new CourseDataBiz(this);
        // 初始化数据模型变量
        gInfo = gDao.query();
        uid = gInfo.getActiveUserUid();
        uData = uDao.query(uid);

        // 初始化状态变量

        // 初始化临时变量
        nHelper = new NetUtil();
        currentWeek = BaseApplication.getInstance().getCurrentWeek();
//        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        ll_back_on_courseactivity.setOnClickListener(this);
        tvCourseSetting.setOnClickListener(this);
        //设置界面
        settingView = LayoutInflater.from(this).inflate(R.layout.course_setting, null);
        switch_quietButton = (Switch) settingView.findViewById(R.id.switch_quiet);
        switch_saveButton = (Switch) settingView.findViewById(R.id.switch_remind);
        //这里模式一定要设置为MODE_MULTI_PROCESS，否则即使相应的xml文件中数据有更新，RemindReceiver中也不能获取更新后的数据，而是一直获取上次的数据， 除非清空缓存
        this.pre = getSharedPreferences("time", Context.MODE_MULTI_PROCESS);
        this.pre_editor = pre.edit();

        //指定该SharedPreferences数据可以跨进称调用
        this.preferences = getSharedPreferences("switch", Context.MODE_MULTI_PROCESS);
        this.editor = preferences.edit();
        //获取系统的闹钟定时服务
        alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        //指定alarmManager要启动的组件
        alarm_receiver = new Intent(this, RemindReceiver.class);
//		alarm_receiver.putExtra("anvance_remindtime", time_choice);
        pi = PendingIntent.getBroadcast(this, 0, alarm_receiver, 0);
        //每次创建该activity时，从preferences中读取switch_quietButton和switch_remindButton的开关信息的数据
        Boolean quiet_status = preferences.getBoolean("switch_quiet", false);
//        Boolean remind_status = preferences.getBoolean("switch_remind", false);
        switch_quietButton.setChecked(quiet_status);
        switch_saveButton.setChecked(false);
        switch_quietButton.setOnCheckedChangeListener(this);
        switch_saveButton.setOnCheckedChangeListener(this);
        alertDialog = new AlertDialog.Builder(this)
                .create();
        if (BaseApplication.getInstance().getIsLocation()) {
            courseInfoList = BaseApplication.getInstance().getCourseInfoList();
            if (courseInfoList.size() > 0) {
                showMyCourseList();
            } else {
                DialogUtil.showDialog(this, "没有课程或者课程未更新");
            }
        } else {
            getCourseListAction();
        }
    }

    private void showMyCourseList() {
        //设置课表界面
        //动态生成12 * maxCourseNum个textview
        for (int i = 1; i <= 12; i++) {

            for (int j = 1; j <= 8; j++) {

                TextView tx = new TextView(this);
                tx.setId((i - 1) * 8 + j);
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                if (j < 8)
                    tx.setBackgroundDrawable(this.
                            getResources().getDrawable(R.drawable.course_text_view_bg));
                else
                    tx.setBackgroundDrawable(this.
                            getResources().getDrawable(R.drawable.course_table_last_colum));
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        (int) aveWidth * 33 / 32 + 1,
                        gridHeight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                //字体样式
                tx.setTextAppearance(this, R.style.courseTableText);
                //如果是第一列，需要设置课的序号（1 到 12）
                if (j == 1) {
                    tx.setText(String.valueOf(i));
                    rp.width = (int) aveWidth * 3 / 4;
                    //设置他们的相对位置
                    if (i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                } else {
                    rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8 + j - 1);
                    rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8 + j - 1);
                    tx.setText("");
                }

                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }

        //五种颜色的背景
        int[] background = {R.drawable.course_info_blue, R.drawable.course_info_green,
                R.drawable.course_info_red, R.drawable.course_info_red,
                R.drawable.course_info_yellow};
        for (int i = 0; i < courseInfoList.size(); i++) {
            //该textview的高度根据其节数的跨度来设置
            courseEntity = courseInfoList.get(i);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    (int) aveWidth * 31 / 32,
                    (gridHeight - 5) * (courseEntity.getLessonto() - courseEntity.getLessonfrom() + 1));
            //textview的位置由课程开始节数和上课的时间（day of week）确定
            rlp.topMargin = (courseEntity.getLessonfrom() - 1) * gridHeight;
            rlp.leftMargin = (courseEntity.getDay() - 1) * (int) aveWidth + 4 * (courseEntity.getDay() - 1);
            // 偏移由这节课是星期几决定
            rlp.addRule(RelativeLayout.RIGHT_OF, 1);

            TextView courseInfo = new TextView(this);
            //字体剧中
            courseInfo.setGravity(Gravity.CENTER);
            // 设置一种背景
            courseInfo.setBackgroundResource(background[1]);
            courseInfo.setTextSize(12);
            courseInfo.setLayoutParams(rlp);
            courseInfo.setTextColor(Color.WHITE);
            courseInfo.setText(courseEntity.getName() + "\n" + "@" + courseEntity.getBld() + courseEntity.getPlace());
            //设置不透明度
            courseInfo.getBackground().setAlpha(222);

            courseInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCourseInfo(courseEntity);
                }
            });
            course_table_layout.addView(courseInfo);
        }

    }

    private void showOthers() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("其他课程")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        String other = "";
        for (CourseEntity courseEntity : BaseApplication.getInstance().getOthers()) {
            other += courseEntity.getName() + " " + courseEntity.getTeacher() + "\n";
        }
        dialog.setMessage(other);
        dialog.show();
    }

    private void showCourseInfo(CourseEntity courseEntity) {
        String weekType = "";
        switch (courseEntity.getWeektype()) {
            case 1:
                weekType = "普通";
                break;
            case 2:
                weekType = "单周";
                break;
            case 3:
                weekType = "双周";
                break;
            default:
                break;
        }
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("课程信息")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage("第" + courseEntity.getWeekfrom() + "周" + "~" + "第" + courseEntity.getWeekto() + "周" +
                        " " + weekType + "\n" +
                        courseEntity.getAttr() + "\n" +
                        courseEntity.getExam() + "\n" +
                        courseEntity.getCredit() + "分" + "\n" +
                        courseEntity.getTeacher()).create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (clickedViewId) {
            case R.id.ll_back_on_courseactivity:
//                onBackPressed();
                finish();
                break;
            case R.id.tvCourseSetting:
                showSetting();
                break;
            default:
                break;
        }
    }

    private void showSetting() {
        //设置对话框
//        alertDialog.setTitle("消息设置");
        alertDialog.setView(settingView);
        alertDialog.show();
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, CourseActivity.class)) {
            Intent intent = new Intent(activity, CourseActivity.class);
            activity.startActivity(intent);
        }
    }

    //设置
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //声明一个获取系统音频服务的类的对象
        final AudioManager audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        //从MainAcivity中获取原始设置的铃声模式
        Intent intent = getIntent();
        final int orgRingerMode = intent.getIntExtra("mode_ringer", AudioManager.RINGER_MODE_NORMAL);
        switch (buttonView.getId()) {
            case R.id.switch_quiet:       //开启静音
                //启动自动静音的service
                Intent intent0 = new Intent();
                intent0.setAction("com.my_swpu.mystudyhelper.service.QUIET_SERVICE");
                intent0.setPackage(getPackageName());
                if (isChecked) {
                    if (startService(intent0) != null)
                        DialogUtil.showToast(this, "成功开启，来电将自动转为振动模式", 3000);
                    else {
                        DialogUtil.showToast(this, "未能成功开启，请重新尝试", 3000);
                        switch_quietButton.setChecked(false);
                    }
                } else {
                    if (stopService(intent0))
                        DialogUtil.showToast(CourseActivity.this, "成功关闭，恢复到原来的响铃模式", 3000);
                    else {
                        DialogUtil.showToast(CourseActivity.this, "未能成功关闭，请重新尝试", 3000);
                        switch_quietButton.setChecked(true);
                    }
                    audioManager.setRingerMode(orgRingerMode);
                }
                //将开关信息数据保存进preferences中
                editor.putBoolean("switch_quiet", isChecked);
                editor.commit();
                break;
            case R.id.switch_remind:         //保存课表
//                    if (isChecked) {
//                        showDialog(SINGLE_DIALOG);
//                    } else {
//                        alarmManager.cancel(pi);
//                    }
                if(!BaseApplication.getInstance().getIsLocation()) {
                    if (saveCourse()) {
                        DialogUtil.showToast(this, "课表已保存到本地", 1);
                    } else {
                        DialogUtil.showToast(this, "保存失败", 1);
                    }
                }else{
                    DialogUtil.showToast(this, "已保存", 1);
                }
                //将开关信息数据保存进preferences中
//                editor.putBoolean("switch_remind", isChecked);
//                editor.commit();
                break;
            default:
                break;
        }
    }

    @Override
    //该方法返回的Dialog将被showDialog()方法回调
    protected Dialog onCreateDialog(int id, Bundle args) {
        //判断生成何种类型的对话框
        if (id == SINGLE_DIALOG) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
            // 设置对话框的标题
            b.setTitle("选择课前提醒时间");
            // 为对话框设置多个列表，参数-1表示默认不选中任何选项
            b.setSingleChoiceItems(R.array.set_remind, -1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    switch (which) {
                        case 0:
                            time_choice = 5;
                            break;
                        case 1:
                            time_choice = 10;
                            break;
                        case 2:
                            time_choice = 20;
                            break;
                        case 3:
                            time_choice = 30;
                            break;
                        case 4:
                            time_choice = 40;
                            break;
                        case 5:
                            time_choice = 50;
                            break;
                        case 6:
                            time_choice = 60;
                            break;
                    }
                }
            });
            // 添加一个“确定”按钮，用于关闭该对话框
            b.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
//					System.out.println("SetActivity:" + time_choice);
                    if (time_choice == 0) {
                        DialogUtil.showToast(CourseActivity.this, "请选择课前提醒的时间", 3000);
                        switch_saveButton.setChecked(false);
                    } else {
                        CourseActivity.this.pre_editor.putInt("time_choice", time_choice);
                        pre_editor.commit();
                        //从当前时间开始，每隔一分钟启动一次pi指定的组件，即发送一次广播
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pi);
                        DialogUtil.showToast(CourseActivity.this, "设置成功，系统将在课前" + time_choice + "分钟提醒您", 1);
                    }
                }
            });
            //添加一个“取消”按钮
            b.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch_saveButton.setChecked(false);
                }
            });
            // 创建对话框
            return b.create();
        } else
            return null;
    }

    /**
     * 获取课表信息
     */
    private void getCourseListAction() {
        try {
            BaseApplication.getInstance().initOthers();
            new Thread(connRunnable).start();
            DialogUtil.showLoading(this, Const.GET_COURSE_LIST_ACTION);
        }catch (Exception e1){
            DialogUtil.dissMissLoading(Const.GET_COURSE_LIST_ACTION);
            DialogUtil.showToast(this, "登录已过期，请重新登录", 0);
            BaseApplication.logout();
            LoginActivity.launch(this);
        }
    }

    /**
     * 将课程列表存入数据库
     *
     * @return
     * @paramcList
     */
    private boolean saveCourse() {
        if (cDataDao.clear(uid)) {
            for (CourseEntity cInfo : courseInfoList) {
                int cid = cInfoDao.insert(cInfo);
                if (cid == 0) {
                    return false;
                }
                CourseData cData = new CourseData();
                cData.setUid(uid);
                cData.setCid(cid);
                if (!cDataDao.insert(cData)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
