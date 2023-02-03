package com.gizwits.opensource.appkit.PushModule;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
//push_baidu-false-start
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
//push_baidu-false-end
import com.gizwits.opensource.appkit.R;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizPushType;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;

import java.util.Set;
//push_jiguang-false-start
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
//push_jiguang-false-end


public class GosPushManager {

    static GizPushType gizPushType;

    static Context context;

    public GosPushManager(int PushType, Context context) {
        super();
        GosPushManager.context = context;
        //push_jiguang-false-start
        if (PushType == 1) {
            GosPushManager.gizPushType = GizPushType.GizPushJiGuang;
            jPush();
        }
        //push_jiguang-false-end
        //push_baidu-false-start
        if (PushType == 2) {
            GosPushManager.gizPushType = GizPushType.GizPushBaiDu;
            bDPush();
        }
        //push_baidu-false-end
    }

    /**
     * Channel_ID
     */
    public static String Channel_ID;

    //push_jiguang-false-start

    /**
     * 此方法完成了初始化JPush SDK等功能 但仍需在MainActivity的onResume和onPause添加相应方法
     * JPushInterface.onResume(context); JPushInterface.onPause(context);
     */
    public void jPush() {
        // 设置JPush调试模式
        JPushInterface.setDebugMode(true);
        // 初始化JPushSDK
        JPushInterface.init(context);
    }
    //push_jiguang-false-end

    //push_baidu-false-start
    public void bDPush() {
        final String BDPushAppKey = GosDeploy.appConfig_BpushAppKey();
        if (BDPushAppKey != null) {
            if (TextUtils.isEmpty(BDPushAppKey) || BDPushAppKey.contains("your_bpush_api_key")) {
                GosBaseActivity.noIDAlert(context, R.string.BDPushAppID_Toast);
            } else {
                PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, BDPushAppKey);
                PushSettings.enableDebugMode(context, true);
            }
        }
    }
    //push_baidu-false-end

    /**
     * 向云端绑定推送
     *
     * @param userId
     * @param token
     */
    public static void pushBindService(String userId, String token) {
        //push_jiguang-false-start
        if (GizPushType.GizPushJiGuang == gizPushType) {
            // 获取JPush的RegistrationID,即Channel_ID
            Channel_ID = JPushInterface.getRegistrationID(context);
            // 设定JPush类型
            JPushInterface.setAlias(context, Channel_ID, new TagAliasCallback() {
                @Override
                public void gotResult(int arg0, String arg1, Set<String> arg2) {
                    if (arg0 == 0) {
                        Log.i("Apptest", "Alias: " + arg1);
                    } else {
                        Log.i("Apptest", "Result: " + arg0);
                    }
                }
            });
        }
        //push_jiguang-false-end
        //push_baidu-false-start
        if (GizPushType.GizPushBaiDu == gizPushType) {
            // 获取BDPush的Channel_ID
            Channel_ID = BaiDuPushReceiver.BaiDuPush_Channel_ID;
            Log.e("Manager", "pushBindService----: " + Channel_ID);
        }
        //push_baidu-false-end
        // TODO 绑定推送
        GizWifiSDK.sharedInstance().channelIDBind(token, Channel_ID, userId, gizPushType);
    }

    public static void pushUnBindService(String token) {
        if (token.isEmpty()) {
            return;
        }
        //push_jiguang-false-start
        if (GizPushType.GizPushJiGuang == gizPushType) {
            // 获取JPush的RegistrationID,即Channel_ID
            Channel_ID = JPushInterface.getRegistrationID(context);
        }
        //push_jiguang-false-end
        //push_baidu-false-start
        if (GizPushType.GizPushBaiDu == gizPushType) {
            // 获取BDPush的Channel_ID
            Channel_ID = BaiDuPushReceiver.BaiDuPush_Channel_ID;
        }
        //push_baidu-false-end
        // TODO 绑定推送
        //Log.i("Apptest", Channel_ID + "\n" + gizPushType.toString() + "\n" + token);
        GizWifiSDK.sharedInstance().channelIDUnBind(token, Channel_ID);
    }

}
