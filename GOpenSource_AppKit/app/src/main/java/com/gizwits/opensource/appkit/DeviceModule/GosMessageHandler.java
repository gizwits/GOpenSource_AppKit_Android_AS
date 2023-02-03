package com.gizwits.opensource.appkit.DeviceModule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

public class GosMessageHandler {

    NotificationManager nm;

    protected static final int SHOWDIALOG = 999;

    private Context mcContext;

    private ArrayList<String> newDeviceList = new ArrayList<String>();

    private Handler mainHandler;
    // 做一个单例
    private static GosMessageHandler mInstance = new GosMessageHandler();

    public static GosMessageHandler getSingleInstance() {
        return mInstance;
    }

    public void SetHandler(Handler handler) {
        this.mainHandler = handler;
    }

    public void StartLooperWifi(Context context) {
        this.mcContext = context;
        HandlerThread looperwifi = new HandlerThread("looperwifi");
        looperwifi.start();
        looper = new MyLooperHandler(looperwifi.getLooper());
        looper.post(mRunnable);
    }

    class MyLooperHandler extends Handler {
        public MyLooperHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    }

    /**
     * 子线程实现
     */
    private Runnable mRunnable = new Runnable() {

        public void run() {
            if (mcContext == null) {
                return;
            }
            newDeviceList.clear();
            List<ScanResult> currentWifiScanResult = NetUtils
                    .getCurrentWifiScanResult(mcContext);
            GosConstant.ssidList = currentWifiScanResult;
            int flog = 0;
            if (currentWifiScanResult != null) {
                for (ScanResult scanResult : currentWifiScanResult) {
                    String ssid = scanResult.SSID;
                    // 获取系统的NotificationManager服务
                    nm = (NotificationManager) mcContext
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    if (ssid.contains(GosBaseActivity.SoftAP_Start)
                            && ssid.length() > GosBaseActivity.SoftAP_Start.length()
                            && !newDeviceList.toString().contains(ssid)) {
                        newDeviceList.add(ssid);
                        flog++;
                    }
                }
            }

            if (mainHandler != null && newDeviceList.size() > 0) {
                mainHandler.sendEmptyMessage(SHOWDIALOG);
            }

            looper.postDelayed(mRunnable, 2000);
        }
    };
    private MyLooperHandler looper;


    public ArrayList<String> getNewDeviceList() {
        return newDeviceList;
    }

}
