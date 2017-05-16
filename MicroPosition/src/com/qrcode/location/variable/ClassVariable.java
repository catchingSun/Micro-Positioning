package com.qrcode.location.variable;

import com.xike.position.database.OperatePositionDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class ClassVariable {
	/**�洢��Ƭ·�� */
	public String photopath = Environment.getExternalStorageDirectory()
			+ "/Daohang/images";
	/** ����λ��·�� */
	public String qrpositionpath = Environment.getExternalStorageDirectory()
			+ "/Daohang/qrpositions";
	@SuppressLint("SdCardPath") 
	/**���ݿ�洢·��  */
	public final static String DATABASE_PATH = "/data/data/com.qrcode.location/databases/";
	public final static String DATABASE_NAME = "RoutePlan.db";
	
	public String[] mCloumnValue = new String [50];
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
	
	/** ���ݿ������ */
	public String[] 
			mTableName = {
			"Location",
			"Route",
			"RoutePoint",
			"RoutePointRelationship"
			};
	
	/** Location������ */
	public String[] 
			mLocationColumn = {
			"LocationID",
			"LocationCode",
			"LocationName",
			"LocationDescription",
			"LocationX",
			"LocationY"
			};
	
	/** Route������ */
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
	
	/** RoutePoint������ */
	public String[] 
			mRoutePointColumn ={
			"RoutePointID",
			"RoutePointX",
			"RoutePointY"
			};
	
	/** RoutePointRelationship������ */
	public String[]
			mRoutePointRelationshipColumn = {
			"RoutePointID",
			"RouteID",
			"RoutePointOrder"
			};
	
	/**������ݿ�*/
	public void SetDatabase(Context context,String table ,String tablecolumn){
		SQLiteDatabase db = null;
		OperatePositionDatabase o = new OperatePositionDatabase();
		o.setPositionDatabase(context, db ,table,tablecolumn );
	}
}
