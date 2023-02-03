package com.gizwits.opensource.appkit.DeviceModule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.ConfigModule.GosAirlinkChooseDeviceWorkWiFiActivity;
import com.gizwits.opensource.appkit.ConfigModule.GosChooseDeviceWorkWiFiActivity;
import com.gizwits.opensource.appkit.ControlModule.GosDeviceControlActivity;
import com.gizwits.opensource.appkit.PushModule.GosPushManager;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.view.SlideListView2;
import com.gizwits.opensource.appkit.view.VerticalSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

// config-all-start
// config_airlink-false-start
// config_airlink-false-end
//config_softap-false-start
//config_softap-false-end
// config-all-end


@SuppressLint("HandlerLeak")
public class GosDeviceListFragment extends GosDeviceModuleBaseFragment implements OnRefreshListener {

    /**
     * The ll NoDevice
     */
    private ScrollView llNoDevice;

    SwipeRefreshLayout mSwipeLayout;

    /**
     * The img NoDevice
     */
    private ImageView imgNoDevice;

    /**
     * The btn NoDevice
     */
    private Button btnNoDevice;

    /**
     * The ic BoundDevices
     */
    private View icBoundDevices;

    /**
     * The ic FoundDevices
     */
    private View icFoundDevices;


    /**
     * The tv BoundDevicesListTitle
     */
    private TextView tvBoundDevicesListTitle;

    /**
     * The tv FoundDevicesListTitle
     */
    private TextView tvFoundDevicesListTitle;


    /**
     * The ll NoBoundDevices
     */
    private LinearLayout llNoBoundDevices;

    /**
     * The ll NoFoundDevices
     */
    private LinearLayout llNoFoundDevices;


    /**
     * The slv BoundDevices
     */
    private SlideListView2 slvBoundDevices;

    /**
     * The slv FoundDevices
     */
    private SlideListView2 slvFoundDevices;


    /**
     * The sv ListGroup
     */
    private ScrollView svListGroup;

    /**
     * 适配器
     */
    private GosDeviceListAdapter myadapter;

    private GosDeviceListAdapter myadapter1;

    /**
     * 设备列表分类
     */
    List<GizWifiDevice> boundDevicesList = new ArrayList<GizWifiDevice>();
    List<GizWifiDevice> foundDevicesList = new ArrayList<GizWifiDevice>();
    List<GizWifiDevice> offlineDevicesList = new ArrayList<GizWifiDevice>();

    /**
     * 设备热点名称列表
     */
    ArrayList<String> softNameList;

    /**
     * 与APP绑定的设备的ProductKey
     */
    private List<String> ProductKeyList;

    Intent intent;

    String softssid, uid, token;

    public static List<String> boundMessage;

    /**
     * 判断用户登录状态 0：未登录 1：实名用户登录 2：匿名用户登录 3：匿名用户登录中 4：匿名用户登录中断
     */
    public static int loginStatus;

    int threeSeconds = 3;

    /**
     * 获取设备列表
     */
    protected static final int GETLIST = 0;

    /**
     * 刷新设备列表
     */
    protected static final int UPDATALIST = 1;

    /**
     * 订阅成功前往控制页面
     */
    protected static final int TOCONTROL = 2;

    /**
     * 通知
     */
    protected static final int TOAST = 3;

    /**
     * 设备绑定
     */
    protected static final int BOUND = 9;

    /**
     * 设备解绑
     */
    protected static final int UNBOUND = 99;


    private static final int PULL_TO_REFRESH = 888;

    private VerticalSwipeRefreshLayout mSwipeLayout1;

    /**
     * 等待框
     */
    public ProgressDialog progressDialog;

    Handler handler = new Handler() {
        private AlertDialog myDialog;
        private TextView dialog_name;

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETLIST:
                    Log.e(TAG, "handleMessage:GETLIST -----------------" + uid);
                    if (!uid.isEmpty() && !token.isEmpty()) {
                        // GizWifiSDK.sharedInstance().getBoundDevices(uid, token, ProductKeyList);
                        GizWifiSDK.sharedInstance().getBoundDevices(uid, token);
                    }

                    //login_anonymous-false-start
                    if (loginStatus == 0 && GosDeploy.appConfig_Login_Anonymous()) {
                        loginStatus = 3;
                        GizWifiSDK.sharedInstance().userLoginAnonymous();
                    }
                    //login_anonymous-false-end
                    break;

                case UPDATALIST:
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.cancel();
                    }
                    UpdateUI();
                    break;

                case BOUND:

                    break;

