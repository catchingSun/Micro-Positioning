package com.qrcode.location.room;


import com.qrcode.location.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RoomMessageShareActivity extends Activity {
	
//@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		 
		 Button btn = (Button)findViewById(R.id.button1);
	      
	        btn.setOnClickListener(new Button.OnClickListener() {
	            public void onClick(View view) {
	            	Toast.makeText(getApplicationContext(), "功能没有完成呢****",
	        			     Toast.LENGTH_SHORT).show(); 
	            }
	        });
	}
}
			
