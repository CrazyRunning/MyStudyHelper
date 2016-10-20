package com.my_swpu.mystudyhelper.util;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.my_swpu.mystudyhelper.biz.LocationBiz;


/**
 * 定位工具类
 * 可获取定位的经度和纬度
 */
public class MyBaiduLocation {
	Context myContext;

	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 4000;
	private static int LOCATION_COUTNS = 0;
	private boolean isFinish = false;
	public MyBDcoordinate myBDcoordinate = null;
	LocationBiz myLocation;
	String strlocation = "";

	public MyBaiduLocation(Context context) {
		// TODO Auto-generated constructor stub
		myContext = context;
		myLocation = new LocationBiz();
		initLockPst();

	}

	class MyBDcoordinate {
		double Latitude;
		double Longitude;
	}

	private void initLockPst() {
		locationClient = new LocationClient(this.myContext);
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
		option.setProdName("LocationDemo"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setScanSpan(UPDATE_TIME); // 设置定时定位的时间间隔。单位毫秒
		locationClient.setLocOption(option);

		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (myBDcoordinate != null) {
					stopOpetateClient();
					// locationInfoTextView.setText("stop" + LOCATION_COUTNS);
					return;
				}
				if (LOCATION_COUTNS > 5) {

					stopOpetateClient();
					return;
				}
				if (location == null) {
					LOCATION_COUTNS++;
					return;
				}
				// location.getLocType();
				// location.getLatitude()
				// location.getLongitude();
				if (location.getLocType() != 161) {
					LOCATION_COUTNS++;
					return;
				}
				myBDcoordinate = new MyBDcoordinate();
				myBDcoordinate.Latitude = location.getLatitude();
				myBDcoordinate.Longitude = location.getLongitude();

			}

//			@Override
//			public void onReceivePoi(BDLocation location) {
//			}

		});
	}

	private void stopOpetateClient() {
		locationClient.stop();
		isFinish = true;
	}

	private void startOpetateClient() {
		locationClient.start();
		/*
		 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
		 * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
		 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
		 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
		 */
		isFinish = false;
		locationClient.requestLocation();
	}

	public boolean getIsFinish() {// 获取定位是否完成或终止
		return isFinish;
	}

	public void opetateClient() {// 开始或停止。
		if (locationClient == null) {
			return;
		}
		if (locationClient.isStarted()) {
			stopOpetateClient();
		} else {
			startOpetateClient();
			/*
			 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
			 * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
			 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
			 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
			 */
			locationClient.requestLocation();

		}
	}

	/*********************************/
	public double getLatValue() {// 纬度
		return myBDcoordinate.Latitude;
	}

	public double getLongValue() {// 经度
		return myBDcoordinate.Longitude;
	}

	public void desClient() {// 当处在定位时Activity销毁时调用
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
	}
}
