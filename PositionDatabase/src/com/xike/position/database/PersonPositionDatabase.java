package com.xike.position.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonPositionDatabase extends SQLiteOpenHelper {

	/* ���ݿ�汾��,�����ϰ汾�ߣ��������������и��� */

	private final static int DATABASE_VERSION = 1;
	/* ���ݿ��ļ��� */
	//Ĭ�Ͻ������������ļ�����
	@SuppressLint("SdCardPath") private final static String DATABASE_PATH = "/data/data/com.qrcode.location/databases/";
	private final static String DATABASE_NAME = "RoutePlan.db";// ���ݿ��ļ���

	// ��ȫ�������Ĺ��캯�����˹��캯���ز�����
	public PersonPositionDatabase(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	// �����������Ĺ��캯�������õ���ʵ�Ǵ����������Ĺ��캯��
	public PersonPositionDatabase(Context context, String name) {
		this(context, name, DATABASE_VERSION);
	}

	// �����������Ĺ��캯�������õ��Ǵ����в����Ĺ��캯��
	public PersonPositionDatabase(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public PersonPositionDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	// �������ݿ��
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db = SQLiteDatabase.openOrCreateDatabase(DATABASE_PATH+DATABASE_NAME, null);
		// autoincrement NOT NULL ON CONFLICT FALL
		String sql = "create table Location (LocationID int primary key NOT NULL AUTO_INCREMENT,Location VARCHAR(90),LocationX int,LocationY int);";
		db.execSQL(sql);
		
	}

	// ���ݿ�����ʱ����
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
