package com.gizwits.opensource.appkit.DeviceModule;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.SettingsModule.GosAboutActivity;
import com.gizwits.opensource.appkit.UserModule.GosUserManager;
import com.gizwits.opensource.appkit.sharingdevice.SharedDeviceListAcitivity;

public class GosSettiingsFragment extends GosBaseFragment implements OnClickListener {

    private static final int SETTINGS = 123;
    /**
     * The ll About
     */
    private LinearLayout llAbout;

    /**
     * The Intent
     */
    Intent intent;

    private LinearLayout usermanager;

    private RelativeLayout lllogin;
    private LinearLayout llDeviceShared;

    private TextView phoneusername;
    private View allView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.activity_gos_settings, container, false);
        initView();
        initEvent();
        return allView;
    }

    private void initView() {
        llAbout = (LinearLayout) allView.findViewById(R.id.llAbout);
        usermanager = (LinearLayout) allView.findViewById(R.id.usermanager);
        lllogin = (RelativeLayout) allView.findViewById(R.id.lllogin);
        phoneusername = (TextView) allView.findViewById(R.id.phoneusername);
        llDeviceShared = (LinearLayout) allView.findViewById(R.id.deviceshared);
    }

    private void initEvent() {
        llAbout.setOnClickListener(this);
        usermanager.setOnClickListener(this);
        lllogin.setOnClickListener(this);
        llDeviceShared.setOnClickListener(this);

        if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
            usermanager.setVisibility(View.VISIBLE);
            lllogin.setVisibility(View.GONE);
            phoneusername.setText(spf.getString("UserName", ""));
        } else if (TextUtils.isEmpty(spf.getString("UserName", "")) && TextUtils.isEmpty(spf.getString("PassWord", ""))
                && !TextUtils.isEmpty(spf.getString("thirdUid", ""))) {
            usermanager.setVisibility(View.VISIBLE);
            String uid = spf.getString("thirdUid", "");

            lllogin.setVisibility(View.GONE);
            String myuid = uid.substring(0, 2) + "***" + uid.substring(uid.length() - 4, uid.length());
            phoneusername.setText(myuid);
        } else {
            usermanager.setVisibility(View.GONE);
            lllogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAbout:
                intent = new Intent(getContext(), GosAboutActivity.class);
                startActivity(intent);
                llAbout.setEnabled(false);
                llAbout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llAbout.setEnabled(true);
                    }
                }, 1000);
                break;

            case R.id.usermanager:
                intent = null;
                if (intent == null) {
                    intent = new Intent(getContext(), GosUserManager.class);
                }
                startActivityForResult(intent, SETTINGS);
                usermanager.setEnabled(false);
                usermanager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        usermanager.setEnabled(true);
                    }
                }, 1000);
                break;
            case R.id.lllogin:
                lllogin.setEnabled(false);
                lllogin.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lllogin.setEnabled(true);
                    }
                }, 1000);
                getActivity().finish();
                break;
            case R.id.deviceshared:
                Intent deviceshared1 = new Intent(getContext(), SharedDeviceListAcitivity.class);
                startActivity(deviceshared1);
                llDeviceShared.setEnabled(false);
                llDeviceShared.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llDeviceShared.setEnabled(true);
                    }
                }, 1000);
                break;
            default:
                break;
        }

    }

}
