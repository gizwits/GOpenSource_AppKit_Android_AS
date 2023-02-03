package com.gizwits.opensource.appkit.CommonModule;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import com.gizwits.opensource.appkit.MessageCenter;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity;
import com.gizwits.opensource.appkit.utils.AssetsUtils;

/**
 * Created by admin on 2017/6/8.
 */

public class SplashActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private final int SPLASH_DISPLAY_LENGHT = 2000;
    private Handler handler;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    //push-all-start
    private GosPushManager gosPushManager;
    //push-all-end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!this.isTaskRoot()) {// 判断此activity是不是任务控件的源Activity，“非”也就是说是被系统重新实例化出来的
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gos_splash);

        handler = new Handler();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                //检测是否有写的权限
                int permission = ActivityCompat.checkSelfPermission(SplashActivity.this,
                        "android.permission.WRITE_EXTERNAL_STORAGE");
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    try {
                        // 没有写的权限，去申请写的权限，会弹出对话框
                        ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    initEvent();
                }
            }
        });

    }

    private void initEvent() {

        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this,
                        GosUserLoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

        MessageCenter.getInstance(SplashActivity.this);
        // 在配置文件中选择推送类型（（2：百度，1：极光推送 ））
        if (GosDeploy.appConfig_Push_BaiDu()) {
            Log.e("TAG", "initEvent----: 百度推送");
            gosPushManager = new GosPushManager(2, this);
        }
        if (GosDeploy.appConfig_Push_JiGuang()) {
            gosPushManager = new GosPushManager(1, this);
        }

        TextView tvName = (TextView) findViewById(R.id.tvName);
        if (AssetsUtils.isZh(SplashActivity.this)) {
            if (GosDeploy.appConfig_LaunchInfoCH() != null) {
                tvName.setText(GosDeploy.appConfig_LaunchInfoCH());
            }
        } else {
            if (GosDeploy.appConfig_LaunchInfoEN() != null) {
                tvName.setText(GosDeploy.appConfig_LaunchInfoEN());
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        initEvent();
    }


}
