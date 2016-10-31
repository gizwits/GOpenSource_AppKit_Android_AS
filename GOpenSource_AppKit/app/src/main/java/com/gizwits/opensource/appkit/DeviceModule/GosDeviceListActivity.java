package com.gizwits.opensource.appkit.DeviceModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.ConfigModule.GosAirlinkChooseDeviceWorkWiFiActivity;
import com.gizwits.opensource.appkit.ConfigModule.GosCheckDeviceWorkWiFiActivity;
import com.gizwits.opensource.appkit.ControlModule.GosDeviceControlActivity;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.SettingsModule.GosSettiingsActivity;
import com.gizwits.opensource.appkit.utils.NetUtils;
import com.gizwits.opensource.appkit.view.SlideListView2;
import com.gizwits.opensource.appkit.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import zxing.CaptureActivity;

@SuppressLint("HandlerLeak")
public class GosDeviceListActivity extends GosDeviceModuleBaseActivity implements OnClickListener {

	/** The ll NoDevice */
	private LinearLayout llNoDevice;

	/** The img NoDevice */
	private ImageView imgNoDevice;

	/** The btn NoDevice */
	private Button btnNoDevice;

	/** The ic BoundDevices */
	private View icBoundDevices;

	/** The ic FoundDevices */
	private View icFoundDevices;

	/** The ic OfflineDevices */
	private View icOfflineDevices;

	/** The tv BoundDevicesListTitle */
	private TextView tvBoundDevicesListTitle;

	/** The tv FoundDevicesListTitle */
	private TextView tvFoundDevicesListTitle;

	/** The tv OfflineDevicesListTitle */
	private TextView tvOfflineDevicesListTitle;

	/** The ll NoBoundDevices */
	private LinearLayout llNoBoundDevices;

	/** The ll NoFoundDevices */
	private LinearLayout llNoFoundDevices;

	/** The ll NoOfflineDevices */
	private LinearLayout llNoOfflineDevices;

	/** The slv BoundDevices */
	private SlideListView2 slvBoundDevices;

	/** The slv FoundDevices */
	private SlideListView2 slvFoundDevices;

	/** The slv OfflineDevices */
	private SlideListView2 slvOfflineDevices;

	/** The sv ListGroup */
	private ScrollView svListGroup;

	/** 适配器 */
	GosDeviceListAdapter myadapter;

	/** 设备列表分类 */
	List<GizWifiDevice> boundDevicesList, foundDevicesList, offlineDevicesList;

	/** 设备热点名称列表 */
	ArrayList<String> softNameList;

	/** 与APP绑定的设备的ProductKey */
	private List<String> ProductKeyList;

	Intent intent;

	String softssid, uid, token;

	boolean isItemClicked = false;

	public static List<String> boundMessage;

	boolean isFrist = true;

	// boolean isLogout = false;
	//
	// public static boolean isAnonymousLoging = false;

	/**
	 * 判断用户登录状态 0：未登录 1：实名用户登录 2：匿名用户登录 3：匿名用户登录中 4：匿名用户登录中断
	 */
	public static int loginStatus;

	int threeSeconds = 3;

	/** 获取设备列表 */
	protected static final int GETLIST = 0;

	/** 刷新设备列表 */
	protected static final int UPDATALIST = 1;

	/** 订阅成功前往控制页面 */
	protected static final int TOCONTROL = 2;

	/** 通知 */
	protected static final int TOAST = 3;

	/** 设备绑定 */
	protected static final int BOUND = 9;

	/** 设备解绑 */
	protected static final int UNBOUND = 99;

	/** 新设备提醒 */
	protected static final int SHOWDIALOG = 999;

