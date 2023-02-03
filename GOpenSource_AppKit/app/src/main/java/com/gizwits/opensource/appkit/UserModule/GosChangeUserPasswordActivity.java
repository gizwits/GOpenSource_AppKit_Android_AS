package com.gizwits.opensource.appkit.UserModule;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.gizwits.opensource.appkit.R;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;

public class GosChangeUserPasswordActivity extends GosBaseActivity {

    private EditText oldpass;
    private EditText newpass;
    private EditText confrimpass;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gos_change_password);
        setToolBar(true, R.string.edit_password);
        initView();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        GizWifiSDK.sharedInstance().setListener(listener);
    }

    GizWifiSDKListener listener = new GizWifiSDKListener() {

        public void didChangeUserPassword(
                com.gizwits.gizwifisdk.enumration.GizWifiErrorCode result) {

            if (result.getResult() == 0) {
                Toast.makeText(GosChangeUserPasswordActivity.this,
                        getResources().getString(R.string.passsuccess), 2000)
                        .show();

                finish();

                spf.edit().putString("PassWord", newpass.getText().toString())
                        .commit();
            } else {

                if (result.getResult() == 9020) {
                    Toast.makeText(GosChangeUserPasswordActivity.this,
                            getResources().getString(R.string.oldpasserror),
                            2000).show();
                } else {
                    Toast.makeText(GosChangeUserPasswordActivity.this,
                            getResources().getString(R.string.passerror), 2000)
                            .show();
                }

            }

        }

        ;
    };
    private CheckBox oldcheck;
    private CheckBox newcheck;
    private CheckBox concheck;

    private void initView() {
        btnConfirm = (Button) findViewById(R.id.btnLogin);
        oldpass = (EditText) findViewById(R.id.oldpass);
        newpass = (EditText) findViewById(R.id.newpass);
        confrimpass = (EditText) findViewById(R.id.confrimpass);
        confrimpass.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        oldpass.setTransformationMethod(PasswordTransformationMethod
                .getInstance());
        newpass.setTransformationMethod(PasswordTransformationMethod
                .getInstance());

        oldcheck = (CheckBox) findViewById(R.id.oldcheck);
        newcheck = (CheckBox) findViewById(R.id.newcheck);
        concheck = (CheckBox) findViewById(R.id.concheck);
// 配置文件部署
        oldcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String psw = oldpass.getText().toString();

                if (isChecked) {
                    // oldpass.setInputType(0x90);
                    oldpass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    // oldpass.setInputType(0x81);
                    oldpass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
                oldpass.setSelection(psw.length());
            }
        });

        newcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String psw = newpass.getText().toString();

                if (isChecked) {
                    // newpass.setInputType(0x90);
                    newpass.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                } else {
                    // newpass.setInputType(0x81);
                    newpass.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
                newpass.setSelection(psw.length());
            }
        });

        concheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                String psw = confrimpass.getText().toString();

                if (isChecked) {
                    // confrimpass.setInputType(0x90);
                    confrimpass
                            .setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());
                } else {
                    // confrimpass.setInputType(0x81);
                    confrimpass
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                }
                confrimpass.setSelection(psw.length());
            }
        });
    }

    public void confirm(View v) {

        if (TextUtils.isEmpty(oldpass.getText().toString())
                || TextUtils.isEmpty(newpass.getText().toString())
                || TextUtils.isEmpty(confrimpass.getText().toString())) {

            if (TextUtils.isEmpty(oldpass.getText().toString())) {

                Toast.makeText(
                        this,
                        getResources().getString(
                                R.string.enter_current_password), 2000).show();

                return;
            }

            if (TextUtils.isEmpty(newpass.getText().toString())) {
                Toast.makeText(this,
                        getResources().getString(R.string.enter_new_password),
                        2000).show();

                return;
            }

            if (TextUtils.isEmpty(confrimpass.getText().toString())) {
                Toast.makeText(
                        this,
                        getResources()
                                .getString(R.string.re_enter_new_password),
                        2000).show();
                return;
            }

        } else {
            //oldpass.getText().length() < 6 ||
            if (newpass.getText().length() < 6 || confrimpass.getText().length() < 6) {
                Toast.makeText(this,
                        getResources().getString(R.string.toast_psw_short),
                        2000).show();
            }

            String npass = newpass.getText().toString();
            String cpass = confrimpass.getText().toString();
            if (npass.equals(cpass)) {
                GizWifiSDK.sharedInstance().changeUserPassword(
                        spf.getString("Token", ""),
                        oldpass.getText().toString(), npass);
            } else {
                Toast.makeText(this,
                        getResources().getString(R.string.nosamepass), 2000)
                        .show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
        }
        return true;
    }
}
