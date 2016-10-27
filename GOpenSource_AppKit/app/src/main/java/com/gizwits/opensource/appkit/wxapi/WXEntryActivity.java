package com.gizwits.opensource.appkit.wxapi;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import com.gizwits.gizwifisdk.enumration.GizThirdAccountType;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity;
import com.gizwits.opensource.appkit.utils.JsonUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class WXEntryActivity extends GosBaseActivity implements IWXAPIEventHandler {

	private static final String TAG = "WXEntryActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IWXAPI api = WXAPIFactory.createWXAPI(this, GosDeploy.setWechatAppID(), true);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
		Log.e(TAG, "onReq...");
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.e(TAG, "onResp...");
		Log.i("Apptest", "onResp...");
		String code = null;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:// 用户同意,只有这种情况的时候code是有效的
			code = ((SendAuth.Resp) resp).code;
			Log.i("Apptest", code);
			getResult(code);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:// 用户拒绝授权
			Log.e("Apptest", "用户拒绝授权");
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消
			Log.e("Apptest", "用户取消");
			break;

		default:// 发送返回

			break;
		}
		finish();
	}

	/**
	 * 获取openid accessToken值用于后期操作
	 * 
	 * @param code
	 *            请求码
	 */
	private void getResult(final String code) {
		new Thread() {// 开启工作线程进行网络请求
			public void run() {
				String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + GosDeploy.setWechatAppID()
						+ "&secret=" + GosDeploy.setWechatAppSecret() + "&code=" + code
						+ "&grant_type=authorization_code";
				try {
					JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
					if (null != jsonObject) {
						String openid = jsonObject.getString("openid").toString().trim();
						String access_token = jsonObject.getString("access_token").toString().trim();
						Log.i(TAG, "openid = " + openid);
						Log.i(TAG, "access_token = " + access_token);

						GosUserLoginActivity.gizThirdAccountType = GizThirdAccountType.GizThirdWeChat;
						GosUserLoginActivity.thirdToken = access_token;
						GosUserLoginActivity.thirdUid = openid;

						Message msg = new Message();
						msg.what = GosUserLoginActivity.handler_key.THRED_LOGIN.ordinal();
						baseHandler.sendMessage(msg);

					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return;
			};
		}.start();
	}

}
