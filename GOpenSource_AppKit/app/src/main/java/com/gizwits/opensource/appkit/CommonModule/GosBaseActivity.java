package com.gizwits.opensource.appkit.CommonModule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.utils.ToolUtils;
import java.util.ArrayList;
import java.util.List;

public class GosBaseActivity extends AppCompatActivity {

    /**
     * 设备热点默认密码
     */
    public static String SoftAP_PSW = "123456789";

    /**
     * 设备热点默认前缀
     */
    public static String SoftAP_Start = "XPG-GAgent";

    /**
     * 存储器默认名称
     */
    public static final String SPF_Name = "set";

    /**
     * Toast time
     */
    public int toastTime = 2000;

    /**
     * 存储器
     */
    public SharedPreferences spf;

    /**
     * 等待框
     */
    public ProgressDialog progressDialog;

    /**
     * 标题栏
     */
//    public ActionBar actionBar;

    public Toolbar mToolbar;

    /**
     * 实现WXEntryActivity与GosUserLoginActivity共用
     */
    public static Handler baseHandler;

    public static boolean isclean = false;
    public TextView tvTitle;

    public void setBaseHandler(Handler basehandler) {
        if (null != basehandler) {
            baseHandler = basehandler;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 设置为竖屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        spf = getSharedPreferences(SPF_Name, Context.MODE_PRIVATE);
        // 初始化
        setProgressDialog();
    }

    /**
     * 添加ProductKeys
     *
     * @param productkeys
     * @return
     */
    public List<String> addProductKey(String[] productkeys) {
        List<String> productkeysList = new ArrayList<String>();
        for (String productkey : productkeys) {
            productkeysList.add(productkey);
        }

        return productkeysList;
    }

    /**
     * 设置setToolBar（工具方法*开发用*）
     *
     * @param Title
     */
    public void setToolBar(boolean isIcon, int Title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // 工具栏的背景颜色
        mToolbar.setBackgroundColor(GosDeploy.appConfig_Background());
        SpannableString ssTitle = new SpannableString(this.getString(Title));
        ssTitle.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvTitle.setText(ssTitle);
        if (isIcon) {
            mToolbar.setNavigationIcon(ToolUtils.editIcon(getResources(), R.drawable.common_page_back_button));
        } else {
            mToolbar.setNavigationIcon(null);
        }
    }

    /**
     * 设置setToolBar（工具方法*开发用*）
     *
     * @param isLeft
     * @param Title
     */
    public void setToolBar(boolean isLeft, String Title) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // 工具栏的背景颜色
        mToolbar.setBackgroundColor(GosDeploy.appConfig_Background());
        SpannableString ssTitle = new SpannableString(Title);
        ssTitle.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tvTitle.setText(ssTitle);
        if (isLeft) {
            mToolbar.setNavigationIcon(ToolUtils.editIcon(getResources(), R.drawable.common_page_back_button));
        } else {
            mToolbar.setNavigationIcon(null);
        }
    }


//    /**
//     * 设置ActionBar（工具方法*开发用*）
//     *
//     * @param HBE
//     * @param DSHE
//     * @param Title
//     */
//    public void setActionBar(Boolean HBE, Boolean DSHE, int Title) {
//        SpannableString ssTitle = new SpannableString(this.getString(Title));
//        ssTitle.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle.length(),
//                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        actionBar = getSupportActionBar();// 初始化ActionBar
//        Log.e("TAG", "setActionBar=========: " + actionBar);
//        if (actionBar != null) {
//            actionBar.setBackgroundDrawable(new ColorDrawable(GosDeploy.appConfig_Background()));
//            actionBar.setHomeButtonEnabled(HBE);
////        actionBar.setIcon(R.drawable.back_bt_);
//            // 设置返回按钮的颜色
//            actionBar.setIcon(ToolUtils.editIcon(getResources(), R.drawable.back_bt_));
//            actionBar.setTitle(ssTitle);
//            actionBar.setDisplayShowHomeEnabled(DSHE);
//        }
//
//    }
//
//
//    /**
//     * 设置ActionBar（工具方法*GosAirlinkChooseDeviceWorkWiFiActivity.java中使用*）
//     *
//     * @param HBE
//     * @param DSHE  使左上角图标是否显示
//     * @param Title
//     */
//    public void setActionBar(Boolean HBE, Boolean DSHE, CharSequence Title) {
//        SpannableString ssTitle = new SpannableString(Title);
//        ssTitle.setSpan(new ForegroundColorSpan(GosDeploy.appConfig_Contrast()), 0, ssTitle.length(),
//                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        actionBar = getSupportActionBar();// 初始化ActionBar
//        if (actionBar != null) {
//            actionBar.setBackgroundDrawable(new ColorDrawable(GosDeploy.appConfig_Background()));
//            actionBar.setHomeButtonEnabled(HBE);
//            // 从资源文件中获取图片
//            actionBar.setIcon(ToolUtils.editIcon(getResources(), R.drawable.back_bt_));
//            actionBar.setTitle(ssTitle);
//            actionBar.setDisplayShowHomeEnabled(DSHE);
//        }
//    }

