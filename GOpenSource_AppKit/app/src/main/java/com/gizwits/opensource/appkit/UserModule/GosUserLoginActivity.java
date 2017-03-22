package com.gizwits.opensource.appkit.UserModule;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizEventType;
import com.gizwits.gizwifisdk.enumration.GizThirdAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.GosApplication;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.CommonModule.TipsDialog;
import com.gizwits.opensource.appkit.DeviceModule.GosDeviceListActivity;
import com.gizwits.opensource.appkit.DeviceModule.GosMainActivity;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.ThirdAccountModule.BaseUiListener;
import com.gizwits.opensource.appkit.view.DotView;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

@SuppressLint("HandlerLeak")
public class GosUserLoginActivity extends GosUserModuleBaseActivity implements OnClickListener {


	GosPushManager gosPushManager;

	/** The et Name */
	private static EditText etName;

	/** The et Psw */
	private static EditText etPsw;

	/** The btn Login */
	private Button btnLogin;

	/** The tv Register */
	private TextView tvRegister;

	/** The tv Forget */
	private TextView tvForget;

	/** The tv Pass */
	private TextView tvPass;

	/** The cb Laws */
	private CheckBox cbLaws;

	/** The ll QQ */
	private LinearLayout llQQ;

	/** The ll Wechat */
	private LinearLayout llWechat;

	/** The Tencent */
	private Tencent mTencent;

	/** The Wechat */
	public static IWXAPI mIwxapi;

	/** The Scope */
	private String Scope = "get_user_info,add_t";

	/** The IUiListener */
	IUiListener listener;

	Intent intent;

	/** The GizThirdAccountType */
	public static GizThirdAccountType gizThirdAccountType;

	/** The THRED_LOGIN UID&TOKEN */
	public static String thirdUid, thirdToken;

	public static enum handler_key {

		/** 登录 */
		LOGIN,

		/** 自动登录 */
		AUTO_LOGIN,

		/** 第三方登录 */
		THRED_LOGIN,

	}

	/** 与WXEntryActivity共用Handler */
	private Handler baseHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {
			// 登录
			case LOGIN:
				progressDialog.show();
				GosDeviceListActivity.loginStatus = 0;
				GizWifiSDK.sharedInstance().userLogin(etName.getText().toString(), etPsw.getText().toString());
				break;
			// 自动登录
			case AUTO_LOGIN:
				progressDialog.show();
				GosDeviceListActivity.loginStatus = 0;
				GizWifiSDK.sharedInstance().userLogin(spf.getString("UserName", ""), spf.getString("PassWord", ""));
				break;
			// 第三方登录
			case THRED_LOGIN:
				progressDialog.show();
				GosDeviceListActivity.loginStatus = 0;
				GizWifiSDK.sharedInstance().loginWithThirdAccount(gizThirdAccountType, thirdUid, thirdToken);
				spf.edit().putString("thirdUid", thirdUid).commit();
				break;

			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
		if (!this.isTaskRoot()) {// 判断此activity是不是任务控件的源Activity，“非”也就是说是被系统重新实例化出来的
			Intent mainIntent = getIntent();
			String action = mainIntent.getAction();
			if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
				finish();
				return;
			}
		}
		if (GosApplication.flag != 0) {
			GosBaseActivity.noIDAlert(this, R.string.AppID_Toast);
		}

		// 在配置文件中选择推送类型（0：不开启推送，1：极光推送，2：百度推送。默认为0）
		gosPushManager = new GosPushManager(GosDeploy.setPushType(), this);

		setContentView(R.layout.activity_gos_user_login);


