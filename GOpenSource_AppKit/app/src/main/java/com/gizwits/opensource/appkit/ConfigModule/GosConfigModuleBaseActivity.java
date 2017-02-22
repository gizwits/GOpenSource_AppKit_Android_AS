package com.gizwits.opensource.appkit.ConfigModule;

import java.util.Timer;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

public class GosConfigModuleBaseActivity extends GosBaseActivity {

	private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

		/** 用于设备配置 */
		public void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {
			GosConfigModuleBaseActivity.this.didSetDeviceOnboarding(result, mac, did, productKey);
		};

	};

	/**
	 * 设备配置回调
	 * 
	 * @param result
	 *            错误码
	 * @param mac
	 *            MAC
	 * @param did
	 *            DID
	 * @param productKey
	 *            PK
	 */
	protected void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
		GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
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

	/**
	 * 推出提示
	 * 
	 * @param context
	 *            当前上下文
	 */
	protected void quitAlert(Context context) {
		final Dialog dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_gos_quit);

		LinearLayout llNo, llSure;
		llNo = (LinearLayout) window.findViewById(R.id.llNo);
		llSure = (LinearLayout) window.findViewById(R.id.llSure);

		llNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		llSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(dialog.isShowing()){
					dialog.cancel();
				}
				finish();
				
			}
		});
	}

	/**
	 * 退出提示
	 * 
	 * @param context
	 *            当前上下文
	 * @param timer
	 *            已开启定时器
	 */
	protected void quitAlert(Context context, final Timer timer) {
		final Dialog dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_gos_quit);

		LinearLayout llNo, llSure;
		llNo = (LinearLayout) window.findViewById(R.id.llNo);
		llSure = (LinearLayout) window.findViewById(R.id.llSure);

		llNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		llSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (timer != null) {
					timer.cancel();
				}
				if(dialog.isShowing()){
					dialog.cancel();
				}
				finish();
			}
		});
	}

}