    /**
     * 设置ProgressDialog
     */
    public void setProgressDialog() {
        progressDialog = new ProgressDialog(this);
        String loadingText = getString(R.string.loadingtext);
        progressDialog.setMessage(loadingText);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 设置ProgressDialog
     *
     * @param Message
     * @param Cancelable
     * @param CanceledOnTouchOutside
     */
    public void setProgressDialog(String Message, boolean Cancelable, boolean CanceledOnTouchOutside) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Message);
        progressDialog.setCancelable(Cancelable);
        progressDialog.setCanceledOnTouchOutside(CanceledOnTouchOutside);
    }

    /**
     * 检查网络连通性（工具方法）
     *
     * @param context
     * @return
     */
    public boolean checkNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null && net.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 验证手机格式.（工具方法）
     */
    // public boolean isMobileNO(String mobiles) {
    // /*
    // * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    // * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
    // * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    // */
    // String telRegex = "[1][3578]\\d{9}";//
    // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
    // if (TextUtils.isEmpty(mobiles))
    // return false;
    // else
    // return !mobiles.matches(telRegex);
    // }
    public String toastError(GizWifiErrorCode errorCode) {
        String errorString = (String) getText(R.string.UNKNOWN_ERROR);
        switch (errorCode) {
            case GIZ_SDK_SUCCESS:
                errorString = (String) getText(R.string.GIZ_SDK_SUCCESS);
                break;
            case GIZ_SDK_PARAM_FORM_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_PARAM_FORM_INVALID);
                break;
            case GIZ_SDK_CLIENT_NOT_AUTHEN:
                errorString = (String) getText(R.string.GIZ_SDK_CLIENT_NOT_AUTHEN);
                break;
            case GIZ_SDK_CLIENT_VERSION_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_CLIENT_VERSION_INVALID);
                break;
            case GIZ_SDK_UDP_PORT_BIND_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_UDP_PORT_BIND_FAILED);
                break;
            case GIZ_SDK_DAEMON_EXCEPTION:
                errorString = (String) getText(R.string.GIZ_SDK_DAEMON_EXCEPTION);
                break;
            case GIZ_SDK_PARAM_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_PARAM_INVALID);
                break;
            case GIZ_SDK_APPID_LENGTH_ERROR:
                errorString = (String) getText(R.string.GIZ_SDK_APPID_LENGTH_ERROR);
                break;
            case GIZ_SDK_LOG_PATH_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_LOG_PATH_INVALID);
                break;
            case GIZ_SDK_LOG_LEVEL_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_LOG_LEVEL_INVALID);
                break;
            case GIZ_SDK_UID_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_UID_INVALID);
                break;
            case GIZ_SDK_TOKEN_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_TOKEN_INVALID);
                break;
            case GIZ_SDK_USER_NOT_LOGIN:
                errorString = (String) getText(R.string.GIZ_SDK_USER_NOT_LOGIN);
                break;
            case GIZ_SDK_APPID_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_APPID_INVALID);
                break;
            case GIZ_SDK_APP_SECRET_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_APP_SECRET_INVALID);
                break;
            case GIZ_SDK_PRODUCT_KEY_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_PRODUCT_KEY_INVALID);
                break;
            case GIZ_SDK_PRODUCT_SECRET_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_PRODUCT_SECRET_INVALID);
                break;
            case GIZ_SDK_DEVICE_NOT_IN_LAN:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_NOT_IN_LAN);
                break;
            case GIZ_SDK_PRODUCTKEY_NOT_IN_SPECIAL_LIST:
                errorString = (String) getText(R.string.GIZ_SDK_PRODUCTKEY_NOT_IN_SPECIAL_LIST);
                break;
            case GIZ_SDK_PRODUCTKEY_NOT_RELATED_WITH_APPID:
                errorString = (String) getText(R.string.GIZ_SDK_PRODUCTKEY_NOT_RELATED_WITH_APPID);
                break;
            case GIZ_SDK_NO_AVAILABLE_DEVICE:
                errorString = (String) getText(R.string.GIZ_SDK_NO_AVAILABLE_DEVICE);
                break;
            case GIZ_SDK_DEVICE_CONFIG_SEND_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_CONFIG_SEND_FAILED);
                break;
            case GIZ_SDK_DEVICE_CONFIG_IS_RUNNING:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING);
                break;
            case GIZ_SDK_DEVICE_CONFIG_TIMEOUT:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_CONFIG_TIMEOUT);
                break;
            case GIZ_SDK_DEVICE_DID_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_DID_INVALID);
                break;
            case GIZ_SDK_DEVICE_MAC_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_MAC_INVALID);
                break;
            case GIZ_SDK_SUBDEVICE_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_SUBDEVICE_INVALID);
                break;
            case GIZ_SDK_DEVICE_PASSCODE_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_PASSCODE_INVALID);
                break;
            case GIZ_SDK_DEVICE_NOT_CENTERCONTROL:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_NOT_CENTERCONTROL);
                break;
            case GIZ_SDK_DEVICE_NOT_SUBSCRIBED:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_NOT_SUBSCRIBED);
                break;
            case GIZ_SDK_DEVICE_NO_RESPONSE:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_NO_RESPONSE);
                break;
            case GIZ_SDK_DEVICE_NOT_READY:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_NOT_READY);
                break;
            case GIZ_SDK_DEVICE_NOT_BINDED:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_NOT_BINDED);
                break;
            case GIZ_SDK_DEVICE_CONTROL_WITH_INVALID_COMMAND:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_CONTROL_WITH_INVALID_COMMAND);
                break;