		// 设置actionBar
		setActionBar(false, false, R.string.app_company);
		initView();
		initEvent();
	}




	@Override
	protected void onResume() {
		super.onResume();

		JPushInterface.onResume(this);
		autoLogin();

		cleanuserthing();
	}

	private void cleanuserthing() {

		if (isclean) {
			etName.setText("");
			;
			etPsw.setText("");
			;
		}
	}

	private void autoLogin() {

		if (TextUtils.isEmpty(spf.getString("UserName", "")) || TextUtils.isEmpty(spf.getString("PassWord", ""))) {
			return;
		}

		baseHandler.sendEmptyMessageDelayed(handler_key.AUTO_LOGIN.ordinal(), 1000);
	}

	private void initView() {
		etName = (EditText) findViewById(R.id.etName);
		etPsw = (EditText) findViewById(R.id.etPsw);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		tvRegister = (TextView) findViewById(R.id.tvRegister);
		tvForget = (TextView) findViewById(R.id.tvForget);
		tvPass = (TextView) findViewById(R.id.tvPass);
		cbLaws = (CheckBox) findViewById(R.id.cbLaws);

		DotView DotView = (DotView) findViewById(R.id.dotView1);
		llQQ = (LinearLayout) findViewById(R.id.llQQ);
		llWechat = (LinearLayout) findViewById(R.id.llWechat);
		String setTencentAppID = GosDeploy.setTencentAppID();
		String setWechatAppID = GosDeploy.setWechatAppID();
		// 判断腾讯和微信是否需要隐藏和显示
		setWechatOrTencentIsVisable(DotView);
		// 配置文件部署
		btnLogin.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
		btnLogin.setTextColor(GosDeploy.setButtonTextColor());

	}

	protected void setWechatOrTencentIsVisable(DotView DotView) {
		if (!GosDeploy.setWechat()) {

			llWechat.setVisibility(View.GONE);
		}
		if (!GosDeploy.setQQ()) {

			llQQ.setVisibility(View.GONE);
		}

		if (!GosDeploy.setWechat() && !GosDeploy.setQQ()) {
			DotView.setVisibility(View.GONE);
		}
	}

	private void initEvent() {
		btnLogin.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		tvForget.setOnClickListener(this);
		tvPass.setOnClickListener(this);

		llQQ.setOnClickListener(this);
		llWechat.setOnClickListener(this);

		cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String psw = etPsw.getText().toString();

				if (isChecked) {
					etPsw.setInputType(0x90);
				} else {
					etPsw.setInputType(0x81);
				}
				etPsw.setSelection(psw.length());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:

			if (TextUtils.isEmpty(etName.getText().toString())) {
				Toast.makeText(GosUserLoginActivity.this, R.string.toast_name_wrong, toastTime).show();
				return;
			}
			if (TextUtils.isEmpty(etPsw.getText().toString())) {
				Toast.makeText(GosUserLoginActivity.this, R.string.toast_psw_wrong, toastTime).show();
				return;
			}
			baseHandler.sendEmptyMessage(handler_key.LOGIN.ordinal());
			break;

		case R.id.tvRegister:
			intent = new Intent(GosUserLoginActivity.this, GosRegisterUserActivity.class);
			startActivity(intent);

			break;
		case R.id.tvForget:
			intent = new Intent(GosUserLoginActivity.this, GosForgetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.tvPass:

			intent = new Intent(GosUserLoginActivity.this, GosMainActivity.class);
			startActivity(intent);

			logoutToClean();
			break;

		case R.id.llQQ:
			String tencentAPPID = GosDeploy.setTencentAppID();
			if (TextUtils.isEmpty(tencentAPPID) || tencentAPPID.contains("your_tencent_app_id")) {
				noIDAlert(this, R.string.TencentAPPID_Toast);
				return;
			} else {
				// 启动QQ登录SDK
				mTencent = Tencent.createInstance(GosDeploy.setTencentAppID(), this.getApplicationContext());
			}

			listener = new BaseUiListener() {
				protected void doComplete(JSONObject values) {
					Message msg = new Message();
					try {
						if (values.getInt("ret") == 0) {
							gizThirdAccountType = GizThirdAccountType.GizThirdQQ;
							thirdUid = values.getString("openid").toString();
							thirdToken = values.getString("access_token").toString();
							msg.what = handler_key.THRED_LOGIN.ordinal();
							baseHandler.sendMessage(msg);
						} else {

							Toast.makeText(GosUserLoginActivity.this, msg.obj.toString(), toastTime).show();

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			mTencent.login(this, Scope, listener);
			break;
		case R.id.llWechat:
			String wechatAppID = GosDeploy.setWechatAppID();
			String wechatAppSecret = GosDeploy.setWechatAppSecret();
			if (TextUtils.isEmpty(wechatAppID) || TextUtils.isEmpty(wechatAppSecret)
					|| wechatAppID.contains("your_wechat_app_id")
					|| wechatAppSecret.contains("your_wechat_app_secret")) {
				noIDAlert(this, R.string.WechatAppID_Toast);
				return;
			} else {

				// 设置与WXEntryActivity共用Handler
				setBaseHandler(baseHandler);

				// 启动微信登录SDK
				mIwxapi = WXAPIFactory.createWXAPI(this, wechatAppID, true);

				// 将应用的AppID注册到微信
				mIwxapi.registerApp(wechatAppID);
			}

			if (!(mIwxapi.isWXAppInstalled() && mIwxapi.isWXAppSupportAPI())) {
				noIDAlert(this, R.string.No_WXApp);
				return;
			}
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo_test";
			mIwxapi.sendReq(req);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Tencent.onActivityResultData(requestCode, resultCode, data, listener);
	}

	/**
	 * 设置云端服务回调
	 */
	protected void didGetCurrentCloudService(GizWifiErrorCode result,
			java.util.concurrent.ConcurrentHashMap<String, String> cloudServiceInfo) {
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			Toast.makeText(this, toastError(result), toastTime).show();
		}
	}

	/**
	 * 用户登录回调
	 */
	@Override
	protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {

		progressDialog.cancel();
		Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "User");
		if (GosDeviceListActivity.loginStatus == 4 || GosDeviceListActivity.loginStatus == 3) {
			return;
		}
		Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "UserLogin");

		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {// 登录失败
			Toast.makeText(GosUserLoginActivity.this, toastError(result), toastTime).show();

		} else {// 登录成功

			GosDeviceListActivity.loginStatus = 1;
			Toast.makeText(GosUserLoginActivity.this, R.string.toast_login_successful, toastTime).show();

			// TODO 绑定推送
			GosPushManager.pushBindService(token);

			if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPsw.getText().toString())
					&& TextUtils.isEmpty(spf.getString("thirdUid", ""))) {
				spf.edit().putString("UserName", etName.getText().toString()).commit();
				spf.edit().putString("PassWord", etPsw.getText().toString()).commit();
			}
			spf.edit().putString("Uid", uid).commit();
			spf.edit().putString("Token", token).commit();

			intent = new Intent(GosUserLoginActivity.this, GosMainActivity.class);
			intent.putExtra("ThredLogin", true);
			startActivity(intent);

		}
	}

	/**
	 * 解绑推送回调
	 * 
	 * @param result
	 */
	protected void didChannelIDUnBind(GizWifiErrorCode result) {
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			Toast.makeText(this, toastError(result), toastTime).show();
		}

		Log.i("Apptest", "UnBind:" + result.toString());
	};

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			String doubleClick = (String) getText(R.string.double_click);
			Toast.makeText(this, doubleClick, toastTime).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			this.finish();
			System.exit(0);
		}
	}

	@Override
	protected void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID,
			String eventMessage) {
		super.didNotifyEvent(eventType, eventSource, eventID, eventMessage);

		if (eventID != null && eventID.getResult() != 8316) {
			String toastError = toastError(eventID);

			TipsDialog dia = new TipsDialog(this, toastError);
			dia.show();
		}

	}

	/** 注销函数 */
	void logoutToClean() {
		spf.edit().putString("UserName", "").commit();
		spf.edit().putString("PassWord", "").commit();
		spf.edit().putString("Uid", "").commit();
		spf.edit().putString("thirdUid", "").commit();

		spf.edit().putString("Token", "").commit();

		if (GosDeviceListActivity.loginStatus == 1) {
			GosDeviceListActivity.loginStatus = 0;
		} else {
			GosDeviceListActivity.loginStatus = 4;
		}

	}






}
