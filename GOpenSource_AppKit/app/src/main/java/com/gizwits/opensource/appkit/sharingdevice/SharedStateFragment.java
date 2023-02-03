package com.gizwits.opensource.appkit.sharingdevice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizDeviceSharingInfo;
import com.gizwits.gizwifisdk.api.GizUserInfo;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingStatus;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingType;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingWay;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.AssetsUtils;
import com.gizwits.opensource.appkit.utils.DateUtil;
import com.gizwits.opensource.appkit.view.SlideListView2;


public class SharedStateFragment extends Fragment {

    // 定义俩个整形值用来区分当前要显示的是哪个view对象
    // 如果是1的话就共享列表， 2的话就是受邀列表
    private int mytpye = -1;


    View contextView;
    private String deviceID;
    private myadapter myadapter;

    private String token;

    private LinearLayout addshared;

    private SlideListView2 mListView;

    private String productname;

    private TextView shareddeviceproductname;
    private TextView tvSharedTo;
    private LinearLayout llSharedTo;
    private LinearLayout llAddShared;
    private LinearLayout rename;
    private SharedPreferences spf;
    private LinearLayout cancel;
    private RelativeLayout rlCancel;

    private static final String TAG = "mySharedFragment3";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                // 当GosConstant.mydeviceSharingInfos 在Activity中被赋值后执行
                case 1:
                    if (GosConstant.mydeviceSharingInfos.size() == 0) {
                        String s = getResources().getString(R.string.have_not_been_shared);
                        String ss = s.substring(3, s.length());
                        StringBuilder sb = new StringBuilder(productname);
                        shareddeviceproductname.setText(sb.append(ss));
                        shareddeviceproductname.setVisibility(View.VISIBLE);
                        llSharedTo.setVisibility(View.GONE);
                        rename.setVisibility(View.GONE);
                        llAddShared.setVisibility(View.VISIBLE);
                    } else {
                        tvSharedTo.setText(productname + getResources().getString(R.string.sharedto));
                        llSharedTo.setVisibility(View.VISIBLE);
                        shareddeviceproductname.setVisibility(View.GONE);
                        if (rlCancel.getVisibility() == View.GONE) {
                            rename.setVisibility(View.VISIBLE);
                            llAddShared.setVisibility(View.VISIBLE);
                        }

                    }

                    if (myadapter == null) {
                        myadapter = new myadapter(true);
                        mListView.setAdapter(myadapter);
                    } else {
                        mListView.setAdapter(myadapter);
                    }

