package com.xike.position.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonPositionDatabase extends SQLiteOpenHelper {

	/* 数据库版本号,若比上版本高，则对所有组件进行更新 */

	private final static int DATABASE_VERSION = 1;
	/* 数据库文件名 */
	//默认建立在主工程文件夹下
	@SuppressLint("SdCardPath") private final static String DATABASE_PATH = "/data/data/com.qrcode.location/databases/";
	private final static String DATABASE_NAME = "RoutePlan.db";// 数据库文件名

	// 带全部参数的构造函数，此构造函数必不可少
	public PersonPositionDatabase(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	// 带两个参数的构造函数，调用的其实是带三个参数的构造函数
	public PersonPositionDatabase(Context context, String name) {
		this(context, name, DATABASE_VERSION);
	}

	// 带三个参数的构造函数，调用的是带所有参数的构造函数
	public PersonPositionDatabase(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public PersonPositionDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// 生成数据库表
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH+DATABASE_NAME, null);
		// autoincrement NOT NULL ON CONFLICT FALL
		String sql = "create table Location (LocationID int primary key NOT NULL AUTO_INCREMENT,Location VARCHAR(90),LocationX int,LocationY int);";
		db.execSQL(sql);
		
	}

	// 数据库升级时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE Location ("
				+ "[LocationID] integer primary key autoincrement,Location VARCHAR(90) NOT NULL ON CONFLICT FALL,[LocationX] INT(90), [LocationY] INT(90))";
		// /*String sql = "CREATE TABLE [t_location]("
		// +"[id] AUTOINC,"
		// +"[location] VARCHAR(90) NOT NULL ON CONFLICT FALL,"
		// +" [abscissa] INT(90), [ordinate] INT(90));"
		// +"CONSTRAINT [sqlite_autoindex_t_location_1] PRIMARY KEY([id]))";
		// */
		db.execSQL(sql);
	}

}
