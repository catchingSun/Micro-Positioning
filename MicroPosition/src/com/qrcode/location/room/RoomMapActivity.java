package com.qrcode.location.room;

import java.util.ArrayList;
import java.util.HashMap;

import com.qrcode.location.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.JsResult;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressLint("NewApi")
public class RoomMapActivity extends Fragment {

	protected static final Context Context = null;
	// 存储数据的数组列表
	ArrayList<HashMap<String, Object>> listData;
	// 适配器
	SimpleAdapter listItemAdapter;
	int n = 0;
	private String tag;
	int a;
	private WebView webView1, webView2, webView3, webView4, webView5;
	private ListView lv1, lv2, lv3, lv4, lv5;
	int i;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		String a1 = "1教";
		String a2 = "2教";
		String a3 = "3教";
		String a4 = "实验楼";
		String a5 = "9教";
		String a6 = "10教";
		String a7 = "11教";
		String a8 = "12教";
		String a9 = "13教";
		String a10 = "14教";
		String a11 = "15教";
		String a12 = "16教";
		String a13 = "17教";
		String a14 = "18教";
		String a15 = "行政楼";
		String a16 = "综合楼";

		tag = getTag();
		// 得到下拉框状态值
		if (a1.equals(tag)) {
			a = 1;
		}
		if (a2.equals(tag)) {
			a = 2;
		}
		if (a3.equals(tag)) {
			a = 3;
		}
		if (a4.equals(tag)) {
			a = 4;
		}
		if (a5.equals(tag)) {
			a = 5;
		}
		if (a6.equals(tag)) {
			a = 6;
		}
		if (a7.equals(tag)) {
			a = 7;
		}
		if (a8.equals(tag)) {
			a = 8;
		}
		if (a9.equals(tag)) {
			a = 9;
		}
		if (a10.equals(tag)) {
			a = 10;
		}
		if (a11.equals(tag)) {
			a = 11;
		}
		if (a12.equals(tag)) {
			a = 12;
		}
		if (a13.equals(tag)) {
			a = 13;
		}
		if (a14.equals(tag)) {
			a = 14;
		}
		if (a15.equals(tag)) {
			a = 15;
		}
		if (a16.equals(tag)) {
			a = 16;
		}

	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 数据库查询实例开始
		RoomDatabaseOperation s = new RoomDatabaseOperation();
		SQLiteDatabase db = s.openDatabase(container.getContext());
		Cursor cor = db.rawQuery("select * from FloorNo16 ", new String[] {});
		Cursor cor03 = db.rawQuery("select * from FloorNo03 ", new String[] {});

