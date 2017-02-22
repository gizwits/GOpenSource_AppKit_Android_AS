package com.gizwits.opensource.appkit.sharingdevice;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizDeviceSharingInfo;
import com.gizwits.gizwifisdk.api.GizUserInfo;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingStatus;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingType;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingWay;
import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.utils.DateUtil;
import com.gizwits.opensource.appkit.view.SlideListView2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class mySharedFragment3 extends Fragment {

	// 定义俩个整形值用来区分当前要显示的是哪个view对象
	// 如果是1的话就共享列表， 2的话就是受邀列表
	private int mytpye = -1;

	private String deviceID;
	private myadapter myadapter;

	private String token;

	private LinearLayout addshared;

	private SlideListView2 mListView;

	private String productname;

	private TextView shareddeviceproductname;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// 动态找到布局文件，再从这个布局中find出TextView对象
		View contextView = inflater.inflate(R.layout.activity_gos_shared_list, container, false);
		mListView = (SlideListView2) contextView.findViewById(R.id.mysharedlist);
		mListView.initSlideMode(SlideListView2.MOD_RIGHT);
		addshared = (LinearLayout) contextView.findViewById(R.id.addshared);

		addshared.setVisibility(View.VISIBLE);

		Bundle arguments = getArguments();
		shareddeviceproductname = (TextView) contextView.findViewById(R.id.shareddeviceproductname);
		productname = arguments.getString("productname");
		if (GosConstant.mydeviceSharingInfos.size() == 0) {
			shareddeviceproductname.setText(getResources().getString(R.string.have_not_been_shared));
		} else {
			shareddeviceproductname.setText(productname + " " + getResources().getString(R.string.sharedto));
		}

		shareddeviceproductname.setVisibility(View.VISIBLE);
		myadapter = new myadapter();
		mListView.setAdapter(myadapter);

		addshared.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent tent = new Intent(getActivity(), addSharedActivity.class);
				tent.putExtra("productname", productname);
				tent.putExtra("did", deviceID);
				startActivity(tent);
			}
		});

		initData();
		return contextView;
	}

	public myadapter getmyadapter() {
		return myadapter;
	}

	public LinearLayout getlinearLayout() {
		return addshared;
	}

	public SlideListView2 getListview() {
		return mListView;
	}

	private void initData() {

		SharedPreferences spf = getActivity().getSharedPreferences("set", Context.MODE_PRIVATE);
		token = spf.getString("Token", "");
		deviceID = getArguments().getString("deviceid");
		GizDeviceSharing.getDeviceSharingInfos(token, GizDeviceSharingType.GizDeviceSharingByMe, deviceID);

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
			return GosConstant.mydeviceSharingInfos.size();
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
			if (GosConstant.mydeviceSharingInfos.size() == 0) {
				shareddeviceproductname.setText(getResources().getString(R.string.have_not_been_shared));
			} else {
				shareddeviceproductname.setText(productname + getResources().getString(R.string.sharedto));
			}
			View view = null;

			view = View.inflate(getActivity(), R.layout.gos_shared_by_me_activity, null);

			final TextView tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);

			RelativeLayout myhomell = (RelativeLayout) view.findViewById(R.id.myhomell);

			TextView tvDeviceMac = (TextView) view.findViewById(R.id.tvDeviceMac);

			TextView tvDeviceStatus = (TextView) view.findViewById(R.id.tvDeviceStatus);

			LinearLayout delete2 = (LinearLayout) view.findViewById(R.id.delete2);

			delete2.setVisibility(View.INVISIBLE);

			TextView delete2name = (TextView) view.findViewById(R.id.delete2name);

			TextView delete3name = (TextView) view.findViewById(R.id.delete3name);

			delete2name.setText(getResources().getString(R.string.cancel_sharing));

			delete3name.setText(getResources().getString(R.string.sharedagain));

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

				tvDeviceName.setText(myuid);

			}

			if (!TextUtils.isEmpty(email) && !email.equals("null")) {

				tvDeviceName.setText(email);

			}

			if (!TextUtils.isEmpty(phone) && !phone.equals("null")) {

				tvDeviceName.setText(phone);

			}

			if (!TextUtils.isEmpty(username) && !username.equals("null")) {

				tvDeviceName.setText(username);

			}

			if (!TextUtils.isEmpty(remark) && !remark.equals("null")) {

				tvDeviceName.setText(remark);

			}

			updatedAt = DateUtil.utc2Local(updatedAt);

			expiredAt = DateUtil.utc2Local(expiredAt);

			tvDeviceMac.setText(updatedAt);

			GizDeviceSharingStatus status = gizDeviceSharingInfo.getStatus();

			int ordinal = status.ordinal();

			switch (ordinal) {
			case 0:

				String timeByFormat = DateUtil.getCurTimeByFormat("yyyy-MM-dd HH:mm:ss");
				long diff = DateUtil.getDiff(timeByFormat, expiredAt);
				delete2.setVisibility(View.VISIBLE);

				if (diff > 0) {
					delete2name.setText(getResources().getString(R.string.delete));

					delete3name.setText(getResources().getString(R.string.sharedagain));

					delete2name.setGravity(Gravity.CENTER);
					delete3name.setGravity(Gravity.CENTER);

					delete2name.setBackgroundColor(getResources().getColor(R.color.tomato));
					delete3name.setBackgroundColor(getResources().getColor(R.color.gray));

					delete3name.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							GizDeviceSharing.sharingDevice(token, myid, GizDeviceSharingWay.GizDeviceSharingByNormal,
									GosConstant.mydeviceSharingInfos.get(position).getUserInfo().getUid(),
									GizUserAccountType.GizUserOther);
						}
					});

					delete2name.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							GizDeviceSharing.revokeDeviceSharing(token, id);
						}
					});

					tvDeviceStatus.setText(getResources().getString(R.string.timeout));
				} else {
					delete3name.setVisibility(View.GONE);
					delete2name.setText(getResources().getString(R.string.cancel_sharing));
					delete2name.setGravity(Gravity.CENTER);
					delete2name.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {

							quitAlert(getActivity(), tvDeviceName.getText().toString(), id);
						}
					});
					tvDeviceStatus.setText(getResources().getString(R.string.waitforaccept));
				}
				break;

			case 1:
				tvDeviceStatus.setText(getResources().getString(R.string.accept));
				delete2.setVisibility(View.VISIBLE);

				delete3name.setVisibility(View.GONE);
				delete2name.setText(getResources().getString(R.string.cancel_sharing));
				delete2name.setGravity(Gravity.CENTER);
				delete2name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						quitAlert(getActivity(), tvDeviceName.getText().toString(), id);
					}
				});

				break;

			case 2:
				delete2.setVisibility(View.VISIBLE);
				tvDeviceStatus.setText(getResources().getString(R.string.refuse));

				delete2name.setText(getResources().getString(R.string.delete));

				delete3name.setText(getResources().getString(R.string.sharedagain));

				delete2name.setGravity(Gravity.CENTER);
				delete3name.setGravity(Gravity.CENTER);

				delete2name.setBackgroundColor(getResources().getColor(R.color.tomato));
				delete3name.setBackgroundColor(getResources().getColor(R.color.gray));

				delete3name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						GizDeviceSharing.sharingDevice(token, myid, GizDeviceSharingWay.GizDeviceSharingByNormal,
								GosConstant.mydeviceSharingInfos.get(position).getUserInfo().getUid(),
								GizUserAccountType.GizUserOther);
					}
				});

				delete2name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						GizDeviceSharing.revokeDeviceSharing(token, id);
					}
				});

				// tvDeviceStatus.setText(getResources().getString(R.string.timeout));

				break;

			case 3:
				delete2.setVisibility(View.VISIBLE);
				tvDeviceStatus.setText(getResources().getString(R.string.cancelled));

				delete2name.setText(getResources().getString(R.string.delete));

				delete2name.setVisibility(View.GONE);

				delete3name.setText(getResources().getString(R.string.sharedagain));

				// delete2name.setGravity(Gravity.CENTER);
				delete3name.setGravity(Gravity.CENTER);

				delete2name.setBackgroundColor(getResources().getColor(R.color.tomato));
				delete3name.setBackgroundColor(getResources().getColor(R.color.tomato));

				delete3name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						GizDeviceSharing.sharingDevice(token, myid, GizDeviceSharingWay.GizDeviceSharingByNormal,
								GosConstant.mydeviceSharingInfos.get(position).getUserInfo().getUid(),
								GizUserAccountType.GizUserOther);
					}
				});

				delete2name.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						GizDeviceSharing.revokeDeviceSharing(token, id);
					}
				});

				break;

			default:
				break;
			}

			if (GosConstant.isEdit) {
				tvDeviceStatus.setVisibility(View.GONE);
			} else {
				tvDeviceStatus.setVisibility(View.VISIBLE);
			}

			return view;
		}

	}

	protected void quitAlert(Context context, String username, final int uid2) {
		final Dialog dialog = new AlertDialog.Builder(getActivity()).setView(new EditText(getActivity())).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_gos_quit);

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
