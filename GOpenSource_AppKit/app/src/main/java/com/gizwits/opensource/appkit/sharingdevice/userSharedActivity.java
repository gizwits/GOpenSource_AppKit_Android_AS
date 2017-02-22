package com.gizwits.opensource.appkit.sharingdevice;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingWay;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.TipsDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class userSharedActivity extends GosBaseActivity {

	private String productname;
	private EditText username;
	private int chooseitem = 0;
	private String did;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_user_shared);

		setActionBar(true, true, R.string.account_shared);

		initData();
		initView();
	}

	private void initView() {
		TextView usersharedtext = (TextView) findViewById(R.id.usersharedtext);

		username = (EditText) findViewById(R.id.username);
		usersharedtext.setText(
				getResources().getString(R.string.shared) + productname + getResources().getString(R.string.friends));
	}

	private void initData() {

		Intent tent = getIntent();
		productname = tent.getStringExtra("productname");
		did = tent.getStringExtra("did");
	}

	public void usershared(View v) {

		final String usernametext = username.getText().toString();
		if (TextUtils.isEmpty(usernametext)) {

			// Toast.makeText(this,
			// getResources().getString(R.string.toast_name_empet), 0).show();

			TipsDialog dia = new TipsDialog(this, getResources().getString(R.string.toast_name_empet));
			dia.show();
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.friends);
		chooseitem = 0;
		builder.setTitle(getResources().getString(R.string.chooseusertype));
		builder.setSingleChoiceItems(R.array.usertype, 0, new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				chooseitem = arg1;
			}
		});

		builder.setPositiveButton(getResources().getString(R.string.confirm), new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				SharedPreferences spf = getSharedPreferences("set", Context.MODE_PRIVATE);
				String token = spf.getString("Token", "");
				GizUserAccountType gizuser = null;
				switch (chooseitem) {
				case 0:
					gizuser = GizUserAccountType.GizUserNormal;
					break;

				case 1:
					gizuser = GizUserAccountType.GizUserPhone;
					break;

				case 2:
					gizuser = GizUserAccountType.GizUserEmail;
					break;

				case 3:
					gizuser = GizUserAccountType.GizUserOther;
					break;

				default:
					break;
				}
				GizDeviceSharing.sharingDevice(token, did, GizDeviceSharingWay.GizDeviceSharingByNormal, usernametext,
						gizuser);

				arg0.dismiss();
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.no), null);

		builder.show();

	}

	@Override
	protected void onResume() {
		super.onResume();

		GizDeviceSharing.setListener(new GizDeviceSharingListener() {

			@Override
			public void didSharingDevice(GizWifiErrorCode result, String deviceID, int sharingID,
					Bitmap QRCodeImage) {
				super.didSharingDevice(result, deviceID, sharingID, QRCodeImage);

				if (result.ordinal() == 0) {
					Toast.makeText(userSharedActivity.this, getResources().getString(R.string.alawyssend), 1).show();
					finish();
				} else {
					Toast.makeText(userSharedActivity.this, getResources().getString(R.string.send_failed1), 2).show();

				}

			}

		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			finish();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

}
