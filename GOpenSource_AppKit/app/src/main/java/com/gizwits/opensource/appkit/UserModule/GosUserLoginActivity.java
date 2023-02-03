package com.gizwits.opensource.appkit.UserModule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.opensource.appkit.R;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizThirdAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.DeviceModule.GosDeviceListFragment;
import com.gizwits.opensource.appkit.DeviceModule.GosMainActivity;
//push-all-start
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
//push_jiguang-false-start

import cn.jpush.android.api.JPushInterface;
//push_jiguang-false-end
//push-all-end

import com.gizwits.opensource.appkit.utils.ToolUtils;
//login_weChat-false-start
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
//login_weChat-false-end
//login_qq-false-start
import com.gizwits.opensource.appkit.ThirdAccountModule.BaseUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
//login_qq-false-end
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("HandlerLeak")
public class GosUserLoginActivity extends com.gizwits.opensource.appkit.UserModule.GosUserModuleBaseActivity implements
        OnClickListener {

    //GosPushManager gosPushManager;

    /**
     * The et Name
     */
    private static EditText etName;

    /**
     * The et Psw
     */
    private static EditText etPsw;

    /**
     * The btn Login
     */
    private Button btnLogin;

    /**
     * The tv Register
     */
    private TextView tvRegister;

    /**
     * The tv Forget
     */
    private TextView tvForget;
    //login_anonymous-false-start
    /**
     * The tv Pass
     */
    private TextView tvPass;
    //login_anonymous-false-end
    /**
     * The cb Laws
     */

    private CheckBox cbLaws;

    /**
     * The ll QQ
     */
    private LinearLayout llQQ;

    /**
     * The ll Wechat
     */
    private LinearLayout llWechat;

    private LinearLayout llFacebook;
    private LinearLayout llTwitter;
    private LinearLayout llWechat1;
    //login_qq-false-start
    /**
     * The Tencent
     */
    private Tencent mTencent;
    //login_qq-false-end
    //login_weChat-false-start
    /**
     * The Wechat
     */
    public static IWXAPI mIwxapi;
    //login_weChat-false-end

    /**
     * The Scope
     */
    private String Scope = "get_user_info,add_t";
    //login_qq-false-start
    /**
     * The IUiListener
     */
    IUiListener listener;
    //login_qq-false-end
    Intent intent;

    /**
     * The GizThirdAccountType
     */
    public static GizThirdAccountType gizThirdAccountType;

    /**
     * The THRED_LOGIN UID&TOKEN
     */
    public static String thirdUid, thirdToken;
    private LinearLayout llInland;
    private LinearLayout llForeign;


    private View viewLine;


    public static enum handler_key {

        /**
         * 登录
         */
        LOGIN,

        /**
         * 自动登录
         */
        AUTO_LOGIN,

        /**
         * 第三方登录
         */
        THRED_LOGIN,

        /**
         * 国外域名登录
         */
        FOREIGN
    }

    /**
     * 与WXEntryActivity共用Handler
     */
    private Handler baseHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                // 登录
                case LOGIN:
                    progressDialog.show();
                    GosDeviceListFragment.loginStatus = 0;
                    GizWifiSDK.sharedInstance()
                            .userLogin(etName.getText().toString(),
                                    etPsw.getText().toString());
                    break;
                // 自动登录
                case AUTO_LOGIN:
                    progressDialog.show();
                    GosDeviceListFragment.loginStatus = 0;
                    GizWifiSDK.sharedInstance().userLogin(
                            spf.getString("UserName", ""),
                            spf.getString("PassWord", ""));
                    break;
                // 第三方登录
                case THRED_LOGIN:
                    progressDialog.show();
                    GosDeviceListFragment.loginStatus = 0;
                    GizWifiSDK.sharedInstance().loginWithThirdAccount(
                            gizThirdAccountType, thirdUid, thirdToken);
                    spf.edit().putString("thirdUid", thirdUid).commit();
                    break;
                case FOREIGN:
                    llForeign.setVisibility(View.VISIBLE);
                    llInland.setVisibility(View.GONE);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_gos_user_login);
        // 设置actionBaru
        //setActionBar(false, false, R.string.app_company);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //push-all-start
        //push_jiguang-false-start
        JPushInterface.onResume(this);
        //push_jiguang-false-end
        //push-all-end
        autoLogin();
        cleanuserthing();
    }


    private void cleanuserthing() {
        if (isclean) {
            etName.setText("");
            etPsw.setText("");
        }
    }

    private void autoLogin() {
        if (TextUtils.isEmpty(spf.getString("UserName", ""))
                || TextUtils.isEmpty(spf.getString("PassWord", ""))) {
            return;
        }
        baseHandler.sendEmptyMessageDelayed(handler_key.AUTO_LOGIN.ordinal(),
                1000);
    }

    private void initView() {
        etName = (EditText) findViewById(R.id.etName);
        etPsw = (EditText) findViewById(R.id.etPsw);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        //register-all-start
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        //register-all-end
        //resetPassword-all-start
        tvForget = (TextView) findViewById(R.id.tvForget);
        //resetPassword-all-end
        //login_anonymous-false-start
        tvPass = (TextView) findViewById(R.id.tvPass);
        //login_anonymous-false-end
        cbLaws = (CheckBox) findViewById(R.id.cbLaws);

        llInland = (LinearLayout) findViewById(R.id.llInland);
        llForeign = (LinearLayout) findViewById(R.id.llForeign);


        //login_qq-false-start
        llQQ = (LinearLayout) findViewById(R.id.llQQ);
        //login_qq-false-end
        //login_qq-false-start  login_qq-true-start
        viewLine = (View) findViewById(R.id.view_line);
        //login_qq-false-end  login_qq-true-end
        //login_weChat-false-start
        llWechat = (LinearLayout) findViewById(R.id.llWechat);
        //login_weChat-false-end
        //login_weChat-false-start
        llWechat1 = (LinearLayout) findViewById(R.id.llWechat1);
        //login_weChat-false-end
        if (!GosDeploy.appConfig_Login_Anonymous()) {
            tvPass.setVisibility(View.GONE);
        }
        if (!GosDeploy.appConfig_Register_PhoneUser()) {
            tvRegister.setVisibility(View.GONE);
        }
        if (!GosDeploy.appConfig_ResetPassword_PhoneUser()) {
            tvForget.setVisibility(View.GONE);
        }
        // 配置文件部署
        btnLogin.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        btnLogin.setTextColor(GosDeploy.appConfig_Contrast());
        //btnLogin.setTextColor( Color.argb(255, 0, 167, 186));
    }

    private void initEvent() {
        btnLogin.setOnClickListener(this);
        //register-all-start
        tvRegister.setOnClickListener(this);
        //register-all-end
        //resetPassword-all-start
        tvForget.setOnClickListener(this);
        //resetPassword-all-end
        //login_anonymous-false-start
        tvPass.setOnClickListener(this);
        //login_anonymous-false-end
        //login_qq-false-start
        llQQ.setOnClickListener(this);
        //login_qq-false-end
        //login_weChat-false-start
        llWechat.setOnClickListener(this);
        //login_weChat-false-end
        //login_weChat-false-start
        llWechat1.setOnClickListener(this);
        //login_weChat-false-end

        //login_qq-true-start  login_qq-false-start
        if (!GosDeploy.appConfig_Login_QQ()) {
            llQQ.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        }
        //login_qq-true-end   login_qq-true-end
        //login_weChat-true-start   login_weChat-false-start
        if (!GosDeploy.appConfig_Login_Wechat()) {
            llWechat.setVisibility(View.GONE);
            llWechat1.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        }
        //login_weChat-false-end  login_weChat-true-end

        cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String psw = etPsw.getText().toString();
                if (isChecked) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
                etPsw.setSelection(psw.length());
            }
        });
        GizWifiSDK.sharedInstance().getCurrentCloudService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    Toast.makeText(GosUserLoginActivity.this,
                            R.string.toast_name_wrong, toastTime).show();
                    return;
                }
                if (TextUtils.isEmpty(etPsw.getText().toString())) {
                    Toast.makeText(GosUserLoginActivity.this,
                            R.string.toast_psw_wrong, toastTime).show();
                    return;
                }
                baseHandler.sendEmptyMessage(handler_key.LOGIN.ordinal());
                break;
            // register-all-start
            case R.id.tvRegister:
                if (ToolUtils.noDoubleClick()) {
                    intent = new Intent(GosUserLoginActivity.this,
                            com.gizwits.opensource.appkit.UserModule.GosRegisterUserActivity.class);
                    startActivity(intent);
                }
                break;
            //register-all-end
            //resetPassword-all-start
            case R.id.tvForget:
                if (ToolUtils.noDoubleClick()) {
                    intent = new Intent(GosUserLoginActivity.this,
                            com.gizwits.opensource.appkit.UserModule.GosForgetPasswordActivity.class);
                    startActivity(intent);
                }
                break;
            //resetPassword-all-end
            //login_anonymous-false-start
            case R.id.tvPass:
                intent = null;
                if (GosDeploy.appConfig_GizwitsInfoAppID() != null && GosDeploy.appConfig_GizwitsInfoAppSecret() != null) {
                }
                if (intent == null) {
                    intent = new Intent(GosUserLoginActivity.this, GosMainActivity.class);
                }
                startActivity(intent);
                break;
            //login_anonymous-false-end
            //login_qq-false-start
            case R.id.llQQ:
                if (ToolUtils.noDoubleClick()) {
                    //login_qq-true-start
                    if (GosDeploy.appConfig_Login_QQ()) {
                        //  login_qq-true-end
                        String tencentAPPID = GosDeploy.appConfig_TencentAppID();
                        if (TextUtils.isEmpty(tencentAPPID)) {
                            noIDAlert(this, R.string.TencentAPPID_Toast);
                            return;
                        } else {
                            // 启动QQ登录SDK
                            mTencent = Tencent.createInstance(GosDeploy.appConfig_TencentAppID(),
                                    this.getApplicationContext());
                        }
                        listener = new BaseUiListener() {
                            protected void doComplete(JSONObject values) {
                                Message msg = new Message();
                                try {
                                    if (values.getInt("ret") == 0) {
                                        gizThirdAccountType = GizThirdAccountType.GizThirdQQ;
                                        thirdUid = values.getString("openid").toString();
                                        thirdToken = values.getString("access_token")
                                                .toString();
                                        msg.what = handler_key.THRED_LOGIN.ordinal();
                                        baseHandler.sendMessage(msg);
                                    } else {
                                        Toast.makeText(GosUserLoginActivity.this,
                                                msg.obj.toString(), toastTime).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        //使用qq登录将此处放开 并且到AndroidManifest 放开activity service
                        mTencent.login(this, Scope, listener);
                        //login_qq-true-start
                    }
                    // login_qq-true-end
                }
                break;
            //login_qq-false-end
            //login_weChat-false-start
            case R.id.llWechat:
                if (ToolUtils.noDoubleClick()) {
                    //  login_weChat-true-start
                    if (GosDeploy.appConfig_Login_Wechat()) {
                        //  login_weChat-true-end
                        String wechatAppID = GosDeploy.appConfig_WechatAppID();
                        String wechatAppSecret = GosDeploy.appConfig_WechatAppSecret();
                        if (TextUtils.isEmpty(wechatAppID)
                                || TextUtils.isEmpty(wechatAppSecret)
                                || wechatAppID.contains("your_wechat_app_id")
                                || wechatAppSecret.contains("your_wechat_app_secret")) {
                            noIDAlert(this, R.string.WechatAppID_Toast);
                            return;
                        } else {
                            // 设置与WXEntryActivity共用Handler
                            setBaseHandler(baseHandler);
                            // 启动微信登录SDK
                            mIwxapi = WXAPIFactory.createWXAPI(this, wechatAppID, false);
                            // 将应用的AppID注册到微信
                            mIwxapi.registerApp(wechatAppID);
                        }
                        if (!(mIwxapi.isWXAppInstalled() && mIwxapi.isWXAppSupportAPI())) {
                            noIDAlert(this, R.string.No_WXApp);
                            return;
                        }
                        SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";
                        req.state = "wechat_sdk_demo";
                        mIwxapi.sendReq(req);
                        //login_weChat-true-start
                    }
                    //  login_weChat-true-end
                }
                break;
            //login_weChat-false-end
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //login_qq-false-start
        if (requestCode == 11101) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        }
        //login_qq-false-end
    }

    /**
     * 设置云端服务回调
     */
    protected void didGetCurrentCloudService(GizWifiErrorCode result,
                                             java.util.concurrent.ConcurrentHashMap<String, String> cloudServiceInfo) {
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            Toast.makeText(this, toastError(result), toastTime).show();
        } else {
            if (cloudServiceInfo != null) {
                String api = cloudServiceInfo.get("openAPIDomain");
                if (api != null) {
                    if (api.equals("usapi.gizwits.com") || api.equals("euapi.gizwits.com")) {
                        baseHandler.sendEmptyMessage(handler_key.FOREIGN.ordinal());
                    }
                }
            }
        }
    }

    /**
     * 用户登录回调
     */
    @Override
    protected void didUserLogin(GizWifiErrorCode result, String uid,
                                String token) {
        progressDialog.cancel();
        Log.i("Apptest", GosDeviceListFragment.loginStatus + "\t" + "User");
        if (GosDeviceListFragment.loginStatus == 4
                || GosDeviceListFragment.loginStatus == 3) {
            return;
        }

        Log.i("Apptest", GosDeviceListFragment.loginStatus + "\t" + "UserLogin");

        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {// 登录失败
            Toast.makeText(GosUserLoginActivity.this, toastError(result),
                    toastTime).show();
        } else {// 登录成功
            GosDeviceListFragment.loginStatus = 1;
            Toast.makeText(GosUserLoginActivity.this,
                    R.string.toast_login_successful, toastTime).show();
            // TODO 绑定推送\
            //push-all-start
            //push_baidu-false-start   push_baidu-true-start
            if (GosDeploy.appConfig_Push_BaiDu()) {
                //push_baidu-true-end
                GosPushManager.pushBindService(uid, token);
                //push_baidu-true-start
            }
            //push_baidu-false-end   push_baidu-true-end
            //push_jiguang-false-start   push_jiguang-true-start
            if (GosDeploy.appConfig_Push_JiGuang()) {
                //push_jiguang-true-end
                GosPushManager.pushBindService(uid, token);
                //push_jiguang-true-start
            }
            //push_jiguang-false-end   push_jiguang-true-end
            //push-all-end
//            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "T45LbeyAo3muOzMHztipttr8");
            if (!TextUtils.isEmpty(etName.getText().toString())
                    && !TextUtils.isEmpty(etPsw.getText().toString())) {
                spf.edit().putString("UserName", etName.getText().toString())
                        .commit();
                spf.edit().putString("PassWord", etPsw.getText().toString())
                        .commit();
            }
            spf.edit().putString("Uid", uid).commit();
            spf.edit().putString("Token", token).commit();

            intent = null;
            if (intent == null) {
                intent = new Intent(GosUserLoginActivity.this,
                        GosMainActivity.class);
            }
            intent.putExtra("ThredLogin", true);
            startActivity(intent);
        }
    }

    /**
     * 解绑推送回调
     *
     * @param result
     */
    protected void didChannelIDUnBind(GizWifiErrorCode result) {
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            Toast.makeText(this, toastError(result), toastTime).show();
        }

        Log.i("Apptest", "UnBind:" + result.toString());
    }

    ;

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click(); // 调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            String doubleClick = (String) getText(R.string.double_click);
            Toast.makeText(this, doubleClick, toastTime).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            this.finish();
            System.exit(0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
