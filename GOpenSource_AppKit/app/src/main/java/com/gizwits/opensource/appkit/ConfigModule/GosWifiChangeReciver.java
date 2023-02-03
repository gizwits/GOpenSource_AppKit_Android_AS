package com.gizwits.opensource.appkit.ConfigModule;

import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.utils.NetUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;


public class GosWifiChangeReciver extends BroadcastReceiver {

    SharedPreferences spf;

    @Override
    public void onReceive(Context context, Intent intent) {

        spf = context.getSharedPreferences(GosBaseActivity.SPF_Name, Context.MODE_PRIVATE);

        String wifiname = spf.getString("workSSID", "");
        String wifipass = spf.getString("workSSIDPsw", "");
        String connectWifiSsid = NetUtils.getConnectWifiSsid(context);
        if (connectWifiSsid != null && connectWifiSsid.contains(GosBaseActivity.SoftAP_Start)) {
        } else {
            if (connectWifiSsid.contains(wifiname)) {
                return;
            }
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiAutoConnectManager manager = new WifiAutoConnectManager(wifiManager);
            manager.connect(wifiname, wifipass, WifiAutoConnectManager.getCipherType(context, wifiname));

        }
    }
}
