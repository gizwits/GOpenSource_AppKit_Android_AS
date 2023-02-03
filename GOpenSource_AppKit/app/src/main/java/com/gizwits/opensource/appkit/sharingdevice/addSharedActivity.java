package com.gizwits.opensource.appkit.sharingdevice;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.R;


public class addSharedActivity extends GosBaseActivity {

    private String productname;
    private String did;
    private LinearLayout devicetwoshared;
    private LinearLayout usershared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gos_addshared);
        setToolBar(true, R.string.addshared);

        initData();
        initView();
    }

    private void initView() {
        usershared = (LinearLayout) findViewById(R.id.usershared);
        devicetwoshared = (LinearLayout) findViewById(R.id.devicetwoshared);
    }

    private void initData() {
        Intent tent = getIntent();
        productname = tent.getStringExtra("productname");
        did = tent.getStringExtra("did");
    }

    //    personalCenter_deviceSharing_qrcode-false-start
    // 二维码分享
    public void devicetwoshared(View v) {
        Intent tent = new Intent(this, twoSharedActivity.class);
        tent.putExtra("productname", productname);
        tent.putExtra("did", did);
        startActivity(tent);

        devicetwoshared.setEnabled(false);
        devicetwoshared.postDelayed(new Runnable() {
            @Override
            public void run() {
                devicetwoshared.setEnabled(true);
            }
        }, 1000);

    }
    //personalCenter_deviceSharing_qrcode-false-end

    // 用户账号分享

    public void usershared(View v) {

        Intent tent = new Intent(this, userSharedActivity.class);
        tent.putExtra("productname", productname);
        tent.putExtra("did", did);
        startActivity(tent);

        usershared.setEnabled(false);
        usershared.postDelayed(new Runnable() {
            @Override
            public void run() {
                usershared.setEnabled(true);
            }
        }, 1000);
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
}
