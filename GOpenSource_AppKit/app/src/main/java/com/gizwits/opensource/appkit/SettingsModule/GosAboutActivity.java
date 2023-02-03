package com.gizwits.opensource.appkit.SettingsModule;

import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.utils.AssetsUtils;

public class GosAboutActivity extends GosBaseActivity {

    /**
     * The tv SDKVersion.
     */
    TextView tv_SDKVersion;

    /**
     * the tv appCode
     */
    TextView tv_AppVersion;

    /**
     * The ActionBar
     */
    ActionBar actionBar;
    private LinearLayout llApp;
    private LinearLayout llSDK;
    private LinearLayout llAbout;
    private TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_about);
        // 设置ActionBar
        setToolBar(true, R.string.about);

        initView();
        initEvent();
    }

    private void initEvent() {
        if (!GosDeploy.appConfig_ShowAPPVersion() && !GosDeploy.appConfig_ShowSDKVersion()) {
            llApp.setVisibility(View.VISIBLE);
            llSDK.setVisibility(View.VISIBLE);
            llAbout.setVisibility(View.VISIBLE);
        } else {
            if (!GosDeploy.appConfig_ShowAPPVersion()) {
                llApp.setVisibility(View.GONE);
            }
            if (!GosDeploy.appConfig_ShowSDKVersion()) {
                llSDK.setVisibility(View.GONE);
            }
            if (GosDeploy.appConfig_AboutInfoCH() != null) {
                if (GosDeploy.appConfig_AboutInfoEN() != null) {
                    if (AssetsUtils.isZh(this)) {
                        tvAbout.setText(GosDeploy.appConfig_AboutInfoCH());
                    } else {
                        tvAbout.setText(GosDeploy.appConfig_AboutInfoEN());
                    }
                } else {
                    tvAbout.setText(GosDeploy.appConfig_AboutInfoCH());
                }
                llAbout.setVisibility(View.VISIBLE);
            } else {
                if (GosDeploy.appConfig_AboutInfoEN() != null) {
                    tvAbout.setText(GosDeploy.appConfig_AboutInfoEN());
                    llAbout.setVisibility(View.VISIBLE);
                } else {
                    llAbout.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * Inits the view.
     */
    private void initView() {
        llApp = (LinearLayout) findViewById(R.id.llAPP);
        llSDK = (LinearLayout) findViewById(R.id.llSDK);
        llAbout = (LinearLayout) findViewById(R.id.llAbout);
        tvAbout = (TextView) findViewById(R.id.tvAbout);
        tv_SDKVersion = (TextView) findViewById(R.id.versionCode);
        tv_AppVersion = (TextView) findViewById(R.id.appCode);

    }

    @Override
    public void onResume() {
        super.onResume();
        tv_SDKVersion.setText(GizWifiSDK.sharedInstance().getVersion().toString());
        tv_AppVersion.setText(getAppVersionName(this));
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch(Exception e) {
            Log.i("Apptest", "Exception", e);
        }
        return versionName;
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
