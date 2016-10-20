package com.my_swpu.mystudyhelper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.my_swpu.lib_menudrawer.MenuDrawer;
import com.my_swpu.lib_menudrawer.Position;
import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.menu.MapActivity;
import com.my_swpu.mystudyhelper.activity.menu.PersonalActivity;
import com.my_swpu.mystudyhelper.biz.CourseInfoBiz;
import com.my_swpu.mystudyhelper.biz.GlobalInfoBiz;
import com.my_swpu.mystudyhelper.biz.PersonalInfoBiz;
import com.my_swpu.mystudyhelper.biz.RollInfoBiz;
import com.my_swpu.mystudyhelper.biz.UserDataBiz;
import com.my_swpu.mystudyhelper.entity.GlobalInfo;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.util.LocationService;
import com.my_swpu.mystudyhelper.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends BaseActivity {

    private TextView myLocation, Menu_main_home, Menu_main_personal, Menu_main_message,
            Menu_main_map, Menu_main_feedback, Menu_main_update, Menu_main_about,
            Menu_main_logout, Menu_main_exit, dateTextView, weekTextView;
    private MenuDrawer mMenuDrawer;
    private int mActiveViewId;
    private static final String STATE_MENUDRAWER = "com.my_swpu.lib_menudrawer.WindowSample.menuDrawer";
    private static final String STATE_ACTIVE_VIEW_ID = "com.my_swpu.lib_menudrawer.WindowSample.activeViewId";
    private Bundle savedInstanceState = null;
    private LocationService locationService = null;
    private String showMyLocation, showMyAddress;
    private LinearLayout ll_menu, ll_course, ll_exam, ll_rate, ll_grade, ll_release, ll_tuling;
    private AlertDialog alertDialog;
    private Handler handler;
    /**
     * 数据模型变量
     */
    private GlobalInfo gInfo;
    /**
     * Dao成员变量
     */
    private GlobalInfoBiz gInfoDao;
    private UserDataBiz uDao;
    private PersonalInfoBiz pDao;
    private RollInfoBiz prDao;
    private CourseInfoBiz courseInfoBiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        handler = new Handler();
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, Position.LEFT, MenuDrawer.MENU_DRAG_WINDOW);
        mMenuDrawer.setMenuView(R.layout.menu_scrollview);
        Menu_main_home = (TextView) mMenuDrawer.findViewById(R.id.Menu_main_home);
        Menu_main_personal = (TextView) mMenuDrawer.findViewById(R.id.Menu_main_personal);
        Menu_main_message = (TextView) mMenuDrawer.findViewById(R.id.Menu_main_message);
        Menu_main_map = (TextView) mMenuDrawer.findViewById(R.id.Menu_main_map);
        Menu_main_feedback = (TextView) mMenuDrawer.findViewById(R.id.Menu_main_feedback);
        Menu_main_update = (TextView) mMenuDrawer.findViewById(R.id.Menu_main_update);
        Menu_main_about = (TextView) mMenuDrawer.findViewById(R.id.Menu_main_about);
        Menu_main_logout = (TextView) mMenuDrawer.findViewById(R.id.Menu_main_logout);
        Menu_main_exit = (TextView) mMenuDrawer.findViewById(R.id.menu_main_exit);
        TextView activeView = (TextView) findViewById(mActiveViewId);
        if (activeView != null) {
            mMenuDrawer.setActiveView(activeView);
        }
    }

    @Override
    protected void onStop() {
        try {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        } catch (Exception e0) {

        }
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mMenuDrawer.toggleMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_MENUDRAWER, mMenuDrawer.saveState());
        outState.putInt(STATE_ACTIVE_VIEW_ID, mActiveViewId);
    }


    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();
        myLocation = (TextView) findViewById(R.id.myLocation);
        ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        ll_course = (LinearLayout) findViewById(R.id.ll_course);
        ll_exam = (LinearLayout) findViewById(R.id.ll_exam);
        ll_rate = (LinearLayout) findViewById(R.id.ll_rate);
        ll_grade = (LinearLayout) findViewById(R.id.ll_grade);
        ll_release = (LinearLayout) findViewById(R.id.ll_release);
        ll_tuling = (LinearLayout) findViewById(R.id.ll_tuling);
        dateTextView = (TextView) findViewById(R.id.swpu_main_TextDate);
        weekTextView = (TextView) findViewById(R.id.swpu_main_TextWeeks);
        if (savedInstanceState != null) {
            mActiveViewId = savedInstanceState.getInt(STATE_ACTIVE_VIEW_ID);
        }
        // 初始化Dao成员变量
        gInfoDao = new GlobalInfoBiz(this);
        uDao = new UserDataBiz(this);
        pDao = new PersonalInfoBiz(this);
        prDao = new RollInfoBiz(this);
        courseInfoBiz = new CourseInfoBiz(this);
        // 初始化数据模型变量
        gInfo = gInfoDao.query();
        uDao.query(gInfo.getActiveUserUid());
        BaseApplication.getInstance().setIsLocal(false);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        ll_menu.setOnClickListener(this);
        ll_course.setOnClickListener(this);
        ll_exam.setOnClickListener(this);
        ll_rate.setOnClickListener(this);
        ll_grade.setOnClickListener(this);
        ll_release.setOnClickListener(this);
        ll_tuling.setOnClickListener(this);
        Menu_main_home.setOnClickListener(this);
        Menu_main_personal.setOnClickListener(this);
        Menu_main_message.setOnClickListener(this);
        Menu_main_map.setOnClickListener(this);
        Menu_main_feedback.setOnClickListener(this);
        Menu_main_update.setOnClickListener(this);
        Menu_main_about.setOnClickListener(this);
        Menu_main_logout.setOnClickListener(this);
        Menu_main_exit.setOnClickListener(this);
        myLocation.setOnClickListener(this);
        DialogUtil.showLoading(this, Const.GET_MY_LOCAZTION_EVENT);
        initLocation();
        locationService.start();// 定位SDK
        initDate();
        initWeek();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_menu:    //菜单
                mMenuDrawer.toggleMenu();
                break;
            case R.id.ll_course:  //我的课表
                CourseActivity.launch(this);
                break;
            case R.id.ll_exam:    //考试信息
                ExamActivity.launch(this);
                break;
            case R.id.ll_grade:   //成绩绩点
                GradeActivity.launch(this);
                break;
            case R.id.ll_rate:    //翻译助手
                TranslateActivity.launch(this);
                break;
            case R.id.ll_release:  //轻松一刻
                TakeReleaseActivity.launch(this);
                break;
            case R.id.ll_tuling:    //图灵机器人
                TuringActivity.launch(this);
                break;
            case R.id.Menu_main_home:

                break;
            case R.id.Menu_main_personal:      //个人信息
                PersonalActivity.launch(this);
                break;
            case R.id.Menu_main_message:       //定位
                DialogUtil.showLoading(this, Const.GET_MY_LOCAZTION_EVENT);
                initLocation();
                locationService.start();// 定位SDK
                mMenuDrawer.closeMenu();
                break;
            case R.id.Menu_main_map:      //地图
                MapActivity.launch(this, true);
                break;
            case R.id.Menu_main_feedback:       //反馈
                DialogUtil.showToast(this, "暂未开通,敬请期待", 0);
                mMenuDrawer.closeMenu();
                break;
            case R.id.Menu_main_update:       //更新
                DialogUtil.showLoading(this, Const.PERSONALINFO_EVENT);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtil.dissMissLoading(Const.PERSONALINFO_EVENT);
                        mMenuDrawer.closeMenu();
                        DialogUtil.showToast(MainActivity.this, "已是最新版本", 0);
                    }
                }, 2000);

                break;
            case R.id.Menu_main_about:        //关于

                break;
            case R.id.Menu_main_logout:       //注销
                userLogout();
                break;
            case R.id.menu_main_exit:        //退出
                BaseApplication.getInstance().exit();
                break;
            case R.id.myLocation:
                showMyAddress();
                break;
            default:
                break;
        }
    }

    private void showMyAddress() {
        alertDialog = new AlertDialog.Builder(this).setPositiveButton("好的", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        }).setNegativeButton("查看地图", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MapActivity.launch(MainActivity.this, true);
            }
        }).setNeutralButton("重定位", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogUtil.showLoading(MainActivity.this, Const.GET_MY_LOCAZTION_EVENT);
                initLocation();
                locationService.start();// 定位SDK
            }
        }).setMessage(showMyAddress).create();
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mMenuDrawer.isMenuVisible() == false) {
                mMenuDrawer.openMenu();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            mMenuDrawer.toggleMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        final int drawerState = mMenuDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }
        super.onBackPressed();
    }

    public static void launch(Activity activity) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, MainActivity.class)) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        // -----------location config ------------
        locationService = ((BaseApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }

    }

    private void initDate() {
        Date currentTime = new Date();
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)  w = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日  ");
        String dateString = formatter.format(currentTime);
        dateTextView.setText(dateString + "星期" + weekDays[w]);
    }

    private void initWeek() {
        weekTextView.setText("第" + Utility.getWeeks(gInfo.getTermBegin()) + "教学周");
        BaseApplication.getInstance().setCurrentWeek(Utility.getWeeks(gInfo.getTermBegin()));
    }

    private void userLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("您确定要注销登录吗？");
        builder.setMessage("注销登录将清空您的所有个人信息");
        builder.setPositiveButton("注销", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final int uid = gInfo.getActiveUserUid();
                //TODO 删除其他用户相关信息
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uDao.delete(uid);
                        pDao.delete(uid);
                        prDao.delete(uid);
                        for(int i = 0; i <= uid; i++) {
                            courseInfoBiz.delete(i);
                        }
//                courseInfoBiz.delete(uid);
                        BaseApplication.getInstance().getCourseInfoList().clear();
                    }
                }).start();

                gInfo.setActiveUserUid(0);
                gInfoDao.insert(gInfo);

                Intent intent=new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        builder.create().show();
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            DialogUtil.dissMissLoading(Const.GET_MY_LOCAZTION_EVENT);
            final Poi poi;
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//                StringBuffer sb = new StringBuffer(256);
//                sb.append("time : ");

