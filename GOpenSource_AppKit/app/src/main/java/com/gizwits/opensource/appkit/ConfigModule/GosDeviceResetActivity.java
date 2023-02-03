package com.gizwits.opensource.appkit.ConfigModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;

public class GosDeviceResetActivity extends GosConfigModuleBaseActivity implements OnClickListener {

    /**
     * The cb Select
     */
    CheckBox cbSelect;

    /**
     * The tv Select
     */
    TextView tvSelect;

    /**
     * The btn Next
     */
    Button btnNext;

    /**
     * The flag
     */
    String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_gos_device_reset);
        // 设置ActionBar
        setToolBar(true, R.string.reset_device);

        initView();
        initEvent();
    }

    private void initView() {
        cbSelect = (CheckBox) findViewById(R.id.cbSelect);
        tvSelect = (TextView) findViewById(R.id.tvSelect);
        btnNext = (Button) findViewById(R.id.btnNext);


        /** 加载标志位 */
        flag = getIntent().getStringExtra("flag").toString();

        // 配置文件部署
        btnNext.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        btnNext.setTextColor(GosDeploy.appConfig_Contrast());

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
                    btnNext.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
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
                if (TextUtils.isEmpty(flag)) {
                    Intent intent = new Intent(GosDeviceResetActivity.this, GosChooseDeviceActivity.class);
                    startActivity(intent);
                }
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
                if (TextUtils.isEmpty(flag)) {
                    Intent intent = new Intent(GosDeviceResetActivity.this, GosDeviceReadyActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                this.finish();

                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (TextUtils.isEmpty(flag)) {
                Intent intent = new Intent(GosDeviceResetActivity.this, GosDeviceReadyActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
            this.finish();

        }
        return true;
    }
}
