package com.gizwits.opensource.appkit.sharingdevice;

import java.util.ArrayList;
import java.util.List;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingUserRole;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosConstant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class mySharedFragment extends Fragment {

	// 定义俩个整形值用来区分当前要显示的是哪个view对象
	// 如果是1的话就共享列表， 2的话就是受邀列表
	private int mytpye = -1;
	private List<GizWifiDevice> list;
	private TextView myview;
	private myadapter myadapter1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// 动态找到布局文件，再从这个布局中find出TextView对象
		View contextView = inflater.inflate(R.layout.activity_gos_shared_list, container, false);
		final ListView mListView = (ListView) contextView.findViewById(R.id.mysharedlist);
		myview = (TextView) contextView.findViewById(R.id.shareddeviceproductname);

		initData();
		myadapter1 = new myadapter();

		mListView.setAdapter(myadapter1);

		mListView.setOnItemClickListener(new OnItemClickListener() {

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

		return contextView;
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

			return view;
		}

	}

	public myadapter getmyadapter() {
		return myadapter1;
	}
}

// 设备列表对应的holder
class Holder {
	View view;

	public Holder(View view) {
		this.view = view;
	}

	private TextView tvDeviceMac, tvDeviceStatus, tvDeviceName;

	private LinearLayout delete2;

	private ImageView imgRight;

	private LinearLayout llLeft, shap, descrip, click;

	public LinearLayout getLlLeft() {
		llLeft = (LinearLayout) view.findViewById(R.id.llLeft);

		return llLeft;
	}

	public LinearLayout getLLShap() {
		shap = (LinearLayout) view.findViewById(R.id.shap);
		return shap;
	}

	public LinearLayout getLLDescrip() {
		descrip = (LinearLayout) view.findViewById(R.id.descrip);
		return descrip;
	}

	public LinearLayout getLLClick() {
		click = (LinearLayout) view.findViewById(R.id.click);
		return click;
	}

	public ImageView getImgRight() {
		if (null == imgRight) {
			imgRight = (ImageView) view.findViewById(R.id.imgRight);
		}
		return imgRight;
	}

	public LinearLayout getDelete2() {
		if (null == delete2) {
			delete2 = (LinearLayout) view.findViewById(R.id.delete2);
		}
		return delete2;
	}

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
