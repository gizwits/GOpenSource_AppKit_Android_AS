package com.gizwits.opensource.appkit.UserModule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.opensource.appkit.R;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressLint("HandlerLeak")
public class GosForgetPasswordActivity extends GosUserModuleBaseActivity implements
        OnClickListener {

    /**
     * The et Name
     */
    private EditText etName;
    /**
     * The ll phone
     */
    private LinearLayout llPhone;

    /**
     * The btn GetCode
     */
    private TextView tvGetCode;

    /**
     * The et Code
     */
    private EditText etCode;

    /**
     * The et Psw
     */
    private EditText etPsw;

    /**
     * The et ConfirmPsw
     */
    private EditText etConfirmPsw;
    /**
     * 验证码重发倒计时
     */
    int secondleft = 60;

    /**
     * The timer.
     */
    Timer timer;
    //resetPassword_phoneUser-false-end
    /**
     * The btn Register
     */
    private Button btnReset;

    /**
     * 数据变量
     */
    String name, code, psw, confirmpsw;


    private enum handler_key {
        //resetPassword_phoneUser-false-start
        /**
         * 获取验证码.
         */
        GETCODE,

        /**
         * 手机验证码发送成功.
         */
        SENDSUCCESSFUL,

        /**
         * 倒计时通知
         */
        TICK_TIME,
        //resetPassword_phoneUser-false-end

        /**
         * 提示信息
         */
        TOAST,

        /**
         * 重置密码
         */
        RESET,
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                //resetPassword_phoneUser-false-start
                case GETCODE:
                    progressDialog.show();
                    String AppSecret = GosDeploy.appConfig_GizwitsInfoAppSecret();
                    GizWifiSDK.sharedInstance().requestSendPhoneSMSCode(AppSecret,
                            msg.obj.toString());
                    break;
                case SENDSUCCESSFUL:
                    etName.setEnabled(false);
                    etName.setTextColor(getResources().getColor(
                            R.color.text_gray_light));
                    isStartTimer();

                    break;

                case TICK_TIME:
                    String getCodeAgain = getString(R.string.getcode_again);
                    String timerMessage = getString(R.string.timer_message);
                    secondleft--;
                    if (secondleft <= 0) {
                        timer.cancel();
                        tvGetCode.setTextColor(getResources().getColor(R.color.tomato));
                        tvGetCode.setEnabled(true);
                        tvGetCode.setText(getCodeAgain);
                    } else {
                        tvGetCode.setText(secondleft + timerMessage);
                    }
                    break;
                //resetPassword_phoneUser-false-end
                case TOAST:
                    String successfulText = (String) getText(R.string.reset_successful);
                    Toast.makeText(GosForgetPasswordActivity.this, msg.obj + "",
                            toastTime).show();
                    if (msg.obj.toString().equals(successfulText)) {
                        //	spf.edit().putString("UserName", name).commit();
                        //	spf.edit().putString("PassWord", psw).commit();
                        isclean = true;
                        //resetPassword_phoneUser-false-start
                        if (llPhone.getVisibility() == View.GONE) {
                            //resetPassword_phoneUser-false-end
                            Intent intent = new Intent(GosForgetPasswordActivity.this, GosSendEmailPasswordActivity.class);
                            startActivity(intent);
                            spf.edit().putString("Email", name).commit();
                            //resetPassword_phoneUser-false-start
                        } else {
                            finish();
                        }
                        //resetPassword_phoneUser-false-end
                    }
                    break;
                case RESET:
                    progressDialog.show();
                        GizWifiSDK.sharedInstance().resetPassword(name, code, psw,
                                GizUserAccountType.GizUserPhone);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_forget_password);
        // 设置ActionBar
        setToolBar(true, R.string.forget_pass);

        initView();
        initEvent();
    }

    private void initView() {
        etName = (EditText) findViewById(R.id.etName);
        //resetPassword_phoneUser-false-start
        llPhone = (LinearLayout) findViewById(R.id.llPhone);
        tvGetCode = (TextView) findViewById(R.id.tvGetCode);
        etCode = (EditText) findViewById(R.id.etCode);
        etPsw = (EditText) findViewById(R.id.etPsw);
        etConfirmPsw = (EditText) findViewById(R.id.etConfirmPsw);
        //resetPassword_phoneUser-false-end
        btnReset = (Button) findViewById(R.id.btnReset);
        setPhoneOrEmailIsVisable();
    }

    private void setPhoneOrEmailIsVisable() {
        //resetPassword_phoneUser-true-start  resetPassword_phoneUser-false-start
        if (GosDeploy.appConfig_ResetPassword_PhoneUser()) {
            //resetPassword_phoneUser-true-end
                etName.setHint(getResources().getString(R.string.name_phone));
                llPhone.setVisibility(View.VISIBLE);
                etName.setInputType(InputType.TYPE_CLASS_PHONE);
                btnReset.setText(getResources().getString(R.string.reset));
            //resetPassword_phoneUser-true-start
        } else {
            //resetPassword_phoneUser-false-end
            //resetPassword_phoneUser-false-start
        }
        //resetPassword_phoneUser-true-end  resetPassword_phoneUser-false-end
    }

    private void initEvent() {

        final Timer etTimer = new Timer();
        etTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                etName.requestFocus();
                InputMethodManager imm = (InputMethodManager) etName
                        .getContext().getSystemService(
                                Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                etTimer.cancel();

            }
        }, 500);
        //resetPassword_phoneUser-false-start
        tvGetCode.setOnClickListener(this);
        //resetPassword_phoneUser-false-end
        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //resetPassword_phoneUser-false-start
            case R.id.tvGetCode:
                name = etName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(GosForgetPasswordActivity.this,
                            R.string.toast_name_wrong, toastTime).show();
                    return;
                }
                Message msg = new Message();
                msg.obj = name;
                msg.what = handler_key.GETCODE.ordinal();
                handler.sendMessage(msg);
                break;
            //resetPassword_phoneUser-false-end
            case R.id.btnReset:
                name = etName.getText().toString();
                //resetPassword_phoneUser-false-start
                code = etCode.getText().toString();
                psw = etPsw.getText().toString();
                confirmpsw = etConfirmPsw.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(GosForgetPasswordActivity.this,
                            R.string.toast_name_wrong, toastTime).show();
                    return;
                }

                if (llPhone.getVisibility() == View.VISIBLE) {
                    if (code.length() != 6) {
                        Toast.makeText(GosForgetPasswordActivity.this, R.string.no_getcode, toastTime).show();
                        return;
                    }
                    if (TextUtils.isEmpty(psw)) {
                        Toast.makeText(GosForgetPasswordActivity.this,
                                R.string.toast_psw_wrong, toastTime).show();
                        return;
                    }
                    if (psw.length() < 6) {
                        Toast.makeText(GosForgetPasswordActivity.this,
                                R.string.toast_psw_short, toastTime).show();
                        return;
                    }
                    if (!psw.equals(confirmpsw)) {
                        Toast.makeText(GosForgetPasswordActivity.this, R.string.toast_psw_confirm_failed, toastTime).show();
                        return;
                    }
                } else {
                    //resetPassword_phoneUser-false-end
                    if (!name.contains("@")) {
                        Toast.makeText(GosForgetPasswordActivity.this, R.string.toase_name_email_fault, toastTime).show();
                        return;
                    }
                    //resetPassword_phoneUser-false-start
                }
                //resetPassword_phoneUser-false-end

                /*
                 * if (psw.length() < 6) {
                 * Toast.makeText(GosForgetPasswordActivity.this,
                 * R.string.toast_psw_short, toastTime).show(); return; }
                 */
                handler.sendEmptyMessage(handler_key.RESET.ordinal());

                break;
        }
    }


    /**
     * 倒计时
     */
    public void isStartTimer() {
        tvGetCode.setEnabled(false);
        tvGetCode.setTextColor(getResources().getColor(R.color.hint_color));
        secondleft = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(handler_key.TICK_TIME.ordinal());
            }
        }, 1000, 1000);
    }

    /**
     * 手机验证码回调
     */
    @Override
    protected void didRequestSendPhoneSMSCode(GizWifiErrorCode result,
                                              String token) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        Message msg = new Message();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            // String sendFailed = (String) getText(R.string.send_failed);
            msg.what = handler_key.TOAST.ordinal();
            msg.obj = toastError(result);// sendFailed + "\n" +
            // errorMessage;toastError(errorCode)
            handler.sendMessage(msg);
        } else {
            handler.sendEmptyMessage(handler_key.SENDSUCCESSFUL.ordinal());
            msg.what = handler_key.TOAST.ordinal();
            String sendSuccessful = (String) getText(R.string.send_successful);
            msg.obj = sendSuccessful;
            handler.sendMessage(msg);
        }
    }
    //resetPassword_phoneUser-false-end

    /**
     * 重置密码回调
     */
    @Override
    public void didChangeUserPassword(GizWifiErrorCode result) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        Message msg = new Message();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            // String resetFailed = (String) getText(R.string.reset_failed);
            msg.what = handler_key.TOAST.ordinal();
            msg.obj = toastError(result);// resetFailed + "\n" + errorMessage;
            handler.sendMessage(msg);
        } else {
            msg.what = handler_key.TOAST.ordinal();
            String resetSuccessful = (String) getText(R.string.reset_successful);
            msg.obj = resetSuccessful;
            handler.sendMessage(msg);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog = null;
    }
}
