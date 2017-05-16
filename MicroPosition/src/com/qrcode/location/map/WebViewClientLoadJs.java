package com.qrcode.location.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewClientLoadJs extends WebViewClient {
	@Override
	public boolean shouldOverrideUrlLoading(WebView view,String url){
		view.loadUrl(url);
		return true;
	}
	@Override
	public void onPageFinished(WebView view,String url){
		super.onPageFinished(view, url);
		loadUrlJs(view);
	}
	
	@SuppressLint("JavascriptInterface")
	private void loadUrlJs(WebView view){
		view.addJavascriptInterface(new InWebMapPosition(MapActivity.mapActivity, MapActivity.handler),"setLocationSVG");
		}
	
	
	/**处理js中alert对话框*/
	public void loadUrlJsAlert(WebView view){
		view.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				Builder alertbuilder = new Builder(MapActivity.mapActivity);
				alertbuilder.setMessage(message);
				alertbuilder.setPositiveButton(android.R.string.ok,
						new AlertDialog.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								result.confirm();
							}
						});
				alertbuilder.setCancelable(false);
				alertbuilder.create();
				alertbuilder.show();
				return true;
			}
		});
	}
}
