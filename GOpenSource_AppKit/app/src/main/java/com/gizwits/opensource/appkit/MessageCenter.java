package com.gizwits.opensource.appkit;

import java.util.concurrent.ConcurrentHashMap;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizLogPrintLevel;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

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
		String AppID = GosDeploy.setAppID();
		String AppSecret = GosDeploy.setAppSecret();
		if (TextUtils.isEmpty(AppID) || AppID.contains("your_app_id") || TextUtils.isEmpty(AppSecret)
				|| AppSecret.contains("your_app_secret")) {
			String AppID_Toast = c.getString(R.string.AppID_Toast);
			if (flag == 0) {
				Toast.makeText(c, AppID_Toast, Toast.LENGTH_LONG).show();
			}
			flag++;
		} else {
			// 启动SDK
			ConcurrentHashMap<String, String> serverMap = new ConcurrentHashMap<String, String>();

			serverMap.put("openAPIInfo", TextUtils.isEmpty((String) GosDeploy.infoMap.get("openAPI_URL"))
					? "api.gizwits.com" : (String) GosDeploy.infoMap.get("openAPI_URL"));
			serverMap.put("siteInfo", TextUtils.isEmpty((String) GosDeploy.infoMap.get("site_URL")) ? "site.gizwits.com"
					: (String) GosDeploy.infoMap.get("site_URL"));
			serverMap.put("pushInfo", (String) GosDeploy.infoMap.get("push_URL"));
			//GizWifiSDK.sharedInstance().startWithAppID(c, AppID, GosDeploy.setProductKeyList(), serverMap);

			GizWifiSDK.sharedInstance().startWithAppID(c, AppID, AppSecret, GosDeploy.setProductKeyList(), serverMap,
					false);

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
			GizWifiSDK.sharedInstance().setLogLevel(GizLogPrintLevel.GizLogPrintAll);

		};
	};

}
