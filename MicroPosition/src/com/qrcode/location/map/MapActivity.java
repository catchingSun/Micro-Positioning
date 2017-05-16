package com.qrcode.location.map;
import java.io.File;
import java.util.Random;

import com.qrcode.location.R;
import com.qrcode.location.room.BuildingsActivity;
import com.qrcode.location.routeplan.RoutePlanActivity;
import com.qrcode.location.variable.ClassVariable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MapActivity extends Activity {
	private static final String TAG = MapActivity.class.getName();
	public static Activity mapActivity = null;
	public static String isMapActivity = "";
	protected static int LocationX = -1;
	protected static int LocationY = -1;
	protected static String isQRBundle = "";
	private String ActivityName;
	int imageID = 0;// 图片id
	protected static int mPositionAction = 0;
	// 菜单选项
	private final static int SCAN_RESULT = 0x1122;

	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
	protected static String LocationName = "";
	// 必須为静态变量，否则传入InWebMapPosition的值为初始值

	private WebView mWebView;
	private WebSettings mWebSettings;
	private int random = new Random().nextInt();// 产生随机数作为图片名称
	ClassVariable c = new ClassVariable();
	private final File photo = new File(c.photopath); // 获取手机sd制定目录
	// private final File qrposition = new File(c.qrpositionpath); // 获取手机sd制定目录

	Uri imageUri = null;
	protected static String Position = "";

	protected static Handler handler = new Handler();

	private Intent intent = null;
	private Bundle mQRcodeBundle = null;

	private TextView mUserLocationTextView;
	private EditText mUserShareEditText;
	private Dialog mphotodialog;
	private Dialog mLocationDialog;

	protected static int[] mRoutePointOrder = null;
	protected static int[] mRoutePointX = null;
	protected static int[] mRoutePointY = null;
	protected static int mStartLocationX;
	protected static int mStartLocationY;
	protected static int mEndLocationX;
	protected static int mEndLocationY;

	/*
	 * private String QRcodepath = Environment.getExternalStorageDirectory() +
	 * "/Daohang/QRcode";
	 */

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_activity);
		mapActivity = this;
		mWebView = (WebView) this.findViewById(R.id.wv_map_view);
		isMapActivity = "true";
		// 删除数据库
		// this.deleteDatabase(DATABASE_PATH + DATABASE_NAME);
		photo.mkdirs();
		// 上下文菜单绑定webview
		registerForContextMenu(mWebView);
		c.SetDatabase(this, c.mTableName[0], c.mLocationColumn[2]);
		// 顺序不可颠倒，否则intent、bundle皆为空
		intent = this.getIntent();
		mQRcodeBundle = intent.getExtras();
		this.getWebMapHtml();
	}

	/** 获取AnalyseQRcodeActivity以及RoutePlanActivity传来的坐标 */
	private void getPositionCorrdinate() {
		if (mQRcodeBundle != null) {
			ActivityName = mQRcodeBundle.getString("ActivityName");
			if (ActivityName.equals("AnalyseQRcodeActivity")) { // bundle来自AnalyseQRcodeActivity
				isQRBundle = "true";
				Log.e(TAG, "getPositionCorrdinate" + ActivityName + isQRBundle);
				Position = mQRcodeBundle.getString("Position");
				LocationName = mQRcodeBundle.getString("LocationName");
				LocationX = mQRcodeBundle.getInt("LocationX");
				LocationY = mQRcodeBundle.getInt("LocationY");
				mPositionAction = 2222;
			} else if (ActivityName.equals("RoutePlanActivity")) {// bundle来自RoutePlanActivity
				isQRBundle = "false";
				// Log.e(TAG, "getPositionCorrdinate"+ActivityName +
				// isqrbundle);
				mRoutePointOrder = mQRcodeBundle.getIntArray("RoutePointOrder");
				mRoutePointX = mQRcodeBundle.getIntArray("RoutePointX");
				mRoutePointY = mQRcodeBundle.getIntArray("RoutePointY");
				mStartLocationX = mQRcodeBundle.getInt("StartLocationX");
				mStartLocationY = mQRcodeBundle.getInt("StartLocationY");
				mEndLocationX = mQRcodeBundle.getInt("EndLocationX");
				mEndLocationY = mQRcodeBundle.getInt("EndLocationY");
			}
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	private void getWebMapHtml() {
		this.getPositionCorrdinate();
		// 设置支持JavaScript等
		mWebSettings = mWebView.getSettings();

		// 设置字符集编码
		mWebSettings.setDefaultTextEncodingName("UTF-8");
		// 开启JavaScript支持
		mWebSettings.setJavaScriptEnabled(true);
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

		// 需要先执行js，否则无法找到方法
		/*
		 * mWebView.addJavascriptInterface(new
		 * InWebMapPosition(MapActivity.this,handler), "setLocationSVG");
		 * load("file:///android_asset/map.html");
		 */
		String url = "file:///android_asset/map.html";
		WebViewClientLoadJs wb = new WebViewClientLoadJs();
		wb.onPageFinished(mWebView, url);
		wb.shouldOverrideUrlLoading(mWebView, url);

		wb.loadUrlJsAlert(mWebView);
	}

	/** 上下文菜单 */
	public void onCreateContextMenu(ContextMenu contextmenu, View view,
			ContextMenuInfo contextmenuinfo) {
		super.onCreateContextMenu(contextmenu, view, contextmenuinfo);
		MenuInflater menuflater = getMenuInflater();
		menuflater.inflate(R.menu.share, contextmenu);
		contextmenu.setHeaderTitle("做什么");
	}

	/** 设置上下文菜单监听事件 */
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.location && mPositionAction == 2222) {// 分享位置
			if(LocationName.equals(null)){
				item.setEnabled(false);
			}
			else{
				this.sharePosition();
			}
			/*
			 * File qrimage = new File(c.qrpositionpath, "" + LocationName +
			 * ".jpg"); // 获取手机sd制定目录 imageUri = Uri.fromFile(qrimage);
			 */
		}
		if (item.getItemId() == R.id.photo) {// 拍照分享
			File cameraimage = new File(c.photopath, "" + random + ".jpg"); // 获取手机sd制定目录
			imageUri = Uri.fromFile(cameraimage);
			this.takeCameraPhoto();
		}
		if (item.getItemId() == R.id.route) {// 路径规划
			Intent i = new Intent();
			i.setClass(MapActivity.this, RoutePlanActivity.class);
			startActivity(i);
		}
		if (item.getItemId() == R.id.menu_room_message) { // 多层楼宇
			Intent i = new Intent();
			i.setClass(MapActivity.this, BuildingsActivity.class);
			startActivity(i);
		}
		return true;
	}

	public void sharePosition() {
		if (mPositionAction == 2222) {
			shareLocationDialog();
		}
	}

	/**
	 * 打开位置分享对话框 注意空指针
	 * **/
	@SuppressLint("ResourceAsColor") private void shareLocationDialog() {
		// TODO
		mLocationDialog = new Dialog(this);
		mLocationDialog.setContentView(R.layout.share_location_dialog);
		mLocationDialog.getWindow().setTitle("分享");
		mLocationDialog.getWindow().setTitleColor(R.color.black);
		mLocationDialog.getWindow().setBackgroundDrawableResource(R.color.share_location_dialog);
		
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.share_location_dialog,
				(ViewGroup) findViewById(R.id.dialog_share_location));
		// new AlertDialog.Builder(this).setView(mWebView).show();
		
		Window dialogWindow = mLocationDialog.getWindow();  
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();  
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);  
		
      //  lp.width = 400; // 宽度
      //  lp.height = 330; // 高度
		dialogWindow.setAttributes(lp);
		
		mUserLocationTextView = (TextView) view
				.findViewById(R.id.tv_user_location);
		mUserLocationTextView.setText(LocationName);
		mUserShareEditText = (EditText) view.findViewById(R.id.et_user_share);
		Button mCancelButton = (Button) view
				.findViewById(R.id.btn_cancel_share_location);
		Button mShareLocationButton = (Button) view
				.findViewById(R.id.btn_share_location);

		mCancelButton.setOnClickListener(new onShareClickListener());// 取消

		mShareLocationButton.setOnClickListener(new onShareClickListener());// 分享

		mLocationDialog.setContentView(view);
		mLocationDialog.show();
	}

	/** 获取拍照所得照片,并保存 */
	private void takeCameraPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用手机进行拍照
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, PHOTO_WITH_CAMERA);
	}

	/**
	 * 打开图片分享对话框注意空指针
	 * **/
	private void sharePhotoDialog(Bitmap bit) {

		mphotodialog = new Dialog(this);
		mphotodialog.getWindow().setBackgroundDrawableResource(R.color.share_location_dialog);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.share_photo_dialog,
				(ViewGroup) findViewById(R.id.sharedialog));
		// new AlertDialog.Builder(this).setView(mWebView).show();
		mphotodialog.setTitle("分享");
		ImageView mphotoView = (ImageView) view.findViewById(R.id.photoiv);
		Button mcancel = (Button) view.findViewById(R.id.cancelbtn);
		Button mshare = (Button) view.findViewById(R.id.sharebtn);

		mcancel.setOnClickListener(new onShareClickListener());// 取消

		mshare.setOnClickListener(new onShareClickListener());// 分享

		mphotodialog.setContentView(view);
		mphotodialog.show();
		mphotoView.setImageBitmap(bit);
	}

	/** 分享监听事件 */
	protected class onShareClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.cancelbtn) {
				mphotodialog.cancel();
			}
			if (v.getId() == R.id.sharebtn) {
				sharePhoto(imageUri);
				mphotodialog.cancel();
			}
			if (v.getId() == R.id.btn_cancel_share_location) {
				mLocationDialog.cancel();
			}
			if (v.getId() == R.id.btn_share_location) {
				String in = mUserShareEditText.getText().toString();
				shareLocation(LocationName + in);
				mLocationDialog.cancel();
			}
		}

	}

	/** 获取手机上已经存在的应用 */
	public boolean checkInstalledApplicaton(String packageName) {
		try {
			this.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			return false;
		}
	}
	/**	分享位置*/
	private void shareLocation(String text) {
		Intent mlocationIntent = new Intent(Intent.ACTION_SEND);
		mlocationIntent.setType("text/plain");
		mlocationIntent.putExtra(Intent.EXTRA_TEXT, text);
		startActivityForResult(Intent.createChooser(mlocationIntent, "请选择"), 0);
	}

	/** 分享照片 */
	private void sharePhoto(Uri imageUri) {
		Intent photointent = new Intent(Intent.ACTION_SEND);
		photointent.setType("image/*");
		// photointent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		photointent.putExtra(Intent.EXTRA_STREAM, imageUri);
		startActivityForResult(Intent.createChooser(photointent, "请选择"), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case PHOTO_WITH_CAMERA:// 拍照获取图片
				System.out.println("onActivityResult:" + random);
				// String.valueOf(random);
				Bitmap bitmap = BitmapFactory.decodeFile(c.photopath + "/"
						+ random + ".jpg");
				// Log.i("onActivityResult", "" + random);
				if (bitmap != null) {
					int w = bitmap.getWidth();
					int h = bitmap.getHeight();
					Matrix matrix = new Matrix();
					float scaleWidth = ((float) bitmap.getWidth() / 5 / w);
					float scaleHeight = ((float) bitmap.getHeight() / 5 / h);
					// 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
					matrix.postScale(scaleWidth, scaleHeight);
					bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
							true);
					sharePhotoDialog(bitmap);
				}
				break;

			case SCAN_RESULT:
			}
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MapActivity.this.finish();
			return false;
		}
		return false;
	}
}
