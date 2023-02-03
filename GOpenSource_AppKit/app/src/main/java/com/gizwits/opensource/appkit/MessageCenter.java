package com.gizwits.opensource.appkit;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizLogPrintLevel;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class MessageCenter {
    private static MessageCenter mCenter;
    int flag = 0;

    GosDeploy gosDeploy;

    private int SETCLOUD = 1111;

    private MessageCenter(Context c) {
        if (mCenter == null) {
            init(c);
        }
    }

    private void init(Context c) {
        gosDeploy = new GosDeploy(c);
        //config-all-start
        if (GosDeploy.appConfig_UseOnboardingDeploy()) {
            if (GosDeploy.appConfig_OnboardingBind()) {
                GosConstant.mNew = 3;
            } else {
                GosConstant.mNew = 2;
            }
        } else {
            if (GosDeploy.appConfig_OnboardingBind()) {
                GosConstant.mNew = 1;
            } else {
                GosConstant.mNew = 0;
            }
        }
        //config-all-end
        String AppID = GosDeploy.appConfig_GizwitsInfoAppID();
        String AppSecret = GosDeploy.appConfig_GizwitsInfoAppSecret();
        List<ConcurrentHashMap<String, String>> productInfoList = GosDeploy.appConfig_ProductInfoList();
        if (productInfoList.size() == 0) {
            productInfoList = null;
        }

        if (AppID == null || AppSecret == null || TextUtils.isEmpty(AppID) || AppID.contains("BeJson")
                || TextUtils.isEmpty(AppSecret) || AppSecret.contains("BeJson")
                || AppID.length() != 32 || AppSecret.length() != 32) {
            String AppID_Toast = c.getString(R.string.AppID_Toast);
            if (flag == 0) {
                Toast.makeText(c, AppID_Toast, Toast.LENGTH_LONG).show();
            }
            flag++;
        } else {
            // 启动SDK
            ConcurrentHashMap<String, String> serverMap = new ConcurrentHashMap<String, String>();
            ConcurrentHashMap<String, String> appInfo = new ConcurrentHashMap<String, String>();
            appInfo.put("appId", AppID);
            appInfo.put("appSecret", AppSecret);

            String api = GosDeploy.appConfig_CloudServiceApi();
            String site = GosDeploy.appConfig_CloudServiceSite();
            String push = GosDeploy.appConfig_CloudServicePush();
            Pattern pattern = Pattern.compile("(^[a-zA-Z0-9\\-]{1,}\\.[a-zA-Z0-9]{1,}\\.[a-zA-Z0-9]{1,}\\:(\\d){1,}\\&(\\d){1,}$)|(^[a-zA-Z0-9\\-]{1,}\\.[a-zA-Z0-9]{1,}\\.[a-zA-Z0-9]{1,}$)");
            Pattern pattern1 = Pattern.compile("(^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}\\:(\\d){1,}\\&(\\d){1,}$)|(^([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$)");
            if (api != null) {
                if (pattern.matcher(api).matches() || pattern1.matcher(api).matches()) {
                    serverMap.put("openAPIInfo", api);
                    if (site != null) {
                        serverMap.put("siteInfo", site);
                    }
                    if (push != null && !push.isEmpty()) {
                        serverMap.put("pushInfo", GosDeploy.appConfig_CloudServicePush());
                    }
                    GizWifiSDK.sharedInstance().startWithAppInfo(c, appInfo, productInfoList, serverMap, false);
                } else {
                    GizWifiSDK.sharedInstance().startWithAppInfo(c, appInfo,
                            productInfoList, null, false);
                }
            } else {
                GizWifiSDK.sharedInstance().startWithAppInfo(c, appInfo,
                        productInfoList, null, false);
            }
        }
        hand.sendEmptyMessageDelayed(SETCLOUD, 3000);
    }

    public static MessageCenter getInstance(Context c) {
        if (mCenter == null) {
            mCenter = new MessageCenter(c);
        }
        return mCenter;
    }

    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {
            GizWifiSDK.sharedInstance().setLogLevel(
                    GizLogPrintLevel.GizLogPrintAll);

        }
    };

}
