package com.gizwits.opensource.appkit.ConfigModule;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.utils.NetUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class GosDeviceReadyActivity extends GosConfigModuleBaseActivity implements OnClickListener {

	/** The cb Select */
	CheckBox cbSelect;

	/** The tv Select */
	TextView tvSelect;

	/** The btn Next */
	Button btnNext;

	/** The tv NoRedLight */
	TextView tvNoRedLight;

	/** The flag */
	boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_device_ready);
		// 设置ActionBar
		setActionBar(true, true, R.string.join_by_hands);

		initView();
		initEvent();
	}

	private void initView() {
		cbSelect = (CheckBox) findViewById(R.id.cbSelect);
		btnNext = (Button) findViewById(R.id.btnNext);
		tvNoRedLight = (TextView) findViewById(R.id.tvNoRedLight);
		tvSelect = (TextView) findViewById(R.id.tvSelect);

		// 配置文件部署
		btnNext.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
		btnNext.setTextColor(GosDeploy.setButtonTextColor());

	}

	private void initEvent() {
		tvNoRedLight.setOnClickListener(this);
		tvSelect.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnNext.setClickable(false);
		btnNext.setBackgroundResource(R.drawable.btn_next_shape_gray);

		cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					btnNext.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());;
					btnNext.setClickable(true);
				} else {
					btnNext.setBackgroundResource(R.drawable.btn_next_shape_gray);
					btnNext.setClickable(false);
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvNoRedLight:
			Intent intent = new Intent(GosDeviceReadyActivity.this, GosDeviceResetActivity.class);
			intent.putExtra("flag", "");
			startActivity(intent);
			finish();
			break;

		case R.id.btnNext:
			Intent intent2 = new Intent(GosDeviceReadyActivity.this, GosChooseDeviceActivity.class);
			startActivity(intent2);
			finish();
			break;
		case R.id.tvSelect:
			if (cbSelect.isChecked()) {
				cbSelect.setChecked(false);
			} else {
				cbSelect.setChecked(true);
			}
			break;

		default:
			break;
		}
	}

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
}
