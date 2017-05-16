package com.qrcode.location;

import java.io.File;
import java.util.Random;

import com.google.zxing.WriterException;
import com.qrcode.location.room.BuildingsActivity;
import com.qrcode.location.routeplan.RoutePlanActivity;
import com.qrcode.location.setqrcode.CreateQRcode;
import com.qrcode.location.variable.ClassVariable;
import com.zxing.encoding.EncodingHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	@SuppressWarnings("unused")
	private String TAG = MainActivity.class.getName();
	protected static int mPositionAction = 0;
	// 菜单选项
	private final static int SCAN_RESULT = 0x1122;

	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片

	protected static String mLocationName = "";
	// 必须为静态变量，否则传入InWebMapPosition的值为初始值

	private String mUserInput;
	private Dialog mPhotoDialog;
	private int random = new Random().nextInt();// 产生随机数作为图片名称
	ClassVariable c = new ClassVariable();
	private final File photo = new File(c.photopath); // 获取手机sd制定目录
	Uri imageUri = null;

	private Bitmap mQRcodePositionBitmap = null;
	protected static int[] mRoutePointOrder = null;
	protected static int[] mRoutePointX = null;
	protected static int[] mRoutePointY = null;
	protected static int mStartLocationX;
	protected static int mStartLocationY;
	protected static int mEndLocationX;
	protected static int mEndLocationY;
	
	private ScanQRcodeFragment mScanQRcodeFragment;
	private CreateQRcodeFragment mCreateQRcodeFragment;
	private ViewMapFragment mViewMapFragment;
	private View mScanQRcodeLayout;
	private View mCreateQRcodeLayout;
	private View mViewMapLayout;
	private TextView mViewMapTextView;
	private TextView mCreateQRcodeTextView;
	private TextView mScanQRcodeTextView;
	private ImageView mViewMapImageView;
	private ImageView mCreateQRcodeImageView;
	private ImageView mScanQRcodeImageView;
	
	private FragmentManager mFragmentManager;

	/** */
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		Initial();
		photo.mkdirs();
		mFragmentManager = getFragmentManager();
		// 第一次启动时选中第0个tab
		selectFragment(0);
		// 上下文菜单绑定webview
		registerForContextMenu(mViewMapLayout);
		c.SetDatabase(this, c.mTableName[0], c.mLocationColumn[2]);
	//	this.getWebMapHtml();
	}

	private void Initial() {
		// 从TabActivity上面获取放置Tab的TabHost
		mViewMapImageView = (ImageView) findViewById(R.id.iv_view_map);
		mCreateQRcodeImageView = (ImageView) findViewById(R.id.iv_create_qrcode);
		mScanQRcodeImageView = (ImageView) findViewById(R.id.iv_scan_qrcode);
		mScanQRcodeLayout = findViewById(R.id.layout_scan_qrcode);
		mCreateQRcodeLayout = findViewById(R.id.layout_create_qrcode);
		mViewMapLayout = findViewById(R.id.layout_view_map);
		mViewMapTextView = (TextView) findViewById(R.id.tv_view_map);
		mCreateQRcodeTextView = (TextView) findViewById(R.id.tv_create_qrcode);
		mScanQRcodeTextView = (TextView) findViewById(R.id.tv_scan_qrcode);
		mScanQRcodeLayout.setOnClickListener(this);
		mCreateQRcodeLayout.setOnClickListener(this);
		mViewMapLayout.setOnClickListener(this);
	}

	/** Button点击事件 */
	@SuppressLint("ShowToast")
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_view_map:
			selectFragment(0);
			break;

		case R.id.layout_create_qrcode:
			selectFragment(1);
			break;

		case R.id.layout_scan_qrcode:
			selectFragment(2);
			break;
		}
	}
	private void selectFragment(int index) {
		// 每次选中之前先清楚掉上次的选中状态
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			mViewMapImageView.setBackgroundColor(Color.parseColor("#99cc33"));
			mViewMapTextView.setTextColor(Color.parseColor("#a3cf62"));
			if (mViewMapFragment == null) {
				mViewMapFragment = new ViewMapFragment();
				transaction.add(R.id.content, mViewMapFragment);
			} else {
				transaction.show(mViewMapFragment);
			}
			break;
		case 1:
			mCreateQRcodeImageView.setBackgroundColor(Color.parseColor("#99cc33"));
			mCreateQRcodeTextView.setTextColor(Color.parseColor("#a3cf62"));
			if (mCreateQRcodeFragment == null) {
				mCreateQRcodeFragment = new CreateQRcodeFragment();
				transaction.add(R.id.content, mCreateQRcodeFragment);
			} else {
				transaction.show(mCreateQRcodeFragment);
			}
			break;
		case 2:
			// newsImage.setImageResource(R.drawable.news_selected);
			mScanQRcodeImageView.setBackgroundColor(Color.parseColor("#99cc33"));
			mScanQRcodeTextView.setTextColor(Color.parseColor("#a3cf62"));
			if (mScanQRcodeFragment == null) {
				mScanQRcodeFragment = new ScanQRcodeFragment();
				transaction.add(R.id.content, mScanQRcodeFragment);
			} else {
				transaction.show(mScanQRcodeFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	/**
	 * 清除掉所有的选中状态。
	 */
	private void clearSelection() {
		mViewMapImageView.setBackground(null);
		mScanQRcodeImageView.setBackground(null);
		mCreateQRcodeImageView.setBackground(null);
		
		// messageImage.setImageResource(R.drawable.message_unselected);
		mViewMapTextView.setTextColor(Color.parseColor("#000000"));
		mViewMapImageView.setBackgroundColor(Color.parseColor("#ffffff"));
		// contactsImage.setImageResource(R.drawable.contacts_unselected);
		mCreateQRcodeImageView.setBackgroundColor(Color.parseColor("#ffffff"));
		mScanQRcodeTextView.setTextColor(Color.parseColor("#000000"));
		// newsImage.setImageResource(R.drawable.news_unselected);
		mCreateQRcodeTextView.setTextColor(Color.parseColor("#000000"));
		mScanQRcodeImageView.setBackgroundColor(Color.parseColor("#ffffff"));
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (mViewMapFragment != null) {
			transaction.hide(mViewMapFragment);
		}
		if (mScanQRcodeFragment != null) {
			transaction.hide(mScanQRcodeFragment);
		}
		if (mCreateQRcodeFragment != null) {
			transaction.hide(mCreateQRcodeFragment);
		}

	}


	/** 上下文菜单*/
	public void onCreateContextMenu(ContextMenu contextmenu, View view,
			ContextMenuInfo contextmenuinfo) {
		super.onCreateContextMenu(contextmenu, view, contextmenuinfo);
		MenuInflater menuflater = getMenuInflater();
		menuflater.inflate(R.menu.share, contextmenu);
		contextmenu.setHeaderTitle("做什么");
	}

	/**设置上下文菜单监听事件*/
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.location && mPositionAction == 2222) {// 分享位置
			this.sharePosition();
		}
		if (item.getItemId() == R.id.photo) {// 拍照分享
			File cameraimage = new File(c.photopath, "" + random + ".jpg"); // 获取手机sd制定目录
			imageUri = Uri.fromFile(cameraimage);
			this.takeCameraPhoto();
		}
		if (item.getItemId() == R.id.route) {// 路径规划
			Intent i = new Intent();
			i.setClass(MainActivity.this, RoutePlanActivity.class);
			startActivity(i);
		}
		if (item.getItemId() == R.id.menu_room_message) { // 多层楼宇
			Intent i = new Intent();
			i.setClass(MainActivity.this, BuildingsActivity.class);
			startActivity(i);
		}
		return true;
	}

	public void sharePosition() {
		if (mPositionAction == 2222) {
			try {
				mQRcodePositionBitmap = EncodingHandler
						.createQRCode(mLocationName, 400);
				CreateQRcode sqr = new CreateQRcode();
				sqr.saveQrCodePicture(MainActivity.this, mQRcodePositionBitmap,
						mLocationName, c.qrpositionpath);
				shareLocationDialog();
			} catch (WriterException e) {
				e.printStackTrace();
			}
		}
	}

	/** 获取拍照所得照片,并保存*/
	private void takeCameraPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用手机进行拍照
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, PHOTO_WITH_CAMERA);
	}

	/**
	 * 打开图片分享对话框 注意空指针
	 * **/
	private void sharePhotoDialog(Bitmap bit) {
		mPhotoDialog = new Dialog(this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.share_photo_dialog,
				(ViewGroup) findViewById(R.id.sharedialog));
		// new AlertDialog.Builder(this).setView(mWebView).show();
		mPhotoDialog.setTitle("分享");
		ImageView mphotoView = (ImageView) view.findViewById(R.id.photoiv);
		Button mcancel = (Button) view.findViewById(R.id.cancelbtn);
		Button mshare = (Button) view.findViewById(R.id.sharebtn);

		mcancel.setOnClickListener(new onShareClickListener());// 取消

		mshare.setOnClickListener(new onShareClickListener());// 分享

		mPhotoDialog.setContentView(view);
		mPhotoDialog.show();
		mphotoView.setImageBitmap(bit);
	}

	private void shareLocationDialog() {
		mPhotoDialog = new Dialog(this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.share_location_dialog,
				(ViewGroup) findViewById(R.id.sharedialog));
		// new AlertDialog.Builder(this).setView(mWebView).show();
		mPhotoDialog.setTitle("分享");
		TextView mLocationTextView = (TextView) view.findViewById(R.id.tv_user_location);
		mLocationTextView.setText(mLocationName);
		EditText mUserInputEditText = (EditText) view.findViewById(R.id.et_user_share);
		mUserInput = mUserInputEditText.getText().toString();
		Button mcancel = (Button) view.findViewById(R.id.btn_cancel_share_location);
		Button mshare = (Button) view.findViewById(R.id.btn_share_location);

		mcancel.setOnClickListener(new onShareClickListener());// 取消

		mshare.setOnClickListener(new onShareClickListener());// 分享

		mPhotoDialog.setContentView(view);
		mPhotoDialog.show();
	}
	/** 分享监听事件*/
	protected class onShareClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.cancelbtn || v.getId() ==R.id.btn_cancel_share_location) {
				mPhotoDialog.cancel();
			}
			if (v.getId() == R.id.sharebtn) {
				sharePhoto(imageUri);
				mPhotoDialog.cancel();
			}
			if(v.getId() == R.id.btn_share_location){
				shareLocation();
				mPhotoDialog.cancel();
			}
		}

	}

	/** 获取手机上已经存在的应用*/
	public boolean checkInstalledApplicaton(String packageName) {
		try {
			this.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/** 分享位置*/
	private void shareLocation() {
		Intent photointent = new Intent(Intent.ACTION_SEND);
		photointent.setType("text/plain");
		photointent.putExtra(Intent.EXTRA_TEXT, mLocationName + mUserInput);
		startActivityForResult(Intent.createChooser(photointent, "请选择"), 0);
	}
	
	/** 分享照片*/
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
			MainActivity.this.finish();
			return false;
		}
		return false;
	}
}