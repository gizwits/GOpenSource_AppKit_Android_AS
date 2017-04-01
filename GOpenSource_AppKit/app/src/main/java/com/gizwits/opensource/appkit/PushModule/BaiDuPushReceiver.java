package com.gizwits.opensource.appkit.PushModule;

import java.util.List;

import com.baidu.android.pushservice.PushMessageReceiver;

import android.content.Context;
import android.util.Log;

public class BaiDuPushReceiver extends PushMessageReceiver {

	// BaiDuPush_Channel_ID(此处需要在百度推送的Receiver里赋值)
	public static String BaiDuPush_Channel_ID;

	@Override
	public void onBind(Context arg0, int arg1, String arg2, String arg3, String arg4, String arg5) {
		// TODO Auto-generated method stub
		BaiDuPush_Channel_ID = arg4;
		Log.i("Apptest", BaiDuPush_Channel_ID);

	}

	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2, List<String> arg3, String arg4) {

	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2, String arg3) {
	}

	@Override
	public void onMessage(Context arg0, String arg1, String arg2) {
	}

	@Override
	public void onNotificationArrived(Context arg0, String arg1, String arg2, String arg3) {
	}

	@Override
	public void onNotificationClicked(Context arg0, String arg1, String arg2, String arg3) {
	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2, List<String> arg3, String arg4) {
	}

	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
	}

}