//             case GIZ_SDK_DEVICE_CONTROL_FAILED:
//                errorString= (String) getText(R.string.GIZ_SDK_DEVICE_CONTROL_FAILED);
//                break;
            case GIZ_SDK_DEVICE_GET_STATUS_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_GET_STATUS_FAILED);
                break;
            case GIZ_SDK_DEVICE_CONTROL_VALUE_TYPE_ERROR:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_CONTROL_VALUE_TYPE_ERROR);
                break;
            case GIZ_SDK_DEVICE_CONTROL_VALUE_OUT_OF_RANGE:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_CONTROL_VALUE_OUT_OF_RANGE);
                break;
            case GIZ_SDK_DEVICE_CONTROL_NOT_WRITABLE_COMMAND:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_CONTROL_NOT_WRITABLE_COMMAND);
                break;
            case GIZ_SDK_BIND_DEVICE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_BIND_DEVICE_FAILED);
                break;
            case GIZ_SDK_UNBIND_DEVICE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_UNBIND_DEVICE_FAILED);
                break;
            case GIZ_SDK_DNS_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_DNS_FAILED);
                break;
            case GIZ_SDK_M2M_CONNECTION_SUCCESS:
                errorString = (String) getText(R.string.GIZ_SDK_M2M_CONNECTION_SUCCESS);
                break;
            case GIZ_SDK_SET_SOCKET_NON_BLOCK_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SET_SOCKET_NON_BLOCK_FAILED);
                break;
            case GIZ_SDK_CONNECTION_TIMEOUT:
                errorString = (String) getText(R.string.GIZ_SDK_CONNECTION_TIMEOUT);
                break;
            case GIZ_SDK_CONNECTION_REFUSED:
                errorString = (String) getText(R.string.GIZ_SDK_CONNECTION_REFUSED);
                break;
            case GIZ_SDK_CONNECTION_ERROR:
                errorString = (String) getText(R.string.GIZ_SDK_CONNECTION_ERROR);
                break;
            case GIZ_SDK_CONNECTION_CLOSED:
                errorString = (String) getText(R.string.GIZ_SDK_CONNECTION_CLOSED);
                break;
            case GIZ_SDK_SSL_HANDSHAKE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SSL_HANDSHAKE_FAILED);
                break;
            case GIZ_SDK_DEVICE_LOGIN_VERIFY_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_LOGIN_VERIFY_FAILED);
                break;
            case GIZ_SDK_INTERNET_NOT_REACHABLE:
                errorString = (String) getText(R.string.GIZ_SDK_INTERNET_NOT_REACHABLE);
                break;
            case GIZ_SDK_M2M_CONNECTION_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_M2M_CONNECTION_FAILED);
                break;
            case GIZ_SDK_HTTP_SERVER_NOT_SUPPORT_API:
                errorString = (String) getText(R.string.GIZ_SDK_HTTP_SERVER_NOT_SUPPORT_API);
                break;
            case GIZ_SDK_HTTP_ANSWER_FORMAT_ERROR:
                errorString = (String) getText(R.string.GIZ_SDK_HTTP_ANSWER_FORMAT_ERROR);
                break;
            case GIZ_SDK_HTTP_ANSWER_PARAM_ERROR:
                errorString = (String) getText(R.string.GIZ_SDK_HTTP_ANSWER_PARAM_ERROR);
                break;
            case GIZ_SDK_HTTP_SERVER_NO_ANSWER:
                errorString = (String) getText(R.string.GIZ_SDK_HTTP_SERVER_NO_ANSWER);
                break;
            case GIZ_SDK_HTTP_REQUEST_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_HTTP_REQUEST_FAILED);
                break;
            case GIZ_SDK_OTHERWISE:
                errorString = (String) getText(R.string.GIZ_SDK_OTHERWISE);
                break;
            case GIZ_SDK_MEMORY_MALLOC_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_MEMORY_MALLOC_FAILED);
                break;
            case GIZ_SDK_THREAD_CREATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_THREAD_CREATE_FAILED);
                break;
            case GIZ_SDK_JSON_OBJECT_CREATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_JSON_OBJECT_CREATE_FAILED);
                break;
            case GIZ_SDK_JSON_PARSE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_JSON_PARSE_FAILED);
                break;
            case GIZ_SDK_SCHEDULER_CREATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_CREATE_FAILED);
                break;
            case GIZ_SDK_SCHEDULER_DELETE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_DELETE_FAILED);
                break;
            case GIZ_SDK_SCHEDULER_EDIT_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_EDIT_FAILED);
                break;
            case GIZ_SDK_SCHEDULER_LIST_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_LIST_UPDATE_FAILED);
                break;
            case GIZ_SDK_SCHEDULER_TASK_EDIT_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_TASK_EDIT_FAILED);
                break;
            case GIZ_SDK_SCHEDULER_TASK_LIST_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_TASK_LIST_UPDATE_FAILED);
                break;
            case GIZ_SDK_SCHEDULER_ID_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_ID_INVALID);
                break;
            case GIZ_SDK_SCHEDULER_ENABLE_DISABLE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_ENABLE_DISABLE_FAILED);
                break;
            case GIZ_SDK_SCHEDULER_STATUS_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCHEDULER_STATUS_UPDATE_FAILED);
                break;
            case GIZ_SDK_GROUP_ID_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_GROUP_ID_INVALID);
                break;
            case GIZ_SDK_GROUP_FAILED_DELETE_DEVICE:
                errorString = (String) getText(R.string.GIZ_SDK_GROUP_FAILED_DELETE_DEVICE);
                break;
            case GIZ_SDK_GROUP_FAILED_ADD_DEVICE:
                errorString = (String) getText(R.string.GIZ_SDK_GROUP_FAILED_ADD_DEVICE);
                break;
            case GIZ_SDK_GROUP_PRODUCTKEY_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_GROUP_PRODUCTKEY_INVALID);
                break;
            case GIZ_SDK_GROUP_CREATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_GROUP_CREATE_FAILED);
                break;
            case GIZ_SDK_GROUP_DELETE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_GROUP_FAILED_DELETE_DEVICE);
                break;
            case GIZ_SDK_GROUP_EDIT_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_GROUP_EDIT_FAILED);
                break;
            case GIZ_SDK_GROUP_LIST_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_GROUP_GET_DEVICE_FAILED);
                break;
            case GIZ_SDK_SCENE_CREATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_CREATE_FAILED);
                break;
            case GIZ_SDK_SCENE_DELETE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_DELETE_FAILED);
                break;
            case GIZ_SDK_SCENE_EDIT_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_EDIT_FAILED);
                break;
            case GIZ_SDK_SCENE_LIST_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_LIST_UPDATE_FAILED);
                break;
            case GIZ_SDK_SCENE_ITEM_LIST_EDIT_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_ITEM_LIST_EDIT_FAILED);
                break;
            case GIZ_SDK_SCENE_ITEM_LIST_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_ITEM_LIST_UPDATE_FAILED);
                break;
            case GIZ_SDK_SCENE_ID_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_ID_INVALID);
                break;
            case GIZ_SDK_SCENE_RUN_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_RUN_FAILED);
                break;
            case GIZ_SDK_SCENE_STATUS_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_SCENE_STATUS_UPDATE_FAILED);
                break;
            case GIZ_SDK_JOINT_ACTION_CREATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_JOINT_ACTION_CREATE_FAILED);
                break;
            case GIZ_SDK_JOINT_ACTION_DELETE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_JOINT_ACTION_DELETE_FAILED);
                break;
            case GIZ_SDK_JOINT_ACTION_VER_UNSUPPORTED:
                errorString = (String) getText(R.string.GIZ_SDK_JOINT_ACTION_VER_UNSUPPORTED);
                break;
