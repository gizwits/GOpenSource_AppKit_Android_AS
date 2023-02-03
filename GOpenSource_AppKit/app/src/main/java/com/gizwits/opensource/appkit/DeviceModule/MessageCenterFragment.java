package com.gizwits.opensource.appkit.DeviceModule;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizMessage;
import com.gizwits.gizwifisdk.enumration.GizMessageStatus;
import com.gizwits.gizwifisdk.enumration.GizMessageType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.sharingdevice.MsgNoticeActivity;
import com.gizwits.opensource.appkit.sharingdevice.deviceSharedMessageActivity;

import java.util.List;

//personalCenter_deviceSharing-false-start
//personalCenter_deviceSharing-false-end

public class MessageCenterFragment extends GosBaseFragment {


    private LinearLayout llGizwitsmes;
    //personalCenter_deviceSharing-false-start
    private View redpoint;
    private LinearLayout llDevicesShared;
    //personalCenter_deviceSharing-false-end
    private View allView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.activity_gos_message, container, false);
        initView();
        initEvent();
        return allView;
    }

    //界面可见时再加载数据
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //请求网络数据
            //personalCenter_deviceSharing-false-start
            String token = spf.getString("Token", "");
            GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSharing);
            //GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSystem);

            GizDeviceSharing.setListener(new GizDeviceSharingListener() {

                @Override
                public void didQueryMessageList(GizWifiErrorCode result, List<GizMessage> messageList) {
                    super.didQueryMessageList(result, messageList);

                    if (messageList.size() > 0) {
                        isShowRedPoint(messageList);
                    } else {
                        redpoint.setVisibility(View.GONE);
                    }

                    if (result.ordinal() != 0) {
                        Toast.makeText(getContext(), toastError(result), 2).show();
                    }
                }
            });
            //personalCenter_deviceSharing-false-end
        }
    }

    //personalCenter_deviceSharing-false-start
    private void isShowRedPoint(List<GizMessage> messageList) {

        boolean isshow = false;
        redpoint.setVisibility(View.GONE);
        for (int i = 0; i < messageList.size(); i++) {

            GizMessage gizMessage = messageList.get(i);
            GizMessageStatus status = gizMessage.getStatus();
            if (status.ordinal() == 0) {
                isshow = true;
                redpoint.setVisibility(View.VISIBLE);
            }
        }
    }
    //personalCenter_deviceSharing-false-end

    private void initView() {
        llGizwitsmes = (LinearLayout) allView.findViewById(R.id.gizwitsmes);
        //personalCenter_deviceSharing-false-start
        // 判断当前的view 是否需要显示这个红点
        redpoint = allView.findViewById(R.id.redpoint);
        llDevicesShared = (LinearLayout) allView.findViewById(R.id.deviceshared);
        //personalCenter_deviceSharing-false-end
    }

    private void initEvent() {
        llGizwitsmes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到机智云公告页面
                Intent intent = new Intent(getContext(), MsgNoticeActivity.class);
                startActivity(intent);
                llGizwitsmes.setEnabled(false);
                llGizwitsmes.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llGizwitsmes.setEnabled(true);
                    }
                }, 1000);
            }
        });
        //personalCenter_deviceSharing-false-start
        llDevicesShared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tent = new Intent(getContext(), deviceSharedMessageActivity.class);
                startActivity(tent);
                llDevicesShared.setEnabled(false);
                llDevicesShared.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        llDevicesShared.setEnabled(true);
                    }
                }, 1000);
            }
        });
        //personalCenter_deviceSharing-false-end
    }

}