                case UNBOUND:
                    if (progressDialog != null) {
                        progressDialog.show();
                    }
                    GizWifiSDK.sharedInstance().unbindDevice(uid, token, msg.obj.toString());
                    break;

                case TOCONTROL:
                    intent = null;
                    Bundle bundle = new Bundle();
                    GizWifiDevice device = (GizWifiDevice) msg.obj;
                    if (intent == null) {
                        intent = new Intent(getContext(), GosDeviceControlActivity.class);
                    }
                    bundle.putParcelable("GizWifiDevice", device);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                    break;

                case TOAST:
                    Toast.makeText(getContext(), msg.obj.toString(), 2000).show();
                    break;

                case PULL_TO_REFRESH:
                    handler.sendEmptyMessage(GETLIST);
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout1.setRefreshing(false);

                    break;
            }
        }
    };
    private View allView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        allView = inflater.inflate(R.layout.activity_gos_device_list, container, false);
        //handler.sendEmptyMessage(GETLIST);

        GosMessageHandler.getSingleInstance().StartLooperWifi(getContext());
        setProgressDialog();
        softNameList = new ArrayList<String>();
        initData();
        initView();
        initEvent();
        return allView;
    }

    /*
     * @Override public void onWindowFocusChanged(boolean hasFocus) {
     * super.onWindowFocusChanged(hasFocus); if (hasFocus && isFrist) {
     * progressDialog.show();
     *
     * isFrist = false; } }
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: -------------");
        handler.sendEmptyMessage(GETLIST);
        GosDeviceModuleBaseFragment.deviceslist = GizWifiSDK.sharedInstance().getDeviceList();
        UpdateUI();
        // TODO GosMessageHandler.getSingleInstance().SetHandler(handler);
        if (boundMessage.size() != 0) {
            progressDialog.show();
            if (boundMessage.size() == 2) {
                GizWifiSDK.sharedInstance().bindDevice(uid, token, boundMessage.get(0), boundMessage.get(1), null);
            } else if (boundMessage.size() == 1) {
                GizWifiSDK.sharedInstance().bindDeviceByQRCode(uid, token, boundMessage.get(0), false);
            } else if (boundMessage.size() == 3) {
                GizDeviceSharing.checkDeviceSharingInfoByQRCode(spf.getString("Token", ""), boundMessage.get(2));
            } else {
                Log.i("Apptest", "ListSize:" + boundMessage.size());
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        boundMessage.clear();
        // TODO GosMessageHandler.getSingleInstance().SetHandler(null);

    }

    private void initView() {
        svListGroup = (ScrollView) allView.findViewById(R.id.svListGroup);
        llNoDevice = (ScrollView) allView.findViewById(R.id.llNoDevice);
        imgNoDevice = (ImageView) allView.findViewById(R.id.imgNoDevice);
        btnNoDevice = (Button) allView.findViewById(R.id.btnNoDevice);

        icBoundDevices = allView.findViewById(R.id.icBoundDevices);
        icFoundDevices = allView.findViewById(R.id.icFoundDevices);

        slvBoundDevices = (SlideListView2) icBoundDevices.findViewById(R.id.slideListView1);
        slvFoundDevices = (SlideListView2) icFoundDevices.findViewById(R.id.slideListView1);

        llNoBoundDevices = (LinearLayout) icBoundDevices.findViewById(R.id.llHaveNotDevice);
        llNoFoundDevices = (LinearLayout) icFoundDevices.findViewById(R.id.llHaveNotDevice);

        tvBoundDevicesListTitle = (TextView) icBoundDevices.findViewById(R.id.tvListViewTitle);
        tvFoundDevicesListTitle = (TextView) icFoundDevices.findViewById(R.id.tvListViewTitle);
        String boundDevicesListTitle = null;
        String foundDevicesListTitle = null;
        boundDevicesListTitle = (String) getText(R.string.my_device);
        foundDevicesListTitle = (String) getText(R.string.found_devices);

        tvBoundDevicesListTitle.setText(boundDevicesListTitle);

        tvFoundDevicesListTitle.setText(foundDevicesListTitle);

        // 下拉刷新

        mSwipeLayout = (VerticalSwipeRefreshLayout) allView.findViewById(R.id.id_swipe_ly);

        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mSwipeLayout1 = (VerticalSwipeRefreshLayout) allView.findViewById(R.id.id_swipe_ly1);
        mSwipeLayout1.setOnRefreshListener(this);
        mSwipeLayout1.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        setActionBar(true, true, "");
//        actionBar.setIcon(getResources().getDrawable(R.drawable.qr_code));
    }

    private void initEvent() {
        myadapter = new GosDeviceListAdapter(getContext(), foundDevicesList);
        myadapter.setHandler(handler);
        myadapter.setSpf(spf);
        slvFoundDevices.setAdapter(myadapter);
        myadapter1 = new GosDeviceListAdapter(getContext(), boundDevicesList);
        myadapter1.setHandler(handler);
        myadapter1.setSpf(spf);
        slvBoundDevices.setAdapter(myadapter1);
        // config-all-start
        imgNoDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice();
            }
        });
        btnNoDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addDevice();
            }
        });
        // config-all-end
        slvFoundDevices.initSlideMode(SlideListView2.MOD_FORBID);
        slvFoundDevices.setFocusable(false);
        slvBoundDevices.initSlideMode(SlideListView2.MOD_RIGHT);
        slvBoundDevices.setFocusable(false);
        slvFoundDevices.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                slvFoundDevices.setEnabled(false);
                slvFoundDevices.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slvFoundDevices.setEnabled(true);
                    }
                }, 1000);
                final GizWifiDevice device = foundDevicesList.get(position);
                device.setListener(getGizWifiDeviceListener(device));
                if (device.getNetStatus() != GizWifiDeviceNetStatus.GizDeviceOffline) {
                    boolean isAuto = false;

                    List<Map<String, String>> list2 = GosDeploy.appConfig_ProductList();
                    for (Map<String, String> map2 : list2) {
                        String productkey = device.getProductKey();
                        Iterator it1 = map2.entrySet().iterator();
                        while (it1.hasNext()) {
                            Map.Entry entry1 = (Map.Entry) it1.next();
                            if (productkey.equals(entry1.getKey())) {
                                isAuto = true;
                                device.setSubscribe(entry1.getValue().toString(), true);
                                progressDialog.show();
                                break;
                            }
                        }
                    }

                    if (device.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOnline
                            && !TextUtils.isEmpty(device.getDid()) && !device.isBind()
                            && device.getProductType() == GizWifiDeviceType.GizDeviceSub) {

                        if (!isAuto) {
                            final Dialog dialog = new AlertDialog.Builder(getContext(), R.style.alert_dialog_style)
                                    .setView(new EditText(getContext())).create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();

                            Window window = dialog.getWindow();
                            window.setContentView(R.layout.alert_gos_edit_name);
                            WindowManager.LayoutParams layoutParams = window.getAttributes();
                            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                            window.setAttributes(layoutParams);
                            TextView title = (TextView) window.findViewById(R.id.tvTitle);
                            final EditText remarkname = (EditText) window.findViewById(R.id.remarkname);
                            title.setText(getString(R.string.pleaseenterps));
                            LinearLayout llyes = (LinearLayout) window.findViewById(R.id.llSure);
                            LinearLayout llno = (LinearLayout) window.findViewById(R.id.llNo);
                            remarkname.setHint(getResources().getString(R.string.pleaseenter32ps));
                            llno.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    dialog.dismiss();
                                }
                            });
                            llyes.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    String ps = remarkname.getText().toString();
                                    if (TextUtils.isEmpty(ps) || ps.length() != 32) {
                                        Toast.makeText(getContext(),
                                                getResources().getString(R.string.psiserror), 0).show();
                                    } else {
                                        device.setSubscribe(remarkname.getText().toString(), true);
                                        //device.setSubscribe(true);
                                        progressDialog.show();
                                        /**隐藏软键盘**/
                                        View view = getActivity().getWindow().peekDecorView();
                                        if (view != null) {
                                            InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                        }
                                        dialog.dismiss();
                                    }
                                }
                            });
                        }
                    } else {
//                           device.setSubscribe(null, true);
                        if (!isAuto) {
                            device.setSubscribe(null, true);
                            progressDialog.show();
                        }
                    }
                }

            }
        });

        slvBoundDevices.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if (position < boundDevicesList.size()) {

                    slvBoundDevices.setEnabled(false);
                    slvBoundDevices.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            slvBoundDevices.setEnabled(true);
                        }
                    }, 1000);

                    final GizWifiDevice device = boundDevicesList.get(position);
                    device.setListener(getGizWifiDeviceListener(device));
                    if (device.getNetStatus() != GizWifiDeviceNetStatus.GizDeviceOffline) {
                        String productKey = device.getProductKey();
                        List<Map<String, String>> list2 = GosDeploy.appConfig_ProductList();
                        boolean isSubscribe = false;
                        for (Map<String, String> map2 : list2) {
                            Iterator it1 = map2.entrySet().iterator();
                            while (it1.hasNext()) {
                                Map.Entry entry1 = (Map.Entry) it1.next();
                                if (productKey.equals(entry1.getKey())) {
                                    isSubscribe = true;
                                    device.setSubscribe(entry1.getValue().toString(), true);
                                }
                            }
                        }
                        if (!isSubscribe) {
                            device.setSubscribe(null, true);
                            progressDialog.show();
                        }
                    }
                }

            }
        });
    }


    private void initData() {
        boundMessage = new ArrayList<String>();
        // ProductKeyList = GosDeploy.setProductKeyList();
        ProductKeyList = null;
        uid = spf.getString("Uid", "");
        token = spf.getString("Token", "");
        if (uid.isEmpty() && token.isEmpty()) {
            loginStatus = 0;
        }
    }

    protected void didDiscovered(GizWifiErrorCode result, List<GizWifiDevice> deviceList) {
        Log.e(TAG, "didDiscovered: 更新数据---------------");
        GosDeviceModuleBaseFragment.deviceslist.clear();
        for (GizWifiDevice gizWifiDevice : deviceList) {
            GosDeviceModuleBaseFragment.deviceslist.add(gizWifiDevice);
        }
        handler.sendEmptyMessage(UPDATALIST);
    }

    protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {
        Log.e(TAG, "didUserLogin: -----------");
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
            loginStatus = 2;
            this.uid = uid;
            this.token = token;
            spf.edit().putString("Uid", this.uid).commit();
            spf.edit().putString("Token", this.token).commit();
            handler.sendEmptyMessage(GETLIST);
            // TODO 绑定推送
            //GosPushManager.pushBindService(token);
            if (GosDeploy.appConfig_Push_BaiDu()) {
                GosPushManager.pushBindService(uid, token);
            }
            if (GosDeploy.appConfig_Push_JiGuang()) {
                GosPushManager.pushBindService(uid, token);
            }
        } else {
            loginStatus = 0;
            if (GosDeploy.appConfig_Login_Anonymous()) {
                tryUserLoginAnonymous();
            }
        }
    }

    protected void didUnbindDevice(GizWifiErrorCode result, String did) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            // String unBoundFailed = (String) getText(R.string.unbound_failed);
            Toast.makeText(getContext(), toastError(result), 2000).show();
        }
    }

    @Override
    protected void didSetSubscribe(GizWifiErrorCode result, GizWifiDevice device, boolean isSubscribed) {
        // TODO 控制页面跳转
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        Message msg = new Message();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
            msg.what = TOCONTROL;
            msg.obj = device;
        } else {
            if (device.isBind()) {
                msg.what = TOAST;
                // String setSubscribeFail = (String)
                // getText(R.string.setsubscribe_failed);
                msg.obj = toastError(result);// setSubscribeFail + "\n" + arg0;
            }
        }
        handler.sendMessage(msg);
    }

    /**
     * 推送绑定回调
     *
     * @param result
     */
    @Override
    protected void didChannelIDBind(GizWifiErrorCode result) {
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
            Toast.makeText(getContext(), toastError(result), 2000).show();
        }
    }

    /**
     * 设备绑定回调(旧)
     *
     * @param error
     * @param errorMessage
     * @param did
     */
    protected void didBindDevice(int error, String errorMessage, String did) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        if (error != 0) {

            String toast = getResources().getString(R.string.bound_failed) + "\n" + errorMessage;
            Toast.makeText(getContext(), toast, 2000).show();
            // Toast.makeText(this, R.string.bound_failed + "\n" + errorMessage,
            // 2000).show();
        } else {

            Toast.makeText(getContext(), R.string.bound_successful, 2000).show();
        }

    }

    private static final String TAG = "GosDeviceListFragment";

    /**
     * 设备绑定回调
     *
     * @param result
     * @param did
     */
    protected void didBindDevice(GizWifiErrorCode result, String did) {
        if (progressDialog != null) {
            progressDialog.cancel();

        }
        if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS) {
            Toast.makeText(getContext(), toastError(result), 2000).show();
        } else {

            Toast.makeText(getContext(), R.string.add_successful, 2000).show();
        }
    }


    private void UpdateUI() {

        if (GosDeviceModuleBaseFragment.deviceslist.isEmpty()) {
            svListGroup.setVisibility(View.GONE);
            llNoDevice.setVisibility(View.VISIBLE);
            mSwipeLayout1.setVisibility(View.VISIBLE);
            return;
        } else {
            llNoDevice.setVisibility(View.GONE);
            mSwipeLayout1.setVisibility(View.GONE);
            svListGroup.setVisibility(View.VISIBLE);
        }

        if (boundDevicesList == null) {
            boundDevicesList = new ArrayList<GizWifiDevice>();
        } else {
            boundDevicesList.clear();
        }
        if (foundDevicesList == null) {
            foundDevicesList = new ArrayList<GizWifiDevice>();
        } else {
            foundDevicesList.clear();
        }
        if (offlineDevicesList == null) {
            offlineDevicesList = new ArrayList<GizWifiDevice>();
        } else {
            offlineDevicesList.clear();
        }

        for (GizWifiDevice gizWifiDevice : GosDeviceModuleBaseFragment.deviceslist) {
            if (gizWifiDevice.isBind()) {
                boundDevicesList.add(gizWifiDevice);
            } else {
                foundDevicesList.add(gizWifiDevice);
            }
        }
        if (foundDevicesList.isEmpty()) {
            slvFoundDevices.setVisibility(View.GONE);
            llNoFoundDevices.setVisibility(View.VISIBLE);
        } else {
            if (myadapter == null) {
                myadapter = new GosDeviceListAdapter(getContext(), foundDevicesList);
                myadapter.setHandler(handler);
                myadapter.setSpf(spf);
                slvFoundDevices.setAdapter(myadapter);
            } else {
                myadapter.notifyDataSetChanged();
            }
            llNoFoundDevices.setVisibility(View.GONE);
            slvFoundDevices.setVisibility(View.VISIBLE);
        }
        if (boundDevicesList.isEmpty()) {
            slvBoundDevices.setVisibility(View.GONE);
            llNoBoundDevices.setVisibility(View.VISIBLE);
        } else {
            if (myadapter1 == null) {
                myadapter1 = new GosDeviceListAdapter(getContext(), boundDevicesList);
                myadapter1.setHandler(handler);
                myadapter1.setSpf(spf);
                slvBoundDevices.setAdapter(myadapter1);
            } else {
                if (slvBoundDevices.isSlided()) {
                    slvBoundDevices.slideBack();
                }
                myadapter1.notifyDataSetChanged();
            }
            llNoBoundDevices.setVisibility(View.GONE);
            slvBoundDevices.setVisibility(View.VISIBLE);
        }
    }

    private void addDevice() {
        if (GosDeploy.appConfig_Config_Softap()) {
            if (!checkNetwork(getContext())) {
                Toast.makeText(getContext(), R.string.network_error, 2000).show();
                return;
            }
            if (GosDeploy.appConfig_Config_Airlink()) {
                final Dialog dialog = new AlertDialog.Builder(getContext(), R.style.alert_dialog_style)
                        .setView(new EditText(getContext())).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.alert_gos_overflow);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                window.setAttributes(layoutParams);
                LinearLayout llAirlink;
                LinearLayout llSoftap;
                llAirlink = (LinearLayout) window.findViewById(R.id.llAirlink);
                llSoftap = (LinearLayout) window.findViewById(R.id.llSoftap);
                llAirlink.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getContext(), GosAirlinkChooseDeviceWorkWiFiActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                llSoftap.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        intent = new Intent(getContext(), GosChooseDeviceWorkWiFiActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            } else {
                Intent intent = new Intent(getContext(), GosChooseDeviceWorkWiFiActivity.class);
                startActivity(intent);
            }
        } else {
            if (GosDeploy.appConfig_Config_Airlink()) {
                if (!checkNetwork(getContext())) {
                    Toast.makeText(getContext(), R.string.network_error, 2000).show();
                    return;
                }
                Intent intent = new Intent(getContext(), GosAirlinkChooseDeviceWorkWiFiActivity.class);
                startActivity(intent);
            }
        }
    }

    private void tryUserLoginAnonymous() {
        threeSeconds = 3;
        final Timer tsTimer = new Timer();
        tsTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                threeSeconds--;
                if (threeSeconds <= 0) {
                    tsTimer.cancel();
                    handler.sendEmptyMessage(GETLIST);
                } else {
                    if (loginStatus == 4) {
                        tsTimer.cancel();
                    }
                }
            }
        }, 1000, 1000);
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(PULL_TO_REFRESH, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog = null;
    }

    /**
     * 设置ProgressDialog
     */
    public void setProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        String loadingText = getString(R.string.loadingtext);
        progressDialog.setMessage(loadingText);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 检查网络连通性（工具方法）
     *
     * @param context
     * @return
     */
    public boolean checkNetwork(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null && net.isConnected()) {
            return true;
        }
        return false;
    }
}
