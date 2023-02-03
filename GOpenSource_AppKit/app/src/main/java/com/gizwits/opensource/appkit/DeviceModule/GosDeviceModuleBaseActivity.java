package com.gizwits.opensource.appkit.DeviceModule;

import com.gizwits.gizwifisdk.api.GizWifiCentralControlDevice;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiCentralControlDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GosDeviceModuleBaseActivity extends GosBaseActivity {

    /**
     * 设备列表
     */
    protected static List<GizWifiDevice> deviceslist = new ArrayList<GizWifiDevice>();

    private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

        /** 用于设备列表 */
        public void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
            GosDeviceModuleBaseActivity.this.didDiscovered(result, deviceList);
        }

        /** 用于用户匿名登录 */
        public void didUserLogin(GizWifiErrorCode result, String uid, String token) {
            GosDeviceModuleBaseActivity.this.didUserLogin(result, uid, token);
        }

        /** 用于设备解绑 */
        public void didUnbindDevice(GizWifiErrorCode result, String did) {
            GosDeviceModuleBaseActivity.this.didUnbindDevice(result, did);
        }

        /** 用于设备绑定 */
        public void didBindDevice(GizWifiErrorCode result, String did) {
            GosDeviceModuleBaseActivity.this.didBindDevice(result, did);
        }

        /** 用于设备绑定（旧） */
        public void didBindDevice(int error, String errorMessage, String did) {
            GosDeviceModuleBaseActivity.this.didBindDevice(error, errorMessage, did);
        }

        ;

        /** 用于绑定推送 */
        public void didChannelIDBind(GizWifiErrorCode result) {
            GosDeviceModuleBaseActivity.this.didChannelIDBind(result);
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
            GosDeviceModuleBaseActivity.this.didSetSubscribe(result, device, isSubscribed);
        }

        ;

    };

    protected GizWifiCentralControlDeviceListener gizWifisubDeviceListener = new GizWifiCentralControlDeviceListener() {

        // 用于设备订阅
        public void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
            GosDeviceModuleBaseActivity.this.didSetSubscribe(result, device, isSubscribed);
        }

        //同步更新子设备列表
        @Override
        public void didUpdateSubDevices(GizWifiCentralControlDevice device, GizWifiErrorCode result, List<GizWifiDevice> subDeviceList) {
            GosDeviceModuleBaseActivity.this.didUpdateSubDevices(device, result, subDeviceList);
        }

        //设备网络状态变化通知
        @Override
        public void didUpdateNetStatus(GizWifiDevice device, GizWifiDeviceNetStatus netStatus) {
            GosDeviceModuleBaseActivity.this.didUpdateNetStatus(device, netStatus);
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
    protected void onResume() {
        super.onResume();
        // 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
        GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
    }

    /**
     * @param result
     * @param cloudServiceInfo
     */
    protected void didGetCurrentCloudService(GizWifiErrorCode result,
                                             ConcurrentHashMap<String, String> cloudServiceInfo) {
    }

}
