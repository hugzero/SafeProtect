package com.vince.safeprotect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vince.safeprotect.util.SmsUtils;

public class AtoolsActivity extends Activity implements View.OnClickListener {
	private TextView tv_atools_applock;//程序锁
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
		tv_atools_applock = (TextView)findViewById(R.id.tv_atools_applock);
		tv_atools_applock.setOnClickListener(this);
	}
	
	/**
	 * 点击事件，进入号码归属地查询的页面
	 * @param view
	 */
	public void numberQuery(View view){
		Intent intentv = new Intent(this,NumberAddressQueryActivity.class);
		startActivity(intentv);
		
	}

	public void smsRestore(View view) {
		SmsUtils.restoreSms(this,true);
		Toast.makeText(this,"成功还原",Toast.LENGTH_SHORT).show();
	}

	public void smsBackup(View view) {
		try {
			SmsUtils.backupSms(this);
			Toast.makeText(this,"备份成功",Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("baobao", "smsBackup: " + e.getMessage());

			Toast.makeText(this,"备份失败",Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.tv_atools_applock://程序锁
				Intent applockIntent = new Intent(this,AppLockActivity.class);
				startActivity(applockIntent);
				break;
		}
	}
}
