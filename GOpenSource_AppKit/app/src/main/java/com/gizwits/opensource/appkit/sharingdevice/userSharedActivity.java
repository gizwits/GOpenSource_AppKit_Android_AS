package com.gizwits.opensource.appkit.sharingdevice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingWay;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.CommonModule.TipsDialog;
import com.gizwits.opensource.appkit.R;

public class userSharedActivity extends GosBaseActivity {

    private String productname;
    private EditText username;
    private int chooseitem = 0;
    private String did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_user_shared);

        setToolBar(true, R.string.account_shared);

        initData();
        initView();
    }

    private void initView() {
        TextView usersharedtext = (TextView) findViewById(R.id.usersharedtext);
        Button button = (Button) findViewById(R.id.button);
        button.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        button.setTextColor(GosDeploy.appConfig_Contrast());

        username = (EditText) findViewById(R.id.username);
        usersharedtext.setText(
                getResources().getString(R.string.shared) + productname + getResources().getString(R.string.friends));
    }

    private void initData() {

        Intent tent = getIntent();
        productname = tent.getStringExtra("productname");
        did = tent.getStringExtra("did");
    }

    public void usershared(View v) {

        final String usernametext = username.getText().toString();
        if (TextUtils.isEmpty(usernametext)) {

            // Toast.makeText(this,
            // getResources().getString(R.string.toast_name_empet), 0).show();

            TipsDialog dia = new TipsDialog(this, getResources().getString(R.string.toast_name_empet));
            dia.show();
            return;
        }

        SharedPreferences spf = getSharedPreferences("set", Context.MODE_PRIVATE);
        String token = spf.getString("Token", "");
        if (usernametext.length() < 32) {
            if (usernametext.matches("[0-9]+")) {
                GizDeviceSharing.sharingDevice(token, did, GizDeviceSharingWay.GizDeviceSharingByNormal, usernametext,
                        GizUserAccountType.GizUserPhone);
                return;
            }

        }
        if (usernametext.contains("@")) {
            GizDeviceSharing.sharingDevice(token, did, GizDeviceSharingWay.GizDeviceSharingByNormal, usernametext,
                    GizUserAccountType.GizUserEmail);
            return;
        }
        if (usernametext.length() == 32) {
            if (usernametext.matches("[a-zA-Z0-9]+")) {
                GizDeviceSharing.sharingDevice(token, did, GizDeviceSharingWay.GizDeviceSharingByNormal, usernametext,
                        GizUserAccountType.GizUserOther);
                return;
            }

        }
        Toast.makeText(this, getString(R.string.account_incorrect), Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onResume() {
        super.onResume();

        GizDeviceSharing.setListener(new GizDeviceSharingListener() {

            @Override
            public void didSharingDevice(GizWifiErrorCode result, String deviceID, int sharingID,
                                         Bitmap QRCodeImage) {
                super.didSharingDevice(result, deviceID, sharingID, QRCodeImage);

                if (result.ordinal() == 0) {
                    Toast.makeText(userSharedActivity.this, getResources().getString(R.string.alawyssend), 1).show();
                    finish();
                } else if (result == GizWifiErrorCode.GIZ_OPENAPI_GUEST_ALREADY_BOUND) {
                    Toast.makeText(userSharedActivity.this, getResources().getString(R.string.account_shared2), toastTime).show();
                } else if (result == GizWifiErrorCode.GIZ_OPENAPI_NOT_FOUND_GUEST) {
                    Toast.makeText(userSharedActivity.this, getResources().getString(R.string.user_not_exist), toastTime).show();
                } else if (result == GizWifiErrorCode.GIZ_OPENAPI_CANNOT_SHARE_TO_SELF) {
                    Toast.makeText(userSharedActivity.this, getResources().getString(R.string.not_shared_self), toastTime).show();
                } else {
                    Toast.makeText(userSharedActivity.this, getResources().getString(R.string.send_failed1), 2).show();
                }

            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
