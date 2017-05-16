package com.zxing.activity;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.qrcode.location.R;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.decoding.RGBLuminanceSource;
import com.zxing.view.ViewfinderView;

public class CaptureActivity extends Activity implements Callback {
	private String TAG = CaptureActivity.class.getName();
	public static final int CROP_PHOTO = 2;
	public static Activity caputre = null;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private Button cancelScanButton;
	private Button getlocationButton;

	private String photo_path;
	private Bitmap scanBitmap;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		caputre = this;
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		getlocationButton = (Button) findViewById(R.id.btn_get_scan);
		getlocationButton.setOnClickListener(new getlocationClickListener());
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	protected class getlocationClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			/*File outputImage = new File(Environment. getExternalStorageDirectory(), "tempImage.jpg");
			imageUri = Uri.fromFile(outputImage);*/
			Intent intent = new Intent("android.intent.action.GET_CONTENT");
			intent.setType("image/*");
			intent.putExtra("crop", true);
		//	intent.putExtra("scale", true);//���S�ü�
			startActivityForResult(intent, CROP_PHOTO);
		}

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
				if(requestCode == CROP_PHOTO){
/*				//��ȡѡ��ͼƬ��·��
				String [] p = {MediaStore.Images.Media.DATA};
				Cursor cursor = getContentResolver().query(data.getData(), p, null, null, null);
				cursor.moveToFirst();
				if (cursor.moveToFirst()) {
					photo_path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				}
				Log.e(TAG, photo_path);
				cursor.close();*/
					String[] proj = {MediaStore.Images.Media.DATA};
			        //������android��ý�����ݿ�ķ�װ�ӿڣ�����Ŀ�Android�ĵ�
			        Cursor cursor = getContentResolver().query(data.getData(), proj, null, null, null);
			        //����û�ѡ���ͼƬ������ֵ
			        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			     //   System.out.println("***************" + column_index);
			        //�����������ͷ ���������Ҫ����С�ĺ���������Խ��
			        cursor.moveToFirst();
			        //����������ֵ��ȡͼƬ·��   ������ƣ�/mnt/sdcard/DCIM/Camera/IMG_20151124_013332.jpg
			        photo_path = cursor.getString(column_index);
			        Log.e(TAG, photo_path);
			        cursor.close();
				Result result = scanningImage(photo_path);
				
				if (result != null) {
					handleDecode(result,scanBitmap);
				} else {
					Toast.makeText(CaptureActivity.this, "ɨ��ʧ��", Toast.LENGTH_LONG).show();
				}
			}
		}
	}
	
	/**
	 * ɨ���ά��ͼƬ�ķ���
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if(TextUtils.isEmpty(path)){
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); //���ö�ά�����ݵı���

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // �Ȼ�ȡԭ��С
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // ��ȡ�µĴ�С
		int sampleSize = (int) (options.outHeight / (float) 200);
		if (sampleSize <= 0){
			sampleSize = 1;
		}
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

		// quit the scan view
		cancelScanButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
				// TODO Auto-generated catch block
				AnalyseQRcode.analyseqrcode.finish();// �ر���Activity
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		String resultString = result.getText();

		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "ɨ��ʧ��", Toast.LENGTH_SHORT)
					.show();
		} else {
			// System.out.println("Result:"+resultString);
			bundle.putString("result", resultString);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);
		}

		CaptureActivity.this.finish();
	}

	@SuppressLint("ShowToast") private void initCamera(SurfaceHolder surfaceHolder) {
		// FIXME
		Toast t = Toast.makeText(this, "�뽫ȡ�����׼��ά�룬׼����ʼɨ��", 300);
		t.setGravity(Gravity.CENTER, 0, -40);
		// toastview.addView(ziyuan);
		t.show();
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	/* �ڱ�activity��ɨ��õ��Ľ����������AnalyseQRcode�������Ҫ�ȹر�CaptureActivity */
	public boolean onKeyDown(int keyCode, KeyEvent event) {// ���÷��ؼ�,&&
															// event.getRepeatCount()
															// ==0
		// TODO Auto-generated catch block
		if (keyCode == KeyEvent.KEYCODE_BACK) {// ���·��ؼ��˳�ɨ�������ά��
			CaptureActivity.this.finish();
			AnalyseQRcode.analyseqrcode.finish();// �ر���Activity
			return false;
		}
		return false;
	}
}