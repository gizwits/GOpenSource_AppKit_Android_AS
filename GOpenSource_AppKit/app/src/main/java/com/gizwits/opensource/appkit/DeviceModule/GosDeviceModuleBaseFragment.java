package com.gizwits.opensource.appkit.DeviceModule;

import android.util.Log;

import com.gizwits.gizwifisdk.api.GizWifiCentralControlDevice;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiCentralControlDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GosDeviceModuleBaseFragment extends GosBaseFragment {

    /**
     * 设备列表
     */
    protected static List<GizWifiDevice> deviceslist = new ArrayList<GizWifiDevice>();

    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        /** 用于设备列表 */
        public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
            GosDeviceModuleBaseFragment.this.didDiscovered(result, deviceList);
            Log.e("GosDeviceModuleBaseFragment", "didDiscovered=====----: 更新数据");
        }

        /** 用于用户匿名登录 */
        public void didUserLogin(GizWifiErrorCode result, String uid, String token) {
            GosDeviceModuleBaseFragment.this.didUserLogin(result, uid, token);
        }

        /** 用于设备解绑 */
        public void didUnbindDevice(GizWifiErrorCode result, String did) {
            GosDeviceModuleBaseFragment.this.didUnbindDevice(result, did);
        }

        /** 用于设备绑定 */
        public void didBindDevice(GizWifiErrorCode result, String did) {
            GosDeviceModuleBaseFragment.this.didBindDevice(result, did);
        }

        /** 用于设备绑定（旧） */
        public void didBindDevice(int error, String errorMessage, String did) {
            GosDeviceModuleBaseFragment.this.didBindDevice(error, errorMessage, did);
        }

        ;

        /** 用于绑定推送 */
        public void didChannelIDBind(GizWifiErrorCode result) {
            GosDeviceModuleBaseFragment.this.didChannelIDBind(result);
        }

    };

    /**
     * 设备列表回调
     *
     * @param result
     * @param deviceList
     */
    protected void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
    }

    /**
     * 用户匿名登录回调
     *
     * @param result
     * @param uid
     * @param token
     */
    protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {
    }

    /**
     * 设备解绑回调
     *
     * @param result
     * @param did
     */
    protected void didUnbindDevice(GizWifiErrorCode result, String did) {
    }

    /**
     * 设备绑定回调(旧)
     *
     * @param error
     * @param errorMessage
     * @param did
     */
    protected void didBindDevice(int error, String errorMessage, String did) {
    }

    ;

    /**
     * 设备绑定回调
     *
     * @param result
     * @param did
     */
    protected void didBindDevice(GizWifiErrorCode result, String did) {
    }

    /**
     * 绑定推送回调
     *
     * @param result
     */
    protected void didChannelIDBind(GizWifiErrorCode result) {
    }

    /**
     * 设备监听
     */
    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        // 用于设备订阅
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            GosDeviceModuleBaseFragment.this.didSetSubscribe(result, device, isSubscribed);
        }

        ;

    };

    protected GizWifiCentralControlDeviceListener gizWifisubDeviceListener = new GizWifiCentralControlDeviceListener() {

        // 用于设备订阅
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            GosDeviceModuleBaseFragment.this.didSetSubscribe(result, device, isSubscribed);
        }

        //同步更新子设备列表
        @Override
        public void didUpdateSubDevices(GizWifiCentralControlDevice device, GizWifiErrorCode result, List<GizWifiDevice> subDeviceList) {
            GosDeviceModuleBaseFragment.this.didUpdateSubDevices(device, result, subDeviceList);
        }

        //设备网络状态变化通知
        @Override
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            GosDeviceModuleBaseFragment.this.didUpdateNetStatus(device, netStatus);
        }

        ;

    };

    public GizWifiDeviceListener getGizWifiDeviceListener(GizWifiDevice device) {

        if (device.getProductType() == GizWifiDeviceType.GizDeviceNormal) {
            return gizWifiDeviceListener;
        } else {
            return gizWifisubDeviceListener;
        }

    }

    /**
     * 设备网络状态变化通知
     *
     * @param device
     * @param netStatus
     */
    protected void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
    }

    /**
     * 同步更新子设备列表
     *
     * @param device
     * @param result
     * @param subDeviceList
     */
    protected void didUpdateSubDevices(GizWifiCentralControlDevice device, GizWifiErrorCode result, List<GizWifiDevice> subDeviceList) {
    }


    /**
     * 设备订阅回调
     *
     * @param result
     * @param device
     * @param isSubscribed
     */
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
        Log.e("TAG", "GosDeviceModuleBaseFragment   ----onResume: ---");
    }


    /**
     * @param result
     * @param cloudServiceInfo
     */
    protected void didGetCurrentCloudService(GizWifiErrorCode result,
                                             ConcurrentHashMap<String, String> cloudServiceInfo) {
    }

}
