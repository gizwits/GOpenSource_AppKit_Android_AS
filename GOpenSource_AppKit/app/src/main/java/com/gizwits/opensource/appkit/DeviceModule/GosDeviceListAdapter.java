package com.gizwits.opensource.appkit.DeviceModule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiCentralControlDevice;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingUserRole;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.sharingdevice.addSharedActivity;
import com.gizwits.opensource.appkit.utils.AssetsUtils;

import java.util.List;
import java.util.Map;

import static com.gizwits.gizwifisdk.enumration.GizWifiDeviceType.GizDeviceCenterControl;


@SuppressLint("InflateParams")
public class GosDeviceListAdapter extends BaseAdapter {

    private static final String TAG = "GosDeviceListAdapter";
    Handler handler = new Handler();
    SharedPreferences spf;
    protected static final int UNBOUND = 99;
    protected static final int SHARE = 100;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    Context context;
    List<GizWifiDevice> deviceList;

    public GosDeviceListAdapter(Context context, List<GizWifiDevice> deviceList) {
        super();
        this.context = context;
        this.deviceList = deviceList;
    }

    public void setSpf(SharedPreferences spf) {
        this.spf = spf;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.item_gos_device_list, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        final GizWifiDevice device = deviceList.get(position);

        String deviceAlias = device.getAlias();
        String devicePN = device.getProductName();

        GizDeviceSharingUserRole role = device.getSharingRole();

        if (role != null) {
            if (role.name().equals("GizDeviceSharingSpecial") || role.name().equals("GizDeviceSharingOwner")) {
                holder.getDelete2().setVisibility(View.VISIBLE);
                holder.getDelete1().setVisibility(View.VISIBLE);
                holder.getDevice().setPadding(0, 0, AssetsUtils.diptopx(context, -181), 0);
            } else {
                holder.getDelete1().setVisibility(View.GONE);
                holder.getDelete2().setVisibility(View.VISIBLE);
                holder.getDevice().setPadding(0, 0, AssetsUtils.diptopx(context, -91), 0);
            }
        }
        GizWifiCentralControlDevice centralControlDevice = null;
        holder.getTvDeviceMac().setVisibility(View.GONE);
        holder.getTvDeviceMac().setVisibility(View.VISIBLE);
        holder.getTvDeviceMac().setText(device.getMacAddress());

        if (device.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOnline
                || device.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceControlled) {

            if (device.isLAN()) {
                holder.getLlLeft().setImageResource(R.drawable.common_device_lan_online);
            } else {
                holder.getLlLeft().setImageResource(R.drawable.common_device_remote_online);
            }
            holder.getImgRight().setVisibility(View.VISIBLE);

            if (device.isBind()) {// 已绑定设备
                if (device.getProductType() == GizDeviceCenterControl) {
                    if (device instanceof GizWifiCentralControlDevice) {
                        centralControlDevice = (GizWifiCentralControlDevice) device;
                    }
                }
                if (centralControlDevice != null) {
                    if (centralControlDevice.getSubDeviceList().size() != 0) {
                        holder.getTvDeviceMac().setVisibility(View.VISIBLE);
                        if (AssetsUtils.isZh(context)) {
                            StringBuffer sb = new StringBuffer();
                            sb.append("已连接");
                            sb.append(centralControlDevice.getSubDeviceList().size());
                            sb.append("个设备");
                            holder.getTvDeviceMac().setText(sb.toString());
                        } else {
                            StringBuffer sb = new StringBuffer();
                            sb.append(centralControlDevice.getSubDeviceList().size());
                            sb.append(" devices connected");
                            holder.getTvDeviceMac().setText(sb.toString());
                        }
                        if (AssetsUtils.isZh(context)) {
                            StringBuffer sb = new StringBuffer();
                            sb.append(centralControlDevice.getMacAddress());
                            sb.append(" 已连接");
                            sb.append(centralControlDevice.getSubDeviceList().size());
                            sb.append("个设备");
                            holder.getTvDeviceMac().setText(sb.toString());
                        } else {
                            StringBuffer sb = new StringBuffer();
                            sb.append(centralControlDevice.getMacAddress());
                            sb.append(" ");
                            sb.append(centralControlDevice.getSubDeviceList().size());
                            sb.append(" devices connected");
                            holder.getTvDeviceMac().setText(sb.toString());
                        }
                    }
                }
                //这里会出现箭头不显示的状态
//                holder.getImgRight().setVisibility(View.VISIBLE);

            } else {// 未绑定设备
                holder.getDelete2().setVisibility(View.GONE);
                holder.getDevice().setPadding(0, 0, AssetsUtils.diptopx(context, 0), 0);
            }
        } else {// 设备不在线
            holder.getSbDeviceStatus().setClickable(false);
            holder.getImgRight().setVisibility(View.GONE);
            if (device.isLAN()) {
                holder.getLlLeft().setImageResource(R.drawable.common_device_lan_offline);
            } else {
                holder.getLlLeft().setImageResource(R.drawable.common_device_remote_offline);
            }
        }
        if (TextUtils.isEmpty(deviceAlias)) {
            List<Map<String, Object>> list = GosDeploy.appConfig_DeviceInfo();
            boolean isName = true;
            for (Map<String, Object> map : list) {
                if (map.containsKey("productKey")) {
                    if (device.getProductKey().equals(map.get("productKey"))) {
                        if (AssetsUtils.isZh(context)) {
                            if (map.containsKey("productNameCH")) {
                                isName = false;
                                holder.getTvDeviceName().setText(map.get("productNameCH").toString());
                            }
                        } else {
                            if (map.containsKey("productNameEN")) {
                                isName = false;
                                holder.getTvDeviceName().setText(map.get("productNameEN").toString());
                            }
                        }
                    }
                }
            }
            if (isName) {
                holder.getTvDeviceName().setText(devicePN);
            }
        } else {
            holder.getTvDeviceName().setText(deviceAlias);
        }
        holder.getDelete2().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = UNBOUND;
                message.obj = device.getDid().toString();
                handler.sendMessage(message);

            }
        });
        holder.getDelete1().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = SHARE;
                message.obj = device.getDid().toString();
                handler.sendMessage(message);

                if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
                    Intent intent = new Intent(context, addSharedActivity.class);
                    intent.putExtra("productname", device.getProductName());
                    intent.putExtra("did", device.getDid());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, context.getString(R.string.please_login), 2000).show();
                }
            }
        });
        return view;
    }

}