                    break;
                case 2:
                    mListView.initSlideMode(SlideListView2.MOD_RIGHT);
                    mListView.setOnItemClickListener(null);
                    rlCancel.setVisibility(View.GONE);
                    llAddShared.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 动态找到布局文件，再从这个布局中find出TextView对象
        contextView = inflater.inflate(R.layout.activity_gos_shared_manage, container, false);
        initView();
        initData();
        initEvent();
        return contextView;
    }

    private void initView() {
        mListView = (SlideListView2) contextView.findViewById(R.id.mysharedlist);
        mListView.initSlideMode(SlideListView2.MOD_RIGHT);
        llAddShared = (LinearLayout) contextView.findViewById(R.id.llAddShared);
        rlCancel = (RelativeLayout) contextView.findViewById(R.id.rlCancel);
        addshared = (LinearLayout) contextView.findViewById(R.id.addshared);
        rename = (LinearLayout) contextView.findViewById(R.id.rename);
        cancel = (LinearLayout) contextView.findViewById(R.id.cancel);
        shareddeviceproductname = (TextView) contextView.findViewById(R.id.shareddeviceproductname);
        tvSharedTo = (TextView) contextView.findViewById(R.id.tvSharedTo);
        llSharedTo = (LinearLayout) contextView.findViewById(R.id.llSharedTo);

    }

    private void initData() {
        Bundle arguments = getArguments();
        productname = arguments.getString("productname");
        spf = getActivity().getSharedPreferences("set", Context.MODE_PRIVATE);
        token = spf.getString("Token", "");
        deviceID = getArguments().getString("deviceid");
    }

    @Override
    public void onResume() {
        super.onResume();
        GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingByMe, deviceID);
    }

    private void initEvent() {
        addshared.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
                    Intent tent = new Intent(getActivity(), addSharedActivity.class);
                    tent.putExtra("productname", productname);
                    tent.putExtra("did", deviceID);
                    startActivity(tent);
                } else {
                    Toast.makeText(getContext(), getString(R.string.please_login), 2000).show();
                }
            }
        });
        rename.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.initSlideMode(SlideListView2.MOD_FORBID);
                llAddShared.setVisibility(View.GONE);
                rlCancel.setVisibility(View.VISIBLE);
                myadapter = new myadapter(false);
                mListView.setAdapter(myadapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        TextView name = (TextView) view.findViewById(R.id.tvDeviceName);

                        final Dialog dialog = new AlertDialog.Builder(getContext(), R.style.edit_dialog_style)
                                .setView(new EditText(getContext())).create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        Window window = dialog.getWindow();
                        window.setContentView(R.layout.alert_gos_rename);
                        WindowManager.LayoutParams layoutParams = window.getAttributes();
                        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                        window.setAttributes(layoutParams);
                        final EditText remarkname = (EditText) window.findViewById(R.id.remarkname);

                        remarkname.setText(name.getText().toString());
                        remarkname.setSelection(name.getText().toString().length());
                        RelativeLayout rlClear = (RelativeLayout) window.findViewById(R.id.rlClear);
                        LinearLayout llyes = (LinearLayout) window.findViewById(R.id.llSure);
                        LinearLayout llno = (LinearLayout) window.findViewById(R.id.llNo);

                        llno.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                dialog.dismiss();
                            }
                        });

                        rlClear.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                remarkname.setText("");
                            }
                        });

                        llyes.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                String token = spf.getString("Token", "");
                                GizDeviceSharingInfo gizDeviceSharingInfo = GosConstant.mydeviceSharingInfos.get(position);
                                GizDeviceSharing.modifySharingInfo(token, gizDeviceSharingInfo.getId(),
                                        remarkname.getText().toString());
                                dialog.dismiss();

                            }
                        });
                    }
                });
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.initSlideMode(SlideListView2.MOD_RIGHT);
                mListView.setOnItemClickListener(null);
                rlCancel.setVisibility(View.GONE);
                llAddShared.setVisibility(View.VISIBLE);
                myadapter = new myadapter(true);
                mListView.setAdapter(myadapter);
            }
        });


    }

    public myadapter getmyadapter() {
        return myadapter;
    }

    public RelativeLayout getRelativeLayout() {
        return rlCancel;
    }

    public SlideListView2 getListview() {
        return mListView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // listview的adapter，通过刷新的bound对象的属性值来判断当前应该现实的是什么
    class myadapter extends BaseAdapter {

        private String uid;

        private boolean isShow;

        @Override
        public int getCount() {
            return GosConstant.mydeviceSharingInfos.size();
        }

        public myadapter(boolean isShow) {
            this.isShow = isShow;
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


            holder.delete2.setVisibility(View.INVISIBLE);

            holder.delete2name.setText(getResources().getString(R.string.cancel_sharing));

            holder.delete3name.setText(getResources().getString(R.string.sharedagain));

            GizDeviceSharingInfo gizDeviceSharingInfo = GosConstant.mydeviceSharingInfos.get(position);

            final int id = gizDeviceSharingInfo.getId();

            final String myid = gizDeviceSharingInfo.getDeviceID();

            GizUserInfo userInfo = gizDeviceSharingInfo.getUserInfo();

            // 更新时间
            String updatedAt = gizDeviceSharingInfo.getUpdatedAt();

            // 超时时间
            String expiredAt = gizDeviceSharingInfo.getExpiredAt();

            uid = userInfo.getUid();

            String email = userInfo.getEmail();

            String phone = userInfo.getPhone();

            String username = userInfo.getUsername();

            String remark = userInfo.getRemark();

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

            updatedAt = DateUtil.utc2Local(updatedAt);

            expiredAt = DateUtil.utc2Local(expiredAt);

            holder.tvDeviceMac.setText(updatedAt);

            GizDeviceSharingStatus status = gizDeviceSharingInfo.getStatus();

            int ordinal = status.ordinal();
            if (isShow) {
                holder.tvDeviceStatus.setVisibility(View.VISIBLE);
            } else {
                holder.tvDeviceStatus.setVisibility(View.GONE);
            }
            switch (ordinal) {
                case 0:

                    String timeByFormat = DateUtil.getCurTimeByFormat("yyyy-MM-dd HH:mm:ss");
                    long diff = DateUtil.getDiff(timeByFormat, expiredAt);

                    if (diff > 0) {
                        holder.delete3name.setText(getResources().getString(R.string.delete));

                        holder.delete2name.setText(getResources().getString(R.string.sharedagain));

                        holder.rlmyhome.setPadding(0, 0, AssetsUtils.diptopx(getContext(), -181), 0);
                        holder.delete2.setVisibility(View.VISIBLE);
                        holder.delete3.setVisibility(View.VISIBLE);
                        holder.delete2.setBackgroundColor(getResources().getColor(R.color.back_gray));
                        holder.delete3.setBackgroundColor(getResources().getColor(R.color.unbind));

                        holder.delete2.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                GizDeviceSharing.sharingDevice(token, myid, GizDeviceSharingWay.GizDeviceSharingByNormal,
                                        GosConstant.mydeviceSharingInfos.get(position).getUserInfo().getUid(),
                                        GizUserAccountType.GizUserOther);
                            }
                        });

                        holder.delete3.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                GizDeviceSharing.revokeDeviceSharing(token, id);
                            }
                        });

                        holder.tvDeviceStatus.setText(getResources().getString(R.string.timeout));
                    } else {
                        holder.delete3.setVisibility(View.GONE);
                        holder.rlmyhome.setPadding(0, 0, AssetsUtils.diptopx(getContext(), -91), 0);
                        holder.delete2.setVisibility(View.VISIBLE);
                        holder.delete2.setBackgroundColor(getResources().getColor(R.color.unbind));
                        holder.delete2name.setText(getResources().getString(R.string.cancel_sharing));
                        final String s = holder.tvDeviceName.getText().toString();
                        holder.delete2.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {

                                quitAlert(getActivity(), s, id);
                            }
                        });
                        holder.tvDeviceStatus.setText(getResources().getString(R.string.waitforaccept));
                    }
                    break;

                case 1:
                    holder.tvDeviceStatus.setText(getResources().getString(R.string.accept));
                    holder.delete3.setVisibility(View.GONE);
                    holder.rlmyhome.setPadding(0, 0, AssetsUtils.diptopx(getContext(), -91), 0);
                    holder.delete2.setVisibility(View.VISIBLE);
                    holder.delete2.setBackgroundColor(getResources().getColor(R.color.unbind));
                    holder.delete2name.setText(getResources().getString(R.string.cancel_sharing));
                    final String s = holder.tvDeviceName.getText().toString();
                    holder.delete2.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            quitAlert(getActivity(), s, id);
                        }
                    });

                    break;

                case 2:
                    holder.rlmyhome.setPadding(0, 0, AssetsUtils.diptopx(getContext(), -181), 0);
                    holder.delete2.setVisibility(View.VISIBLE);
                    holder.delete3.setVisibility(View.VISIBLE);
                    holder.delete2.setBackgroundColor(getResources().getColor(R.color.back_gray));
                    holder.delete3.setBackgroundColor(getResources().getColor(R.color.unbind));
                    holder.tvDeviceStatus.setText(getResources().getString(R.string.refuse));

                    holder.delete3name.setText(getResources().getString(R.string.delete));

                    holder.delete2name.setText(getResources().getString(R.string.sharedagain));


                    holder.delete2.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            GizDeviceSharing.sharingDevice(token, myid, GizDeviceSharingWay.GizDeviceSharingByNormal,
                                    GosConstant.mydeviceSharingInfos.get(position).getUserInfo().getUid(),
                                    GizUserAccountType.GizUserOther);
                        }
                    });

                    holder.delete3.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            GizDeviceSharing.revokeDeviceSharing(token, id);
                        }
                    });

                    // tvDeviceStatus.setText(getResources().getString(R.string.timeout));

                    break;

                case 3:
                    holder.rlmyhome.setPadding(0, 0, AssetsUtils.diptopx(getContext(), -181), 0);
                    holder.delete2.setVisibility(View.VISIBLE);
                    holder.delete3.setVisibility(View.VISIBLE);
                    holder.delete2.setBackgroundColor(getResources().getColor(R.color.back_gray));
                    holder.delete3.setBackgroundColor(getResources().getColor(R.color.unbind));
                    holder.tvDeviceStatus.setText(getResources().getString(R.string.cancelled));

                    holder.delete3name.setText(getResources().getString(R.string.delete));


                    holder.delete2name.setText(getResources().getString(R.string.sharedagain));

                    // delete2name.setGravity(Gravity.CENTER);

                    holder.delete2.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            GizDeviceSharing.sharingDevice(token, myid, GizDeviceSharingWay.GizDeviceSharingByNormal,
                                    GosConstant.mydeviceSharingInfos.get(position).getUserInfo().getUid(),
                                    GizUserAccountType.GizUserOther);
                        }
                    });

                    holder.delete3.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            GizDeviceSharing.revokeDeviceSharing(token, id);
                        }
                    });

                    break;

                default:
                    break;
            }


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

    protected void quitAlert(Context context, String username, final int uid2) {
        final Dialog dialog = new AlertDialog.Builder(getActivity(), R.style.alert_dialog_style)
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
                // GizDeviceSharing.unbindGuestUser(token, deviceID, uid2);

                GizDeviceSharing.revokeDeviceSharing(token, uid2);
                dialog.cancel();
            }
        });
    }

}