//            case GIZ_SDK_JOINT_ACTION_INVALID_CONDITION_TYPE:
//                errorString = (String) getText(R.string.GIZ_SDK_JOINT_ACTION_INVALID_CONDITION_TYPE);
//                break;
//            case GIZ_SDK_JOINT_ACTION_INVALID_RESULT_EVENT_TYPE:
//                errorString = (String) getText(R.string.GIZ_SDK_JOINT_ACTION_INVALID_RESULT_EVENT_TYPE);
//                break;
            case GIZ_SDK_DATAPOINT_NOT_DOWNLOAD:
                errorString = (String) getText(R.string.GIZ_SDK_DATAPOINT_NOT_DOWNLOAD);
                break;
            case GIZ_SDK_DATAPOINT_SERVICE_UNAVAILABLE:
                errorString = (String) getText(R.string.GIZ_SDK_DATAPOINT_SERVICE_UNAVAILABLE);
                break;
            case GIZ_SDK_DATAPOINT_PARSE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_DATAPOINT_PARSE_FAILED);
                break;
            // case GIZ_SDK_NOT_INITIALIZED:
            // errorString= (String) getText(R.string.GIZ_SDK_SDK_NOT_INITIALIZED);
            // break;
            case GIZ_SDK_APK_CONTEXT_IS_NULL:
                errorString = (String) getText(R.string.GIZ_SDK_APK_CONTEXT_IS_NULL);
                break;
            case GIZ_SDK_APK_PERMISSION_NOT_SET:
                errorString = (String) getText(R.string.GIZ_SDK_APK_PERMISSION_NOT_SET);
                break;
            case GIZ_SDK_CHMOD_DAEMON_REFUSED:
                errorString = (String) getText(R.string.GIZ_SDK_CHMOD_DAEMON_REFUSED);
                break;
            case GIZ_SDK_EXEC_DAEMON_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_EXEC_DAEMON_FAILED);
                break;
            case GIZ_SDK_EXEC_CATCH_EXCEPTION:
                errorString = (String) getText(R.string.GIZ_SDK_EXEC_CATCH_EXCEPTION);
                break;
            case GIZ_SDK_APPID_IS_EMPTY:
                errorString = (String) getText(R.string.GIZ_SDK_APPID_IS_EMPTY);
                break;
            case GIZ_SDK_UNSUPPORTED_API:
                errorString = (String) getText(R.string.GIZ_SDK_UNSUPPORTED_API);
                break;
            case GIZ_SDK_REQUEST_TIMEOUT:
                errorString = (String) getText(R.string.GIZ_SDK_REQUEST_TIMEOUT);
                break;
            case GIZ_SDK_DAEMON_VERSION_INVALID:
                errorString = (String) getText(R.string.GIZ_SDK_DAEMON_VERSION_INVALID);
                break;
            case GIZ_SDK_PHONE_NOT_CONNECT_TO_SOFTAP_SSID:
                errorString = (String) getText(R.string.GIZ_SDK_PHONE_NOT_CONNECT_TO_SOFTAP_SSID);
                break;
            case GIZ_SDK_DEVICE_CONFIG_SSID_NOT_MATCHED:
                errorString = (String) getText(R.string.GIZ_SDK_DEVICE_CONFIG_SSID_NOT_MATCHED);
                break;
            case GIZ_SDK_NOT_IN_SOFTAPMODE:
                errorString = (String) getText(R.string.GIZ_SDK_NOT_IN_SOFTAPMODE);
                break;
