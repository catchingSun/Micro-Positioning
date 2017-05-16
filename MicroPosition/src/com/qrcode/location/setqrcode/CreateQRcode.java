package com.qrcode.location.setqrcode;

import com.google.zxing.WriterException;
import com.qrcode.location.R;
import com.zxing.encoding.EncodingHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CreateQRcode extends Activity {
	private ImageView mSetQRcodeImageView;
	private EditText mSetQRcodeEditText;
	private Bitmap mSetQRcodeBitmap;
	@SuppressWarnings("unused")
	private Button mSetQRcodeButton;
	private String QRcodepath = Environment.getExternalStorageDirectory()
			+ "/Daohang/QRcode";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		mSetQRcodeButton = (Button) findViewById(R.id.btn_set_qrcode);
		mSetQRcodeImageView = (ImageView) findViewById(R.id.iv_set_qrcode);
		mSetQRcodeEditText = (EditText) findViewById(R.id.et_set_qrcode);
	}

	// ���ɶ�ά��
	public class setqrOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String in = mSetQRcodeEditText.getText().toString();
			if (in.equals("")) {
				Toast.makeText(CreateQRcode.this, "�������ı�", Toast.LENGTH_SHORT)
						.show();
			} else {
				try {

					mSetQRcodeBitmap = EncodingHandler.createQRCode(in, 400);
					mSetQRcodeImageView.setImageBitmap(mSetQRcodeBitmap);
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
		}

	}

	// �����ά��
	public class SaveQRbitmap implements OnClickListener {
		@Override
		public void onClick(View v) {
			String in = mSetQRcodeEditText.getText().toString();
			if (in != "" && mSetQRcodeImageView != null) {
				saveQrCodePicture(CreateQRcode.this, mSetQRcodeBitmap, in, QRcodepath);
			}
			// writeFile("/mnt/sdcard/QRcode/",setqrb);
			// StartMainActivity();
			// Intent newActivity=new Intent(SetQRcode.this,MainActivity.class);
			// startActivity(newActivity);//����������
			Toast.makeText(CreateQRcode.this, "����ɹ�", Toast.LENGTH_LONG).show();
			mSetQRcodeEditText.setText("");
			mSetQRcodeImageView.setImageBitmap(null);
		}

	}

	// �����ɵĶ�ά�뱣���ڱ����ļ���
	public void saveQrCodePicture(Context con, Bitmap bit, String st,
			String path) {
		final File qrImage = new File(path, st + ".jpg"); // ��ȡ�ֻ���Ŀ¼
		FileOutputStream fOut = null;
		qrImage.mkdirs();
		if (qrImage.exists()) {
			qrImage.delete();
		}
		try {
			qrImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut = new FileOutputStream(qrImage);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (bit == null) {
			Toast.makeText(con, "��ά�벻����", Toast.LENGTH_LONG).show();
			return;
		}
		bit.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// д��SD��
	public void writeFile(String fileName, Bitmap setqrbt) {
		try {

			FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

			int bytes = setqrbt.getByteCount();

			fout.write(bytes);
			fout.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ������
	public String readFile(String fileName) {
		String res = "";
		try {
			FileInputStream fin = openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}

	public CreateQRcode() {
		// TODO Auto-generated constructor stub
	}

}
