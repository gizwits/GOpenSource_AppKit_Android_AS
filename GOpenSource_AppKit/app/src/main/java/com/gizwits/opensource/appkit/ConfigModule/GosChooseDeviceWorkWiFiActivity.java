package com.gizwits.opensource.appkit.ConfigModule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.opensource.appkit.CommonModule.GosConstant;
import com.gizwits.opensource.appkit.CommonModule.GosDeploy;
import com.gizwits.opensource.appkit.DeviceModule.GosMainActivity;
import com.gizwits.opensource.appkit.R;
import com.gizwits.opensource.appkit.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GosChooseDeviceWorkWiFiActivity extends GosConfigModuleBaseActivity
        implements OnClickListener, OnItemClickListener {

    private AlertDialog create;
    private ArrayList<ScanResult> wifiList;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"};

    /**
     * wifi信息
     */
    public WifiInfo wifiInfo;

    /**
     * The et SSID
     */
    private EditText etSSID;

    /**
     * The et Psw
     */
    private EditText etPsw;

    /**
     * The btn Next
     */
    private Button btnNext;

    /**
     * The cb Laws
     */
    private CheckBox cbLaws;

    /**
     * The img WiFiList
     */
    private RelativeLayout rlWiFiList;

    /**
     * 配置用参数
     */
    private String softSSID, workSSID, workSSIDPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gos_choose_device_workwifi);
        // 设置ActionBar
        setToolBar(true, R.string.choose_wifi);

        initData();
        initView();
        ininEvent();
    }

    private void initView() {
        etSSID = (EditText) findViewById(R.id.etSSID);
        etPsw = (EditText) findViewById(R.id.etPsw);
        btnNext = (Button) findViewById(R.id.btnNext);
        cbLaws = (CheckBox) findViewById(R.id.cbLaws);
        rlWiFiList = (RelativeLayout) findViewById(R.id.rlWiFiList);
        // 配置文件部署
        btnNext.setBackgroundDrawable(GosDeploy.appConfig_BackgroundColor());
        btnNext.setTextColor(GosDeploy.appConfig_Contrast());
        if (!TextUtils.isEmpty(workSSID)) {
            etSSID.setText(workSSID);
            etSSID.setSelection(workSSID.length());
            if (checkworkSSIDUsed(workSSID)) {
                if (!TextUtils.isEmpty(spf.getString("workSSIDPsw", ""))) {
                    etPsw.setText(spf.getString("workSSIDPsw", ""));
                }
            }
        }
    }


    private void ininEvent() {

        btnNext.setOnClickListener(this);
        rlWiFiList.setOnClickListener(this);

        cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String psw = etPsw.getText().toString();

                if (isChecked) {
                    etPsw.setInputType(0x90);
                } else {
                    etPsw.setInputType(0x81);
                }
                etPsw.setSelection(psw.length());
            }
        });

        //检测是否有位置定位的权限
        int permission = ActivityCompat.checkSelfPermission(GosChooseDeviceWorkWiFiActivity.this,
                "android.permission.ACCESS_FINE_LOCATION");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            try {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(GosChooseDeviceWorkWiFiActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initData() {
        //softSSID = getIntent().getStringExtra("softssid");
        workSSID = spf.getString("workSSID", "");
    }


    @Override
    protected void onResume() {
        super.onResume();

        try {
            // 预设workSSID && workSSIDPsw
            workSSID = NetUtils.getCurentWifiSSID(this);
            String mypass = spf.getString("mypass", "");

            if (!TextUtils.isEmpty(workSSID)) {
                etSSID.setText(workSSID);
                etSSID.setSelection(workSSID.length());
                if (!TextUtils.isEmpty(mypass)) {
                    JSONObject obj = new JSONObject(mypass);

                    if (obj.has(workSSID)) {
                        String pass = obj.getString(workSSID);
                        etPsw.setText(pass);
                    } else {
                        etPsw.setText("");
                    }
                }

            } else {
                etSSID.setText(NetUtils.getCurentWifiSSID(this));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, GosMainActivity.class);
                startActivity(intent);
                //quitAlert(this, intent);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                workSSID = etSSID.getText().toString();
                workSSIDPsw = etPsw.getText().toString();

                if (TextUtils.isEmpty(workSSID)) {
                    Toast.makeText(GosChooseDeviceWorkWiFiActivity.this, R.string.choose_wifi_list_title,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(workSSIDPsw)) {
                    final Dialog dialog = new AlertDialog.Builder(this, R.style.alert_dialog_style)
                            .setView(new EditText(this)).create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.alert_gos_empty);
                    WindowManager.LayoutParams layoutParams = window.getAttributes();
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes(layoutParams);
                    LinearLayout llNo, llSure;
                    llNo = (LinearLayout) window.findViewById(R.id.llNo);
                    llSure = (LinearLayout) window.findViewById(R.id.llSure);

                    llNo.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.cancel();

                        }
                    });

                    llSure.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (dialog.isShowing()) {
                                dialog.cancel();
                            }
                            Intent intent = null;
                            //wifiModuleType-false-start  wifiModuleType-true-start
                            if (GosDeploy.appConfig_WifiModuleType().size() == 0) {
                                //wifiModuleType-false-end
                                intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
                                        GosModeListActivity2.class);
                                //wifiModuleType-false-start
                            } else {
                                //wifiModuleType-true-end
                                List<Integer> moduleTypes = GosDeploy.appConfig_WifiModuleType();
                                JSONArray array = new JSONArray();
                                for (int type : moduleTypes) {
                                    if (type == 4) {
                                        type = 0;
                                    } else if (type < 4) {
                                        type = type + 1;
                                    } else if (type == 12) {
                                        type = 6;
                                    } else if (type >= 6 && type < 12) {
                                        type = type + 1;
                                    }
                                    array.put(type);
                                }
                                spf.edit().putString("modulestyles", array.toString()).commit();
                                intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
                                        GosDeviceReadyActivity.class);
                                //wifiModuleType-true-start
                            }
                            //wifiModuleType-false-end  wifiModuleType-true-end
                            spf.edit().putString("workSSID", workSSID).commit();
                            spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();
                            intent.putExtra("isAirlink", false);
                            startActivity(intent);
                        }
                    });
                } else {
                    Intent intent = null;
                    //wifiModuleType-false-start  wifiModuleType-true-start
                    if (GosDeploy.appConfig_WifiModuleType().size() == 0) {
                        //wifiModuleType-false-end
                        intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
                                GosModeListActivity2.class);
                        //wifiModuleType-false-start
                    } else {
                        //wifiModuleType-true-end
                        List<Integer> moduleTypes = GosDeploy.appConfig_WifiModuleType();
                        JSONArray array = new JSONArray();
                        for (int type : moduleTypes) {
                            if (type == 4) {
                                type = 0;
                            } else if (type < 4) {
                                type = type + 1;
                            } else if (type == 12) {
                                type = 6;
                            } else if (type >= 6 && type < 12) {
                                type = type + 1;
                            }
                            array.put(type);
                        }
                        spf.edit().putString("modulestyles", array.toString()).commit();
                        intent = new Intent(GosChooseDeviceWorkWiFiActivity.this,
                                GosDeviceReadyActivity.class);
                        //wifiModuleType-true-start
                    }
                    //wifiModuleType-false-end  wifiModuleType-true-end
                    spf.edit().putString("workSSID", workSSID).commit();
                    spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();
                    /**   判断是否是从一键配置界面传过去的  */
                    intent.putExtra("isAirlink", false);
                    startActivity(intent);
                }
                break;

            case R.id.rlWiFiList:
                AlertDialog.Builder dia = new AlertDialog.Builder(GosChooseDeviceWorkWiFiActivity.this);
                View view = View.inflate(GosChooseDeviceWorkWiFiActivity.this, R.layout.alert_gos_wifi_list, null);
                ListView listview = (ListView) view.findViewById(R.id.wifi_list);
                List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
                List<String> localList = new ArrayList<String>();
                localList.clear();
                wifiList = new ArrayList<ScanResult>();
                wifiList.clear();

                for (ScanResult sss : rsList) {
                    if (sss.SSID.contains(GosConstant.SoftAP_Start)) {

                    } else {
                        if (localList.toString().contains(sss.SSID)) {
                        } else {
                            localList.add(sss.SSID);
                            wifiList.add(sss);
                        }
                    }
                }
                if (wifiList.size() == 0) {
                    LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
                        Toast.makeText(this, getString(R.string.open), Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                WifiListAdapter adapter = new WifiListAdapter(wifiList);
                listview.setAdapter(adapter);
                listview.setOnItemClickListener(this);
                dia.setView(view);
                create = dia.create();
                create.show();

                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        ScanResult sResult = wifiList.get(arg2);
        String sSID = sResult.SSID;
        etSSID.setText(sSID);
        etSSID.setSelection(sSID.length());
        etPsw.setText("");
        create.dismiss();
    }

    // 检查当前使用的WiFi是否曾经用过
    protected boolean checkworkSSIDUsed(String workSSID) {
        if (spf.contains("workSSID")) {
            if (spf.getString("workSSID", "").equals(workSSID)) {
                return true;
            }
        }
        return false;
    }

    // 屏蔽掉返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, GosMainActivity.class);
            startActivity(intent);
            //quitAlert(this, intent);
            return true;
        }
        return false;
    }

    class WifiListAdapter extends BaseAdapter {

        ArrayList<ScanResult> xpgList;

        public WifiListAdapter(ArrayList<ScanResult> list) {
            this.xpgList = list;
        }

        @Override
        public int getCount() {
            return xpgList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            Holder holder;
            if (view == null) {
                view = LayoutInflater.from(GosChooseDeviceWorkWiFiActivity.this).inflate(R.layout.item_gos_wifi_list,
                        null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            String ssid = xpgList.get(position).SSID;
            holder.getTextView().setText(ssid);

            return view;
        }

    }

    class Holder {
        View view;

        public Holder(View view) {
            this.view = view;
        }

        TextView textView;

        public TextView getTextView() {
            if (textView == null) {
                textView = (TextView) view.findViewById(R.id.SSID_text);
            }
            return textView;
        }
    }
}
