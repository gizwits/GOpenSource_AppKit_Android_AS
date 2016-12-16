package com.gizwits.opensource.appkit.UserModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.DeviceModule.GosDeviceListActivity;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;

public class GosUserManager extends GosBaseActivity {

	private static final int GOSUSERMANAGER = 234;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar(true, true, R.string.user_management);
		setContentView(R.layout.activity_gos_usermanager);

		initView();
	}

	private void initView() {
		TextView phoneusernumber = (TextView) findViewById(R.id.phoneusernumber);
		LinearLayout changeuserpassword = (LinearLayout) findViewById(R.id.changeuserpassword);
		if(!TextUtils.isEmpty(spf.getString("UserName", ""))){
			phoneusernumber.setText(spf.getString("UserName", ""));
		}else{
			
			String uid = spf.getString("thirdUid", "");
			String myuid = uid.substring(0, 2)+"***"+uid.substring(uid.length()-4, uid.length());
			phoneusernumber.setText(myuid);
			changeuserpassword.setVisibility(View.GONE);
		}
		
	}

	public void userlogout(View v) {
		setResult(GOSUSERMANAGER);
		logoutToClean();
		finish();
	}

	public void changeuserpassword(View v) {
		Intent tent = new Intent(this, GosChangeUserPasswordActivity.class);
		startActivity(tent);
	}

	private void logoutToClean() {
		GosPushManager.pushUnBindService(spf.getString("Token", ""));
		spf.edit().putString("UserName", "").commit();
		isclean = true;
		spf.edit().putString("PassWord", "").commit();
		spf.edit().putString("Uid", "").commit();
		spf.edit().putString("Token", "").commit();

		spf.edit().putString("thirdUid", "").commit();

		if (GosDeviceListActivity.loginStatus == 1) {
			GosDeviceListActivity.loginStatus = 0;
		} else {
			GosDeviceListActivity.loginStatus = 4;
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			isclean = false;
			finish();
			break;
		}
		return true;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isclean = false;
			finish();
			return true;
		}
		return false;
	}

}
