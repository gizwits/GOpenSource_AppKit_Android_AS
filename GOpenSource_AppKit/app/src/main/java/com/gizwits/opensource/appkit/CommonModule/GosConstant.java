package com.gizwits.opensource.appkit.CommonModule;

import android.net.wifi.ScanResult;

import com.gizwits.gizwifisdk.api.GizDeviceSharingInfo;
import com.gizwits.gizwifisdk.api.GizUserInfo;

import java.util.ArrayList;
import java.util.List;

public class GosConstant {

    // 设备热点默认前缀
    public static final String SoftAP_Start = "XPG-GAgent";

    public static final String SSIDPsw = "#10v3#";
    public static boolean isOpenHot = false;
    /**
     * 0  使用旧接口 setDeviceOnboarding
     * 1 配网绑定接口  setDeviceOnboardingByBind
     * 2 域名配网接口  setDeviceOnboardingDeploy false
     * 3 域名配网接口  setDeviceOnboardingDeploy true
     */
    public static int mNew = 0;
    public static List<ScanResult> ssidList = new ArrayList<ScanResult>();
    public static int nowPager = -1;
    public static List<GizUserInfo> mybindUsers = new ArrayList<GizUserInfo>();
    public static boolean isEdit = false;

    public static List<GizDeviceSharingInfo> mydeviceSharingInfos = new ArrayList<GizDeviceSharingInfo>();
    public static List<GizDeviceSharingInfo> newmydeviceSharingInfos = new ArrayList<GizDeviceSharingInfo>();


}
