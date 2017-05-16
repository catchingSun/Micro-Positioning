package com.qrcode.location;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ViewMapFragment extends Fragment {
	private WebView mWebView;
	private WebSettings mWebSettings;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View ViewMapLayout = inflater.inflate(R.layout.view_map_fragment,
				container, false);
		return ViewMapLayout;
	}
	public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        mWebView = (WebView) getActivity().findViewById(R.id.wv_svg_map);
        registerForContextMenu(mWebView);
        getWebMapHtml();
	}  
	@SuppressWarnings("deprecation")
	private void getWebMapHtml() {

		// 设置支持JavaScript等
		mWebSettings = mWebView.getSettings();
		// 设置字符集编码
		mWebSettings.setDefaultTextEncodingName("UTF-8");
		// 设置缩放按钮
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setLightTouchEnabled(true);
		// 支持缩放
		mWebSettings.setSupportZoom(true);
		mWebView.setHapticFeedbackEnabled(false);
		// 初始化为适合屏幕大小
		mWebSettings.setUseWideViewPort(true);
		mWebSettings.setLoadWithOverviewMode(true);
		// 改变这个值可以设定初始大小
		mWebView.setInitialScale(0);
		mWebView.loadUrl("file:///android_asset/index.html");
	}
}
