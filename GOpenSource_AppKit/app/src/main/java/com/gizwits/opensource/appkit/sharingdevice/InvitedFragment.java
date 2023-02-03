package com.gizwits.opensource.appkit.sharingdevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizDeviceSharingInfo;
import com.gizwits.gizwifisdk.api.GizUserInfo;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingStatus;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.DateUtil;

public class InvitedFragment extends Fragment {

    // 定义俩个整形值用来区分当前要显示的是哪个view对象
    // 如果是1的话就共享列表， 2的话就是受邀列表
    private int mytpye = -1;

//	private List<GizDeviceSharingInfo> mydeviceSharingInfos = new ArrayList<GizDeviceSharingInfo>();

    private String token;

    private myadapter myadapter;

    private TextView myview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initdata();
        // 动态找到布局文件，再从这个布局中find出TextView对象
        View contextView = inflater.inflate(R.layout.activity_gos_shared_list, container, false);
        ListView mListView = (ListView) contextView.findViewById(R.id.mysharedlist);
        myview = (TextView) contextView.findViewById(R.id.shareddeviceproductname);
        myview.setText(getString(R.string.no_guest_users));
        if (GosConstant.newmydeviceSharingInfos != null) {
            myadapter = new myadapter();
            mListView.setAdapter(myadapter);
        }


        return contextView;
    }


    public TextView getmyview() {
        return myview;
    }

    // 初始化接口数据
    private void initdata() {
        SharedPreferences spf = getActivity().getSharedPreferences("set", Context.MODE_PRIVATE);
        token = spf.getString("Token", "");
//		GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingToMe, null);
//
//		GizDeviceSharing.setListener(new GizDeviceSharingListener() {
//
//			@Override
//			public void didGetDeviceSharingInfos(GizWifiErrorCode result, String deviceID,
//					List<GizDeviceSharingInfo> deviceSharingInfos) {
//				super.didGetDeviceSharingInfos(result, deviceID, deviceSharingInfos);
//				mydeviceSharingInfos = deviceSharingInfos;
//
//				if (mydeviceSharingInfos.size() == 0) {
//					myview.setVisibility(View.VISIBLE);
//					myview.setText(getResources().getString(R.string.you_have_no_invited_message));
//				} else {
//					myview.setVisibility(View.GONE);
//				}
//				myadapter.notifyDataSetChanged();
//			}
//
//			@Override
//			public void didAcceptDeviceSharing(GizWifiErrorCode result, String sharingID) {
//				super.didAcceptDeviceSharing(result, sharingID);
//
//				SharedDeviceListAcitivity activity = (SharedDeviceListAcitivity) getActivity();
//				if (result.ordinal() != 0) {
//					Toast.makeText(activity, activity.toastError(result), 2).show();
//				}
//
//				GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingToMe, null);
//			}
//
//		});
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // listview的adapter，通过刷新的bound对象的属性值来判断当前应该现实的是什么
    class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return GosConstant.newmydeviceSharingInfos.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = View.inflate(getActivity(), R.layout.gos_shared_to_me_activity, null);

            TextView mess = (TextView) view.findViewById(R.id.mess);

            View redpoint = view.findViewById(R.id.redpoint);

            TextView timemess = (TextView) view.findViewById(R.id.timemess);

            TextView mystatues = (TextView) view.findViewById(R.id.mystatues);

            LinearLayout buttionline = (LinearLayout) view.findViewById(R.id.buttionline);

            TextView accept = (TextView) view.findViewById(R.id.accept);

            TextView refuse = (TextView) view.findViewById(R.id.refuse);


            final GizDeviceSharingInfo gizDeviceSharingInfo = GosConstant.newmydeviceSharingInfos.get(position);

            GizUserInfo userInfo = gizDeviceSharingInfo.getUserInfo();

            String username = userInfo.getUsername();

            String email = userInfo.getEmail();

            String phone = userInfo.getPhone();

            String remark = userInfo.getRemark();
            String uid = userInfo.getUid();


            accept.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    GizDeviceSharing.acceptDeviceSharing(token, gizDeviceSharingInfo.getId(), true);
                }
            });

            refuse.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    GizDeviceSharing.acceptDeviceSharing(token, gizDeviceSharingInfo.getId(), false);
                }
            });
            String passs = getResources().getString(R.string.tomeshareddevice);

            String[] split = passs.split("xxx");

            if (!TextUtils.isEmpty(uid) && !uid.equals("null")) {
                uid = uid.substring(0, 3) + "***" + uid.substring(uid.length() - 4, uid.length());

                if (split.length > 2) {
                    mess.setText(split[0] + uid + split[split.length - 1]);
                } else {

                    mess.setText(uid + split[split.length - 1]);
                }

            }
            if (!TextUtils.isEmpty(email) && !email.equals("null")) {
                if (split.length > 2) {
                    mess.setText(split[0] + email + split[split.length - 1]);
                } else {
                    mess.setText(email + split[split.length - 1]);
                }
            }

            if (!TextUtils.isEmpty(phone) && !phone.equals("null")) {
                if (split.length > 2) {
                    mess.setText(split[0] + phone + split[split.length - 1]);
                } else {

                    mess.setText(phone + split[split.length - 1]);
                }
            }

            if (!TextUtils.isEmpty(username) && !username.equals("null")) {
                if (split.length > 2) {
                    mess.setText(split[0] + username + split[split.length - 1]);
                } else {

                    mess.setText(username + split[split.length - 1]);
                }
            }

            if (!TextUtils.isEmpty(remark) && !remark.equals("null")) {
                if (split.length > 2) {
                    mess.setText(split[0] + remark + split[split.length - 1]);
                } else {

                    mess.setText(remark + split[split.length - 1]);
                }
            }

            GizDeviceSharingStatus status = gizDeviceSharingInfo.getStatus();

            String updatedAt = gizDeviceSharingInfo.getUpdatedAt();
            updatedAt = DateUtil.utc2Local(updatedAt);

            String expiredAt = gizDeviceSharingInfo.getExpiredAt();
            expiredAt = DateUtil.utc2Local(expiredAt);

            timemess.setText(updatedAt + " " + gizDeviceSharingInfo.getProductName());
            int myintstatus = status.ordinal();


            if (myintstatus == 0) {
                String timeByFormat = DateUtil.getCurTimeByFormat("yyyy-MM-dd HH:mm:ss");
                long diff = DateUtil.getDiff(timeByFormat, expiredAt);

                if (diff > 0) {
                    redpoint.setVisibility(View.GONE);
                    mystatues.setVisibility(View.VISIBLE);
                    buttionline.setVisibility(View.GONE);
                    mystatues.setText(getResources().getString(R.string.requsettimeout));
                } else {
                    redpoint.setVisibility(View.VISIBLE);
                    buttionline.setVisibility(View.VISIBLE);
                    mystatues.setVisibility(View.GONE);
                }

            } else {
                redpoint.setVisibility(View.GONE);
                mystatues.setVisibility(View.VISIBLE);
                buttionline.setVisibility(View.GONE);
                if (myintstatus == 1) {
                    mystatues.setText(getResources().getString(R.string.accept));
                } else if (myintstatus == 2) {
                    mystatues.setText(getResources().getString(R.string.refuse));
                } else if (myintstatus == 3) {
                    mystatues.setText(getResources().getString(R.string.cancelled));
                }

            }

            return view;
        }

    }


    public myadapter getmyadapter() {
        return myadapter;
    }

}
