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
	// �˵�ѡ��
	private final static int SCAN_RESULT = 0x1122;

	private static final int PHOTO_WITH_CAMERA = 37;// ������Ƭ

	protected static String mLocationName = "";
	// ����Ϊ��̬������������InWebMapPosition��ֵΪ��ʼֵ

	private String mUserInput;
	private Dialog mPhotoDialog;
	private int random = new Random().nextInt();// �����������ΪͼƬ����
	ClassVariable c = new ClassVariable();
	private final File photo = new File(c.photopath); // ��ȡ�ֻ�sd�ƶ�Ŀ¼
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
		// ��һ������ʱѡ�е�0��tab
		selectFragment(0);
		// �����Ĳ˵���webview
		registerForContextMenu(mViewMapLayout);
		c.SetDatabase(this, c.mTableName[0], c.mLocationColumn[2]);
	//	this.getWebMapHtml();
	}

	private void Initial() {
		// ��TabActivity�����ȡ����Tab��TabHost
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

	/** Button����¼� */
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
		// ÿ��ѡ��֮ǰ��������ϴε�ѡ��״̬
		clearSelection();
		// ����һ��Fragment����
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		// �����ص����е�Fragment���Է�ֹ�ж��Fragment��ʾ�ڽ����ϵ����
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
	 * ��������е�ѡ��״̬��
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
	 * �����е�Fragment����Ϊ����״̬��
	 * 
	 * @param transaction
	 *            ���ڶ�Fragmentִ�в���������
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


	/** �����Ĳ˵�*/
	public void onCreateContextMenu(ContextMenu contextmenu, View view,
			ContextMenuInfo contextmenuinfo) {
		super.onCreateContextMenu(contextmenu, view, contextmenuinfo);
		MenuInflater menuflater = getMenuInflater();
		menuflater.inflate(R.menu.share, contextmenu);
		contextmenu.setHeaderTitle("��ʲô");
	}

	/**���������Ĳ˵������¼�*/
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.location && mPositionAction == 2222) {// ����λ��
			this.sharePosition();
		}
		if (item.getItemId() == R.id.photo) {// ���շ���
			File cameraimage = new File(c.photopath, "" + random + ".jpg"); // ��ȡ�ֻ�sd�ƶ�Ŀ¼
			imageUri = Uri.fromFile(cameraimage);
			this.takeCameraPhoto();
		}
		if (item.getItemId() == R.id.route) {// ·���滮
			Intent i = new Intent();
			i.setClass(MainActivity.this, RoutePlanActivity.class);
			startActivity(i);
		}
		if (item.getItemId() == R.id.menu_room_message) { // ���¥��
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

	/** ��ȡ����������Ƭ,������*/
	private void takeCameraPhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// �����ֻ���������
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, PHOTO_WITH_CAMERA);
	}

	/**
	 * ��ͼƬ����Ի��� ע���ָ��
	 * **/
	private void sharePhotoDialog(Bitmap bit) {
		mPhotoDialog = new Dialog(this);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.share_photo_dialog,
				(ViewGroup) findViewById(R.id.sharedialog));
		// new AlertDialog.Builder(this).setView(mWebView).show();
		mPhotoDialog.setTitle("����");
		ImageView mphotoView = (ImageView) view.findViewById(R.id.photoiv);
		Button mcancel = (Button) view.findViewById(R.id.cancelbtn);
		Button mshare = (Button) view.findViewById(R.id.sharebtn);

		mcancel.setOnClickListener(new onShareClickListener());// ȡ��

		mshare.setOnClickListener(new onShareClickListener());// ����

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
		mPhotoDialog.setTitle("����");
		TextView mLocationTextView = (TextView) view.findViewById(R.id.tv_user_location);
		mLocationTextView.setText(mLocationName);
		EditText mUserInputEditText = (EditText) view.findViewById(R.id.et_user_share);
		mUserInput = mUserInputEditText.getText().toString();
		Button mcancel = (Button) view.findViewById(R.id.btn_cancel_share_location);
		Button mshare = (Button) view.findViewById(R.id.btn_share_location);

		mcancel.setOnClickListener(new onShareClickListener());// ȡ��

		mshare.setOnClickListener(new onShareClickListener());// ����

		mPhotoDialog.setContentView(view);
		mPhotoDialog.show();
	}
	/** ��������¼�*/
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

	/** ��ȡ�ֻ����Ѿ����ڵ�Ӧ��*/
	public boolean checkInstalledApplicaton(String packageName) {
		try {
			this.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/** ����λ��*/
	private void shareLocation() {
		Intent photointent = new Intent(Intent.ACTION_SEND);
		photointent.setType("text/plain");
		photointent.putExtra(Intent.EXTRA_TEXT, mLocationName + mUserInput);
		startActivityForResult(Intent.createChooser(photointent, "��ѡ��"), 0);
	}
	
	/** ������Ƭ*/
	private void sharePhoto(Uri imageUri) {
		Intent photointent = new Intent(Intent.ACTION_SEND);
		photointent.setType("image/*");
		// photointent.putExtra(Intent.EXTRA_SUBJECT, "����");
		photointent.putExtra(Intent.EXTRA_STREAM, imageUri);
		startActivityForResult(Intent.createChooser(photointent, "��ѡ��"), 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case PHOTO_WITH_CAMERA:// ���ջ�ȡͼƬ
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
					// Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
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