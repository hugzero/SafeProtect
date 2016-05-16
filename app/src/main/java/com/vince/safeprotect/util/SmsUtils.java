package com.vince.safeprotect.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**短信的工具类
 * Created by 10960 on 2016/5/14.
 */
public class SmsUtils {
    /**
     * 备份用户的短信
     */
    public static void backupSms(Context context) throws Exception {
        ContentResolver resolver = context.getContentResolver();
        File file  = new File(Environment.getExternalStorageDirectory(),"backup.xml");
        FileOutputStream fos = new FileOutputStream(file);
        //把用户的短信一条一条读出来，按照一定的格式写到文件里
        //获取XML的文件的生成器
        XmlSerializer serializer = Xml.newSerializer();
        //初始化生成器
        serializer.setOutput(fos, "utf-8");
        serializer.startDocument("utf-8", true);
        serializer.startTag(null, "smss");
        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[]{"body", "address", "type", "date"}, null, null, null);
        while (cursor.moveToNext()){
            String body = cursor.getString(0);
            String address = cursor.getString(1);
            String type = cursor.getString(2);
            String date = cursor.getString(3);
            serializer.startTag(null,"sms");

            serializer.startTag(null,"body");
            serializer.text(body);
            serializer.endTag(null, "body");

            serializer.startTag(null,"address");
            serializer.text(address);
            serializer.endTag(null,"address");

            serializer.startTag(null,"type");
            serializer.text(type);
            serializer.endTag(null,"type");

            serializer.startTag(null,"date");
            serializer.text(date);
            serializer.endTag(null,"date");

            serializer.endTag(null,"sms");

        }
        cursor.close();
        serializer.endTag(null, "smss");

        serializer.endDocument();
        fos.close();
    }

    public static void restoreSms(Context context,boolean flag){
        Uri uri = Uri.parse("content://sms/");
        if (flag){
            context.getContentResolver().delete(uri,null,null);
        }

        //1.读取SD卡上的XML
        //xml.newpullparser

        //2.读取max

        //3.读取每一条短信的信息,body,date,type,address

        //4.把短信插入到系统短息的应用

        ContentValues values = new ContentValues();
        values.put("body","woshi duanxin neirong");
        values.put("date","12345");
        values.put("type","1");
        values.put("address","5556");
        context.getContentResolver().insert(uri,values);
    }
}
