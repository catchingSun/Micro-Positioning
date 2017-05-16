package com.qrcode.location.room;


import com.qrcode.location.R;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.annotation.SuppressLint;  
import android.annotation.TargetApi;  
import android.os.Build;
import android.app.ActionBar;  
import android.app.ActionBar.OnNavigationListener;  
import android.app.FragmentManager;  
import android.app.FragmentTransaction;  
import android.widget.ArrayAdapter;  
import android.widget.SpinnerAdapter; 
import android.app.Activity;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi")  
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BuildingsActivity extends TabActivity {

private TabHost tabhost;
public static  Activity title; 
@SuppressLint("NewApi")    
@Override
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);       
        setContentView(R.layout.shiliujiao);
        title=this;
	
        SpinnerAdapter adapter=ArrayAdapter.createFromResource(this, R.array.floor,   
                android.R.layout.simple_spinner_dropdown_item);  
        ActionBar bar=getActionBar();  
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);  
        bar.setListNavigationCallbacks(adapter, new Down_to_Up()); 
        
        //��TabActivity�����ȡ����Tab��TabHost
        tabhost = getTabHost();
        
        //�����±�ǩone
        TabHost.TabSpec tab1 = tabhost.newTabSpec("one");
        //���ñ�ǩ����
        tab1.setIndicator("һ¥");
        //���øñ�ǩ�Ĳ�������
        tab1.setContent(R.id.LinearLayout1);
        
        
        TabHost.TabSpec tab2 = tabhost.newTabSpec("two");
        tab2.setIndicator("��¥");
        tab2.setContent(R.id.LinearLayout2);
        
        TabHost.TabSpec tab3 = tabhost.newTabSpec("three");
        tab3.setIndicator("��¥");
        tab3.setContent(R.id.LinearLayout3);
        
        TabHost.TabSpec tab4 = tabhost.newTabSpec("four");
        tab4.setIndicator("��¥");
        tab4.setContent(R.id.LinearLayout4);
        
        TabHost.TabSpec tab5 = tabhost.newTabSpec("five");
        tab5.setIndicator("��¥");
        tab5.setContent(R.id.LinearLayout5);
        
        tabhost.addTab(tab1);
        tabhost.addTab(tab2);
        tabhost.addTab(tab3);
        tabhost.addTab(tab4);
        tabhost.addTab(tab5);

        //readHtmlFormAssets();	
			 
    }
    
//�������¼� 
@TargetApi(Build.VERSION_CODES.HONEYCOMB)  
class Down_to_Up implements OnNavigationListener{  

    String[] listNames = getResources().getStringArray(R.array.floor);  

     //��ѡ�������˵����ʱ�򣬽�Activity�е������û�Ϊ��Ӧ��Fragment   
    public boolean onNavigationItemSelected(int itemPosition, long itemId)  
    {  
      // �����Զ���Fragment  
        RoomMapActivity student = new RoomMapActivity();  
        FragmentManager manager = getFragmentManager();  
        FragmentTransaction transaction = manager.beginTransaction(); 
        // ��Activity�е������滻�ɶ�Ӧѡ���Fragment  
       transaction.replace(R.id.context, student, listNames[itemPosition]);       
        transaction.commit();  
                return true;  
       
    }
    
       
}   

    
}
