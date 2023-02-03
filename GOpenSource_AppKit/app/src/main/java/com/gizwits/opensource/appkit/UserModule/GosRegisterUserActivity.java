package com.gizwits.opensource.appkit.UserModule;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ArrayAdapter;
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
public class GosRegisterUserActivity extends GosUserModuleBaseActivity implements OnClickListener, AdapterView.OnItemSelectedListener {

    /**
     * The et Name
     */
    private EditText etName;
    //register_phoneUser-false-start
    /**
     * The ll code
     */
    private LinearLayout llCode;

    /**
     * The btn GetCode
     */
    private TextView tvGetCode;

    /**
     * The et Code
     */
    private EditText etCode;
    /**
     * 验证码重发倒计时
     */
    int secondleft = 60;

    /**
     * The timer.
     */
    Timer timer;

    //register_phoneUser-false-end
    /**
     * The et Psw
     */
    private EditText etPsw;

    /**
     * The et ConfirmPsw
     */
    private EditText etConfirmPsw;

    /**
     * The btn Rrgister
     */
    private Button btnRrgister;

    /**
     * 数据变量
     */
    String name, code, psw, confirmpsw;

    private int usertype = 0;

    private enum handler_key {
        //register_phoneUser-false-start
        /**
         * 获取验证码.
         */
        GETCODE,
        /**
         * 手机验证码发送成功.
         */
        SENDSUCCESSFUL,
        //register_phoneUser-false-end

        /**
         * 提示信息
         */
        TOAST,
        /**
         * 倒计时通知
         */
        TICK_TIME,

        /**
         * 注册
         */
        REGISTER,
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handler_key key = handler_key.values()[msg.what];
            switch (key) {
                //register_phoneUser-false-start
                case GETCODE:
                    progressDialog.show();
                    String AppSecret = GosDeploy.appConfig_GizwitsInfoAppSecret();
                    GizWifiSDK.sharedInstance().requestSendPhoneSMSCode(AppSecret, msg.obj.toString());
                    break;
                case SENDSUCCESSFUL:
                    etName.setEnabled(false);
                    etName.setTextColor(getResources().getColor(R.color.text_gray_light));
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
                //register_phoneUser-false-end
                case TOAST:
                    Toast.makeText(GosRegisterUserActivity.this, msg.obj.toString(), toastTime).show();
                    String successfulText = (String) getText(R.string.register_successful);

                    if (msg.obj.toString().equals(successfulText)) {
                        spf.edit().putString("UserName", name).commit();
                        spf.edit().putString("PassWord", psw).commit();
                        isclean = true;
                        finish();
                    }
                    break;
                case REGISTER:
                    progressDialog.show();
                    GizWifiSDK.sharedInstance().registerUser(name, psw, code, GizUserAccountType.GizUserPhone);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_register_user);
        // 设置ActionBar
        setToolBar(true, R.string.register);
        initView();
        initEvent();
    }

    private void initView() {
        etName = (EditText) findViewById(R.id.etName);
        //register_phoneUser-false-start
        tvGetCode = (TextView) findViewById(R.id.tvGetCode);
        etCode = (EditText) findViewById(R.id.etCode);
        llCode = (LinearLayout) findViewById(R.id.llCode);
        //register_phoneUser-false-end
        etPsw = (EditText) findViewById(R.id.etPsw);
        etConfirmPsw = (EditText) findViewById(R.id.etConfirmPsw);
        btnRrgister = (Button) findViewById(R.id.btnRrgister);
        setPhoneOrEmailOrNormalIsVisable();
    }

    private void setPhoneOrEmailOrNormalIsVisable() {
        etName.setHint(getResources().getString(R.string.name_phone));
        usertype = 0;
    }


    private void initEvent() {
        final Timer etTimer = new Timer();
        etTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                etName.requestFocus();
                InputMethodManager imm = (InputMethodManager) etName.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                etTimer.cancel();

            }
        }, 500);
        //register_phoneUser-false-start
        tvGetCode.setOnClickListener(this);
        //register_phoneUser-false-end
        btnRrgister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //register_phoneUser-false-start
            case R.id.tvGetCode:
                name = etName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(GosRegisterUserActivity.this, R.string.toast_name_wrong, toastTime).show();
                    return;
                }
                Message msg = new Message();
                msg.obj = name;
                msg.what = handler_key.GETCODE.ordinal();
                handler.sendMessage(msg);
                break;
            //register_phoneUser-false-end
            case R.id.btnRrgister:
                name = etName.getText().toString();
                //register_phoneUser-false-start
                code = etCode.getText().toString();
                //register_phoneUser-false-end
                psw = etPsw.getText().toString();
                confirmpsw = etConfirmPsw.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(GosRegisterUserActivity.this, R.string.toast_name_wrong, toastTime).show();
                    return;
                }
                if (usertype == 0) {
                    if (code.length() != 6) {
                        Toast.makeText(GosRegisterUserActivity.this, R.string.no_getcode, toastTime).show();
                        return;
                    }
                }
                if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(GosRegisterUserActivity.this, R.string.toast_psw_wrong, toastTime).show();
                    return;
                }

                if (psw.length() < 6) {
                    Toast.makeText(GosRegisterUserActivity.this, R.string.toast_psw_short, toastTime).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmpsw)) {
                    Toast.makeText(GosRegisterUserActivity.this, R.string.toast_psw_confirm, toastTime).show();
                    return;
                }
                if (!psw.equals(confirmpsw)) {
                    Toast.makeText(GosRegisterUserActivity.this, R.string.toast_psw_confirm_failed, toastTime).show();
                    return;
                }

                /*
                 * if (psw.length() < 6) {
                 * Toast.makeText(GosRegisterUserActivity.this,
                 * R.string.toast_psw_short, toastTime).show(); return; }
                 */
                handler.sendEmptyMessage(handler_key.REGISTER.ordinal());
                break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        etName.setText("");
        etName.setEnabled(true);
        etName.setTextColor(getResources().getColor(R.color.text_color));
        //register_phoneUser-false-start
        etCode.setText("");
        //register_phoneUser-false-end
        etPsw.setText("");
        etConfirmPsw.setText("");
        etName.setHint(getResources().getString(R.string.name_phone));
        llCode.setVisibility(View.VISIBLE);
        etName.setInputType(InputType.TYPE_CLASS_PHONE);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    //register_phoneUser-false-start

    /**
     * 手机验证码回调
     */
    @Override
    protected void didRequestSendPhoneSMSCode(GizWifiErrorCode result, String token) {
        progressDialog.cancel();
        Message msg = new Message();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            msg.what = handler_key.TOAST.ordinal();
            msg.obj = toastError(result);
            handler.sendMessage(msg);
        } else {
            handler.sendEmptyMessage(handler_key.SENDSUCCESSFUL.ordinal());
            msg.what = handler_key.TOAST.ordinal();
            String sendSuccessful = (String) getText(R.string.send_successful);
            msg.obj = sendSuccessful;
            handler.sendMessage(msg);
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
    //register_phoneUser-false-end

    /**
     * 用户注册回调
     */
    @Override
    protected void didRegisterUser(GizWifiErrorCode result, String uid, String token) {
        progressDialog.cancel();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            Message msg = new Message();
            msg.what = handler_key.TOAST.ordinal();
            msg.obj = toastError(result);
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = handler_key.TOAST.ordinal();
            String registerSuccessful = (String) getText(R.string.register_successful);
            msg.obj = registerSuccessful;
            handler.sendMessage(msg);
        }
    }


}
