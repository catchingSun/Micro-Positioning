package com.xike.position.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class OperatePositionDatabase {
	@SuppressWarnings("unused")
	private String TAG = OperatePositionDatabase.class.getName();
	@SuppressLint("SdCardPath") 
	private final static String DATABASE_PATH = "/data/data/com.qrcode.location/databases/";
	private final static String DATABASE_NAME = "RoutePlan.db";

	public int mTableLength;
	public String[] mCloumnValue;
	public int mLocationID = 0;
	public String mLocationCode = null;
	public String mLocationName = null;
	public String mLocationDescription = null;
	public int mLocationX = 0;
	public int mLocationY = 0;

	public int mRoutePointID;
	public int mRoutePointX;
	public int mRoutePointY;

	public int mRouteID;
	public String mRouteCode;
	public String mRouteType;
	public String mRouteName;
	public String mRouteDescription;
	public String mRouteDirection;
	public String mRouteFilePath;
	public int mStartLocationID;
	public int mEndLocationID;

	public int mRRoutePointID;
	public int mRRouteID;
	public int mRoutePointOrder;

	/** 数据库表名称 */
	public String[] 
			mTableName = {
			"Location",
			"Route",
			"RoutePoint",
			"RoutePointRelationship"
			};
	
	/** Location中属性 */
	public String[] 
			mLocationColumn = {
			"LocationID",
			"LocationCode",
			"LocationName",
			"LocationDescription",
			"LocationX",
			"LocationY"
			};
	
	/** Route中属性 */
	public String[]
			mRouteColumn = {
			"RouteID",
			"RouteCode",
			"RouteType",
			"RouteName",
			"RouteDescription",
			"RouteDirection",
			"RouteFilePath",
			"StartLocationID",
			"EndLocationID"
			};
	
	/** RoutePoint中属性 */
	public String[] 
			mRoutePointColumn ={
			"RoutePointID",
			"RoutePointX",
			"RoutePointY"
			};
	
	/** RoutePointRelationship中属性 */
	public String[]
			mRoutePointRelationshipColumn = {
			"RoutePointID",
			"RouteID",
			"RoutePointOrder"
			};
	

	/** 向本地文件夹写入路径规划数据库 */
	public void setPositionDatabase(Context context, SQLiteDatabase db,
			String Table, String TableColumnName) {

		if(db != null){
			db.close();
		}
		if ((new File(DATABASE_PATH + DATABASE_NAME)).exists()) {
			new File(DATABASE_PATH + DATABASE_NAME).delete();
		}

		File f = new File(DATABASE_PATH);
		if (f.exists() == false) {
			f.mkdir();
		}
		try {
			InputStream is = context.getAssets().open(DATABASE_NAME);
			OutputStream fos = new FileOutputStream(DATABASE_PATH
					+ DATABASE_NAME);
			byte[] buffer = new byte[10240];
			int count = 0;
			// 开始复制dictionary.db文件
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
			fos.flush();
			fos.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH + DATABASE_NAME,
				null);
		this.searchLocationData(db, Table, TableColumnName);
		mTableLength = getTableLength(db,Table);
		db.close();
	}

	public int getLocationCount(SQLiteDatabase db,String sql,String TableColumnName){
		db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH + DATABASE_NAME,
				null);
		int count;
		Cursor result = db.rawQuery(sql, new String[]{TableColumnName});
		count = result.getCount();
		db.close();
		return count;
	}
	/** 查找数据库中Table中各个表中各个属性对应的值 */
	public void searchCorrdinate(SQLiteDatabase db, String Table, String sql,String CloumnName1) {
		db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH + DATABASE_NAME,
				null);
		Cursor result = db.rawQuery(sql, new String[]{CloumnName1});// 第二参数为占位数
		if (result.moveToFirst()) {
			if (mTableName[0].equals(Table)) {
				mLocationID = result.getInt(result
						.getColumnIndex(mLocationColumn[0]));
				mLocationCode = result.getString(result
						.getColumnIndex(mLocationColumn[1]));
				mLocationName = result.getString(result
						.getColumnIndex(mLocationColumn[2]));
				mLocationDescription = result.getString(result
						.getColumnIndex(mLocationColumn[3]));
				mLocationX = result.getInt(result
						.getColumnIndex(mLocationColumn[4]));
				mLocationY = result.getInt(result
						.getColumnIndex(mLocationColumn[5]));
			} else if (mTableName[1].equals(Table)) {
				mRouteID = result
						.getInt(result.getColumnIndex(mRouteColumn[0]));
				mRouteCode = result.getString(result
						.getColumnIndex(mRouteColumn[1]));
				mRouteType = result.getString(result
						.getColumnIndex(mRouteColumn[2]));
				mRouteName = result.getString(result
						.getColumnIndex(mRouteColumn[3]));
				mRouteDescription = result.getString(result
						.getColumnIndex(mRouteColumn[4]));
				mRouteDirection = result.getString(result
						.getColumnIndex(mRouteColumn[5]));
				mRouteFilePath = result.getString(result
						.getColumnIndex(mRouteColumn[6]));
				mStartLocationID = result.getInt(result
						.getColumnIndex(mRouteColumn[7]));
				mEndLocationID = result.getInt(result
						.getColumnIndex(mRouteColumn[8]));
			} else if (mTableName[2].equals(Table)) {
				mRoutePointID = result.getInt(result
						.getColumnIndex(mRoutePointColumn[0]));
				mRoutePointX = result.getInt(result
						.getColumnIndex(mRoutePointColumn[1]));
				mRoutePointY = result.getInt(result
						.getColumnIndex(mRoutePointColumn[2]));

			} else if (mTableName[3].equals(Table)) {
				mRRouteID = result.getInt(result
						.getColumnIndex(mRoutePointRelationshipColumn[0]));
				mRRoutePointID = result.getInt(result
						.getColumnIndex(mRoutePointRelationshipColumn[0]));
				mRoutePointOrder = result.getInt(result
						.getColumnIndex(mRoutePointRelationshipColumn[0]));
			}
		}
		result.close();
		db.close();
	}
	
	/**获得数据库表的长度*/
	public int getTableLength(SQLiteDatabase db ,String Table){
		String sql = "select * from "+Table;
		Cursor gc = db.rawQuery(sql, null);
		int tableCount = gc.getCount();
		gc.close();
		return tableCount;
	}

	/** 查找数据 */
	public void searchLocationData(SQLiteDatabase db, String Table,
			String TableColumnName) {
		mCloumnValue = new String[getTableLength(db,Table)];
		Cursor c = db.query(Table, null, null, null, null, null, null);
		c.moveToFirst();
		if (c.moveToFirst()) {// 移到表头
			// 遍历数据表
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				mCloumnValue[i] = c
						.getString(c.getColumnIndex(TableColumnName));
			}
		}
		c.close();
	}
}
