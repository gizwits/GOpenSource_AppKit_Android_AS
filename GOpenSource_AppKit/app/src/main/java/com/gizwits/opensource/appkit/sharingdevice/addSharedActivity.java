package com.gizwits.opensource.appkit.sharingdevice;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class addSharedActivity extends GosBaseActivity {

	private String productname;
	private String did;
	private LinearLayout devicetwoshared;
	private LinearLayout usershared;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.gos_activity_shar);
		setActionBar(true, true, R.string.addshared);

		initData();
		initView();
	}

	private void initView() {
		TextView usersharedtext = (TextView) findViewById(R.id.usersharedtext);
		
		devicetwoshared = (LinearLayout) findViewById(R.id.devicetwoshared);
		usershared = (LinearLayout) findViewById(R.id.usershared);
		
		usersharedtext.setText(
				getResources().getString(R.string.shared) + productname + getResources().getString(R.string.friends));
	}

	private void initData() {

		Intent tent = getIntent();
		productname = tent.getStringExtra("productname");
		did = tent.getStringExtra("did");
	}

	// 二维码分享
	public void devicetwoshared(View v) {

		Intent tent = new Intent(this, twoSharedActivity.class);
		tent.putExtra("productname", productname);
		tent.putExtra("did", did);
		startActivity(tent);
		
		devicetwoshared.setEnabled(false);
		devicetwoshared.postDelayed(new Runnable() {
			@Override
			public void run() {
				devicetwoshared.setEnabled(true);
			}
		}, 1000);

	}

	// 用户账号分享

	public void usershared(View v) {

		Intent tent = new Intent(this, userSharedActivity.class);
		tent.putExtra("productname", productname);
		tent.putExtra("did", did);
		startActivity(tent);
		
		usershared.setEnabled(false);
		usershared.postDelayed(new Runnable() {
			@Override
			public void run() {
				usershared.setEnabled(true);
			}
		}, 1000);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