	Handler handler = new Handler() {
		private AlertDialog myDialog;
		private TextView dialog_name;

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GETLIST:

				if (!uid.isEmpty() && !token.isEmpty()) {
					GizWifiSDK.sharedInstance().getBoundDevices(uid, token, ProductKeyList);
				}

				if (loginStatus == 0&&GosDeploy.setAnonymousLogin()) {
					loginStatus = 3;
					GizWifiSDK.sharedInstance().userLoginAnonymous();
				}

				break;

			case UPDATALIST:
				
				progressDialog.cancel();
				UpdateUI();
				break;

			case BOUND:

				break;

			case UNBOUND:
				progressDialog.show();
				GizWifiSDK.sharedInstance().unbindDevice(uid, token, msg.obj.toString());
				break;

			case TOCONTROL:
				intent = new Intent(GosDeviceListActivity.this, GosDeviceControlActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("GizWifiDevice", (GizWifiDevice) msg.obj);
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			case TOAST:

				Toast.makeText(GosDeviceListActivity.this, msg.obj.toString(), 2000).show();
				break;

			case SHOWDIALOG:

				if (!softNameList.toString()
						.contains(GosMessageHandler.getSingleInstance().getNewDeviceList().toString())) {
					AlertDialog.Builder builder = new AlertDialog.Builder(GosDeviceListActivity.this);
					View view = View.inflate(GosDeviceListActivity.this, R.layout.alert_gos_new_device, null);
					Button diss = (Button) view.findViewById(R.id.diss);
					Button ok = (Button) view.findViewById(R.id.ok);
					dialog_name = (TextView) view.findViewById(R.id.dialog_name);
					String foundOneDevice, foundManyDevices;
					foundOneDevice = (String) getText(R.string.not_text);
					foundManyDevices = (String) getText(R.string.found_many_devices);
					if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() < 1) {
						return;
					}
					if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() == 1) {
						String ssid = GosMessageHandler.getSingleInstance().getNewDeviceList().get(0);
						if (!TextUtils.isEmpty(ssid)
								&& ssid.equalsIgnoreCase(NetUtils.getCurentWifiSSID(GosDeviceListActivity.this))) {
							return;
						}
						if (softNameList.toString().contains(ssid)) {
							return;
						}
						softNameList.add(ssid);
						dialog_name.setText(ssid + foundOneDevice);
						softssid = ssid;
					} else {
						for (String s : GosMessageHandler.getSingleInstance().getNewDeviceList()) {
							if (!softNameList.toString().contains(s)) {
								softNameList.add(s);
							}
						}
						dialog_name.setText(foundManyDevices);
					}
					myDialog = builder.create();
					Window window = myDialog.getWindow();
					myDialog.setView(view);
					myDialog.show();
					window.setGravity(Gravity.BOTTOM);
					ok.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() == 1) {
								Intent intent = new Intent(GosDeviceListActivity.this,
										GosCheckDeviceWorkWiFiActivity.class);
								intent.putExtra("softssid", softssid);
								startActivity(intent);
							} else {
								Intent intent = new Intent(GosDeviceListActivity.this,
										GosCheckDeviceWorkWiFiActivity.class);
								startActivity(intent);
							}
							myDialog.cancel();
						}
					});
					diss.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							myDialog.cancel();
						}
					});
				}
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_device_list);
		// 设置ActionBar
		setActionBar(true, true, R.string.devicelist_title);
		actionBar.setIcon(R.drawable.button_refresh);

		handler.sendEmptyMessage(GETLIST);
		GosMessageHandler.getSingleInstance().StartLooperWifi(this);
		softNameList = new ArrayList<String>();
		initData();
		initView();
		initEvent();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && isFrist) {
			progressDialog.show();

			isFrist = false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		GosDeviceModuleBaseActivity.deviceslist=GizWifiSDK.sharedInstance().getDeviceList();
		UpdateUI();
		// TODO GosMessageHandler.getSingleInstance().SetHandler(handler);
		if (boundMessage.size() != 0) {
			progressDialog.show();
			if (boundMessage.size() == 2) {
				GizWifiSDK.sharedInstance().bindDevice(uid, token, boundMessage.get(0), boundMessage.get(1), null);
			} else if (boundMessage.size() == 1) {
				GizWifiSDK.sharedInstance().bindDeviceByQRCode(uid, token, boundMessage.get(0));
			} else {
				Log.i("Apptest", "ListSize:" + boundMessage.size());
			}
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		boundMessage.clear();
		 GosMessageHandler.getSingleInstance().SetHandler(null);

	}

	private void initView() {
		svListGroup = (ScrollView) findViewById(R.id.svListGroup);
		llNoDevice = (LinearLayout) findViewById(R.id.llNoDevice);
		imgNoDevice = (ImageView) findViewById(R.id.imgNoDevice);
		btnNoDevice = (Button) findViewById(R.id.btnNoDevice);

		icBoundDevices = findViewById(R.id.icBoundDevices);
		icFoundDevices = findViewById(R.id.icFoundDevices);
		icOfflineDevices = findViewById(R.id.icOfflineDevices);

		slvBoundDevices = (SlideListView2) icBoundDevices.findViewById(R.id.slideListView1);
		slvFoundDevices = (SlideListView2) icFoundDevices.findViewById(R.id.slideListView1);
		slvOfflineDevices = (SlideListView2) icOfflineDevices.findViewById(R.id.slideListView1);

		llNoBoundDevices = (LinearLayout) icBoundDevices.findViewById(R.id.llHaveNotDevice);
		llNoFoundDevices = (LinearLayout) icFoundDevices.findViewById(R.id.llHaveNotDevice);
		llNoOfflineDevices = (LinearLayout) icOfflineDevices.findViewById(R.id.llHaveNotDevice);

		tvBoundDevicesListTitle = (TextView) icBoundDevices.findViewById(R.id.tvListViewTitle);
		tvFoundDevicesListTitle = (TextView) icFoundDevices.findViewById(R.id.tvListViewTitle);
		tvOfflineDevicesListTitle = (TextView) icOfflineDevices.findViewById(R.id.tvListViewTitle);

		String boundDevicesListTitle = (String) getText(R.string.bound_divices);
		tvBoundDevicesListTitle.setText(boundDevicesListTitle);
		String foundDevicesListTitle = (String) getText(R.string.found_devices);
		tvFoundDevicesListTitle.setText(foundDevicesListTitle);
		String offlineDevicesListTitle = (String) getText(R.string.offline_devices);
		tvOfflineDevicesListTitle.setText(offlineDevicesListTitle);

	}

	private void initEvent() {

		imgNoDevice.setOnClickListener(this);
		btnNoDevice.setOnClickListener(this);

		slvFoundDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				progressDialog.show();
				slvFoundDevices.setEnabled(false);
				slvFoundDevices.postDelayed(new Runnable() {
					@Override
					public void run() {
						slvFoundDevices.setEnabled(true);
					}
				}, 3000);
				GizWifiDevice device = foundDevicesList.get(position);
				device.setListener(getGizWifiDeviceListener());
				device.setSubscribe(true);

			}
		});

		slvBoundDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				progressDialog.show();
				slvBoundDevices.setEnabled(false);
				slvBoundDevices.postDelayed(new Runnable() {
					@Override
					public void run() {
						slvBoundDevices.setEnabled(true);
					}
				}, 3000);
				GizWifiDevice device = boundDevicesList.get(position);
				device.setListener(getGizWifiDeviceListener());
				device.setSubscribe(true);
			}
		});

		slvBoundDevices.initSlideMode(SlideListView2.MOD_RIGHT);
		slvOfflineDevices.initSlideMode(SlideListView2.MOD_RIGHT);
	}

	private void initData() {
		boundMessage = new ArrayList<String>();
		ProductKeyList = GosDeploy.setProductKeyList();
		uid = spf.getString("Uid", "");
		token = spf.getString("Token", "");

		if (uid.isEmpty() && token.isEmpty()) {
			loginStatus = 0;
		}
	}

	protected void didDiscovered(GizWifiErrorCode result, java.util.List<GizWifiDevice> deviceList) {
		GosDeviceModuleBaseActivity.deviceslist.clear();
		for (GizWifiDevice gizWifiDevice : deviceList) {
			GosDeviceModuleBaseActivity.deviceslist.add(gizWifiDevice);
		}
		handler.sendEmptyMessage(UPDATALIST);

	}

	protected void didUserLogin(GizWifiErrorCode result, java.lang.String uid, java.lang.String token) {

		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
			loginStatus = 2;
			this.uid = uid;
			this.token = token;
			spf.edit().putString("Uid", this.uid).commit();
			spf.edit().putString("Token", this.token).commit();
			handler.sendEmptyMessage(GETLIST);
			// TODO 绑定推送
			GosPushManager.pushBindService(token);
		} else {
			loginStatus = 0;
			if(GosDeploy.setAnonymousLogin()){
				tryUserLoginAnonymous();
			}
			
		}
	}

	protected void didUnbindDevice(GizWifiErrorCode result, java.lang.String did) {
		progressDialog.cancel();
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			// String unBoundFailed = (String) getText(R.string.unbound_failed);
			Toast.makeText(this, toastError(result), 2000).show();
		}
	}

	@Override
	protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
		// TODO 控制页面跳转
		progressDialog.cancel();
		Message msg = new Message();
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
			msg.what = TOCONTROL;
			msg.obj = device;
		} else {
			if (device.isBind()) {
				msg.what = TOAST;
				// String setSubscribeFail = (String)
				// getText(R.string.setsubscribe_failed);
				msg.obj = toastError(result);// setSubscribeFail + "\n" + arg0;
			}
		}
		handler.sendMessage(msg);
	}

	/**
	 * 推送绑定回调
	 * 
	 * @param result
	 */
	@Override
	protected void didChannelIDBind(GizWifiErrorCode result) {
		Log.i("Apptest", result.toString());
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			Toast.makeText(this, toastError(result), 2000).show();
		}
	}

	/**
	 * 设备绑定回调(旧)
	 * 
	 * @param error
	 * @param errorMessage
	 * @param did
	 */
	protected void didBindDevice(int error, String errorMessage, String did) {
		progressDialog.cancel();
		if (error != 0) {
			Toast.makeText(this, R.string.bound_failed + "\n" + errorMessage, 2000).show();
		} else {

			Toast.makeText(this, R.string.bound_successful, 2000).show();
		}

	};

	/**
	 * 设备绑定回调
	 * 
	 * @param result
	 * @param did
	 */
	protected void didBindDevice(GizWifiErrorCode result, java.lang.String did) {
		progressDialog.cancel();
		if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
			Toast.makeText(this, toastError(result), 2000).show();
		} else {

			Toast.makeText(this, R.string.add_successful, 2000).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
			getMenuInflater().inflate(R.menu.devicelist_logout, menu);
		} else {
			if (getIntent().getBooleanExtra("ThredLogin", false)) {
				getMenuInflater().inflate(R.menu.devicelist_logout, menu);
			} else {
				getMenuInflater().inflate(R.menu.devicelist_login, menu);
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			if (checkNetwork(GosDeviceListActivity.this)) {
				progressDialog.show();
				handler.sendEmptyMessage(GETLIST);
			}
			break;
		case R.id.action_QR_code:
			intent = new Intent(GosDeviceListActivity.this, CaptureActivity.class);
			startActivity(intent);
			break;
		case R.id.action_change_user:

			if (item.getTitle() == getText(R.string.login)) {
				logoutToClean();
				break;
			}
			final Dialog dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
			dialog.show();

			Window window = dialog.getWindow();
			window.setContentView(R.layout.alert_gos_logout);

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
					logoutToClean();
				}
			});

			break;
		case R.id.action_addDevice:
			if (!checkNetwork(GosDeviceListActivity.this)) {
				Toast.makeText(GosDeviceListActivity.this, R.string.network_error, 2000).show();
			} else {
				intent = new Intent(GosDeviceListActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.action_site:
			intent = new Intent(GosDeviceListActivity.this, GosSettiingsActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void UpdateUI() {
		if (GosDeviceModuleBaseActivity.deviceslist.isEmpty()) {
			svListGroup.setVisibility(View.GONE);
			llNoDevice.setVisibility(View.VISIBLE);
			return;
		} else {
			llNoDevice.setVisibility(View.GONE);
			svListGroup.setVisibility(View.VISIBLE);
		}

		boundDevicesList = new ArrayList<GizWifiDevice>();
		foundDevicesList = new ArrayList<GizWifiDevice>();
		offlineDevicesList = new ArrayList<GizWifiDevice>();

		for (GizWifiDevice gizWifiDevice : GosDeviceModuleBaseActivity.deviceslist) {
			if (GizWifiDeviceNetStatus.GizDeviceOnline == gizWifiDevice.getNetStatus()
					|| GizWifiDeviceNetStatus.GizDeviceControlled == gizWifiDevice.getNetStatus()) {
				if (gizWifiDevice.isBind()) {
					boundDevicesList.add(gizWifiDevice);
				} else {
					foundDevicesList.add(gizWifiDevice);
				}
			} else {
				offlineDevicesList.add(gizWifiDevice);
			}
		}

		if (boundDevicesList.isEmpty()) {
			slvBoundDevices.setVisibility(View.GONE);
			llNoBoundDevices.setVisibility(View.VISIBLE);
		} else {
			myadapter = new GosDeviceListAdapter(this, boundDevicesList);
			myadapter.setHandler(handler);
			slvBoundDevices.setAdapter(myadapter);
			llNoBoundDevices.setVisibility(View.GONE);
			slvBoundDevices.setVisibility(View.VISIBLE);
		}

		if (foundDevicesList.isEmpty()) {
			slvFoundDevices.setVisibility(View.GONE);
			llNoFoundDevices.setVisibility(View.VISIBLE);
		} else {
			myadapter = new GosDeviceListAdapter(this, foundDevicesList);
			slvFoundDevices.setAdapter(myadapter);
			llNoFoundDevices.setVisibility(View.GONE);
			slvFoundDevices.setVisibility(View.VISIBLE);
		}

		if (offlineDevicesList.isEmpty()) {
			slvOfflineDevices.setVisibility(View.GONE);
			llNoOfflineDevices.setVisibility(View.VISIBLE);
		} else {
			myadapter = new GosDeviceListAdapter(this, offlineDevicesList);
			myadapter.setHandler(handler);
			slvOfflineDevices.setAdapter(myadapter);
			llNoOfflineDevices.setVisibility(View.GONE);
			slvOfflineDevices.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgNoDevice:
		case R.id.btnNoDevice:
			if (!checkNetwork(GosDeviceListActivity.this)) {
				Toast.makeText(GosDeviceListActivity.this, R.string.network_error, 2000).show();
				return;
			}
			intent = new Intent(GosDeviceListActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void tryUserLoginAnonymous() {
		threeSeconds = 3;
		final Timer tsTimer = new Timer();
		tsTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				threeSeconds--;
				if (threeSeconds <= 0) {
					tsTimer.cancel();
					handler.sendEmptyMessage(GETLIST);
				} else {
					if (loginStatus == 4) {
						tsTimer.cancel();
					}
				}
			}
		}, 1000, 1000);
	}

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
			isExit = true; // 准备退出；
			String doubleClick;
			if (!TextUtils.isEmpty(spf.getString("UserName", ""))
					&& !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
				doubleClick = (String) getText(R.string.doubleclick_logout);
			} else {
				if (getIntent().getBooleanExtra("ThredLogin", false)) {
					doubleClick = (String) getText(R.string.doubleclick_logout);
				} else {
					doubleClick = (String) getText(R.string.doubleclick_back);
				}
			}

			Toast.makeText(this, doubleClick, 2000).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			logoutToClean();
		}
	}

	/** 注销函数 */
	private void logoutToClean() {

		spf.edit().putString("UserName", "").commit();
		spf.edit().putString("PassWord", "").commit();
		spf.edit().putString("Uid", "").commit();
		spf.edit().putString("Token", "").commit();
		GosPushManager.pushUnBindService(token);
		finish();
		if (loginStatus == 1) {
			loginStatus = 0;
		} else {
			loginStatus = 4;
		}

	}

}