//             case GIZ_SDK_PHONE_WIFI_IS_UNAVAILABLE:
//             errorString= (String)getText(R.string.GIZ_SDK_PHONE_WIFI_IS_UNAVAILABLE);
//             break;
            case GIZ_SDK_RAW_DATA_TRANSMIT:
                errorString = (String) getText(R.string.GIZ_SDK_RAW_DATA_TRANSMIT);
                break;
            case GIZ_SDK_PRODUCT_IS_DOWNLOADING:
                errorString = (String) getText(R.string.GIZ_SDK_PRODUCT_IS_DOWNLOADING);
                break;
            case GIZ_SDK_START_SUCCESS:
                errorString = (String) getText(R.string.GIZ_SDK_START_SUCCESS);
                break;
            case GIZ_SDK_NEED_UPDATE_TO_LATEST:
                errorString = (String) getText(R.string.GIZ_SDK_NEED_UPDATE_TO_LATEST);
                break;
            case GIZ_SDK_ONBOARDING_STOPPED:
                errorString = (String) getText(R.string.GIZ_SDK_ONBOARDING_STOPPED);
                break;
            case GIZ_SDK_ONBOARDING_WIFI_IS_5G:
                errorString = (String) getText(R.string.GIZ_SDK_ONBOARDING_WIFI_IS_5G);
                break;
            case GIZ_SDK_OTA_FIRMWARE_IS_LATEST:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_FIRMWARE_IS_LATEST);
                break;
            case GIZ_SDK_OTA_FIRMWARE_CHECK_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_FIRMWARE_CHECK_UPDATE_FAILED);
                break;
            case GIZ_SDK_OTA_FIRMWARE_DOWNLOAD_OK:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_FIRMWARE_DOWNLOAD_OK);
                break;
            case GIZ_SDK_OTA_FIRMWARE_DOWNLOAD_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_FIRMWARE_DOWNLOAD_FAILED);
                break;
            case GIZ_SDK_OTA_DEVICE_BUSY_IN_UPGRADE:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_DEVICE_BUSY_IN_UPGRADE);
                break;
            case GIZ_SDK_OTA_PUSH_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_PUSH_FAILED);
                break;
            case GIZ_SDK_OTA_FIRMWARE_VERSION_TOO_LOW:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_FIRMWARE_VERSION_TOO_LOW);
                break;
            case GIZ_SDK_OTA_FIRMWARE_CHECK_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_FIRMWARE_CHECK_FAILED);
                break;
            case GIZ_SDK_OTA_UPGRADE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_UPGRADE_FAILED);
                break;
            case GIZ_SDK_OTA_FIRMWARE_VERIFY_SUCCESS:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_FIRMWARE_VERIFY_SUCCESS);
                break;
            case GIZ_SDK_OTA_DEVICE_NOT_SUPPORT:
                errorString = (String) getText(R.string.GIZ_SDK_OTA_DEVICE_NOT_SUPPORT);
                break;
            case GIZ_SDK_WS_HANDSHAKE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_WS_HANDSHAKE_FAILED);
                break;
            case GIZ_SDK_WS_LOGIN_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_WS_LOGIN_FAILED);
                break;
