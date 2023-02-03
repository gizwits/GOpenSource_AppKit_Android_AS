package com.gizwits.opensource.appkit.ThirdAccountModule;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class BaseUiListener implements IUiListener {

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onComplete(Object arg0) {
		doComplete((JSONObject)arg0);
	}

	protected void doComplete(JSONObject values) {
	}

	@Override
	public void onError(UiError arg0) {
		// TODO Auto-generated method stu
	}

}
