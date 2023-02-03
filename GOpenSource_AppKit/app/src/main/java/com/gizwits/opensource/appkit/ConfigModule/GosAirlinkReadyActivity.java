package com.gizwits.opensource.appkit.ConfigModule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.utils.AssetsUtils;
import com.gizwits.opensource.appkit.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

public class GosAirlinkReadyActivity extends GosConfigModuleBaseActivity implements OnClickListener {


    /**
     * The tv Ready
     */
    TextView tvReady;


    /**
     * The btn Next
     */
    Button btnNext;

    TextView tvDeviceTip;

    private int sum = 0;

    //private TextView moudlechoose;

    private List<String> modeList;
    private ImageView ivReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_gos_airlink_ready);
        // 设置ActionBar
        setToolBar(true, R.string.airlink_ready_title);

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
        tvReady = (TextView) findViewById(R.id.tvReady);
        btnNext = (Button) findViewById(R.id.btnNext);
        tvDeviceTip = (TextView) findViewById(R.id.tvDeviceTip);
        ivReady = (ImageView) findViewById(R.id.ivReady);
        SpannableString spannableString = new SpannableString(getString(R.string.common_ready_message));
        if (AssetsUtils.isZh(this)) {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#007AFF")), 9, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#007AFF")), 28, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        tvReady.setText(spannableString);

        // 配置文件部署
        btnNext.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        btnNext.setTextColor(GosDeploy.appConfig_Contrast());
    }

    private void initEvent() {
        btnNext.setOnClickListener(this);
        tvDeviceTip.setOnClickListener(this);
        ivReady.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                if (ToolUtils.noDoubleClick()) {
                    sum = 0;
                    Intent intent = new Intent(this, GosAirlinkConfigCountdownActivity.class);
                    startActivity(intent);
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
                sum = 0;
                this.finish();
                //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
        return true;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
//        startActivity(intent);
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//        this.finish();
//        return true;
//    }


}
