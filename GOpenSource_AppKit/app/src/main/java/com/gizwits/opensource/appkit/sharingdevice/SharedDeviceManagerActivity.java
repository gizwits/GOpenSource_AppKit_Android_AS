package com.gizwits.opensource.appkit.sharingdevice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizDeviceSharingInfo;
import com.gizwits.gizwifisdk.api.GizUserInfo;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.sharingdevice.ViewPagerIndicator.PageChangeListener;
import com.gizwits.opensource.appkit.sharingdevice.mySharedFragment3.myadapter;
import com.gizwits.opensource.appkit.utils.DateUtil;
import com.gizwits.opensource.appkit.view.SlideListView2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SharedDeviceManagerActivity extends GosBaseActivity {

	private List<String> tabList;
	private String productname;
	private String deviceid;
	private List<Fragment> myfragmentlist;
	private int viewPagerSelected = 0;
	private boolean isgetsharing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gos_shared_device_list);

		setActionBar(true, true, R.string.sharedmanager);
		GosConstant.isEdit = false;
		initData();
		initView();

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

				SharedPreferences spf = getSharedPreferences("set", Context.MODE_PRIVATE);
				String token = spf.getString("Token", "");
				GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingByMe, deviceid);

			}

			@Override
			public void didModifySharingInfo(GizWifiErrorCode result, int sharingID) {
				super.didModifySharingInfo(result, sharingID);
				String token = spf.getString("Token", "");

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
				mySharedFragment3 fragment = (mySharedFragment3) myfragmentlist.get(0);
				myadapter getmyadapter = fragment.getmyadapter();

				if (getmyadapter != null) {
					getmyadapter.notifyDataSetChanged();
				}

				if (result.ordinal() != 0) {
					Toast.makeText(SharedDeviceManagerActivity.this, toastError(result), 1).show();
				}

			}

			@Override
			public void didGetBindingUsers(GizWifiErrorCode result, String deviceID, List<GizUserInfo> bindUsers) {
				super.didGetBindingUsers(result, deviceID, bindUsers);
				GosConstant.mybindUsers = bindUsers;
				mySharedFragment4 fragment = (mySharedFragment4) myfragmentlist.get(1);
				mySharedFragment4.myadapter getmyadapter = fragment.getmyadapter();

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

				SharedPreferences spf = getSharedPreferences("set", Context.MODE_PRIVATE);
				String token = spf.getString("Token", "");
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

					if (GosConstant.mydeviceSharingInfos.size() > 0) {

						int position = -1;
						for (int i = 0; i < GosConstant.mydeviceSharingInfos.size(); i++) {

							GizDeviceSharingInfo gizDeviceSharingInfo = GosConstant.mydeviceSharingInfos.get(i);

							if (sharingID == gizDeviceSharingInfo.getId()) {

								position = i;

							}
						}

						if (position != -1) {

							GosConstant.mydeviceSharingInfos.remove(position);

							mySharedFragment3 fragment = (mySharedFragment3) myfragmentlist.get(0);
							myadapter getmyadapter = fragment.getmyadapter();
							getmyadapter.notifyDataSetChanged();
						}
					}
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

		mySharedFragment3 shared = new mySharedFragment3();

		mySharedFragment4 shared1 = new mySharedFragment4();

		myfragmentlist = new ArrayList<Fragment>();

		myfragmentlist.add(shared);
		myfragmentlist.add(shared1);
	}

	private void initView() {

		com.gizwits.opensource.appkit.sharingdevice.ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(
				R.id.vpi_indicator);

		indicator.setVisibleTabCount(2);
		indicator.setTabItemTitles(tabList);
		com.gizwits.opensource.appkit.CommonModule.NoScrollViewPager vp_shared = (com.gizwits.opensource.appkit.CommonModule.NoScrollViewPager) findViewById(
				R.id.vp_shared_list);

		vp_shared.setNoScroll(true);

		vp_shared.setAdapter(new myFragmentAdapter(getSupportFragmentManager()));

		indicator.setViewPager(vp_shared, 0);
		indicator.setOnPageChangeListener(new PageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				viewPagerSelected = position;

				refreshMenu();
				if (position == 0 && isgetsharing) {
					SharedPreferences spf = getSharedPreferences("set", Context.MODE_PRIVATE);
					String token = spf.getString("Token", "");
					GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingByMe, deviceid);

				} else if (position == 1 && isgetsharing) {
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
			// if (arg0 == 0) {
			// mySharedFragment3 shared = new mySharedFragment3();
			// shared.setArguments(b);
			//
			// return shared;
			// } else {
			// mySharedFragment4 shared = new mySharedFragment4();
			// shared.setArguments(b);
			// return shared;
			//
			// }

		}

		@Override
		public int getCount() {
			return myfragmentlist.size();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		switch (viewPagerSelected) {
		case 0:

			if (GosConstant.isEdit) {
				getMenuInflater().inflate(R.menu.shareddevice2, menu);

			} else {
				getMenuInflater().inflate(R.menu.shareddevice, menu);

			}

			break;

		default:
			break;
		}

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case R.id.showhelppage:

			mySharedFragment3 fragment = (mySharedFragment3) myfragmentlist.get(0);
			myadapter getmyadapter = fragment.getmyadapter();
			SlideListView2 listview = fragment.getListview();

			CharSequence title = item.getTitle();
			if (title.equals(getResources().getString(R.string.edit))) {
				item.setTitle(getResources().getString(R.string.cancel));

				fragment.getlinearLayout().setVisibility(View.GONE);
				GosConstant.isEdit = true;
				listview.initSlideMode(SlideListView2.MOD_FORBID);

				listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
						TextView name = (TextView) arg1.findViewById(R.id.tvDeviceName);

						final Dialog dialog = new AlertDialog.Builder(SharedDeviceManagerActivity.this)
								.setView(new EditText(SharedDeviceManagerActivity.this)).create();
						dialog.setCanceledOnTouchOutside(false);
						dialog.show();

						Window window = dialog.getWindow();
						window.setContentView(R.layout.mychanggeremark);

						final EditText remarkname = (EditText) window.findViewById(R.id.remarkname);

						remarkname.setText(name.getText().toString());
						ImageView clearview = (ImageView) window.findViewById(R.id.clearview);
						LinearLayout llyes = (LinearLayout) window.findViewById(R.id.llSure);
						LinearLayout llno = (LinearLayout) window.findViewById(R.id.llNo);

						llno.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								dialog.dismiss();

							}
						});

						clearview.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {

								remarkname.setText("");

							}
						});

						llyes.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								String token = spf.getString("Token", "");
								GizDeviceSharingInfo gizDeviceSharingInfo = GosConstant.mydeviceSharingInfos.get(arg2);
								GizDeviceSharing.modifySharingInfo(token, gizDeviceSharingInfo.getId(),
										remarkname.getText().toString());
								dialog.dismiss();

							}
						});
					}

				});
			} else {
				item.setTitle(getResources().getString(R.string.edit));

				fragment.getlinearLayout().setVisibility(View.VISIBLE);
				listview.initSlideMode(SlideListView2.MOD_RIGHT);
				listview.setOnItemClickListener(null);
				GosConstant.isEdit = false;
			}
			getmyadapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
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
