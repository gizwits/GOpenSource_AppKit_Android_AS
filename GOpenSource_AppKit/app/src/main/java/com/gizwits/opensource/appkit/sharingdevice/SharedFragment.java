package com.gizwits.opensource.appkit.sharingdevice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingUserRole;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.R;

import java.util.ArrayList;
import java.util.List;

public class SharedFragment extends Fragment {

    // 定义俩个整形值用来区分当前要显示的是哪个view对象
    // 如果是1的话就共享列表， 2的话就是受邀列表
    private int mytpye = -1;
    private List<GizWifiDevice> list;
    private TextView myview;
    private myadapter myadapter1;
    private ListView mListView;

    private View contextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 动态找到布局文件，再从这个布局中find出TextView对象
        contextView = inflater.inflate(R.layout.activity_gos_shared_list, container, false);

        initView();
        initData();
        initEvent();
        return contextView;
    }

    private void initView() {
        mListView = (ListView) contextView.findViewById(R.id.mysharedlist);
        myview = (TextView) contextView.findViewById(R.id.shareddeviceproductname);
    }


    private void initEvent() {
        myadapter1 = new myadapter();

        mListView.setAdapter(myadapter1);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                GizWifiDevice wifiDevice = list.get(arg2);

                GizDeviceSharingUserRole sharingRole = wifiDevice.getSharingRole();
                int role = sharingRole.ordinal();
                boolean isgetsharing = false;

                if (role == 2) {
                    isgetsharing = true;
                }

                mListView.setEnabled(false);
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setEnabled(true);
                    }
                }, 1000);

                Intent tent = new Intent(getActivity(), SharedDeviceManagerActivity.class);

                GosConstant.mybindUsers.clear();
                GosConstant.mydeviceSharingInfos.clear();
                tent.putExtra("productname", wifiDevice.getProductName());
                tent.putExtra("deviceid", wifiDevice.getDid());
                tent.putExtra("isgetsharing", isgetsharing);
                startActivity(tent);
            }
        });
    }

    // 获取当前的设备列表
    private void initData() {

        list = new ArrayList<GizWifiDevice>();
        List<GizWifiDevice> deviceList = GizWifiSDK.sharedInstance().getDeviceList();

        for (GizWifiDevice gizWifiDevice : deviceList) {

            GizDeviceSharingUserRole sharingRole = gizWifiDevice.getSharingRole();

            if (sharingRole != null) {

                if (sharingRole.ordinal() == 1 || sharingRole.ordinal() == 2) {
                    list.add(gizWifiDevice);
                }
            }

        }

        if (list.size() == 0) {
            myview.setVisibility(View.VISIBLE);
            myview.setText(getResources().getString(R.string.you_have_no_device));

        } else {
            myview.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // listview的adapter，通过刷新的bound对象的属性值来判断当前应该现实的是什么
    class myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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

            View view = convertView;
            Holder holder;

            if (view == null) {
                view = View.inflate(getActivity(), R.layout.item_gos_device_shared_list, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            holder.getTvDeviceName().setText(list.get(position).getProductName());
            holder.getTvDeviceMac().setText(list.get(position).getMacAddress());
            GizDeviceSharingUserRole sharingRole = list.get(position).getSharingRole();
            if (sharingRole.ordinal() == 1) {
                holder.getTvDeviceStatus().setVisibility(View.VISIBLE);
                holder.getTvDeviceStatus().setText(getString(R.string.not_sharing));
            } else if (sharingRole.ordinal() == 2) {
                holder.getTvDeviceStatus().setVisibility(View.GONE);
            }
            return view;
        }

    }

    public myadapter getmyadapter() {
        return myadapter1;
    }

    // 设备列表对应的holder
    class Holder {
        View view;

        public Holder(View view) {
            this.view = view;
        }

        private TextView tvDeviceMac, tvDeviceStatus, tvDeviceName;


        public TextView getTvDeviceMac() {
            if (null == tvDeviceMac) {
                tvDeviceMac = (TextView) view.findViewById(R.id.tvDeviceMac);
            }
            return tvDeviceMac;
        }

        public TextView getTvDeviceStatus() {
            if (null == tvDeviceStatus) {
                tvDeviceStatus = (TextView) view.findViewById(R.id.tvDeviceStatus);
            }
            return tvDeviceStatus;
        }

        public TextView getTvDeviceName() {
            if (null == tvDeviceName) {
                tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
            }
            return tvDeviceName;
        }

    }
}


