package com.gizwits.opensource.appkit.DeviceModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.CommonModule.NoScrollViewPager;
import com.gizwits.opensource.appkit.CommonModule.TipsDialog;
import com.gizwits.opensource.appkit.ConfigModule.GosAirlinkChooseDeviceWorkWiFiActivity;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.SettingsModule.GosSettiingsActivity;
import com.gizwits.opensource.appkit.sharingdevice.messageCenterActivity;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import android.Manifest;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import zxing.CaptureActivity;

public class GosMainActivity extends GosDeviceModuleBaseActivity {

	Context context = null;
	@SuppressWarnings("deprecation")
	LocalActivityManager manager = null;
	NoScrollViewPager pager = null;
	TabHost tabHost = null;
	LinearLayout t1, t2, t3;

	String softssid, uid, token;

	private int offset = 0;// 动画偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private ImageView cursor;// 动画图片

	/** 获取设备列表 */
	protected static final int GETLIST = 0;

	/** 刷新设备列表 */
	protected static final int UPDATALIST = 1;
	private Handler myHandler;
	private GosDeviceListActivity activity;

	private int viewPagerSelected = 0;
	private Intent intent;
	private messageCenterActivity center;

	public static Activity instance = null;
	private ImageView img1;
	private ImageView img2;
	private ImageView img3;
	private TextView tx1;
	private TextView tx2;
	private TextView tx3;
	private static final  int REQUEST_CODE_SETTING = 100;