class Holder {
    View view;

    public Holder(View view) {
        this.view = view;
    }

    private TextView tvDeviceMac, tvDeviceStatus, tvDeviceName;

    private RelativeLayout delete1, delete2, device;

    private ImageView imgRight;

    private ImageView lvLeft;

    private Switch sbDeviceStatus;

    public ImageView getLlLeft() {
        if (null == lvLeft) {
            lvLeft = (ImageView) view.findViewById(R.id.imgLeft);
        }
        return lvLeft;
    }

    public ImageView getImgRight() {
        if (null == imgRight) {
            imgRight = (ImageView) view.findViewById(R.id.imgRight);
        }
        return imgRight;
    }

    public RelativeLayout getDevice() {
        if (null == device) {
            device = (RelativeLayout) view.findViewById(R.id.rl_device);
        }
        return device;
    }

    public RelativeLayout getDelete2() {
        if (null == delete2) {
            delete2 = (RelativeLayout) view.findViewById(R.id.delete2);
        }
        return delete2;
    }

    public RelativeLayout getDelete1() {
        if (null == delete1) {
            delete1 = (RelativeLayout) view.findViewById(R.id.delete1);
        }
        return delete1;
    }

    public TextView getTvDeviceMac() {
        if (null == tvDeviceMac) {
            tvDeviceMac = (TextView) view.findViewById(R.id.tvDeviceMac);
        }
        return tvDeviceMac;
    }


    public TextView getTvDeviceName() {
        if (null == tvDeviceName) {
            tvDeviceName = (TextView) view.findViewById(R.id.tvDeviceName);
        }
        return tvDeviceName;
    }

    public Switch getSbDeviceStatus() {
        if (null == sbDeviceStatus) {
            sbDeviceStatus = (Switch) view.findViewById(R.id.sbDeviceStatus);
        }
        return sbDeviceStatus;
    }

}
