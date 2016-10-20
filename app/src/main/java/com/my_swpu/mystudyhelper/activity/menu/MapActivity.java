package com.my_swpu.mystudyhelper.activity.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.my_swpu.mystudyhelper.BaseApplication;
import com.my_swpu.mystudyhelper.R;
import com.my_swpu.mystudyhelper.activity.BaseActivity;
import com.my_swpu.mystudyhelper.util.CommonUtils;
import com.my_swpu.mystudyhelper.util.Const;
import com.my_swpu.mystudyhelper.util.DialogUtil;
import com.my_swpu.mystudyhelper.util.LocationService;
import com.my_swpu.mystudyhelper.view.EditTextWithFrame;
import com.my_swpu.mystudyhelper.view.widget.WalkingRouteOverlay;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    /**
     * MapView 是地图主控件
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatLng myLatLng;
    private BitmapDescriptor bd;
    private LinearLayout ll_back_on_mapactivity;
    private FrameLayout fl_mapactivity;
    private Button btClear, btLoca, btSearch;
    /**
     * 搜索
     */
    private View dialog_show;
    private EditTextWithFrame etFrom, etEnd;
    private AlertDialog alertDialog;

    /**
     * 定位服务
     */
    private LocationService locationService = null;
    private Map<String, String> map;
    private MapStatus mMapStatus;
    private BufferedReader in = null;
    private String fromAddress, endAddress;
    private MapStatusUpdate mMapStatusUpdate;

    private String showMyLocation;

    //搜索
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    /**
     * 导航
     */
    private BNRoutePlanNode sNode;    //导航出发地节点
    private BNRoutePlanNode eNode;    //导航目的地节点
    private boolean isNaviOrWalk = false;   //是否为导航还是步行
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private String mSDCardPath = null;
    private static final String APP_FOLDER_NAME = "MyStudyHelper";
    private String authinfo = "";
    private boolean isFrom = false;
    private boolean isShow = false;     //是否演示
    private AlertDialog isShowDialog;

    private boolean useDefaultIcon = false;
    private boolean hasLocation = false;

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    showToastMsg("Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    showToastMsg("Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };
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
//                showMyAddress = location.getAddrStr();
                if (showMyLocation.isEmpty()) {

                } else {
                    //定位成功
                    locationService.stop();
                    Const.LATITUDE_OF_MY_LOCATION = location.getLatitude();
                    Const.LONGITUDE_OF_MY_LOCATION = location.getLongitude();
                    MapActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DialogUtil.showToast(MapActivity.this, showMyLocation, 1);
                            if(!hasLocation){
                                initMapView();
                            }
                        }
                    });
                }
            }
        }

    };

    /**
     * 出发地搜索线程
     */
    private Runnable getFromLatitude = new Runnable() {
        @Override
        public void run() {
            try {
                //将地址转换成utf-8的16进制
                String address = "";
                address = fromAddress;
                address = URLEncoder.encode(address, "UTF-8");
//       如果有代理，要设置代理，没代理可注释
//      System.setProperty("http.proxyHost","192.168.1.188");
//      System.setProperty("http.proxyPort","3128");
                URL tirc = new URL("http://api.map.baidu.com/geocoder?address=" + address + "&output=json&key=" + Const.KEY_1);

                in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
                String res;
                StringBuilder sb = new StringBuilder("");
                while ((res = in.readLine()) != null) {
                    sb.append(res.trim());
                }
                String str = sb.toString();
//                Map<String,String> map = null;
                if (!str.isEmpty()) {
                    int lngStart = str.indexOf("lng\":");
                    int lngEnd = str.indexOf(",\"lat");
                    int latEnd = str.indexOf("},\"precise");
                    if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                        String lng = str.substring(lngStart + 5, lngEnd);
                        String lat = str.substring(lngEnd + 7, latEnd);
                        Log.i("lat", lat);
                        Log.i("lng", lng);
                        map.put("lng0", lng);
                        map.put("lat0", lat);
                        sNode = new BNRoutePlanNode(Double.valueOf(lng), Double.valueOf(lat), fromAddress, null, BNRoutePlanNode.CoordinateType.BD09LL);
                        new Thread(getEndLatitude).start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(networkFail);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(getLatitudeFail);
                }
            }
        }
    };
    /**
     * 目的地搜索线程
     */
    private Runnable getEndLatitude = new Runnable() {
        @Override
        public void run() {
            try {
                //将地址转换成utf-8的16进制
                String address = "";
                address = endAddress;
                address = URLEncoder.encode(address, "UTF-8");
//       如果有代理，要设置代理，没代理可注释
//      System.setProperty("http.proxyHost","192.168.1.188");
//      System.setProperty("http.proxyPort","3128");
                URL tirc = new URL("http://api.map.baidu.com/geocoder?address=" + address + "&output=json&key=" + Const.KEY_1);

                in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
                String res;
                StringBuilder sb = new StringBuilder("");
                while ((res = in.readLine()) != null) {
                    sb.append(res.trim());
                }
                String str = sb.toString();
//                Map<String,String> map = null;
                if (!str.isEmpty()) {
                    int lngStart = str.indexOf("lng\":");
                    int lngEnd = str.indexOf(",\"lat");
                    int latEnd = str.indexOf("},\"precise");
                    if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                        String lng = str.substring(lngStart + 5, lngEnd);
                        String lat = str.substring(lngEnd + 7, latEnd);
                        Log.i("lat", lat);
                        Log.i("lng", lng);
                        map.put("lng", lng);
                        map.put("lat", lat);
                        eNode = new BNRoutePlanNode(Double.valueOf(lng), Double.valueOf(lat), endAddress, null, BNRoutePlanNode.CoordinateType.BD09LL);
                        if (isNaviOrWalk) {
                            //导航
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (BaiduNaviManager.isNaviInited()) {
                                        isShowDialog.show();
                                    }
                                }
                            });

                        } else {
                            //步行路径规划
                            runOnUiThread(networkSuccess);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(networkFail);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(getLatitudeFail);
                }
            }
        }
    };


    private Runnable networkSuccess = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading();
            //设定中心点坐标
            double lat0 = 0;
            double lng0 = 0;
            if (isFrom) {
                //自定义出发地坐标
                lat0 = Double.valueOf(map.get("lat0"));
                lng0 = Double.valueOf(map.get("lng0"));
            }
            //目的地坐标
            double lat = Double.valueOf(map.get("lat"));
            double lng = Double.valueOf(map.get("lng"));
            myLatLng = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions().position(myLatLng).icon(bd);
            mBaiduMap.addOverlay(markerOptions);
            mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            LatLng stlatLng;
            if (isFrom) {
                stlatLng = new LatLng(lat0, lng0);
            } else {
                stlatLng = new LatLng(Const.LATITUDE_OF_MY_LOCATION, Const.LONGITUDE_OF_MY_LOCATION);
            }
            LatLng enlatLng = new LatLng(lat, lng);
            PlanNode stNode = PlanNode.withLocation(stlatLng);
            PlanNode enNode = PlanNode.withLocation(enlatLng);
            mSearch.walkingSearch((new WalkingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));

        }
    };

    private Runnable networkFail = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading();
            DialogUtil.showToast(MapActivity.this, "网络连接错误，请稍后再试", 0);
        }
    };

    private Runnable getLatitudeFail = new Runnable() {
        @Override
        public void run() {
            DialogUtil.dissMissLoading();
            DialogUtil.showToast(MapActivity.this, "地址不详细", 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLocation();
        hasLocation = getIntent().getBooleanExtra("hasLocation", false);
        if(hasLocation) {
            initMapView();
        }else{
            locationService.start();
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
//        mMapView.onResume();
        super.onResume();

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
    protected void onDestroy() {
        bd.recycle();
        mMapView.onDestroy();
        super.onDestroy();


    }

    @Override
    protected void OnInitUiAndData() {
        super.OnInitUiAndData();

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        ll_back_on_mapactivity = (LinearLayout) findViewById(R.id.ll_back_on_mapactivity);
        fl_mapactivity = (FrameLayout) findViewById(R.id.fl_mapactivity);
        btSearch = (Button) findViewById(R.id.btSearch);
        btClear = (Button) findViewById(R.id.btClear);
        btLoca = (Button) findViewById(R.id.btLoca);
        dialog_show = LayoutInflater.from(this).inflate(R.layout.dialog_show, null);
        etFrom = (EditTextWithFrame) dialog_show.findViewById(R.id.etFrom);
        etEnd = (EditTextWithFrame) dialog_show.findViewById(R.id.etEnd);

    }



    /**
     * 初始化地图
     */
    private void initMapView() {
        //设定中心点坐标

        myLatLng = new LatLng(Const.LATITUDE_OF_MY_LOCATION,Const.LONGITUDE_OF_MY_LOCATION);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(myLatLng)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        bd = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        MarkerOptions markerOptions = new MarkerOptions().position(myLatLng).icon(bd);
        mBaiduMap.addOverlay(markerOptions);
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    @Override
    protected void OnBindDataWithUi() {
        super.OnBindDataWithUi();
        ll_back_on_mapactivity.setOnClickListener(this);
        fl_mapactivity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        btClear.setOnClickListener(this);
        btLoca.setOnClickListener(this);
        btSearch.setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(this)
                .setPositiveButton("步行", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initSearch();
                        isNaviOrWalk = false;
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("导航", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //导航
                        initSearch();
                        isNaviOrWalk = true;
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(dialog_show);
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        isShowDialog = new AlertDialog.Builder(MapActivity.this)
                .setTitle("是否演示？")
                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isShow = false;
                        dialog.dismiss();
                        if (BaiduNaviManager.isNaviInited()) {
                            readyNavi();
                        }
                    }
                })
                .setNegativeButton("进入导航", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isShow = true;
                        dialog.dismiss();
                        if (BaiduNaviManager.isNaviInited()) {
                            readyNavi();
                        }
                    }
                })
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        // 初始化搜索模块

        if (initDirs()) {
            initNavi();
        }
    }


    /**
     * 设置底图显示模式
     *
     * @param view
     */
    public void setMapMode(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.normal:
                if (checked) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.statellite:
                if (checked) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置是否显示交通图
     *
     * @param view
     */
    public void setTraffic(View view) {
        mBaiduMap.setTrafficEnabled(((CheckBox) view).isChecked());
    }

    /**
     * 设置是否显示百度热力图
     *
     * @param view
     */
    public void setBaiduHeatMap(View view) {
        mBaiduMap.setBaiduHeatMapEnabled(((CheckBox) view).isChecked());
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

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 初始化导航
     */
    private void initNavi() {

        try {
            BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
                @Override
                public void onAuthResult(int status, String msg) {

                    if (0 == status) {
                        authinfo = "key校验成功!";
                    } else {
                        authinfo = "key校验失败, " + msg;
                    }
                    MapActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(MapActivity.this, authinfo, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                public void initSuccess() {
                    Toast.makeText(MapActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                    initSetting();
                }

                public void initStart() {
                    Toast.makeText(MapActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                }

                public void initFailed() {
                    Toast.makeText(MapActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                }


            }, null, ttsHandler, null);
        } catch (Exception e) {
            Log.i("导航初始化", e.getMessage().toString());
        }
    }

    private void initSetting() {
        BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
        BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back_on_mapactivity:
//                onBackPressed();
                finish();
                break;
            case R.id.btClear:    //清空
                mBaiduMap.clear();
                initMapView();
                break;
            case R.id.btSearch:   //搜索
                map = new HashMap<>();
                alertDialog.show();
                break;
            case R.id.btLoca:     //定位
                locationService.start();
                initMapView();
                break;
            default:
                break;
        }
    }

    public void showToastMsg(final String msg) {
        MapActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(MapActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        fromAddress = etFrom.getText().toString().trim();
        endAddress = etEnd.getText().toString().trim();
        if (!TextUtils.isEmpty(fromAddress) && !TextUtils.isEmpty(endAddress)) {
            DialogUtil.showLoading(MapActivity.this);
            if (fromAddress.equals("我的位置")) {
                isFrom = false;
                sNode = new BNRoutePlanNode(Const.LONGITUDE_OF_MY_LOCATION, Const.LATITUDE_OF_MY_LOCATION, "我的位置", null, BNRoutePlanNode.CoordinateType.BD09LL);
                new Thread(getEndLatitude).start();
            } else {
                isFrom = true;
                new Thread(getFromLatitude).start();
            }
        } else {
            DialogUtil.showToast(MapActivity.this, "请输入出发地和目的地", Toast.LENGTH_SHORT);
        }
    }

    /**
     * 准备导航
     */
    private void readyNavi() {
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, isShow, new NaviRoutePlanListener(sNode));
        }
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            DialogUtil.showToast(this, "抱歉，未找到结果", 0);
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
//            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
//            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }

    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    private class NaviRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;

        public NaviRoutePlanListener(BNRoutePlanNode node) {
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            DialogUtil.dissMissLoading();
            for (Activity ac : BaseApplication.activities) {

                if (ac.getClass().getName().endsWith("BNGuideActivity")) {

                    return;
                }
            }
            BNGuideActivity.launch(MapActivity.this, ROUTE_PLAN_NODE, mBNRoutePlanNode);
            finish();
        }

        @Override
        public void onRoutePlanFailed() {
            Toast.makeText(MapActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
        }
    }



    public static void launch(Activity activity, boolean hasLocation) {
        if (!CommonUtils.isActivityAreRunningBefore(activity, MapActivity.class)) {
            Intent intent = new Intent(activity, MapActivity.class);
            intent.putExtra("hasLocation", hasLocation);
            activity.startActivity(intent);
        }
    }
}