	private static final  int REQUEST_ZXINGCODE_SETTING = 200;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_main);

		context = GosMainActivity.this;
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		instance = this;
		InitImageView();
		initTextView();
		initPagerViewer();
		initHandler();


		AndPermission.with(this)
				.requestCode(REQUEST_CODE_SETTING)
				.permission(Manifest.permission.ACCESS_FINE_LOCATION).rationale(new RationaleListener() {

			@Override
			public void showRequestPermissionRationale(int arg0, Rationale arg1) {
				AndPermission.rationaleDialog(GosMainActivity.this, arg1).show();
			}
		})
				.send();
	

	}

	@SuppressWarnings("deprecation")
	private void initHandler() {
		activity = (GosDeviceListActivity) manager.getActivity("A");
		center = (messageCenterActivity) manager.getActivity("B");
		myHandler = activity.getMyHandler();
	}

	@Override
	protected void onResume() {
		
		// TODO Auto-generated method stub
		super.onResume();
		switch (viewPagerSelected) {
		case 0:
			activity.onResume();
			break;

		case 1:
			center.onResume();
			break;

		default:
			break;
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		activity.onPause();
	}

	/**
	 * 初始化标题
	 */
	private void initTextView() {
		t1 = (LinearLayout) findViewById(R.id.text1);
		t2 = (LinearLayout) findViewById(R.id.text2);
		t3 = (LinearLayout) findViewById(R.id.text3);

		img1 = (ImageView) findViewById(R.id.img1);
		img2 = (ImageView) findViewById(R.id.img2);
		img3 = (ImageView) findViewById(R.id.img3);

		tx1 = (TextView) findViewById(R.id.tx1);
		tx2 = (TextView) findViewById(R.id.tx2);
		tx3 = (TextView) findViewById(R.id.tx3);

		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		t3.setOnClickListener(new MyOnClickListener(2));

	}

	/**
	 * 初始化PageViewer
	 */
	private void initPagerViewer() {
		pager = (NoScrollViewPager) findViewById(R.id.viewpage);

		pager.setNoScroll(true);
		final ArrayList<View> list = new ArrayList<View>();

		Intent intent = new Intent(context, GosDeviceListActivity.class);
		list.add(getView("A", intent));
		Intent intent2 = new Intent(context, messageCenterActivity.class);
		list.add(getView("B", intent2));
		Intent intent3 = new Intent(context, GosSettiingsActivity.class);
		list.add(getView("C", intent3));

		pager.setAdapter(new MyPagerAdapter(list));
		pager.setCurrentItem(0);
		// t1.setBackgroundColor(getResources().getColor(R.color.gray));
		// t2.setBackgroundColor(getResources().getColor(R.color.white));
		// t3.setBackgroundColor(getResources().getColor(R.color.white));

		img1.setBackgroundResource(R.drawable.grid);
		img2.setBackgroundResource(R.drawable.message_grey);
		img3.setBackgroundResource(R.drawable.user_grey);

		tx1.setTextColor(getResources().getColor(R.color.black));
		tx2.setTextColor(getResources().getColor(R.color.gray));
		tx3.setTextColor(getResources().getColor(R.color.gray));
		setActionBar(false, false, R.string.devicelist_title);
		
		
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化动画
	 */
	@SuppressWarnings("deprecation")
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller).getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置

		LinearLayout myll = (LinearLayout) findViewById(R.id.linearLayout1);
		
		myll.setVisibility(GosDeploy.setUsingTabSetOn());
		
		myll.setBackgroundDrawable(GosDeploy.setNavigationBarColor());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		switch (viewPagerSelected) {
		case 0:
			getMenuInflater().inflate(R.menu.devicelist_logout, menu);
			break;

		case 1:

			break;

		case 2:

			break;

		default:
			break;
		}

		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// activity.logoutToClean();

			// finish();
			break;
		case R.id.action_QR_code:


			AndPermission.with(this)
					.requestCode(REQUEST_ZXINGCODE_SETTING)
					.permission(Manifest.permission.CAMERA).rationale(new RationaleListener() {

				@Override
				public void showRequestPermissionRationale(int arg0, Rationale arg1) {
					AndPermission.rationaleDialog(GosMainActivity.this, arg1).show();
				}
			})
					.send();

			break;
		case R.id.action_addDevice:
			if (!checkNetwork(GosMainActivity.this)) {
				Toast.makeText(GosMainActivity.this, R.string.network_error, 2000).show();
			} else {
				intent = new Intent(GosMainActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
				startActivity(intent);
			}
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 通过activity获取视图
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/**
	 * Pager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {

			viewPagerSelected = arg0;

			refreshMenu();
			Animation animation = null;
			switch (arg0) {
			case 0:

				if (currIndex == 1) {

					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}

				setActionBar(false, false, R.string.devicelist_title);
				activity.onResume();
				break;
			case 1:

				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				setActionBar(false, false, R.string.messagecenter);
				activity.onPause();
				center.onResume();
				break;

			case 2:
				activity.onPause();
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				setActionBar(false, false, R.string.personal_center);
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True 图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 图标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;

		}

		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);

			switch (index) {
			case 0:
				// t1.setBackgroundColor(getResources().getColor(R.color.gray));
				// t2.setBackgroundColor(getResources().getColor(R.color.white));
				// t3.setBackgroundColor(getResources().getColor(R.color.white));

				img1.setBackgroundResource(R.drawable.grid);
				img2.setBackgroundResource(R.drawable.message_grey);
				img3.setBackgroundResource(R.drawable.user_grey);

				tx1.setTextColor(getResources().getColor(R.color.black));
				tx2.setTextColor(getResources().getColor(R.color.gray));
				tx3.setTextColor(getResources().getColor(R.color.gray));
				break;

			case 1:
				// t1.setBackgroundColor(getResources().getColor(R.color.white));
				// t2.setBackgroundColor(getResources().getColor(R.color.gray));
				// t3.setBackgroundColor(getResources().getColor(R.color.white));

				img1.setBackgroundResource(R.drawable.grid_grey);
				img2.setBackgroundResource(R.drawable.message);
				img3.setBackgroundResource(R.drawable.user_grey);

				tx1.setTextColor(getResources().getColor(R.color.gray));
				tx2.setTextColor(getResources().getColor(R.color.black));
				tx3.setTextColor(getResources().getColor(R.color.gray));
				break;

			case 2:
				// t1.setBackgroundColor(getResources().getColor(R.color.white));
				// t2.setBackgroundColor(getResources().getColor(R.color.white));
				// t3.setBackgroundColor(getResources().getColor(R.color.gray));

				img1.setBackgroundResource(R.drawable.grid_grey);
				img2.setBackgroundResource(R.drawable.message_grey);
				img3.setBackgroundResource(R.drawable.user);

				tx1.setTextColor(getResources().getColor(R.color.gray));
				tx2.setTextColor(getResources().getColor(R.color.gray));
				tx3.setTextColor(getResources().getColor(R.color.black));
				break;

			default:
				break;
			}
		}
	};

	// 刷新menu的方法
	private void refreshMenu() {
		// 核心是Activity这个方法
		supportInvalidateOptionsMenu();
	}

	// @SuppressWarnings("deprecation")
	// protected void didDiscovered(GizWifiErrorCode result,
	// java.util.List<GizWifiDevice> deviceList) {
	// GosDeviceModuleBaseActivity.deviceslist.clear();
	// for (GizWifiDevice gizWifiDevice : deviceList) {
	// GosDeviceModuleBaseActivity.deviceslist.add(gizWifiDevice);
	// }
	// // handler.sendEmptyMessage(UPDATALIST);
	//
	// myHandler.sendEmptyMessage(UPDATALIST);
	//
	// }
	//
	// protected void didUserLogin(GizWifiErrorCode result, java.lang.String
	// uid, java.lang.String token) {
	//
	// if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
	// GosDeviceListActivity.loginStatus = 2;
	// this.uid = uid;
	// this.token = token;
	// spf.edit().putString("Uid", this.uid).commit();
	// spf.edit().putString("Token", this.token).commit();
	// myHandler.sendEmptyMessage(GETLIST);
	// // TODO 绑定推送
	// GosPushManager.pushBindService(token);
	// } else {
	// GosDeviceListActivity.loginStatus = 0;
	// if (GosDeploy.setAnonymousLogin()) {
	// //tryUserLoginAnonymous();
	// }
	//
	// }
	// }
	//
	//
	// protected void didUnbindDevice(GizWifiErrorCode result, java.lang.String
	// did) {
	// progressDialog.cancel();
	// if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
	// // String unBoundFailed = (String) getText(R.string.unbound_failed);
	// Toast.makeText(this, toastError(result), 2000).show();
	// }
	// }
	//

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click();
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 666) {
			finish();
		} else if (resultCode == 98765) {
			TipsDialog dialog = new TipsDialog(GosMainActivity.this,
					getResources().getString(R.string.devicedisconnected));

			dialog.show();
		}

	}

	public void finishMe() {
		finish();
	}

	public void exitBy2Click() {
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
	void logoutToClean() {
		spf.edit().putString("UserName", "").commit();
		spf.edit().putString("PassWord", "").commit();
		spf.edit().putString("Uid", "").commit();
		spf.edit().putString("thirdUid", "").commit();

		GosPushManager.pushUnBindService(spf.getString("Token", ""));
		spf.edit().putString("Token", "").commit();

		finish();
		if (GosDeviceListActivity.loginStatus == 1) {
			GosDeviceListActivity.loginStatus = 0;
		} else {
			GosDeviceListActivity.loginStatus = 4;
		}

	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;



	@Override
	public void onSucceed(int requestCode, List<String> grantPermissions) {
		super.onSucceed(requestCode, grantPermissions);


	switch (requestCode){



		case REQUEST_CODE_SETTING:
			GosMessageHandler.getSingleInstance().StartLooperWifi(this);
			break;


		case REQUEST_ZXINGCODE_SETTING:
			Intent	intent = new Intent(GosMainActivity.this, CaptureActivity.class);
			startActivity(intent);
			break;


		default:

			break;
	}


	}

	@Override
	public void onFailed(int requestCode, List<String> deniedPermissions) {
		super.onFailed(requestCode, deniedPermissions);
		{
			// 权限申请失败回调。

			// 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
			if (AndPermission.hasAlwaysDeniedPermission(GosMainActivity.this, deniedPermissions)) {
				// 第一种：用默认的提示语。
				AndPermission.defaultSettingDialog(GosMainActivity.this, REQUEST_CODE_SETTING).show();

				// 第二种：用自定义的提示语。
				// AndPermission.defaultSettingDialog(this, REQUEST_CODE_SETTING)
				// .setTitle("权限申请失败")
				// .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
				// .setPositiveButton("好，去设置")
				// .show();

				// 第三种：自定义dialog样式。
				// SettingService settingService =
				//    AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
				// 你的dialog点击了确定调用：
				// settingService.execute();
				// 你的dialog点击了取消调用：
				// settingService.cancel();
			}

		}
	}
}
