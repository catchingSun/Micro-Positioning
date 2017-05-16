package com.qrcode.location.map;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qrcode.location.R;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class InWebMapPosition {
	private String TAG = InWebMapPosition.class.getName();
	private Handler mHandler = null;
	private WebView mWebView = null;
	protected String LocationName = "";
	protected int LocationX = -1;
	protected int LocationY = -1;

	private static String mLocationName = "";
	private static int mLocationX = -1;
	private static int mLocationY = -1;
	
	private static int mStartEndPointHeight = 20;
	private static int mStartEndPointWeight = 10;
	private static int mBiaoZhuSVGHeight = 27;
	private static int mBiaoZhuSVGWeight = 11;

	protected String isOpenFromQRcode = "false";
	protected String isOpenFromRoutePlan = "false";

	public static int mRoutePointCount;
	
	private static int mRoutePointOrder[];
	private static int mRoutePointX[];
	private static int mRoutePointY[];
	private static int mStartLocationX;
	private static int mStartLocationY;
	private static int mEndLocationX;
	private static int mEndLocationY;

	// MapActivity mapactivity = new MapActivity();

	public InWebMapPosition(Activity activity, Handler handler) {
		this.mWebView = (WebView) activity.findViewById(R.id.wv_map_view);
		this.mHandler = handler;
		if (MapActivity.isQRBundle.equals("true")) {
			isOpenFromQRcode = "true";
			this.LocationName = MapActivity.LocationName;
			this.LocationX = MapActivity.LocationX;
			this.LocationY = MapActivity.LocationY;
		} else if (MapActivity.isQRBundle.equals("false")) {
			isOpenFromRoutePlan = "true";
			InWebMapPosition.mRoutePointOrder = MapActivity.mRoutePointOrder;
			InWebMapPosition.mRoutePointX = MapActivity.mRoutePointX;
			InWebMapPosition.mRoutePointY = MapActivity.mRoutePointY;
			InWebMapPosition.mEndLocationX = MapActivity.mEndLocationX;
			InWebMapPosition.mEndLocationY = MapActivity.mEndLocationY;
			InWebMapPosition.mStartLocationX = MapActivity.mStartLocationX;
			InWebMapPosition.mStartLocationY = MapActivity.mStartLocationY;
		}
	}

	protected String getLocation() {
		return LocationName;
	}

	protected int getAbscissa() {
		return LocationX;
	}

	protected int getOrdinate() {
		return LocationY;
	}
	
	// 设置方法类型必须为public否则js不可访问
	@JavascriptInterface
	public void init() {
		// 通过handler来确保init方法的执行在handler绑定的Activity的主线程中
		mHandler.post(new Runnable() {
			public void run() {
				Log.e(TAG, "init");
				mWebView.loadUrl("javascript:loadFunction('" + getJsonStr()
						+ "')");
			}
		});
	}

	/** 设置要传递给js的数据 */
	protected String getJsonStr() {
		Log.e(TAG, "getJsonStr");
		mLocationName = LocationName;
		mLocationX = LocationX;
		mLocationY = LocationY;
		try {
			JSONObject object = new JSONObject();
			object.put("isOpenFromQRcode", isOpenFromQRcode);
			object.put("isOpenFromRoutePlan", isOpenFromRoutePlan);

			/* 传递扫描二维码获取的位置值 */
			JSONObject locationobject = new JSONObject();
			locationobject.put("location", mLocationName);
			locationobject.put("abscissa", mLocationX);
			locationobject.put("ordinate", mLocationY);

			/* 传递路径规划相关数据 */
			JSONObject routeplanobject = new JSONObject();

			ArrayList<Integer> routePointOrder = new ArrayList<Integer>();
			ArrayList<Integer> routePointX = new ArrayList<Integer>();
			ArrayList<Integer> routePointY = new ArrayList<Integer>();
			if (isOpenFromRoutePlan.equals("true") && mRoutePointOrder.length != 0) {
				routePointOrder.add(1);
				for (int i = 0; i < mRoutePointOrder.length; i++) {
					routePointOrder.add(mRoutePointOrder[i] + 1);
				}
				routePointOrder
						.add(mRoutePointOrder[mRoutePointOrder.length - 1] + 2);

				routePointX.add(mStartLocationX + mBiaoZhuSVGWeight);
				for (int i = 0; i < mRoutePointOrder.length; i++) {
					routePointX.add(mRoutePointX[i] - mStartEndPointWeight);
				}
				routePointX.add(mEndLocationX + mBiaoZhuSVGWeight);

				routePointY.add(mStartLocationY + mBiaoZhuSVGHeight);
				for (int i = 0; i < mRoutePointOrder.length; i++) {
					routePointY.add(mRoutePointY[i] - mStartEndPointHeight);
				}
				routePointY.add(mEndLocationY + mBiaoZhuSVGHeight);
			}
			routeplanobject.put("RoutePointOrder", routePointOrder);
			routeplanobject.put("RoutePointX", routePointX);
			routeplanobject.put("RoutePointY", routePointY);
			routeplanobject.put("EndLocationX", mEndLocationX);
			routeplanobject.put("EndLocationY", mEndLocationY);
			routeplanobject.put("StartLocationX", mStartLocationX);
			routeplanobject.put("StartLocationY", mStartLocationY);

			JSONArray jsonArray = new JSONArray();
			jsonArray.put(object);
			if (isOpenFromQRcode.equals("true")) {
				jsonArray.put(locationobject);
			} else if (isOpenFromRoutePlan.equals("true")) {
				jsonArray.put(routeplanobject);
			}
			Log.e(TAG, jsonArray.toString());
			isOpenFromQRcode = "false";
			isOpenFromRoutePlan = "false";
			MapActivity.isQRBundle = "";
			return jsonArray.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
