package com.gizwits.opensource.appkit.ConfigModule;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.ConfigModule.GosModeListActivity.ModeListAdapter;
import com.gizwits.opensource.appkit.utils.NetUtils;
import com.gizwits.opensource.appkit.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("InflateParams")
public class GosAirlinkChooseDeviceWorkWiFiActivity extends
		GosConfigModuleBaseActivity implements OnClickListener {

	private AlertDialog create;
	private ArrayList<ScanResult> wifiList;

	/** wifi信息 */
	public WifiInfo wifiInfo;

	/** The et SSID */
	private EditText etSSID;

	/** The et Psw */
	private EditText etPsw;

	/** The btn Next */
	private Button btnNext;

	/** The ll ChooseMode */
	private LinearLayout llChooseMode;

	/** The cb Laws */
	private CheckBox cbLaws;

	/** The tv Mode */
	private TextView tvMode;

	/** The img WiFiList */
	private ImageView imgWiFiList;

	/** 配置用参数 */
	private String workSSID, workSSIDPsw;

	/** The data */
	List<String> modeList;

	/** The Adapter */
	ModeListAdapter modeListAdapter;

	/** The modeNum */
	static int modeNum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_airlink_choose_device_workwifi);
		// 设置ActionBar
		String title = getString(R.string.add_device)
				+ GosDeploy.setAddDeviceTitle();
		setActionBar(true, true, title);

		initData();
		initView();
		ininEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			tvMode.setText(modeList.get(modeNum));
			// 预设workSSID && workSSIDPsw
			workSSID = NetUtils.getCurentWifiSSID(this);
			String mypass = spf.getString("mypass", "");

			if (!TextUtils.isEmpty(workSSID)) {
				etSSID.setText(workSSID);
				if(!TextUtils.isEmpty(mypass)){
					JSONObject obj = new JSONObject(mypass);

					if (obj.has(workSSID)) {
						String pass = obj.getString(workSSID);
						etPsw.setText(pass);
					}else{
						etPsw.setText("");
					}
				}
				
			} else {
				etSSID.setText(NetUtils.getCurentWifiSSID(this));
			}

			// 当没有任何文字的时候设置为明文显示
			if (TextUtils.isEmpty(etPsw.getText().toString())) {
				cbLaws.setChecked(true);
				etPsw.setInputType(0x90);
			} else {
				etPsw.setInputType(0x81);
				cbLaws.setChecked(false);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		tvMode = (TextView) findViewById(R.id.tvMode);
		etSSID = (EditText) findViewById(R.id.etSSID);
		etPsw = (EditText) findViewById(R.id.etPsw);
		cbLaws = (CheckBox) findViewById(R.id.cbLaws);
		btnNext = (Button) findViewById(R.id.btnNext);
		llChooseMode = (LinearLayout) findViewById(R.id.llChooseMode);
		imgWiFiList = (ImageView) findViewById(R.id.imgWiFiList);

		// 配置文件部署
		btnNext.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
		btnNext.setTextColor(GosDeploy.setButtonTextColor());
		llChooseMode.setVisibility(GosDeploy.setModuleSelectOn());
	}

	private void ininEvent() {
		btnNext.setOnClickListener(this);
		imgWiFiList.setOnClickListener(this);
		llChooseMode.setOnClickListener(this);

		cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
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
		// workSSID = spf.getString("workSSID", "");

		modeList = new ArrayList<String>();
		String[] modes = this.getResources().getStringArray(R.array.mode);
		for (String string : modes) {
			modeList.add(string);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			workSSID = etSSID.getText().toString();
			workSSIDPsw = etPsw.getText().toString();

			if (TextUtils.isEmpty(workSSID)) {
				Toast.makeText(GosAirlinkChooseDeviceWorkWiFiActivity.this,
						R.string.choose_wifi_list_title, toastTime).show();
				return;
			}
			if (TextUtils.isEmpty(workSSIDPsw)) {
				final Dialog dialog = new AlertDialog.Builder(this).setView(
						new EditText(this)).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();

				Window window = dialog.getWindow();
				window.setContentView(R.layout.alert_gos_empty);

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
						toAirlinkReady();
					}
				});
			} else {
				toAirlinkReady();
			}

			break;

		case R.id.imgWiFiList:
			AlertDialog.Builder dia = new AlertDialog.Builder(
					GosAirlinkChooseDeviceWorkWiFiActivity.this);
			View view = View.inflate(
					GosAirlinkChooseDeviceWorkWiFiActivity.this,
					R.layout.alert_gos_wifi_list, null);
			ListView listview = (ListView) view.findViewById(R.id.wifi_list);
			List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
			List<String> localList = new ArrayList<String>();
			localList.clear();
			wifiList = new ArrayList<ScanResult>();
			wifiList.clear();
			for (ScanResult sss : rsList) {

				if (sss.SSID.contains(SoftAP_Start)) {
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
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ScanResult sResult = wifiList.get(arg2);
					String sSID = sResult.SSID;
					etSSID.setText(sSID);
					etPsw.setText("");
					create.dismiss();
				}
			});
			dia.setView(view);
			create = dia.create();
			create.show();

			break;

		case R.id.llChooseMode:
			Intent intent = new Intent(this, GosModeListActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void toAirlinkReady() {
		// 需要记录所有配置过的wifi和密码

		try {
			String mypass = spf.getString("mypass", "");
			if (TextUtils.isEmpty(mypass)) {
				JSONObject mUserAndPass = new JSONObject();

				mUserAndPass.put(workSSID, workSSIDPsw);
				spf.edit().putString("mypass", mUserAndPass.toString())
						.commit();
			} else {
				JSONObject obj = new JSONObject(mypass);

				obj.put(workSSID, workSSIDPsw);

				spf.edit().putString("mypass", obj.toString()).commit();

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		spf.edit().putString("workSSID", workSSID).commit();
		spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();

		Intent intent = new Intent(this, GosAirlinkReadyActivity.class);
		startActivity(intent);
		finish();
	}

	/*
	 * // 检查当前使用的WiFi是否曾经用过 protected boolean checkworkSSIDUsed(String workSSID)
	 * { if (spf.contains("workSSID")) { if (spf.getString("workSSID",
	 * "").equals(workSSID)) { return true; } } return false; }
	 */

	// 屏蔽掉返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			quitAlert(this);
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			quitAlert(this);
			break;
		}
		return true;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Holder holder;
			if (view == null) {
				view = LayoutInflater.from(
						GosAirlinkChooseDeviceWorkWiFiActivity.this).inflate(
						R.layout.item_gos_wifi_list, null);
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
}
