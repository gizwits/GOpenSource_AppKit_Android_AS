package com.gizwits.opensource.appkit.wxapi;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.gizwits.gizwifisdk.enumration.GizThirdAccountType;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.UserModule.GosUserLoginActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class WXEntryActivity extends GosBaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IWXAPI api = WXAPIFactory.createWXAPI(this, GosDeploy.appConfig_WechatAppID(), true);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.i(TAG, "onReq...");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i(TAG, "onResp: " + resp);
        String code = null;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:// 用户同意,只有这种情况的时候code是有效的
                code = ((SendAuth.Resp) resp).code;
                Log.i("Apptest", code);
                try {
                    requesUserInfo(code);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:// 用户拒绝授权
                Log.i("Apptest", "用户拒绝授权");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消
                Log.i("Apptest", "用户取消");
                break;

            default:// 发送返回

                break;
        }
        finish();
    }

    public void requesUserInfo(final String code) throws Exception {
        final String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + GosDeploy.appConfig_WechatAppID()
                + "&secret=" + GosDeploy.appConfig_WechatAppSecret() + "&code=" + code
                + "&grant_type=authorization_code";
        final android.os.Handler handler = new android.os.Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle b = msg.getData();
                String newsTemp = b.getString("msg");
                try {
                    JSONObject jsonObject = new JSONObject(newsTemp);
                    if (null != jsonObject) {
                        String openid = jsonObject.getString("openid").toString().trim();
                        String access_token = jsonObject.getString("access_token").toString().trim();

                        GosUserLoginActivity.gizThirdAccountType = GizThirdAccountType.GizThirdWeChat;
                        GosUserLoginActivity.thirdToken = access_token;
                        GosUserLoginActivity.thirdUid = openid;

                        Message msg1 = new Message();
                        msg1.what = GosUserLoginActivity.handler_key.THRED_LOGIN.ordinal();
                        baseHandler.sendMessage(msg1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                    httpconn.setRequestProperty("accept", "*/*");
                    httpconn.setDoInput(true);
                    httpconn.setDoOutput(true);
                    httpconn.setConnectTimeout(5000);
                    httpconn.connect();
                    int stat = 200;
                    String msg = "";
                    if (stat == 200) {
                        br = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
                        msg = br.readLine();
                        Bundle b = new Bundle();
                        b.putString("msg", msg);
                        Message m = new Message();
                        m.setData(b);
                        handler.sendMessage(m);
                    } else {
                        msg = "请求失败";
                        Log.i(TAG, msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

}

