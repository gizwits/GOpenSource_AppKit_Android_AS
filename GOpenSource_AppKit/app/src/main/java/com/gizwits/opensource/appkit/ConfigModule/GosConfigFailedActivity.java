package com.gizwits.opensource.appkit.ConfigModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.DeviceModule.GosMainActivity;
import com.gizwits.opensource.appkit.utils.ToolUtils;

public class GosConfigFailedActivity extends GosConfigModuleBaseActivity implements OnClickListener {

    /**
     * The btn Again
     */
    Button btnAgain;

    /**
     * The soft SSID
     */
    String softSSID;

    /**
     * The data
     */
    String promptText, cancelBesureText, beSureText, cancelText;
    private boolean isAirLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_config_failed);
        // 设置ActionBar
        setToolBar(false, R.string.join_failed_title);
        TextView tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setVisibility(View.VISIBLE);
        SpannableString ssTitle = new SpannableString(this.getString(R.string.cancel));
        ssTitle.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvLeft.setText(ssTitle);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GosConfigFailedActivity.this, GosMainActivity.class);
                quitAlert(GosConfigFailedActivity.this, intent);
            }
        });
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        btnAgain = (Button) findViewById(R.id.btnAgain);

        // 配置文件部署
        btnAgain.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        btnAgain.setTextColor(GosDeploy.appConfig_Contrast());
    }

    private void initEvent() {
        btnAgain.setOnClickListener(this);
    }

    private void initData() {
        /**   判断是否是从一键配置界面传过去的  */
        isAirLink = getIntent().getBooleanExtra("isAirLink", false);
        promptText = (String) getText(R.string.prompt);
        cancelBesureText = (String) getText(R.string.cancel_besure);
        beSureText = (String) getText(R.string.besure);
        cancelText = (String) getText(R.string.cancel);
    }

    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GosConfigFailedActivity.this, GosMainActivity.class);
            quitAlert(this, intent);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAgain:
                if (ToolUtils.noDoubleClick()) {
                    if (isAirLink) {
                        //config_airlink-false-start
                        Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
                        startActivity(intent);
                        //config_airlink-false-end
                    } else {
                        //  config_softap-false-start
                        Intent intent = new Intent(this, GosChooseDeviceWorkWiFiActivity.class);
                        startActivity(intent);
                        //  config_softap-false-end
                    }

                }

                break;

            default:
                break;
        }
    }
}
