package com.qrcode.location.room;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * ��������ʵ�ִ�assetsĿ¼��ȡ���ݿ��ļ�Ȼ��д��SDcard��,�����SDcard�д��ڣ��ʹ����ݿ⣬�����ھʹ�assetsĿ¼�¸��ƹ�ȥ
 * @author Big_Adamapple
 *
 */
@SuppressLint("SdCardPath") public class RoomDatabaseOperation {  
    
  //���ݿ�洢·��  
  String filePath = "/data/data/com.qrcode.location/databases/floors.db";  
  //���ݿ��ŵ��ļ��� data/data/com.main.jh ����  
  String pathStr = "data/data/com.qrcode.location";  
    
  SQLiteDatabase database;   
  public  SQLiteDatabase openDatabase(Context context){  
    System.out.println("filePath:"+filePath);  
    File jhPath=new File(filePath);  
      //�鿴���ݿ��ļ��Ƿ����  
      if(jhPath.exists()){  
        Log.i("floors", "�������ݿ�");
        //������ֱ�ӷ��ش򿪵����ݿ�  
        return SQLiteDatabase.openOrCreateDatabase(jhPath, null);  
      }else{  
        //�������ȴ����ļ���  
        File path=new File(pathStr);  
        Log.i("floors", "pathStr="+path);
        if (path.mkdir()){  
          Log.i("floors", "�����ɹ�"); 
        }else{  
          Log.i("floors", "����ʧ��");
        };  
        try {  
          //�õ���Դ  
          AssetManager am= context.getAssets();  
          //�õ����ݿ��������  
          InputStream is=am.open("floors.db");  
          Log.i("floors", is+"");
          //�������д��SDcard����	
          FileOutputStream fos=new FileOutputStream(jhPath);  
          Log.i("floors", "fos="+fos);
          Log.i("floors", "jhPath="+jhPath);
          //����byte����  ����1KBдһ��  
          byte[] buffer=new byte[1024];  
          int count = 0;  
          while((count = is.read(buffer))>0){  
            Log.i("floors", "�õ�");
            fos.write(buffer,0,count);  
          }  
          //���رվͿ�����  
          fos.flush();  
          fos.close();  
          is.close();  
        } catch (IOException e) {  
          // TODO Auto-generated catch block  
          e.printStackTrace();  
          return null;
        }  
        //���û��������ݿ�  �����Ѿ�����д��SD�����ˣ�Ȼ����ִ��һ��������� �Ϳ��Է������ݿ���  
        return openDatabase(context);  
      }  
  }  
}
