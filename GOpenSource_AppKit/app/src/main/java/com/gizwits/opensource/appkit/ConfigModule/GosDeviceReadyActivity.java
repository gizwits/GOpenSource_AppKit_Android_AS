package com.gizwits.opensource.appkit.ConfigModule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.DeviceModule.GosMainActivity;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.AssetsUtils;
import com.gizwits.opensource.appkit.utils.ToolUtils;

public class GosDeviceReadyActivity extends GosConfigModuleBaseActivity implements OnClickListener {

    /**
     * The tv Ready
     */
    TextView tvReady;

    /**
     * The tv DeviceTip
     */
    TextView tvDeviceTips;

    /**
     * The btn Next
     */
    Button btnNext;

    private int sum = 0;
    /**
     * The flag
     */
    boolean flag = false;

    boolean isAirLink = false;
    private ImageView ivReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_device_ready);
        // 设置ActionBar
        setToolBar(true, R.string.model_confirmation);
        /**   判断是否是从一键配置界面传过去的  */
        isAirLink = getIntent().getBooleanExtra("isAirLink", false);

        initView();
        initEvent();
    }

    private void initView() {
        tvReady = (TextView) findViewById(R.id.tvReady);
        tvDeviceTips = (TextView) findViewById(R.id.tvDeviceTip);
        btnNext = (Button) findViewById(R.id.btnNext);
        ivReady = (ImageView) findViewById(R.id.ivReady);
        SpannableString spannableString = new SpannableString(getString(R.string.common_ready_message));
        if (AssetsUtils.isZh(this)) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF9500")), 9, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF9500")), 28, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvReady.setText(spannableString);

        // 配置文件部署
        btnNext.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        btnNext.setTextColor(GosDeploy.appConfig_Contrast());

    }

    private void initEvent() {
        btnNext.setOnClickListener(this);
        tvDeviceTips.setOnClickListener(this);
        ivReady.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnNext:
                if (ToolUtils.noDoubleClick()) {
                    sum = 0;
                    Intent intent2 = new Intent(GosDeviceReadyActivity.this, GosChooseDeviceActivity.class);
                    startActivity(intent2);
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
            sum = 0;
            if (isAirLink) {
                Intent intent = new Intent(GosDeviceReadyActivity.this, GosMainActivity.class);
                startActivity(intent);
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sum = 0;
                if (isAirLink) {
                    Intent intent = new Intent(GosDeviceReadyActivity.this, GosMainActivity.class);
                    startActivity(intent);
                } else {
                    finish();
                }
                break;
        }
        return true;
    }


}
