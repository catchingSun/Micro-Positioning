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

		// ����֧��JavaScript��
		mWebSettings = mWebView.getSettings();
		// �����ַ�������
		mWebSettings.setDefaultTextEncodingName("UTF-8");
		// �������Ű�ť
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setLightTouchEnabled(true);
		// ֧������
		mWebSettings.setSupportZoom(true);
		mWebView.setHapticFeedbackEnabled(false);
		// ��ʼ��Ϊ�ʺ���Ļ��С
		mWebSettings.setUseWideViewPort(true);
		mWebSettings.setLoadWithOverviewMode(true);
		// �ı����ֵ�����趨��ʼ��С
		mWebView.setInitialScale(0);
		mWebView.loadUrl("file:///android_asset/index.html");
	}
}
