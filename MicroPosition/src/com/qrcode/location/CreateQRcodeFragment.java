package com.qrcode.location;

import com.google.zxing.WriterException;
import com.qrcode.location.setqrcode.CreateQRcode;
import com.zxing.encoding.EncodingHandler;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateQRcodeFragment extends Fragment implements OnClickListener{
	public Button mCreateQRcodeButton;
	public Button mSaveQRcodeButton;
	public ImageView mQRcodeImageView;
	public EditText mInputQRcodeEditText;
	private Bitmap mQRcodeBitmap;
	private String mQRcodePath = Environment.getExternalStorageDirectory()
			+ "/Daohang/QRcode";
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View setQRcodeLayout = inflater.inflate(R.layout.create_qrcode_fragment,
				container, false);
		return setQRcodeLayout;
	}
	public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        mCreateQRcodeButton = (Button) getActivity().findViewById(R.id.btn_set_qrcode);
        mQRcodeImageView = (ImageView) getActivity().findViewById(R.id.iv_set_qrcode);
        mInputQRcodeEditText = (EditText) getActivity().findViewById(R.id.et_set_qrcode);
		mSaveQRcodeButton = (Button) getActivity().findViewById(R.id.btn_save_qrcode);
		mCreateQRcodeButton.setOnClickListener(this);
		mSaveQRcodeButton.setOnClickListener(this);
    }
	@SuppressLint("ShowToast") @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_set_qrcode:
			String in = mInputQRcodeEditText.getText().toString();
			if (in.equals("")) {
				Toast.makeText(getActivity(), "请输入文本", Toast.LENGTH_SHORT)
						.show();
			} else {
				try {
					mQRcodeBitmap = EncodingHandler.createQRCode(in, 400);
					mQRcodeImageView.setImageBitmap(mQRcodeBitmap);
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.btn_save_qrcode:
			String in1 = mInputQRcodeEditText.getText().toString();
			final CreateQRcode sqr = new CreateQRcode();
			if (in1 != "" && mQRcodeImageView != null) {
				sqr.saveQrCodePicture(getActivity(), mQRcodeBitmap, in1,
						mQRcodePath);
			}
			Toast.makeText(getActivity(), "保存成功", 300).show();
			mQRcodeImageView.setImageBitmap(null);
			mInputQRcodeEditText.setText("");
			break;
		}
	}
}