//            case GIZ_SDK_WS_DEVICE_SUBSCRIBE_FAILED:
//                errorString = (String) getText(R.string.GIZ_SDK_WS_DEVICE_SUBSCRIBE_FAILED);
//                break;
//            case GIZ_SDK_WS_DEVICE_UNSUBSCRIBE_FAILED:
//                errorString = (String) getText(R.string.GIZ_SDK_WS_DEVICE_UNSUBSCRIBE_FAILED);
//                break;
            case GIZ_SITE_PRODUCTKEY_INVALID:
                errorString = (String) getText(R.string.GIZ_SITE_PRODUCTKEY_INVALID);
                break;
            case GIZ_SITE_DATAPOINTS_NOT_DEFINED:
                errorString = (String) getText(R.string.GIZ_SITE_DATAPOINTS_NOT_DEFINED);
                break;
            case GIZ_SITE_DATAPOINTS_NOT_MALFORME:
                errorString = (String) getText(R.string.GIZ_SITE_DATAPOINTS_NOT_MALFORME);
                break;
            case GIZ_OPENAPI_MAC_ALREADY_REGISTERED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_MAC_ALREADY_REGISTERED);
                break;
            case GIZ_OPENAPI_PRODUCT_KEY_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_PRODUCT_KEY_INVALID);
                break;
            case GIZ_OPENAPI_APPID_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_APPID_INVALID);
                break;
            case GIZ_OPENAPI_TOKEN_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_TOKEN_INVALID);
                break;
            case GIZ_OPENAPI_USER_NOT_EXIST:
                errorString = (String) getText(R.string.GIZ_OPENAPI_USER_NOT_EXIST);
                break;
            case GIZ_OPENAPI_TOKEN_EXPIRED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_TOKEN_EXPIRED);
                break;
            case GIZ_OPENAPI_M2M_ID_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_M2M_ID_INVALID);
                break;
            case GIZ_OPENAPI_SERVER_ERROR:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SERVER_ERROR);
                break;
            case GIZ_OPENAPI_CODE_EXPIRED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_CODE_EXPIRED);
                break;
            case GIZ_OPENAPI_CODE_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_CODE_INVALID);
                break;
            case GIZ_OPENAPI_SANDBOX_SCALE_QUOTA_EXHAUSTED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SANDBOX_SCALE_QUOTA_EXHAUSTED);
                break;
            case GIZ_OPENAPI_PRODUCTION_SCALE_QUOTA_EXHAUSTED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_PRODUCTION_SCALE_QUOTA_EXHAUSTED);
                break;
            case GIZ_OPENAPI_PRODUCT_HAS_NO_REQUEST_SCALE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_PRODUCT_HAS_NO_REQUEST_SCALE);
                break;
            case GIZ_OPENAPI_DEVICE_NOT_FOUND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_DEVICE_NOT_FOUND);
                break;
            case GIZ_OPENAPI_FORM_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_FORM_INVALID);
                break;
            case GIZ_OPENAPI_DID_PASSCODE_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_DID_PASSCODE_INVALID);
                break;
            case GIZ_OPENAPI_DEVICE_NOT_BOUND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_DEVICE_NOT_BOUND);
                break;
            case GIZ_OPENAPI_PHONE_UNAVALIABLE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_PHONE_UNAVALIABLE);
                break;
            case GIZ_OPENAPI_USERNAME_UNAVALIABLE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_USERNAME_UNAVALIABLE);
                break;
            case GIZ_OPENAPI_USERNAME_PASSWORD_ERROR:
                errorString = (String) getText(R.string.GIZ_OPENAPI_USERNAME_PASSWORD_ERROR);
                break;
            case GIZ_OPENAPI_SEND_COMMAND_FAILED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SEND_COMMAND_FAILED);
                break;
            case GIZ_OPENAPI_EMAIL_UNAVALIABLE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_EMAIL_UNAVALIABLE);
                break;
            case GIZ_OPENAPI_DEVICE_DISABLED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_DEVICE_DISABLED);
                break;
            case GIZ_OPENAPI_FAILED_NOTIFY_M2M:
                errorString = (String) getText(R.string.GIZ_OPENAPI_FAILED_NOTIFY_M2M);
                break;
            case GIZ_OPENAPI_ATTR_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_ATTR_INVALID);
                break;
            case GIZ_OPENAPI_USER_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_USER_INVALID);
                break;
            case GIZ_OPENAPI_FIRMWARE_NOT_FOUND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_FIRMWARE_NOT_FOUND);
                break;
            case GIZ_OPENAPI_JD_PRODUCT_NOT_FOUND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_JD_PRODUCT_NOT_FOUND);
                break;
            case GIZ_OPENAPI_DATAPOINT_DATA_NOT_FOUND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_DATAPOINT_DATA_NOT_FOUND);
                break;
            case GIZ_OPENAPI_SCHEDULER_NOT_FOUND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SCHEDULER_NOT_FOUND);
                break;
            case GIZ_OPENAPI_QQ_OAUTH_KEY_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_QQ_OAUTH_KEY_INVALID);
                break;
            case GIZ_OPENAPI_OTA_SERVICE_OK_BUT_IN_IDLE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_OTA_SERVICE_OK_BUT_IN_IDLE);
                break;
            case GIZ_OPENAPI_BT_FIRMWARE_UNVERIFIED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_BT_FIRMWARE_UNVERIFIED);
                break;
            case GIZ_OPENAPI_BT_FIRMWARE_NOTHING_TO_UPGRADE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SAVE_KAIROSDB_ERROR);
                break;
            case GIZ_OPENAPI_SAVE_KAIROSDB_ERROR:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SAVE_KAIROSDB_ERROR);
                break;
            case GIZ_OPENAPI_EVENT_NOT_DEFINED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_EVENT_NOT_DEFINED);
                break;
            case GIZ_OPENAPI_SEND_SMS_FAILED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SEND_SMS_FAILED);
                break;
