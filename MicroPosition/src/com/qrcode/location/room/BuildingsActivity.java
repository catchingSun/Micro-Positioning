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
        
        //从TabActivity上面获取放置Tab的TabHost
        tabhost = getTabHost();
        
        //创建新标签one
        TabHost.TabSpec tab1 = tabhost.newTabSpec("one");
        //设置标签标题
        tab1.setIndicator("一楼");
        //设置该标签的布局内容
        tab1.setContent(R.id.LinearLayout1);
        
        
        TabHost.TabSpec tab2 = tabhost.newTabSpec("two");
        tab2.setIndicator("二楼");
        tab2.setContent(R.id.LinearLayout2);
        
        TabHost.TabSpec tab3 = tabhost.newTabSpec("three");
        tab3.setIndicator("三楼");
        tab3.setContent(R.id.LinearLayout3);
        
        TabHost.TabSpec tab4 = tabhost.newTabSpec("four");
        tab4.setIndicator("四楼");
        tab4.setContent(R.id.LinearLayout4);
        
        TabHost.TabSpec tab5 = tabhost.newTabSpec("five");
        tab5.setIndicator("五楼");
        tab5.setContent(R.id.LinearLayout5);
        
        tabhost.addTab(tab1);
        tabhost.addTab(tab2);
        tabhost.addTab(tab3);
        tabhost.addTab(tab4);
        tabhost.addTab(tab5);

        //readHtmlFormAssets();	
			 
    }
    
//下拉框事件 
@TargetApi(Build.VERSION_CODES.HONEYCOMB)  
class Down_to_Up implements OnNavigationListener{  

    String[] listNames = getResources().getStringArray(R.array.floor);  

     //当选择下拉菜单项的时候，将Activity中的内容置换为对应的Fragment   
    public boolean onNavigationItemSelected(int itemPosition, long itemId)  
    {  
      // 生成自定的Fragment  
        RoomMapActivity student = new RoomMapActivity();  
        FragmentManager manager = getFragmentManager();  
        FragmentTransaction transaction = manager.beginTransaction(); 
        // 将Activity中的内容替换成对应选择的Fragment  
       transaction.replace(R.id.context, student, listNames[itemPosition]);       
        transaction.commit();  
                return true;  
       
    }
    
       
}   

    
}
