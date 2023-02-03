package com.gizwits.opensource.appkit.sharingdevice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizUserInfo;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.AssetsUtils;
import com.gizwits.opensource.appkit.utils.DateUtil;
import com.gizwits.opensource.appkit.view.SlideListView2;


public class SharedUserFragment extends Fragment {

    // 定义俩个整形值用来区分当前要显示的是哪个view对象
    // 如果是1的话就共享列表， 2的话就是受邀列表
    private int mytpye = -1;
    private myadapter myadapter;
    private String deviceID;
    private String token;
    private String uid;
    private String productname;
    private TextView shareddeviceproductname;
    private SlideListView2 mListView;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (GosConstant.mydeviceSharingInfos.size() == 0) {
                        String s = getResources().getString(R.string.have_not_been_shared);
                        String ss = s.substring(3, s.length());
                        StringBuilder sb = new StringBuilder(productname);
                        shareddeviceproductname.setText(sb.append(ss));
                        shareddeviceproductname.setVisibility(View.VISIBLE);
                        llSharedTo.setVisibility(View.GONE);
                    } else {
                        tvSharedTo.setText(productname + getResources().getString(R.string.sharedto));
                        llSharedTo.setVisibility(View.VISIBLE);
                        shareddeviceproductname.setVisibility(View.GONE);
                    }
                    myadapter = new myadapter();
                    mListView.setAdapter(myadapter);
                    break;
            }
        }
    };
    private TextView tvSharedTo;
    private LinearLayout llSharedTo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 动态找到布局文件，再从这个布局中find出TextView对象
        View contextView = inflater.inflate(R.layout.activity_gos_shared_list, container, false);
        mListView = (SlideListView2) contextView.findViewById(R.id.mysharedlist);
        mListView.initSlideMode(SlideListView2.MOD_RIGHT);

        shareddeviceproductname = (TextView) contextView.findViewById(R.id.shareddeviceproductname);
        tvSharedTo = (TextView) contextView.findViewById(R.id.tvSharedTo);
        llSharedTo = (LinearLayout) contextView.findViewById(R.id.llSharedTo);
        Bundle arguments = getArguments();
        productname = arguments.getString("productname");


        initdata();
        return contextView;
    }

    public myadapter getmyadapter() {
        return myadapter;
    }

    private void initdata() {
        SharedPreferences spf = getActivity().getSharedPreferences("set", Context.MODE_PRIVATE);
        token = spf.getString("Token", "");
        uid = spf.getString("Uid", "");

        deviceID = getArguments().getString("deviceid");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // listview的adapter，通过刷新的bound对象的属性值来判断当前应该现实的是什么
    class myadapter extends BaseAdapter {

        private String uid;

        @Override
        public int getCount() {
            return GosConstant.mybindUsers.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.gos_shared_by_me_activity, null);
                holder = new ViewHolder();
                holder.rlmyhome = (RelativeLayout) convertView.findViewById(R.id.rlmyhome);
                holder.tvDeviceName = (TextView) convertView.findViewById(R.id.tvDeviceName);
                holder.tvDeviceMac = (TextView) convertView.findViewById(R.id.tvDeviceMac);
                holder.tvDeviceStatus = (TextView) convertView.findViewById(R.id.tvDeviceStatus);
                holder.delete2name = (TextView) convertView.findViewById(R.id.delete2name);
                holder.delete3name = (TextView) convertView.findViewById(R.id.delete3name);
                holder.delete2 = (LinearLayout) convertView.findViewById(R.id.delete2);
                holder.delete3 = (LinearLayout) convertView.findViewById(R.id.delete3);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.rlmyhome.setPadding(0, 0, AssetsUtils.diptopx(getContext(), -90), 0);
            holder.delete3.setVisibility(View.GONE);

            holder.delete2name.setText(getResources().getString(R.string.cancel_sharing));

            holder.delete2.setVisibility(View.VISIBLE);

            holder.delete2name.setText(getResources().getString(R.string.cancel_sharing));

            holder.tvDeviceStatus.setVisibility(View.GONE);

            GizUserInfo userInfo = GosConstant.mybindUsers.get(position);

            uid = userInfo.getUid();

            String email = userInfo.getEmail();

            String phone = userInfo.getPhone();

            String username = userInfo.getUsername();

            String remark = userInfo.getRemark();
            String deviceBindTime = userInfo.getDeviceBindTime();

            deviceBindTime = DateUtil.utc2Local(deviceBindTime);
            holder.tvDeviceMac.setText(deviceBindTime);

            if (!TextUtils.isEmpty(uid) && !uid.equals("null")) {
                String myuid = uid.substring(0, 3) + "***" + uid.substring(uid.length() - 4, uid.length());
                holder.tvDeviceName.setText(myuid);

            }

            if (!TextUtils.isEmpty(email) && !email.equals("null")) {

                holder.tvDeviceName.setText(email);

            }

            if (!TextUtils.isEmpty(phone) && !phone.equals("null")) {

                holder.tvDeviceName.setText(phone);

            }

            if (!TextUtils.isEmpty(username) && !username.equals("null")) {

                holder.tvDeviceName.setText(username);

            }

            if (!TextUtils.isEmpty(remark) && !remark.equals("null")) {

                holder.tvDeviceName.setText(remark);

            }
            final String s = holder.tvDeviceName.getText().toString();
            holder.delete2name.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    quitAlert(getActivity(), s, GosConstant.mybindUsers.get(position).getUid());
                }
            });

            return convertView;
        }

    }

    class ViewHolder {
        TextView tvDeviceName;
        TextView tvDeviceMac;
        TextView tvDeviceStatus;
        TextView delete2name;
        TextView delete3name;
        LinearLayout delete2;
        LinearLayout delete3;
        RelativeLayout rlmyhome;
    }

    protected void quitAlert(Context context, String username, final String uid2) {
        final Dialog dialog = new AlertDialog.Builder(getActivity(),R.style.alert_dialog_style)
                .setView(new EditText(getActivity())).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Window window = dialog.getWindow();
        window.setContentView(R.layout.alert_gos_quit);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        LinearLayout llNo, llSure;
        llNo = (LinearLayout) window.findViewById(R.id.llNo);
        llSure = (LinearLayout) window.findViewById(R.id.llSure);

        TextView view3 = (TextView) window.findViewById(R.id.textView3);
        view3.setVisibility(View.VISIBLE);
        TextView tv = (TextView) window.findViewById(R.id.tv_prompt);

        String userstring = getResources().getString(R.string.deleteuserpremiss);
        String[] split = userstring.split("xxx");

        tv.setText(split[0] + username + split[1]);

        llNo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        llSure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                GizDeviceSharing.unbindUser(token, deviceID, uid2);
                dialog.cancel();
            }
        });
    }

}
