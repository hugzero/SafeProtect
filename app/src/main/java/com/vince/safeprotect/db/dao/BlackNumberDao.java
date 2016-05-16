package com.vince.safeprotect.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vince.safeprotect.db.dao.BlackNumberDBOpenHelper;
import com.vince.safeprotect.domain.BlackNumberInfo;
import com.vince.safeprotect.util.StreamTools;

/**
 * 黑名单数据库的增删改查业务类
 * @author Administrator
 *
 */
public class BlackNumberDao {
	private BlackNumberDBOpenHelper helper;
	/**
	 * 构造方法
	 * @param context 上下文
	 */
	public BlackNumberDao(Context context) {
		helper = new BlackNumberDBOpenHelper(context);
	}
	/**
	 * 查询黑名单号码是是否存在
	 * @param number
	 * @return
	 */
	public boolean find(String number){
		boolean result = false;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blacknumber where number=?", new String[]{number});
		if(cursor.moveToNext()){
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 查询黑名单号码的拦截模式
	 * @param number
	 * @return 返回号码的拦截模式，不是黑名单号码返回null
	 */
	public String findMode(String number){
		String result = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select mode from blacknumber where number=?", new String[]{number});
		if(cursor.moveToNext()){
			result = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return result;
	}
	/**
	 * 查询全部黑名单号码
	 * offset从哪个位置获取数据
	 * maxnumber 一次最多获取多少条记录
	 * @return
	 */
	public List<BlackNumberInfo> findPart(int offset,int maxnumber){

		List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select number,mode from blacknumber order by _id desc limit ? offset ?",new
		String[]{String.valueOf(maxnumber), String.valueOf(offset)});
		while(cursor.moveToNext()){
			BlackNumberInfo info = new BlackNumberInfo();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			info.setMode(mode);
			info.setNumber(number);
			result.add(info);
		}
		cursor.close();
		db.close();
		return result;
	}
	
	
	/**
	 * 添加黑名单号码
	 * @param number 黑名单号码
	 * @param mode 拦截模式 1.电话拦截 2.短信拦截 3.全部拦截
	 */
	public void add(String number,String mode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		db.insert("blacknumber", null, values);
		db.close();
	}
	/**
	 * 修改黑名单号码的拦截模式
	 * @param number 要修改的黑名单号码
	 * @param newmode 新的拦截模式
	 */
	public void update(String number,String newmode){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blacknumber", values, "number=?", new String[]{number});
		db.close();
	}
	/**
	 * 删除黑名单号码
	 * @param number 要删除的黑名单号码
	 */
	public void delete(String number){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blacknumber", "number=?", new String[]{number});
		db.close();
	}

	/**
	 * 查找一条黑名单号码的拦截模式
	 */
	public int findNumberMode(String number) {
		// 拦截模式只有3种：0代表拦截短信，1代表拦截电话，2代表拦截短信与电话。这里的默认值为-1，表示的是没有标记拦截模式
		int result = -1;
		SQLiteDatabase db = helper.getReadableDatabase();
		if (db.isOpen()) {
			// 由于一条号码值对应一个拦截模式，所以，该结果集中只有一条数据
			Cursor cursor = db.rawQuery(
					"select mode from blacknumber where number =?",
					new String[] { number });
			if (cursor.moveToFirst()) {
				// 获取第一条数据（也仅有一条数据）
				result = cursor.getInt(0);
			}
			cursor.close();
			db.close();
		}
		return result;
	}

}
