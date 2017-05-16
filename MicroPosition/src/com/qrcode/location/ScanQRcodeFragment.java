package com.qrcode.location;

import com.zxing.activity.AnalyseQRcode;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ScanQRcodeFragment extends Fragment{
	private Button mScanQRcodeButton;
	private final static int SCAN_RESULT = 0x1122;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View scanQRcodeLayout = inflater.inflate(R.layout.scan_qrcode_fragment,
				container, false);
		return scanQRcodeLayout;
	}
	public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState); 
        mScanQRcodeButton = (Button) getActivity().findViewById(R.id.btn_scan_qrcode);
        mScanQRcodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent startScan = new Intent(getActivity(),
						AnalyseQRcode.class);
				startActivityForResult(startScan, SCAN_RESULT);// 进入二维码解析界面
			}
		});
	}
}