//                sb.append(location.getTime());
//                sb.append("\nerror code : ");
//                sb.append(location.getLocType());
//                sb.append("\nlatitude : ");
//                sb.append(location.getLatitude());
//                sb.append("\nlontitude : ");
//                sb.append(location.getLongitude());
//                sb.append("\nradius : ");
//                sb.append(location.getRadius());
//                sb.append("\nCountryCode : ");
//                sb.append(location.getCountryCode());
//                sb.append("\nCountry : ");
//                sb.append(location.getCountry());
//                sb.append("\ncitycode : ");
//                sb.append(location.getCityCode());
//                sb.append("\ncity : ");
//                sb.append(location.getCity());
//                sb.append("\nDistrict : ");
//                sb.append(location.getDistrict());
//                sb.append("\nStreet : ");
//                sb.append(location.getStreet());
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\nDescribe: ");
//                sb.append(location.getLocationDescribe());
//                sb.append("\nDirection(not all devices have value): ");
//                sb.append(location.getDirection());
//                sb.append("\nPoi: ");
//                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = (Poi) location.getPoiList().get(i);
//                        sb.append(poi.getName() + ";");
//                    }
//                }
//                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                    sb.append("\nspeed : ");
//                    sb.append(location.getSpeed());// 单位：km/h
//                    sb.append("\nsatellite : ");
//                    sb.append(location.getSatelliteNumber());
//                    sb.append("\nheight : ");
//                    sb.append(location.getAltitude());// 单位：米
//                    sb.append("\ndescribe : ");
//                    sb.append("gps定位成功");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
//                    // 运营商信息
//                    sb.append("\noperationers : ");
//                    sb.append(location.getOperators());
//                    sb.append("\ndescribe : ");
//                    sb.append("网络定位成功");
//                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                    sb.append("\ndescribe : ");
//                    sb.append("离线定位成功，离线定位结果也是有效的");
//                } else if (location.getLocType() == BDLocation.TypeServerError) {
//                    sb.append("\ndescribe : ");
//                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
//                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//                }
////                logMsg(sb.toString());
////                Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_LONG).show();
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                showMyLocation = location.getCity() + location.getDistrict() + location.getStreet();
                for (int i = 0; i < location.getPoiList().size(); i++) {
                    Poi position = (Poi) location.getPoiList().get(i);
                    Log.i("poi" + i, position.getName() + "");
                }
                poi = (Poi) location.getPoiList().get(0);
                showMyLocation += poi.getName();
                showMyAddress = location.getAddrStr();
                if (showMyLocation.isEmpty()) {

                } else {
                    //定位成功
                    locationService.stop();
                    Const.LATITUDE_OF_MY_LOCATION = location.getLatitude();
                    Const.LONGITUDE_OF_MY_LOCATION = location.getLongitude();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.showToast(MainActivity.this, showMyLocation, 1);
                            if(poi.getName().length() < 6) {
                                myLocation.setText(poi.getName());
                            }
                        }
                    });
                }
            }
        }

    };


    //把输入流转换成String
//    private static String changeInputeStream(InputStream inputStream, String ecode) {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//        StringBuilder sb = new StringBuilder();
//
//        String line = null;
//        try {
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return sb.toString();
//    }
}
