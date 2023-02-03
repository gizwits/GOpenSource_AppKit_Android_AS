package com.gizwits.opensource.appkit.sharingdevice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizDeviceSharingInfo;
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

public class SharedDeviceListAcitivity extends GosBaseActivity {

	private List<String> tabList;
	private List<Fragment> myfragmentlist;
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gos_shared_device_list);

		setToolBar(true, R.string.sharedlist);
		initData();
		initView();
	}

	// 初始化tab标签中应该显示的文字
	private void initData() {

		token = spf.getString("Token", "");

		myfragmentlist = new ArrayList<Fragment>();

		SharedFragment ment1 = new SharedFragment();

		InvitedFragment ment2 = new InvitedFragment();


		myfragmentlist.add(ment1);
		myfragmentlist.add(ment2);

		tabList = new ArrayList<String>();
		tabList.add(getResources().getString(R.string.shared));
		tabList.add(getResources().getString(R.string.invited));
	}

	private void initView() {

		ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(
				R.id.vpi_indicator);

		indicator.setVisibleTabCount(2);
		indicator.setTabItemTitles(tabList);
		NoScrollViewPager vp_shared = (NoScrollViewPager) findViewById(R.id.vp_shared_list);

		vp_shared.setNoScroll(true);

		vp_shared.setAdapter(new myFragmentAdapter(getSupportFragmentManager()));

		indicator.setViewPager(vp_shared, 0);

		indicator.setOnPageChangeListener(new ViewPagerIndicator.PageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				GosConstant.nowPager = position;

				switch (GosConstant.nowPager) {
				case 0:

					break;

				case 1:
					GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingToMe, null);
					break;

				default:
					break;
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

			if (arg0 == 0) {
				SharedFragment shared = new SharedFragment();

				return shared;
			} else {
				InvitedFragment shared = (InvitedFragment) myfragmentlist.get(arg0);

				return shared;

			}

		}

		@Override
		public int getCount() {
			return 2;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		initListener();
	}

	// 初始化接口数据
	private void initListener() {

		GizDeviceSharing.setListener(new GizDeviceSharingListener() {

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

				GosConstant.newmydeviceSharingInfos = deviceSharingInfos;

				InvitedFragment fragment = (InvitedFragment) myfragmentlist.get(1);
				TextView myview = fragment.getmyview();
				if (deviceSharingInfos.size() == 0) {

					myview.setVisibility(View.VISIBLE);
					myview.setText(getResources().getString(R.string.no_guest_users));
				} else {
					myview.setVisibility(View.GONE);
				}

				InvitedFragment.myadapter getmyadapter = fragment.getmyadapter();
				getmyadapter.notifyDataSetChanged();

				if (result.ordinal() != 0) {
					Toast.makeText(SharedDeviceListAcitivity.this, toastError(result), 2).show();
				}


			}

			@Override
			public void didAcceptDeviceSharing(GizWifiErrorCode result, int sharingID) {
				super.didAcceptDeviceSharing(result, sharingID);

				if (result.ordinal() != 0) {
					Toast.makeText(SharedDeviceListAcitivity.this, toastError(result), 2).show();
				}

				GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingToMe, null);
			}

		});
	}

}
