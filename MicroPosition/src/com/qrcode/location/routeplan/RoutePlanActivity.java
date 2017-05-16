package com.qrcode.location.routeplan;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.qrcode.location.R;
import com.qrcode.location.map.MapActivity;
import com.qrcode.location.variable.ClassVariable;
import com.xike.position.database.OperatePositionDatabase;

public class RoutePlanActivity extends Activity{
	private String TAG = RoutePlanActivity.class.getName();
	private EditText mStartLocationEditText;
	private EditText mEndLocationEditText;
	private Button mSetRouteButton;
	private Button mCancleButton;

	@SuppressWarnings("unused")
	private String mStartLocationName;
	@SuppressWarnings("unused")
	private String mStartLocationDescription;
	private int mStartLocationID;
	private int mStartLocationX;
	private int mStartLocationY;

	private String mStartText = null;
	private String mEndText = null;

	@SuppressWarnings("unused")
	private String mEndLocationName;
	@SuppressWarnings("unused")
	private String mEndLocationDescription;
	private int mEndLocationID;
	private int mEndLocationX;
	private int mEndLocationY;

	private int mRoutePointOrder[];
	private int mRoutePointX[];
	private int mRoutePointY[];

	private String mActivityName; // 传递得到值需为public型

	ClassVariable mClassVariable = new ClassVariable();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.routeplan_activity);
		Initial();
	}

	/** 初始化控件 */
	public void Initial() {
		mStartLocationEditText = (EditText) findViewById(R.id.et_start_station);
		mEndLocationEditText = (EditText) findViewById(R.id.et_end_station);
		mSetRouteButton = (Button) findViewById(R.id.btn_set_route);
		mCancleButton = (Button) findViewById(R.id.btn_cancle_route);
		mSetRouteButton.setOnClickListener(new onClickRoutePlan());
		mCancleButton.setOnClickListener(new onClickRoutePlan());
		
	}

	
	private class onClickRoutePlan implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.btn_set_route){
				mStartText = mStartLocationEditText.getText().toString();
				mEndText = mEndLocationEditText.getText().toString();
				SQLiteDatabase db = null;
				OperatePositionDatabase o = new OperatePositionDatabase();
				String sql = "select * from Location where LocationName=?";
				int tableStartCount = o.getLocationCount( db, sql ,mStartText);
				int tableEndCount = o.getLocationCount( db, sql ,mEndText);
				if (!mStartText.isEmpty() && !mEndText.isEmpty() && tableStartCount > 0 && tableEndCount > 0) {
					getLocationCoordinate();
					SkipToMap();
				}
				else if(mStartText.isEmpty() && !mEndText.isEmpty()){
					Toast.makeText(RoutePlanActivity.this,"请输入起点",Toast.LENGTH_LONG).show();
				}
				else if(mEndText.isEmpty() && !mStartText.isEmpty()){
					Toast.makeText(RoutePlanActivity.this,"请输入终点",Toast.LENGTH_LONG).show();
				}
				else if(mStartText.isEmpty() && mEndText.isEmpty()){
					Toast.makeText(RoutePlanActivity.this,"请输入起点和终点",Toast.LENGTH_LONG).show();
				}
				else if(tableStartCount <= 0 && tableEndCount <= 0){
					Toast.makeText(RoutePlanActivity.this,"请输入正确起点和终点",Toast.LENGTH_LONG).show();
				}
				else if(tableStartCount <= 0 && tableEndCount > 0){
					Toast.makeText(RoutePlanActivity.this,"请输入正确起点",Toast.LENGTH_LONG).show();
				}
				else if(tableEndCount <= 0 && tableStartCount > 0){
					Toast.makeText(RoutePlanActivity.this,"请输入正确终点",Toast.LENGTH_LONG).show();
				}
			} 
			if(v.getId() == R.id.btn_cancle_route){
				RoutePlanActivity.this.finish();
			}
		}
	}
	
	public static boolean isEmpty( String input ) 
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}
	
	/** 跳转至MapActivity */
	private void SkipToMap() {
		Intent i = new Intent();
		Bundle bundle = new Bundle();
		mActivityName = "RoutePlanActivity";
		bundle.putString("ActivityName", mActivityName);
		bundle.putIntArray("RoutePointOrder", mRoutePointOrder);
		bundle.putIntArray("RoutePointX", mRoutePointX);
		bundle.putIntArray("RoutePointY", mRoutePointY);
		bundle.putInt("StartLocationX", mStartLocationX);
		bundle.putInt("StartLocationY", mStartLocationY);
		bundle.putInt("EndLocationX", mEndLocationX);
		bundle.putInt("EndLocationY", mEndLocationY);
		i.putExtras(bundle);
		i.setClass(RoutePlanActivity.this, MapActivity.class);
		if (MapActivity.isMapActivity.equals("true")) {
			if (!MapActivity.mapActivity.isFinishing()) {
				MapActivity.mapActivity.finish();
			}
		}
		startActivity(i);
		this.finish();
	}



	/** 获取起点终点坐标 */
	private void getLocationCoordinate() {
		SQLiteDatabase db = null;
		OperatePositionDatabase o = new OperatePositionDatabase();
		o.setPositionDatabase(this, db, mClassVariable.mTableName[0],
				mClassVariable.mLocationColumn[2]);
		int locationTableLength = o.mTableLength;
		// 查找所有地理位置
		for (int i = 0; i < locationTableLength; i++) {

			if (mStartText.equals(o.mCloumnValue[i])) {
				String sql = "SELECT * from Location where LocationName=?";
				o.searchCorrdinate(db, mClassVariable.mTableName[0], sql,
						mStartText);

				mStartLocationName = o.mCloumnValue[i];
				mStartLocationDescription = o.mLocationDescription;
				mStartLocationID = o.mLocationID;
				mStartLocationX = o.mLocationX;
				mStartLocationY = o.mLocationY;

			}
			/*else {
				Toast.makeText(this, "请输入有效起点", Toast.LENGTH_LONG).show();
			}*/
			if (mEndText.equals(o.mCloumnValue[i])) {
				String sql = "SELECT * from Location where LocationName=?";
				o.searchCorrdinate(db, mClassVariable.mTableName[0], sql,
						mEndText);

				mEndLocationName = o.mCloumnValue[i];
				mEndLocationDescription = o.mLocationDescription;
				mEndLocationID = o.mLocationID;
				mEndLocationX = o.mLocationX;
				mEndLocationY = o.mLocationY;
			}
		}
		Log.e(TAG, "mStartLocationID mEndLocationID" + mStartLocationID
				+ mEndLocationID+ "   "+locationTableLength+"mStartText"+mStartText+mEndText);
		getRoutePlanPoint(db);
	}

	/** 获取绘图点及其坐标 */
	public void getRoutePlanPoint(SQLiteDatabase db) {
		String sql = "select distinct RoutePointX,RoutePointY,RoutePointOrder "
				+ "from RoutePointRelationship,RoutePoint "
				+ "where RoutePoint.[RoutePointID]=RoutePointRelationship.[RoutePointID] "
				+ "and RoutePoint.RoutePointID in (select RoutePointID from RoutePointRelationship "
				+ "where RoutePointRelationship.RouteID=(select RouteID from Route "
				+ "where Route.StartLocationID=? and Route.EndLocationID=?)) "
				+ "order by RoutePointOrder asc";
		db = SQLiteDatabase.openOrCreateDatabase(ClassVariable.DATABASE_PATH
				+ ClassVariable.DATABASE_NAME, null);
		Cursor result = db.rawQuery(sql, new String[] { "" + mStartLocationID,
				"" + mEndLocationID }); // 第二参数为占位数
		if (result == null) {
			Log.e(TAG, "null");
		}
		Log.e(TAG, "Count" + result.getCount());
		mRoutePointOrder = new int[result.getCount()];
		mRoutePointX = new int[result.getCount()];
		mRoutePointY = new int[result.getCount()];
		for (int i = 0; i < result.getCount(); i++) {
			result.moveToPosition(i);
			mRoutePointOrder[i] = result.getInt(result
					.getColumnIndex("RoutePointOrder"));
			mRoutePointX[i] = result.getInt(result
					.getColumnIndex("RoutePointX"));
			mRoutePointY[i] = result.getInt(result
					.getColumnIndex("RoutePointY"));
		}
		result.close();
	}
}
