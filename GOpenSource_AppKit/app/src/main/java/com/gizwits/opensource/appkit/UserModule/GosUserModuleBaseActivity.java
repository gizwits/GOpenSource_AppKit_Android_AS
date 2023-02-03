package com.gizwits.opensource.appkit.UserModule;

import android.view.MenuItem;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;

public class GosUserModuleBaseActivity extends GosBaseActivity {

	private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

		/** 用于用户登录 */
		public void didUserLogin(GizWifiErrorCode result, String uid, String token) {
			GosUserModuleBaseActivity.this.didUserLogin(result, uid, token);
		};

		/** 用于手机验证码 */
		public void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
			GosUserModuleBaseActivity.this.didRequestSendPhoneSMSCode(result, token);

		};

		/** 用于用户注册 */
		public void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
			GosUserModuleBaseActivity.this.didRegisterUser(result, uid, token);
		};

		/** 用于重置密码 */
		public void didChangeUserPassword(GizWifiErrorCode result) {
			GosUserModuleBaseActivity.this.didChangeUserPassword(result);
		};

		/** 用于解绑推送 */
		public void didChannelIDUnBind(GizWifiErrorCode result) {
			GosUserModuleBaseActivity.this.didChannelIDUnBind(result);
		};

		/** 用于设置云端服务环境 */
		public void didGetCurrentCloudService(GizWifiErrorCode result,
				java.util.concurrent.ConcurrentHashMap<String, String> cloudServiceInfo) {
			GosUserModuleBaseActivity.this.didGetCurrentCloudService(result, cloudServiceInfo);
		};

	};

	/**
	 * 用户登录回调
	 * 
	 * @param result
	 *            错误码
	 * @param uid
	 *            用户ID
	 * @param token
	 *            授权令牌
	 */
	protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {
	};

	/**
	 * 手机验证码回调
	 * 
	 * @param result
	 *            错误码
	 * @param token
	 *            口令
	 */
	protected void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
	};

	/**
	 * 用户注册回调
	 * 
	 * @param result
	 *            错误码
	 * @param uid
	 *            用户ID
	 * @param token
	 *            授权令牌
	 */
	protected void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
	};

	/**
	 * 重置密码回调
	 * 
	 * @param result
	 *            错误码
	 */
	protected void didChangeUserPassword(GizWifiErrorCode result) {
	};

	/**
	 * 解绑推送回调
	 * 
	 * @param result
	 *            错误码
	 */
	protected void didChannelIDUnBind(GizWifiErrorCode result) {
	};

	/**
	 * 设置云端服务环境回调
	 * 
	 * @param result
	 *            错误码
	 * @param cloudServiceInfo
	 *            云端服务信息
	 */
	protected void didGetCurrentCloudService(GizWifiErrorCode result,
			java.util.concurrent.ConcurrentHashMap<String, String> cloudServiceInfo) {
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

}
