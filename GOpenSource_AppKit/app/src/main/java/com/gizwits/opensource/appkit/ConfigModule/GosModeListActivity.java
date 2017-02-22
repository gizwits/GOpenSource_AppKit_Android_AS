package com.gizwits.opensource.appkit.ConfigModule;

import java.util.ArrayList;
import java.util.List;

import com.gizwits.opensource.appkit.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GosModeListActivity extends GosConfigModuleBaseActivity {

	/** The lv Mode */
	GridView lvMode;

	/** The data */
	List<String> modeList;

	/** The Adapter */
	ModeListAdapter modeListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_modelist);
		// 设置ActionBar
		setActionBar(true, true, R.string.choose_mode_title);
		initData();
		initView();
		initEvent();
	}

	private void initView() {
		lvMode = (GridView) findViewById(R.id.lvMode);

		lvMode.setAdapter(modeListAdapter);// 初始化
	}

	private void initEvent() {

		lvMode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				GosAirlinkChooseDeviceWorkWiFiActivity.modeNum = arg2;
				Intent intent = new Intent(GosModeListActivity.this, GosAirlinkReadyActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	private void initData() {
		modeList = new ArrayList<String>();
		String[] modes = this.getResources().getStringArray(R.array.mode);
		for (String string : modes) {
			modeList.add(string);
		}
		modeListAdapter = new ModeListAdapter(this, modeList);
	}

	class ModeListAdapter extends BaseAdapter {

		Context context;
		List<String> modeList;

		public ModeListAdapter(Context context, List<String> modeList) {
			super();
			this.context = context;
			this.modeList = modeList;
		}

		@Override
		public int getCount() {
			return modeList.size();
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
			if (null == convertView) {
				convertView = View.inflate(context, R.layout.new_item_gos_mode_list, null);
			}

			TextView tvModeText = (TextView) convertView.findViewById(R.id.tvModeText);

			String modeText = modeList.get(position);
			tvModeText.setText(modeText);

			ImageView ivChoosed = (ImageView) convertView.findViewById(R.id.ivChoosed);

			switch (position) {
			case 0:
				ivChoosed.setBackgroundDrawable(getResources().getDrawable(R.drawable.esp));
				break;

			case 1:
				ivChoosed.setBackgroundDrawable(getResources().getDrawable(R.drawable.mxchip));
				break;
			case 2:
				ivChoosed.setBackgroundDrawable(getResources().getDrawable(R.drawable.hf));
				break;
			case 3:
				ivChoosed.setBackgroundDrawable(getResources().getDrawable(R.drawable.rtk));
				break;
			case 4:
				ivChoosed.setBackgroundDrawable(getResources().getDrawable(R.drawable.wm));
				break;
			case 5:
				ivChoosed.setBackgroundDrawable(getResources().getDrawable(R.drawable.qca));
				break;
			case 6:
				ivChoosed.setBackgroundDrawable(getResources().getDrawable(R.drawable.ti));
				break;

			default:
				ivChoosed.setBackgroundDrawable(getResources().getDrawable(R.drawable.mydefaultpic));
				break;
			}
			// int i = GosAirlinkChooseDeviceWorkWiFiActivity.modeNum;

			return convertView;
		}

	}
	
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			this.finish();
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		this.finish();
		return true;
	}

}
