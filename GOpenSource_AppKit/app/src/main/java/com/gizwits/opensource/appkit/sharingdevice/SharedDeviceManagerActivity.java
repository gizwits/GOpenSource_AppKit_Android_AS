package com.gizwits.opensource.appkit.sharingdevice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MenuItem;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizDeviceSharingInfo;
import com.gizwits.gizwifisdk.api.GizUserInfo;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.CommonModule.NoScrollViewPager;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.DateUtil;
import com.gizwits.opensource.appkit.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SharedDeviceManagerActivity extends GosBaseActivity {

    private List<String> tabList;
    private String productname;
    private String deviceid;
    private List<Fragment> myfragmentlist;
    private int viewPagerSelected = 0;
    private boolean isgetsharing;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gos_shared_device_list);

        setToolBar(true, R.string.sharedmanager);
        GosConstant.isEdit = false;
        initData();
        initView();
        token = spf.getString("Token", "");

    }

    @Override
    protected void onResume() {
        super.onResume();

        GizDeviceSharing.setListener(new GizDeviceSharingListener() {

            @Override
            public void didSharingDevice(GizWifiErrorCode result, String deviceID, int sharingID, Bitmap QRCodeImage) {
                super.didSharingDevice(result, deviceID, sharingID, QRCodeImage);

                if (result.ordinal() != 0) {
                    Toast.makeText(SharedDeviceManagerActivity.this, toastError(result), 1).show();
                }
                GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingByMe, deviceid);
            }


            @Override
            public void didModifySharingInfo(GizWifiErrorCode result, int sharingID) {
                super.didModifySharingInfo(result, sharingID);

                if (result.ordinal() != 0) {
                    Toast.makeText(SharedDeviceManagerActivity.this, toastError(result), 1).show();
                }
                GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingByMe, deviceid);

            }

            @Override
            public void didGetDeviceSharingInfos(GizWifiErrorCode result, String deviceID,
                                                 List<GizDeviceSharingInfo> deviceSharingInfos) {
                super.didGetDeviceSharingInfos(result, deviceID, deviceSharingInfos);

                if (deviceSharingInfos != null) {
                    Collections.sort(deviceSharingInfos, new Comparator<GizDeviceSharingInfo>() {

                        @Override
                        public int compare(GizDeviceSharingInfo arg0, GizDeviceSharingInfo arg1) {

                            String updatedAt = DateUtil.utc2Local(arg0.getUpdatedAt());
                            String updatedAt2 = DateUtil.utc2Local(arg1.getUpdatedAt());

                            int diff = (int) DateUtil.getDiff(updatedAt2, updatedAt);

                            return diff;
                        }

                    });
                }
                GosConstant.mydeviceSharingInfos = deviceSharingInfos;
                SharedStateFragment fragment = (SharedStateFragment) myfragmentlist.get(0);
                Message msg = new Message();
                msg.what = 1;
                fragment.handler.sendMessage(msg);
                SharedUserFragment fragment1 = (SharedUserFragment) myfragmentlist.get(1);
                Message msg1 = new Message();
                msg1.what = 1;
                fragment1.handler.sendMessage(msg1);
                if (result.ordinal() != 0) {
                    Toast.makeText(SharedDeviceManagerActivity.this, toastError(result), 1).show();
                }

            }

            @Override
            public void didGetBindingUsers(GizWifiErrorCode result, String deviceID, List<GizUserInfo> bindUsers) {
                super.didGetBindingUsers(result, deviceID, bindUsers);
                GosConstant.mybindUsers = bindUsers;
                SharedUserFragment fragment = (SharedUserFragment) myfragmentlist.get(1);
                SharedUserFragment.myadapter getmyadapter = fragment.getmyadapter();

                if (getmyadapter != null) {
                    getmyadapter.notifyDataSetChanged();
                }
                if (result.ordinal() != 0) {
                    Toast.makeText(SharedDeviceManagerActivity.this, toastError(result), 1).show();
                }

            }

            @Override
            public void didUnbindUser(GizWifiErrorCode result, String deviceID, String guestUID) {
                // TODO Auto-generated method stub
                super.didUnbindUser(result, deviceID, guestUID);

                GizDeviceSharing.getBindingUsers(token, deviceid);

                if (result.ordinal() != 0) {
                    Toast.makeText(SharedDeviceManagerActivity.this, toastError(result), 1).show();
                }
            }

            @Override
            public void didRevokeDeviceSharing(GizWifiErrorCode result, int sharingID) {
                // TODO Auto-generated method stub
                super.didRevokeDeviceSharing(result, sharingID);

                if (result.ordinal() == 0) {
                    GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingByMe, deviceid);
                } else {

                    Toast.makeText(SharedDeviceManagerActivity.this, toastError(result), 1).show();
                }
            }
        });


    }

    // 初始化tab标签中应该显示的文字
    private void initData() {
        productname = getIntent().getStringExtra("productname");
        deviceid = getIntent().getStringExtra("deviceid");
        isgetsharing = getIntent().getBooleanExtra("isgetsharing", false);

        tabList = new ArrayList<String>();
        tabList.add(getResources().getString(R.string.sharedstated));
        tabList.add(getResources().getString(R.string.boundusers));

        SharedStateFragment shared = new SharedStateFragment();

        SharedUserFragment shared1 = new SharedUserFragment();

        myfragmentlist = new ArrayList<Fragment>();

        myfragmentlist.add(shared);
        myfragmentlist.add(shared1);
    }

    private void initView() {

        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(
                R.id.vpi_indicator);

        indicator.setVisibleTabCount(2);
        indicator.setTabItemTitles(tabList);
        NoScrollViewPager vp_shared = (NoScrollViewPager) findViewById(
                R.id.vp_shared_list);

        vp_shared.setNoScroll(true);

        vp_shared.setAdapter(new myFragmentAdapter(getSupportFragmentManager()));

        indicator.setViewPager(vp_shared, 0);
        indicator.setOnPageChangeListener(new ViewPagerIndicator.PageChangeListener() {

            @Override
            public void onPageSelected(int position) {

                viewPagerSelected = position;

                if (position == 0 && isgetsharing) {
                    SharedPreferences spf = getSharedPreferences("set", Context.MODE_PRIVATE);
                    String token = spf.getString("Token", "");
                    GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingByMe, deviceid);

                } else if (position == 1 && isgetsharing) {
                    SharedStateFragment fragment3 = (SharedStateFragment) myfragmentlist.get(0);
                    Message msg = new Message();
                    msg.what = 2;
                    fragment3.handler.sendMessage(msg);
                    SharedPreferences spf = getSharedPreferences("set", Context.MODE_PRIVATE);
                    String token = spf.getString("Token", "");
                    GizDeviceSharing.getBindingUsers(token, deviceid);
                }

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class myFragmentAdapter extends FragmentStatePagerAdapter {

        public myFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {

            Bundle b = new Bundle();
            b.putString("productname", productname);
            b.putString("deviceid", deviceid);

            Fragment fragment = myfragmentlist.get(arg0);

            fragment.setArguments(b);

            return fragment;

        }

        @Override
        public int getCount() {
            return myfragmentlist.size();
        }

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    // 刷新menu的方法
    private void refreshMenu() {
        // 核心是Activity这个方法
        supportInvalidateOptionsMenu();
    }
}
