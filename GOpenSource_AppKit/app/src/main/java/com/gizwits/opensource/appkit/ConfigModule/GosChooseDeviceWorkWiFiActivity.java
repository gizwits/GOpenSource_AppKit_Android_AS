package com.gizwits.opensource.appkit.ConfigModule;

import java.util.ArrayList;
import java.util.List;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.utils.NetUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GosChooseDeviceWorkWiFiActivity extends GosConfigModuleBaseActivity
		implements OnClickListener, OnItemClickListener {

	private AlertDialog create;
	private ArrayList<ScanResult> wifiList;

	/** wifi信息 */
	public WifiInfo wifiInfo;

	/** The et SSID */
	private EditText etSSID;

	/** The et Psw */
	private EditText etPsw;

	/** The ll Next */
	private LinearLayout llNext;

	/** The cb Laws */
	private CheckBox cbLaws;

	/** The img WiFiList */
	private ImageView imgWiFiList;

	/** 配置用参数 */
	private String softSSID, workSSID, workSSIDPsw;


	private static final  int REQUEST_CODE_SETTING = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_choose_device_workwifi);
		// 设置ActionBar
		setActionBar(true, true, R.string.configuration_title);

		initData();
		initView();
		ininEvent();
	}

	private void initView() {
		etSSID = (EditText) findViewById(R.id.etSSID);
		etPsw = (EditText) findViewById(R.id.etPsw);
		llNext = (LinearLayout) findViewById(R.id.llNext);
		cbLaws = (CheckBox) findViewById(R.id.cbLaws);
		imgWiFiList = (ImageView) findViewById(R.id.imgWiFiList);

		if (!TextUtils.isEmpty(workSSID)) {
			etSSID.setText(workSSID);
			if (checkworkSSIDUsed(workSSID)) {
				if (!TextUtils.isEmpty(spf.getString("workSSIDPsw", ""))) {
					etPsw.setText(spf.getString("workSSIDPsw", ""));
				}
			}
		}
	}

	private void ininEvent() {
		llNext.setOnClickListener(this);
		imgWiFiList.setOnClickListener(this);

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

	private void initData() {
		softSSID = getIntent().getStringExtra("softssid");
		workSSID = spf.getString("workSSID", "");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llNext:
			workSSID = etSSID.getText().toString();
			workSSIDPsw = etPsw.getText().toString();
			
			if (TextUtils.isEmpty(softSSID)) {
				Toast.makeText(GosChooseDeviceWorkWiFiActivity.this, R.string.softssid_error, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (TextUtils.isEmpty(workSSID)) {
				Toast.makeText(GosChooseDeviceWorkWiFiActivity.this, R.string.choose_wifi_list_title,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(workSSIDPsw)) {
				new AlertDialog.Builder(this).setTitle(R.string.prompt).setMessage(R.string.workwifi_isnull)
						.setNegativeButton(R.string.cancel, null).setPositiveButton(R.string.besure, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
										GosConfigCountdownActivity.class);
								spf.edit().putString("workSSID", workSSID).commit();
								spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();
								intent.putExtra("softSSID", softSSID);
								startActivity(intent);
								finish();
							}
						}).show();
			} else {
				Intent intent = new Intent(GosChooseDeviceWorkWiFiActivity.this, GosConfigCountdownActivity.class);
				spf.edit().putString("workSSID", workSSID).commit();
				spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();
				intent.putExtra("softSSID", softSSID);
				startActivity(intent);
				finish();
			}

			break;

		case R.id.imgWiFiList:


			AndPermission.with(this)
					.requestCode(REQUEST_CODE_SETTING)
					.permission(Manifest.permission.ACCESS_FINE_LOCATION).rationale(new RationaleListener() {

				@Override
				public void showRequestPermissionRationale(int arg0, Rationale arg1) {
					AndPermission.rationaleDialog(GosChooseDeviceWorkWiFiActivity.this, arg1).show();
				}
			})
					.send();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		ScanResult sResult = wifiList.get(arg2);
		String sSID = sResult.SSID;
		etSSID.setText(sSID);
		etPsw.setText("");
		create.dismiss();
	}

	// 检查当前使用的WiFi是否曾经用过
	protected boolean checkworkSSIDUsed(String workSSID) {
		if (spf.contains("workSSID")) {
			if (spf.getString("workSSID", "").equals(workSSID)) {
				return true;
			}
		}
		return false;
	}

	class WifiListAdapter extends BaseAdapter {

		ArrayList<ScanResult> xpgList;

		public WifiListAdapter(ArrayList<ScanResult> list) {
			this.xpgList = list;
		}

		@Override
		public int getCount() {
			return xpgList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Holder holder;
			if (view == null) {
				view = LayoutInflater.from(GosChooseDeviceWorkWiFiActivity.this).inflate(R.layout.item_gos_wifi_list,
						null);
				holder = new Holder(view);
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}
			String ssid = xpgList.get(position).SSID;
			holder.getTextView().setText(ssid);

			return view;
		}

	}

	class Holder {
		View view;

		public Holder(View view) {
			this.view = view;
		}

		TextView textView;

		public TextView getTextView() {
			if (textView == null) {
				textView = (TextView) view.findViewById(R.id.SSID_text);
			}
			return textView;
		}
	}





	@Override
	public void onSucceed(int requestCode, List<String> grantPermissions) {
		super.onSucceed(requestCode, grantPermissions);
		AlertDialog.Builder dia = new AlertDialog.Builder(GosChooseDeviceWorkWiFiActivity.this);
		View view = View.inflate(GosChooseDeviceWorkWiFiActivity.this, R.layout.alert_gos_wifi_list, null);
		ListView listview = (ListView) view.findViewById(R.id.wifi_list);
		List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
		List<String> localList = new ArrayList<String>();
		localList.clear();
		wifiList = new ArrayList<ScanResult>();
		wifiList.clear();
		for (ScanResult sss : rsList) {

			if (sss.SSID.contains(GosConstant.SoftAP_Start)) {
			} else {
				if (localList.toString().contains(sss.SSID)) {
				} else {
					localList.add(sss.SSID);
					wifiList.add(sss);
				}
			}
		}
		WifiListAdapter adapter = new WifiListAdapter(wifiList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		dia.setView(view);
		create = dia.create();
		create.show();

	}

	@Override
	public void onFailed(int requestCode, List<String> deniedPermissions) {
		super.onFailed(requestCode, deniedPermissions);
		{
			// 权限申请失败回调。

			// 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
			if (AndPermission.hasAlwaysDeniedPermission(GosChooseDeviceWorkWiFiActivity.this, deniedPermissions)) {
				// 第一种：用默认的提示语。
				AndPermission.defaultSettingDialog(GosChooseDeviceWorkWiFiActivity.this, REQUEST_CODE_SETTING).show();

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