//             case GIZ_OPENAPI_APPLICATION_AUTH_INVALID:
//             errorString= (String)
//             getText(R.string.GIZ_OPENAPI_APPLICATION_AUTH_INVALID);
//             break;
            case GIZ_OPENAPI_NOT_ALLOWED_CALL_API:
                errorString = (String) getText(R.string.GIZ_OPENAPI_NOT_ALLOWED_CALL_API);
                break;
            case GIZ_OPENAPI_BAD_QRCODE_CONTENT:
                errorString = (String) getText(R.string.GIZ_OPENAPI_BAD_QRCODE_CONTENT);
                break;
            case GIZ_OPENAPI_REQUEST_THROTTLED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_REQUEST_THROTTLED);
                break;
            case GIZ_OPENAPI_DEVICE_OFFLINE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_DEVICE_OFFLINE);
                break;
            case GIZ_OPENAPI_TIMESTAMP_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_TIMESTAMP_INVALID);
                break;
            case GIZ_OPENAPI_SIGNATURE_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SIGNATURE_INVALID);
                break;
            case GIZ_OPENAPI_DEPRECATED_API:
                errorString = (String) getText(R.string.GIZ_OPENAPI_DEPRECATED_API);
                break;
            case GIZ_OPENAPI_REGISTER_IS_BUSY:
                errorString = (String) getText(R.string.GIZ_OPENAPI_REGISTER_IS_BUSY);
                break;
            case GIZ_OPENAPI_ALTER_PASSWORD_FAILED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_ALTER_PASSWORD_FAILED);
                break;
            case GIZ_OPENAPI_APPID_PK_NOT_RELATION:
                errorString = (String) getText(R.string.GIZ_OPENAPI_APPID_PK_NOT_RELATION);
                break;
            case GIZ_OPENAPI_CALL_INNER_FAILED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_CALL_INNER_FAILED);
                break;
            case GIZ_OPENAPI_DEVICE_SHARING_NOT_ENABLED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_DEVICE_SHARING_NOT_ENABLED);
                break;
            case GIZ_OPENAPI_NOT_FIRST_USER_OF_DEVICE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_NOT_FIRST_USER_OF_DEVICE);
                break;
            case GIZ_OPENAPI_PRODUCT_KEY_AUTHEN_FAULT:
                errorString = (String) getText(R.string.GIZ_OPENAPI_PRODUCT_KEY_AUTHEN_FAULT);
                break;
            case GIZ_OPENAPI_BUSY_NOW:
                errorString = (String) getText(R.string.GIZ_OPENAPI_BUSY_NOW);
                break;
            case GIZ_OPENAPI_TWITTER_CONSUMER_KEY_INVALID:
                errorString = (String) getText(R.string.GIZ_OPENAPI_TWITTER_CONSUMER_KEY_INVALID);
                break;
            case GIZ_OPENAPI_NOT_ALLOW_WEEK_PASSWORD:
                errorString = (String) getText(R.string.GIZ_OPENAPI_NOT_ALLOW_WEEK_PASSWORD);
                break;
