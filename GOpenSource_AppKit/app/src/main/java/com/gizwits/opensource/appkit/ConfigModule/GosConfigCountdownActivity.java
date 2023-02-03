package com.gizwits.opensource.appkit.ConfigModule;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.DeviceModule.GosMainActivity;
import com.gizwits.opensource.appkit.utils.NetUtils;
import com.gizwits.opensource.appkit.view.RoundProgressBar;
import com.gizwits.opensource.appkit.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import static com.gizwits.gizwifisdk.enumration.GizWifiErrorCode.GIZ_SDK_ONBOARDING_STOPPED;

@SuppressLint("HandlerLeak")
public class GosConfigCountdownActivity extends GosConfigModuleBaseActivity {

    private GosWifiChangeReciver broadcase;

    /**
     * The rpb Config
     */
    private RoundProgressBar rpbConfig;

    /**
     * 倒计时
     */
    int secondleft = 60;

    /**
     * The timer
     */
    Timer timer;

    /**
     * The Frist
     */
    boolean isFrist = true;

    /**
     * The isChecked
     */
    boolean isChecked = false;

    String softSSID, presentSSID, workSSID, workSSIDPsw;

    private boolean isShowing = false;
    private String SSID = null, SSIDPsw = null;
    List<GizWifiGAgentType> modeList, modeDataList;
    private TextView tvLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_config_countdown);
        // 设置ActionBar
        setToolBar(false, R.string.search_join);
        initView();
        initData();
        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvLeft.setVisibility(View.VISIBLE);
        SpannableString ssTitle = new SpannableString(this.getString(R.string.cancel));
        ssTitle.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvLeft.setText(ssTitle);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GosConfigCountdownActivity.this, GosMainActivity.class);
                quitAlert(GosConfigCountdownActivity.this, intent, getString(R.string.cancel_configuration));
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFrist) {
            isShowing = true;
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1 * 1000);
                        readyToSoftAP();
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                ;
            }.start();

            isFrist = false;
        }
    }

    private void initView() {
        rpbConfig = (RoundProgressBar) findViewById(R.id.rpbConfig);
    }

    private void initData() {
        softSSID = getIntent().getStringExtra("softSSID");
        workSSIDPsw = spf.getString("workSSIDPsw", "");
        modeDataList = new ArrayList<GizWifiGAgentType>();
        modeDataList.add(GizWifiGAgentType.GizGAgentESP);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP);
        modeDataList.add(GizWifiGAgentType.GizGAgentHF);
        modeDataList.add(GizWifiGAgentType.GizGAgentRTK);
        modeDataList.add(GizWifiGAgentType.GizGAgentWM);
        modeDataList.add(GizWifiGAgentType.GizGAgentQCA);
        modeDataList.add(GizWifiGAgentType.GizGAgentFlyLink);
        modeDataList.add(GizWifiGAgentType.GizGAgentTI);
        modeDataList.add(GizWifiGAgentType.GizGAgentFSK);
        modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP3);
        modeDataList.add(GizWifiGAgentType.GizGAgentBL);
        modeDataList.add(GizWifiGAgentType.GizGAgentAtmelEE);
        modeDataList.add(GizWifiGAgentType.GizGAgentOther);
        modeList = new ArrayList<GizWifiGAgentType>();

        String types = spf.getString("modulestyles", null);
        if (types != null) {
            try {
                JSONArray array = new JSONArray(types);
                for (int i = 0; i < array.length(); i++) {
                    int type = (Integer) array.get(i);
                    modeList.add(modeDataList.get(type));
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private enum handler_key {

        /**
         * 倒计时结束
         */
        TICK_TIME,

        /**
         * 设置手机开启热点
         */
        OPEN_HOT,

        /**
         * 倒计时开始
         */
        START_TIMER,

        /**
         * 配置成功
         */
        SUCCESSFUL,

        /**
         * 配置失败
         */
        FAILED,

    }

    private static final String TAG = "GosConfigCountdownActiv";
    /**
     * The handler.
     */
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {

                case TICK_TIME:

                    break;
                case OPEN_HOT:
                    if (GosConstant.isOpenHot) {
                        boolean b = setWifiApEnabled(true);
                        //               boolean b = setWifiAp(true);
                    }
                    break;

                case START_TIMER:
                    isStartTimer();
                    break;
                // 配置成功
                case SUCCESSFUL:
                    Toast.makeText(GosConfigCountdownActivity.this, R.string.configuration_successful, toastTime)
                            .show();
                    Intent intent1 = new Intent(GosConfigCountdownActivity.this, GosMainActivity.class);
                    startActivity(intent1);
                    break;
                //   配置失败
                case FAILED:
                    isChecked = false;
                    if (msg.obj != null) {
                        Toast.makeText(GosConfigCountdownActivity.this, msg.obj.toString(), toastTime)
                                .show();
                    }
                    Intent intent = new Intent(GosConfigCountdownActivity.this, GosConfigFailedActivity.class);
                    startActivity(intent);
                    finish();
                    break;


                default:
                    break;

            }
        }
    };

    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            quitAlert(this, timer);
            return true;
        }
        return false;
    }

    // 倒计时
    public void isStartTimer() {
        secondleft = 60;
        timer = new Timer();
        // 切换至设备热点
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final WifiAutoConnectManager.WifiCipherType cipherType = WifiAutoConnectManager.getCipherType(GosConfigCountdownActivity.this, softSSID);
        final WifiAutoConnectManager manager = new WifiAutoConnectManager(wifiManager);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isShowing) {
                    manager.connect(softSSID, SoftAP_PSW, cipherType);
                }
                secondleft--;
                rpbConfig.setProgress((60 - secondleft) * (100 / 60.0));
                if (secondleft == 1) {
                    handler.sendEmptyMessage(handler_key.FAILED.ordinal());
                    timer.cancel();
                }

            }
        }, 1000, 1000);
    }

    private void readyToSoftAP() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

        workSSID = spf.getString("workSSID", "");
        workSSIDPsw = spf.getString("workSSIDPsw", "");
        if (!workSSIDPsw.isEmpty() && workSSIDPsw.equals(GosConstant.SSIDPsw)) {
            GosConstant.isOpenHot = true;
        }
        //新加
        handler.sendEmptyMessage(handler_key.START_TIMER.ordinal());
        isChecked = true;
        while (isChecked) {
            String presentSSID = NetUtils.getCurentWifiSSID(GosConfigCountdownActivity.this);
            if (!TextUtils.isEmpty(presentSSID) && presentSSID.contains(SoftAP_Start)) {
                if (checkNetwork(GosConfigCountdownActivity.this)) {
                    String connectWifiSsid = NetUtils.getConnectWifiSsid(GosConfigCountdownActivity.this);
                    isShowing = false;
                    isChecked = false;
                    //handler.sendEmptyMessage(handler_key.START_TIMER.ordinal());
                    switch (GosConstant.mNew) {
                        case 0:
                            GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw,
                                    GizWifiConfigureMode.GizWifiSoftAP, presentSSID, 60, modeList);
                            break;
                        case 1:
                            GizWifiSDK.sharedInstance().setDeviceOnboardingByBind(workSSID, workSSIDPsw,
                                    GizWifiConfigureMode.GizWifiSoftAP, presentSSID, 60, modeList);
                            break;
                        case 2:
                            GizWifiSDK.sharedInstance().setDeviceOnboardingDeploy(workSSID, workSSIDPsw,
                                    GizWifiConfigureMode.GizWifiSoftAP, presentSSID, 60, modeList, false);
                            break;
                        case 3:
                            GizWifiSDK.sharedInstance().setDeviceOnboardingDeploy(workSSID, workSSIDPsw,
                                    GizWifiConfigureMode.GizWifiSoftAP, presentSSID, 60, modeList, true);
                            break;
                    }
                    if (GosConstant.isOpenHot) {
                        final Timer mtimer1 = new Timer();
                        mtimer1.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(handler_key.OPEN_HOT.ordinal());
                            }
                        }, 5 * 1000);
                    }
                    //	handler.sendEmptyMessageDelayed(handler_key.OFFTIME.ordinal(), 2000);
                }
                if (broadcase == null && !GosConstant.isOpenHot) {
                    broadcase = new GosWifiChangeReciver();
                    registerReceiver(broadcase, filter);
                }

            }
        }
    }


    private WifiManager wifiManager = null;


    // wifi热点开关
    public boolean setWifiApEnabled(boolean enabled) {
        //获取wifi管理服务
        if (enabled) { // disable WiFi in any case
            //wifi和热点不能同时打开，所以打开热点的时候需要关闭wifi
            wifiManager.setWifiEnabled(false);
        }
        try {
            //热点的配置类
            WifiConfiguration apConfig = new WifiConfiguration();
            //配置热点的名称(可以在名字后面加点随机数什么的)
            if (SSID == null && SSID.equals("")) {
                SSID = android.os.Build.MODEL;
                apConfig.SSID = SSID;
                apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            } else if (SSIDPsw == null && SSIDPsw.equals("")) {
                apConfig.SSID = SSID;
                apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            } else {
                apConfig.SSID = SSID;
                //配置热点的密码
                apConfig.preSharedKey = SSIDPsw;
                //返回热点打开状态
//                for (int i = 0; i < WifiConfiguration.KeyMgmt.strings.length; i++) {
//                    if ("WPA2_PSK".equals(WifiConfiguration.KeyMgmt.strings[i])) {
//                        apConfig.allowedKeyManagement.set(i);//直接给它赋索引的值
//                        Log.e("wpa2索引", String.valueOf(i));//结果是4
//                    }
//                }
                apConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);//直接给它赋索引的值
            }
            //通过反射调用设置热点
            Method method = wifiManager.getClass().getMethod(
                    "setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
            //返回热点打开状态
            return (Boolean) method.invoke(wifiManager, apConfig, enabled);
        } catch(Exception e) {
            return false;
        }

    }


    /**
     * 设备配置回调
     *
     * @param result     错误码
     * @param mac        MAC
     * @param did        DID
     * @param productKey PK
     */
    protected void didSetDeviceOnboarding(GizWifiErrorCode result, String mac, String did, String productKey) {
        if (GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING == result) {
            return;
        }
        if (timer != null) {
            timer.cancel();
        }
        Message message = new Message();
        if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            message.what = handler_key.SUCCESSFUL.ordinal();
        } else if (result == GIZ_SDK_ONBOARDING_STOPPED) {

        } else {
            message.what = handler_key.FAILED.ordinal();
            message.obj = toastError(result);
        }
        handler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isChecked = false;
        if (timer != null) {
            timer.cancel();
        }
        if (broadcase != null) {
            unregisterReceiver(broadcase);
            broadcase = null;
        }


    }
}