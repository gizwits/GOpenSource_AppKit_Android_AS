package com.gizwits.opensource.appkit.ConfigModule;

import java.util.ArrayList;
import java.util.List;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.view.GifView;

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

public class GosAirlinkReadyActivity extends GosConfigModuleBaseActivity implements OnClickListener {

	/** The cb Select */
	CheckBox cbSelect;

	/** The tv Select */
	TextView tvSelect;

	/** The btn Next */
	Button btnNext;

	private TextView moudlechoose;

	private List<String> modeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_gos_airlink_ready);
		// 设置ActionBar
		setActionBar(true, true, R.string.airlink_ready_title);

		initData();
		initView();
		initEvent();
	}

	private void initData() {
		// workSSID = spf.getString("workSSID", "");

		modeList = new ArrayList<String>();
		String[] modes = this.getResources().getStringArray(R.array.mode);
		for (String string : modes) {
			modeList.add(string);
		}
	}

	private void initView() {
		cbSelect = (CheckBox) findViewById(R.id.cbSelect);
		tvSelect = (TextView) findViewById(R.id.tvSelect);
		btnNext = (Button) findViewById(R.id.btnNext);

		moudlechoose = (TextView) findViewById(R.id.moudlechoose);

		String mdchoose = getResources().getString(R.string.moudlechoose);

		String[] split = mdchoose.split("xx");

		int setModuleSelectOn = GosDeploy.setModuleSelectOn();

		if (setModuleSelectOn == 0) {
			moudlechoose.setVisibility(View.VISIBLE);
		} else {
			moudlechoose.setVisibility(View.INVISIBLE);
		}

		if (split.length > 1) {
			moudlechoose.setText(
					split[0] + " " + modeList.get(GosAirlinkChooseDeviceWorkWiFiActivity.modeNum) + " " + split[1]);
		} else {
			moudlechoose.setText(
					split[0] + " " + modeList.get(GosAirlinkChooseDeviceWorkWiFiActivity.modeNum) + " " );
		}

		/** 加载Gif */
		GifView gif = (GifView) findViewById(R.id.softreset);
		gif.setMovieResource(R.drawable.airlink);

		// 配置文件部署
		btnNext.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
		btnNext.setTextColor(GosDeploy.setButtonTextColor());

	}

	private void initEvent() {
		btnNext.setOnClickListener(this);
		tvSelect.setOnClickListener(this);
		btnNext.setClickable(false);
		btnNext.setBackgroundResource(R.drawable.btn_next_shape_gray);

		cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					btnNext.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
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
		case R.id.btnNext:
			Intent intent = new Intent(this, GosAirlinkConfigCountdownActivity.class);
			startActivity(intent);
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