		cor.getCount();
		cor.getColumnCount();
		String[] shiliu1 = new String[9]; // 需要的字符串数组
		String[] shiliu2 = new String[8];
		String[] shiliu3 = new String[22];
		String[] shiliu4 = new String[11];
		String[] shiliu5 = new String[11];
		String[] sanjiao1 = new String[11]; // 需要的字符串数组
		String[] sanjiao2 = new String[16];
		String[] sanjiao3 = new String[16];
		String[] sanjiao4 = new String[16];
		String[] sanjiao5 = new String[16];
		System.out.println("数据库查询测试");
		// for循环查询16教元素
		cor.moveToPosition(1);
		for (n = 0; n < 9; n++) {
			String temp1 = cor.getString(cor.getColumnIndex("RoomId"));
			String temp2 = cor.getString(cor.getColumnIndex("RoomMessage"));
			String temp3 = cor.getString(cor.getColumnIndex("RoomAction"));
			shiliu1[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor.moveToNext();
		}
		// for循环查询16教元素
		cor.moveToPosition(11);
		for (n = 0; n < 8; n++) {
			String temp1 = cor.getString(cor.getColumnIndex("RoomId"));
			String temp2 = cor.getString(cor.getColumnIndex("RoomMessage"));
			String temp3 = cor.getString(cor.getColumnIndex("RoomAction"));
			shiliu2[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor.moveToNext();
		}
		// for循环查询16教元素
		cor.moveToPosition(20);
		for (n = 0; n < 22; n++) {
			String temp1 = cor.getString(cor.getColumnIndex("RoomId"));
			String temp2 = cor.getString(cor.getColumnIndex("RoomMessage"));
			String temp3 = cor.getString(cor.getColumnIndex("RoomAction"));
			shiliu3[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor.moveToNext();
		}
		// for循环查询16教元素
		cor.moveToPosition(43);
		for (n = 0; n < 11; n++) {
			String temp1 = cor.getString(cor.getColumnIndex("RoomId"));
			String temp2 = cor.getString(cor.getColumnIndex("RoomMessage"));
			String temp3 = cor.getString(cor.getColumnIndex("RoomAction"));
			shiliu4[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor.moveToNext();
		}
		// for循环查询16教元素
		cor.moveToPosition(55);
		for (n = 0; n < 11; n++) {
			String temp1 = cor.getString(cor.getColumnIndex("RoomId"));
			String temp2 = cor.getString(cor.getColumnIndex("RoomMessage"));
			String temp3 = cor.getString(cor.getColumnIndex("RoomAction"));
			shiliu5[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor.moveToNext();
		}

		// for循环查询3教元素
		cor03.moveToPosition(1);
		for (n = 0; n < 11; n++) {
			String temp1 = cor03.getString(cor03.getColumnIndex("RoomId"));
			String temp2 = cor03.getString(cor03.getColumnIndex("RoomMessage"));
			String temp3 = cor03.getString(cor03.getColumnIndex("RoomAction"));
			sanjiao1[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor03.moveToNext();
		}
		// for循环查询3教元素
		System.out.println(sanjiao1[1]);
		System.out.println(n);

		cor03.moveToPosition(13);
		for (n = 0; n < 16; n++) {
			System.out.println(n);
			String temp1 = cor03.getString(cor03.getColumnIndex("RoomId"));
			String temp2 = cor03.getString(cor03.getColumnIndex("RoomMessage"));
			String temp3 = cor03.getString(cor03.getColumnIndex("RoomAction"));
			sanjiao2[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor03.moveToNext();
		}
		// for循环查询3教元素
		cor03.moveToPosition(30);
		for (n = 0; n < 16; n++) {
			String temp1 = cor03.getString(cor03.getColumnIndex("RoomId"));
			String temp2 = cor03.getString(cor03.getColumnIndex("RoomMessage"));
			String temp3 = cor03.getString(cor03.getColumnIndex("RoomAction"));
			sanjiao3[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor03.moveToNext();
		}
		// for循环查询3教元素
		cor03.moveToPosition(47);
		for (n = 0; n < 16; n++) {
			String temp1 = cor03.getString(cor03.getColumnIndex("RoomId"));
			String temp2 = cor03.getString(cor03.getColumnIndex("RoomMessage"));
			String temp3 = cor03.getString(cor03.getColumnIndex("RoomAction"));
			sanjiao4[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor03.moveToNext();
		}
		// for循环查询3教元素
		cor03.moveToPosition(64);
		for (n = 0; n < 16; n++) {
			String temp1 = cor03.getString(cor03.getColumnIndex("RoomId"));
			String temp2 = cor03.getString(cor03.getColumnIndex("RoomMessage"));
			String temp3 = cor03.getString(cor03.getColumnIndex("RoomAction"));
			sanjiao5[n] = "房号:" + temp1 + "; 简介:" + temp2 + "; 用途:" + temp3;
			cor03.moveToNext();
		}
		cor.close();
		cor03.close();

		TextView tv = new TextView(getActivity());

		webView1 = (WebView) container.findViewById(R.id.webView1);
		webView2 = (WebView) container.findViewById(R.id.webView2);
		webView3 = (WebView) container.findViewById(R.id.webView3);
		webView4 = (WebView) container.findViewById(R.id.webView4);
		webView5 = (WebView) container.findViewById(R.id.webView5);

		WebSettings webSettings1 = webView1.getSettings();
		WebSettings webSettings2 = webView2.getSettings();
		WebSettings webSettings3 = webView3.getSettings();
		WebSettings webSettings4 = webView4.getSettings();
		WebSettings webSettings5 = webView5.getSettings();

		webSettings1.setJavaScriptEnabled(true);// 设置支持javascript脚本
		webSettings1.setUseWideViewPort(true);
		webSettings1.setLoadWithOverviewMode(true);
		webSettings2.setJavaScriptEnabled(true);// 设置支持javascript脚本
		webSettings2.setUseWideViewPort(true);
		webSettings2.setLoadWithOverviewMode(true);
		webSettings3.setJavaScriptEnabled(true);// 设置支持javascript脚本
		webSettings3.setUseWideViewPort(true);
		webSettings3.setLoadWithOverviewMode(true);
		webSettings4.setJavaScriptEnabled(true);// 设置支持javascript脚本
		webSettings4.setUseWideViewPort(true);
		webSettings4.setLoadWithOverviewMode(true);
		webSettings5.setJavaScriptEnabled(true);// 设置支持javascript脚本
		webSettings5.setUseWideViewPort(true);
		webSettings5.setLoadWithOverviewMode(true);
		webView1.getSettings().setBuiltInZoomControls(true);// 会出现放大缩小的按钮
		webView1.getSettings().setSupportZoom(true);// 支持缩放
		webView1.getSettings().setSupportMultipleWindows(true);
		webView1.getSettings().setDomStorageEnabled(true);
		webView1.setInitialScale(45);// 初始显示比例
		webView1.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
		webView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView1.requestFocus();

		webView2.getSettings().setBuiltInZoomControls(true);// 会出现放大缩小的按钮
		webView2.getSettings().setSupportZoom(true);// 支持缩放
		webView2.getSettings().setSupportMultipleWindows(true);
		webView2.getSettings().setDomStorageEnabled(true);
		webView2.setInitialScale(45);// 初始显示比例
		webView2.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
		webView2.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView2.requestFocus();

		webView3.getSettings().setBuiltInZoomControls(true);// 会出现放大缩小的按钮
		webView3.getSettings().setSupportZoom(true);// 支持缩放
		webView3.getSettings().setSupportMultipleWindows(true);
		webView3.getSettings().setDomStorageEnabled(true);
		webView3.setInitialScale(45);// 初始显示比例
		webView3.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
		webView3.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView3.requestFocus();

		webView4.getSettings().setBuiltInZoomControls(true);// 会出现放大缩小的按钮
		webView4.getSettings().setSupportZoom(true);// 支持缩放
		webView4.getSettings().setSupportMultipleWindows(true);
		webView4.getSettings().setDomStorageEnabled(true);
		webView4.setInitialScale(45);// 初始显示比例
		webView4.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
		webView4.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView4.requestFocus();
		webView5.getSettings().setBuiltInZoomControls(true);// 会出现放大缩小的按钮
		webView5.getSettings().setSupportZoom(true);// 支持缩放
		webView5.getSettings().setSupportMultipleWindows(true);
		webView5.getSettings().setDomStorageEnabled(true);
		webView5.setInitialScale(45);// 初始显示比例
		webView5.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
		webView5.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView5.requestFocus();
		/*
		 * webView1.setWebChromeClient(new WebChromeClient(){
		 * 
		 * @Override public boolean onJsAlert(WebView view,String url, String
		 * message,JsResult result){
		 * 
		 * return super.onJsAlert(view,url,message,result); }});
		 */

		webView1.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Builder alertbuilder = new Builder(BuildingsActivity.title);
				alertbuilder.setTitle("详细信息");
				alertbuilder.setMessage(message);
				alertbuilder.setPositiveButton("我知道了",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				// alertbuilder.setCancelable(false);//不显示取消按钮
				alertbuilder.create();
				alertbuilder.show();
				return true;
			}
		});
		webView2.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Builder alertbuilder = new Builder(BuildingsActivity.title);
				alertbuilder.setTitle("详细信息");
				alertbuilder.setMessage(message);
				alertbuilder.setPositiveButton("我知道了",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				// alertbuilder.setCancelable(false);//不显示取消按钮
				alertbuilder.create();
				alertbuilder.show();
				return true;
			}
		});
		webView3.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Builder alertbuilder = new Builder(BuildingsActivity.title);
				alertbuilder.setTitle("详细信息");
				alertbuilder.setMessage(message);
				alertbuilder.setPositiveButton("我知道了",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				// alertbuilder.setCancelable(false);//不显示取消按钮
				alertbuilder.create();
				alertbuilder.show();
				return true;
			}
		});
		webView4.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Builder alertbuilder = new Builder(BuildingsActivity.title);
				alertbuilder.setTitle("详细信息");
				alertbuilder.setMessage(message);
				alertbuilder.setPositiveButton("我知道了",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				// alertbuilder.setCancelable(false);//不显示取消按钮
				alertbuilder.create();
				alertbuilder.show();
				return true;
			}
		});

		webView5.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Builder alertbuilder = new Builder(BuildingsActivity.title);
				alertbuilder.setTitle("详细信息");
				alertbuilder.setMessage(message);
				alertbuilder.setPositiveButton("我知道了",
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				// alertbuilder.setCancelable(false);//不显示取消按钮
				alertbuilder.create();
				alertbuilder.show();
				return true;
			}
		});

		switch (a) {
		case 1:
			tv.setText(tag);
			break;
		case 2:
			tv.setText(tag);
			break;
		case 3:

			lv1 = (ListView) container.findViewById(R.id.ListView1);
			lv2 = (ListView) container.findViewById(R.id.ListView2);
			lv3 = (ListView) container.findViewById(R.id.ListView3);
			lv4 = (ListView) container.findViewById(R.id.ListView4);
			lv5 = (ListView) container.findViewById(R.id.ListView5);
			// 得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/

			// Fragment不是context的子类，所以不能使用this。只要换成getActivity()就行
			lv1.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, sanjiao1));
			lv2.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, sanjiao2));
			lv3.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, sanjiao3));
			lv4.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, sanjiao4));
			lv5.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, sanjiao5));
			// webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			try {
				// SVG图所在路径
				String svg_path1 = "file:///android_asset/031.svg";
				String svg_path2 = "file:///android_asset/032.svg";
				String svg_path3 = "file:///android_asset/033.svg";
				String svg_path4 = "file:///android_asset/034.svg";
				String svg_path5 = "file:///android_asset/035.svg";

				webView1.loadUrl(svg_path1);
				webView2.loadUrl(svg_path2);
				webView3.loadUrl(svg_path3);
				webView4.loadUrl(svg_path4);
				webView5.loadUrl(svg_path5);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// svg读取方法结束
			break;
		case 4:
			tv.setText(tag);
			break;
		case 5:
			tv.setText(tag);
			break;
		case 6:
			tv.setText(tag);
			break;
		case 7:
			tv.setText(tag);
			break;
		case 8:
			tv.setText(tag);
			break;
		case 9:
			tv.setText(tag);
			break;
		case 10:
			tv.setText(tag);
			break;
		case 11:
			tv.setText(tag);
			break;
		case 12:
			lv1 = (ListView) container.findViewById(R.id.ListView1);
			lv2 = (ListView) container.findViewById(R.id.ListView2);
			lv3 = (ListView) container.findViewById(R.id.ListView3);
			lv4 = (ListView) container.findViewById(R.id.ListView4);
			lv5 = (ListView) container.findViewById(R.id.ListView5);
			// 得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/

			// Fragment不是context的子类，所以不能使用this。只要换成getActivity()就行
			lv1.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, shiliu1));
			lv2.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, shiliu2));
			lv3.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, shiliu3));
			lv4.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, shiliu4));
			lv5.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1, shiliu5));
			// webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			try {
				// SVG图所在路径
				String svg_path1 = "file:///android_asset/161.svg";
				String svg_path2 = "file:///android_asset/162.svg";
				String svg_path3 = "file:///android_asset/163.svg";
				String svg_path4 = "file:///android_asset/164.svg";
				String svg_path5 = "file:///android_asset/165.svg";

				webView1.loadUrl(svg_path1);
				webView2.loadUrl(svg_path2);
				webView3.loadUrl(svg_path3);
				webView4.loadUrl(svg_path4);
				webView5.loadUrl(svg_path5);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// svg读取方法结束
			break;
		case 13:
			tv.setText(tag);
			break;
		case 14:
			tv.setText(tag);
			break;
		case 15:
			tv.setText(tag);
			break;
		case 16:
			tv.setText(tag);
			break;
		default:
			tv.setText("没有选择");
		}
		return tv;
	}

}
