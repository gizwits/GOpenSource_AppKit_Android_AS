package com.gizwits.opensource.appkit.ConfigModule;

import java.util.Timer;
import java.util.TimerTask;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.WifiAutoConnectManager;
import com.gizwits.opensource.appkit.CommonModule.WifiAutoConnectManager.WifiCipherType;
import com.gizwits.opensource.appkit.utils.NetUtils;
import com.gizwits.opensource.appkit.view.RoundProgressBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class GosConfigCountdownActivity extends GosConfigModuleBaseActivity {

	private GosWifiChangeReciver broadcase;

	/** The tv Time */
	private TextView tvTimer;

	/** The rpb Config */
	private RoundProgressBar rpbConfig;

	/** 倒计时 */
	int secondleft = 60;

	/** The timer */
	Timer timer;

	/** The Frist */
	boolean isFrist = true;

	/** The isChecked */
	boolean isChecked = false;

	String softSSID, presentSSID, workSSID, workSSIDPsw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_config_countdown);
		// 设置ActionBar
		setActionBar(false, false, R.string.configcountDown_title);

		initView();
		initData();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isFrist) {
			progressDialog.show();
			new Thread() {
				public void run() {
					try {
						Thread.sleep(1 * 1000);
						readyToSoftAP();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				};
			}.start();

			isFrist = false;
		}
	}

	private void initView() {
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		RelativeLayout cel_layout = (RelativeLayout) findViewById(R.id.layoutparms);
		LayoutParams params = cel_layout.getLayoutParams();
		params.height = width;
		params.width = width;
		cel_layout.setLayoutParams(params);
		tvTimer = (TextView) findViewById(R.id.tvTimer);
		rpbConfig = (RoundProgressBar) findViewById(R.id.rpbConfig);

		String softAPLoadingText = (String) getText(R.string.softap_loading);
		progressDialog.setMessage(softAPLoadingText);

	}

	private void initData() {
		softSSID = getIntent().getStringExtra("softSSID");
		
	}

	private enum handler_key {

		/**
		 * 倒计时通知
		 */
		TICK_TIME,

		/**
		 * 倒计时开始
		 */
		START_TIMER,

		/**
		 * 配置成功
		 */
		SUCCESSFUL,

		/**
		 * 配置失败
		 */
		FAILED, OFFTIME,

	}

	/**
	 * The handler.
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {

			case TICK_TIME:
				String timerText = (String) getText(R.string.timer_text);
				tvTimer.setText(timerText);
				break;
			case START_TIMER:
				isStartTimer();
				break;

			case SUCCESSFUL:
				Toast.makeText(GosConfigCountdownActivity.this, R.string.configuration_successful, toastTime)
						.show();
				finish();
				break;

			case FAILED:
				Toast.makeText(GosConfigCountdownActivity.this,  msg.obj.toString(), toastTime)
						.show();
				Intent intent = new Intent(GosConfigCountdownActivity.this, GosConfigFailedActivity.class);
				startActivity(intent);
				finish();
				break;
				
			case OFFTIME:
				GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw,
						GizWifiConfigureMode.GizWifiSoftAP, presentSSID, 60, null);
				break;

			default:
				break;

			}
		}

	};

	// 屏蔽掉返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			quitAlert(this, timer);
			return true;
		}
		return false;
	}

	// 倒计时
	public void isStartTimer() {
		secondleft = 60;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				secondleft--;
				rpbConfig.setProgress((60 - secondleft) * (100 / 60.0));
				if (secondleft == 58) {
					handler.sendEmptyMessage(handler_key.TICK_TIME.ordinal());
				}
				if (secondleft < 0) {

				}

			}
		}, 1000, 1000);
	}

	private void readyToSoftAP() {

		// 切换至设备热点
		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiCipherType cipherType = WifiAutoConnectManager.getCipherType(GosConfigCountdownActivity.this, softSSID);
		WifiAutoConnectManager manager = new WifiAutoConnectManager(wifiManager);
		manager.connect(softSSID, SoftAP_PSW, cipherType);
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

		workSSID = spf.getString("workSSID", "");
		workSSIDPsw = spf.getString("workSSIDPsw", "");
		final Timer mtimer = new Timer();
		mtimer.schedule(new TimerTask() {

			@Override	
			public void run() {
				if (progressDialog.isShowing()) {
					progressDialog.cancel();
					mtimer.cancel();
					String failText=(String) getText(R.string.configuration_failed);
					Message msg=new Message();
					msg.what=handler_key.FAILED.ordinal();
					msg.obj=failText;
					handler.sendMessage(msg);
				} else {
					mtimer.cancel();
				}
			}
		}, 10 * 1000);
		isChecked = true;
		while (isChecked) {
			String presentSSID = NetUtils.getCurentWifiSSID(GosConfigCountdownActivity.this);
			if (!TextUtils.isEmpty(presentSSID) && presentSSID.contains(SoftAP_Start)) {
				if (checkNetwork(GosConfigCountdownActivity.this)) {
					
					progressDialog.cancel();
					isChecked = false;
					handler.sendEmptyMessage(handler_key.START_TIMER.ordinal());
					GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw,
							GizWifiConfigureMode.GizWifiSoftAP, presentSSID, 60, null);
				//	handler.sendEmptyMessageDelayed(handler_key.OFFTIME.ordinal(), 2000);
				}
				if(broadcase==null){
					broadcase = new GosWifiChangeReciver();
					registerReceiver(broadcase, filter);
				}
				
			}
		}
	}

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
		if (GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING == result) {
			return;
		}
		if (timer != null) {
			timer.cancel();
		}
		Message message = new Message();
		if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
			message.what = handler_key.SUCCESSFUL.ordinal();
		} else {
			message.what = handler_key.FAILED.ordinal();
			message.obj = toastError(result);
		}
		handler.sendMessage(message);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isChecked = false;
		
		if(broadcase!=null){
			unregisterReceiver(broadcase);
			broadcase = null;
		}
		

	}
}
