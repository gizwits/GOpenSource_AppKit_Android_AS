package com.gizwits.opensource.appkit.UserModule;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.DeviceModule.GosDeviceListFragment;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.R;

//push-all-start
//push-all-end

public class GosUserManager extends GosBaseActivity {

    private static final int GOSUSERMANAGER = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_usermanager);
        setToolBar(true, R.string.user_management);
        initView();
    }

    private void initView() {
        TextView phoneusernumber = (TextView) findViewById(R.id.phoneusernumber);
        //personalCenter_changePassword-false-start
        LinearLayout changeuserpassword = (LinearLayout) findViewById(R.id.changeuserpassword);
        if (!GosDeploy.appConfig_PersonalCenter_ChangePassword()) {
            changeuserpassword.setVisibility(View.GONE);
        }
        //personalCenter_changePassword-false-end
        if (!TextUtils.isEmpty(spf.getString("UserName", ""))) {
            phoneusernumber.setText(spf.getString("UserName", ""));
        } else {
            //personalCenter_changePassword-false-start
            String uid = spf.getString("thirdUid", "");
            if (!TextUtils.isEmpty(uid) && uid.length() != 0) {
                String myuid = uid.substring(0, 2) + "***" + uid.substring(uid.length() - 4, uid.length());
                phoneusernumber.setText(myuid);
                changeuserpassword.setVisibility(View.GONE);
            }
            //personalCenter_changePassword-false-end
        }

    }

    public void userlogout(View v) {
        setResult(GOSUSERMANAGER);
        logoutToClean();

        Intent intent = new Intent(GosUserManager.this, GosUserLoginActivity.class);
        quitAlert(intent, getString(R.string.exit_login));

    }

    //personalCenter_changePassword-false-start
    public void changeuserpassword(View v) {
        Intent tent = new Intent(this, GosChangeUserPasswordActivity.class);
        startActivity(tent);
    }
    //personalCenter_changePassword-false-end

    private void logoutToClean() {
        //push-all-start
        GosPushManager.pushUnBindService(spf.getString("Token", ""));
        //push-all-end
        spf.edit().putString("UserName", "").commit();
        isclean = true;
        spf.edit().putString("PassWord", "").commit();
        spf.edit().putString("Uid", "").commit();
        spf.edit().putString("Token", "").commit();

        spf.edit().putString("thirdUid", "").commit();

        if (GosDeviceListFragment.loginStatus == 1) {
            GosDeviceListFragment.loginStatus = 0;
        } else {
            GosDeviceListFragment.loginStatus = 4;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                isclean = false;
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isclean = false;
            finish();
            return true;
        }
        return false;
    }

    /**
     * 推出提示
     *
     * @param
     */
    protected void quitAlert(final Intent intent, String content) {
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

}
