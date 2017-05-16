package com.zxing.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.qrcode.location.*;
import com.qrcode.location.map.MapActivity;
import com.qrcode.location.variable.ClassVariable;
import com.xike.position.database.OperatePositionDatabase;

public class AnalyseQRcode extends Activity {
	@SuppressWarnings("unused")
	private String TAG = AnalyseQRcode.class.getName();
	static Activity analyseqrcode = null;// 定义Activity变量，在主CaptureActivity中进行关闭
	ClassVariable c = new ClassVariable();
	public String LocationName;
	public int LocationX;
	public int LocationY;
	public String LocationDescription;
	private String mActivityName;
	
	private TextView mResultTextView;
	private TextView mScanResultTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.analyseqrcode_activity);
		Initial();
		Intent startScan = new Intent(AnalyseQRcode.this, CaptureActivity.class);
		startActivityForResult(startScan, 0);// 进入二维码解析界面
	}

	private void Initial(){
		analyseqrcode = this;
		mResultTextView = (TextView) findViewById(R.id.tv_result);
		mScanResultTextView = (TextView) findViewById(R.id.tv_scan_result);
		mResultTextView.setText("扫描结果：");
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Bundle bundle = null;
		bundle = data.getExtras();// 获取数据
		String scanResult = bundle.getString("result");
		mScanResultTextView.setText(scanResult);
		if (resultCode == RESULT_OK) {

			if (scanResult.contains("http://")
					|| ((scanResult.contains(".com"))
							|| (scanResult.contains(".cn")) || scanResult
								.contains("www."))) {// 判断是否为URL信息，如果是则打开连接
				Intent i = new Intent(Intent.ACTION_VIEW);// 打开连接
				String url ;
				if (scanResult.contains("www.")
						&& !scanResult.contains("http://")) {
					url = "http://" + scanResult;
				}
				else{
					url = scanResult;
				}
				i.setData(Uri.parse(url));
				startActivity(i);
			}

			SQLiteDatabase db = null;
			OperatePositionDatabase o = new OperatePositionDatabase();

			o.setPositionDatabase(this, db,c.mTableName[0], c.mLocationColumn[2]);
			// 查找所有地理位置
			for (int i = 0; i < o.mCloumnValue.length; i++) {

				if (scanResult.equals(o.mCloumnValue[i])) {

					String sql = "SELECT * from Location where LocationName=?";
					o.searchCorrdinate(db, c.mTableName[0],sql,scanResult);

					LocationName = o.mCloumnValue[i];
					LocationDescription = o.mLocationDescription;
					LocationX= o.mLocationX;
					LocationY = o.mLocationY;
					/* 跳转至地图 */
					Intent webmapintent = new Intent();
					webmapintent.setClass(AnalyseQRcode.this,
							MapActivity.class);
					Bundle b = new Bundle();
					mActivityName = "AnalyseQRcodeActivity";
					b.putString("ActivityName", mActivityName);
					b.putString("Position", "xike");
					b.putString("LocationName", LocationName);
					b.putString("LocationDescription", LocationDescription);
					b.putInt("LocationX", LocationX);
					b.putInt("LocationY", LocationY);
					webmapintent.putExtras(b);
					if(MapActivity.isMapActivity .equals("true")){
						if (!MapActivity.mapActivity.isFinishing()) {
							MapActivity.mapActivity.finish();
						}
					}
					startActivity(webmapintent);
					AnalyseQRcode.this.finish();

				}
			}

		}

	}
}