//            case GIZ_OPENAPI_CODE_NOT_EXIST:
//                errorString = (String) getText(R.string.GIZ_OPENAPI_CODE_NOT_EXIST);
//                break;
//            case GIZ_OPENAPI_EMAIL_NOT_ACTIVE:
//                errorString = (String) getText(R.string.GIZ_OPENAPI_EMAIL_NOT_ACTIVE);
//                break;
//            case GIZ_OPENAPI_EMAIL_NOT_ENABLE:
//                errorString = (String) getText(R.string.GIZ_OPENAPI_EMAIL_NOT_ENABLE);
//                break;
//            case GIZ_OPENAPI_DEVICE_REGISTER_NOT_FOUND:
//                errorString = (String) getText(R.string.GIZ_OPENAPI_DEVICE_REGISTER_NOT_FOUND);
//                break;
            case GIZ_OPENAPI_CANNOT_SHARE_TO_SELF:
                errorString = (String) getText(R.string.GIZ_OPENAPI_CANNOT_SHARE_TO_SELF);
                break;
            case GIZ_OPENAPI_ONLY_OWNER_CAN_SHARE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_ONLY_OWNER_CAN_SHARE);
                break;
            case GIZ_OPENAPI_NOT_FOUND_GUEST:
                errorString = (String) getText(R.string.GIZ_OPENAPI_NOT_FOUND_GUEST);
                break;
            case GIZ_OPENAPI_GUEST_ALREADY_BOUND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_GUEST_ALREADY_BOUND);
                break;
            case GIZ_OPENAPI_NOT_FOUND_SHARING_INFO:
                errorString = (String) getText(R.string.GIZ_OPENAPI_NOT_FOUND_SHARING_INFO);
                break;
            case GIZ_OPENAPI_NOT_FOUND_THE_MESSAGE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_NOT_FOUND_THE_MESSAGE);
                break;
            case GIZ_OPENAPI_SHARING_IS_WAITING_FOR_ACCEPT:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SHARING_IS_WAITING_FOR_ACCEPT);
                break;
            case GIZ_OPENAPI_SHARING_IS_EXPIRED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SHARING_IS_EXPIRED);
                break;
            case GIZ_OPENAPI_SHARING_IS_COMPLETED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SHARING_IS_COMPLETED);
                break;
            case GIZ_OPENAPI_INVALID_SHARING_BECAUSE_UNBINDING:
                errorString = (String) getText(R.string.GIZ_OPENAPI_INVALID_SHARING_BECAUSE_UNBINDING);
                break;
            case GIZ_OPENAPI_ONLY_OWNER_CAN_BIND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_ONLY_OWNER_CAN_BIND);
                break;
            case GIZ_OPENAPI_ONLY_OWNER_CAN_OPERATE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_ONLY_OWNER_CAN_OPERATE);
                break;
            case GIZ_OPENAPI_SHARING_ALREADY_CANCELLED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_SHARING_ALREADY_CANCELLED);
                break;
            case GIZ_OPENAPI_OWNER_CANNOT_UNBIND_SELF:
                errorString = (String) getText(R.string.GIZ_OPENAPI_OWNER_CANNOT_UNBIND_SELF);
                break;
            case GIZ_OPENAPI_ONLY_GUEST_CAN_CHECK_QRCODE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_ONLY_GUEST_CAN_CHECK_QRCODE);
                break;
            case GIZ_OPENAPI_MESSAGE_ALREADY_DELETED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_MESSAGE_ALREADY_DELETED);
                break;
            case GIZ_OPENAPI_BINDING_NOTIFY_FAILED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_BINDING_NOTIFY_FAILED);
                break;
            case GIZ_OPENAPI_ONLY_SELF_CAN_MODIFY_ALIAS:
                errorString = (String) getText(R.string.GIZ_OPENAPI_ONLY_SELF_CAN_MODIFY_ALIAS);
                break;
            case GIZ_OPENAPI_ONLY_RECEIVER_CAN_MARK_MESSAGE:
                errorString = (String) getText(R.string.GIZ_OPENAPI_ONLY_RECEIVER_CAN_MARK_MESSAGE);
                break;
            case GIZ_OPENAPI_GUEST_NOT_BIND:
                errorString = (String) getText(R.string.GIZ_OPENAPI_GUEST_NOT_BIND);
                break;
            case GIZ_OPENAPI_CANNOT_TRANSFER_OWNER_TO_SELF:
                errorString = (String) getText(R.string.GIZ_OPENAPI_CANNOT_TRANSFER_OWNER_TO_SELF);
                break;
            case GIZ_OPENAPI_TRANSFER_OWNER_TO_LIMIT_GUEST:
                errorString = (String) getText(R.string.GIZ_OPENAPI_TRANSFER_OWNER_TO_LIMIT_GUEST);
                break;
            case GIZ_OPENAPI_RESERVED:
                errorString = (String) getText(R.string.GIZ_OPENAPI_RESERVED);
                break;
            case GIZ_PUSHAPI_BODY_JSON_INVALID:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_BODY_JSON_INVALID);
                break;
            case GIZ_PUSHAPI_DATA_NOT_EXIST:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_DATA_NOT_EXIST);
                break;
            case GIZ_PUSHAPI_NO_CLIENT_CONFIG:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_NO_CLIENT_CONFIG);
                break;
            case GIZ_PUSHAPI_NO_SERVER_DATA:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_NO_SERVER_DATA);
                break;
            case GIZ_PUSHAPI_GIZWITS_APPID_EXIST:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_GIZWITS_APPID_EXIST);
                break;
            case GIZ_PUSHAPI_PARAM_ERROR:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_PARAM_ERROR);
                break;
            case GIZ_PUSHAPI_AUTH_KEY_INVALID:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_AUTH_KEY_INVALID);
                break;
            case GIZ_PUSHAPI_APPID_OR_TOKEN_ERROR:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_APPID_OR_TOKEN_ERROR);
                break;
            case GIZ_PUSHAPI_TYPE_PARAM_ERROR:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_TYPE_PARAM_ERROR);
                break;
            case GIZ_PUSHAPI_ID_PARAM_ERROR:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_ID_PARAM_ERROR);
                break;
            case GIZ_PUSHAPI_APPKEY_SECRETKEY_INVALID:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_APPKEY_SECRETKEY_INVALID);
                break;
            case GIZ_PUSHAPI_CHANNELID_ERROR_INVALID:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_CHANNELID_ERROR_INVALID);
                break;
            case GIZ_PUSHAPI_PUSH_ERROR:
                errorString = (String) getText(R.string.GIZ_PUSHAPI_PUSH_ERROR);
                break;
            case GIZ_SDK_SUBDEVICE_ADD_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_ADD_SUBDEVICE_FAILED);
                break;

            case GIZ_SDK_SUBDEVICE_DELETE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_DELETE_SUBDEVICE_FAILED);
                break;

            case GIZ_SDK_SUBDEVICE_LIST_UPDATE_FAILED:
                errorString = (String) getText(R.string.GIZ_SDK_GET_SUBDEVICES_FAILED);
                break;
            default:
                errorString = (String) getText(R.string.UNKNOWN_ERROR);
                break;
        }
        return errorString;
    }

    /**
     * NoID 提示
     *
     * @param context     当前上下文
     * @param alertTextID 提示内容
     */
    public static void noIDAlert(Context context, int alertTextID) {
        final Dialog dialog = new AlertDialog.Builder(context, R.style.alert_dialog_style)
                .setView(new EditText(context)).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Window window = dialog.getWindow();
        window.setContentView(R.layout.alert_gos_no_id);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        LinearLayout llSure;
        TextView tvAlert;
        llSure = (LinearLayout) window.findViewById(R.id.llSure);
        tvAlert = (TextView) window.findViewById(R.id.tvAlert);
        tvAlert.setText(alertTextID);

        llSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    public void startAlert(final Intent intent, String content) {
        final Dialog dialog = new AlertDialog.Builder(this, R.style.alert_dialog_style)
                .setView(new EditText(this)).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.alert_gos_quit);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        TextView tvContent;
        LinearLayout llNo, llSure;
        llNo = (LinearLayout) window.findViewById(R.id.llNo);
        llSure = (LinearLayout) window.findViewById(R.id.llSure);
        tvContent = (TextView) window.findViewById(R.id.tv_prompt);

        tvContent.setText(content);

        llNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        llSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                startActivity(intent);

            }
        });
    }

    /**
     * 推出提示  AppId  无效
     *
     * @param context 当前上下文
     */
    public void tipAlert(Context context, String content) {
        final Dialog dialog = new AlertDialog.Builder(context, R.style.alert_dialog_style)
                .setView(new EditText(context)).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.alert_gos_tip);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        TextView tvContent;
        LinearLayout llSure;
        llSure = (LinearLayout) window.findViewById(R.id.llSure);
        tvContent = (TextView) window.findViewById(R.id.tvContent);
        tvContent.setText(content);

        llSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


}
