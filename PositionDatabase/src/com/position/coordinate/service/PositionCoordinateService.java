package com.position.coordinate.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

public class PositionCoordinateService extends Service {
	private Message coodinatemessage;
	@SuppressWarnings("unused")
	private Messenger mesenger ;
	private final int xike = 1;
	public static final String EXTRA_MESSAGER = "com.xike.webmap.EXTRA_MESSAGER";
	private final int ANALYSEQRCODE_INTENT = 0X0001;
	private final int XIKEWEBMAP_INTENT = 0X0002;
	private String position = "";
	private String location = "";
	private int abscissa = -1;
	private int ordinate = -1;
	public PositionCoordinateService() {
		super();
	}

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("onBind");
		return null;
	
	//	return super.onBind(intent);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		System.out.println("onStart");
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {
		System.out.println("onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		System.out.println("onDestroy");
		super.onDestroy();
	}

	@SuppressLint("NewApi") @Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onStartCommand");
		onHandleIntent(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	public void setIntentRedelivery(boolean enabled) {
		System.out.println("setIntentRedelivery");
		//super.setIntentRedelivery(enabled);
	}

	@SuppressLint("HandlerLeak") 
	Handler handler  = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			}
	};
	protected void onHandleIntent(Intent intent) {
	//	mesenger = new Messenger(handler);
		
		Bundle getbundle = intent.getExtras();
		
		if (getbundle != null) {
			if(intent.getExtras().getInt("intent") == ANALYSEQRCODE_INTENT){
				System.out.println("..............positi---------------------------");
				position = intent.getExtras().getString("position");
				location = getbundle.getString("location");
				abscissa = getbundle.getInt("abscissa");
				ordinate = getbundle.getInt("ordinate");
				
			}
			Bundle setbundle = new Bundle();
		//	mesenger = (Messenger) getbundle.get(EXTRA_MESSAGER);//获得客户端信使
			coodinatemessage = Message.obtain();
		//	coodinatemessage.replyTo = mesenger;
			if (position.equals("xike")) {
				if(intent.getExtras().getInt("intent") == XIKEWEBMAP_INTENT){
					System.out.println("..............positionpositionpositionpositionposition");
					// 利用bundle发送数据 
					setbundle.putString("location", location);
					setbundle.putInt("abscissa", abscissa);
					setbundle.putInt("ordinate", ordinate);
					coodinatemessage.what = xike;// 定义状态码
					coodinatemessage.setData(setbundle);
					/*new Thread(new Runnable() {
						
						@Override
						public void run() {*/
							// TODO Auto-generated method stub
							handler.sendMessage(coodinatemessage);
							try {
//								mesenger.send(coodinatemessage);
							} catch (Exception e) {
								e.printStackTrace();
						}
					/*			}
					}).start();*/
				}
				System.out.println("onHandleIntent");
				position = "";
				location = "";
				abscissa = -1;
				ordinate = -1;
				}
				
		}

	}
}
