package com.gizwits.opensource.appkit.sharingdevice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizMessage;
import com.gizwits.gizwifisdk.enumration.GizMessageStatus;
import com.gizwits.gizwifisdk.enumration.GizMessageType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.CommonModule.GosBaseActivity;
import com.gizwits.opensource.appkit.utils.DateUtil;
import com.gizwits.opensource.appkit.view.SlideListView2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class deviceSharedMessageActivity extends GosBaseActivity {

	private List<GizMessage> mymessageList = new ArrayList<GizMessage>();
	private myadapter myadapter;
	private String token;
	private String myid = "";
	private int myposition = -1;

	// 删除时需要用到的对话框
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_device_shared_message_list);

		setActionBar(true, true, R.string.devicesharedmess);

		initView();

		initData();

		initProgressBar();
	}

	private void initProgressBar() {

		progressDialog = new ProgressDialog(this);
		String loadingText = getString(R.string.loadingtext);
		progressDialog.setMessage(loadingText);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	// 初始化分享设备的数据
	private void initData() {
		//
		// GizDeviceSharing.setListener(new GizDeviceSharingListener() {
		//
		// @Override
		// public void didQueryMessageList(GizWifiErrorCode result,
		// List<GizMessage> messageList) {
		// super.didQueryMessageList(result, messageList);
		//
		// mymessageList = messageList;
		// myadapter.notifyDataSetChanged();
		// }
		//
		// });
		token = spf.getString("Token", "");
		GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSharing);
		// GizDeviceSharing.queryMessageList(token,
		// GizMessageType.GizMessageSystem);

	}

	private void initView() {

		SlideListView2 devicelist = (SlideListView2) findViewById(R.id.devicelist);

		devicelist.initSlideMode(SlideListView2.MOD_RIGHT);
		myadapter = new myadapter();
		devicelist.setAdapter(myadapter);

		devicelist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				View redpoint = arg1.findViewById(R.id.redpoint);

				if (redpoint.getVisibility() == 0) {
					redpoint.setVisibility(View.GONE);
					GizMessage gizMessage = mymessageList.get(arg2);

					GizDeviceSharing.markMessageStatus(token, gizMessage.getId(), GizMessageStatus.GizMessageRead);
				}

			}
		});

	}

	class myadapter extends BaseAdapter {

		@Override
		public int getCount() {

			return mymessageList.size();
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
		public View getView(final int arg0, View arg1, ViewGroup arg2) {

			View view = View.inflate(deviceSharedMessageActivity.this, R.layout.activity_device_item, null);

			TextView mess = (TextView) view.findViewById(R.id.mess);
			TextView timemess = (TextView) view.findViewById(R.id.timemess);

			View redpoint = view.findViewById(R.id.redpoint);

			RelativeLayout delete2 = (RelativeLayout) view.findViewById(R.id.delete2);

			delete2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View c) {
					GizMessage gizMessage = mymessageList.get(arg0);

					String id = gizMessage.getId();
					myid = id;
					myposition = arg0;
					GizDeviceSharing.markMessageStatus(token, gizMessage.getId(), GizMessageStatus.GizMessageDeleted);
					progressDialog.show();
				}
			});

			GizMessage gizMessage = mymessageList.get(arg0);

			mess.setText(gizMessage.getContent());

			timemess.setText(DateUtil.utc2Local(gizMessage.getUpdatedAt()));

			int ordinal = gizMessage.getStatus().ordinal();

			if (ordinal == 0) {
				redpoint.setVisibility(View.VISIBLE);
			} else {
				redpoint.setVisibility(View.GONE);
			}

			return view;
		}

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
		super.onResume();

		GizDeviceSharing.setListener(new GizDeviceSharingListener() {

			@Override
			public void didMarkMessageStatus(GizWifiErrorCode result, String messageID) {
				super.didMarkMessageStatus(result, messageID);

				if (result.ordinal() == 0 && myid.equals(messageID)) {

					if (mymessageList.size() > myposition && myposition != -1) {
						// mymessageList.remove(myposition);
						// myadapter.notifyDataSetChanged();
						GizDeviceSharing.queryMessageList(token, GizMessageType.GizMessageSharing);
					}
				}else{

					if (progressDialog!=null&&progressDialog.isShowing()) {
						progressDialog.cancel();
					}
				}
			}

			@Override
			public void didQueryMessageList(GizWifiErrorCode result, List<GizMessage> messageList) {
				super.didQueryMessageList(result, messageList);

				if (messageList != null) {
					Collections.sort(messageList, new Comparator<GizMessage>() {

						@Override
						public int compare(GizMessage arg0, GizMessage arg1) {

							String updatedAt = DateUtil.utc2Local(arg0.getUpdatedAt());
							String updatedAt2 = DateUtil.utc2Local(arg1.getUpdatedAt());

							int diff = (int) DateUtil.getDiff(updatedAt2, updatedAt);

							return diff;
						}

					});
				}

				
				if (progressDialog.isShowing()) {
					progressDialog.cancel();
				}
				mymessageList = messageList;
				myadapter.notifyDataSetChanged();
			}

		});
	}
}
